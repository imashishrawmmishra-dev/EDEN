package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UpdateState
import com.example.viewmodel.EdenViewModel

@Composable
fun AppUpdateDialog(
    viewModel: EdenViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val updateInfo by viewModel.appUpdateInfo.collectAsState()
    val isDownloading = updateInfo.status == UpdateState.DOWNLOADING
    val isUpToDate = !updateInfo.isUpdateAvailable || updateInfo.status == UpdateState.UP_TO_DATE
    var showArchInfo by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = {
            if (!isDownloading) onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .border(
                    1.dp,
                    if (isUpToDate) Color(0xFF2E7D32).copy(alpha = 0.5f) else Color(0xFFFFB300),
                    RoundedCornerShape(24.dp)
                )
                .testTag("app_update_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Banner
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isUpToDate) Color(0xFFE8F5E9) else Color(0xFFFFF3E0),
                        modifier = Modifier.size(52.dp)
                    ) {
                        Icon(
                            imageVector = if (isUpToDate) Icons.Default.CloudDone else Icons.Default.SystemUpdate,
                            contentDescription = "Update",
                            tint = if (isUpToDate) Color(0xFF2E7D32) else Color(0xFFE65100),
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = if (isUpToDate) "EDEN is Up to Date" else "Auto-Update Available",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isUpToDate)
                                "Running latest release v${updateInfo.currentVersionName}"
                            else
                                "New version v${updateInfo.latestVersionName} ready for instant sync",
                            fontSize = 12.sp,
                            color = if (isUpToDate) Color(0xFF2E7D32) else Color(0xFFE65100),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Version Badge Strip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isUpToDate) Color(0xFFE8F5E9) else Color(0xFFFFF8E1),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Installed Version", fontSize = 10.sp, color = if (isUpToDate) Color(0xFF1B5E20) else Color(0xFF5D4037))
                            Text("v${updateInfo.currentVersionName} (Build ${updateInfo.currentVersionCode})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isUpToDate) Color(0xFF1B5E20) else Color(0xFF2E7D32))
                        }
                        Text(if (isUpToDate) "✓" else "➔", fontSize = 14.sp, color = if (isUpToDate) Color(0xFF2E7D32) else Color(0xFFE65100), fontWeight = FontWeight.Bold)
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Cloud Version", fontSize = 10.sp, color = if (isUpToDate) Color(0xFF1B5E20) else Color(0xFF5D4037))
                            Text("v${updateInfo.latestVersionName} (Build ${updateInfo.latestVersionCode})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color(0xFF00796B))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Last Update & Verification Timestamps
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Last Installed Update", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(updateInfo.lastUpdateTime, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Last Cloud Check", fontSize = 10.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Text(updateInfo.getFormattedLastCheck(), fontSize = 11.sp, fontWeight = FontWeight.Medium, color = MaterialTheme.colorScheme.onSurface)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (isUpToDate) {
                    // Success View when already up to date
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFE8F5E9).copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "All Environmental Engines Synchronized",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1B5E20)
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "• IPCC AR6 (GWP100) 2026 emission factors verified\n• WHO 2021 Global Air Quality thresholds active\n• Offline Room Knowledge Graph synced\n• Auto-check active for subsequent cloud releases",
                                fontSize = 11.sp,
                                color = Color(0xFF2E7D32),
                                lineHeight = 16.sp
                            )
                        }
                    }
                } else {
                    // Release Details Box when update is ready
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text("Package Size: ~${updateInfo.apkSizeMb} MB", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text("Delivery: Seamless OTA", fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF00796B))
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "What's New in this Update:",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(6.dp))
                            updateInfo.releaseNotes.forEach { note ->
                                Row(
                                    modifier = Modifier.padding(vertical = 2.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Text("• ", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                                    Text(
                                        text = note,
                                        fontSize = 11.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        lineHeight = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Downloading Progress Indicator
                if (isDownloading) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            val stageText = when {
                                updateInfo.downloadProgress < 0.7f -> "Downloading update package (${updateInfo.apkSizeMb} MB)..."
                                updateInfo.downloadProgress < 0.95f -> "Validating SHA-256 integrity..."
                                else -> "Applying update to customer device..."
                            }
                            Text(
                                text = stageText,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "${(updateInfo.downloadProgress * 100).toInt()}%",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { updateInfo.downloadProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Direct OTA update syncs instantly without leaving the application",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Architecture explanation banner (toggleable)
                if (showArchInfo) {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "How Auto-Updates Work for Customer Devices:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "1. Silent Startup Check: When customers open the app, it queries the cloud release endpoint.\n2. Over-The-Air (OTA) Hot-Patching: Updates the environmental calculation coefficients, models, and UI assets directly inside the app.\n3. Zero Dependency: Customers do not need an external store link or Google account to stay up to date.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                lineHeight = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }

                // Auto-Download Wi-Fi Preference
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Checkbox(
                        checked = updateInfo.autoDownloadOnWifi,
                        onCheckedChange = { viewModel.setAutoDownloadWifi(it) }
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Auto-sync updates over Wi-Fi for all customer phones",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Action Buttons
                if (!isUpToDate) {
                    Button(
                        onClick = {
                            viewModel.startDownloadAndInstall(context, openStore = false)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("dialog_auto_update_now_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE65100)),
                        enabled = !isDownloading
                    ) {
                        Icon(imageVector = Icons.Default.CloudDownload, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isDownloading) "Updating..." else "Auto-Update Now (${updateInfo.apkSizeMb} MB)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                } else {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("dialog_done_btn"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
                    ) {
                        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Done — App is Up to Date", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = { viewModel.checkForAppUpdates(manual = true) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp)
                            .testTag("dialog_recheck_btn"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.CloudSync, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Re-check Cloud for New Releases", fontSize = 12.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Demo Simulation: Allows testing the auto-update flow anytime
                    TextButton(
                        onClick = { viewModel.triggerTestUpdate() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("dialog_demo_simulate_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Replay, contentDescription = null, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = "Reset to v1.0.0 to Test Auto-Update Flow", fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                }

                // Info toggle button
                OutlinedButton(
                    onClick = { showArchInfo = !showArchInfo },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .testTag("dialog_toggle_arch_btn"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(imageVector = Icons.Default.Info, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showArchInfo) "Hide Distribution Details" else "How Auto-Update Works on Customer Phones",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (!isUpToDate) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(36.dp)
                            .testTag("dialog_dismiss_update_btn"),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(text = "Remind Me Later", fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }
    }
}
