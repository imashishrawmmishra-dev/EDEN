package com.example.data.model

enum class AiResponseMode(val code: String, val displayName: String, val badgeColor: Long) {
    LIVE_AI_GROUNDED("live_ai_grounded", "Grounded Live AI", 0xFF0F6E43),
    LIVE_AI("live_ai", "Live AI (Direct)", 0xFF137A74),
    OFFLINE_KNOWLEDGE_GRAPH("offline_knowledge_graph", "Offline Knowledge Graph", 0xFF2E7D32),
    OFFLINE_FALLBACK("offline_fallback", "Offline Fallback", 0xFFC25400)
}

enum class AiErrorCode(val code: String, val title: String, val guidance: String) {
    API_KEY_NOT_SET(
        "ERR_API_KEY_NOT_CONFIGURED",
        "Gemini API Key Missing",
        "No API key detected in build config or environment. EDEN has seamlessly switched to the offline verified Knowledge Graph."
    ),
    NETWORK_TIMEOUT(
        "ERR_NETWORK_TIMEOUT",
        "Network Timeout",
        "The upstream server took longer than 60 seconds to respond. EDEN served an offline verified response."
    ),
    QUOTA_EXCEEDED(
        "ERR_QUOTA_EXCEEDED",
        "Upstream Rate Limit / Quota",
        "API rate limit or quota exceeded for the configured key. Fallback to local deterministic knowledge layer activated."
    ),
    SERVICE_UNAVAILABLE(
        "ERR_UPSTREAM_SERVICE_UNAVAILABLE",
        "Provider Service Unavailable",
        "The AI service is temporarily unreachable (HTTP 503/502). Verified Knowledge Graph is powering your query."
    ),
    NETWORK_OFFLINE(
        "ERR_NETWORK_OFFLINE",
        "Device Offline",
        "No active internet connection. Operating in zero-latency offline Knowledge Graph mode."
    ),
    NONE("OK", "Successful", "Operating normally.")
}

data class EdenAnswer(
    val query: String,
    val text: String,
    val mode: AiResponseMode,
    val errorCode: AiErrorCode = AiErrorCode.NONE,
    val debugDetails: String? = null,
    val retrievedNodes: List<KnowledgeEntity> = emptyList(),
    val citations: List<Citation> = emptyList(),
    val suggestedActions: List<String> = emptyList(),
    val timestamp: Long = System.currentTimeMillis()
)

data class Citation(
    val source: String,
    val authority: String,
    val year: Int,
    val evidenceTier: String,
    val keyEvidence: String
)
