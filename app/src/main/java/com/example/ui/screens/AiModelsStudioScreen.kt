package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArchitectureNode
import com.example.data.model.SpecializedAiModel
import com.example.data.model.SpecializedAiModelType
import com.example.data.model.SpecializedAiRegistry
import com.example.ui.components.ArchitectureFlowchartView
import com.example.ui.components.getModelIcon
import com.example.viewmodel.EdenTab
import com.example.viewmodel.EdenViewModel

enum class StudioSubTab(val title: String) {
    FLOWCHART("Architectures"),
    SIMULATOR("Neural Simulator"),
    MATRIX("Specs & Matrix"),
    GRID_ALL("All 8 Overview")
}

@Composable
fun AiModelsStudioScreen(
    viewModel: EdenViewModel,
    onNavigate: (EdenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedModelType by viewModel.selectedAiModelType.collectAsState()
    val activeStepIndex by viewModel.activeSimulationStepIndex.collectAsState()
    val isSimulating by viewModel.isSimulatingPipeline.collectAsState()
    val selectedPresetIndex by viewModel.selectedPresetIndex.collectAsState()

    var activeSubTab by remember { mutableStateOf(StudioSubTab.FLOWCHART) }
    var inspectedNode by remember { mutableStateOf<ArchitectureNode?>(null) }
    var matchupModelA by remember { mutableStateOf(SpecializedAiModelType.LLM) }
    var matchupModelB by remember { mutableStateOf(SpecializedAiModelType.SLM) }

    val currentModel = remember(selectedModelType) {
        SpecializedAiRegistry.getModel(selectedModelType)
    }

    val currentPreset = remember(currentModel, selectedPresetIndex) {
        currentModel.presets.getOrNull(selectedPresetIndex) ?: currentModel.presets.first()
    }

    val currentActiveStep = remember(currentPreset, activeStepIndex) {
        if (activeStepIndex > 0 && activeStepIndex <= currentPreset.simulationSteps.size) {
            currentPreset.simulationSteps[activeStepIndex - 1]
        } else null
    }

    val activeNodeIds = currentActiveStep?.activeNodeIds ?: emptyList()
    val darkSlateBg = Color(0xFF03141B)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(darkSlateBg)
    ) {
        // Top Banner: Title with Glowing Brain icon
        Surface(
            color = Color(0xFF07212C),
            border = BorderStroke(0.5.dp, Color(0xFF00B4D8).copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF00E5FF).copy(alpha = 0.15f),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "Neural Architecture",
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "8 Different Specialized AI Models",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 0.3.sp
                            )
                            Text(
                                text = "LLM • LCM • LAM • MoE • VLM • SLM • MLM • SAM",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    // Button to test this model directly in Ask EDEN
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF00E5FF).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f)),
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable {
                                viewModel.selectTab(EdenTab.ASK_EDEN)
                            }
                            .testTag("btn_ask_with_active_model")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Ask EDEN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(12.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Studio Navigation Sub-Tabs
                TabRow(
                    selectedTabIndex = activeSubTab.ordinal,
                    containerColor = Color.Transparent,
                    contentColor = Color(0xFF00E5FF),
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[activeSubTab.ordinal]),
                            color = Color(0xFF00E5FF),
                            height = 2.5.dp
                        )
                    }
                ) {
                    StudioSubTab.values().forEach { tab ->
                        Tab(
                            selected = activeSubTab == tab,
                            onClick = { activeSubTab = tab },
                            text = {
                                Text(
                                    text = tab.title,
                                    fontSize = 12.sp,
                                    fontWeight = if (activeSubTab == tab) FontWeight.Bold else FontWeight.Normal,
                                    color = if (activeSubTab == tab) Color(0xFF00E5FF) else Color(0xFFB0BEC5)
                                )
                            },
                            modifier = Modifier.testTag("subtab_${tab.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // Horizontal Model Selector Chips (shown on FLOWCHART & SIMULATOR tabs)
        if (activeSubTab == StudioSubTab.FLOWCHART || activeSubTab == StudioSubTab.SIMULATOR) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .background(Color(0xFF041922))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SpecializedAiModelType.values().forEach { modelType ->
                    val isSelected = selectedModelType == modelType
                    val accent = Color(modelType.accentColor)

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = if (isSelected) accent.copy(alpha = 0.25f) else Color(0xFF06232E),
                        border = BorderStroke(
                            if (isSelected) 1.5.dp else 1.dp,
                            if (isSelected) accent else Color(0xFF006978).copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { viewModel.selectAiModelType(modelType) }
                            .testTag("model_chip_${modelType.acronym.lowercase()}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                imageVector = getModelIcon(modelType),
                                contentDescription = null,
                                tint = if (isSelected) accent else Color(0xFF80DEEA),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = modelType.acronym,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else Color(0xFFB2EBF2)
                            )
                        }
                    }
                }
            }
        }

        // Tab Content
        Box(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            when (activeSubTab) {
                StudioSubTab.FLOWCHART -> {
                    FlowchartTabContent(
                        model = currentModel,
                        onSelectNode = { inspectedNode = it }
                    )
                }
                StudioSubTab.SIMULATOR -> {
                    SimulatorTabContent(
                        viewModel = viewModel,
                        model = currentModel,
                        preset = currentPreset,
                        activeStep = currentActiveStep,
                        activeStepIndex = activeStepIndex,
                        isSimulating = isSimulating,
                        activeNodeIds = activeNodeIds,
                        onSelectNode = { inspectedNode = it }
                    )
                }
                StudioSubTab.MATRIX -> {
                    MatrixTabContent(
                        matchupA = matchupModelA,
                        matchupB = matchupModelB,
                        onSelectMatchupA = { matchupModelA = it },
                        onSelectMatchupB = { matchupModelB = it },
                        onSelectModel = {
                            viewModel.selectAiModelType(it)
                            activeSubTab = StudioSubTab.FLOWCHART
                        }
                    )
                }
                StudioSubTab.GRID_ALL -> {
                    AllEightOverviewContent(
                        onSelectModel = {
                            viewModel.selectAiModelType(it)
                            activeSubTab = StudioSubTab.FLOWCHART
                        }
                    )
                }
            }
        }
    }

    // Node Inspector Modal Dialog
    if (inspectedNode != null) {
        val node = inspectedNode!!
        AlertDialog(
            onDismissRequest = { inspectedNode = null },
            containerColor = Color(0xFF092531),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFB2EBF2),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = Color(currentModel.type.accentColor).copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, Color(currentModel.type.accentColor)),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = com.example.ui.components.getNodeIcon(node.iconType),
                                contentDescription = null,
                                tint = Color(currentModel.type.accentColor),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = node.label,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "${currentModel.type.acronym} Pipeline • Stage ${node.stage}",
                            fontSize = 11.sp,
                            color = Color(currentModel.type.accentColor)
                        )
                    }
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        text = node.description,
                        fontSize = 13.sp,
                        color = Color(0xFFE0F7FA),
                        lineHeight = 18.sp
                    )

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF03171F),
                        border = BorderStroke(1.dp, Color(0xFF006978).copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "Tensor Dimension / Shape:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF80DEEA)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = node.tensorShape,
                                fontSize = 12.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color(currentModel.type.accentColor)
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { inspectedNode = null },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(currentModel.type.accentColor)),
                    modifier = Modifier.testTag("btn_close_node_inspector")
                ) {
                    Text("Close Inspector", color = Color(0xFF03141B), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun FlowchartTabContent(
    model: SpecializedAiModel,
    onSelectNode: (ArchitectureNode) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Architecture Overview Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(model.type.accentColor).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PRIMARY OBJECTIVE",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(model.type.accentColor),
                            letterSpacing = 0.5.sp
                        )
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF004D5A).copy(alpha = 0.3f)
                        ) {
                            Text(
                                text = model.type.primaryDomain,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF80DEEA),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = model.specifications.lossFunctionObjective,
                        fontSize = 12.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFE0F7FA),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Typical Scale:", fontSize = 10.sp, color = Color(0xFF80CBC4))
                            Text(model.specifications.typicalParameters, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Inference Latency:", fontSize = 10.sp, color = Color(0xFF80CBC4))
                            Text(model.specifications.inferenceLatency, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(model.type.accentColor))
                        }
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Memory Footprint:", fontSize = 10.sp, color = Color(0xFF80CBC4))
                            Text(model.specifications.memoryFootprint, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }

        // The Full Flowchart Node Graph
        item {
            ArchitectureFlowchartView(
                model = model,
                onSelectNode = onSelectNode
            )
        }

        // Flagship Implementations
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(0xFF006978).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "FLAGSHIP REAL-WORLD IMPLEMENTATIONS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF80DEEA),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        model.specifications.flagshipImplementations.forEach { impl ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF0A2B38),
                                border = BorderStroke(1.dp, Color(0xFF00838F).copy(alpha = 0.4f)),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(
                                    text = impl,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFFE0F7FA),
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SimulatorTabContent(
    viewModel: EdenViewModel,
    model: SpecializedAiModel,
    preset: com.example.data.model.ArchitecturePreset,
    activeStep: com.example.data.model.SimulationStep?,
    activeStepIndex: Int,
    isSimulating: Boolean,
    activeNodeIds: List<String>,
    onSelectNode: (ArchitectureNode) -> Unit
) {
    val totalSteps = preset.simulationSteps.size

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Preset Scenario Picker
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(model.type.accentColor).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "TEST SIMULATION SCENARIO",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(model.type.accentColor),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        model.presets.forEachIndexed { idx, p ->
                            val isChosen = preset == p
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isChosen) Color(model.type.accentColor).copy(alpha = 0.25f) else Color(0xFF06232E),
                                border = BorderStroke(1.dp, if (isChosen) Color(model.type.accentColor) else Color(0xFF006978).copy(alpha = 0.4f)),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { viewModel.selectPreset(idx) }
                            ) {
                                Text(
                                    text = p.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isChosen) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isChosen) Color.White else Color(0xFF80DEEA),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF041820),
                        border = BorderStroke(1.dp, Color(0xFF004D5A).copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text("Input Prompt:", fontSize = 10.sp, color = Color(0xFF80CBC4), fontWeight = FontWeight.Bold)
                            Text(preset.inputPrompt, fontSize = 12.sp, color = Color.White)
                            if (preset.secondaryInput != null) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text("Secondary Input:", fontSize = 10.sp, color = Color(0xFF80CBC4), fontWeight = FontWeight.Bold)
                                Text(preset.secondaryInput, fontSize = 11.sp, color = Color(0xFF80DEEA))
                            }
                        }
                    }
                }
            }
        }

        // Playback Controller Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF06202A)),
                border = BorderStroke(1.dp, Color(0xFF00B4D8).copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (activeStepIndex == 0) "PIPELINE READY" else "STEP $activeStepIndex OF $totalSteps",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(model.type.accentColor)
                        )

                        Text(
                            text = if (activeStepIndex == 0) "Press Run to execute" else "${activeStep?.latencyMs ?: 0} ms elapsed",
                            fontSize = 11.sp,
                            color = Color(0xFF80CBC4)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = { if (totalSteps > 0) activeStepIndex.toFloat() / totalSteps else 0f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = Color(model.type.accentColor),
                        trackColor = Color(0xFF003840)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.previousSimulationStep() },
                            enabled = activeStepIndex > 0 && !isSimulating,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, Color(0xFF00838F))
                        ) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Step Back", tint = Color(0xFF80DEEA))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Back", color = Color(0xFF80DEEA), fontSize = 11.sp)
                        }

                        Button(
                            onClick = { viewModel.autoPlaySimulation() },
                            enabled = !isSimulating,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(model.type.accentColor)),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("btn_auto_run_pipeline")
                        ) {
                            Icon(
                                imageVector = if (isSimulating) Icons.Default.Pause else Icons.Default.PlayArrow,
                                contentDescription = "Run",
                                tint = Color(0xFF03141B)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isSimulating) "Running..." else "Auto-Run",
                                color = Color(0xFF03141B),
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = { viewModel.nextSimulationStep() },
                            enabled = activeStepIndex < totalSteps && !isSimulating,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, Color(0xFF00838F))
                        ) {
                            Text("Next", color = Color(0xFF80DEEA), fontSize = 11.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Step Forward", tint = Color(0xFF80DEEA))
                        }

                        IconButton(
                            onClick = { viewModel.resetSimulation() },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = Color(0xFF80CBC4))
                        }
                    }
                }
            }
        }

        // Live Computation Inspector Card (when active step > 0)
        if (activeStep != null) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF041F2A)),
                    border = BorderStroke(1.5.dp, Color(model.type.accentColor)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = activeStep.stageTitle,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(model.type.accentColor).copy(alpha = 0.2f)
                            ) {
                                Text(
                                    text = "${activeStep.latencyMs} ms",
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(model.type.accentColor),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = activeStep.computationDetails,
                            fontSize = 12.sp,
                            color = Color(0xFFE0F7FA)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Mathematical Formulation
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF03141B),
                            border = BorderStroke(1.dp, Color(0xFF006978).copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Mathematical Transformation:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF80DEEA)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = activeStep.mathematicalFormula,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFF80CBC4)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Intermediate Payload Output
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF03141B),
                            border = BorderStroke(1.dp, Color(model.type.accentColor).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Intermediate Tensor Payload:",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(model.type.accentColor)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = activeStep.intermediatePayload,
                                    fontSize = 11.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Animated Flowchart
        item {
            ArchitectureFlowchartView(
                model = model,
                activeNodeIds = activeNodeIds,
                onSelectNode = onSelectNode
            )
        }
    }
}

@Composable
private fun MatrixTabContent(
    matchupA: SpecializedAiModelType,
    matchupB: SpecializedAiModelType,
    onSelectMatchupA: (SpecializedAiModelType) -> Unit,
    onSelectMatchupB: (SpecializedAiModelType) -> Unit,
    onSelectModel: (SpecializedAiModelType) -> Unit
) {
    val modelA = remember(matchupA) { SpecializedAiRegistry.getModel(matchupA) }
    val modelB = remember(matchupB) { SpecializedAiRegistry.getModel(matchupB) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Matchup Arena Header
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "DUAL-ARCHITECTURE MATCHUP ARENA",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00E5FF),
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Compare trade-offs, FLOPs, edge viability, and compute paradigms side-by-side.",
                        fontSize = 11.sp,
                        color = Color(0xFF80CBC4)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Selector Model A
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Architecture A:", fontSize = 10.sp, color = Color(0xFF80CBC4))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                SpecializedAiModelType.values().forEach { t ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (matchupA == t) Color(t.accentColor).copy(alpha = 0.3f) else Color(0xFF041B24),
                                        border = BorderStroke(1.dp, if (matchupA == t) Color(t.accentColor) else Color(0xFF006978).copy(alpha = 0.4f)),
                                        modifier = Modifier.clickable { onSelectMatchupA(t) }
                                    ) {
                                        Text(
                                            text = t.acronym,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (matchupA == t) Color.White else Color(0xFF80DEEA),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }

                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "VS",
                            tint = Color(0xFF00E5FF),
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )

                        // Selector Model B
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Architecture B:", fontSize = 10.sp, color = Color(0xFF80CBC4))
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                modifier = Modifier.horizontalScroll(rememberScrollState()),
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                SpecializedAiModelType.values().forEach { t ->
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = if (matchupB == t) Color(t.accentColor).copy(alpha = 0.3f) else Color(0xFF041B24),
                                        border = BorderStroke(1.dp, if (matchupB == t) Color(t.accentColor) else Color(0xFF006978).copy(alpha = 0.4f)),
                                        modifier = Modifier.clickable { onSelectMatchupB(t) }
                                    ) {
                                        Text(
                                            text = t.acronym,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (matchupB == t) Color.White else Color(0xFF80DEEA),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Side-by-Side Comparison Specs Table
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF061E28)),
                border = BorderStroke(1.dp, Color(0xFF006978).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Table Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Specification", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFF80CBC4), modifier = Modifier.weight(1.2f))
                        Text(modelA.type.acronym, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(modelA.type.accentColor), modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
                        Text(modelB.type.acronym, fontSize = 12.sp, fontWeight = FontWeight.Black, color = Color(modelB.type.accentColor), modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = Color(0xFF004D5A).copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    ComparisonRow("Typical Parameters", modelA.specifications.typicalParameters, modelB.specifications.typicalParameters)
                    ComparisonRow("Inference Latency", modelA.specifications.inferenceLatency, modelB.specifications.inferenceLatency)
                    ComparisonRow("Memory Footprint", modelA.specifications.memoryFootprint, modelB.specifications.memoryFootprint)
                    ComparisonRow("Edge Feasibility", modelA.specifications.edgeDeploymentFeasibility, modelB.specifications.edgeDeploymentFeasibility)
                    ComparisonRow("Primary Modality", modelA.type.primaryDomain, modelB.type.primaryDomain)
                }
            }
        }

        // All 8 Specifications Summary Cards
        item {
            Text(
                text = "ALL 8 ARCHITECTURAL SPECIFICATIONS",
                fontSize = 12.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                letterSpacing = 0.5.sp
            )
        }

        items(SpecializedAiRegistry.allModels) { model ->
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(model.type.accentColor).copy(alpha = 0.35f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelectModel(model.type) }
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = model.type.acronym,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(model.type.accentColor)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = model.type.fullName,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }

                        Text(
                            text = model.specifications.typicalParameters,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = Color(0xFF80DEEA)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = model.type.tagline,
                        fontSize = 11.sp,
                        color = Color(0xFFB2EBF2)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Edge: ${model.specifications.edgeDeploymentFeasibility.take(15)}",
                            fontSize = 10.sp,
                            color = Color(0xFF80CBC4)
                        )
                        Text(
                            text = "Latency: ${model.specifications.inferenceLatency}",
                            fontSize = 10.sp,
                            color = Color(model.type.accentColor)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ComparisonRow(label: String, valA: String, valB: String) {
    Column(modifier = Modifier.padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Medium, color = Color(0xFF80CBC4), modifier = Modifier.weight(1.2f))
            Text(valA, fontSize = 10.sp, color = Color.White, modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
            Text(valB, fontSize = 10.sp, color = Color.White, modifier = Modifier.weight(1.4f), textAlign = TextAlign.Center)
        }
        Spacer(modifier = Modifier.height(4.dp))
        HorizontalDivider(color = Color(0xFF003840).copy(alpha = 0.3f))
    }
}

@Composable
private fun AllEightOverviewContent(
    onSelectModel: (SpecializedAiModelType) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF07212C)),
                border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "COMPLETE 8 SPECIALIZED AI MODEL TAXONOMY",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF00E5FF),
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Direct visual translation of the technical architecture blueprint. Tap any model to view its detailed node pipeline flowchart and step-by-step simulator.",
                        fontSize = 11.sp,
                        color = Color(0xFFB2EBF2),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        items(SpecializedAiRegistry.allModels) { model ->
            ArchitectureFlowchartView(
                model = model,
                modifier = Modifier.clickable { onSelectModel(model.type) }
            )
        }
    }
}
