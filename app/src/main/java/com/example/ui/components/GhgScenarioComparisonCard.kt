package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CompareArrows
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.GhgScenario
import com.example.calculator.GhgScenarioComparison
import com.example.calculator.GhgUnitSystem
import com.example.calculator.GhgUnitConverter
import com.example.util.GhgCsvExporter
import java.util.Locale

@Composable
fun GhgScenarioComparisonCard(
    comparison: GhgScenarioComparison,
    savedScenarios: List<GhgScenario>,
    onSelectBaseline: (String) -> Unit,
    onLoadScenarioInputs: (GhgScenario) -> Unit,
    onDismiss: () -> Unit,
    unitSystem: GhgUnitSystem = GhgUnitSystem.METRIC,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var baselineDropdownExpanded by remember { mutableStateOf(false) }
    var showInputDeltas by remember { mutableStateOf(true) }

    val isReduction = comparison.isNetReduction
    val isNeutral = comparison.isNeutral

    val deltaThemeColor = when {
        isReduction -> Color(0xFF2E7D32) // Forest Emerald Green
        isNeutral -> Color(0xFF1976D2)   // Informational Blue
        else -> Color(0xFFD32F2F)        // Amber / Brick Red
    }

    val deltaBgColor = deltaThemeColor.copy(alpha = 0.12f)

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, deltaThemeColor.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
            .testTag("ghg_comparison_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CompareArrows,
                            contentDescription = "Side-by-side comparison",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Side-by-Side GHG Comparison",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Scenario Benchmarking & Variance Analysis",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .size(32.dp)
                        .testTag("dismiss_comparison_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Dismiss comparison",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Baseline Selector Dropdown Bar
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { baselineDropdownExpanded = true }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                        .testTag("select_baseline_scenario_trigger"),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmark,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(16.dp)
                        )
                        Column {
                            Text(
                                text = "BENCHMARK BASELINE:",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = comparison.baseline.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                maxLines = 1,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Switch",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Switch baseline",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = baselineDropdownExpanded,
                        onDismissRequest = { baselineDropdownExpanded = false }
                    ) {
                        savedScenarios.forEach { scenario ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(
                                            text = scenario.name,
                                            fontWeight = if (scenario.id == comparison.baseline.id) FontWeight.Bold else FontWeight.Normal
                                        )
                                        Text(
                                            text = "%.2f t CO₂e • %s".format(scenario.result.totalTonnes, scenario.shortDate),
                                            fontSize = 11.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                },
                                onClick = {
                                    onSelectBaseline(scenario.id)
                                    baselineDropdownExpanded = false
                                },
                                leadingIcon = {
                                    if (scenario.id == comparison.baseline.id) {
                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = MaterialTheme.colorScheme.primary)
                                    } else {
                                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = MaterialTheme.colorScheme.outline)
                                    }
                                }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Primary Highlight Variance Banner
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(deltaBgColor)
                    .border(1.dp, deltaThemeColor.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                    .padding(14.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when {
                                isReduction -> "NET DECARBONIZATION"
                                isNeutral -> "IDENTICAL FOOTPRINT"
                                else -> "EMISSIONS INCREASE"
                            },
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = deltaThemeColor,
                            letterSpacing = 0.5.sp
                        )

                        Text(
                            text = if (isReduction) "SBTi Trajectory Tracked" else "Action Required",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = deltaThemeColor
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(
                                    imageVector = if (isReduction) Icons.Default.TrendingDown else if (isNeutral) Icons.Default.CompareArrows else Icons.Default.TrendingUp,
                                    contentDescription = null,
                                    tint = deltaThemeColor,
                                    modifier = Modifier.size(24.dp)
                                )
                                Text(
                                    text = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                                        val deltaUsTons = comparison.deltaTotalTonnes * GhgUnitConverter.SHORT_TONS_PER_METRIC_TONNE
                                        "%s%.2f US tons CO₂e".format(if (deltaUsTons > 0) "+" else "", deltaUsTons)
                                    } else {
                                        "%s%.2f t CO₂e".format(if (comparison.deltaTotalTonnes > 0) "+" else "", comparison.deltaTotalTonnes)
                                    },
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontFamily = FontFamily.Monospace,
                                    color = deltaThemeColor
                                )
                            }
                            Text(
                                text = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                                    val deltaLbs = comparison.deltaTotalKg * GhgUnitConverter.LBS_PER_KG
                                    "Net variance: %s%.1f lbs CO₂e (%.1f kg)".format(if (deltaLbs > 0) "+" else "", deltaLbs, comparison.deltaTotalKg)
                                } else {
                                    "Net variance: %s%.1f kg CO₂e (%.2f t)".format(if (comparison.deltaTotalKg > 0) "+" else "", comparison.deltaTotalKg, comparison.deltaTotalTonnes)
                                },
                                fontSize = 11.sp,
                                color = deltaThemeColor.copy(alpha = 0.9f)
                            )
                        }

                        // Percentage Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(deltaThemeColor)
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "%s%.1f%%".format(if (comparison.deltaTotalPercent > 0) "+" else "", comparison.deltaTotalPercent),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = comparison.mitigationTrajectoryAssessment,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 15.sp
                    )

                    if (isReduction) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Key Driver: ${comparison.dominantAbatementDriver}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = deltaThemeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Side-by-Side Metrics Table
            Text(
                text = "Detailed Scope-by-Scope Breakdown",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(6.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    // Table Header
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Scope Metric", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1.3f))
                        Text("Baseline", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                        Text("Current", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                        Text("Variance", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.weight(1f))
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(6.dp))

                    // Row: Scope 1
                    ComparisonMetricRow(
                        title = "Scope 1 (Direct)",
                        baselineVal = "%.0f kg".format(comparison.baseline.result.scope1Kg),
                        currentVal = "%.0f kg".format(comparison.current.result.scope1Kg),
                        deltaKg = comparison.deltaScope1Kg,
                        deltaPercent = comparison.deltaScope1Percent
                    )

                    // Row: Scope 2
                    ComparisonMetricRow(
                        title = "Scope 2 (Grid)",
                        baselineVal = "%.0f kg".format(comparison.baseline.result.scope2Kg),
                        currentVal = "%.0f kg".format(comparison.current.result.scope2Kg),
                        deltaKg = comparison.deltaScope2Kg,
                        deltaPercent = comparison.deltaScope2Percent
                    )

                    // Row: Scope 3
                    ComparisonMetricRow(
                        title = "Scope 3 (Logistics)",
                        baselineVal = "%.0f kg".format(comparison.baseline.result.scope3Kg),
                        currentVal = "%.0f kg".format(comparison.current.result.scope3Kg),
                        deltaKg = comparison.deltaScope3Kg,
                        deltaPercent = comparison.deltaScope3Percent
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(6.dp))

                    // Total Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TOTAL GHG",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1.3f)
                        )
                        Text(
                            text = "%.2f t".format(comparison.baseline.result.totalTonnes),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "%.2f t".format(comparison.current.result.totalTonnes),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f)
                        )
                        Text(
                            text = "%s%.1f%%".format(if (comparison.deltaTotalPercent > 0) "+" else "", comparison.deltaTotalPercent),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = deltaThemeColor,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Expandable Activity Parameter Comparison
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showInputDeltas = !showInputDeltas }
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Activity Input Variations",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (showInputDeltas) "Hide" else "Show Details",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            AnimatedVisibility(
                visible = showInputDeltas,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    val baseFuelQtyDisplay = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                        val q = GhgUnitConverter.convertFuelFromMetric(comparison.baseline.inputs.fuelQuantity, comparison.baseline.inputs.fuelType, GhgUnitSystem.IMPERIAL)
                        "%.0f %s".format(q, GhgUnitConverter.getFuelUnit(comparison.baseline.inputs.fuelType, GhgUnitSystem.IMPERIAL))
                    } else {
                        "%.0f %s".format(comparison.baseline.inputs.fuelQuantity, comparison.baseline.inputs.fuelType.unit)
                    }
                    val curFuelQtyDisplay = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                        val q = GhgUnitConverter.convertFuelFromMetric(comparison.current.inputs.fuelQuantity, comparison.current.inputs.fuelType, GhgUnitSystem.IMPERIAL)
                        "%.0f %s".format(q, GhgUnitConverter.getFuelUnit(comparison.current.inputs.fuelType, GhgUnitSystem.IMPERIAL))
                    } else {
                        "%.0f %s".format(comparison.current.inputs.fuelQuantity, comparison.current.inputs.fuelType.unit)
                    }

                    InputVarianceItem(
                        icon = Icons.Default.LocalGasStation,
                        label = "Fuel Source & Qty",
                        baselineText = "${comparison.baseline.inputs.fuelType.displayName}: $baseFuelQtyDisplay",
                        currentText = "${comparison.current.inputs.fuelType.displayName}: $curFuelQtyDisplay"
                    )

                    InputVarianceItem(
                        icon = Icons.Default.ElectricBolt,
                        label = "Electricity & Grid",
                        baselineText = "${comparison.baseline.inputs.gridRegion.displayName}: %.0f kWh".format(comparison.baseline.inputs.electricityKwh),
                        currentText = "${comparison.current.inputs.gridRegion.displayName}: %.0f kWh".format(comparison.current.inputs.electricityKwh)
                    )

                    val baseTransDisplay = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                        val t = GhgUnitConverter.convertTransportFromMetric(comparison.baseline.inputs.transportVolume, comparison.baseline.inputs.transportMode, GhgUnitSystem.IMPERIAL)
                        "%.0f %s".format(t, GhgUnitConverter.getTransportUnit(comparison.baseline.inputs.transportMode, GhgUnitSystem.IMPERIAL))
                    } else {
                        "%.0f %s".format(comparison.baseline.inputs.transportVolume, comparison.baseline.inputs.transportMode.unit)
                    }
                    val curTransDisplay = if (unitSystem == GhgUnitSystem.IMPERIAL) {
                        val t = GhgUnitConverter.convertTransportFromMetric(comparison.current.inputs.transportVolume, comparison.current.inputs.transportMode, GhgUnitSystem.IMPERIAL)
                        "%.0f %s".format(t, GhgUnitConverter.getTransportUnit(comparison.current.inputs.transportMode, GhgUnitSystem.IMPERIAL))
                    } else {
                        "%.0f %s".format(comparison.current.inputs.transportVolume, comparison.current.inputs.transportMode.unit)
                    }

                    InputVarianceItem(
                        icon = Icons.Default.LocalShipping,
                        label = "Freight & Travel",
                        baselineText = "${comparison.baseline.inputs.transportMode.displayName}: $baseTransDisplay",
                        currentText = "${comparison.current.inputs.transportMode.displayName}: $curTransDisplay"
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        onLoadScenarioInputs(comparison.baseline)
                        Toast.makeText(context, "Loaded '${comparison.baseline.name}' inputs into calculator", Toast.LENGTH_SHORT).show()
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("load_baseline_inputs_button"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Load Baseline", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = {
                        val csv = comparison.generateComparisonCsv()
                        val file = GhgCsvExporter.createExportFile(context, csv, "eden_scenario_comparison")
                        val shareIntent = GhgCsvExporter.createShareIntent(context, file)
                        context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Scenario Comparison CSV"))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(42.dp)
                        .testTag("share_comparison_report_button"),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Share CSV", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun ComparisonMetricRow(
    title: String,
    baselineVal: String,
    currentVal: String,
    deltaKg: Double,
    deltaPercent: Double
) {
    val isRed = deltaKg < -0.01
    val isInc = deltaKg > 0.01
    val deltaColor = when {
        isRed -> Color(0xFF2E7D32)
        isInc -> Color(0xFFD32F2F)
        else -> MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1.3f)
        )
        Text(
            text = baselineVal,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = currentVal,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = "%s%.0f kg (%s%.1f%%)".format(
                if (deltaKg > 0) "+" else "",
                deltaKg,
                if (deltaPercent > 0) "+" else "",
                deltaPercent
            ),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = deltaColor,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InputVarianceItem(
    icon: ImageVector,
    label: String,
    baselineText: String,
    currentText: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Base: $baselineText", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text("➔", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary)
                Text("Curr: $currentText", fontSize = 10.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}
