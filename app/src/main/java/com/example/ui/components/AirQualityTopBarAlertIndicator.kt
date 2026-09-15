package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AirPollutantType
import com.example.data.model.AirQualityStation
import com.example.viewmodel.EdenTab
import com.example.viewmodel.EdenViewModel

/**
 * Real-time 'Alert' status indicator in the topbar.
 * Turns bold red and pulsates continuously whenever the current simulated
 * sensor values exceed WHO guidelines (e.g., PM2.5 > 15 µg/m³, NO₂ > 25 µg/m³).
 */
@Composable
fun AirQualityTopBarAlertIndicator(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val isAirAlert by viewModel.isAirQualityThresholdExceeded.collectAsState()
    val isAnyAlert by viewModel.isAnyEnvironmentalThresholdExceeded.collectAsState()
    val isAlertActive = isAirAlert || isAnyAlert
    val currentStation by viewModel.currentAirQualityStation.collectAsState()

    // Pulsating animation active when thresholds are exceeded
    val infiniteTransition = rememberInfiniteTransition(label = "air_alert_pulse_transition")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.55f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        // Outer pulsating aura ring when alert is triggered
        if (isAlertActive) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .graphicsLayer {
                        scaleX = pulseScale * 1.14f
                        scaleY = pulseScale * 1.22f
                        alpha = pulseAlpha * 0.35f
                    }
                    .background(
                        color = Color(0xFFD32F2F),
                        shape = RoundedCornerShape(18.dp)
                    )
            )
        }

        Surface(
            shape = RoundedCornerShape(18.dp),
            color = if (isAlertActive) Color(0xFFD32F2F) else Color(0xFF2E7D32).copy(alpha = 0.12f),
            border = BorderStroke(
                width = if (isAlertActive) 1.5.dp else 1.dp,
                color = if (isAlertActive) Color(0xFFFFCDD2) else Color(0xFF2E7D32).copy(alpha = 0.4f)
            ),
            modifier = Modifier
                .graphicsLayer {
                    if (isAlertActive) {
                        scaleX = pulseScale
                        scaleY = pulseScale
                    }
                }
                .clip(RoundedCornerShape(18.dp))
                .clickable {
                    viewModel.setAirQualityAlertDialog(true)
                }
                .testTag("topbar_alert_indicator")
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
            ) {
                if (isAlertActive) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Environmental Threshold Alert",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Alert",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        letterSpacing = 0.4.sp
                    )
                    // Synchronized pulsating micro-dot
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .graphicsLayer { alpha = pulseAlpha }
                            .background(Color.White, CircleShape)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Environmental Conditions Safe",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = "Env OK",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2E7D32)
                    )
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .background(Color(0xFF2E7D32), CircleShape)
                    )
                }
            }
        }
    }
}

/**
 * Detailed informational modal displayed when user taps the topbar 'Alert' indicator.
 */
@Composable
fun AirQualityAlertDetailsModal(
    viewModel: EdenViewModel,
    onDismiss: () -> Unit
) {
    val currentStation by viewModel.currentAirQualityStation.collectAsState()
    val isAlertActive by viewModel.isAirQualityThresholdExceeded.collectAsState()
    val exceededList = currentStation.getExceededPollutants()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    shape = CircleShape,
                    color = if (isAlertActive) Color(0xFFD32F2F).copy(alpha = 0.15f) else Color(0xFF2E7D32).copy(alpha = 0.15f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = if (isAlertActive) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (isAlertActive) Color(0xFFD32F2F) else Color(0xFF2E7D32),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = if (isAlertActive) "WHO Threshold Alert" else "Air Quality Status",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isAlertActive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Telemetry: ${currentStation.name}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (isAlertActive) {
                    // Active Alert Banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFFD32F2F).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFFD32F2F).copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = Color(0xFFD32F2F),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Current simulated atmospheric readings exceed World Health Organization (WHO 2021) 24h guidelines.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFD32F2F),
                                lineHeight = 15.sp
                            )
                        }
                    }
                } else {
                    // Safe compliant banner
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = Color(0xFF2E7D32).copy(alpha = 0.1f),
                        border = BorderStroke(1.dp, Color(0xFF2E7D32).copy(alpha = 0.35f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = Color(0xFF2E7D32),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "All atmospheric parameters comply with WHO safe thresholds. Clean environmental conditions.",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF2E7D32),
                                lineHeight = 15.sp
                            )
                        }
                    }
                }

                Text(
                    text = "Live Simulated Sensor Values:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                // 4-Pollutant Telemetry Grid
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    PollutantThresholdRow(
                        name = "PM2.5",
                        fullName = "Fine Particles",
                        value = currentStation.pm25,
                        limit = AirPollutantType.PM25.whoGuideline24h,
                        unit = "µg/m³",
                        isExceeded = currentStation.isPm25Exceeded
                    )
                    PollutantThresholdRow(
                        name = "NO₂",
                        fullName = "Nitrogen Dioxide",
                        value = currentStation.no2,
                        limit = AirPollutantType.NO2.whoGuideline24h,
                        unit = "µg/m³",
                        isExceeded = currentStation.isNo2Exceeded
                    )
                    PollutantThresholdRow(
                        name = "PM10",
                        fullName = "Coarse Dust",
                        value = currentStation.pm10,
                        limit = AirPollutantType.PM10.whoGuideline24h,
                        unit = "µg/m³",
                        isExceeded = currentStation.isPm10Exceeded
                    )
                    PollutantThresholdRow(
                        name = "O₃",
                        fullName = "Tropospheric Ozone",
                        value = currentStation.o3,
                        limit = AirPollutantType.O3.whoGuideline24h,
                        unit = "µg/m³",
                        isExceeded = currentStation.isO3Exceeded
                    )
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // Advisory note
                Text(
                    text = if (isAlertActive) {
                        "Health Recommendation: Close windows, run indoor HEPA filtration, and avoid strenuous outdoor exercise until concentrations drop below safe WHO levels."
                    } else {
                        "Recommendation: Excellent ambient conditions for outdoor sports, cycling, and natural ventilation."
                    },
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    viewModel.selectTab(EdenTab.HOME)
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isAlertActive) Color(0xFFD32F2F) else MaterialTheme.colorScheme.primary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("View Air Quality Section", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                OutlinedButton(
                    onClick = {
                        viewModel.refreshAirQualityReadings()
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(13.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sync Telemetry", fontSize = 11.sp)
                }
                TextButton(
                    onClick = onDismiss
                ) {
                    Text("Close", fontSize = 11.sp)
                }
            }
        },
        shape = RoundedCornerShape(18.dp)
    )
}

@Composable
private fun PollutantThresholdRow(
    name: String,
    fullName: String,
    value: Double,
    limit: Double,
    unit: String,
    isExceeded: Boolean
) {
    val alertRed = Color(0xFFD32F2F)
    val safeGreen = Color(0xFF2E7D32)

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isExceeded) alertRed.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
        border = BorderStroke(
            1.dp,
            if (isExceeded) alertRed.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = name,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        color = if (isExceeded) alertRed else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = fullName,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Text(
                    text = "WHO 24h limit: ≤${limit.toInt()} $unit",
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "%.1f %s".format(value, unit),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = if (isExceeded) alertRed else safeGreen
                )
                Spacer(modifier = Modifier.width(6.dp))
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = if (isExceeded) alertRed else safeGreen
                ) {
                    Text(
                        text = if (isExceeded) "BREACH" else "OK",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            }
        }
    }
}
