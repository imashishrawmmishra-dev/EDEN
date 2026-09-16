package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UserAuthProfile

@Composable
fun EdenWelcomeCelebrationDialog(
    userAuth: UserAuthProfile,
    onDismiss: () -> Unit,
    onPlayWelcomeSound: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(28.dp))
                .border(
                    width = 2.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF0F6E43), Color(0xFF2E7D32), Color(0xFF81C784))
                    ),
                    shape = RoundedCornerShape(28.dp)
                )
                .testTag("eden_welcome_celebration_dialog"),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 10.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Glowing Eco Badge
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(Color(0xFF0F6E43), Color(0xFF1B5E20))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "EDEN Active Icon",
                        tint = Color.White,
                        modifier = Modifier.size(42.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // User-requested exact welcome phrasing:
                // "welcome to EDEN a powerful environment"
                Text(
                    text = "Welcome to EDEN, a powerful environment",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center,
                    lineHeight = 27.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.testTag("welcome_celebration_headline")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Your digital sustainability workspace and planetary intelligence center is now active.",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Member Identity Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("welcome_member_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column {
                                Text(
                                    text = userAuth.displayName,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = userAuth.designation,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.padding(start = 6.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "Verified Member",
                                        tint = Color.White,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "VERIFIED",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                        )

                        // Institution Affiliation details
                        val typeEmoji = when (userAuth.institutionType.lowercase()) {
                            "college" -> "🎓"
                            "school" -> "🏫"
                            "office" -> "🏢"
                            "ngo" -> "🤝"
                            "ministry" -> "🏛️"
                            else -> "🏛️"
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = typeEmoji, fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${userAuth.institutionType.uppercase()}: ${userAuth.institutionName.ifBlank { userAuth.organization }}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Welcome Eco-Reward Points Highlight
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = Color(0xFFE8F5E9),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = "Eco Points",
                                        tint = Color(0xFF2E7D32),
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Welcome Reward Bonus",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color(0xFF1B5E20)
                                    )
                                }
                                Text(
                                    text = "+${userAuth.rewardPoints} Eco-Points",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = Color(0xFF0F6E43)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Highlights of What's Unlocked in EDEN
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Unlocked Planetary Intelligence Tools:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    UnlockedFeatureItem(
                        icon = "⚡",
                        title = "Real-Time Telemetry & Planetary Monitoring",
                        description = "Continuous AQI, PM2.5, Sound decibels & Live Device GHG tracking."
                    )
                    UnlockedFeatureItem(
                        icon = "📋",
                        title = "20 Statutory ESG & EIA Domains",
                        description = "Standard operating procedures, legal guidelines & statutory compliances."
                    )
                    UnlockedFeatureItem(
                        icon = "🔬",
                        title = "Numerical Environmental Modeling",
                        description = "Industrial stack dispersion, noise attenuation & watershed models."
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Replay Welcome Chime (Audio)
                OutlinedButton(
                    onClick = onPlayWelcomeSound,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("welcome_replay_sound_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Replay Sound",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Replay Welcome Chime (Audio)",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Enter App Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("welcome_enter_eden_btn"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F6E43)
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Enter Workspace",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Enter EDEN Workspace",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun UnlockedFeatureItem(
    icon: String,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = icon, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 15.sp
            )
        }
    }
}
