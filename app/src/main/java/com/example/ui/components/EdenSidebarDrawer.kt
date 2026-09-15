package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
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
import com.example.monitoring.model.MonitoringProcedureDomain
import com.example.viewmodel.EdenTab

@Composable
fun EdenSidebarDrawer(
    currentTab: EdenTab,
    selectedProcedureDomain: MonitoringProcedureDomain,
    onSelectTab: (EdenTab) -> Unit,
    onSelectProcedureDomain: (MonitoringProcedureDomain) -> Unit,
    onCloseDrawer: () -> Unit,
    onOpenJobSearch: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    ModalDrawerSheet(
        modifier = modifier
            .width(320.dp)
            .fillMaxHeight(),
        drawerContainerColor = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .verticalScroll(rememberScrollState())
        ) {
            // Header: Brand & Compliance Seal
            Surface(
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 20.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Sensors,
                                    contentDescription = "EDEN Logo",
                                    tint = MaterialTheme.colorScheme.onPrimary,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "EDEN",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Environmental Intelligence",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF0F6E43).copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF0F6E43).copy(alpha = 0.3f))
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Verified,
                                    contentDescription = null,
                                    tint = Color(0xFF0F6E43),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "ISO/IEC 17025 QMS READY",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0F6E43)
                                )
                            }
                        }

                        Text(
                            text = "v2.5 Enterprise",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ==========================================
            // NEW DEDICATED SECTION: MONITORING PROCEDURES
            // ==========================================
            SectionHeader(title = "MONITORING PROCEDURES", badge = "ISO • USEPA • LOCAL")

            // Main Procedures Hub Item
            DrawerNavigationItem(
                title = "All Procedures & PDF Sheets",
                subtitle = "Printable FDS & Regulatory SOPs",
                icon = Icons.Default.PictureAsPdf,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES,
                testTag = "sidebar_monitoring_procedures_hub",
                onClick = {
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            // Five Individual Requested Domain Items
            ProcedureSubItem(
                title = "Ambient Air Monitoring",
                subtitle = "ISO 12341 • USEPA 40 CFR 50 • CPCB",
                icon = Icons.Default.Air,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES && selectedProcedureDomain == MonitoringProcedureDomain.AMBIENT,
                testTag = "sidebar_proc_ambient",
                onClick = {
                    onSelectProcedureDomain(MonitoringProcedureDomain.AMBIENT)
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            ProcedureSubItem(
                title = "Indoor Air Quality (IAQ)",
                subtitle = "ISO 16000 • ASHRAE 62.1 • EN 16798",
                icon = Icons.Default.MeetingRoom,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES && selectedProcedureDomain == MonitoringProcedureDomain.INDOOR,
                testTag = "sidebar_proc_indoor",
                onClick = {
                    onSelectProcedureDomain(MonitoringProcedureDomain.INDOOR)
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            ProcedureSubItem(
                title = "Noise Level Monitoring",
                subtitle = "ISO 1996-2 • USEPA Noise Act • CPCB",
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES && selectedProcedureDomain == MonitoringProcedureDomain.NOISE,
                testTag = "sidebar_proc_noise",
                onClick = {
                    onSelectProcedureDomain(MonitoringProcedureDomain.NOISE)
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            ProcedureSubItem(
                title = "Stack Emission (Isokinetic)",
                subtitle = "USEPA Method 5 • ISO 9096 • BS EN",
                icon = Icons.Default.CloudUpload,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES && selectedProcedureDomain == MonitoringProcedureDomain.STACK,
                testTag = "sidebar_proc_stack",
                onClick = {
                    onSelectProcedureDomain(MonitoringProcedureDomain.STACK)
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            ProcedureSubItem(
                title = "Water Quality Parameters",
                subtitle = "ISO 5667 • USEPA 40 CFR 136 • APHA",
                icon = Icons.Default.WaterDrop,
                isSelected = currentTab == EdenTab.MONITORING_PROCEDURES && selectedProcedureDomain == MonitoringProcedureDomain.WATER,
                testTag = "sidebar_proc_water",
                onClick = {
                    onSelectProcedureDomain(MonitoringProcedureDomain.WATER)
                    onSelectTab(EdenTab.MONITORING_PROCEDURES)
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
            )
            Spacer(modifier = Modifier.height(10.dp))

            // ==========================================
            // APPLICATION MODULES & TOOLS
            // ==========================================
            SectionHeader(title = "CORE APPLICATION MODULES")

            DrawerNavigationItem(
                title = "Home & Dashboard",
                subtitle = "Active overview & plant status",
                icon = Icons.Default.Home,
                isSelected = currentTab == EdenTab.HOME,
                testTag = "sidebar_nav_home",
                onClick = {
                    onSelectTab(EdenTab.HOME)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Ask EDEN AI Advisor",
                subtitle = "Regulatory & engineering assistant",
                icon = Icons.Default.Psychology,
                isSelected = currentTab == EdenTab.ASK_EDEN,
                testTag = "sidebar_nav_ask_eden",
                onClick = {
                    onSelectTab(EdenTab.ASK_EDEN)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "11-Domain Monitoring Suite",
                subtitle = "Live calculations & sensor inputs",
                icon = Icons.Default.Sensors,
                isSelected = currentTab == EdenTab.DATA,
                testTag = "sidebar_nav_sensors",
                onClick = {
                    onSelectTab(EdenTab.DATA)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Environmental Calculators",
                subtitle = "Aerosol, AQI, Dispersion & Stack",
                icon = Icons.Default.Calculate,
                isSelected = currentTab == EdenTab.CALCULATORS,
                testTag = "sidebar_nav_calculator",
                onClick = {
                    onSelectTab(EdenTab.CALCULATORS)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Live Carbon Tracker",
                subtitle = "GHG Scope 1, 2, 3 footprint",
                icon = Icons.Default.Co2,
                isSelected = currentTab == EdenTab.LIVE_CARBON,
                testTag = "sidebar_nav_carbon",
                onClick = {
                    onSelectTab(EdenTab.LIVE_CARBON)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Knowledge Graph",
                subtitle = "Interactive environmental network",
                icon = Icons.Default.Hub,
                isSelected = currentTab == EdenTab.KNOWLEDGE,
                testTag = "sidebar_nav_graph",
                onClick = {
                    onSelectTab(EdenTab.KNOWLEDGE)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Learning Academy & QMS",
                subtitle = "ISO & USEPA compliance training",
                icon = Icons.AutoMirrored.Filled.MenuBook,
                isSelected = currentTab == EdenTab.LEARN,
                testTag = "sidebar_nav_learn",
                onClick = {
                    onSelectTab(EdenTab.LEARN)
                    onCloseDrawer()
                }
            )

            DrawerNavigationItem(
                title = "Environmental Jobs Hub",
                subtitle = "NGO • Public • Private • 16+ Portals",
                icon = Icons.Default.Work,
                isSelected = false,
                testTag = "sidebar_nav_job_search",
                onClick = {
                    onOpenJobSearch()
                    onCloseDrawer()
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Footer
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
                    .clip(RoundedCornerShape(8.dp))
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Text(
                        text = "Statutory QA/QC Record System",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Field data sheets generated with cryptographic hash & ISO 17025 verification standards.",
                        fontSize = 9.sp,
                        lineHeight = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun SectionHeader(title: String, badge: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontSize = 11.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        if (badge != null) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Text(
                    text = badge,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 1.dp)
                )
            }
        }
    }
}

@Composable
private fun DrawerNavigationItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent
    val contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurface

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 2.dp)
            .clip(RoundedCornerShape(10.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 12.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    color = contentColor
                )
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun ProcedureSubItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val containerColor = if (isSelected) MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.7f) else Color.Transparent

    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 22.dp, end = 12.dp, top = 2.dp, bottom = 2.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
            .testTag(testTag)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 11.5.sp,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 9.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
