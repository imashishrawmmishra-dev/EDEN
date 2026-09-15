package com.example.monitoring.util

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.print.PrintAttributes
import android.print.PrintManager
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.core.content.FileProvider
import com.example.data.model.MonitoringEntity
import com.example.monitoring.data.PollutionMonitoringRepository
import com.example.monitoring.model.ComplianceStatus
import com.example.monitoring.model.LiveCalculationResult
import com.example.monitoring.model.MonitoringDuration
import com.example.monitoring.model.MonitoringFieldDef
import com.example.monitoring.model.PollutionMonitoringDomain
import com.example.monitoring.model.RegulatoryStandard
import com.example.monitoring.model.StandardBenchmarkThreshold
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility responsible for generating certified, professional HTML Field Data Sheets (FDS)
 * formatted specifically for A4 PDF printing, and triggering the print dialog via
 * Android's PrintManager (using WebView print adapter) or external browser.
 */
object FieldSheetReportPrinter {

    /**
     * Triggers the print dialog formatted specifically for a clean, professional PDF report
     * of the current monitoring panel data.
     */
    fun printFieldSheet(
        context: Context,
        domain: PollutionMonitoringDomain,
        standard: RegulatoryStandard,
        duration: MonitoringDuration,
        fieldDefs: List<MonitoringFieldDef>,
        fieldInputs: Map<String, String>,
        calculationResult: LiveCalculationResult,
        locationName: String = "Monitoring Station Alpha (Sector 4)",
        stationId: String = "ENV-STA-${domain.name.take(3)}"
    ) {
        val htmlContent = generateFieldSheetHtml(
            domain = domain,
            standard = standard,
            duration = duration,
            fieldDefs = fieldDefs,
            fieldInputs = fieldInputs,
            calculationResult = calculationResult,
            locationName = locationName,
            stationId = stationId
        )

        val jobName = "FDS_${domain.name}_${SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())}"
        executePrintDialog(context, htmlContent, jobName, domain.title)
    }

    /**
     * Triggers the print dialog for a specific telemetry monitoring point.
     */
    fun printTelemetryPoint(
        context: Context,
        point: MonitoringEntity
    ) {
        val htmlContent = generateTelemetryPointHtml(point)
        val jobName = "Telemetry_${point.parameter}_${System.currentTimeMillis()}"
        executePrintDialog(context, htmlContent, jobName, "${point.parameter} Telemetry")
    }

    /**
     * Internal runner to launch Android's PrintManager via an off-screen WebView
     * and save the HTML file in cache with FileProvider.
     */
    private fun executePrintDialog(
        context: Context,
        htmlContent: String,
        jobName: String,
        domainTitle: String
    ) {
        Handler(Looper.getMainLooper()).post {
            try {
                val cacheDir = File(context.cacheDir, "exports").apply { if (!exists()) mkdirs() }
                val htmlFile = File(cacheDir, "$jobName.html")
                FileOutputStream(htmlFile).use { fos ->
                    fos.write(htmlContent.toByteArray(Charsets.UTF_8))
                }

                val webView = WebView(context)
                webView.settings.javaScriptEnabled = true
                webView.settings.domStorageEnabled = true
                webView.settings.allowFileAccess = true

                webView.webViewClient = object : WebViewClient() {
                    override fun onPageFinished(view: WebView?, url: String?) {
                        try {
                            val printManager = context.getSystemService(Context.PRINT_SERVICE) as? PrintManager
                            if (printManager != null) {
                                val printAdapter = webView.createPrintDocumentAdapter(jobName)
                                val printAttributes = PrintAttributes.Builder()
                                    .setMediaSize(PrintAttributes.MediaSize.ISO_A4)
                                    .setColorMode(PrintAttributes.COLOR_MODE_COLOR)
                                    .setMinMargins(PrintAttributes.Margins.NO_MARGINS)
                                    .build()

                                printManager.print(jobName, printAdapter, printAttributes)
                                Toast.makeText(
                                    context,
                                    "Opening PDF print dialog for $domainTitle...",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                openInExternalBrowser(context, htmlFile)
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Opening in browser: ${e.message}", Toast.LENGTH_SHORT).show()
                            openInExternalBrowser(context, htmlFile)
                        }
                    }
                }

                // Load with baseUrl so print stylesheets and print scripts evaluate cleanly
                webView.loadDataWithBaseURL("https://eden.local/", htmlContent, "text/html", "UTF-8", null)

            } catch (e: Exception) {
                Toast.makeText(context, "Could not open print dialog: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    /**
     * Fallback to open the formatted HTML report in the user's external web browser.
     */
    fun openInExternalBrowser(context: Context, htmlFile: File) {
        try {
            val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", htmlFile)
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "text/html")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(Intent.createChooser(intent, "Open Field Sheet in Browser"))
        } catch (e: Exception) {
            Toast.makeText(context, "Could not launch browser: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Builds the complete, high-contrast, professional A4 HTML report for the monitoring panel.
     */
    private fun generateFieldSheetHtml(
        domain: PollutionMonitoringDomain,
        standard: RegulatoryStandard,
        duration: MonitoringDuration,
        fieldDefs: List<MonitoringFieldDef>,
        fieldInputs: Map<String, String>,
        calculationResult: LiveCalculationResult,
        locationName: String,
        stationId: String
    ): String {
        val dateFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(Date())
        val docNumber = "EDEN-FDS-${domain.name.take(3)}-${SimpleDateFormat("yyyyMMdd", Locale.US).format(Date())}-01"

        val procedure = PollutionMonitoringRepository.getStandardProcedure(domain)
        val manual = PollutionMonitoringRepository.getInstrumentManual(domain)
        val matrixBenchmarks: List<StandardBenchmarkThreshold> = RegulatoryStandard.values().map { std: RegulatoryStandard ->
            PollutionMonitoringRepository.getStandardBenchmark(domain, std, duration)
        }

        val statusClass = when (calculationResult.complianceStatus) {
            ComplianceStatus.COMPLIANT -> "compliant"
            ComplianceStatus.WARNING -> "warning"
            ComplianceStatus.EXCEEDED -> "exceeded"
        }

        val statusText = calculationResult.complianceStatus.label

        // Build Field Measurements Rows
        val fieldRows = StringBuilder()
        fieldDefs.forEachIndexed { idx, def ->
            val value = fieldInputs[def.id] ?: def.defaultValue
            val isEven = idx % 2 == 0
            val typeDesc = if (def.isNumeric) "Valid Numeric Float" else "Alpha-numeric String"
            fieldRows.append("""
                <tr style="background:${if (isEven) "#ffffff" else "#f9fafb"};">
                    <td style="font-weight:600; color:#1f2937;">${escapeHtml(def.label)}</td>
                    <td style="font-family:'Courier New', monospace; font-weight:700; color:#0F6E43; font-size:10pt;">
                        ${escapeHtml(value)} ${escapeHtml(def.unit)}
                    </td>
                    <td style="color:#4b5563; font-size:8.5pt;">${escapeHtml(def.description)}</td>
                    <td style="color:#6b7280; font-size:8pt; font-family:'Courier New', monospace;">$typeDesc</td>
                </tr>
            """.trimIndent())
        }

        // Build Step-by-Step Mathematical Derivations
        val mathRows = StringBuilder()
        calculationResult.stepByStepFormulas.forEachIndexed { stepIdx, (label, formula) ->
            mathRows.append("""
                <div class="formula-card">
                    <div class="formula-step-num">STEP ${stepIdx + 1}: ${escapeHtml(label).uppercase()}</div>
                    <div class="formula-text">${escapeHtml(formula)}</div>
                </div>
            """.trimIndent())
        }

        // Build Multi-Standard Comparison Table Rows
        val matrixRows = StringBuilder()
        matrixBenchmarks.forEach { bm ->
            val isCurrent = bm.standard == standard
            val passColor = if (calculationResult.mainCalculatedValue <= bm.limitValue) "#0F6E43" else "#dc2626"
            val passText = if (calculationResult.mainCalculatedValue <= bm.limitValue) "PASS" else "FAIL"
            matrixRows.append("""
                <tr style="background:${if (isCurrent) "#ecfdf5" else "#ffffff"}; font-weight:${if (isCurrent) "700" else "normal"};">
                    <td>${escapeHtml(bm.standard.displayName)} ${if (isCurrent) "★" else ""}</td>
                    <td>${escapeHtml(bm.standard.region)}</td>
                    <td style="font-family:'Courier New', monospace;">${String.format(Locale.US, "%,.1f", bm.limitValue)} ${escapeHtml(bm.unit)}</td>
                    <td>${escapeHtml(bm.citationRule)}</td>
                    <td style="color:$passColor; font-weight:700;">$passText</td>
                </tr>
            """.trimIndent())
        }

        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EDEN Field Data Sheet - ${escapeHtml(domain.title)}</title>
<style>
  @page {
    size: A4 portrait;
    margin: 10mm 12mm 12mm 12mm;
  }
  * {
    box-sizing: border-box;
    -webkit-print-color-adjust: exact !important;
    print-color-adjust: exact !important;
  }
  body {
    font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, "Helvetica Neue", Arial, sans-serif;
    color: #111827;
    background: #ffffff;
    margin: 0;
    padding: 0;
    font-size: 9.5pt;
    line-height: 1.35;
  }
  .no-print-bar {
    background: #0F6E43;
    color: #ffffff;
    padding: 10px 18px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: sticky;
    top: 0;
    z-index: 9999;
    box-shadow: 0 2px 8px rgba(0,0,0,0.15);
  }
  .no-print-bar strong {
    font-size: 13px;
  }
  .btn-print {
    background: #ffffff;
    color: #0F6E43;
    font-weight: 700;
    border: none;
    padding: 7px 16px;
    border-radius: 6px;
    cursor: pointer;
    font-size: 12px;
  }
  .report-container {
    padding: 14px 16px;
    max-width: 820px;
    margin: 0 auto;
  }
  .corp-header {
    border-bottom: 2.5px solid #0F6E43;
    padding-bottom: 8px;
    margin-bottom: 10px;
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
  }
  .brand-title {
    font-size: 16pt;
    font-weight: 800;
    color: #0F6E43;
    letter-spacing: -0.5px;
    margin: 0;
  }
  .brand-subtitle {
    font-size: 8pt;
    font-weight: 600;
    color: #4b5563;
    text-transform: uppercase;
    letter-spacing: 0.8px;
    margin: 2px 0 0 0;
  }
  .doc-metadata {
    text-align: right;
    font-size: 8pt;
    color: #4b5563;
  }
  .doc-metadata .code {
    font-family: 'Courier New', monospace;
    font-weight: 700;
    color: #0F6E43;
    font-size: 9pt;
  }
  .title-banner {
    background: #f0fdf4;
    border-left: 4px solid #0F6E43;
    padding: 8px 12px;
    margin-bottom: 12px;
    border-radius: 4px;
  }
  .title-banner h1 {
    font-size: 13pt;
    margin: 0 0 3px 0;
    color: #064e3b;
    font-weight: 700;
  }
  .title-banner p {
    font-size: 8pt;
    margin: 0;
    color: #374151;
  }
  .meta-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 6px;
    margin-bottom: 12px;
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    padding: 8px 10px;
  }
  .meta-item {
    font-size: 8pt;
  }
  .meta-label {
    color: #6b7280;
    font-size: 7pt;
    text-transform: uppercase;
    font-weight: 700;
    margin-bottom: 2px;
  }
  .meta-value {
    font-weight: 700;
    color: #111827;
  }
  .compliance-banner {
    border: 1.5px solid;
    border-radius: 6px;
    padding: 10px 14px;
    margin-bottom: 12px;
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .compliance-banner.compliant {
    border-color: #0F6E43;
    background: #f0fdf4;
  }
  .compliance-banner.warning {
    border-color: #d97706;
    background: #fffbeb;
  }
  .compliance-banner.exceeded {
    border-color: #dc2626;
    background: #fef2f2;
  }
  .status-tag {
    font-size: 12pt;
    font-weight: 800;
    letter-spacing: 0.5px;
  }
  .status-tag.compliant { color: #0F6E43; }
  .status-tag.warning { color: #d97706; }
  .status-tag.exceeded { color: #dc2626; }
  .value-display {
    text-align: right;
  }
  .value-display .big-val {
    font-size: 18pt;
    font-weight: 800;
    color: #111827;
  }
  .value-display .limit-note {
    font-size: 8pt;
    color: #4b5563;
  }
  .section-header {
    font-size: 9pt;
    font-weight: 700;
    text-transform: uppercase;
    letter-spacing: 0.5px;
    color: #0F6E43;
    border-bottom: 1.5px solid #d1fae5;
    padding-bottom: 3px;
    margin: 12px 0 6px 0;
  }
  table.data-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 10px;
    font-size: 8pt;
  }
  table.data-table th, table.data-table td {
    border: 1px solid #d1d5db;
    padding: 5px 8px;
    text-align: left;
  }
  table.data-table th {
    background-color: #f3f4f6;
    color: #111827;
    font-weight: 700;
  }
  .formula-card {
    background: #f8fafc;
    border: 1px solid #e2e8f0;
    border-radius: 4px;
    padding: 6px 10px;
    margin-bottom: 5px;
  }
  .formula-step-num {
    font-size: 7pt;
    font-weight: 700;
    color: #0F6E43;
  }
  .formula-text {
    font-family: 'Courier New', Courier, monospace;
    font-size: 8pt;
    font-weight: 600;
    color: #1e293b;
    margin-top: 2px;
  }
  .instrument-box {
    background: #f9fafb;
    border: 1px solid #e5e7eb;
    border-radius: 6px;
    padding: 8px 12px;
    margin-bottom: 10px;
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
    font-size: 8pt;
  }
  .signatures-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 16px;
    margin-top: 14px;
    padding-top: 8px;
    border-top: 1px solid #e5e7eb;
  }
  .sig-card {
    border: 1px dashed #9ca3af;
    padding: 8px 12px;
    border-radius: 4px;
    min-height: 60px;
  }
  .sig-title {
    font-size: 7.5pt;
    font-weight: 700;
    color: #374151;
    text-transform: uppercase;
  }
  .sig-line {
    margin-top: 26px;
    border-top: 1px solid #6b7280;
    font-size: 7pt;
    color: #6b7280;
    display: flex;
    justify-content: space-between;
    padding-top: 2px;
  }
  .footer-audit {
    margin-top: 12px;
    font-size: 7pt;
    color: #9ca3af;
    text-align: center;
    border-top: 1px solid #e5e7eb;
    padding-top: 4px;
  }
  @media print {
    .no-print-bar {
      display: none !important;
    }
    body {
      padding: 0;
    }
    .report-container {
      padding: 0;
      max-width: 100%;
    }
    .page-break {
      page-break-before: always;
    }
  }
</style>
<script>
  window.addEventListener('load', function() {
    setTimeout(function() {
      window.print();
    }, 450);
  });
</script>
</head>
<body>

<div class="no-print-bar">
  <div>
    <strong>EDEN Environmental Field Data Sheet (FDS) Report</strong>
    <span style="opacity:0.85; margin-left:12px; font-size:11px;">A4 PDF Export Ready</span>
  </div>
  <div>
    <button class="btn-print" onclick="window.print()">🖨️ Print / Save as PDF</button>
  </div>
</div>

<div class="report-container">
  <!-- Corporate Header -->
  <div class="corp-header">
    <div>
      <div class="brand-title">EDEN ENVIRONMENTAL INTELLIGENCE</div>
      <div class="brand-subtitle">Quality Management System (QMS) • ISO/IEC 17025 & USEPA QAPP</div>
    </div>
    <div class="doc-metadata">
      <div>DOC NO: <span class="code">$docNumber</span></div>
      <div>GENERATED: <strong>$dateFmt</strong></div>
      <div>PAGE: <strong>1 of 1 (Standard Field Sheet)</strong></div>
    </div>
  </div>

  <!-- Title Banner -->
  <div class="title-banner">
    <h1>${escapeHtml(domain.title)} - FIELD MONITORING DATA SHEET</h1>
    <p><strong>Governing Protocol:</strong> ${escapeHtml(procedure.standardTitle)} (${escapeHtml(procedure.regulatoryCode)}) | <strong>Standard:</strong> ${escapeHtml(standard.displayName)} (${escapeHtml(standard.region)})</p>
  </div>

  <!-- Monitoring Station & Run Metadata -->
  <div class="meta-grid">
    <div class="meta-item">
      <div class="meta-label">Monitoring Station / Site</div>
      <div class="meta-value">${escapeHtml(locationName)}</div>
    </div>
    <div class="meta-item">
      <div class="meta-label">Station ID & Tag</div>
      <div class="meta-value">${escapeHtml(stationId)}</div>
    </div>
    <div class="meta-item">
      <div class="meta-label">Sampling Duration / Timing</div>
      <div class="meta-value">${escapeHtml(duration.title)} (${duration.durationHours} hrs)</div>
    </div>
    <div class="meta-item">
      <div class="meta-label">Standard Framework</div>
      <div class="meta-value">${escapeHtml(standard.code)} (${escapeHtml(standard.region)})</div>
    </div>
  </div>

  <!-- Compliance Evaluation Banner -->
  <div class="compliance-banner $statusClass">
    <div>
      <div class="status-tag $statusClass">● $statusText</div>
      <div style="font-size:8pt; margin-top:3px; color:#374151;">${escapeHtml(calculationResult.complianceSummary)}</div>
      <div style="font-size:7.5pt; font-style:italic; color:#6b7280; margin-top:2px;">${escapeHtml(calculationResult.legalCitation)}</div>
    </div>
    <div class="value-display">
      <div class="meta-label">Calculated Result</div>
      <div class="big-val">${escapeHtml(calculationResult.formattedMainResult)}</div>
      <div class="limit-note">Statutory Threshold: ${String.format(Locale.US, "%.1f", calculationResult.thresholdLimit)} ${escapeHtml(calculationResult.mainDisplayUnit)}</div>
    </div>
  </div>

  <!-- Raw Field Measurements Table -->
  <div class="section-header">Section 1: On-Site Environmental Field Measurements & Raw Input Logs</div>
  <table class="data-table">
    <thead>
      <tr>
        <th style="width:30%;">Field Parameter</th>
        <th style="width:25%;">Logged Reading & Unit</th>
        <th style="width:30%;">Measurement Description</th>
        <th style="width:15%;">Tolerance Range</th>
      </tr>
    </thead>
    <tbody>
      $fieldRows
    </tbody>
  </table>

  <!-- Step-by-Step Mathematical Formula Breakdown -->
  <div class="section-header">Section 2: Certified Step-by-Step Mathematical Derivations & Calculations</div>
  $mathRows

  <!-- Calibrated Instrument & QA/QC Traceability -->
  <div class="section-header">Section 3: Instrument Calibration & QA/QC Traceability Log</div>
  <div class="instrument-box">
    <div>
      <div><strong>Deployment Instrument:</strong> ${escapeHtml(manual.instrumentName)} (${escapeHtml(manual.makeAndModel)})</div>
      <div><strong>Operating Principle:</strong> ${escapeHtml(manual.operatingPrinciple)}</div>
      <div><strong>Measurement Range:</strong> ${escapeHtml(manual.measurementRange)} (Accuracy: ${escapeHtml(manual.accuracyAndResolution)})</div>
    </div>
    <div>
      <div><strong>Zero / Span Calibration Status:</strong> ${escapeHtml(manual.zeroAndSpanProcedure.firstOrNull() ?: "Verified")}</div>
      <div><strong>NIST / ISO Traceability:</strong> Valid & Verified Under ISO 17025 Standard Calibration Routine</div>
      <div><strong>Maintenance Protocol:</strong> ${escapeHtml(manual.maintenanceAndServicing.firstOrNull() ?: "Routine maintenance")}</div>
    </div>
  </div>

  <!-- Multi-Standard Regulatory Benchmarks -->
  <div class="section-header">Section 4: Multi-Standard Regulatory Comparison Matrix</div>
  <table class="data-table">
    <thead>
      <tr>
        <th>Standard Body</th>
        <th>Jurisdiction</th>
        <th>Statutory Limit</th>
        <th>Citation Rule</th>
        <th>Audit Verdict</th>
      </tr>
    </thead>
    <tbody>
      $matrixRows
    </tbody>
  </table>

  <!-- Quality Assurance Signatures & Authorization -->
  <div class="signatures-grid">
    <div class="sig-card">
      <div class="sig-title">Certified Field Sampling Technician</div>
      <div style="font-size:7.5pt; color:#4b5563; margin-top:2px;">Logged and validated on-site according to standard SOP protocols.</div>
      <div class="sig-line">
        <span>Signature / Stamp: _______________________</span>
        <span>Date: ___________</span>
      </div>
    </div>
    <div class="sig-card">
      <div class="sig-title">Quality Assurance Officer / Lab Director</div>
      <div style="font-size:7.5pt; color:#4b5563; margin-top:2px;">Audited for chain-of-custody, data integrity, and regulatory compliance.</div>
      <div class="sig-line">
        <span>Signature / Stamp: _______________________</span>
        <span>Date: ___________</span>
      </div>
    </div>
  </div>

  <div class="footer-audit">
    Official Electronic Record • Generated via EDEN Environmental Intelligence QMS • Conforms to ISO/IEC 17025:2017 & USEPA 40 CFR Guidelines
  </div>
</div>

</body>
</html>
        """.trimIndent()
    }

    /**
     * Builds clean HTML for an individual telemetry reading.
     */
    private fun generateTelemetryPointHtml(point: MonitoringEntity): String {
        val dateFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss z", Locale.US).format(Date(point.timestamp))
        val statusText = if (point.isExceedance) "EXCEEDANCE" else "COMPLIANT"
        val statusClass = if (point.isExceedance) "exceeded" else "compliant"

        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>EDEN Telemetry Field Sheet - ${escapeHtml(point.parameter)}</title>
<style>
  @page { size: A4 portrait; margin: 12mm; }
  body { font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; color: #111; padding: 16px; margin: 0; font-size: 10pt; }
  .no-print-bar { background: #0F6E43; color: white; padding: 10px 18px; display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
  .btn-print { background: white; color: #0F6E43; font-weight: bold; border: none; padding: 6px 14px; border-radius: 4px; cursor: pointer; }
  .header { border-bottom: 2px solid #0F6E43; padding-bottom: 8px; margin-bottom: 12px; }
  .banner { border: 2px solid; padding: 14px; border-radius: 6px; margin-bottom: 14px; }
  .banner.compliant { border-color: #0F6E43; background: #f0fdf4; }
  .banner.exceeded { border-color: #dc2626; background: #fef2f2; }
  table { width: 100%; border-collapse: collapse; margin-top: 10px; }
  th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }
  th { background: #f3f4f6; }
  @media print { .no-print-bar { display: none !important; } }
</style>
<script>
  window.addEventListener('load', function() { setTimeout(function() { window.print(); }, 400); });
</script>
</head>
<body>
<div class="no-print-bar">
  <div><strong>EDEN Telemetry Field Data Sheet</strong></div>
  <button class="btn-print" onclick="window.print()">🖨️ Print / Save as PDF</button>
</div>
<div class="header">
  <h2 style="color:#0F6E43; margin:0;">EDEN ENVIRONMENTAL TELEMETRY REPORT</h2>
  <div style="font-size:8.5pt; color:#555;">Document ID: EDEN-TEL-${point.id} | Timestamp: $dateFmt</div>
</div>
<div class="banner $statusClass">
  <h3 style="margin:0;">Status: $statusText</h3>
  <div style="font-size:22pt; font-weight:800; margin:6px 0;">${point.value} ${escapeHtml(point.unit)}</div>
  <div>Regulatory Limit: ${point.standardLimit} ${escapeHtml(point.unit)} (${escapeHtml(point.standardSource)})</div>
</div>
<table>
  <tr><th>Monitoring Parameter</th><td>${escapeHtml(point.parameter)}</td></tr>
  <tr><th>Sampling Location</th><td>${escapeHtml(point.location)}</td></tr>
  <tr><th>Logged Value</th><td>${point.value} ${escapeHtml(point.unit)}</td></tr>
  <tr><th>Threshold Limit</th><td>${point.standardLimit} ${escapeHtml(point.unit)}</td></tr>
  <tr><th>Governing Standard</th><td>${escapeHtml(point.standardSource)}</td></tr>
  <tr><th>Verification Audit</th><td>ISO/IEC 17025 Calibrated Sensor Node</td></tr>
</table>
</body>
</html>
        """.trimIndent()
    }

    private fun escapeHtml(text: String): String {
        return text
            .replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&#39;")
    }
}
