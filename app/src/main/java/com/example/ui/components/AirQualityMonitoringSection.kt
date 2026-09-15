package com.example.ui.components

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.graphics.Color
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
import com.example.data.model.AirPollutantType
import com.example.data.model.AirQualityHourlyPoint
import com.example.data.model.AirQualityStation
import com.example.viewmodel.EdenViewModel

@Composable
fun AirQualityMonitoringSection(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val stations by viewModel.airStations.collectAsState()
    val selectedStationId by viewModel.selectedAirStationId.collectAsState()
    val selectedPollutant by viewModel.selectedAirPollutant.collectAsState()
    val selectedHourlyIndex by viewModel.selectedHourlyPointIndex.collectAsState()
    val alertsEnabled by viewModel.airQualityAlertsEnabled.collectAsState()
    val dismissedStationId by viewModel.dismissedAlertStation.collectAsState()

    val currentStation = stations.find { it.id == selectedStationId } ?: stations.first()
    val historyPoints = currentStation.hourlyHistory[selectedPollutant] ?: emptyList()
    val activePoint = selectedHourlyIndex?.let { historyPoints.getOrNull(it) } ?: historyPoints.lastOrNull()

    val isPm25Exceeded = currentStation.pm25 > AirPollutantType.PM25.whoGuideline24h
    val isNo2Exceeded = currentStation.no2 > AirPollutantType.NO2.whoGuideline24h
    val hasThresholdBreach = isPm25Exceeded || isNo2Exceeded

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.5f) else MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                RoundedCornerShape(22.dp)
            )
            .testTag("air_quality_monitoring_section")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // --- 1. SECTION HEADER WITH STATION SELECTION & TELEMETRY STATUS ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = if (hasThresholdBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.Air,
                                contentDescription = "Air Quality",
                                tint = if (hasThresholdBreach) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Air Quality Monitoring",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            // Live Telemetry Pulse indicator (Red if breach, green otherwise)
                            Surface(
                                shape = CircleShape,
                                color = if (hasThresholdBreach) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        }
                        Text(
                            text = "Live telemetry • WHO thresholds: PM2.5 ≤15, NO₂ ≤25",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Notification Alerts Toggle Button
                    IconButton(
                        onClick = {
                            val newState = !alertsEnabled
                            viewModel.toggleAirQualityAlerts(newState)
                            Toast.makeText(
                                context,
                                if (newState) "Air quality threshold notifications enabled" else "Threshold notifications muted",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_toggle_air_alerts")
                    ) {
                        Icon(
                            imageVector = if (alertsEnabled) Icons.Default.NotificationsActive else Icons.Default.NotificationsOff,
                            contentDescription = "Toggle Threshold Alerts",
                            tint = if (alertsEnabled) {
                                if (hasThresholdBreach) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                            },
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    IconButton(
                        onClick = {
                            viewModel.refreshAirQualityReadings()
                            Toast.makeText(context, "Sensors recalibrated & synced with station", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_refresh_air_quality")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Telemetry",
                            tint = MaterialTheme.colorScheme.primary,
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
                    val stationHasBreach = station.pm25 > AirPollutantType.PM25.whoGuideline24h || station.no2 > AirPollutantType.NO2.whoGuideline24h

                    FilterChip(
                        selected = isSelected,
                        onClick = { viewModel.selectAirStation(station.id) },
                        label = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = station.name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                                if (stationHasBreach) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "⚠️",
                                        fontSize = 10.sp
                                    )
                                }
                            }
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (stationHasBreach && isSelected) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(14.dp)
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = if (stationHasBreach) Color(0xFFD32F2F).copy(alpha = 0.15f) else MaterialTheme.colorScheme.primaryContainer,
                            selectedLabelColor = if (stationHasBreach) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )
                }
            }

            // Current Station Subtitle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${currentStation.locationType} (${currentStation.distanceKm} km away)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (hasThresholdBreach) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
                )
                Text(
                    text = currentStation.lastUpdated,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // --- THRESHOLD-BASED NOTIFICATION ALERT BANNER SYSTEM ---
            AirQualityThresholdNotificationBanner(
                currentStation = currentStation,
                alertsEnabled = alertsEnabled,
                isPm25Exceeded = isPm25Exceeded,
                isNo2Exceeded = isNo2Exceeded,
                isDismissed = dismissedStationId == currentStation.id,
                onDismiss = { viewModel.dismissAirQualityAlert(currentStation.id) },
                onRestore = { viewModel.resetDismissedAlert() },
                onSelectPollutant = { viewModel.selectAirPollutant(it) }
            )

            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

            // --- 2. FOUR CORE METRIC CARDS (PM2.5, PM10, NO2, O3) ---
            Text(
                text = "Key Atmospheric Pollutants (Tap to inspect trend):",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // PM2.5 Card (Highlighted in Red if > 15.0 WHO guideline)
                PollutantMetricCard(
                    pollutant = AirPollutantType.PM25,
                    currentValue = currentStation.pm25,
                    isSelected = selectedPollutant == AirPollutantType.PM25,
                    isThresholdTarget = true,
                    onClick = { viewModel.selectAirPollutant(AirPollutantType.PM25) },
                    modifier = Modifier.weight(1f)
                )
                // PM10 Card
                PollutantMetricCard(
                    pollutant = AirPollutantType.PM10,
                    currentValue = currentStation.pm10,
                    isSelected = selectedPollutant == AirPollutantType.PM10,
                    isThresholdTarget = false,
                    onClick = { viewModel.selectAirPollutant(AirPollutantType.PM10) },
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // NO2 Card (Highlighted in Red if > 25.0 WHO guideline)
                PollutantMetricCard(
                    pollutant = AirPollutantType.NO2,
                    currentValue = currentStation.no2,
                    isSelected = selectedPollutant == AirPollutantType.NO2,
                    isThresholdTarget = true,
                    onClick = { viewModel.selectAirPollutant(AirPollutantType.NO2) },
                    modifier = Modifier.weight(1f)
                )
                // O3 Card
                PollutantMetricCard(
                    pollutant = AirPollutantType.O3,
                    currentValue = currentStation.o3,
                    isSelected = selectedPollutant == AirPollutantType.O3,
                    isThresholdTarget = false,
                    onClick = { viewModel.selectAirPollutant(AirPollutantType.O3) },
                    modifier = Modifier.weight(1f)
                )
            }

            // --- 3. INTERACTIVE STATUS CHART ---
            InteractiveAirQualityStatusChart(
                pollutant = selectedPollutant,
                points = historyPoints,
                selectedIndex = selectedHourlyIndex,
                onPointSelected = { viewModel.selectHourlyPoint(it) },
                onSimulatePeak = {
                    viewModel.simulateAirSpike(selectedPollutant)
                    Toast.makeText(context, "Simulated transient ${selectedPollutant.code} surge", Toast.LENGTH_SHORT).show()
                }
            )

            // --- 4. DEEP SCIENTIFIC & HEALTH STATUS DIAGNOSTICS FOR ACTIVE POLLUTANT ---
            PollutantDetailAdvisoryCard(
                pollutant = selectedPollutant,
                currentValue = currentStation.getValue(selectedPollutant),
                activePoint = activePoint
            )
        }
    }
}

/**
 * Dedicated threshold notification banner for PM2.5 and NO2 levels exceeding WHO guidelines.
 */
@Composable
private fun AirQualityThresholdNotificationBanner(
    currentStation: AirQualityStation,
    alertsEnabled: Boolean,
    isPm25Exceeded: Boolean,
    isNo2Exceeded: Boolean,
    isDismissed: Boolean,
    onDismiss: () -> Unit,
    onRestore: () -> Unit,
    onSelectPollutant: (AirPollutantType) -> Unit
) {
    val hasBreach = isPm25Exceeded || isNo2Exceeded

    AnimatedVisibility(
        visible = alertsEnabled,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        if (hasBreach) {
            if (!isDismissed) {
                // Active Red Threshold Notification Card
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFD32F2F).copy(alpha = 0.09f)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, Color(0xFFD32F2F), RoundedCornerShape(14.dp))
                        .testTag("air_quality_threshold_alert_banner")
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color(0xFFD32F2F),
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.NotificationsActive,
                                            contentDescription = "Alert",
                                            tint = Color.White,
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "WHO Safe Guideline Breach Alert",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFFD32F2F)
                                )
                            }

                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.size(24.dp).testTag("btn_dismiss_air_alert")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Dismiss Alert",
                                    tint = Color(0xFFD32F2F),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        // Specific Exceeded Parameters
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (isPm25Exceeded) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(Color(0xFFD32F2F).copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "PM2.5: %.1f µg/m³ (WHO safe limit: ≤15.0 µg/m³ • +%.0f%% excess)".format(
                                            currentStation.pm25,
                                            ((currentStation.pm25 / 15.0) - 1.0) * 100.0
                                        ),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                            }

                            if (isNo2Exceeded) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .background(Color(0xFFD32F2F).copy(alpha = 0.12f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                        .fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = Color(0xFFD32F2F),
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "NO₂: %.1f µg/m³ (WHO safe limit: ≤25.0 µg/m³ • +%.0f%% excess)".format(
                                            currentStation.no2,
                                            ((currentStation.no2 / 25.0) - 1.0) * 100.0
                                        ),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD32F2F)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Advisory: Elevated exposure to combustion particles/gas. Close windows, run indoor HEPA filtration, and refrain from strenuous outdoor exercise.",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 13.sp
                        )

                        // Quick Navigation and Dismiss Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (isPm25Exceeded) {
                                OutlinedButton(
                                    onClick = { onSelectPollutant(AirPollutantType.PM25) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFD32F2F)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F)),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Focus PM2.5", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            if (isNo2Exceeded) {
                                OutlinedButton(
                                    onClick = { onSelectPollutant(AirPollutantType.NO2) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        contentColor = Color(0xFFD32F2F)
                                    ),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFD32F2F)),
                                    modifier = Modifier.height(30.dp),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                                ) {
                                    Text("Focus NO₂", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            TextButton(
                                onClick = onDismiss,
                                modifier = Modifier.height(30.dp),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 6.dp)
                            ) {
                                Text("Dismiss", fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            } else {
                // Collapsed indicator allowing user to re-expand the alert
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFD32F2F).copy(alpha = 0.08f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onRestore() }
                        .border(1.dp, Color(0xFFD32F2F).copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WHO Threshold Alert active for ${currentStation.name} • Tap to view",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFD32F2F)
                            )
                        }
                        Text(
                            text = "Restore",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color(0xFFD32F2F)
                        )
                    }
                }
            }
        } else {
            // Calm Green Safe Banner
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF2E7D32).copy(alpha = 0.06f),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF2E7D32).copy(alpha = 0.25f), RoundedCornerShape(10.dp))
                    .testTag("air_quality_safe_compliance_banner")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Safe",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "WHO Safe Guidelines: PM2.5 (≤15) & NO₂ (≤25) within limits",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF2E7D32)
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(4.dp),
                        color = Color(0xFF2E7D32).copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "ALERTS ON",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PollutantMetricCard(
    pollutant: AirPollutantType,
    currentValue: Double,
    isSelected: Boolean,
    isThresholdTarget: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val status = remember(currentValue) { pollutant.evaluateStatus(currentValue) }
    val isWhoLimitExceeded = currentValue > pollutant.whoGuideline24h
    // When PM2.5 or NO2 (or any monitored pollutant) exceeds WHO guidelines, highlight in bold Red
    val isRedAlert = isWhoLimitExceeded && (isThresholdTarget || pollutant == AirPollutantType.PM25 || pollutant == AirPollutantType.NO2)

    val alertRed = Color(0xFFD32F2F)
    val displayValueColor = if (isRedAlert) alertRed else status.color

    val cardBgColor = when {
        isRedAlert && isSelected -> alertRed.copy(alpha = 0.16f)
        isRedAlert -> alertRed.copy(alpha = 0.08f)
        isSelected -> status.color.copy(alpha = 0.08f)
        else -> MaterialTheme.colorScheme.surface
    }

    val cardBorderColor = when {
        isRedAlert && isSelected -> alertRed
        isRedAlert -> alertRed.copy(alpha = 0.75f)
        isSelected -> status.color
        else -> MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
    }

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = cardBgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected || isRedAlert) 3.dp else 1.dp),
        modifier = modifier
            .clickable { onClick() }
            .border(if (isSelected || isRedAlert) 2.dp else 1.dp, cardBorderColor, RoundedCornerShape(14.dp))
            .testTag("metric_card_${pollutant.name.lowercase()}")
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = pollutant.code,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = if (isRedAlert) alertRed else MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isRedAlert) alertRed.copy(alpha = 0.18f) else status.color.copy(alpha = 0.15f)
                ) {
                    Text(
                        text = if (isWhoLimitExceeded) "EXCEEDS WHO" else "WHO OK",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isRedAlert) alertRed else status.color,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            // Numeric Value Highlighted in Red when exceeding WHO guideline
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                if (isRedAlert) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Exceeds WHO Safe Limit",
                        tint = alertRed,
                        modifier = Modifier
                            .size(18.dp)
                            .padding(bottom = 2.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                }
                Text(
                    text = "%.1f".format(currentValue),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = displayValueColor,
                    modifier = Modifier.testTag("value_${pollutant.name.lowercase()}")
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = pollutant.unit,
                    fontSize = 10.sp,
                    fontWeight = if (isRedAlert) FontWeight.Bold else FontWeight.Normal,
                    color = if (isRedAlert) alertRed else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 3.dp)
                )
            }

            Text(
                text = "WHO Limit: ≤${pollutant.whoGuideline24h.toInt()} ${pollutant.unit}${if (isWhoLimitExceeded) " [EXCEEDED]" else ""}",
                fontSize = 9.sp,
                fontWeight = if (isRedAlert) FontWeight.Bold else FontWeight.Normal,
                color = if (isRedAlert) alertRed else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Surface(
                shape = RoundedCornerShape(6.dp),
                color = if (isRedAlert) alertRed.copy(alpha = 0.16f) else status.color.copy(alpha = 0.12f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = if (isRedAlert) "UNSAFE: Exceeds Safe Limit" else status.label,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isRedAlert) alertRed else status.color,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun InteractiveAirQualityStatusChart(
    pollutant: AirPollutantType,
    points: List<AirQualityHourlyPoint>,
    selectedIndex: Int?,
    onPointSelected: (Int?) -> Unit,
    onSimulatePeak: () -> Unit
) {
    val activeIndex = selectedIndex ?: (points.size - 1).coerceAtLeast(0)
    val activePoint = points.getOrNull(activeIndex)
    val isExceededAtActive = activePoint != null && activePoint.value > pollutant.whoGuideline24h

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isExceededAtActive) Color(0xFFD32F2F).copy(alpha = 0.4f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                RoundedCornerShape(16.dp)
            )
            .testTag("interactive_status_chart")
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Chart Title & Active Point Tooltip
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Assessment,
                        contentDescription = null,
                        tint = if (isExceededAtActive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "12-Hour Status Trend: ${pollutant.code}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                OutlinedButton(
                    onClick = onSimulatePeak,
                    modifier = Modifier.height(28.dp).testTag("btn_simulate_spike"),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, modifier = Modifier.size(12.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Test Surge", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Interactive Tooltip Banner
            if (activePoint != null) {
                val pointExceeds = activePoint.value > pollutant.whoGuideline24h
                val activeTooltipColor = if (pointExceeds) Color(0xFFD32F2F) else activePoint.status.color

                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, activeTooltipColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Time: ${activePoint.hourLabel}",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "%.1f %s".format(activePoint.value, pollutant.unit),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (pointExceeds) Color(0xFFD32F2F) else activePoint.status.color
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = activeTooltipColor.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = if (pointExceeds) "BREACH" else activePoint.status.label,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = activeTooltipColor,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            val ratio = (activePoint.value / pollutant.whoGuideline24h) * 100
                            Text(
                                text = "%.0f%% of WHO limit".format(ratio),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (pointExceeds) Color(0xFFD32F2F) else Color(0xFF2E7D32)
                            )
                            Text(
                                text = if (pointExceeds) "⚠️ Exceeds safe limit" else "Tap bar to inspect",
                                fontSize = 9.sp,
                                color = if (pointExceeds) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Canvas Chart
            val maxValue = remember(points, pollutant) {
                val maxObserved = points.maxOfOrNull { it.value } ?: pollutant.whoGuideline24h
                maxOf(maxObserved * 1.25, pollutant.whoGuideline24h * 1.4)
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(170.dp)
                    .pointerInput(points) {
                        detectTapGestures { offset ->
                            val count = points.size
                            if (count > 0) {
                                val colWidth = size.width / count
                                val clickedIdx = (offset.x / colWidth).toInt().coerceIn(0, count - 1)
                                onPointSelected(clickedIdx)
                            }
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val bottomPadding = 24.dp.toPx()
                    val chartHeight = height - bottomPadding
                    val count = points.size

                    // Draw Safe Zone Background (Good status zone)
                    val safeThresholdY = chartHeight - ((pollutant.whoGuideline24h / maxValue).toFloat() * chartHeight)
                    drawRect(
                        color = Color(0xFF2E7D32).copy(alpha = 0.06f),
                        topLeft = Offset(0f, safeThresholdY),
                        size = Size(width, chartHeight - safeThresholdY)
                    )

                    // Draw WHO 2021 dashed benchmark guideline line (Highlighted in red)
                    drawLine(
                        color = Color(0xFFD32F2F),
                        start = Offset(0f, safeThresholdY),
                        end = Offset(width, safeThresholdY),
                        strokeWidth = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                    )

                    if (count > 0) {
                        val barWidth = (width / count) * 0.65f
                        val slotWidth = width / count

                        points.forEachIndexed { index, point ->
                            val barHeight = ((point.value / maxValue).toFloat() * chartHeight).coerceAtLeast(6f)
                            val left = index * slotWidth + (slotWidth - barWidth) / 2f
                            val top = chartHeight - barHeight

                            val isPointSelected = index == activeIndex
                            val pointExceeds = point.value > pollutant.whoGuideline24h

                            // When point exceeds WHO safe guidelines, render in bold RED
                            val barColor = when {
                                pointExceeds -> if (isPointSelected) Color(0xFFD32F2F) else Color(0xFFD32F2F).copy(alpha = 0.75f)
                                isPointSelected -> point.status.color
                                else -> point.status.color.copy(alpha = 0.65f)
                            }

                            // Draw individual status column bar
                            drawRoundRect(
                                color = barColor,
                                topLeft = Offset(left, top),
                                size = Size(barWidth, barHeight),
                                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
                            )

                            // Highlight selected bar with indicator ring/outline
                            if (isPointSelected) {
                                drawRoundRect(
                                    color = if (pointExceeds) Color(0xFFD32F2F) else Color.White,
                                    topLeft = Offset(left - 2.dp.toPx(), top - 2.dp.toPx()),
                                    size = Size(barWidth + 4.dp.toPx(), barHeight + 4.dp.toPx()),
                                    cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                                    style = Stroke(width = 2.dp.toPx())
                                )
                            }
                        }
                    }
                }

                // X-Axis Hour Labels Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    points.forEachIndexed { index, pt ->
                        if (index % 2 == 0 || index == points.size - 1) {
                            Text(
                                text = pt.hourLabel,
                                fontSize = 8.sp,
                                color = if (index == activeIndex) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontWeight = if (index == activeIndex) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Legend Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFF2E7D32), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Safe (≤WHO)", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    Spacer(modifier = Modifier.width(8.dp))
                    Box(modifier = Modifier.size(8.dp).background(Color(0xFFD32F2F), CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("WHO Exceeded (Red)", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = Color(0xFFD32F2F))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .height(2.dp)
                            .background(Color(0xFFD32F2F))
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Limit (${pollutant.whoGuideline24h.toInt()} ${pollutant.unit})",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD32F2F)
                    )
                }
            }
        }
    }
}

@Composable
private fun PollutantDetailAdvisoryCard(
    pollutant: AirPollutantType,
    currentValue: Double,
    activePoint: AirQualityHourlyPoint?
) {
    val status = remember(currentValue) { pollutant.evaluateStatus(currentValue) }
    val isExceeded = currentValue > pollutant.whoGuideline24h
    val alertRed = Color(0xFFD32F2F)
    val cardColor = if (isExceeded) alertRed else status.color

    Surface(
        shape = RoundedCornerShape(14.dp),
        color = cardColor.copy(alpha = 0.08f),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, cardColor.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isExceeded) Icons.Default.Warning else Icons.Default.HealthAndSafety,
                        contentDescription = null,
                        tint = cardColor,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${pollutant.fullName} Diagnostic",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = cardColor.copy(alpha = 0.2f)
                ) {
                    Text(
                        text = if (isExceeded) "LIMIT BREACH" else pollutant.category,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        color = cardColor,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Primary Sources & Measurement Technology
            Text(
                text = "Primary Sources: ${pollutant.primarySource}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Analytical Sensor: ${pollutant.measurementMethod}",
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 14.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Health Advisory & Guidance
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = if (isExceeded) Icons.Default.Warning else Icons.Default.Info,
                        contentDescription = null,
                        tint = cardColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = if (isExceeded) "Health Advisory (WHO Limit Exceeded):" else "Health & Ventilation Advisory:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = cardColor
                        )
                        Text(
                            text = if (isExceeded) {
                                "Caution: Levels exceed the WHO safe threshold (${pollutant.whoGuideline24h} ${pollutant.unit}). ${status.healthAdvice}"
                            } else {
                                status.healthAdvice
                            },
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}
