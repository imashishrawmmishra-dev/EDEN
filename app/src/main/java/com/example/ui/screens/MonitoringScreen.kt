package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MonitoringEntity
import com.example.ui.components.MetricStatCard
import com.example.viewmodel.EdenViewModel

@Composable
fun MonitoringScreen(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val points by viewModel.monitoringPoints.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var paramName by remember { mutableStateOf("") }
    var paramValue by remember { mutableStateOf("") }
    var paramUnit by remember { mutableStateOf("µg/m³") }
    var paramLocation by remember { mutableStateOf("Monitoring Station Alpha") }
    var paramLimit by remember { mutableStateOf("15.0") }
    var paramSource by remember { mutableStateOf("WHO 2021 24h Guideline") }

    val exceedanceCount = points.count { it.isExceedance }
    val compliantCount = points.size - exceedanceCount

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Summary KPI row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricStatCard(
                    label = "Total Points",
                    value = "${points.size}",
                    unit = "sensors",
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    label = "Compliant",
                    value = "$compliantCount",
                    unit = "pts",
                    statusText = "Within Standards",
                    modifier = Modifier.weight(1f)
                )
                MetricStatCard(
                    label = "Exceedances",
                    value = "$exceedanceCount",
                    unit = "alerts",
                    statusText = if (exceedanceCount > 0) "Requires Action" else "Clean",
                    isWarning = exceedanceCount > 0,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Add Log Action Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Field Telemetry & Sampling Data",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Button(
                    onClick = { showAddDialog = !showAddDialog },
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("add_datapoint_button")
                ) {
                    Icon(
                        imageVector = if (showAddDialog) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = "Add measurement",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (showAddDialog) "Cancel" else "Log Data")
                }
            }
        }

        // Add Form Card
        if (showAddDialog) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(14.dp))
                        .testTag("log_data_form")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Log Verified Environmental Reading", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = paramName,
                                onValueChange = { paramName = it },
                                label = { Text("Parameter (e.g. PM2.5, SO2, BOD)") },
                                modifier = Modifier.weight(1.5f)
                            )
                            OutlinedTextField(
                                value = paramValue,
                                onValueChange = { paramValue = it },
                                label = { Text("Value") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = paramUnit,
                                onValueChange = { paramUnit = it },
                                label = { Text("Unit (µg/m³, mg/L, ppm)") },
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = paramLimit,
                                onValueChange = { paramLimit = it },
                                label = { Text("Regulatory Limit") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = paramLocation,
                            onValueChange = { paramLocation = it },
                            label = { Text("Location / Sampling Station") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = paramSource,
                            onValueChange = { paramSource = it },
                            label = { Text("Standard Reference / Authority") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                val v = paramValue.toDoubleOrNull() ?: 0.0
                                val lim = paramLimit.toDoubleOrNull() ?: 0.0
                                if (paramName.isNotBlank() && paramValue.isNotBlank()) {
                                    viewModel.addFieldMonitoringPoint(
                                        param = paramName.trim(),
                                        value = v,
                                        unit = paramUnit.trim(),
                                        location = paramLocation.trim(),
                                        standardLimit = lim,
                                        standardSource = paramSource.trim()
                                    )
                                    showAddDialog = false
                                    paramName = ""
                                    paramValue = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Commit Telemetry Record")
                        }
                    }
                }
            }
        }

        // Monitoring Points List
        items(points) { point ->
            MonitoringPointCard(point = point)
        }
    }
}

@Composable
private fun MonitoringPointCard(point: MonitoringEntity) {
    val isExceeded = point.isExceedance
    val statusBg = if (isExceeded) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
    val statusFg = if (isExceeded) Color(0xFFC62828) else Color(0xFF2E7D32)
    val statusIcon = if (isExceeded) Icons.Default.Warning else Icons.Default.CheckCircle
    val statusLabel = if (isExceeded) "EXCEEDANCE" else "COMPLIANT"

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                1.dp,
                if (isExceeded) Color(0xFFE53935).copy(alpha = 0.5f) else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                RoundedCornerShape(12.dp)
            )
            .testTag("monitoring_card_${point.parameter.lowercase()}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = point.parameter,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = point.location,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = statusBg
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            imageVector = statusIcon,
                            contentDescription = statusLabel,
                            tint = statusFg,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = statusLabel,
                            color = statusFg,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "${point.value}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isExceeded) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = point.unit,
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 3.dp)
                    )
                }

                Text(
                    text = "Limit: ${point.standardLimit} ${point.unit}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Governing Standard: ${point.standardSource}",
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}
