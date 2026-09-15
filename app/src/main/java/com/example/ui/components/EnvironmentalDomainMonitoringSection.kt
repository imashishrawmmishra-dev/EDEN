package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Hearing
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.EnvironmentalHourlyPoint
import com.example.data.model.NoiseMonitoringStation
import com.example.data.model.NoiseParameterType
import com.example.data.model.ParameterStatus
import com.example.data.model.SoilMonitoringStation
import com.example.data.model.SoilParameterType
import com.example.data.model.WaterMonitoringStation
import com.example.data.model.WaterParameterType
import com.example.viewmodel.EdenViewModel
import com.example.viewmodel.EdenViewModel.MonitoringDomainTab

/**
 * Complete Multi-Domain Environmental Monitoring Suite.
 * Covers Air Quality, Water Quality, Noise & Acoustics, and Soil & Ecology
 * with real-time stations, 24-hour interactive Canvas charts, WHO/EPA/OSHA standards,
 * and threshold breach alerting.
 */
@Composable
fun EnvironmentalDomainMonitoringSection(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val selectedTab by viewModel.selectedMonitoringDomainTab.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- DOMAIN SELECTOR CHIPS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MonitoringDomainTab.values().forEach { tab ->
                val isSelected = selectedTab == tab
                val icon = when (tab) {
                    MonitoringDomainTab.AIR -> Icons.Default.Air
                    MonitoringDomainTab.WATER -> Icons.Default.WaterDrop
                    MonitoringDomainTab.NOISE -> Icons.Default.Hearing
                    MonitoringDomainTab.SOIL -> Icons.Default.Eco
                }
                val tintColor = when (tab) {
                    MonitoringDomainTab.AIR -> Color(0xFF0F6E43)
                    MonitoringDomainTab.WATER -> Color(0xFF0288D1)
                    MonitoringDomainTab.NOISE -> Color(0xFFE65100)
                    MonitoringDomainTab.SOIL -> Color(0xFF5D4037)
                }

                FilterChip(
                    selected = isSelected,
                    onClick = { viewModel.selectMonitoringDomainTab(tab) },
                    label = {
                        Text(
                            text = tab.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = icon,
                            contentDescription = tab.label,
                            tint = if (isSelected) tintColor else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = tintColor.copy(alpha = 0.14f),
                        selectedLabelColor = tintColor
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("domain_chip_${tab.name.lowercase()}")
                )
            }
        }

        // --- CONTENT OF SELECTED DOMAIN ---
        when (selectedTab) {
            MonitoringDomainTab.AIR -> {
                AirQualityMonitoringSection(viewModel = viewModel)
            }
            MonitoringDomainTab.WATER -> {
                WaterQualityMonitoringSection(viewModel = viewModel)
            }
            MonitoringDomainTab.NOISE -> {
                NoiseQualityMonitoringSection(viewModel = viewModel)
            }
            MonitoringDomainTab.SOIL -> {
                SoilQualityMonitoringSection(viewModel = viewModel)
            }
        }
    }
}

// =========================================================================
// 1. WATER QUALITY MONITORING SECTION
// =========================================================================

@Composable
fun WaterQualityMonitoringSection(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stations by viewModel.waterStations.collectAsState()
    val selectedStationId by viewModel.selectedWaterStationId.collectAsState()
    val selectedParam by viewModel.selectedWaterParameter.collectAsState()
    val selectedHourlyIndex by viewModel.selectedWaterHourlyIndex.collectAsState()
    val alertsEnabled by viewModel.waterAlertsEnabled.collectAsState()
    val dismissedStationId by viewModel.dismissedWaterStation.collectAsState()

    val currentStation = stations.find { it.id == selectedStationId } ?: stations.first()
    val historyPoints = currentStation.hourlyHistory[selectedParam] ?: emptyList()
    val activePoint = selectedHourlyIndex?.let { historyPoints.getOrNull(it) } ?: historyPoints.lastOrNull()

    val hasThresholdBreach = currentStation.isExceeded
    val isDismissed = dismissedStationId == currentStation.id

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.5f) else Color(0xFF0288D1).copy(alpha = 0.3f),
                RoundedCornerShape(22.dp)
            )
            .testTag("water_quality_monitoring_section")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFF0288D1).copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.WaterDrop,
                                contentDescription = "Water Quality",
                                tint = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF0288D1),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Water Quality Telemetry",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Text(
                            text = "Continuous Sonde • EPA/WHO limits: DO ≥5.0 mg/L, BOD ≤5.0",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val newState = !alertsEnabled
                            viewModel.toggleWaterAlerts(newState)
                            Toast.makeText(
                                context,
                                if (newState) "Water threshold alerts enabled" else "Threshold alerts muted",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_toggle_water_alerts")
                    ) {
                        Icon(
                            imageVector = if (alertsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Toggle Water Alerts",
                            tint = if (alertsEnabled) {
                                if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF0288D1)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.refreshWaterReadings()
                            Toast.makeText(context, "Water sensors recalibrated & synced", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_refresh_water")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = Color(0xFF0288D1),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Station Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stations.forEach { station ->
                    val isSelected = station.id == selectedStationId
                    val stationBreach = station.isExceeded
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectWaterStation(station.id) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = station.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (stationBreach) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚠️", fontSize = 10.sp)
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (stationBreach && isSelected) Color(0xFFD32F2F) else Color(0xFF0288D1),
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (stationBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFF0288D1).copy(alpha = 0.15f),
                            selectedLabelColor = if (stationBreach) Color(0xFFD32F2F) else Color(0xFF0288D1)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Station Subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentStation.locationType} (${currentStation.distanceKm} km away)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF0288D1)
                )
                Text(
                    text = currentStation.lastUpdated,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Threshold Breach Warning Card
            if (hasThresholdBreach && alertsEnabled && !isDismissed) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFD32F2F).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Water Quality Standard Exceedance", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFD32F2F))
                            }
                            IconButton(onClick = { viewModel.dismissWaterAlert(currentStation.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Station '${currentStation.name}' has breached surface water safety thresholds: DO is ${currentStation.dissolvedOxygen} mg/L (min 5.0) or BOD is ${currentStation.bod} mg/L (max 5.0). Immediate aeration and source tracing required.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Parameter KPI Selector Grid
            Text("Core Water Parameters (Tap to Chart):", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WaterParameterType.values().forEach { param ->
                    val value = currentStation.getValue(param)
                    val status = param.evaluateStatus(value)
                    val isSelected = selectedParam == param

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFF0288D1).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF0288D1) else Color.Transparent
                        ),
                        modifier = Modifier
                            .width(110.dp)
                            .clickable { viewModel.selectWaterParameter(param) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = param.code, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = "%.1f %s".format(value, param.unit),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = status.color
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = status.label, fontSize = 9.sp, color = status.color, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Interactive 24h Time-series Chart
            Text(
                text = "24h Trend: ${selectedParam.fullName} (${selectedParam.unit})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            UniversalTimeSeriesChart(
                points = historyPoints,
                unit = selectedParam.unit,
                standardLimit = selectedParam.standardLimit,
                selectedIndex = selectedHourlyIndex,
                onPointSelected = { viewModel.selectWaterHourlyPoint(it) },
                lineColor = Color(0xFF0288D1)
            )

            // Ecological Advisory Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = activePoint?.status?.color?.copy(alpha = 0.1f) ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = activePoint?.status?.color ?: Color(0xFF0288D1), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Ecological Status: ${activePoint?.status?.label ?: "Standard"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = activePoint?.status?.color ?: MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedParam.environmentalImpact,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Guideline: ${selectedParam.standardReference} | Method: ${selectedParam.measurementMethod}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// =========================================================================
// 2. NOISE & ACOUSTICS MONITORING SECTION
// =========================================================================

@Composable
fun NoiseQualityMonitoringSection(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stations by viewModel.noiseStations.collectAsState()
    val selectedStationId by viewModel.selectedNoiseStationId.collectAsState()
    val selectedParam by viewModel.selectedNoiseParameter.collectAsState()
    val selectedHourlyIndex by viewModel.selectedNoiseHourlyIndex.collectAsState()
    val alertsEnabled by viewModel.noiseAlertsEnabled.collectAsState()
    val dismissedStationId by viewModel.dismissedNoiseStation.collectAsState()

    val currentStation = stations.find { it.id == selectedStationId } ?: stations.first()
    val historyPoints = currentStation.hourlyHistory[selectedParam] ?: emptyList()
    val activePoint = selectedHourlyIndex?.let { historyPoints.getOrNull(it) } ?: historyPoints.lastOrNull()

    val hasThresholdBreach = currentStation.isExceeded
    val isDismissed = dismissedStationId == currentStation.id

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.5f) else Color(0xFFE65100).copy(alpha = 0.3f),
                RoundedCornerShape(22.dp)
            )
            .testTag("noise_quality_monitoring_section")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFFE65100).copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Hearing,
                                contentDescription = "Noise Acoustics",
                                tint = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFFE65100),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Acoustics & Sound Monitoring",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Text(
                            text = "IEC 61672 Class 1 • WHO/OSHA limits: Leq,day ≤55, Lmax ≤85",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val newState = !alertsEnabled
                            viewModel.toggleNoiseAlerts(newState)
                            Toast.makeText(
                                context,
                                if (newState) "Noise threshold alerts enabled" else "Noise alerts muted",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_toggle_noise_alerts")
                    ) {
                        Icon(
                            imageVector = if (alertsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Toggle Noise Alerts",
                            tint = if (alertsEnabled) {
                                if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFFE65100)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.refreshNoiseReadings()
                            Toast.makeText(context, "Acoustic meters calibrated & synced", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_refresh_noise")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = Color(0xFFE65100),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Station Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stations.forEach { station ->
                    val isSelected = station.id == selectedStationId
                    val stationBreach = station.isExceeded
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectNoiseStation(station.id) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = station.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (stationBreach) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚠️", fontSize = 10.sp)
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (stationBreach && isSelected) Color(0xFFD32F2F) else Color(0xFFE65100),
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (stationBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFFE65100).copy(alpha = 0.15f),
                            selectedLabelColor = if (stationBreach) Color(0xFFD32F2F) else Color(0xFFE65100)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Station Subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentStation.locationType} (${currentStation.distanceKm} km away)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFFE65100)
                )
                Text(
                    text = currentStation.lastUpdated,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Breach warning banner
            if (hasThresholdBreach && alertsEnabled && !isDismissed) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFD32F2F).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Acoustic Pressure Threshold Breach", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFD32F2F))
                            }
                            IconButton(onClick = { viewModel.dismissNoiseAlert(currentStation.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Station '${currentStation.name}' records Leq,day ${currentStation.leqDay} dB(A) (WHO max 55) or Peak Lmax ${currentStation.lmaxPeak} dB(A) (OSHA 85). Acoustic screening or hearing protection required.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Parameter KPI Selector Grid
            Text("Acoustic Parameters (Tap to Chart):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                NoiseParameterType.values().forEach { param ->
                    val value = currentStation.getValue(param)
                    val status = param.evaluateStatus(value)
                    val isSelected = selectedParam == param

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFFE65100).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFFE65100) else Color.Transparent
                        ),
                        modifier = Modifier
                            .width(115.dp)
                            .clickable { viewModel.selectNoiseParameter(param) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = param.code, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = "%.1f %s".format(value, param.unit),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = status.color
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = status.label, fontSize = 9.sp, color = status.color, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Interactive 24h Time-series Chart
            Text(
                text = "24h Trend: ${selectedParam.fullName} (${selectedParam.unit})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            UniversalTimeSeriesChart(
                points = historyPoints,
                unit = selectedParam.unit,
                standardLimit = selectedParam.standardLimit,
                selectedIndex = selectedHourlyIndex,
                onPointSelected = { viewModel.selectNoiseHourlyPoint(it) },
                lineColor = Color(0xFFE65100)
            )

            // Health Advisory Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = activePoint?.status?.color?.copy(alpha = 0.1f) ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = activePoint?.status?.color ?: Color(0xFFE65100), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Auditory Impact: ${activePoint?.status?.label ?: "Normal"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = activePoint?.status?.color ?: MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedParam.healthImpact,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Guideline: ${selectedParam.standardReference} | Standard: ${selectedParam.measurementMethod}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// =========================================================================
// 3. SOIL & ECOLOGY MONITORING SECTION
// =========================================================================

@Composable
fun SoilQualityMonitoringSection(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stations by viewModel.soilStations.collectAsState()
    val selectedStationId by viewModel.selectedSoilStationId.collectAsState()
    val selectedParam by viewModel.selectedSoilParameter.collectAsState()
    val selectedHourlyIndex by viewModel.selectedSoilHourlyIndex.collectAsState()
    val alertsEnabled by viewModel.soilAlertsEnabled.collectAsState()
    val dismissedStationId by viewModel.dismissedSoilStation.collectAsState()

    val currentStation = stations.find { it.id == selectedStationId } ?: stations.first()
    val historyPoints = currentStation.hourlyHistory[selectedParam] ?: emptyList()
    val activePoint = selectedHourlyIndex?.let { historyPoints.getOrNull(it) } ?: historyPoints.lastOrNull()

    val hasThresholdBreach = currentStation.isExceeded
    val isDismissed = dismissedStationId == currentStation.id

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.5f) else Color(0xFF5D4037).copy(alpha = 0.3f),
                RoundedCornerShape(22.dp)
            )
            .testTag("soil_quality_monitoring_section")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFF5D4037).copy(alpha = 0.15f),
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Eco,
                                contentDescription = "Soil Ecology",
                                tint = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF5D4037),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Soil & Ecology Health",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = CircleShape,
                                color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Text(
                            text = "TDR Probes & ICP-MS • FAO/EPA limits: pH 6.0–7.5, Pb ≤200 mg/kg",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            val newState = !alertsEnabled
                            viewModel.toggleSoilAlerts(newState)
                            Toast.makeText(
                                context,
                                if (newState) "Soil threshold alerts enabled" else "Soil alerts muted",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_toggle_soil_alerts")
                    ) {
                        Icon(
                            imageVector = if (alertsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Toggle Soil Alerts",
                            tint = if (alertsEnabled) {
                                if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF5D4037)
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.refreshSoilReadings()
                            Toast.makeText(context, "Soil probes recalibrated & synced", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(36.dp).testTag("btn_refresh_soil")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = Color(0xFF5D4037),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Station Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                stations.forEach { station ->
                    val isSelected = station.id == selectedStationId
                    val stationBreach = station.isExceeded
                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectSoilStation(station.id) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = station.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (stationBreach) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("⚠️", fontSize = 10.sp)
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (stationBreach && isSelected) Color(0xFFD32F2F) else Color(0xFF5D4037),
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (stationBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFF5D4037).copy(alpha = 0.15f),
                            selectedLabelColor = if (stationBreach) Color(0xFFD32F2F) else Color(0xFF5D4037)
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Station Subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentStation.locationType} (${currentStation.distanceKm} km away)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF5D4037)
                )
                Text(
                    text = currentStation.lastUpdated,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Breach warning banner
            if (hasThresholdBreach && alertsEnabled && !isDismissed) {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD32F2F).copy(alpha = 0.1f)),
                    modifier = Modifier.fillMaxWidth().border(1.dp, Color(0xFFD32F2F).copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFD32F2F), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Soil Contamination / Degradation Warning", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = Color(0xFFD32F2F))
                            }
                            IconButton(onClick = { viewModel.dismissSoilAlert(currentStation.id) }, modifier = Modifier.size(24.dp)) {
                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = Color(0xFFD32F2F), modifier = Modifier.size(16.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Station '${currentStation.name}' has logged elevated Heavy Metal Lead Pb (${currentStation.leadHeavyMetal} mg/kg vs EPA limit 200) or severe acidification (pH ${currentStation.soilPh}). Phytoremediation and biochar amendment advised.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // Parameter KPI Selector Grid
            Text("Edaphic Parameters (Tap to Chart):", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Row(
                modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                SoilParameterType.values().forEach { param ->
                    val value = currentStation.getValue(param)
                    val status = param.evaluateStatus(value)
                    val isSelected = selectedParam == param

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (isSelected) Color(0xFF5D4037).copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                        border = BorderStroke(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF5D4037) else Color.Transparent
                        ),
                        modifier = Modifier
                            .width(115.dp)
                            .clickable { viewModel.selectSoilParameter(param) }
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(text = param.code, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(
                                text = "%.1f %s".format(value, param.unit),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = status.color
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = status.label, fontSize = 9.sp, color = status.color, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Interactive 24h Time-series Chart
            Text(
                text = "24h Trend: ${selectedParam.fullName} (${selectedParam.unit})",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            UniversalTimeSeriesChart(
                points = historyPoints,
                unit = selectedParam.unit,
                standardLimit = selectedParam.standardLimit,
                selectedIndex = selectedHourlyIndex,
                onPointSelected = { viewModel.selectSoilHourlyPoint(it) },
                lineColor = Color(0xFF5D4037)
            )

            // Agronomic Advisory Card
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = activePoint?.status?.color?.copy(alpha = 0.1f) ?: MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = null, tint = activePoint?.status?.color ?: Color(0xFF5D4037), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Agronomic Health: ${activePoint?.status?.label ?: "Satisfactory"}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = activePoint?.status?.color ?: MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = selectedParam.ecologicalImpact,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Standard: ${selectedParam.standardReference} | Methodology: ${selectedParam.measurementMethod}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

// =========================================================================
// UNIVERSAL INTERACTIVE TIME-SERIES CANVAS CHART
// =========================================================================

@Composable
fun UniversalTimeSeriesChart(
    points: List<EnvironmentalHourlyPoint>,
    unit: String,
    standardLimit: Double,
    selectedIndex: Int?,
    onPointSelected: (Int?) -> Unit,
    lineColor: Color,
    modifier: Modifier = Modifier
) {
    if (points.isEmpty()) return

    val values = points.map { it.value }
    val minVal = (values.minOrNull() ?: 0.0).coerceAtMost(standardLimit * 0.7)
    val maxVal = ((values.maxOrNull() ?: 10.0).coerceAtLeast(standardLimit * 1.3) * 1.15)
    val valRange = (maxVal - minVal).coerceAtLeast(1.0)

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
    ) {
        Box(modifier = Modifier.fillMaxSize().padding(12.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(points) {
                        awaitPointerEventScope {
                            while (true) {
                                val event = awaitPointerEvent()
                                val change = event.changes.firstOrNull()
                                if (change != null && change.pressed) {
                                    val touchX = change.position.x
                                    val step = size.width / (points.size - 1).coerceAtLeast(1)
                                    val index = ((touchX + step / 2) / step).toInt().coerceIn(0, points.size - 1)
                                    onPointSelected(index)
                                }
                            }
                        }
                    }
            ) {
                val w = size.width
                val h = size.height - 30f // Leave room for time labels
                val stepX = w / (points.size - 1).coerceAtLeast(1)

                // 1. Regulatory Standard Dashed Line
                val standardY = h - (((standardLimit - minVal) / valRange) * h).toFloat()
                if (standardY in 0f..h) {
                    drawLine(
                        color = Color(0xFFD32F2F).copy(alpha = 0.65f),
                        start = Offset(0f, standardY),
                        end = Offset(w, standardY),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
                    )
                }

                // 2. Line Chart Path & Gradient Fill
                val path = Path()
                val fillPath = Path()

                points.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val y = h - (((pt.value - minVal) / valRange) * h).toFloat()
                    if (i == 0) {
                        path.moveTo(x, y)
                        fillPath.moveTo(x, h)
                        fillPath.lineTo(x, y)
                    } else {
                        path.lineTo(x, y)
                        fillPath.lineTo(x, y)
                    }
                }
                fillPath.lineTo((points.size - 1) * stepX, h)
                fillPath.close()

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(lineColor.copy(alpha = 0.25f), lineColor.copy(alpha = 0.02f)),
                        startY = 0f,
                        endY = h
                    )
                )

                drawPath(
                    path = path,
                    color = lineColor,
                    style = Stroke(width = 3.dp.toPx())
                )

                // 3. Draw Points
                points.forEachIndexed { i, pt ->
                    val x = i * stepX
                    val y = h - (((pt.value - minVal) / valRange) * h).toFloat()
                    val isSelected = selectedIndex == i

                    drawCircle(
                        color = if (isSelected) pt.status.color else lineColor,
                        radius = if (isSelected) 6.dp.toPx() else 3.5.dp.toPx(),
                        center = Offset(x, y)
                    )
                    if (isSelected) {
                        drawCircle(
                            color = Color.White,
                            radius = 2.5.dp.toPx(),
                            center = Offset(x, y)
                        )
                    }
                }
            }

            // Scrubbed Callout Bubble
            val activePoint = selectedIndex?.let { points.getOrNull(it) } ?: points.lastOrNull()
            if (activePoint != null) {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = activePoint.status.color,
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    Text(
                        text = "${activePoint.hourLabel}: %.1f %s".format(activePoint.value, unit),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}
