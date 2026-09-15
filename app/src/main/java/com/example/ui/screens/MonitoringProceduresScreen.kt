package com.example.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.monitoring.data.MonitoringProceduresRepository
import com.example.monitoring.model.FdsInstrumentInfo
import com.example.monitoring.model.FdsProjectInfo
import com.example.monitoring.model.MonitoringProcedureDoc
import com.example.monitoring.model.MonitoringProcedureDomain
import com.example.monitoring.util.PdfFieldDataSheetGenerator
import com.example.viewmodel.EdenViewModel

@Composable
fun MonitoringProceduresScreen(
    viewModel: EdenViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val selectedDomain by viewModel.selectedProcedureDomain.collectAsState()

    var activeSubView by remember { mutableStateOf(0) } // 0 = Printable PDF FDS, 1 = Standard Operating Procedure (SOP), 2 = Regulatory Matrix

    val procedureDoc = remember(selectedDomain) {
        MonitoringProceduresRepository.getProcedure(selectedDomain)
    }

    val fdsTemplate = remember(selectedDomain) {
        MonitoringProceduresRepository.getFieldDataSheetTemplate(selectedDomain)
    }

    // Editable Project Info state
    var projectInfo by remember(selectedDomain) {
        mutableStateOf(fdsTemplate.defaultProjectInfo)
    }

    // Editable Instrument Info state
    var instrumentInfo by remember(selectedDomain) {
        mutableStateOf(fdsTemplate.defaultInstrumentInfo)
    }

    var showEditDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top Header Banner
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Monitoring Procedures & FDS",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f)
                            ) {
                                Text(
                                    text = "ISO • USEPA • Local",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Standard Operating Procedures & Printable PDF Field Data Sheets",
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 5-Domain Horizontal Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    MonitoringProcedureDomain.values().forEach { domain ->
                        val isSelected = domain == selectedDomain
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectProcedureDomain(domain) },
                            label = {
                                Text(
                                    text = domain.shortName,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 12.sp
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
                            modifier = Modifier.testTag("domain_chip_${domain.id}")
                        )
                    }
                }
            }
        }

        // Sub-View Segmented Tab Selector (Printable PDF FDS vs SOP vs Regulatory Matrix)
        Surface(
            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 6.dp)
                .clip(RoundedCornerShape(10.dp))
        ) {
            Row(modifier = Modifier.padding(3.dp)) {
                listOf(
                    "📄 Printable PDF FDS",
                    "📋 Standard Procedure (SOP)",
                    "⚖️ Regulatory Requirements"
                ).forEachIndexed { index, label ->
                    val selected = activeSubView == index
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { activeSubView = index }
                            .testTag("subview_tab_$index")
                    ) {
                        Text(
                            text = label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(vertical = 7.dp)
                        )
                    }
                }
            }
        }

        // Main Content Area
        Box(modifier = Modifier.fillMaxSize()) {
            when (activeSubView) {
                0 -> PrintableFdsView(
                    template = fdsTemplate,
                    projectInfo = projectInfo,
                    instrumentInfo = instrumentInfo,
                    showEditSection = showEditDialog,
                    onToggleEditSection = { showEditDialog = !showEditDialog },
                    onUpdateProjectInfo = { projectInfo = it },
                    onUpdateInstrumentInfo = { instrumentInfo = it },
                    onResetDefaults = {
                        projectInfo = fdsTemplate.defaultProjectInfo
                        instrumentInfo = fdsTemplate.defaultInstrumentInfo
                        Toast.makeText(context, "Reset to default certified sample values", Toast.LENGTH_SHORT).show()
                    },
                    onPrintPdf = {
                        val pdf = PdfFieldDataSheetGenerator.generatePdfFile(context, fdsTemplate, projectInfo, instrumentInfo)
                        PdfFieldDataSheetGenerator.printPdf(context, pdf, "FDS_${fdsTemplate.domain.id}")
                    },
                    onSharePdf = {
                        val pdf = PdfFieldDataSheetGenerator.generatePdfFile(context, fdsTemplate, projectInfo, instrumentInfo)
                        PdfFieldDataSheetGenerator.sharePdf(context, pdf)
                    },
                    onOpenPdf = {
                        val pdf = PdfFieldDataSheetGenerator.generatePdfFile(context, fdsTemplate, projectInfo, instrumentInfo)
                        PdfFieldDataSheetGenerator.openPdf(context, pdf)
                    }
                )

                1 -> StandardProcedureView(doc = procedureDoc)

                2 -> RegulatoryRequirementsView(doc = procedureDoc)
            }
        }
    }
}

@Composable
private fun PrintableFdsView(
    template: com.example.monitoring.model.FieldDataSheetTemplate,
    projectInfo: FdsProjectInfo,
    instrumentInfo: FdsInstrumentInfo,
    showEditSection: Boolean,
    onToggleEditSection: () -> Unit,
    onUpdateProjectInfo: (FdsProjectInfo) -> Unit,
    onUpdateInstrumentInfo: (FdsInstrumentInfo) -> Unit,
    onResetDefaults: () -> Unit,
    onPrintPdf: () -> Unit,
    onSharePdf: () -> Unit,
    onOpenPdf: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // PDF Action Controls Bar
        item {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
                ),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PictureAsPdf,
                                contentDescription = null,
                                tint = Color(0xFFC62828),
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Printable A4 PDF Field Data Sheet",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "ISO 17025 & USEPA QAPP standard format with legal sign-off",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        IconButton(
                            onClick = onToggleEditSection,
                            modifier = Modifier.size(32.dp).testTag("btn_toggle_edit_fds")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit Fields",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = onPrintPdf,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1.3f)
                                .height(38.dp)
                                .testTag("btn_download_field_sheet")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Print,
                                contentDescription = "Download Field Sheet",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Download Field Sheet", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = onSharePdf,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(38.dp)
                                .testTag("btn_share_pdf")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share",
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Share PDF", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = onOpenPdf,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .weight(0.9f)
                                .height(38.dp)
                                .testTag("btn_open_pdf")
                        ) {
                            Text("View", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Optional Customizer Collapsible Card
        item {
            AnimatedVisibility(visible = showEditSection) {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "Customize Field Data Sheet Fields",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Reset",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier
                                    .clickable { onResetDefaults() }
                                    .padding(4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = projectInfo.facilityName,
                            onValueChange = { onUpdateProjectInfo(projectInfo.copy(facilityName = it)) },
                            label = { Text("Facility / Client Name", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth().testTag("input_facility_name")
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = projectInfo.stationId,
                                onValueChange = { onUpdateProjectInfo(projectInfo.copy(stationId = it)) },
                                label = { Text("Station ID", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f).testTag("input_station_id")
                            )
                            OutlinedTextField(
                                value = projectInfo.monitoringDate,
                                onValueChange = { onUpdateProjectInfo(projectInfo.copy(monitoringDate = it)) },
                                label = { Text("Monitoring Date", fontSize = 11.sp) },
                                singleLine = true,
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = projectInfo.technicianName,
                            onValueChange = { onUpdateProjectInfo(projectInfo.copy(technicianName = it)) },
                            label = { Text("Lead Field Chemist / Officer", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = instrumentInfo.instrumentModel,
                            onValueChange = { onUpdateInstrumentInfo(instrumentInfo.copy(instrumentModel = it)) },
                            label = { Text("Instrument Make & Model", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }

        // Live Simulated A4 Sheet Preview Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Header Bar
                    Surface(
                        color = Color(0xFF0F6E43),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "EDEN ENVIRONMENTAL QMS",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "ISO/IEC 17025 CERTIFIED",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 9.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = template.formTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Doc Ref: ${template.documentNumber}",
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // 1. Project & Facility Identification Box
                    PreviewSectionTitle("1. PROJECT & FACILITY IDENTIFICATION")
                    PreviewGridRow("Facility:", projectInfo.facilityName)
                    PreviewGridRow("Location:", projectInfo.siteLocation)
                    PreviewGridRow("Station ID:", projectInfo.stationId, "Date:", projectInfo.monitoringDate)
                    PreviewGridRow("Chemist:", projectInfo.technicianName, "Reviewer:", projectInfo.reviewerName)
                    PreviewGridRow("Weather:", projectInfo.weatherCondition)

                    Spacer(modifier = Modifier.height(10.dp))

                    // 2. Instrument & Calibration Verification Box
                    PreviewSectionTitle("2. CERTIFIED INSTRUMENTATION & CALIBRATION")
                    PreviewGridRow("Model:", instrumentInfo.instrumentModel, "Serial No:", instrumentInfo.serialNumber)
                    PreviewGridRow("Cert No:", instrumentInfo.calibrationCertNumber, "Valid Until:", instrumentInfo.calibrationValidUntil)
                    PreviewGridRow("Pre-Cal Check:", instrumentInfo.preCalReading, "Post-Cal Check:", instrumentInfo.postCalReading)
                    PreviewGridRow("Drift Rate:", instrumentInfo.calibrationDrift)

                    Spacer(modifier = Modifier.height(10.dp))

                    // 3. Pre-Sampling Checklist
                    PreviewSectionTitle("3. PRE-SAMPLING VERIFICATION & LEAK CHECKS")
                    template.preSamplingChecklist.forEach { checkItem ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 2.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(3.dp),
                                color = Color(0xFF0F6E43),
                                modifier = Modifier.size(13.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(10.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = checkItem,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 4. Sampling Run Table
                    PreviewSectionTitle("4. SAMPLING RUN DATA & FIELD SENSOR LOGS")
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            // Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                    .padding(vertical = 5.dp, horizontal = 6.dp)
                            ) {
                                template.runTableHeaders.take(4).forEach { colHeader ->
                                    Text(
                                        text = colHeader,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))

                            // Rows
                            template.sampleRunData.forEachIndexed { idx, rowData ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(if (idx % 2 == 1) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f) else Color.Transparent)
                                        .padding(vertical = 5.dp, horizontal = 6.dp)
                                ) {
                                    Text(rowData.pointOrTime, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                    Text(rowData.param1, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                    Text(rowData.param2, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                    Text(rowData.param3, fontSize = 10.sp, modifier = Modifier.weight(1f))
                                }
                                if (idx < template.sampleRunData.size - 1) {
                                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 5. Calculations & Derivations
                    PreviewSectionTitle("5. FIELD CALCULATIONS & DERIVATIONS")
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f),
                        border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            template.calculations.forEach { (label, value) ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 2.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(label, fontSize = 10.5.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
                                    Text(value, fontSize = 10.5.sp, color = Color(0xFF0F6E43), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 6. Regulatory Verdict Banner
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (template.isCompliant) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (template.isCompliant) Color(0xFF2E7D32) else Color(0xFFC62828)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (template.isCompliant) Color(0xFF2E7D32) else Color(0xFFC62828),
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "STATUTORY VERDICT: ${if (template.isCompliant) "PASSED - COMPLIANT" else "NON-COMPLIANT"}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (template.isCompliant) Color(0xFF1B5E20) else Color(0xFFB71C1C)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Final Evaluated Result: ${template.measuredResult}",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = template.regulatoryVerdict,
                                fontSize = 9.5.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // 7. Chain of Custody & Dual Sign-off
                    PreviewSectionTitle("6. CHAIN OF CUSTODY & STATUTORY SIGN-OFF")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("Lead Field Sampler", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(projectInfo.technicianName, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Sign: ___________________", fontSize = 9.sp, color = Color.Gray)
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("QA/QC Reviewer", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text(projectInfo.reviewerName, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Sign: ___________________", fontSize = 9.sp, color = Color.Gray)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StandardProcedureView(doc: MonitoringProcedureDoc) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Procedure Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = doc.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Standard Operating Procedure: ${doc.regulatoryCode}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(10.dp))

                    // Standards reference badges
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        StandardRefChip("ISO", doc.isoStandardRef, Color(0xFF0D47A1))
                        StandardRefChip("USEPA", doc.usepaStandardRef, Color(0xFF1B5E20))
                        StandardRefChip("LOCAL", doc.localStandardRef, Color(0xFFBF360C))
                    }
                }
            }
        }

        // Scope & Principle
        item {
            ProcedureCard(title = "1. Scope & Operating Principle", icon = Icons.Default.Description) {
                Text(
                    text = "Scope & Target Applications:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = doc.scopeAndObjective,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Principle of Analytical Method:",
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = doc.principleOfMethod,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Required Equipment
        item {
            ProcedureCard(title = "2. Required Equipment, Reagents & Standards", icon = Icons.Default.Verified) {
                doc.requiredEquipmentAndReagents.forEachIndexed { index, item ->
                    BulletText("${index + 1}. $item")
                }
            }
        }

        // Pre-Sampling Checks
        item {
            ProcedureCard(title = "3. Pre-Monitoring Verification & Leak Test Protocols", icon = Icons.Default.CheckCircle) {
                doc.preSamplingVerificationAndLeakCheck.forEach { check ->
                    BulletText("• $check")
                }
            }
        }

        // Step-by-Step Procedure
        item {
            ProcedureCard(title = "4. Step-by-Step Field Sampling Execution", icon = Icons.AutoMirrored.Filled.MenuBook) {
                doc.samplingExecutionProtocol.forEachIndexed { index, step ->
                    BulletText("Step ${index + 1}: $step")
                }
            }
        }

        // Sample Handling & Preservation
        item {
            ProcedureCard(title = "5. Sample Handling, Preservation & Chain of Custody", icon = Icons.Default.Description) {
                doc.sampleHandlingAndPreservation.forEach { protocol ->
                    BulletText("• $protocol")
                }
            }
        }

        // QA/QC Criteria
        item {
            ProcedureCard(title = "6. Quality Assurance / Quality Control (QA/QC) Acceptance Rules", icon = Icons.Default.CheckCircle) {
                doc.qaQcProtocols.forEach { qaItem ->
                    BulletText("✓ $qaItem")
                }
            }
        }

        // Governing Mathematical Formulas
        item {
            ProcedureCard(title = "7. Governing Mathematical Formulas & Normalization", icon = Icons.Default.Gavel) {
                doc.governingFormulas.forEach { (label, formula) ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(text = label, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Text(text = formula, fontSize = 10.5.sp, fontFamily = FontFamily.Monospace, color = MaterialTheme.colorScheme.onSurface)
                    }
                }
            }
        }
    }
}

@Composable
private fun RegulatoryRequirementsView(doc: MonitoringProcedureDoc) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 14.dp, vertical = 6.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Gavel,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Standardized Regulatory Thresholds Comparison",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Benchmark limits across ISO (International), USEPA (US Federal), and Local Statutory Standards",
                        fontSize = 10.5.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    doc.regulatoryBenchmarks.forEach { bench ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                            border = androidx.compose.foundation.BorderStroke(0.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = bench.parameter,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = bench.averagingTime,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))

                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("ISO Standard:", fontSize = 9.sp, color = Color(0xFF0D47A1), fontWeight = FontWeight.Bold)
                                        Text(bench.isoLimit, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("USEPA Regulation:", fontSize = 9.sp, color = Color(0xFF1B5E20), fontWeight = FontWeight.Bold)
                                        Text(bench.usepaLimit, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text("Local Standards:", fontSize = 9.sp, color = Color(0xFFBF360C), fontWeight = FontWeight.Bold)
                                        Text(bench.localLimit, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = "Statutory Quality Assurance & Retention Mandate",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• Record Retention: Under ISO/IEC 17025 Clause 8.4 and USEPA CFR guidelines, all original completed Field Data Sheets, calibration logs, and Chain of Custody forms must be securely archived for a minimum statutory period of 5 years.\n" +
                                "• Calibration Traceability: All transfer standards (orifices, Class 1 sound calibrators, weights) must carry active calibration certificates issued by an ILAC-MRA / NIST / NPL accredited metrology laboratory.\n" +
                                "• Data Integrity: Any handwritten or digitized corrections on field data sheets must follow Good Laboratory Practice (GLP) single-line strike-through with technician initials and date.",
                        fontSize = 11.sp,
                        lineHeight = 16.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ProcedureCard(title: String, icon: ImageVector, content: @Composable () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
private fun BulletText(text: String) {
    Text(
        text = text,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(vertical = 2.dp)
    )
}

@Composable
private fun PreviewSectionTitle(title: String) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
        )
    }
    Spacer(modifier = Modifier.height(4.dp))
}

@Composable
private fun PreviewGridRow(label1: String, val1: String, label2: String? = null, val2: String? = null) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
    ) {
        Row(modifier = Modifier.weight(1f)) {
            Text(label1, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.width(4.dp))
            Text(val1, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurface)
        }
        if (label2 != null && val2 != null) {
            Row(modifier = Modifier.weight(1f)) {
                Text(label2, fontSize = 9.5.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(modifier = Modifier.width(4.dp))
                Text(val2, fontSize = 9.5.sp, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun StandardRefChip(title: String, code: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(6.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(0.5.dp, color.copy(alpha = 0.3f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                text = title,
                fontSize = 9.5.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = code,
                fontSize = 9.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

private fun getDomainIcon(domain: MonitoringProcedureDomain): ImageVector {
    return when (domain) {
        MonitoringProcedureDomain.AMBIENT -> Icons.Default.Air
        MonitoringProcedureDomain.INDOOR -> Icons.Default.MeetingRoom
        MonitoringProcedureDomain.NOISE -> Icons.AutoMirrored.Filled.VolumeUp
        MonitoringProcedureDomain.STACK -> Icons.Default.CloudUpload
        MonitoringProcedureDomain.WATER -> Icons.Default.WaterDrop
    }
}
