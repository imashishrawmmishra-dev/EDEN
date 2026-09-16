package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DeviceThermostat
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AirQualityMonitoringSection
import com.example.ui.components.EnvironmentalDomainMonitoringSection
import com.example.ui.components.EnvironmentalIntelligenceHub
import com.example.viewmodel.EdenTab
import com.example.viewmodel.EdenViewModel
import kotlinx.coroutines.delay

@Composable
fun HomeScreen(
    viewModel: EdenViewModel,
    onNavigate: (EdenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    val userAuth by viewModel.userAuthProfile.collectAsState()
    val deviceGhg by viewModel.deviceGhgProfile.collectAsState()
    val sensors by viewModel.environmentalSensors.collectAsState()
    val forceOffline by viewModel.forceOffline.collectAsState()
    val appUpdateInfo by viewModel.appUpdateInfo.collectAsState()
    val context = LocalContext.current

    // Live real-time grams emitted counter ticker for active session
    var liveSeconds by remember { mutableDoubleStateOf(0.0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L)
            liveSeconds += 1.0
        }
    }

    // Dialog state for customizing device specs
    var showDeviceDialog by remember { mutableStateOf(false) }
    var editDeviceType by remember { mutableStateOf(deviceGhg.deviceType) }
    var editMake by remember { mutableStateOf(deviceGhg.make) }
    var editModel by remember { mutableStateOf(deviceGhg.model) }
    var editWatts by remember { mutableStateOf(deviceGhg.powerWatts.toString()) }
    var editHours by remember { mutableStateOf(deviceGhg.dailyScreenHours.toString()) }

    var dailyChallengeCompleted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Live Network Status & Last Update Strip
        item {
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.checkForAppUpdates(manual = true) }
                    .testTag("home_last_update_status_strip")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(Color(0xFF2E7D32), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Last Update: ${appUpdateInfo.lastUpdateTime}",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFE8F5E9)
                                ) {
                                    Text(
                                        text = "LIVE",
                                        fontSize = 8.5.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF0F6E43),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Checked: ${appUpdateInfo.getFormattedLastCheck()} • v${appUpdateInfo.currentVersionName} (Build ${appUpdateInfo.currentVersionCode})",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .clickable { viewModel.checkForAppUpdates(manual = true) }
                            .testTag("home_sync_check_btn")
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.CloudDone,
                                contentDescription = "Check Latest Update",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(17.dp)
                            )
                        }
                    }
                }
            }
        }

        // Customer Auto-Update Banner
        if (appUpdateInfo.isUpdateAvailable) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, Color(0xFFFFB300), RoundedCornerShape(16.dp))
                        .clickable { viewModel.setUpdateDialog(true) }
                        .testTag("home_auto_update_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFFF8F00),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.SystemUpdate,
                                contentDescription = "Auto Update Ready",
                                tint = Color.White,
                                modifier = Modifier.padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Auto-Update Ready: v${appUpdateInfo.latestVersionName}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFFE65100)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFFFFE082)
                                ) {
                                    Text(
                                        text = "SYNC",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFBF360C),
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Released: ${appUpdateInfo.releaseDate} • Last checked: ${appUpdateInfo.getFormattedLastCheck()}",
                                fontSize = 11.sp,
                                color = Color(0xFF5D4037)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = { viewModel.setUpdateDialog(true) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                            modifier = Modifier.height(34.dp).testTag("home_banner_update_btn")
                        ) {
                            Text("Update", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // User Authentication & EcoPoints Header Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(20.dp))
                    .testTag("home_user_profile_card"),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = userAuth.displayName.take(1).uppercase(),
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (userAuth.isLoggedIn) userAuth.displayName else "Guest User",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Surface(
                                        shape = RoundedCornerShape(4.dp),
                                        color = Color(0xFFE8F5E9)
                                    ) {
                                        Text(
                                            text = if (userAuth.isLoggedIn) "ACTIVE" else "UNREGISTERED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF0F6E43),
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = userAuth.designation,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // EcoPoints Badge & Action
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFFFF3E0),
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { viewModel.setSignInDialog(true) }
                                .padding(2.dp)
                                .testTag("home_points_badge")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFE65100),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${userAuth.rewardPoints} pts",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFFE65100)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Purpose: ${userAuth.purpose}",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                        ) {
                            Text(
                                text = userAuth.badgeTitle,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        TextButton(
                            onClick = { viewModel.setSignInDialog(true) },
                            modifier = Modifier.testTag("manage_profile_or_signin_btn")
                        ) {
                            Text(
                                text = if (userAuth.isLoggedIn) "Manage Account" else "Sign In (+100 Pts)",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // 8 Specialized AI Models Feature Banner
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF041922)),
                border = BorderStroke(1.5.dp, Color(0xFF00E5FF).copy(alpha = 0.6f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .clickable { onNavigate(EdenTab.AI_MODELS) }
                    .testTag("home_ai_models_studio_banner")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF00E5FF).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF).copy(alpha = 0.6f))
                        ) {
                            Text(
                                text = "SPECIALIZED AI ARCHITECTURE",
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF00E5FF),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Explore Lab",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF00E5FF)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = null,
                                tint = Color(0xFF00E5FF),
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF00E5FF).copy(alpha = 0.2f),
                            border = BorderStroke(1.dp, Color(0xFF00E5FF)),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Psychology,
                                    contentDescription = "8 AI Models",
                                    tint = Color(0xFF00E5FF),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "8 Specialized AI Models",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            Text(
                                text = "Flowcharts, Neural Nodes & Live Simulation Lab",
                                fontSize = 11.sp,
                                color = Color(0xFF80DEEA)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // 8 Acronym Chips Row
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val modelChips = listOf(
                            "LLM" to Color(0xFF00E5FF),
                            "LCM" to Color(0xFF1DE9B6),
                            "LAM" to Color(0xFFFFAB00),
                            "MoE" to Color(0xFF7C4DFF),
                            "VLM" to Color(0xFF00B0FF),
                            "SLM" to Color(0xFF00E676),
                            "MLM" to Color(0xFFFF5252),
                            "SAM" to Color(0xFFE040FB)
                        )
                        modelChips.forEach { (tag, color) ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = color.copy(alpha = 0.15f),
                                border = BorderStroke(0.8.dp, color.copy(alpha = 0.5f))
                            ) {
                                Text(
                                    text = tag,
                                    fontSize = 10.sp,
                                    fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                                    fontWeight = FontWeight.Bold,
                                    color = color,
                                    modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // Live Environmental Sensors & WHO AQI Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .testTag("home_live_sensors_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.DeviceThermostat,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Live Environmental Conditions",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE8F5E9)
                        ) {
                            Text(
                                text = sensors.whoAqiStatus,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // 4-Grid Sensor Tiles in a single horizontal line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Indoor Temp
                        SensorMiniCard(
                            label = "Indoor Temp",
                            value = "%.1f°C".format(sensors.indoorTempC),
                            subValue = "%.1f°F".format(sensors.indoorTempF),
                            icon = Icons.Default.DeviceThermostat,
                            modifier = Modifier.width(135.dp)
                        )
                        // Outdoor Temp
                        SensorMiniCard(
                            label = "Outdoor Temp",
                            value = "%.1f°C".format(sensors.outdoorTempC),
                            subValue = "%.1f°F".format(sensors.outdoorTempF),
                            icon = Icons.Default.DeviceThermostat,
                            modifier = Modifier.width(135.dp)
                        )
                        // Indoor Humidity
                        SensorMiniCard(
                            label = "Indoor RH",
                            value = "%.1f%%".format(sensors.indoorHumidityRh),
                            subValue = "Optimum: 40-60%",
                            icon = Icons.Default.WaterDrop,
                            modifier = Modifier.width(135.dp)
                        )
                        // Outdoor Humidity
                        SensorMiniCard(
                            label = "Outdoor RH",
                            value = "%.1f%%".format(sensors.outdoorHumidityRh),
                            subValue = "Ambient Station",
                            icon = Icons.Default.Air,
                            modifier = Modifier.width(135.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // WHO AQI Pollutant Breakdown in a horizontal line
                    Text(
                        text = "WHO 2021 Global Air Quality Parameters:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        PollutantPill(name = "PM2.5", value = "%.1f".format(sensors.pm25), unit = "µg/m³", limit = "15")
                        PollutantPill(name = "PM10", value = "%.1f".format(sensors.pm10), unit = "µg/m³", limit = "45")
                        PollutantPill(name = "NO2", value = "%.1f".format(sensors.no2), unit = "µg/m³", limit = "25")
                        PollutantPill(name = "SO2", value = "%.1f".format(sensors.so2), unit = "µg/m³", limit = "40")
                        PollutantPill(name = "CO", value = "%.1f".format(sensors.co), unit = "mg/m³", limit = "4")
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Advisory: " + sensors.whoAdvisory,
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Multi-Domain Environmental Monitoring Suite (Air, Water, Noise, Soil) with Telemetry & Thresholds
        item {
            EnvironmentalDomainMonitoringSection(viewModel = viewModel)
        }

        // Live Device GHG Emission Tracking Card
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color(0xFF0F6E43).copy(alpha = 0.3f), RoundedCornerShape(20.dp))
                    .testTag("home_device_ghg_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (deviceGhg.deviceType.contains("Phone", true)) Icons.Default.Smartphone else Icons.Default.Computer,
                                contentDescription = null,
                                tint = Color(0xFF0F6E43),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Live Device GHG Tracking",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }

                        IconButton(
                            onClick = { showDeviceDialog = true },
                            modifier = Modifier.size(28.dp).testTag("edit_device_specs_btn")
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit Specs", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "${deviceGhg.make} ${deviceGhg.model} (${deviceGhg.deviceType})",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Power Draw: ${deviceGhg.powerWatts}W • Active Use: ${deviceGhg.dailyScreenHours} hrs/day • Grid: ${deviceGhg.gridCarbonIntensityKgKwh} kg/kWh",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Live Ticker Metric Box
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "LIVE SESSION EMISSION",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F6E43)
                                )
                                val sessionGrams = liveSeconds * deviceGhg.gramsPerSecond
                                Text(
                                    text = "%.4f g CO₂e".format(sessionGrams),
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 18.sp,
                                    color = Color(0xFF0F6E43)
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "RATE PER HOUR",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = "%.2f g CO₂e/h".format(deviceGhg.operationalGramsPerHour),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Daily Operational: %.3f kg CO₂e".format(deviceGhg.dailyOperationalCarbonKg),
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = "Annual Total: %.1f kg CO₂e".format(deviceGhg.annualTotalKg),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }

        // App Information, User Manual & Verification Banner (Clickable to open EdenAboutModal)
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.setAboutDialog(true) }
                    .testTag("home_about_eden_banner")
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "About EDEN & User Manual",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Tap to view full app documentation, content verification (IPCC/ISO/EPA/WHO), and mission.",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                        )
                    }
                }
            }
        }

        // Free Open Research Library & Study Modules Preview
        item {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                    .testTag("home_research_preview_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Free Environmental Research",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                        }
                        TextButton(
                            onClick = { onNavigate(EdenTab.RESOURCES) },
                            modifier = Modifier.testTag("home_view_all_research_btn")
                        ) {
                            Text("View All", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Access peer-reviewed papers, open study modules, IPCC reports, and technical standards for free:",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Research modules in a horizontal line
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        ResearchMiniCard(title = "IPCC AR6: Global Warming Potential (GWP100)", org = "IPCC / WMO / UNEP", tag = "Report")
                        ResearchMiniCard(title = "WHO Global Air Quality Guidelines 2021", org = "World Health Organization", tag = "Guidelines")
                        ResearchMiniCard(title = "Project Drawdown: 100 Technical Solutions", org = "Drawdown Consortium", tag = "Research")
                        ResearchMiniCard(title = "Metcalf & Eddy: Activated Sludge Kinetics", org = "Water Environment Fed.", tag = "Study Module")
                    }
                }
            }
        }

        // Quick Navigation: Core Environmental Tools in a horizontal line
        item {
            Text(
                text = "Core Environmental Tools",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                ToolNavCard(
                    title = "8 AI Models",
                    subtitle = "LLM • MoE • SAM • VLM",
                    icon = Icons.Default.Psychology,
                    color = Color(0xFF00B4D8),
                    onClick = { onNavigate(EdenTab.AI_MODELS) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_ai_models"
                )
                ToolNavCard(
                    title = "Calculators",
                    subtitle = "Scope 1-3 & Stack Flow",
                    icon = Icons.Default.Calculate,
                    color = Color(0xFF0F6E43),
                    onClick = { onNavigate(EdenTab.CALCULATORS) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_calc"
                )
                ToolNavCard(
                    title = "Live GPS Carbon",
                    subtitle = "Trip Commute Audit",
                    icon = Icons.Default.GpsFixed,
                    color = Color(0xFF1E88E5),
                    onClick = { onNavigate(EdenTab.LIVE_CARBON) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_gps"
                )
                ToolNavCard(
                    title = "Ask EDEN AI",
                    subtitle = if (forceOffline) "Offline Engine" else "Open AI Active",
                    icon = Icons.Default.Psychology,
                    color = if (forceOffline) Color(0xFFC25400) else Color(0xFF8E24AA),
                    onClick = { onNavigate(EdenTab.ASK_EDEN) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_ai"
                )
                ToolNavCard(
                    title = "Pollution Suite",
                    subtitle = "11 Domains • 8 Standards",
                    icon = Icons.Default.Sensors,
                    color = Color(0xFF00796B),
                    onClick = { onNavigate(EdenTab.DATA) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_pollution_suite"
                )
                ToolNavCard(
                    title = "Procedures & FDS",
                    subtitle = "SOPs & Printable PDF",
                    icon = Icons.Default.Description,
                    color = Color(0xFF1565C0),
                    onClick = { onNavigate(EdenTab.MONITORING_PROCEDURES) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_procedures"
                )
                ToolNavCard(
                    title = "Knowledge Graph",
                    subtitle = "5-Tier Concepts",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    color = Color(0xFF00897B),
                    onClick = { onNavigate(EdenTab.KNOWLEDGE) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_knowledge"
                )
                ToolNavCard(
                    title = "ESG & Solutions Hub",
                    subtitle = "20 Domains • OTP Toolkits",
                    icon = Icons.Default.Shield,
                    color = Color(0xFF1B5E20),
                    onClick = { onNavigate(EdenTab.ESG_SOLUTIONS) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_esg_solutions"
                )
                ToolNavCard(
                    title = "Study & Research",
                    subtitle = "Open Standards & QMS",
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    color = Color(0xFF5E35B1),
                    onClick = { onNavigate(EdenTab.RESOURCES) },
                    modifier = Modifier.width(185.dp),
                    testTag = "home_nav_resources"
                )
            }
        }

        // Section: Basic to Advanced Environmental Intelligence Hub
        // Covers Air Quality, Water, Animals & Biodiversity, Physical Qualities,
        // Soil Control, Noise Quality, Food Microbiology, Pollution Testing,
        // Pollution Monitoring, and Pollution Remediation with Charts, QMS, Ongoing Research & Field Inputs.
        item {
            EnvironmentalIntelligenceHub(viewModel = viewModel)
        }

        // Daily Sustainability Action Challenge
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF0F6E43), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Eco-Action Challenge",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = Color(0xFF0F6E43)
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFFC8E6C9)
                        ) {
                            Text(
                                text = "+15 Pts",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Conduct a device energy audit today: lower screen brightness by 20% or power down non-essential background devices.",
                        fontSize = 12.sp,
                        color = Color(0xFF1B5E20)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!dailyChallengeCompleted) {
                        Button(
                            onClick = {
                                dailyChallengeCompleted = true
                                viewModel.awardEcoPoints(15)
                                Toast.makeText(context, "+15 EcoPoints Awarded! Great job for the planet!", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(40.dp)
                                .testTag("complete_daily_action_btn"),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Complete Action & Claim 15 Pts", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFC8E6C9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "✓ Completed Today! +15 EcoPoints credited.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.padding(8.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    // Edit Device Specs Dialog
    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Configure Device Specifications", fontWeight = FontWeight.Bold, fontSize = 16.sp) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Select or enter device details to accurately compute live operational GHG emissions:", fontSize = 11.sp)

                    OutlinedTextField(
                        value = editDeviceType,
                        onValueChange = { editDeviceType = it },
                        label = { Text("Device Type (Smartphone / Laptop / Desktop)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editMake,
                        onValueChange = { editMake = it },
                        label = { Text("Make / Brand (e.g. Samsung, Apple, Dell)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editModel,
                        onValueChange = { editModel = it },
                        label = { Text("Model Name") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editWatts,
                        onValueChange = { editWatts = it },
                        label = { Text("Power Draw (Watts)") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = editHours,
                        onValueChange = { editHours = it },
                        label = { Text("Daily Active Screen Hours") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val w = editWatts.toDoubleOrNull() ?: 5.0
                        val h = editHours.toDoubleOrNull() ?: 6.0
                        viewModel.updateDeviceProfile(editDeviceType, editMake, editModel, w, h)
                        showDeviceDialog = false
                    }
                ) {
                    Text("Save & Update")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeviceDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun SensorMiniCard(
    label: String,
    value: String,
    subValue: String,
    icon: ImageVector,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = label, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.onSurface)
            Text(text = subValue, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun PollutantPill(name: String, value: String, unit: String, limit: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
        ) {
            Text(text = name, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "(≤$limit $unit)", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun ResearchMiniCard(title: String, org: String, tag: String) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)),
        modifier = Modifier.width(220.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(4.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                ) {
                    Text(
                        text = tag,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
                Text(
                    text = org,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = title,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun ToolNavCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String = ""
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = modifier
            .border(1.dp, color.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = color.copy(alpha = 0.15f),
                modifier = Modifier.size(36.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(text = subtitle, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
