package com.example.calculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object EnvironmentalCalculatorsExporter {

    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
    private val fileDateFormat = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US)

    // ==========================================
    // 1. AIR STACK CSV EXPORT
    // ==========================================
    fun generateAirCsv(
        velocity: Double,
        diameter: Double,
        concMgM3: Double,
        result: AirCalculatorEngine.StackFlowResult,
        tempCelsius: Double = 165.0
    ): String {
        val now = dateFormat.format(Date())
        return buildString {
            appendLine("\"Record Type\",\"Parameter\",\"Value\",\"Unit\",\"Standard / Methodology\"")
            appendLine("\"METADATA\",\"Report Name\",\"EDEN Air Stack & Chimney Dispersion Audit\",\"\",\"EPA Method 2\"")
            appendLine("\"METADATA\",\"Timestamp\",\"$now\",\"\",\"UTC Generated\"")
            appendLine("\"INPUT\",\"Gas Velocity\",\"$velocity\",\"m/s\",\"Pitot Tube Velocity\"")
            appendLine("\"INPUT\",\"Stack Diameter\",\"$diameter\",\"m\",\"Circular Chimney Diameter\"")
            appendLine("\"INPUT\",\"Stack Temperature\",\"$tempCelsius\",\"°C\",\"Flue Gas Thermocouple (Ts)\"")
            appendLine("\"INPUT\",\"Pollutant Concentration\",\"$concMgM3\",\"mg/Nm³\",\"Dry Standard Conditions\"")
            appendLine("\"OUTPUT\",\"Stack Cross-Sectional Area\",\"${String.format(Locale.US, "%.4f", result.ductAreaM2)}\",\"m²\",\"A = π·(D/2)²\"")
            appendLine("\"OUTPUT\",\"Volumetric Flow Rate (s)\",\"${String.format(Locale.US, "%.3f", result.flowRateM3Sec)}\",\"m³/s\",\"Q = V·A\"")
            appendLine("\"OUTPUT\",\"Volumetric Flow Rate (h)\",\"${String.format(Locale.US, "%.2f", result.flowRateM3Hour)}\",\"m³/h\",\"Q_hour = Q_sec * 3600\"")
            appendLine("\"OUTPUT\",\"Normalized Flow Rate (Nm³/h)\",\"${String.format(Locale.US, "%.2f", result.normalizedFlowRateM3Hour)}\",\"Nm³/h\",\"Q_std = Q * (293.15 / (Ts + 273.15))\"")
            appendLine("\"OUTPUT\",\"Mass Emission Rate (kg/h)\",\"${String.format(Locale.US, "%.5f", result.emissionRateKgHour)}\",\"kg/h\",\"E = Q·C·10⁻⁶\"")
            appendLine("\"OUTPUT\",\"Mass Emission Rate (g/s)\",\"${String.format(Locale.US, "%.5f", result.emissionRateGSec)}\",\"g/s\",\"E_sec = E_hour * 1000 / 3600\"")
            appendLine("\"VERIFICATION\",\"Methodology\",\"${result.formulaExplanation.replace("\"", "\"\"")}\",\"\",\"EPA Method 2 Verified\"")
        }
    }

    // ==========================================
    // 2. WATER LOADING CSV EXPORT
    // ==========================================
    fun generateWaterCsv(
        flowM3Day: Double,
        bodMgL: Double,
        codMgL: Double,
        tankVolM3: Double,
        result: WaterCalculatorEngine.WaterLoadResult
    ): String {
        val now = dateFormat.format(Date())
        return buildString {
            appendLine("\"Record Type\",\"Parameter\",\"Value\",\"Unit\",\"Standard / Methodology\"")
            appendLine("\"METADATA\",\"Report Name\",\"EDEN Wastewater Loading & Sizing Audit\",\"\",\"Metcalf & Eddy Standard\"")
            appendLine("\"METADATA\",\"Timestamp\",\"$now\",\"\",\"UTC Generated\"")
            appendLine("\"INPUT\",\"Daily Flow Rate\",\"$flowM3Day\",\"m³/day\",\"Influent Volumetric Flow\"")
            appendLine("\"INPUT\",\"Influent BOD₅\",\"$bodMgL\",\"mg/L\",\"Biochemical Oxygen Demand\"")
            appendLine("\"INPUT\",\"Influent COD\",\"$codMgL\",\"mg/L\",\"Chemical Oxygen Demand\"")
            appendLine("\"INPUT\",\"Aeration Basin Volume\",\"$tankVolM3\",\"m³\",\"Bioreactor Volume\"")
            appendLine("\"OUTPUT\",\"Daily BOD Load\",\"${String.format(Locale.US, "%.2f", result.bodLoadKgDay)}\",\"kg BOD₅/day\",\"Load = Q · C · 10⁻³\"")
            appendLine("\"OUTPUT\",\"Daily COD Load\",\"${String.format(Locale.US, "%.2f", result.codLoadKgDay)}\",\"kg COD/day\",\"Load = Q · C · 10⁻³\"")
            appendLine("\"OUTPUT\",\"Population Equivalent (PE)\",\"${String.format(Locale.US, "%.0f", result.populationEquivalent)}\",\"capita\",\"1 PE = 0.06 kg BOD/day\"")
            appendLine("\"OUTPUT\",\"Hydraulic Retention Time (HRT)\",\"${String.format(Locale.US, "%.2f", result.hrtHours)}\",\"hours\",\"HRT = (V / Q) · 24\"")
            appendLine("\"OUTPUT\",\"BOD / COD Ratio\",\"${String.format(Locale.US, "%.3f", result.bodCodRatio)}\",\"\",\"Biodegradability Index\"")
            appendLine("\"OUTPUT\",\"Biodegradability Classification\",\"${result.biodegradabilityClass}\",\"\",\"ISO/EPA Criteria\"")
            appendLine("\"RECOMMENDATION\",\"Treatment Recommendation\",\"${result.treatmentRecommendation.replace("\"", "\"\"")}\",\"\",\"Process Engineering\"")
        }
    }

    // ==========================================
    // 3. NOISE ACOUSTICS CSV EXPORT
    // ==========================================
    fun generateNoiseCsv(
        sourcesDb: List<Double>,
        dayLeq: Double,
        nightLeq: Double,
        refDistM: Double,
        targetDistM: Double,
        result: NoiseCalculatorEngine.AcousticResult
    ): String {
        val now = dateFormat.format(Date())
        return buildString {
            appendLine("\"Record Type\",\"Parameter\",\"Value\",\"Unit\",\"Standard / Methodology\"")
            appendLine("\"METADATA\",\"Report Name\",\"EDEN Environmental Acoustics & Decibel Audit\",\"\",\"ISO 9613 / IEC 61672\"")
            appendLine("\"METADATA\",\"Timestamp\",\"$now\",\"\",\"UTC Generated\"")
            sourcesDb.forEachIndexed { i, db ->
                appendLine("\"INPUT\",\"Acoustic Source #${i + 1}\",\"$db\",\"dB(A)\",\"Sound Pressure Level\"")
            }
            appendLine("\"INPUT\",\"Daytime Leq (07-22)\",\"$dayLeq\",\"dB(A)\",\"Equivalent Continuous Sound\"")
            appendLine("\"INPUT\",\"Nighttime Leq (22-07)\",\"$nightLeq\",\"dB(A)\",\"Night Sound with 10dB Penalty\"")
            appendLine("\"INPUT\",\"Reference Distance\",\"$refDistM\",\"m\",\"Baseline Measurement Distance\"")
            appendLine("\"INPUT\",\"Target Distance\",\"$targetDistM\",\"m\",\"Receiver Attenuation Distance\"")
            appendLine("\"OUTPUT\",\"Logarithmic Summed Decibels\",\"${String.format(Locale.US, "%.2f", result.totalDecibels)}\",\"dB(A)\",\"L_sum = 10·log10(Σ 10^(Li/10))\"")
            appendLine("\"OUTPUT\",\"Day-Night Sound Level (Ldn)\",\"${String.format(Locale.US, "%.2f", result.dayNightLevelLdn)}\",\"dB(A)\",\"EPA 55 dBA Level Criterion\"")
            appendLine("\"OUTPUT\",\"Attenuated SPL at Target Distance\",\"${String.format(Locale.US, "%.2f", result.attenuatedDbAtDistance)}\",\"dB(A)\",\"L2 = L1 - 20·log10(r2/r1)\"")
            appendLine("\"COMPLIANCE\",\"Status\",\"${result.complianceStatus.replace("\"", "\"\"")}\",\"\",\"WHO / OSHA Criteria\"")
        }
    }

    fun createExportFile(context: Context, filenamePrefix: String, csvContent: String): File {
        val exportDir = File(context.cacheDir, "environmental_reports")
        if (!exportDir.exists()) exportDir.mkdirs()
        val timestamp = fileDateFormat.format(Date())
        val file = File(exportDir, "${filenamePrefix}_${timestamp}.csv")
        file.writeText(csvContent)
        return file
    }

    fun createShareIntent(context: Context, file: File, subject: String): Intent {
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        return Intent(Intent.ACTION_SEND).apply {
            type = "text/csv"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    fun copyToClipboard(context: Context, label: String, content: String) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(label, content)
        clipboard.setPrimaryClip(clip)
    }
}
