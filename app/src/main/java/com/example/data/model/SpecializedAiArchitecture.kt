package com.example.data.model

/**
 * Data definitions and repository for the "8 Different Specialized AI Models"
 * featured in the state-of-the-art AI architecture blueprint:
 * 1. LLM (Large Language Model)
 * 2. LCM (Large Concept Model)
 * 3. LAM (Large Action Model)
 * 4. MoE (Mixture of Experts)
 * 5. VLM (Vision Language Model)
 * 6. SLM (Small Language Model)
 * 7. MLM (Masked Language Model)
 * 8. SAM (Segment Anything Model)
 */

enum class SpecializedAiModelType(
    val acronym: String,
    val fullName: String,
    val tagline: String,
    val accentColor: Long,
    val primaryDomain: String
) {
    LLM(
        acronym = "LLM",
        fullName = "Large Language Model",
        tagline = "Autoregressive Predictive Sequence Intelligence",
        accentColor = 0xFF00E5FF, // Vivid Cyan
        primaryDomain = "Text Generation & Reasoning"
    ),
    LCM(
        acronym = "LCM",
        fullName = "Large Concept Model",
        tagline = "Continuous Concept Space Reasoning & Diffusion",
        accentColor = 0xFF00B0FF, // Deep Cyan
        primaryDomain = "Abstract Semantic Diffusion"
    ),
    LAM(
        acronym = "LAM",
        fullName = "Large Action Model",
        tagline = "Autonomous Agentic Intent & Action Execution",
        accentColor = 0xFF1DE9B6, // Vibrant Teal
        primaryDomain = "Action Planning & Tool Calling"
    ),
    MOE(
        acronym = "MoE",
        fullName = "Mixture of Experts",
        tagline = "Dynamic Gating Router with Top-K Sparse Activation",
        accentColor = 0xFF64FFDA, // Bright Mint Cyan
        primaryDomain = "Modular Specialized Routing"
    ),
    VLM(
        acronym = "VLM",
        fullName = "Vision Language Model",
        tagline = "Cross-Modal Projection & Multimodal Perception",
        accentColor = 0xFF40C4FF, // Sky Cyan
        primaryDomain = "Image + Text Multimodal Fusion"
    ),
    SLM(
        acronym = "SLM",
        fullName = "Small Language Model",
        tagline = "Quantized High-Efficiency On-Device Intelligence",
        accentColor = 0xFF00BFA5, // Dark Teal
        primaryDomain = "Edge & Low-Compute Inference"
    ),
    MLM(
        acronym = "MLM",
        fullName = "Masked Language Model",
        tagline = "Bidirectional Dual-Context Reconstruction",
        accentColor = 0xFF26A69A, // Sea Green Teal
        primaryDomain = "Contextual Embedding & Verification"
    ),
    SAM(
        acronym = "SAM",
        fullName = "Segment Anything Model",
        tagline = "Promptable Foundation Zero-Shot Mask Segmentation",
        accentColor = 0xFF18FFFF, // Neon Cyan
        primaryDomain = "Spatial & Terrain Segmentation"
    )
}

data class ArchitectureNode(
    val id: String,
    val label: String,
    val sublabel: String,
    val stage: Int, // 1 to 7 for visual level
    val branchIndex: Int = 0, // -1 (left), 0 (center), 1 (right)
    val iconType: String,
    val description: String,
    val tensorShape: String
)

data class ArchitectureConnection(
    val fromNodeId: String,
    val toNodeId: String,
    val label: String = ""
)

data class SimulationStep(
    val stepNumber: Int,
    val activeNodeIds: List<String>,
    val stageTitle: String,
    val computationDetails: String,
    val mathematicalFormula: String,
    val intermediatePayload: String,
    val latencyMs: Int
)

data class ArchitecturePreset(
    val title: String,
    val inputPrompt: String,
    val secondaryInput: String? = null,
    val expectedOutput: String,
    val simulationSteps: List<SimulationStep>
)

data class ModelSpecifications(
    val typicalParameters: String,
    val inferenceLatency: String,
    val memoryFootprint: String,
    val edgeDeploymentFeasibility: String, // "High (Mobile/IoT)", "Moderate (Edge Server)", "Low (Cloud Clustered)"
    val lossFunctionObjective: String,
    val flagshipImplementations: List<String>,
    val architecturalStrengths: List<String>,
    val primaryTradeoffs: List<String>
)

data class SpecializedAiModel(
    val type: SpecializedAiModelType,
    val nodes: List<ArchitectureNode>,
    val connections: List<ArchitectureConnection>,
    val specifications: ModelSpecifications,
    val presets: List<ArchitecturePreset>
)

object SpecializedAiRegistry {

    val allModels: List<SpecializedAiModel> by lazy {
        listOf(
            createLlmModel(),
            createLcmModel(),
            createLamModel(),
            createMoeModel(),
            createVlmModel(),
            createSlmModel(),
            createMlmModel(),
            createSamModel()
        )
    }

    fun getModel(type: SpecializedAiModelType): SpecializedAiModel {
        return allModels.firstOrNull { it.type == type } ?: allModels.first()
    }

    // =========================================================================
    // 1. LLM (Large Language Model)
    // Pipeline: input -> tokenization -> embedding -> transformer -> output
    // =========================================================================
    private fun createLlmModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("llm_input", "input", "Raw Text Prompt", 1, 0, "input", "Raw string stream entered by user or system prompt", "Seq[1, T_raw]"),
            ArchitectureNode("llm_token", "tokenization", "Byte-Pair / SentencePiece", 2, 0, "token", "Splits unicode string into vocabulary subword tokens", "[B=1, Seq_len=128]"),
            ArchitectureNode("llm_embed", "embedding", "Positional & Token Vector", 3, 0, "embedding", "Maps discrete token IDs to continuous high-dimensional vector space with RoPE", "[1, 128, D_model=4096]"),
            ArchitectureNode("llm_trans", "transformer", "Stacked Decoder Layers", 4, 0, "transformer", "Multi-Head Self-Attention + SwiGLU Feed-Forward + LayerNorm residual blocks", "[32 layers, 32 heads, 4096-d]"),
            ArchitectureNode("llm_output", "output", "Autoregressive Stream", 5, 0, "output", "Softmax logits projected to vocabulary probability distribution generating tokens", "P(w_t | w_<t)")
        )

        val connections = listOf(
            ArchitectureConnection("llm_input", "llm_token"),
            ArchitectureConnection("llm_token", "llm_embed"),
            ArchitectureConnection("llm_embed", "llm_trans"),
            ArchitectureConnection("llm_trans", "llm_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "7B to 1.8T parameters",
            inferenceLatency = "15 - 45 ms / token",
            memoryFootprint = "16 GB (FP16 7B) to 1.2 TB (Distributed 70B+)",
            edgeDeploymentFeasibility = "Moderate (requires 4-bit INT4 quantization for mobile)",
            lossFunctionObjective = "Cross-Entropy over Next-Token: L = -sum log P(x_t | x_<t)",
            flagshipImplementations = listOf("Google Gemini 1.5", "OpenAI GPT-4o", "Meta LLaMA 3.3", "Mistral Large"),
            architecturalStrengths = listOf(
                "Universal zero-shot generalization across open-ended reasoning tasks",
                "Massive world knowledge compression in parametric weights",
                "Exceptional instruction following and code generation"
            ),
            primaryTradeoffs = listOf(
                "Autoregressive generation is memory-bandwidth bound (O(N) sequential steps)",
                "Hallucination risks without external verification or grounding",
                "High GPU inference VRAM costs at scale"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Carbon Budget & Net-Zero Strategy",
                inputPrompt = "Formulate a carbon decarbonization roadmap for a municipal manufacturing plant.",
                expectedOutput = "3-Phase Decarbonization Plan: Energy audit (Scope 1 direct reduction), Solar PPA transition (Scope 2), Supplier code of conduct (Scope 3).",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("llm_input"), "Input Text Processing", "Parsed 72 characters into raw UTF-8 byte stream.", "x = TextStream(prompt)", "Tokens: 14 | Length: 72 chars", 3),
                    SimulationStep(2, listOf("llm_token"), "Subword Tokenization", "Byte-Pair Encoding mapped prompt into token ID sequences.", "T = BPE.encode(\"Formulate...\") -> [18452, 264, 4398, 1205...]", "IDs: [18452, 264, 4398, 1205, 31920, 287, 264, 18902]", 5),
                    SimulationStep(3, listOf("llm_embed"), "Token & Rotary Positional Embedding", "Projected token indices into 4096-dim vector space and applied RoPE frequencies.", "E = W_embed[T] + RoPE(pos)", "Tensor: [1, 14, 4096] Float16", 4),
                    SimulationStep(4, listOf("llm_trans"), "Stacked Transformer Layers", "Propagated through 32 Decoder blocks with Grouped-Query Attention (GQA) and KV-caching.", "H = LayerNorm(SelfAttention(Q,K,V) + FFN(H))", "32 Layers complete | Attention Entropy: 2.14", 28),
                    SimulationStep(5, listOf("llm_output"), "Logit Projection & Sampling", "Softmax applied over 128k vocabulary with temperature 0.4. Sampled next tokens.", "P(token) = softmax(H_last * W_unembed / T)", "Next Tokens: \"Phase 1: Energy Audit...\"", 12)
                )
            ),
            ArchitecturePreset(
                title = "Environmental Law & Compliance Check",
                inputPrompt = "Does an unpermitted stormwater discharge violate the Clean Water Act NPDES?",
                expectedOutput = "Yes. Section 301(a) of the Clean Water Act prohibits the discharge of any pollutant from a point source into navigable waters without an NPDES permit.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("llm_input"), "Legal Query Ingestion", "Received environmental regulatory verification query.", "x = LegalPrompt", "Prompt size: 12 tokens", 2),
                    SimulationStep(2, listOf("llm_token"), "Regulatory Lexicon Tokenization", "Extracted specialized legal tokens: [NPDES, Section 301(a), Stormwater].", "T = Tokenizer(x)", "IDs: [1402, 381, 7490, 8912]", 4),
                    SimulationStep(3, listOf("llm_embed"), "Dense Vector Formulation", "Aligned regulatory terminology with US Code environmental embeddings.", "E = Embed(T)", "Embedding norm: 1.002", 4),
                    SimulationStep(4, listOf("llm_trans"), "Causal Attention Over Statues", "Transformer layers retrieved statutory cross-attention correlations.", "Attn(Q,K,V) -> CWA Title 33 § 1342", "CWA 301(a) confidence: 99.4%", 24),
                    SimulationStep(5, listOf("llm_output"), "Authoritative Statutory Output", "Synthesized direct legal ruling with citation.", "y = Sample(P_vocab)", "Direct confirmation of violation", 10)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.LLM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 2. LCM (Large Concept Model)
    // Pipeline: input -> sentence segmentation -> SONAR embedding -> diffusion
    //           -> [advanced patterning, hidden process, quantization] -> output
    // =========================================================================
    private fun createLcmModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("lcm_input", "input", "Raw Multi-Sentence Text", 1, 0, "input", "High-level contextual discourse or paragraph", "Paragraph / Text Stream"),
            ArchitectureNode("lcm_seg", "sentence segmentation", "Discourse Boundary Parser", 2, 0, "segment", "Splits input into autonomous semantic sentence propositions", "List[Sentence_i]"),
            ArchitectureNode("lcm_sonar", "SONAR embedding", "Universal Multilingual Latent", 3, 0, "sonar", "Meta SONAR sentence-level continuous embedding mapping to semantic space", "[N_sentences, D=1024]"),
            ArchitectureNode("lcm_diff", "diffusion", "Concept Latent Denoising", 4, 0, "diffusion", "Iterative continuous denoising diffusion generating concept vectors in latent space", "z_t -> z_0 in R^1024"),
            ArchitectureNode("lcm_pattern", "advanced patterning", "Inter-concept Syntax Dynamics", 5, -1, "pattern", "Models semantic structure and thematic relationships across sentences", "Pattern Matrix [K, K]"),
            ArchitectureNode("lcm_hidden", "hidden process", "Non-autoregressive Latent Reasoning", 5, 0, "hidden", "Continuous latent-space computation bypassing token-by-token generation", "H_latent [1024-d]"),
            ArchitectureNode("lcm_quant", "quantization", "Semantic Vector Codebook", 5, 1, "quantize", "Residual vector quantization discretizing concept vectors for exact decoding", "Codebook VQ [B, 1024]"),
            ArchitectureNode("lcm_output", "output", "Concept-to-Text Generation", 6, 0, "output", "Decodes diffused concept vectors back into fluent natural language sentences", "Decoded Discourse")
        )

        val connections = listOf(
            ArchitectureConnection("lcm_input", "lcm_seg"),
            ArchitectureConnection("lcm_seg", "lcm_sonar"),
            ArchitectureConnection("lcm_sonar", "lcm_diff"),
            ArchitectureConnection("lcm_diff", "lcm_pattern"),
            ArchitectureConnection("lcm_diff", "lcm_hidden"),
            ArchitectureConnection("lcm_diff", "lcm_quant"),
            ArchitectureConnection("lcm_pattern", "lcm_output"),
            ArchitectureConnection("lcm_hidden", "lcm_output"),
            ArchitectureConnection("lcm_quant", "lcm_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "1B to 13B parameters",
            inferenceLatency = "10 - 25 ms / sentence (parallel generation)",
            memoryFootprint = "4 GB to 24 GB",
            edgeDeploymentFeasibility = "High (parallel sentence generation dramatically cuts compute cycles)",
            lossFunctionObjective = "Denoising Score Matching in SONAR Space: E_{t,z}[||eps - eps_theta(z_t, t)||^2]",
            flagshipImplementations = listOf("Meta LCM (Large Concept Model)", "SONAR Multilingual Foundation", "Diffusion-LM Research"),
            architecturalStrengths = listOf(
                "Operates on higher-level 'concepts' rather than low-level token frequencies",
                "Non-autoregressive sentence generation achieves true long-horizon coherence",
                "Language-agnostic representation natively supports 200+ languages without token bias"
            ),
            primaryTradeoffs = listOf(
                "Requires high-quality sentence-level continuous embedding spaces (SONAR)",
                "Fine-grained token-level formatting can require auxiliary decoders",
                "Emergent frontier research paradigm requiring specialized training data"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Ecosystem Restoration Concept Synthesis",
                inputPrompt = "Mangrove wetlands act as blue carbon sinks. Rising sea levels threaten coastal marshes. We require adaptive restoration bio-corridors.",
                expectedOutput = "Concept sequence synthesized: 1. Marine Carbon Sequestration -> 2. Tidal Inundation Stress -> 3. Vegetative Buffer Zone Implementation.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("lcm_input"), "Discourse Ingestion", "Received 3 multi-sentence environmental propositions.", "Input text parsed", "3 sentences loaded", 2),
                    SimulationStep(2, listOf("lcm_seg"), "Sentence Segmentation", "Identified proposition boundaries and sentence dependency graphs.", "S = Segment(text) -> [S1, S2, S3]", "S1: 'Mangrove...', S2: 'Rising...', S3: 'We require...'", 4),
                    SimulationStep(3, listOf("lcm_sonar"), "SONAR Continuous Embedding", "Mapped each sentence to Meta SONAR 1024-dimensional semantic space.", "z = SONAR_encoder(S) in R^{3 x 1024}", "Concept Cosine Similarity: 0.884", 8),
                    SimulationStep(4, listOf("lcm_diff"), "Diffusion in Concept Space", "Applied 20-step reverse diffusion to sample next conceptual proposition vectors.", "z_{t-1} = 1/sqrt(alpha) * (z_t - (1-alpha)/sqrt(1-bar_alpha) * eps)", "Denoised Latent Vector z_0 (Residual: 0.012)", 18),
                    SimulationStep(5, listOf("lcm_pattern", "lcm_hidden", "lcm_quant"), "Tri-Branch Patterning & Quantization", "Simultaneously resolved inter-concept thematic coherence, hidden latent states, and VQ-codebook quantization.", "c = Quantize(Pattern(z_0) + Hidden(z_0))", "Quantized Vector Codebook match: #4928", 12),
                    SimulationStep(6, listOf("lcm_output"), "Concept-to-Discourse Reconstruction", "Decoded the continuous concept vector into natural language.", "Discourse = SONAR_decoder(c)", "Result: 'Co-benefits include biodiversity surge...'", 6)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.LCM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 3. LAM (Large Action Model)
    // Pipeline: input processing -> perception system -> intent recognition
    //           -> [task breakdown, neuro-symbolic integration, memory system, quantization]
    //           -> action planning -> feedback integration
    // =========================================================================
    private fun createLamModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("lam_input", "input processing", "Multimodal User Command", 1, 0, "input", "Natural voice, text, or sensor trigger initiating autonomous actions", "User Command / Event"),
            ArchitectureNode("lam_percep", "perception system", "UI & Environment Scanner", 2, 0, "perception", "Visual DOM, UI hierarchy parser, or environmental sensor stream listener", "Screen DOM / Sensor State"),
            ArchitectureNode("lam_intent", "intent recognition", "Goal & Boundary Extraction", 3, 0, "intent", "Deduce target goal state, permissions, constraints, and safety invariants", "Goal State G*"),
            ArchitectureNode("lam_breakdown", "task breakdown", "Hierarchical Planner", 4, -1, "planner", "Decomposes complex macro-goals into directed acyclic graph (DAG) sub-tasks", "DAG[SubTask_1..N]"),
            ArchitectureNode("lam_neuro", "neuro-symbolic integration", "Formal Logic & Safety Verifier", 4, 0, "neuro", "Validates action safety against formal constraints, API schemas, and laws", "Invariant Check (Safe=true)"),
            ArchitectureNode("lam_mem", "memory system", "Episodic & Procedural Memory", 4, 1, "memory", "Stores past action trajectories, user preferences, and state history", "VectorDB + Working RAM"),
            ArchitectureNode("lam_quant", "quantization", "Low-Latency Action Kernels", 4, 0, "quantize", "Optimized lightweight neural policies for real-time robotic or mobile interaction", "INT8 Policy Latency <10ms"),
            ArchitectureNode("lam_plan", "action planning", "Executable Sequence Dispatcher", 5, 0, "action", "Generates atomic API calls, click coordinates, SQL queries, or hardware commands", "Call: ExportReport(format=PDF)"),
            ArchitectureNode("lam_feedback", "feedback integration", "Closed-Loop State Validator", 6, 0, "feedback", "Observes UI/sensor outcome, computes reward, and triggers corrective adjustments", "Observation -> Next Step")
        )

        val connections = listOf(
            ArchitectureConnection("lam_input", "lam_percep"),
            ArchitectureConnection("lam_percep", "lam_intent"),
            ArchitectureConnection("lam_intent", "lam_breakdown"),
            ArchitectureConnection("lam_intent", "lam_neuro"),
            ArchitectureConnection("lam_intent", "lam_mem"),
            ArchitectureConnection("lam_intent", "lam_quant"),
            ArchitectureConnection("lam_breakdown", "lam_plan"),
            ArchitectureConnection("lam_neuro", "lam_plan"),
            ArchitectureConnection("lam_mem", "lam_plan"),
            ArchitectureConnection("lam_quant", "lam_plan"),
            ArchitectureConnection("lam_plan", "lam_feedback"),
            ArchitectureConnection("lam_feedback", "lam_percep", "Iterative Loop")
        )

        val specs = ModelSpecifications(
            typicalParameters = "3B to 30B parameters",
            inferenceLatency = "20 - 80 ms / action decision",
            memoryFootprint = "8 GB to 32 GB",
            edgeDeploymentFeasibility = "High (optimized action heads run directly in mobile OS or robotics)",
            lossFunctionObjective = "Behavioral Cloning + Reinforcement Learning with Human Feedback (RLHF) / DPO on Trajectories",
            flagshipImplementations = listOf("Rabbit OS (LAM Engine)", "Adept ACT-1", "Google Project Astra", "AutoGPT / Devin Agentic Runtimes"),
            architecturalStrengths = listOf(
                "Translates abstract user intents into concrete, deterministic, multi-step actions",
                "Integrates formal neuro-symbolic logic to prevent invalid API calls",
                "Closed-loop feedback enables real-time self-correction upon system errors"
            ),
            primaryTradeoffs = listOf(
                "Requires robust safety sandboxing to prevent unintended real-world side effects",
                "Sensitive to UI layout changes or dynamic DOM shifts without visual grounding",
                "State drift over extremely long sequential trajectories"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Automated ISO 14001 Environmental Audit",
                inputPrompt = "Audit current stack emissions, calculate breach probabilities, and generate a formal compliance notification.",
                expectedOutput = "Action plan executed: 1. Fetch sensor telemetry -> 2. Run EPA Dispersion model -> 3. Draft PDF report -> 4. Send encrypted notification.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("lam_input"), "Input Command Parsing", "Extracted action request: 'Audit stack emissions, check breach, alert'.", "cmd = Parse(user_intent)", "Goal: StackAuditAndAlert", 2),
                    SimulationStep(2, listOf("lam_percep"), "Perception System Scan", "Queried live stack sensors and station telemetry database.", "State S_0 = SensorRepo.getLiveMetrics()", "SO2: 78 ug/m3, NO2: 84 ug/m3", 10),
                    SimulationStep(3, listOf("lam_intent"), "Intent & Constraint Extraction", "Deduce target: ISO 14001 compliance audit with strict EPA limits.", "Goal: Audit(target='Stack-01', limit=80ug)", "Severity Level: Warning", 5),
                    SimulationStep(4, listOf("lam_breakdown", "lam_neuro", "lam_mem", "lam_quant"), "Task Breakdown & Symbolic Logic Verification", "Generated 4-step DAG, verified legal schema against CPCB/EPA rules, and retrieved past baseline from memory.", "DAG: [Fetch -> Disperse -> RenderPDF -> Dispatch]", "Safety Check: PASSED (No unauthorized writes)", 16),
                    SimulationStep(5, listOf("lam_plan"), "Action Planning & Execution", "Dispatched atomic tasks: Generated formal PDF sheet and queued notification.", "Exec: GenerateAuditSheet(Stack-01)", "Report UUID: AUDIT-2026-9012", 14),
                    SimulationStep(6, listOf("lam_feedback"), "Feedback Integration Loop", "Verified report generation success code 200 and notified dashboard.", "R = VerifyExecution(status=SUCCESS)", "Execution verified. Closed-loop complete.", 6)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.LAM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 4. MoE (Mixture of Experts)
    // Pipeline: input -> router mechanism -> [expert 1, expert 2, expert 3, expert 4]
    //           -> top-K selection -> weighted combination -> output
    // =========================================================================
    private fun createMoeModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("moe_input", "input", "Token Representation", 1, 0, "input", "Hidden state vector from earlier transformer layer", "Hidden State H [1, D=4096]"),
            ArchitectureNode("moe_router", "router mechanism", "Sparse Gating Network", 2, 0, "router", "Softmax gating linear projection estimating suitability score for each expert", "G(x) = softmax(x * W_g)"),
            ArchitectureNode("moe_exp1", "expert 1", "Carbon & GHG Specialist", 3, -2, "expert", "Feed-forward network trained on Scope 1/2/3 math, IPCC AR6, and carbon chemistry", "FFN_1(x) [4096 -> 14336 -> 4096]"),
            ArchitectureNode("moe_exp2", "expert 2", "Air Quality & Dispersion", 3, -1, "expert", "Specialized parameters for AERMOD, PM2.5 chemistry, and atmospheric transport", "FFN_2(x) [4096 -> 14336 -> 4096]"),
            ArchitectureNode("moe_exp3", "expert 3", "Water & Hydrology", 3, 1, "expert", "Deep parameters for BOD/COD kinetics, Streeter-Phelps, and aquifer flow", "FFN_3(x) [4096 -> 14336 -> 4096]"),
            ArchitectureNode("moe_exp4", "expert 4", "Environmental Policy & ESG", 3, 2, "expert", "Trained on ISO 14001, EPA CFR 40, EU CSRD, and corporate governance", "FFN_4(x) [4096 -> 14336 -> 4096]"),
            ArchitectureNode("moe_topk", "top-K selection", "Top-2 Sparse Filter", 4, 0, "topk", "Zeros out all expert scores except the top-K highest activations (e.g. K=2)", "TopK(G(x), K=2)"),
            ArchitectureNode("moe_weight", "weighted combination", "Weighted Sum Aggregator", 5, 0, "weighted", "Computes linear combination of selected expert outputs weighted by gating scores", "y = sum_{i in TopK} G_i(x) * FFN_i(x)"),
            ArchitectureNode("moe_output", "output", "Dense Composite Representation", 6, 0, "output", "Blended expert representation returned to downstream transformer block", "H_out [1, D=4096]")
        )

        val connections = listOf(
            ArchitectureConnection("moe_input", "moe_router"),
            ArchitectureConnection("moe_router", "moe_exp1", "Score: 0.54"),
            ArchitectureConnection("moe_router", "moe_exp2", "Score: 0.38"),
            ArchitectureConnection("moe_router", "moe_exp3", "Score: 0.05"),
            ArchitectureConnection("moe_router", "moe_exp4", "Score: 0.03"),
            ArchitectureConnection("moe_exp1", "moe_topk"),
            ArchitectureConnection("moe_exp2", "moe_topk"),
            ArchitectureConnection("moe_exp3", "moe_topk"),
            ArchitectureConnection("moe_exp4", "moe_topk"),
            ArchitectureConnection("moe_topk", "moe_weight"),
            ArchitectureConnection("moe_weight", "moe_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "8x7B (47B total, 13B active) to 8x22B (141B total, 39B active)",
            inferenceLatency = "12 - 25 ms / token (speed of small model with capacity of huge model)",
            memoryFootprint = "32 GB to 96 GB (weights must reside in VRAM)",
            edgeDeploymentFeasibility = "Moderate (requires high RAM for expert weights or dynamic offloading)",
            lossFunctionObjective = "Cross-Entropy + Load Balancing Loss: L_total = L_task + alpha * L_balance",
            flagshipImplementations = listOf("Mistral Mixtral 8x7B / 8x22B", "DeepSeek-V2 / DeepSeek-V3", "Google Gemini 1.5 MoE", "Databricks DBRX"),
            architecturalStrengths = listOf(
                "Drastically lower compute FLOPs per token by only activating a sparse fraction of parameters",
                "High parameter capacity allows sub-networks to specialize deeply in distinct domains",
                "Faster training throughput and inference efficiency compared to dense equivalents"
            ),
            primaryTradeoffs = listOf(
                "Router load imbalance can cause expert collapse without careful loss balancing",
                "High aggregate VRAM requirement to hold all expert weights in memory",
                "All-to-All network communication overhead across multi-GPU clusters"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Flue Gas Desulfurization & Scope 1 Emissions",
                inputPrompt = "Evaluate limestone scrubber efficiency on SO2 removal and its corresponding CO2 byproduct generation.",
                expectedOutput = "Router dynamically activates Expert 1 (Carbon: 56%) and Expert 2 (Air Quality: 41%) for joint chemical equilibrium calculation.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("moe_input"), "Token State Ingestion", "Received query with dual chemical domains: SO2 scrubbing & CO2 footprint.", "x in R^4096", "Token: 'limestone scrubber SO2 CO2'", 2),
                    SimulationStep(2, listOf("moe_router"), "Gating Router Softmax Scoring", "Gating network computed relevance probabilities across all 4 experts.", "G(x) = softmax(x * W_gate)", "E1(Carbon): 0.56 | E2(Air): 0.41 | E3(Water): 0.02 | E4(Policy): 0.01", 4),
                    SimulationStep(3, listOf("moe_exp1", "moe_exp2"), "Sparse Expert Activation", "Dispatched token state only to Expert 1 and Expert 2. Experts 3 and 4 bypassed.", "Compute: FFN_1(x) and FFN_2(x)", "Zero compute expended on E3 & E4", 12),
                    SimulationStep(4, listOf("moe_topk"), "Top-2 Sparsity Gate", "Hard thresholding clamped bottom experts to exactly zero.", "Active Indices: [1, 2]", "Sparsity Ratio: 50% Compute Active", 3),
                    SimulationStep(5, listOf("moe_weight"), "Normalized Weighted Combination", "Normalized active weights (0.577, 0.423) and computed linear vector combination.", "y = 0.577 * FFN_1 + 0.423 * FFN_2", "Combined Vector Norm: 1.00", 4),
                    SimulationStep(6, listOf("moe_output"), "Unified Expert Synthesis", "Propagated to output layer, synthesizing scrubber reaction: CaCO3 + SO2 + 0.5 O2 -> CaSO4 + CO2.", "Output: Chemical Balance", "SO2 Removal: 98% | Scope 1 CO2: +0.44 t/t-SO2", 5)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.MOE, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 5. VLM (Vision Language Model)
    // Pipeline: [image input, text input] -> [vision encoder, text encoder]
    //           -> projection interface -> multimodal processor -> language model
    //           -> output generation
    // =========================================================================
    private fun createVlmModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("vlm_img_in", "image input", "Satellite / Camera Image", 1, -1, "image", "RGB pixel matrix (e.g. 448x448 or dynamic high-resolution tiles)", "Image [3, 448, 448]"),
            ArchitectureNode("vlm_txt_in", "text input", "Text Prompt / Question", 1, 1, "text", "Natural language query inquiring about image features", "Text Prompt"),
            ArchitectureNode("vlm_vis_enc", "vision encoder", "Vision Transformer (ViT)", 2, -1, "vision", "Patches image into 14x14 tokens and applies multi-head visual self-attention", "ViT [N_patches=1024, D_v=1152]"),
            ArchitectureNode("vlm_txt_enc", "text encoder", "Tokenizer & Embedding", 2, 1, "token", "Tokenizes text prompt into linguistic token vector embeddings", "[B, T_text, D_t=4096]"),
            ArchitectureNode("vlm_proj", "projection interface", "Cross-Attention / MLP Aligner", 3, 0, "projection", "Projects visual feature space into language model latent dimension (e.g. 1152 -> 4096)", "W_proj * V_patches -> [1024, 4096]"),
            ArchitectureNode("vlm_multi_proc", "multimodal processor", "Unified Sequence Assembler", 4, 0, "processor", "Interleaves visual tokens with text tokens: [BOS, <image>, patches..., <query>, EOS]", "Seq[1024 + T_text, 4096]"),
            ArchitectureNode("vlm_llm", "language model", "Autoregressive Multimodal LLM", 5, 0, "transformer", "Processes concatenated visual and linguistic tokens through multimodal transformer", "32 Multi-Modal Layers"),
            ArchitectureNode("vlm_output", "output generation", "Visual Grounded Output", 6, 0, "output", "Generates grounded descriptions, bounding boxes, or environmental hazard analysis", "Natural Text + Coordinates")
        )

        val connections = listOf(
            ArchitectureConnection("vlm_img_in", "vlm_vis_enc"),
            ArchitectureConnection("vlm_txt_in", "vlm_txt_enc"),
            ArchitectureConnection("vlm_vis_enc", "vlm_proj"),
            ArchitectureConnection("vlm_txt_enc", "vlm_multi_proc"),
            ArchitectureConnection("vlm_proj", "vlm_multi_proc"),
            ArchitectureConnection("vlm_multi_proc", "vlm_llm"),
            ArchitectureConnection("vlm_llm", "vlm_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "2B (PaliGemma) to 34B (LLaVA-NeXT) to 1T+ (Gemini)",
            inferenceLatency = "30 - 90 ms first token (image encoding time)",
            memoryFootprint = "6 GB (INT4) to 32 GB (FP16)",
            edgeDeploymentFeasibility = "Moderate (PaliGemma-2B & MobileVLM run on modern mobile NPUs)",
            lossFunctionObjective = "Autoregressive Next-Token Cross-Entropy conditioned on Visual Prefix: L = -sum log P(w_t | V, w_<t)",
            flagshipImplementations = listOf("Google PaliGemma / Gemini Vision", "LLaVA-1.6 (NeXT)", "OpenAI GPT-4o Vision", "Qwen2-VL"),
            architecturalStrengths = listOf(
                "Unified reasoning over complex visual scenes, satellite imagery, and technical blueprints",
                "Spatial grounding capability with coordinate bounding box outputs",
                "Direct extraction of tabular metrics from environmental field monitors and gauges"
            ),
            primaryTradeoffs = listOf(
                "Visual patches consume significant context length (e.g. 1024 tokens per high-res image)",
                "Fine detail blur in ultra-dense spatial or satellite imagery without dynamic tiling",
                "Optical character recognition (OCR) fragility on handwritten or low-contrast text"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Satellite Deforestation & Canopy Health",
                inputPrompt = "Analyze this multispectral satellite tile and estimate forest canopy loss percentage.",
                secondaryInput = "Sentinel-2 NIR/RGB Composite [448x448]",
                expectedOutput = "Detected 14.2% canopy reduction along the western drainage corridor. NDVI index dropped from 0.82 to 0.41.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("vlm_img_in", "vlm_txt_in"), "Dual Input Ingestion", "Loaded Sentinel-2 image tile and text query prompt.", "Img: [3, 448, 448] | Text: 'Analyze forest canopy...'", "Inputs ready", 5),
                    SimulationStep(2, listOf("vlm_vis_enc", "vlm_txt_enc"), "Dual Specialized Encoders", "ViT segmented image into 1024 patches (14x14 pixels). Text encoder processed prompt.", "Vis: [1024, 1152] | Txt: [16, 4096]", "Visual patches & text tokens encoded", 18),
                    SimulationStep(3, listOf("vlm_proj"), "Projection Interface Bridge", "2-layer MLP projected 1152-dim vision features into 4096-dim LLM latent space.", "V_proj = GELU(V * W_1) * W_2", "Projected visual tokens: [1024, 4096]", 8),
                    SimulationStep(4, listOf("vlm_multi_proc"), "Multimodal Sequence Interleaving", "Assembled unified context: <image_start> [1024 visual tokens] <image_end> Analyze canopy loss...", "Context length: 1040 tokens", "Context tensor ready", 6),
                    SimulationStep(5, listOf("vlm_llm"), "Multimodal Self-Attention", "Full cross-attention across image patches and spatial query coordinates.", "Attn(Vision, Text)", "Localized canopy defect region", 28),
                    SimulationStep(6, listOf("vlm_output"), "Spatial Grounded Output", "Generated diagnostic text with bounding box coordinates and NDVI estimation.", "Output tokens streamed", "Western corridor: 14.2% loss | BBox: [120, 45, 310, 280]", 12)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.VLM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 6. SLM (Small Language Model)
    // Pipeline: input processing -> compact tokenization -> efficient transformer
    //           -> [model quantization, memory optimization] -> edge deployment
    //           -> output generation
    // =========================================================================
    private fun createSlmModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("slm_input", "input processing", "Field Sensor / Mobile Input", 1, 0, "input", "Low-overhead input stream from mobile device or remote field monitoring station", "Mobile Stream"),
            ArchitectureNode("slm_tok", "compact tokenization", "Dense Pruned Vocabulary", 2, 0, "token", "Optimized 32k vocabulary eliminating rare tokens to shrink embedding parameter table", "Vocab: 32,768 (Compact)"),
            ArchitectureNode("slm_trans", "efficient transformer", "FlashAttention & GQA Core", 3, 0, "transformer", "Grouped-Query Attention (GQA) with rotary embeddings and shared attention heads", "16 Layers, 12 Heads, D=2048"),
            ArchitectureNode("slm_quant", "model quantization", "INT4 / AWQ Precision", 4, -1, "quantize", "Post-training quantization (AWQ/GPTQ) reducing FP16 weights to 4-bit integers with zero perplexity drop", "Weight size: 1.8 GB (INT4)"),
            ArchitectureNode("slm_mem", "memory optimization", "Paged KV-Cache & Sliding Window", 4, 1, "memory", "Rolling sliding window attention restricting memory to recent 2048 tokens", "KV Cache: <120 MB RAM"),
            ArchitectureNode("slm_edge", "edge deployment", "On-Device NPU / CPU Engine", 5, 0, "edge", "Compiled via ONNX Runtime / MediaPipe / ExecuTorch directly on mobile silicon", "NPU Acceleration 45 tok/s"),
            ArchitectureNode("slm_output", "output generation", "Instant Low-Latency Response", 6, 0, "output", "Zero cloud dependence, 0ms network latency, 100% offline private output", "Offline Deterministic Text")
        )

        val connections = listOf(
            ArchitectureConnection("slm_input", "slm_tok"),
            ArchitectureConnection("slm_tok", "slm_trans"),
            ArchitectureConnection("slm_trans", "slm_quant"),
            ArchitectureConnection("slm_trans", "slm_mem"),
            ArchitectureConnection("slm_quant", "slm_edge"),
            ArchitectureConnection("slm_mem", "slm_edge"),
            ArchitectureConnection("slm_edge", "slm_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "1B to 3.8B parameters",
            inferenceLatency = "8 - 18 ms / token (on modern smartphone NPU)",
            memoryFootprint = "900 MB to 2.2 GB RAM",
            edgeDeploymentFeasibility = "Extremely High (runs locally on Android, iOS, Raspberry Pi, drones)",
            lossFunctionObjective = "Cross-Entropy + Knowledge Distillation from 70B+ Teacher: L = alpha*L_CE + beta*L_KD",
            flagshipImplementations = listOf("Microsoft Phi-3.5 Mini (3.8B)", "Google Gemma 2 (2B)", "Apple OpenELM", "Meta LLaMA 3.2 1B/3B"),
            architecturalStrengths = listOf(
                "Zero cloud dependency: fully operational offline in remote forests or oceanic vessels",
                "Complete privacy and zero data leakage: tokens never leave local hardware",
                "Sub-second cold start with minimal battery drain"
            ),
            primaryTradeoffs = listOf(
                "Constrained parametric world knowledge compared to 70B+ frontier models",
                "Lower tolerance for multi-step open-ended symbolic reasoning without chain-of-thought",
                "Shorter maximum effective context window"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Offline Water Quality Field Calculation",
                inputPrompt = "Raw river water sample: Temp=22C, Dissolved Oxygen=4.8 mg/L, BOD5=14 mg/L. Assess health tier.",
                expectedOutput = "Offline Assessment: Stressed Aquifer. Critical DO deficit. Stream aeration required. Zero cloud latency: 14ms.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("slm_input"), "Direct Hardware Input", "Received telemetry directly from local Bluetooth dissolved oxygen probe.", "Input: DO=4.8, BOD=14", "Offline telemetry captured", 1),
                    SimulationStep(2, listOf("slm_tok"), "Compact Tokenizer", "Parsed metrics using lightweight 32k vocabulary dictionary.", "Tokens: [DO, 4.8, BOD, 14]", "Token IDs: [821, 492, 1084, 39]", 2),
                    SimulationStep(3, listOf("slm_trans"), "Efficient Transformer Execution", "Computed forward pass across 16 GQA transformer blocks.", "Forward pass over 2048-dim state", "Attention FLOPs: 0.04 GFLOPs", 8),
                    SimulationStep(4, listOf("slm_quant", "slm_mem"), "INT4 Quantization & Paged Memory", "Integer tensor cores executed INT4 matrix math; memory restricted to 85MB.", "W_int4 * a_fp16 -> accumulator", "RAM allocated: 85 MB", 3),
                    SimulationStep(5, listOf("slm_edge"), "Mobile NPU Dispatch", "Hardware accelerated using on-device Neural Processing Unit (NPU).", "NPU Core Utilization: 42%", "Speed: 52 tokens/sec", 2),
                    SimulationStep(6, listOf("slm_output"), "Instant Local Output", "Delivered immediate water quality diagnosis with zero network round-trip.", "Latency: 14ms (100% Offline)", "Status: Class D - Stressed | Action: Aerate", 2)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.SLM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 7. MLM (Masked Language Model)
    // Pipeline: text input -> token masking -> embedding layer
    //           -> [left context, right context] -> bidirectional attention
    //           -> masked token prediction -> feature representation
    // =========================================================================
    private fun createMlmModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("mlm_input", "text input", "Corrupted / Incomplete Text", 1, 0, "input", "Raw sentence containing missing, masked, or redacted clauses", "Text with [MASK]"),
            ArchitectureNode("mlm_mask", "token masking", "Dynamic [MASK] Injector", 2, 0, "mask", "Replaces 15% of tokens with special [MASK] token for bidirectional infilling", "x_masked = Mask(x, 15%)"),
            ArchitectureNode("mlm_embed", "embedding layer", "Token + Segment + Position", 3, 0, "embedding", "Combines token vector, segment ID, and absolute learned positional embeddings", "[B, Seq_len, D=768]"),
            ArchitectureNode("mlm_left", "left context", "Preceding Semantic Context (<-)", 4, -1, "left", "Attends to all preceding tokens up to start of sequence simultaneously", "Attn(i, j <= i)"),
            ArchitectureNode("mlm_right", "right context", "Subsequent Semantic Context (->)", 4, 1, "right", "Attends to all future tokens up to end of sequence simultaneously without causal mask", "Attn(i, j >= i)"),
            ArchitectureNode("mlm_bi_attn", "bidirectional attention", "Full Non-Causal Attention", 5, 0, "biattn", "Complete unmasked attention matrix: every token attends to every other token", "A_{ij} = softmax(Q_i K_j^T / sqrt(d))"),
            ArchitectureNode("mlm_pred", "masked token prediction", "Vocab Probability Distribution", 6, 0, "prediction", "Linear classifier predicting the exact identity of the [MASK] token", "P(w_[MASK] | Left, Right)"),
            ArchitectureNode("mlm_feat", "feature representation", "Dense Contextual Embeddings", 7, 0, "feature", "Outputs rich contextual token vectors ideal for classification, NER, and search", "CLS Vector [768-d]")
        )

        val connections = listOf(
            ArchitectureConnection("mlm_input", "mlm_mask"),
            ArchitectureConnection("mlm_mask", "mlm_embed"),
            ArchitectureConnection("mlm_embed", "mlm_left"),
            ArchitectureConnection("mlm_embed", "mlm_right"),
            ArchitectureConnection("mlm_left", "mlm_bi_attn"),
            ArchitectureConnection("mlm_right", "mlm_bi_attn"),
            ArchitectureConnection("mlm_bi_attn", "mlm_pred"),
            ArchitectureConnection("mlm_pred", "mlm_feat")
        )

        val specs = ModelSpecifications(
            typicalParameters = "110M (BERT Base) to 340M (BERT Large) to 560M (DeBERTa-v3)",
            inferenceLatency = "2 - 6 ms (instant batch inference)",
            memoryFootprint = "400 MB to 1.5 GB",
            edgeDeploymentFeasibility = "Extremely High (runs instantaneously on any CPU or edge chip)",
            lossFunctionObjective = "Masked Language Modeling Cross-Entropy: L_MLM = -sum_{m in Masked} log P(x_m | x_unmasked)",
            flagshipImplementations = listOf("Google BERT", "RoBERTa (Meta)", "Microsoft DeBERTa-v3", "ELECTRA"),
            architecturalStrengths = listOf(
                "True bidirectional context: understands both past and future words simultaneously",
                "Gold standard for search ranking, semantic similarity, and named entity recognition (NER)",
                "Extremely fast and lightweight for high-throughput enterprise classification"
            ),
            primaryTradeoffs = listOf(
                "Cannot generate continuous long-form creative text naturally (non-autoregressive)",
                "Masking discrepancy: [MASK] token appears during training but rarely in production inference",
                "Fixed context length typically limited to 512 tokens"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Environmental Regulation Redaction Reconstruction",
                inputPrompt = "The facility must reduce sulfur dioxide emissions to prevent [MASK] rain in downwind habitats.",
                expectedOutput = "Predicted [MASK]: 'acid' (Probability: 99.4%). Left context: 'sulfur dioxide', Right context: 'rain in downwind'.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("mlm_input"), "Input Text Ingestion", "Received sentence with missing environmental term.", "x = '...prevent [MASK] rain...'", "Sequence loaded", 1),
                    SimulationStep(2, listOf("mlm_mask"), "Token Mask Location", "Identified token at position index 11 as [MASK] target.", "MaskIndex = 11", "Target: [MASK]", 1),
                    SimulationStep(3, listOf("mlm_embed"), "Tri-part Embedding Sum", "Summed Token + Segment + Learned Positional Vectors.", "E = E_tok + E_seg + E_pos in R^768", "Vector initialized", 2),
                    SimulationStep(4, listOf("mlm_left", "mlm_right"), "Dual-Horizon Context Splitting", "Left attention extracted 'sulfur dioxide emissions'. Right attention extracted 'rain in downwind habitats'.", "Contextual bounds: [0..10] & [12..16]", "Dual context isolated", 3),
                    SimulationStep(5, listOf("mlm_bi_attn"), "Bidirectional Self-Attention Matrix", "Full 12-layer DeBERTa attention matrix correlated 'sulfur dioxide' with 'rain'.", "Score = Attn(Mask, All_Tokens)", "Cross-attention Peak at 'sulfur'", 6),
                    SimulationStep(6, listOf("mlm_pred"), "Mask Prediction Logits", "Softmax over 30,522 vocabulary words produced candidate probabilities.", "Top 1: 'acid' (99.4%) | Top 2: 'toxic' (0.3%) | Top 3: 'heavy' (0.1%)", "Prediction: 'acid'", 2),
                    SimulationStep(7, listOf("mlm_feat"), "Dense Feature Representation", "Generated [CLS] classification vector summarizing environmental risk statement.", "CLS = H_0 in R^768", "Embedding exported for vector search", 1)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.MLM, nodes, connections, specs, presets)
    }

    // =========================================================================
    // 8. SAM (Segment Anything Model)
    // Pipeline: [prompt input, image input] -> [prompt encoder, image encoder]
    //           -> [image embedding, correlation] -> mask decoder -> segmentation output
    // =========================================================================
    private fun createSamModel(): SpecializedAiModel {
        val nodes = listOf(
            ArchitectureNode("sam_prompt_in", "prompt input", "Points / Bounding Box / Text", 1, -1, "prompt", "Foreground/background clicks, bounding box rectangle, or semantic text prompt", "Points [N, 2] / Box [4]"),
            ArchitectureNode("sam_img_in", "image input", "High-Resolution Image", 1, 1, "image", "Raw high-resolution terrain, aerial drone, or microscopic image (1024x1024)", "Image [3, 1024, 1024]"),
            ArchitectureNode("sam_prompt_enc", "prompt encoder", "Positional & Mask Encoder", 2, -1, "encoder", "Encodes sparse points/boxes into positional embeddings and dense masks via convolutions", "Prompt Embeddings [K, 256]"),
            ArchitectureNode("sam_img_enc", "image encoder", "Heavy ViT Backbone", 2, 1, "vision", "Massive Vision Transformer (ViT-H/L/B) processing image into 16x downsampled feature map", "Image Embedding [64, 64, 256]"),
            ArchitectureNode("sam_corr", "image embedding / correlation", "Cross-Modal Affinity Bridge", 3, 0, "correlation", "Correlates prompt query vectors against spatial image patch embeddings", "Affinity Matrix [K, 4096]"),
            ArchitectureNode("sam_decoder", "mask decoder", "Two-Way Transformer & MLP", 4, 0, "decoder", "Two-way cross-attention between prompt tokens and image embedding; predicts dynamic mask weights", "Mask Tokens + Dynamic MLP"),
            ArchitectureNode("sam_output", "segmentation output", "Multi-Mask Masks & IoU Scores", 5, 0, "segmentation", "Outputs 3 disambiguated binary segmentation masks with predicted IoU quality scores", "Masks [3, 1024, 1024] + IoU")
        )

        val connections = listOf(
            ArchitectureConnection("sam_prompt_in", "sam_prompt_enc"),
            ArchitectureConnection("sam_img_in", "sam_img_enc"),
            ArchitectureConnection("sam_prompt_enc", "sam_corr"),
            ArchitectureConnection("sam_img_enc", "sam_corr"),
            ArchitectureConnection("sam_corr", "sam_decoder"),
            ArchitectureConnection("sam_decoder", "sam_output")
        )

        val specs = ModelSpecifications(
            typicalParameters = "ViT-B (91M), ViT-L (308M), ViT-H (636M), SAM 2 (Video & Image)",
            inferenceLatency = "Image encoder: ~100ms (one-time) | Mask decoder: <5ms per click (real-time)",
            memoryFootprint = "2 GB (ViT-B) to 8 GB (ViT-H)",
            edgeDeploymentFeasibility = "High for decoder (decoder runs at 60 FPS on phone); Encoder runs on GPU/NPU",
            lossFunctionObjective = "Focal Loss + Dice Loss on Predicted Masks: L = alpha*Focal + beta*Dice + MSE(IoU)",
            flagshipImplementations = listOf("Meta SAM (Segment Anything)", "Meta SAM 2 (Unified Video & Image)", "MobileSAM", "FastSAM"),
            architecturalStrengths = listOf(
                "Zero-shot generalization to unseen objects, satellite terrain, water bodies, and micro-particles",
                "Instant interactive segmentation: once image is encoded, mask responds to clicks in under 5 milliseconds",
                "Ambiguity awareness: predicts 3 valid candidate masks (whole, part, subpart) with IoU scores"
            ),
            primaryTradeoffs = listOf(
                "Heavy initial image encoder backbone takes significant compute for 1024x1024 images",
                "Pure geometric segmentation lacks high-level semantic class names without VLM pairing",
                "Semi-transparent boundaries (e.g. smoke or fine aerosol haze) can exhibit boundary bleeding"
            )
        )

        val presets = listOf(
            ArchitecturePreset(
                title = "Wetland & Water Body Delineation",
                inputPrompt = "Point click at coordinate [x=512, y=384] (center of alluvial reservoir).",
                secondaryInput = "Satellite Multispectral Tile [1024x1024]",
                expectedOutput = "Segmented reservoir boundary: 42.8 hectares. Mask IoU Confidence: 0.96. Three valid boundary tiers generated.",
                simulationSteps = listOf(
                    SimulationStep(1, listOf("sam_prompt_in", "sam_img_in"), "Dual Input Ingestion", "Received high-resolution 1024x1024 aerial tile and user positive point click.", "Point: (512, 384) | Image: [3, 1024, 1024]", "Point & Image received", 4),
                    SimulationStep(2, listOf("sam_img_enc"), "Heavy Vision Transformer Backbone", "ViT-H encoded 1024x1024 image into 64x64x256 spatial latent feature map.", "FeatMap = ViT_H(Image)", "Shape: [1, 256, 64, 64] | Time: 88ms (cached)", 88),
                    SimulationStep(3, listOf("sam_prompt_enc"), "Positional Prompt Encoding", "Converted click coordinate into Fourier positional embedding vector.", "PE = PosEnc(512, 384) in R^256", "Prompt token ready in 2ms", 2),
                    SimulationStep(4, listOf("sam_corr"), "Cross-Modal Spatial Correlation", "Attended prompt token across all 4,096 spatial image locations.", "Affinity = Q_prompt * K_image^T", "Peak correlation at reservoir water body", 4),
                    SimulationStep(5, listOf("sam_decoder"), "Two-Way Transformer Mask Decoder", "Decoded mask queries and predicted dynamic MLP weights for linear mask synthesis.", "Masks = MLP(DecoderTokens) * TransposedFeatMap", "Decoded in 4ms", 4),
                    SimulationStep(6, listOf("sam_output"), "Disambiguated Segmentations & IoU", "Generated 3 candidate masks: Subpart (inner pool), Part (full reservoir 0.96 IoU), Whole (watershed basin 0.88 IoU).", "Final Binary Mask [1024, 1024]", "Boundary Area: 42.8 ha | IoU: 96.4%", 2)
                )
            )
        )

        return SpecializedAiModel(SpecializedAiModelType.SAM, nodes, connections, specs, presets)
    }
}
