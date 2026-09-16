package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Source
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.EdenTab
import com.example.data.model.UserAuthProfile

@Composable
fun DesktopNavigationRail(
    currentTab: EdenTab,
    onSelectTab: (EdenTab) -> Unit,
    userAuth: UserAuthProfile,
    forceOffline: Boolean,
    onToggleForceOffline: () -> Unit,
    isBlackAndWhite: Boolean,
    onToggleBlackAndWhite: () -> Unit,
    onOpenDesktopHub: () -> Unit,
    onOpenJobSearch: () -> Unit,
    onOpenAbout: () -> Unit,
    onOpenAccount: () -> Unit,
    onPlaySound: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(88.dp)
            .fillMaxHeight(),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .padding(vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Brand Top Emblem
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .clickable { onOpenAbout() }
                    .testTag("desktop_rail_brand_logo")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Eco,
                        contentDescription = "EDEN",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "EDEN",
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 0.5.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            // Online / Offline Status Dot
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onToggleForceOffline() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (forceOffline) Color(0xFFC25400) else Color(0xFF0F6E43))
                )
                Spacer(modifier = Modifier.width(3.dp))
                Text(
                    text = if (forceOffline) "OFF" else "AI",
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (forceOffline) Color(0xFFC25400) else Color(0xFF0F6E43)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier
                    .width(48.dp)
                    .padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
            )

            // Primary Navigation Items
            RailNavItem(
                icon = Icons.Default.Home,
                label = "Home",
                shortcutKey = "1",
                isSelected = currentTab == EdenTab.HOME,
                testTag = "rail_nav_home",
                onClick = { onSelectTab(EdenTab.HOME) }
            )

            RailNavItem(
                icon = Icons.Default.Psychology,
                label = "8 AI Models",
                shortcutKey = "M",
                isSelected = currentTab == EdenTab.AI_MODELS,
                testTag = "rail_nav_ai_models",
                onClick = { onSelectTab(EdenTab.AI_MODELS) }
            )

            RailNavItem(
                icon = Icons.Default.Psychology,
                label = "Ask EDEN",
                shortcutKey = "2",
                isSelected = currentTab == EdenTab.ASK_EDEN,
                testTag = "rail_nav_ask",
                onClick = { onSelectTab(EdenTab.ASK_EDEN) }
            )

            RailNavItem(
                icon = Icons.AutoMirrored.Filled.MenuBook,
                label = "Knowledge",
                shortcutKey = "3",
                isSelected = currentTab == EdenTab.KNOWLEDGE,
                testTag = "rail_nav_knowledge",
                onClick = { onSelectTab(EdenTab.KNOWLEDGE) }
            )

            RailNavItem(
                icon = Icons.Default.Calculate,
                label = "Calculators",
                shortcutKey = "4",
                isSelected = currentTab == EdenTab.CALCULATORS,
                testTag = "rail_nav_calc",
                onClick = { onSelectTab(EdenTab.CALCULATORS) }
            )

            RailNavItem(
                icon = Icons.Default.Sensors,
                label = "Sensors",
                shortcutKey = "5",
                isSelected = currentTab == EdenTab.DATA,
                testTag = "rail_nav_sensors",
                onClick = { onSelectTab(EdenTab.DATA) }
            )

            RailNavItem(
                icon = Icons.Default.PictureAsPdf,
                label = "Procedures",
                shortcutKey = "6",
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES,
                testTag = "rail_nav_procedures",
                onClick = { onSelectTab(EdenTab.MONITORING_PROCEDURES) }
            )

            RailNavItem(
                icon = Icons.Default.Shield,
                label = "ESG Hub",
                shortcutKey = "7",
                isSelected = currentTab == EdenTab.ESG_SOLUTIONS,
                testTag = "rail_nav_esg",
                onClick = { onSelectTab(EdenTab.ESG_SOLUTIONS) }
            )

            RailNavItem(
                icon = Icons.Default.School,
                label = "Learn",
                shortcutKey = "8",
                isSelected = currentTab == EdenTab.LEARN,
                testTag = "rail_nav_learn",
                onClick = { onSelectTab(EdenTab.LEARN) }
            )

            RailNavItem(
                icon = Icons.Default.Source,
                label = "Resources",
                shortcutKey = "",
                isSelected = currentTab == EdenTab.RESOURCES,
                testTag = "rail_nav_resources",
                onClick = { onSelectTab(EdenTab.RESOURCES) }
            )

            RailNavItem(
                icon = Icons.Default.Person,
                label = "Profile",
                shortcutKey = "",
                isSelected = currentTab == EdenTab.PROFILE,
                testTag = "rail_nav_profile",
                onClick = { onSelectTab(EdenTab.PROFILE) }
            )

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(
                modifier = Modifier
                    .width(48.dp)
                    .padding(vertical = 4.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
            )

            // Multi-Platform & Desktop Hub Button
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onOpenDesktopHub() }
                    .testTag("rail_action_desktop_hub")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Computer,
                        contentDescription = "Multi-Platform & Desktop Hub (Windows, Linux, macOS)",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Environmental Jobs Quick Direct Button
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = Color(0xFF0F6E43),
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onOpenJobSearch() }
                    .testTag("rail_action_search_jobs")
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Work,
                        contentDescription = "Search Environmental Jobs",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // B&W Monochrome Mode Toggle
            IconButton(
                onClick = onToggleBlackAndWhite,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("rail_action_theme")
            ) {
                Icon(
                    imageVector = if (isBlackAndWhite) Icons.Default.Palette else Icons.Default.Contrast,
                    contentDescription = "Toggle Color / Black & White Mode",
                    tint = if (isBlackAndWhite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(19.dp)
                )
            }

            // Audio Welcome Chime Test Button
            IconButton(
                onClick = onPlaySound,
                modifier = Modifier
                    .size(38.dp)
                    .testTag("rail_action_sound")
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Play Welcome Chime",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // EcoPoints chip
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f),
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .clickable { onOpenAccount() }
                    .padding(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFC25400), modifier = Modifier.size(11.dp))
                    Text(
                        text = "${userAuth.rewardPoints}",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun RailNavItem(
    icon: ImageVector,
    label: String,
    shortcutKey: String,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(vertical = 3.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = containerColor,
            modifier = Modifier.size(40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )
                if (shortcutKey.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(2.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(3.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.8f)
                        ) {
                            Text(
                                text = shortcutKey,
                                fontSize = 7.5.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 2.dp, vertical = 0.5.dp)
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 9.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}
