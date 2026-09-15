package com.example.calculator

import com.example.util.GhgCsvExporter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Encapsulates a snapshot of GHG accounting parameters and the resulting inventory.
 * Used for scenario modeling, baseline benchmarking, and side-by-side comparative analysis.
 */
data class GhgScenario(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val inputs: GhgCsvExporter.GhgInputParameters,
    val result: CarbonCalculatorEngine.CarbonInventoryResult,
    val isBaseline: Boolean = false
) {
    val formattedDate: String
        get() = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.US).format(Date(timestamp))

    val shortDate: String
        get() = SimpleDateFormat("MM/dd HH:mm", Locale.US).format(Date(timestamp))
}

/**
 * Quantifies mathematical variances between a Baseline scenario and a Proposed/Current scenario.
 */
data class GhgScenarioComparison(
    val baseline: GhgScenario,
    val current: GhgScenario
) {
    val deltaScope1Kg: Double = current.result.scope1Kg - baseline.result.scope1Kg
    val deltaScope2Kg: Double = current.result.scope2Kg - baseline.result.scope2Kg
    val deltaScope3Kg: Double = current.result.scope3Kg - baseline.result.scope3Kg
    val deltaTotalKg: Double = current.result.totalKg - baseline.result.totalKg
    val deltaTotalTonnes: Double = current.result.totalTonnes - baseline.result.totalTonnes

    val deltaTotalPercent: Double = if (baseline.result.totalKg > 0) {
        ((current.result.totalKg - baseline.result.totalKg) / baseline.result.totalKg) * 100.0
    } else {
        0.0
    }

    val deltaScope1Percent: Double = if (baseline.result.scope1Kg > 0) {
        ((current.result.scope1Kg - baseline.result.scope1Kg) / baseline.result.scope1Kg) * 100.0
    } else {
        0.0
    }

    val deltaScope2Percent: Double = if (baseline.result.scope2Kg > 0) {
        ((current.result.scope2Kg - baseline.result.scope2Kg) / baseline.result.scope2Kg) * 100.0
    } else {
        0.0
    }

    val deltaScope3Percent: Double = if (baseline.result.scope3Kg > 0) {
        ((current.result.scope3Kg - baseline.result.scope3Kg) / baseline.result.scope3Kg) * 100.0
    } else {
        0.0
    }

    val isNetReduction: Boolean get() = deltaTotalKg < -0.01
    val isNeutral: Boolean get() = kotlin.math.abs(deltaTotalKg) <= 0.01
    val isEmissionsIncrease: Boolean get() = deltaTotalKg > 0.01

    val absoluteReductionTonnes: Double get() = kotlin.math.abs(deltaTotalTonnes)
    val absoluteReductionKg: Double get() = kotlin.math.abs(deltaTotalKg)
    val absolutePercentageDelta: Double get() = kotlin.math.abs(deltaTotalPercent)

    val mitigationTrajectoryAssessment: String
        get() = when {
            deltaTotalPercent <= -40.0 -> "Aligned with SBTi 1.5°C Paris Agreement trajectory (>40% deep decarbonization)."
            deltaTotalPercent <= -20.0 -> "Significant decarbonization (-%.1f%%). On track for intermediate corporate sustainability targets.".format(Locale.US, absolutePercentageDelta)
            deltaTotalPercent < 0.0 -> "Moderate reduction (-%.1f%%). Consider additional renewable power purchasing to accelerate abatement.".format(Locale.US, absolutePercentageDelta)
            isNeutral -> "Carbon neutral variance. Operational footprint is identical to baseline."
            else -> "Emissions increase (+%.1f%%). Requires mitigation review to prevent Scope creep.".format(Locale.US, deltaTotalPercent)
        }

    val dominantAbatementDriver: String
        get() {
            if (!isNetReduction) return "No net reduction achieved."
            val absS1 = kotlin.math.abs(deltaScope1Kg)
            val absS2 = kotlin.math.abs(deltaScope2Kg)
            val absS3 = kotlin.math.abs(deltaScope3Kg)
            return when {
                deltaScope1Kg < 0 && absS1 >= absS2 && absS1 >= absS3 -> "Scope 1 Direct Fuel Switching (saved %.1f kg CO₂e)".format(Locale.US, absS1)
                deltaScope2Kg < 0 && absS2 >= absS1 && absS2 >= absS3 -> "Scope 2 Clean Power / Grid Decarbonization (saved %.1f kg CO₂e)".format(Locale.US, absS2)
                deltaScope3Kg < 0 && absS3 >= absS1 && absS3 >= absS2 -> "Scope 3 Low-Carbon Logistics & Rail (saved %.1f kg CO₂e)".format(Locale.US, absS3)
                else -> "Multimodal decarbonization across combined Scopes"
            }
        }

    fun generateComparisonCsv(): String {
        val sb = StringBuilder()
        sb.appendLine(GhgCsvExporter.csvRow("EDEN Environmental Intelligence - GHG Scenario Comparison Report"))
        sb.appendLine(GhgCsvExporter.csvRow("Baseline Scenario", baseline.name, baseline.formattedDate))
        sb.appendLine(GhgCsvExporter.csvRow("Comparison Scenario", current.name, current.formattedDate))
        sb.appendLine(GhgCsvExporter.csvRow("Trajectory Assessment", mitigationTrajectoryAssessment))
        sb.appendLine(GhgCsvExporter.csvRow("Primary Abatement Driver", dominantAbatementDriver))
        sb.appendLine("")
        sb.appendLine(GhgCsvExporter.csvRow("Metric", "Baseline (${baseline.name})", "Current (${current.name})", "Delta (kg CO2e)", "Delta (%)", "Status"))
        sb.appendLine(GhgCsvExporter.csvRow("Scope 1 Direct Fuel", "%.2f".format(Locale.US, baseline.result.scope1Kg), "%.2f".format(Locale.US, current.result.scope1Kg), "%.2f".format(Locale.US, deltaScope1Kg), "%.2f%%".format(Locale.US, deltaScope1Percent), if (deltaScope1Kg <= 0) "Reduced" else "Increased"))
        sb.appendLine(GhgCsvExporter.csvRow("Scope 2 Purchased Grid", "%.2f".format(Locale.US, baseline.result.scope2Kg), "%.2f".format(Locale.US, current.result.scope2Kg), "%.2f".format(Locale.US, deltaScope2Kg), "%.2f%%".format(Locale.US, deltaScope2Percent), if (deltaScope2Kg <= 0) "Reduced" else "Increased"))
        sb.appendLine(GhgCsvExporter.csvRow("Scope 3 Freight & Travel", "%.2f".format(Locale.US, baseline.result.scope3Kg), "%.2f".format(Locale.US, current.result.scope3Kg), "%.2f".format(Locale.US, deltaScope3Kg), "%.2f%%".format(Locale.US, deltaScope3Percent), if (deltaScope3Kg <= 0) "Reduced" else "Increased"))
        sb.appendLine(GhgCsvExporter.csvRow("TOTAL GHG INVENTORY (kg)", "%.2f".format(Locale.US, baseline.result.totalKg), "%.2f".format(Locale.US, current.result.totalKg), "%.2f".format(Locale.US, deltaTotalKg), "%.2f%%".format(Locale.US, deltaTotalPercent), if (deltaTotalKg <= 0) "Net Decarbonization" else "Emissions Surge"))
        sb.appendLine(GhgCsvExporter.csvRow("TOTAL GHG INVENTORY (tonnes)", "%.4f".format(Locale.US, baseline.result.totalTonnes), "%.4f".format(Locale.US, current.result.totalTonnes), "%.4f".format(Locale.US, deltaTotalTonnes), "%.2f%%".format(Locale.US, deltaTotalPercent), if (deltaTotalTonnes <= 0) "Reduced" else "Increased"))
        return sb.toString()
    }
}
