package com.example.data.remote

import android.content.Context
import com.example.BuildConfig
import com.example.data.model.AiErrorCode
import com.example.data.model.AiResponseMode
import com.example.data.model.Citation
import com.example.data.model.EdenAnswer
import com.example.data.model.KnowledgeEntity
import com.example.data.repository.EdenRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit

class EdenAiService(private val repository: EdenRepository) {

    private val httpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    private val jsonMediaType = "application/json; charset=utf-8".toMediaType()

    suspend fun answerQuestion(
        query: String,
        forceOffline: Boolean = false
    ): EdenAnswer = withContext(Dispatchers.IO) {
        val trimmedQuery = query.trim()
        val matchingNodes = repository.findMatchingKnowledgeSync(trimmedQuery)

        // If user forced offline mode, use offline knowledge graph directly
        if (forceOffline) {
            return@withContext buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_KNOWLEDGE_GRAPH,
                errorCode = AiErrorCode.NONE,
                debugMsg = "Offline mode explicitly enabled by user"
            )
        }

        // 1. Primary: Query the official live EDEN Render platform backend
        val renderBackendAnswer = queryEdenRenderBackend(trimmedQuery, matchingNodes)
        if (renderBackendAnswer != null) {
            return@withContext renderBackendAnswer
        }

        // 2. Secondary: If Render backend unreachable, try Open-Source AI engine (Free public access)
        val openSourceAnswer = queryOpenSourceAi(trimmedQuery, matchingNodes)
        if (openSourceAnswer != null) {
            return@withContext openSourceAnswer
        }

        // 3. Check Gemini API Key if user provided one in secrets
        val rawApiKey = try { BuildConfig.GEMINI_API_KEY } catch (_: Exception) { "" }
        val apiKey = rawApiKey.trim()

        if (apiKey.isEmpty() || apiKey == "MY_GEMINI_API_KEY" || apiKey == "null") {
            return@withContext buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_KNOWLEDGE_GRAPH,
                errorCode = AiErrorCode.API_KEY_NOT_SET,
                debugMsg = "Render and Open-Source AI endpoints unreachable. Retrieved deterministic knowledge from verified EDEN repository."
            )
        }

        // 3. Grounded Gemini Live AI (if key is provided)
        try {
            val groundedContext = buildGroundedPromptContext(matchingNodes)
            val systemPrompt = """
                You are EDEN (Explore • Discover • Educate • Nurture), an Environmental Intelligence and Learning Platform.
                Provide rigorous, scientifically accurate explanations adhering to international standards (WHO, EPA, IPCC, ISO).
                Scientific Rules:
                1. Never invent environmental data.
                2. Never invent citations.
                3. Show explicit units and standard references.
                4. Distinguish empirical facts from theoretical estimates.
                5. Reference the following verified EDEN Knowledge Graph context when addressing the question:
                
                $groundedContext
            """.trimIndent()

            val requestJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", trimmedQuery)
                            })
                        })
                    })
                })
                put("systemInstruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply {
                            put("text", systemPrompt)
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("temperature", 0.2)
                    put("topP", 0.8)
                })
            }

            val requestBody = requestJson.toString().toRequestBody(jsonMediaType)
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val httpRequest = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = httpClient.newCall(httpRequest).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                val errorCode = when (response.code) {
                    429 -> AiErrorCode.QUOTA_EXCEEDED
                    503, 502 -> AiErrorCode.SERVICE_UNAVAILABLE
                    else -> AiErrorCode.SERVICE_UNAVAILABLE
                }
                val debug = "HTTP ${response.code}: $responseBody"
                return@withContext buildKnowledgeGraphAnswer(
                    query = trimmedQuery,
                    nodes = matchingNodes,
                    mode = AiResponseMode.OFFLINE_FALLBACK,
                    errorCode = errorCode,
                    debugMsg = debug
                )
            }

            // Parse response
            val parsedJson = JSONObject(responseBody)
            val candidates = parsedJson.optJSONArray("candidates")
            val firstCandidate = candidates?.optJSONObject(0)
            val content = firstCandidate?.optJSONObject("content")
            val parts = content?.optJSONArray("parts")
            val text = parts?.optJSONObject(0)?.optString("text")

            if (!text.isNullOrBlank()) {
                val citations = matchingNodes.map {
                    Citation(
                        source = it.authoritativeSource,
                        authority = it.authority,
                        year = it.sourceYear,
                        evidenceTier = it.evidenceTier,
                        keyEvidence = it.formulaOrStandard
                    )
                }

                val actions = mutableListOf(
                    "Explore ${matchingNodes.firstOrNull()?.category ?: "Environmental Concepts"}",
                    "Perform deterministic calculation",
                    "Take level assessment quiz"
                )

                EdenAnswer(
                    query = trimmedQuery,
                    text = text,
                    mode = if (matchingNodes.isNotEmpty()) AiResponseMode.LIVE_AI_GROUNDED else AiResponseMode.LIVE_AI,
                    errorCode = AiErrorCode.NONE,
                    debugDetails = "Gemini 3.5 Flash Live Grounded Inference (Latency ~ Normal)",
                    retrievedNodes = matchingNodes,
                    citations = citations,
                    suggestedActions = actions
                )
            } else {
                buildKnowledgeGraphAnswer(
                    query = trimmedQuery,
                    nodes = matchingNodes,
                    mode = AiResponseMode.OFFLINE_FALLBACK,
                    errorCode = AiErrorCode.SERVICE_UNAVAILABLE,
                    debugMsg = "Empty response candidate from upstream model"
                )
            }
        } catch (e: SocketTimeoutException) {
            buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_FALLBACK,
                errorCode = AiErrorCode.NETWORK_TIMEOUT,
                debugMsg = e.localizedMessage ?: "Socket timeout > 60s"
            )
        } catch (e: UnknownHostException) {
            buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_FALLBACK,
                errorCode = AiErrorCode.NETWORK_OFFLINE,
                debugMsg = "DNS resolution failed or internet offline: ${e.message}"
            )
        } catch (e: IOException) {
            buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_FALLBACK,
                errorCode = AiErrorCode.NETWORK_OFFLINE,
                debugMsg = "I/O error: ${e.message}"
            )
        } catch (e: Exception) {
            buildKnowledgeGraphAnswer(
                query = trimmedQuery,
                nodes = matchingNodes,
                mode = AiResponseMode.OFFLINE_FALLBACK,
                errorCode = AiErrorCode.SERVICE_UNAVAILABLE,
                debugMsg = "Unexpected failure: ${e.message}"
            )
        }
    }

    private fun queryEdenRenderBackend(
        query: String,
        matchingNodes: List<KnowledgeEntity>
    ): EdenAnswer? {
        return try {
            val jsonPayload = JSONObject().apply {
                put("question", query)
            }
            val requestBody = jsonPayload.toString().toRequestBody(jsonMediaType)
            val request = Request.Builder()
                .url("https://eden-environmental-intelligence.onrender.com/api/v1/ask")
                .post(requestBody)
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return null
            val body = response.body?.string() ?: return null
            val json = JSONObject(body)
            val answerText = json.optString("answer", "")
            if (answerText.isBlank()) return null

            val modeStr = json.optString("mode", "")
            val mode = if (modeStr.contains("live", ignoreCase = true)) {
                AiResponseMode.LIVE_AI_GROUNDED
            } else {
                AiResponseMode.OFFLINE_KNOWLEDGE_GRAPH
            }

            val citations = matchingNodes.map {
                Citation(
                    source = it.authoritativeSource,
                    authority = it.authority,
                    year = it.sourceYear,
                    evidenceTier = it.evidenceTier,
                    keyEvidence = it.formulaOrStandard
                )
            }

            val actions = mutableListOf<String>()
            val suggestedActionsObj = json.optJSONObject("suggestedActions")
            if (suggestedActionsObj != null) {
                suggestedActionsObj.optJSONObject("learn")?.let {
                    val title = it.optString("conceptTitle", "")
                    if (title.isNotBlank()) actions.add("Learn: $title")
                }
                suggestedActionsObj.optJSONObject("simulate")?.let {
                    val title = it.optString("title", "")
                    if (title.isNotBlank()) actions.add("Simulate: $title")
                }
                suggestedActionsObj.optJSONObject("act")?.let {
                    val title = it.optString("title", "")
                    if (title.isNotBlank()) actions.add("Action: $title")
                }
            }
            if (actions.isEmpty()) {
                actions.addAll(listOf("Explore Knowledge Graph", "Deterministic Calculators", "Field Sampling"))
            }

            EdenAnswer(
                query = query,
                text = answerText,
                mode = mode,
                errorCode = AiErrorCode.NONE,
                debugDetails = "Served from EDEN Render Backend Engine (https://eden-environmental-intelligence.onrender.com/api/v1/ask)",
                retrievedNodes = matchingNodes,
                citations = citations,
                suggestedActions = actions
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun queryOpenSourceAi(
        query: String,
        matchingNodes: List<KnowledgeEntity>
    ): EdenAnswer? {
        return try {
            val groundedContext = buildGroundedPromptContext(matchingNodes)
            val promptText = "You are EDEN Environmental Intelligence. Answer this environmental query with scientific rigor (WHO/EPA/IPCC):\nQuery: $query\nVerified Context:\n$groundedContext"
            val encodedPrompt = java.net.URLEncoder.encode(promptText, "UTF-8")
            val request = Request.Builder()
                .url("https://text.pollinations.ai/$encodedPrompt?model=openai&seed=42")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            if (!response.isSuccessful) return null
            val answerText = response.body?.string()?.trim() ?: return null
            if (answerText.length < 25) return null

            val citations = matchingNodes.map {
                Citation(
                    source = it.authoritativeSource,
                    authority = it.authority,
                    year = it.sourceYear,
                    evidenceTier = it.evidenceTier,
                    keyEvidence = it.formulaOrStandard
                )
            }

            EdenAnswer(
                query = query,
                text = answerText,
                mode = if (matchingNodes.isNotEmpty()) AiResponseMode.LIVE_AI_GROUNDED else AiResponseMode.LIVE_AI,
                errorCode = AiErrorCode.NONE,
                debugDetails = "Served via Open-Source Environmental AI Engine (Public Free Access)",
                retrievedNodes = matchingNodes,
                citations = citations,
                suggestedActions = listOf(
                    "Inspect WHO & EPA Compliance Guidelines",
                    "Compute Lifecycle GHG in Calculators",
                    "Track Device & Transit Footprint"
                )
            )
        } catch (_: Exception) {
            null
        }
    }

    private fun buildGroundedPromptContext(nodes: List<KnowledgeEntity>): String {
        if (nodes.isEmpty()) return "No specific knowledge nodes retrieved from local database."
        return nodes.joinToString("\n---\n") { node ->
            """
                Title: ${node.title} (${node.category})
                Summary: ${node.summary}
                Scientific Context: ${node.scientificContext}
                Formula / Standard: ${node.formulaOrStandard}
                Authoritative Source: ${node.authoritativeSource} (${node.sourceYear}, ${node.authority})
                Evidence Tier: ${node.evidenceTier}
                Related Concepts: ${node.relatedConcepts}
            """.trimIndent()
        }
    }

    private fun buildKnowledgeGraphAnswer(
        query: String,
        nodes: List<KnowledgeEntity>,
        mode: AiResponseMode,
        errorCode: AiErrorCode,
        debugMsg: String?
    ): EdenAnswer {
        val citations = nodes.map {
            Citation(
                source = it.authoritativeSource,
                authority = it.authority,
                year = it.sourceYear,
                evidenceTier = it.evidenceTier,
                keyEvidence = it.formulaOrStandard
            )
        }

        val text = if (nodes.isNotEmpty()) {
            val primary = nodes.first()
            buildString {
                append("### ${primary.title}\n\n")
                append("${primary.summary}\n\n")
                append("#### Scientific Context\n")
                append("${primary.scientificContext}\n\n")
                append("#### Governed Standard / Formula\n")
                append("` ${primary.formulaOrStandard} `\n\n")
                append("#### Authoritative Reference\n")
                append("• Source: ${primary.authoritativeSource} (${primary.sourceYear})\n")
                append("• Authority: ${primary.authority} | ${primary.evidenceTier}\n\n")
                if (primary.relatedConcepts.isNotBlank()) {
                    append("#### Related Environmental Concepts\n")
                    append("${primary.relatedConcepts}\n")
                }
            }
        } else {
            "No direct match in the offline Knowledge Graph for \"$query\". " +
                    "Try exploring related categories such as Air Quality (PM2.5, Stack Flow), Water Quality (BOD, COD, pH), Carbon Accounting (Scope 1, 2, 3), or Life Cycle Assessment (ISO 14040)."
        }

        val actions = listOf(
            "Review 5-level concept breakdown",
            "Calculate emissions or load",
            "Verify regulatory standard"
        )

        return EdenAnswer(
            query = query,
            text = text,
            mode = mode,
            errorCode = errorCode,
            debugDetails = debugMsg,
            retrievedNodes = nodes,
            citations = citations,
            suggestedActions = actions
        )
    }
}
