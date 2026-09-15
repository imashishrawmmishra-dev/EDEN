package com.example.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.calculator.CarbonCalculatorEngine
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/**
 * Audit-grade CSV Exporter for Corporate Greenhouse Gas (GHG) Inventory assessments.
 * Conforms to RFC 4180 specification and the GHG Protocol Corporate Accounting Standard.
 */
object GhgCsvExporter {

    data class GhgInputParameters(
        val fuelType: CarbonCalculatorEngine.FuelType = CarbonCalculatorEngine.FuelType.DIESEL,
        val fuelQuantity: Double = 1500.0,
        val electricityKwh: Double = 25000.0,
        val gridRegion: CarbonCalculatorEngine.GridRegion = CarbonCalculatorEngine.GridRegion.US_AVERAGE,
        val transportMode: CarbonCalculatorEngine.TransportMode = CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
        val transportVolume: Double = 8000.0,
        val timestamp: Long = System.currentTimeMillis()
    )

    /**
     * Generates a fully structured, multi-section RFC 4180 compliant CSV string
     * representing the verified GHG inventory calculation.
     */
    fun generateGhgCsv(
        inputs: GhgInputParameters,
        result: CarbonCalculatorEngine.CarbonInventoryResult
    ): String {
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val humanFormat = SimpleDateFormat("MMM dd, yyyy HH:mm:ss z", Locale.US)
        val exportDateUtc = isoFormat.format(Date(inputs.timestamp))
        val exportDateLocal = humanFormat.format(Date(inputs.timestamp))

        val sb = StringBuilder()

        // --- Section 1: Executive Metadata Header ---
        sb.appendLine(csvRow("EDEN Environmental Intelligence - GHG Protocol Carbon Inventory Report"))
        sb.appendLine(csvRow("Standard Compliance", "GHG Protocol Corporate Accounting and Reporting Standard (Revised)"))
        sb.appendLine(csvRow("Accounting Principles", "Relevance, Completeness, Consistency, Transparency, Accuracy"))
        sb.appendLine(csvRow("Export Timestamp (UTC)", exportDateUtc))
        sb.appendLine(csvRow("Export Timestamp (Local)", exportDateLocal))
        sb.appendLine(csvRow("Reporting Boundaries", "Operational Control: Scope 1 (Direct), Scope 2 (Location-Based Grid), Scope 3 (Freight Logistics)"))
        sb.appendLine("")

        // --- Section 2: Quantified Carbon Inventory Summary ---
        sb.appendLine(csvRow("--- SECTION 1: GHG INVENTORY QUANTIFICATION SUMMARY ---"))
        sb.appendLine(csvRow(
            "Emissions Scope",
            "Boundary Description",
            "Emissions (kg CO2e)",
            "Emissions (Metric Tonnes CO2e)",
            "Inventory Share (%)",
            "GHG Accounting Class"
        ))
        sb.appendLine(csvRow(
            "Scope 1",
            "Direct Combustion: ${inputs.fuelType.displayName}",
            "%.2f".format(Locale.US, result.scope1Kg),
            "%.4f".format(Locale.US, result.scope1Kg / 1000.0),
            "%.2f%%".format(Locale.US, result.scope1Percent),
            "Direct Facility & Fleet"
        ))
        sb.appendLine(csvRow(
            "Scope 2",
            "Indirect Electricity: ${inputs.gridRegion.displayName}",
            "%.2f".format(Locale.US, result.scope2Kg),
            "%.4f".format(Locale.US, result.scope2Kg / 1000.0),
            "%.2f%%".format(Locale.US, result.scope2Percent),
            "Purchased Electricity / Grid Mix"
        ))
        sb.appendLine(csvRow(
            "Scope 3",
            "Upstream/Downstream Logistics: ${inputs.transportMode.displayName}",
            "%.2f".format(Locale.US, result.scope3Kg),
            "%.4f".format(Locale.US, result.scope3Kg / 1000.0),
            "%.2f%%".format(Locale.US, result.scope3Percent),
            "Value Chain Transportation"
        ))
        sb.appendLine(csvRow(
            "TOTAL GHG FOOTPRINT",
            "Combined Scope 1, 2, and 3 Corporate Inventory",
            "%.2f".format(Locale.US, result.totalKg),
            "%.4f".format(Locale.US, result.totalTonnes),
            "100.00%",
            "Consolidated Corporate Emissions"
        ))
        sb.appendLine("")

        // --- Section 3: Activity Data & Emission Factor Breakdown ---
        sb.appendLine(csvRow("--- SECTION 2: ACTIVITY DATA & EMISSION FACTOR AUDIT TRAIL ---"))
        sb.appendLine(csvRow(
            "Scope",
            "Activity Stream",
            "Input Parameter",
            "Activity Quantity",
            "Activity Unit",
            "Emission Factor",
            "Factor Unit",
            "Quantification Formula",
            "Authority / Reference Dataset"
        ))
        sb.appendLine(csvRow(
            "Scope 1",
            "Fuel Combustion",
            inputs.fuelType.displayName,
            "%.2f".format(Locale.US, inputs.fuelQuantity),
            inputs.fuelType.unit,
            "%.4f".format(Locale.US, inputs.fuelType.factorKgCo2e),
            "kg CO2e / ${inputs.fuelType.unit}",
            "Activity Quantity x Fuel Factor",
            inputs.fuelType.authority
        ))
        sb.appendLine(csvRow(
            "Scope 2",
            "Grid Power",
            inputs.gridRegion.displayName,
            "%.2f".format(Locale.US, inputs.electricityKwh),
            "kWh",
            "%.4f".format(Locale.US, inputs.gridRegion.factorKgCo2ePerKwh),
            "kg CO2e / kWh",
            "Electricity Consumed x Grid Emission Factor",
            "eGRID / EEA / IPCC Benchmark"
        ))
        sb.appendLine(csvRow(
            "Scope 3",
            "Transportation",
            inputs.transportMode.displayName,
            "%.2f".format(Locale.US, inputs.transportVolume),
            inputs.transportMode.unit,
            "%.4f".format(Locale.US, inputs.transportMode.factorKgCo2e),
            "kg CO2e / ${inputs.transportMode.unit}",
            "Transport Volume x Freight Factor",
            "UK DEFRA / GLEC Framework"
        ))
        sb.appendLine("")

        // --- Section 4: Analytical Findings & Decarbonization Pathways ---
        sb.appendLine(csvRow("--- SECTION 3: ANALYTICAL FINDINGS & MITIGATION ROADMAP ---"))
        sb.appendLine(csvRow("Dominant Emission Scope Driver", result.dominantScope))
        sb.appendLine(csvRow("Dominant Scope Share (%)", "%.1f%%".format(Locale.US, when {
            result.scope1Kg >= result.scope2Kg && result.scope1Kg >= result.scope3Kg -> result.scope1Percent
            result.scope2Kg >= result.scope1Kg && result.scope2Kg >= result.scope3Kg -> result.scope2Percent
            else -> result.scope3Percent
        })))
        result.reductionScenarios.forEachIndexed { idx, pathway ->
            sb.appendLine(csvRow("Target Decarbonization Pathway ${idx + 1}", pathway))
        }
        sb.appendLine("")

        // --- Section 5: Technical Assurance & Methodology ---
        sb.appendLine(csvRow("--- SECTION 4: METHODOLOGY & ASSURANCE STATEMENT ---"))
        sb.appendLine(csvRow("Methodology Statement", result.methodologyNote))
        sb.appendLine(csvRow("Assurance Status", "Engineered deterministic calculation with verifiable emission coefficients"))
        sb.appendLine(csvRow("Software Platform", "EDEN Environmental Intelligence Mobile Suite"))

        return sb.toString()
    }

    /**
     * Escapes and formats a single row for RFC 4180 CSV compliance.
     */
    fun csvRow(vararg fields: String): String {
        return fields.joinToString(separator = ",") { escapeCsvCell(it) }
    }

    private fun escapeCsvCell(field: String): String {
        val containsSpecial = field.contains(',') || field.contains('"') || field.contains('\n') || field.contains('\r')
        return if (containsSpecial) {
            "\"${field.replace("\"", "\"\"")}\""
        } else {
            "\"$field\""
        }
    }

    /**
     * Creates and caches a physical .csv file in the application's cache directory.
     */
    fun createExportFile(
        context: Context,
        csvContent: String,
        baseFileName: String = "eden_ghg_inventory_report"
    ): File {
        val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val fileName = "${baseFileName}_$timestamp.csv"
        val exportDir = File(context.cacheDir, "exports")
        if (!exportDir.exists()) {
            exportDir.mkdirs()
        }
        val file = File(exportDir, fileName)
        FileOutputStream(file).use { out ->
            out.write(csvContent.toByteArray(Charsets.UTF_8))
        }
        return file
    }

    /**
     * Writes CSV string into an arbitrary output stream (e.g. Storage Access Framework Uri).
     */
    fun writeCsvToStream(outputStream: OutputStream, csvContent: String): Boolean {
        return try {
            outputStream.use { stream ->
                stream.write(csvContent.toByteArray(Charsets.UTF_8))
                stream.flush()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Constructs a Share Intent using Android's FileProvider to share the CSV report.
     */
    fun createShareIntent(context: Context, file: File): Intent {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )

        return Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "EDEN Corporate GHG Inventory Report (.csv)")
            putExtra(Intent.EXTRA_TEXT, "Attached is the verified Corporate GHG Inventory Report exported from EDEN Environmental Intelligence.")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    /**
     * Copies the raw CSV contents to the system clipboard for immediate pasting into spreadsheets.
     */
    fun copyToClipboard(context: Context, csvContent: String, label: String = "EDEN GHG CSV Report"): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
            val clip = ClipData.newPlainText(label, csvContent)
            clipboard?.setPrimaryClip(clip)
            true
        } catch (e: Exception) {
            false
        }
    }
}
