package com.example.ui.monitoring

import android.content.Context
import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.FormatPaint
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monitoring.data.PollutionMonitoringRepository
import com.example.monitoring.engine.PollutionCalculationEngine
import com.example.monitoring.model.ComplianceStatus
import com.example.monitoring.model.InstrumentManualDetails
import com.example.monitoring.model.LiveCalculationResult
import com.example.monitoring.model.MonitoringDuration
import com.example.monitoring.model.MonitoringFieldDef
import com.example.monitoring.model.PollutionMonitoringDomain
import com.example.monitoring.model.RegulatoryStandard
import com.example.monitoring.model.StandardProcedureDetails
import com.example.monitoring.util.FieldSheetReportPrinter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PollutionMonitoringHubScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // Active domain (11 domains)
    var selectedDomain by remember { mutableStateOf(PollutionMonitoringDomain.AMBIENT_AIR) }

    // Active duration (Spot, 1-Hr, 8-Hr, 24-Hr)
    var selectedDuration by remember { mutableStateOf(MonitoringDuration.TWENTY_FOUR_HOUR) }

    // Customer-chosen standard (ISO, AS/NZS, USEPA, BS, EU, Indian, Chinese, Brazilian)
    var selectedStandard by remember { mutableStateOf(RegulatoryStandard.USEPA) }

    // Selected sub-tab
    var activeSubTab by remember { mutableIntStateOf(0) }
    val subTabTitles = listOf(
        "Field Data Sheet (Live)",
        "SOP & Procedure",
        "Instrument & Manual",
        "Multi-Standard Matrix",
        "FDS Export & Audit"
    )

    // Dynamic field inputs store for current domain
    val fieldInputs = remember { mutableStateMapOf<String, String>() }

    // Initialize/sync default inputs whenever domain changes
    val fieldDefs = remember(selectedDomain) {
        val defs = PollutionMonitoringRepository.getFieldDefinitions(selectedDomain)
        defs.forEach { def ->
            if (!fieldInputs.containsKey(def.id)) {
                fieldInputs[def.id] = def.defaultValue
            }
        }
        defs
    }

    // Live calculation computed reactively
    val calculationResult by remember(selectedDomain, selectedStandard, selectedDuration, fieldInputs.toMap()) {
        derivedStateOf {
            PollutionCalculationEngine.calculate(
                domain = selectedDomain,
                standard = selectedStandard,
                duration = selectedDuration,
                inputs = fieldInputs.toMap()
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- 1. Header Banner & Scope Title ---
        item {
            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("pollution_hub_header_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(38.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Science,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Pollution Monitoring Suite",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "11 Certified Domains • 8 Global Standards",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }

                        // Reset button
                        IconButton(
                            onClick = {
                                fieldDefs.forEach { def ->
                                    fieldInputs[def.id] = def.defaultValue
                                }
                            },
                            modifier = Modifier.testTag("reset_defaults_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset default values",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Laboratory-grade field protocols, live calculations, instrument manuals, and dynamic compliance evaluation across ISO, AS/NZS, USEPA, BS, EU, Indian (CPCB), Chinese (GB), and Brazilian (CONAMA) standards.",
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // --- 2. Horizontal Scrollable Domain Chips (11 Domains) ---
        item {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "SELECT MONITORING DOMAIN (11 TYPES)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "${selectedDomain.ordinal + 1} of 11",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PollutionMonitoringDomain.values().forEach { domain ->
                        val isSelected = domain == selectedDomain
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedDomain = domain
                                // Sync duration defaults if needed
                                if (domain == PollutionMonitoringDomain.WORKZONE_AIR) {
                                    selectedDuration = MonitoringDuration.EIGHT_HOUR
                                } else if (domain == PollutionMonitoringDomain.STACK_EMISSION || domain == PollutionMonitoringDomain.FLUE_GAS) {
                                    selectedDuration = MonitoringDuration.ONE_HOUR
                                }
                            },
                            label = {
                                Text(
                                    text = domain.shortName,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = getDomainIcon(domain),
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            modifier = Modifier.testTag("domain_chip_${domain.name.lowercase()}")
                        )
                    }
                }
            }
        }

        // --- 3. Timing / Duration Selector & Standard Selector Row ---
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    // Timing Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "MONITORING TIMING:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = "${selectedDuration.durationHours} hrs total",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        MonitoringDuration.values().forEach { duration ->
                            val isSel = duration == selectedDuration
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable {
                                        selectedDuration = duration
                                        // Auto-update duration minutes in ambient air if present
                                        if (fieldInputs.containsKey("duration_minutes")) {
                                            fieldInputs["duration_minutes"] = (duration.durationHours * 60.0).toInt().toString()
                                        }
                                    }
                                    .testTag("duration_btn_${duration.name.lowercase()}")
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = duration.title,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                        color = if (isSel) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Customer-Chosen Standard Selector
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Public,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "CUSTOMER-CHOSEN STANDARD:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Text(
                            text = selectedStandard.region,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        RegulatoryStandard.values().forEach { standard ->
                            val isSel = standard == selectedStandard
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { selectedStandard = standard }
                                    .testTag("standard_btn_${standard.name.lowercase()}")
                            ) {
                                Text(
                                    text = standard.displayName,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSel) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSel) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 4. Active Live Compliance Status Banner with Download Field Sheet Action ---
        item {
            ComplianceEvaluationCard(
                calculationResult = calculationResult,
                onDownloadFieldSheet = {
                    FieldSheetReportPrinter.printFieldSheet(
                        context = context,
                        domain = selectedDomain,
                        standard = selectedStandard,
                        duration = selectedDuration,
                        fieldDefs = fieldDefs,
                        fieldInputs = fieldInputs.toMap(),
                        calculationResult = calculationResult
                    )
                }
            )
        }

        // --- 5. Tab Navigation Bar ---
        item {
            ScrollableTabRow(
                selectedTabIndex = activeSubTab,
                edgePadding = 0.dp,
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                    .testTag("pollution_subtabs_row")
            ) {
                subTabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = activeSubTab == index,
                        onClick = { activeSubTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (activeSubTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }
        }

        // --- 6. Tab Content Switcher ---
        when (activeSubTab) {
            0 -> {
                // TAB 0: Field Data Sheet (FDS) with Live Calculation
                item {
                    FieldDataSheetTabContent(
                        domain = selectedDomain,
                        fieldDefs = fieldDefs,
                        fieldInputs = fieldInputs,
                        calculationResult = calculationResult,
                        onInputValueChanged = { id, value ->
                            fieldInputs[id] = value
                        },
                        onDownloadFieldSheet = {
                            FieldSheetReportPrinter.printFieldSheet(
                                context = context,
                                domain = selectedDomain,
                                standard = selectedStandard,
                                duration = selectedDuration,
                                fieldDefs = fieldDefs,
                                fieldInputs = fieldInputs.toMap(),
                                calculationResult = calculationResult
                            )
                        }
                    )
                }
            }

            1 -> {
                // TAB 1: SOP & Standard Procedure
                item {
                    StandardProcedureTabContent(
                        domain = selectedDomain,
                        procedure = PollutionMonitoringRepository.getStandardProcedure(selectedDomain)
                    )
                }
            }

            2 -> {
                // TAB 2: Instrument Specs, Principle & Manual
                item {
                    InstrumentManualTabContent(
                        domain = selectedDomain,
                        manual = PollutionMonitoringRepository.getInstrumentManual(selectedDomain)
                    )
                }
            }

            3 -> {
                // TAB 3: Multi-Standard Comparison Matrix (Side-by-Side across 8 global standards)
                item {
                    MultiStandardMatrixTabContent(
                        domain = selectedDomain,
                        duration = selectedDuration,
                        activeStandard = selectedStandard,
                        calculatedValue = calculationResult.mainCalculatedValue
                    )
                }
            }

            4 -> {
                // TAB 4: Field Data Sheet Export, Chart & Audit Log
                item {
                    FdsExportAndAuditTabContent(
                        domain = selectedDomain,
                        standard = selectedStandard,
                        duration = selectedDuration,
                        fieldDefs = fieldDefs,
                        fieldInputs = fieldInputs,
                        calculationResult = calculationResult,
                        context = context
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

// -----------------------------------------------------------------------------
// COMPONENT: Compliance Evaluation Hero Card
// -----------------------------------------------------------------------------
@Composable
private fun ComplianceEvaluationCard(
    calculationResult: LiveCalculationResult,
    onDownloadFieldSheet: () -> Unit
) {
    val status = calculationResult.complianceStatus
    val cardColor = when (status) {
        ComplianceStatus.COMPLIANT -> Color(0xFF0F6E43).copy(alpha = 0.12f)
        ComplianceStatus.WARNING -> Color(0xFFE65100).copy(alpha = 0.12f)
        ComplianceStatus.EXCEEDED -> Color(0xFFBA1A1A).copy(alpha = 0.12f)
    }
    val borderColor = when (status) {
        ComplianceStatus.COMPLIANT -> Color(0xFF0F6E43)
        ComplianceStatus.WARNING -> Color(0xFFE65100)
        ComplianceStatus.EXCEEDED -> Color(0xFFBA1A1A)
    }
    val icon = when (status) {
        ComplianceStatus.COMPLIANT -> Icons.Default.CheckCircle
        ComplianceStatus.WARNING -> Icons.Default.Warning
        ComplianceStatus.EXCEEDED -> Icons.Default.Warning
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, borderColor, RoundedCornerShape(16.dp))
            .testTag("compliance_evaluation_card")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = borderColor,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = status.label,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = borderColor
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = borderColor.copy(alpha = 0.15f),
                    modifier = Modifier.padding(2.dp)
                ) {
                    Text(
                        text = calculationResult.chosenStandard.code,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = borderColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "LIVE CALCULATED RESULT:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = calculationResult.formattedMainResult,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = borderColor
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "REGULATORY THRESHOLD:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = String.format(Locale.US, "%.1f %s", calculationResult.thresholdLimit, calculationResult.mainDisplayUnit),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = borderColor.copy(alpha = 0.25f))
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = calculationResult.complianceSummary,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = calculationResult.legalCitation,
                fontSize = 11.sp,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = borderColor.copy(alpha = 0.2f))
            Spacer(modifier = Modifier.height(10.dp))

            // Prominent Download Field Sheet Button
            Button(
                onClick = onDownloadFieldSheet,
                colors = ButtonDefaults.buttonColors(
                    containerColor = borderColor,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("download_field_sheet_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = "Download Field Sheet",
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Download Field Sheet",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// -----------------------------------------------------------------------------
// TAB 0: Field Data Sheet (FDS) with Live Calculation
// -----------------------------------------------------------------------------
@Composable
private fun FieldDataSheetTabContent(
    domain: PollutionMonitoringDomain,
    fieldDefs: List<MonitoringFieldDef>,
    fieldInputs: Map<String, String>,
    calculationResult: LiveCalculationResult,
    onInputValueChanged: (String, String) -> Unit,
    onDownloadFieldSheet: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Section description with Download Field Sheet Action Button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "INTERACTIVE FIELD DATA SHEET (FDS)",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            OutlinedButton(
                onClick = onDownloadFieldSheet,
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier.testTag("fds_tab_download_field_sheet_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Print,
                    contentDescription = null,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Download Field Sheet",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Live calculation formula breakdown card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .testTag("live_formula_steps_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Calculate,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Real-Time Mathematical Step-by-Step Derivation",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))

                calculationResult.stepByStepFormulas.forEachIndexed { index, (label, formula) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(
                            text = "${index + 1}. $label",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1.2f)
                        )
                        Text(
                            text = formula,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1.8f)
                        )
                    }
                    if (index < calculationResult.stepByStepFormulas.size - 1) {
                        HorizontalDivider(
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f),
                            modifier = Modifier.padding(vertical = 2.dp)
                        )
                    }
                }
            }
        }

        // Dynamic Input Fields Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
                .testTag("fds_input_fields_card")
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Field Raw Parameters & Telemetry Inputs",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Edit any field to immediately recalculate concentration and compliance status.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                fieldDefs.forEach { def ->
                    val currentValue = fieldInputs[def.id] ?: def.defaultValue
                    Column(modifier = Modifier.padding(vertical = 6.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = def.label,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = def.unit,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        OutlinedTextField(
                            value = currentValue,
                            onValueChange = { onInputValueChanged(def.id, it) },
                            placeholder = { Text(def.defaultValue) },
                            keyboardOptions = if (def.isNumeric) {
                                KeyboardOptions(keyboardType = KeyboardType.Number)
                            } else {
                                KeyboardOptions.Default
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("field_input_${def.id}")
                        )

                        Text(
                            text = def.description,
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 2.dp, start = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// TAB 1: Standard Operating Procedure (SOP)
// -----------------------------------------------------------------------------
@Composable
private fun StandardProcedureTabContent(
    domain: PollutionMonitoringDomain,
    procedure: StandardProcedureDetails
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Procedure Header Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = procedure.standardTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "Regulatory SOP Code: ${procedure.regulatoryCode}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = procedure.scope,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Section 1: Pre-Sampling Preparation
        ProcedureStepSection(
            stepNumber = "1",
            title = "Pre-Sampling Filter & Sensor Conditioning",
            items = procedure.preSamplingProtocol
        )

        // Section 2: Instrument Setup & Leak Check
        ProcedureStepSection(
            stepNumber = "2",
            title = "Instrument Setup, Deployment & Leak Check",
            items = procedure.instrumentSetupAndLeakCheck
        )

        // Section 3: Sampling Execution Protocol
        ProcedureStepSection(
            stepNumber = "3",
            title = "Field Sampling Execution & Flow Logging",
            items = procedure.samplingExecutionProtocol
        )

        // Section 4: Sample Handling, Transport & Preservation
        ProcedureStepSection(
            stepNumber = "4",
            title = "Sample Recovery, Preservation & Chain of Custody",
            items = procedure.sampleHandlingAndPreservation
        )

        // Section 5: Governing Mathematical Formula
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Governing Mathematical & Conversion Equations:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = procedure.mathCalculationFormula,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}

@Composable
private fun ProcedureStepSection(
    stepNumber: String,
    title: String,
    items: List<String>
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier
            .fillMaxWidth()
            .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = stepNumber,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            items.forEachIndexed { idx, point ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 6.dp)
                    )
                    Text(
                        text = point,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// TAB 2: Instrument Specs, Principle & Operating Manual
// -----------------------------------------------------------------------------
@Composable
private fun InstrumentManualTabContent(
    domain: PollutionMonitoringDomain,
    manual: InstrumentManualDetails
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Equipment Identity Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Speed,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = manual.instrumentName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Make / Reference Models: ${manual.makeAndModel}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "OPERATING PRINCIPLE & PHYSICS:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = manual.operatingPrinciple,
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Specifications Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Technical Specifications & Operating Envelope",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Dynamic Measurement Range", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(manual.measurementRange, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Accuracy & Resolution", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(manual.accuracyAndResolution, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Environmental Limits", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(manual.operatingEnvironmentLimits, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Calibration Protocol
        ProcedureStepSection(
            stepNumber = "CAL",
            title = "Calibration & Verification Protocol",
            items = manual.calibrationProtocol
        )

        // Zero & Span Procedure
        ProcedureStepSection(
            stepNumber = "Z/S",
            title = "Zero & Span Adjustment Procedure",
            items = manual.zeroAndSpanProcedure
        )

        // Maintenance & Servicing
        ProcedureStepSection(
            stepNumber = "MNT",
            title = "Routine Maintenance & Sensor Servicing",
            items = manual.maintenanceAndServicing
        )
    }
}

// -----------------------------------------------------------------------------
// TAB 3: Multi-Standard Comparison Matrix (Side-by-Side across 8 global standards)
// -----------------------------------------------------------------------------
@Composable
private fun MultiStandardMatrixTabContent(
    domain: PollutionMonitoringDomain,
    duration: MonitoringDuration,
    activeStandard: RegulatoryStandard,
    calculatedValue: Double
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.CompareArrows,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Global Standards Benchmark Matrix",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Live comparison of your calculated reading against all 8 national and international regulatory jurisdictions for ${duration.title}.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        RegulatoryStandard.values().forEach { standard ->
            val benchmark = PollutionMonitoringRepository.getStandardBenchmark(domain, standard, duration)
            val isCurrentActive = standard == activeStandard

            // Calculate status against this specific standard
            val limit = benchmark.limitValue
            val isCompliant = if (domain == PollutionMonitoringDomain.PAINT_BOOTH || domain == PollutionMonitoringDomain.LUX_MONITORING) {
                calculatedValue >= limit
            } else {
                calculatedValue <= limit
            }

            val badgeColor = if (isCompliant) Color(0xFF0F6E43) else Color(0xFFBA1A1A)
            val badgeText = if (isCompliant) "COMPLIANT" else "NON-COMPLIANT"

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentActive) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isCurrentActive) 1.5.dp else 0.5.dp,
                        color = if (isCurrentActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = standard.displayName,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isCurrentActive) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = MaterialTheme.colorScheme.primary
                                ) {
                                    Text(
                                        text = "SELECTED",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = badgeColor.copy(alpha = 0.15f)
                        ) {
                            Text(
                                text = badgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = badgeColor,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Statutory Limit: ${String.format(Locale.US, "%,.1f", benchmark.limitValue)} ${benchmark.unit}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Authority: ${standard.region}",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Rule: ${benchmark.citationRule}",
                        fontSize = 11.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = benchmark.advisoryNote,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// TAB 4: Field Data Sheet Export, Chart & Audit Log
// -----------------------------------------------------------------------------
@Composable
private fun FdsExportAndAuditTabContent(
    domain: PollutionMonitoringDomain,
    standard: RegulatoryStandard,
    duration: MonitoringDuration,
    fieldDefs: List<MonitoringFieldDef>,
    fieldInputs: Map<String, String>,
    calculationResult: LiveCalculationResult,
    context: Context
) {
    val timestamp = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(Date()) }
    var exportSuccessMessage by remember { mutableStateOf<String?>(null) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Export Action Card
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f), RoundedCornerShape(14.dp))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Formal Field Data Sheet (FDS) Report",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Generate and share certified field monitoring audit logs with complete calculation audit trails.",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Primary Download Field Sheet (Print Dialog / PDF) Action
                Button(
                    onClick = {
                        FieldSheetReportPrinter.printFieldSheet(
                            context = context,
                            domain = domain,
                            standard = standard,
                            duration = duration,
                            fieldDefs = fieldDefs,
                            fieldInputs = fieldInputs,
                            calculationResult = calculationResult
                        )
                        exportSuccessMessage = "Opening print dialog for certified A4 PDF report."
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF0F6E43),
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("export_download_field_sheet_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Download Field Sheet",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = {
                            val report = buildFdsReportText(
                                domain = domain,
                                standard = standard,
                                duration = duration,
                                fieldDefs = fieldDefs,
                                fieldInputs = fieldInputs,
                                result = calculationResult,
                                timestamp = timestamp
                            )
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_SUBJECT, "FDS Audit Report - ${domain.title}")
                                putExtra(Intent.EXTRA_TEXT, report)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share Field Data Sheet"))
                            exportSuccessMessage = "Report dispatched to system share sheet."
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("share_fds_report_btn")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Share FDS Report")
                    }

                    OutlinedButton(
                        onClick = {
                            val csvContent = buildFdsCsvContent(
                                domain = domain,
                                standard = standard,
                                duration = duration,
                                fieldDefs = fieldDefs,
                                fieldInputs = fieldInputs,
                                result = calculationResult,
                                timestamp = timestamp
                            )
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/csv"
                                putExtra(Intent.EXTRA_SUBJECT, "FDS_${domain.name}_${duration.name}.csv")
                                putExtra(Intent.EXTRA_TEXT, csvContent)
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Export RFC-4180 CSV"))
                            exportSuccessMessage = "CSV ready for export."
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("export_fds_csv_btn")
                    ) {
                        Text("Export CSV")
                    }
                }

                if (exportSuccessMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = exportSuccessMessage ?: "",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F6E43)
                    )
                }
            }
        }

        // Preview of Certified Sheet
        Card(
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "FIELD DATA SHEET AUDIT RECORD PREVIEW",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "PROJECT: EDEN ENVIRONMENTAL INTELLIGENCE SUITE",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "DOMAIN: ${domain.title.uppercase(Locale.US)}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "SAMPLING TIMING: ${duration.title} | TIMESTAMP: $timestamp",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "GOVERNING JURISDICTION: ${standard.displayName}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "--------------------------------------------------------",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.outline
                        )

                        fieldDefs.forEach { def ->
                            val v = fieldInputs[def.id] ?: def.defaultValue
                            Text(
                                text = "${def.label}: $v ${def.unit}",
                                fontSize = 10.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }

                        Text(
                            text = "--------------------------------------------------------",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = "CALCULATED VALUE: ${calculationResult.formattedMainResult}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "REGULATORY CEILING: ${calculationResult.thresholdLimit} ${calculationResult.mainDisplayUnit}",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "COMPLIANCE STATUS: ${calculationResult.complianceStatus.label}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// HELPER FUNCTIONS & EXPORT GENERATORS
// -----------------------------------------------------------------------------
private fun getDomainIcon(domain: PollutionMonitoringDomain): ImageVector {
    return when (domain) {
        PollutionMonitoringDomain.AMBIENT_AIR -> Icons.Default.Air
        PollutionMonitoringDomain.INDOOR_AIR -> Icons.Default.MeetingRoom
        PollutionMonitoringDomain.WORKZONE_AIR -> Icons.Default.Engineering
        PollutionMonitoringDomain.NOISE -> Icons.AutoMirrored.Filled.VolumeUp
        PollutionMonitoringDomain.STACK_EMISSION -> Icons.Default.CloudUpload
        PollutionMonitoringDomain.FLUE_GAS -> Icons.Default.LocalFireDepartment
        PollutionMonitoringDomain.PAINT_BOOTH -> Icons.Default.FormatPaint
        PollutionMonitoringDomain.AIR_MICROBIOLOGY -> Icons.Default.Biotech
        PollutionMonitoringDomain.CLEAN_ROOM -> Icons.Default.CleanHands
        PollutionMonitoringDomain.LUX_MONITORING -> Icons.Default.Lightbulb
        PollutionMonitoringDomain.WATER_MONITORING -> Icons.Default.WaterDrop
    }
}

private fun buildFdsReportText(
    domain: PollutionMonitoringDomain,
    standard: RegulatoryStandard,
    duration: MonitoringDuration,
    fieldDefs: List<MonitoringFieldDef>,
    fieldInputs: Map<String, String>,
    result: LiveCalculationResult,
    timestamp: String
): String {
    val sb = StringBuilder()
    sb.appendLine("==============================================================")
    sb.appendLine("EDEN ENVIRONMENTAL INTELLIGENCE - CERTIFIED FIELD DATA SHEET")
    sb.appendLine("==============================================================")
    sb.appendLine("Domain: ${domain.title}")
    sb.appendLine("Timing Mode: ${duration.title} (${duration.durationHours} hours)")
    sb.appendLine("Governing Standard: ${standard.displayName} (${standard.authority})")
    sb.appendLine("Generation Timestamp: $timestamp")
    sb.appendLine()
    sb.appendLine("----------------- RAW FIELD MEASUREMENTS -----------------")
    fieldDefs.forEach { def ->
        val v = fieldInputs[def.id] ?: def.defaultValue
        sb.appendLine("${def.label}: $v ${def.unit}")
    }
    sb.appendLine()
    sb.appendLine("----------------- MATHEMATICAL DERIVATION -----------------")
    result.stepByStepFormulas.forEachIndexed { i, (l, f) ->
        sb.appendLine("${i + 1}. $l => $f")
    }
    sb.appendLine()
    sb.appendLine("----------------- FINAL COMPLIANCE AUDIT -----------------")
    sb.appendLine("Calculated Result: ${result.formattedMainResult}")
    sb.appendLine("Regulatory Limit: ${result.thresholdLimit} ${result.mainDisplayUnit}")
    sb.appendLine("Compliance Status: ${result.complianceStatus.label}")
    sb.appendLine("Summary: ${result.complianceSummary}")
    sb.appendLine("Citation: ${result.legalCitation}")
    sb.appendLine("==============================================================")
    return sb.toString()
}

private fun buildFdsCsvContent(
    domain: PollutionMonitoringDomain,
    standard: RegulatoryStandard,
    duration: MonitoringDuration,
    fieldDefs: List<MonitoringFieldDef>,
    fieldInputs: Map<String, String>,
    result: LiveCalculationResult,
    timestamp: String
): String {
    val sb = StringBuilder()
    sb.appendLine("Timestamp,Domain,Duration,Standard,Parameter,CalculatedValue,Unit,ThresholdLimit,ComplianceStatus")
    sb.append("\"$timestamp\",")
    sb.append("\"${domain.title}\",")
    sb.append("\"${duration.title}\",")
    sb.append("\"${standard.code}\",")
    sb.append("\"${result.parameterEvaluated}\",")
    sb.append("${result.mainCalculatedValue},")
    sb.append("\"${result.mainDisplayUnit}\",")
    sb.append("${result.thresholdLimit},")
    sb.appendLine("\"${result.complianceStatus.name}\"")
    sb.appendLine()
    sb.appendLine("FieldParameter,MeasuredValue,Unit,Description")
    fieldDefs.forEach { def ->
        val v = fieldInputs[def.id] ?: def.defaultValue
        sb.appendLine("\"${def.label}\",\"$v\",\"${def.unit}\",\"${def.description}\"")
    }
    return sb.toString()
}
