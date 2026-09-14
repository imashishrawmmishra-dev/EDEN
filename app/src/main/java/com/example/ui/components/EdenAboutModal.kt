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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun EdenAboutModal(
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.25f), RoundedCornerShape(24.dp))
                .testTag("eden_about_modal_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // Header with Close Button
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "E",
                                    color = Color.White,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 22.sp
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "EDEN",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Environmental Intelligence & Learning",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("close_about_modal_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(16.dp))

                // Section 1: Proper App Description
                AboutSectionCard(
                    title = "What is EDEN?",
                    icon = Icons.Default.Public,
                    content = "EDEN (Explore • Discover • Educate • Nurture) is an enterprise-grade Environmental Intelligence platform engineered for environmental engineers, sustainability managers, students, and citizens. It combines deterministic environmental engineering calculators, continuous GPS and device emission telemetry, and AI intelligence grounded in verified international scientific standards."
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Section 2: What is it Used For?
                AboutSectionCard(
                    title = "What is it Used For?",
                    icon = Icons.Default.Science,
                    content = "• GHG Scope 1, 2, and 3 Carbon Footprint Auditing (IPCC/ISO 14064)\n" +
                            "• Real-Time Device GHG & Electricity Emissions Tracking\n" +
                            "• Live Outdoor & Indoor Air Quality (WHO 2021 Global Guidelines)\n" +
                            "• Live GPS Transit Commute Emission & Carbon Offset Tracker\n" +
                            "• Industrial Stack Gas Volumetric Flow & Emission Rate (EPA Method 2)\n" +
                            "• Wastewater BOD, COD Loading & Population Equivalent\n" +
                            "• Noise Decibel Summation, Attenuation & Ldn Day-Night Level"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Section 3: User Manual / How to Use
                AboutSectionCard(
                    title = "User Manual & How to Use",
                    icon = Icons.Default.MenuBook,
                    content = "1. HOME TAB: View your live device GHG emissions, indoor/outdoor weather, WHO AQI metrics, and current EcoPoints balance.\n" +
                            "2. CALCULATORS TAB: Enter industrial metrics (fuel liters, kWh electricity, stack velocity, wastewater mg/L) to obtain instant deterministic reports.\n" +
                            "3. ASK EDEN TAB: Ask any environmental question in Online Mode (connected to Open-Source AI) or Offline Mode (deterministic local knowledge graph).\n" +
                            "4. KNOWLEDGE TAB: Explore 5 levels of granular depth from layman summaries to laboratory protocols and regulatory frameworks.\n" +
                            "5. SENSORS & TRACKER: Log physical field samples or activate real-time GPS commute carbon calculation.\n" +
                            "6. OPEN RESEARCH LIBRARY: Access free peer-reviewed articles, IPCC reports, and study modules."
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Section 4: Content Verification & Scientific Standards
                AboutSectionCard(
                    title = "Content Verification & Methodology",
                    icon = Icons.Default.Security,
                    content = "All calculations in EDEN are mathematically deterministic and validated against:\n" +
                            "• IPCC 2006/2019 National GHG Inventory Guidelines (GWP100 AR6)\n" +
                            "• ISO 14064-1:2018 (Greenhouse Gases Specification)\n" +
                            "• US EPA Method 2 (40 CFR Part 60 Appendix A-1)\n" +
                            "• WHO 2021 Global Air Quality Guidelines (PM2.5, PM10, NO2, SO2)\n" +
                            "• Metcalf & Eddy Wastewater Engineering 5th Edition\n" +
                            "• UK DEFRA 2023 / 2024 Conversion Factors"
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Section 5: Reason for Creation, Mission & Vision
                AboutSectionCard(
                    title = "Mission & Vision",
                    icon = Icons.Default.Visibility,
                    content = "• REASON: Fragmented environmental standards and complex spreadsheets prevent everyday people and organizations from knowing their exact ecological impact.\n\n" +
                            "• MISSION: Democratize verified environmental engineering tools, transparent carbon accounting, and open-source scientific literacy across the globe with zero paywalls.\n\n" +
                            "• VISION: Accelerate the global transition to Net-Zero emissions through verifiable data, transparent methodologies, and collective action."
                )

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("dismiss_about_dialog_btn"),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Understood & Ready to Explore", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AboutSectionCard(
    title: String,
    icon: ImageVector,
    content: String
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = content,
                fontSize = 13.sp,
                lineHeight = 19.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
