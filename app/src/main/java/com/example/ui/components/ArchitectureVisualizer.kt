package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.AltRoute
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CropFree
import androidx.compose.material.icons.filled.DataObject
import androidx.compose.material.icons.filled.DeviceHub
import androidx.compose.material.icons.filled.FilterCenterFocus
import androidx.compose.material.icons.filled.FindInPage
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ArchitectureNode
import com.example.data.model.SpecializedAiModel
import com.example.data.model.SpecializedAiModelType

/**
 * Visual Flowchart Component that renders the exact node architectures
 * depicted in the "8 Different Specialized AI Models" infographic:
 * LLM, LCM, LAM, MoE, VLM, SLM, MLM, SAM.
 */
@Composable
fun ArchitectureFlowchartView(
    model: SpecializedAiModel,
    activeNodeIds: List<String> = emptyList(),
    onSelectNode: (ArchitectureNode) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val modelColor = Color(model.type.accentColor)
    val darkTealBg = Color(0xFF061B24)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = darkTealBg),
        border = BorderStroke(1.dp, modelColor.copy(alpha = 0.4f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("architecture_flowchart_card_${model.type.acronym.lowercase()}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header with Model Identity
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = modelColor.copy(alpha = 0.2f),
                        border = BorderStroke(1.dp, modelColor.copy(alpha = 0.6f)),
                        modifier = Modifier.padding(end = 10.dp)
                    ) {
                        Text(
                            text = model.type.acronym,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            fontFamily = FontFamily.Monospace,
                            color = modelColor,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Column {
                        Text(
                            text = model.type.fullName,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = model.type.tagline,
                            fontSize = 11.sp,
                            color = Color(0xFF80DEEA)
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = modelColor.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, modelColor.copy(alpha = 0.3f)),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getModelIcon(model.type),
                            contentDescription = model.type.acronym,
                            tint = modelColor,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFF004D5A).copy(alpha = 0.6f))
            Spacer(modifier = Modifier.height(16.dp))

            // Render Stages Sequentially
            val groupedByStage = model.nodes.groupBy { it.stage }.toSortedMap()

            groupedByStage.forEach { (stage, nodesInStage) ->
                // Draw nodes in this stage (single, dual input, or multi-branch quad)
                if (nodesInStage.size == 1) {
                    val node = nodesInStage.first()
                    SingleNodeView(
                        node = node,
                        accentColor = modelColor,
                        isActive = activeNodeIds.contains(node.id),
                        onClick = { onSelectNode(node) }
                    )
                } else {
                    // Multi-branch row (e.g. VLM dual inputs, MoE 4 experts, LCM 3 branches)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        nodesInStage.forEach { node ->
                            Box(modifier = Modifier.weight(1f, fill = false)) {
                                MultiBranchNodeView(
                                    node = node,
                                    accentColor = modelColor,
                                    isActive = activeNodeIds.contains(node.id),
                                    onClick = { onSelectNode(node) }
                                )
                            }
                        }
                    }
                }

                // Render Connection Down Arrow if not the final stage
                if (stage < groupedByStage.lastKey()) {
                    FlowDownArrow(accentColor = modelColor, isAnimated = activeNodeIds.any { id ->
                        nodesInStage.any { it.id == id }
                    })
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "Tap any neural node to inspect tensor dimensions & computational operation",
                fontSize = 11.sp,
                color = Color(0xFF80CBC4),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun SingleNodeView(
    node: ArchitectureNode,
    accentColor: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    val containerBg = if (isActive) Color(0xFF003840) else Color(0xFF092531)
    val borderClr = if (isActive) accentColor else Color(0xFF006978).copy(alpha = 0.5f)

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerBg,
        border = BorderStroke(if (isActive) 2.dp else 1.dp, borderClr),
        modifier = Modifier
            .fillMaxWidth(0.92f)
            .scale(if (isActive) pulseScale else 1f)
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .testTag("node_${node.id}")
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isActive) accentColor.copy(alpha = 0.25f) else Color(0xFF06333D),
                    border = BorderStroke(1.dp, if (isActive) accentColor else Color(0xFF00838F).copy(alpha = 0.4f)),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = getNodeIcon(node.iconType),
                            contentDescription = node.label,
                            tint = if (isActive) accentColor else Color(0xFF80DEEA),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = node.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isActive) Color.White else Color(0xFFE0F7FA)
                    )
                    Text(
                        text = node.sublabel,
                        fontSize = 11.sp,
                        color = if (isActive) accentColor else Color(0xFF80CBC4)
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = Color(0xFF041A22),
                border = BorderStroke(1.dp, Color(0xFF004D5A).copy(alpha = 0.5f))
            ) {
                Text(
                    text = node.tensorShape,
                    fontSize = 10.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = if (isActive) accentColor else Color(0xFF4DD0E1),
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun MultiBranchNodeView(
    node: ArchitectureNode,
    accentColor: Color,
    isActive: Boolean,
    onClick: () -> Unit
) {
    val containerBg = if (isActive) Color(0xFF003840) else Color(0xFF092531)
    val borderClr = if (isActive) accentColor else Color(0xFF006978).copy(alpha = 0.5f)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = containerBg,
        border = BorderStroke(if (isActive) 2.dp else 1.dp, borderClr),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag("node_${node.id}")
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = if (isActive) accentColor.copy(alpha = 0.25f) else Color(0xFF06333D),
                modifier = Modifier.size(26.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = getNodeIcon(node.iconType),
                        contentDescription = node.label,
                        tint = if (isActive) accentColor else Color(0xFF80DEEA),
                        modifier = Modifier.size(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = node.label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) Color.White else Color(0xFFE0F7FA),
                textAlign = TextAlign.Center,
                maxLines = 2
            )

            Text(
                text = node.sublabel,
                fontSize = 9.sp,
                color = if (isActive) accentColor else Color(0xFF80CBC4),
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun FlowDownArrow(
    accentColor: Color,
    isAnimated: Boolean
) {
    Box(
        modifier = Modifier
            .padding(vertical = 4.dp)
            .size(18.dp),
        contentAlignment = Alignment.Center
    ) {
        // Vertical dashed line / glowing cyan pip
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(18.dp)
                .background(if (isAnimated) accentColor else Color(0xFF00838F).copy(alpha = 0.4f))
        )
    }
}

fun getNodeIcon(iconType: String): ImageVector {
    return when (iconType.lowercase()) {
        "input" -> Icons.Default.PlayArrow
        "token" -> Icons.Default.Grain
        "embedding" -> Icons.Default.Layers
        "transformer" -> Icons.Default.Psychology
        "output" -> Icons.Default.Check
        "segment" -> Icons.Default.CropFree
        "sonar" -> Icons.Default.Hub
        "diffusion" -> Icons.Default.AutoAwesome
        "pattern" -> Icons.Default.DeviceHub
        "hidden" -> Icons.Default.DataObject
        "quantize" -> Icons.Default.Tune
        "perception" -> Icons.Default.Visibility
        "intent" -> Icons.Default.Check
        "planner" -> Icons.Default.AccountTree
        "neuro" -> Icons.Default.Bolt
        "memory" -> Icons.Default.Memory
        "action" -> Icons.Default.Bolt
        "feedback" -> Icons.Default.Speed
        "router" -> Icons.Default.AltRoute
        "expert" -> Icons.Default.Psychology
        "topk" -> Icons.Default.FilterCenterFocus
        "weighted" -> Icons.Default.Tune
        "image" -> Icons.Default.Image
        "text" -> Icons.Default.TextFields
        "vision" -> Icons.Default.Visibility
        "projection" -> Icons.Default.Layers
        "processor" -> Icons.Default.Hub
        "edge" -> Icons.Default.Smartphone
        "mask" -> Icons.Default.CropFree
        "left" -> Icons.AutoMirrored.Filled.ArrowForward
        "right" -> Icons.AutoMirrored.Filled.ArrowForward
        "biattn" -> Icons.Default.DeviceHub
        "prediction" -> Icons.Default.FindInPage
        "feature" -> Icons.Default.Layers
        "prompt" -> Icons.Default.SelectAll
        "encoder" -> Icons.Default.Layers
        "correlation" -> Icons.Default.Hub
        "decoder" -> Icons.Default.Psychology
        "segmentation" -> Icons.Default.CropFree
        else -> Icons.Default.Psychology
    }
}

fun getModelIcon(type: SpecializedAiModelType): ImageVector {
    return when (type) {
        SpecializedAiModelType.LLM -> Icons.Default.Psychology
        SpecializedAiModelType.LCM -> Icons.Default.Hub
        SpecializedAiModelType.LAM -> Icons.Default.Bolt
        SpecializedAiModelType.MOE -> Icons.Default.AltRoute
        SpecializedAiModelType.VLM -> Icons.Default.Visibility
        SpecializedAiModelType.SLM -> Icons.Default.Smartphone
        SpecializedAiModelType.MLM -> Icons.Default.FindInPage
        SpecializedAiModelType.SAM -> Icons.Default.CropFree
    }
}
