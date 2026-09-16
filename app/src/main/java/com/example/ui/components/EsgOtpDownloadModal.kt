package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.solutions.model.DownloadableResourceItem
import com.example.solutions.model.EsgSolutionsRepository
import com.example.viewmodel.EdenViewModel
import kotlinx.coroutines.delay
import java.io.File
import java.io.FileOutputStream

@Composable
fun EsgOtpDownloadModal(
    viewModel: EdenViewModel,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var isOtpVerified by remember { mutableStateOf(false) }
    var destinationInput by remember { mutableStateOf("") }
    var inputMode by remember { mutableStateOf("PHONE") } // "PHONE" or "EMAIL"
    var selectedRole by remember { mutableStateOf("Student") }
    var otpSent by remember { mutableStateOf(false) }
    var generatedOtpCode by remember { mutableStateOf("") }
    var enteredOtpCode by remember { mutableStateOf("") }
    var timerSeconds by remember { mutableIntStateOf(60) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var previewResource by remember { mutableStateOf<DownloadableResourceItem?>(null) }

    LaunchedEffect(otpSent) {
        if (otpSent && timerSeconds > 0) {
            while (timerSeconds > 0) {
                delay(1000L)
                timerSeconds--
            }
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f), RoundedCornerShape(24.dp))
                .testTag("esg_otp_download_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 8.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = if (isOtpVerified) Color(0xFF0F6E43) else MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (isOtpVerified) Icons.Default.CheckCircle else Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isOtpVerified) "Unlocked Downloads" else "Statutory Toolkit Downloads",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isOtpVerified) "OTP Verified • Direct File Export" else "OTP Authentication Required",
                                fontSize = 11.sp,
                                color = if (isOtpVerified) Color(0xFF0F6E43) else MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (!isOtpVerified) {
                    // ==========================================
                    // STEP 1 / 2: OTP AUTHENTICATION FLOW
                    // ==========================================
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)
                        ),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Shield,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Secure Verification for Compliance Dossiers",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "To safeguard regulatory templates, EIA toolkits, and ISO manuals, please verify your mobile number or professional email with a one-time passcode.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                lineHeight = 15.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Mode Selector (Phone / Email)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilterChip(
                            label = "Mobile Number (SMS)",
                            selected = inputMode == "PHONE",
                            onClick = {
                                inputMode = "PHONE"
                                destinationInput = ""
                                otpSent = false
                                errorMessage = null
                            },
                            modifier = Modifier.weight(1f)
                        )
                        FilterChip(
                            label = "Work / Academic Email",
                            selected = inputMode == "EMAIL",
                            onClick = {
                                inputMode = "EMAIL"
                                destinationInput = ""
                                otpSent = false
                                errorMessage = null
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Role selector
                    Text(
                        text = "Your Environmental Role:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("Student", "Professional", "Researcher").forEach { role ->
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (selectedRole == role) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                contentColor = if (selectedRole == role) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedRole = role }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(text = role, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Destination input field
                    OutlinedTextField(
                        value = destinationInput,
                        onValueChange = {
                            destinationInput = it
                            errorMessage = null
                        },
                        label = {
                            Text(if (inputMode == "PHONE") "Enter Mobile Number (e.g. +1 555-0199 / +91 9876543210)" else "Enter Work/University Email")
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = if (inputMode == "PHONE") Icons.Default.Phone else Icons.Default.Key,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("otp_destination_input")
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!otpSent) {
                        Button(
                            onClick = {
                                if (destinationInput.isBlank()) {
                                    errorMessage = "Please enter your ${if (inputMode == "PHONE") "mobile number" else "email address"}."
                                } else {
                                    // Generate secure 6-digit OTP
                                    val code = (100000..999999).random().toString()
                                    generatedOtpCode = code
                                    otpSent = true
                                    timerSeconds = 60
                                    errorMessage = null
                                    Toast.makeText(context, "Secure OTP Code dispatched: $code", Toast.LENGTH_LONG).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("request_otp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Send 6-Digit Secure OTP", fontWeight = FontWeight.Bold)
                        }
                    } else {
                        // OTP Code Display Banner (Simulated gateway for immediate seamless testing)
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFF0F6E43).copy(alpha = 0.12f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F6E43).copy(alpha = 0.4f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "DEMO OTP GATEWAY DISPATCH",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color(0xFF0F6E43)
                                    )
                                    Text(
                                        text = "Your verification code: $generatedOtpCode",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Monospace,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = if (timerSeconds > 0) "Expires in ${timerSeconds}s" else "Expired. Tap resend.",
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                TextButton(
                                    onClick = { enteredOtpCode = generatedOtpCode }
                                ) {
                                    Text("Auto-Fill", fontWeight = FontWeight.Bold, color = Color(0xFF0F6E43))
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = enteredOtpCode,
                            onValueChange = {
                                if (it.length <= 6) {
                                    enteredOtpCode = it
                                    errorMessage = null
                                }
                            },
                            label = { Text("Enter 6-Digit Code") },
                            leadingIcon = {
                                Icon(Icons.Default.Key, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("enter_otp_input")
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = {
                                if (enteredOtpCode == generatedOtpCode && enteredOtpCode.isNotBlank()) {
                                    isOtpVerified = true
                                    viewModel.awardEcoPoints(150)
                                    Toast.makeText(context, "Verification Successful! +150 EcoPoints Awarded.", Toast.LENGTH_SHORT).show()
                                } else {
                                    errorMessage = "Invalid verification code. Please check the code and try again."
                                }
                            },
                            modifier = Modifier.fillMaxWidth().height(48.dp).testTag("verify_otp_button"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43))
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Verify OTP & Unlock Downloads", fontWeight = FontWeight.Bold)
                        }

                        if (timerSeconds == 0) {
                            TextButton(
                                onClick = {
                                    val code = (100000..999999).random().toString()
                                    generatedOtpCode = code
                                    timerSeconds = 60
                                    Toast.makeText(context, "New OTP Code: $code", Toast.LENGTH_SHORT).show()
                                },
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text("Resend OTP Code")
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = errorMessage ?: "",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Medium
                        )
                    }
                } else {
                    // ==========================================
                    // STEP 2: UNLOCKED DIRECT DOWNLOADS SECTION
                    // ==========================================
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F6E43).copy(alpha = 0.12f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF0F6E43), modifier = Modifier.size(20.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Access Granted to Official Regulatory Materials",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F6E43)
                                )
                                Text(
                                    text = "Verified for: $destinationInput ($selectedRole)",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Available Compliance Dossiers & Toolkits:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    EsgSolutionsRepository.downloadableResources.forEach { resource ->
                        ResourceCard(
                            resource = resource,
                            onPreview = { previewResource = resource },
                            onDownload = {
                                saveAndShareResource(context, resource)
                            }
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Close")
                }
            }
        }
    }

    // Modal to preview text / markdown / CSV directly
    if (previewResource != null) {
        val resource = previewResource!!
        Dialog(onDismissRequest = { previewResource = null }) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth(0.95f)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f), RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surface
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = resource.title,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.weight(1f),
                            maxLines = 1
                        )
                        IconButton(onClick = { previewResource = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Close")
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    val content = remember(resource.id) { resource.contentGenerator() }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp)
                    ) {
                        Text(
                            text = content,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            lineHeight = 15.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                clipboardManager.setText(AnnotatedString(content))
                                Toast.makeText(context, "Copied content to clipboard", Toast.LENGTH_SHORT).show()
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Text", fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                saveAndShareResource(context, resource)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Save File", fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ResourceCard(
    resource: DownloadableResourceItem,
    onPreview: () -> Unit,
    onDownload: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = when (resource.fileExtension) {
                        "CSV" -> Color(0xFF0F6E43)
                        "PY" -> Color(0xFF1565C0)
                        else -> Color(0xFF7B1FA2)
                    },
                    contentColor = Color.White
                ) {
                    Text(
                        text = ".${resource.fileExtension} • ${resource.estimatedSize}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

                Text(
                    text = resource.category,
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = resource.title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = resource.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Target Audience: ${resource.targetAudience}",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onPreview,
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Visibility, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Preview", fontSize = 11.sp)
                }

                Button(
                    onClick = onDownload,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0F6E43)),
                    modifier = Modifier.weight(1.3f).height(36.dp)
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Export File", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent
        ),
        modifier = modifier.clickable { onClick() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun saveAndShareResource(context: Context, resource: DownloadableResourceItem) {
    try {
        val fileName = "${resource.id}_eden.${resource.fileExtension.lowercase()}"
        val content = resource.contentGenerator()
        val file = File(context.cacheDir, fileName)
        FileOutputStream(file).use { out ->
            out.write(content.toByteArray(Charsets.UTF_8))
        }

        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = if (resource.fileExtension == "CSV") "text/csv" else "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "EDEN Dossier: ${resource.title}")
            putExtra(Intent.EXTRA_TEXT, content)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(Intent.createChooser(shareIntent, "Save or Export ${resource.title}"))
        Toast.makeText(context, "Saved $fileName. Ready to export.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Export error: ${e.message}", Toast.LENGTH_LONG).show()
    }
}
