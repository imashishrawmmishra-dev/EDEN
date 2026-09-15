package com.example.monitoring.model

/**
 * 11 Comprehensive Environmental & Pollution Monitoring Domains requested:
 * 1. Ambient Air Quality Monitoring (AAQM)
 * 2. Indoor Air Quality Monitoring (IAQ)
 * 3. Workzone Air Quality Monitoring (Occupational)
 * 4. Noise & Acoustic Monitoring
 * 5. Stack Emission Monitoring (Isokinetic)
 * 6. Flue Gas Monitoring (Combustion analysis)
 * 7. Paint Booth Monitoring (VOC & Ventilation)
 * 8. Air Microbiology Sampling & Analysis (Bioaerosols)
 * 9. Cleanroom Validation Monitoring (ISO 14644)
 * 10. Lux / Illumination Monitoring (Workplace)
 * 11. Multi-Parameter Water & Wastewater Monitoring
 */
enum class PollutionMonitoringDomain(
    val title: String,
    val shortName: String,
    val subtitle: String,
    val iconName: String,
    val primaryParameter: String,
    val primaryUnit: String
) {
    AMBIENT_AIR(
        title = "Ambient Air Quality Monitoring",
        shortName = "Ambient Air",
        subtitle = "EPA/ISO Regional Criteria Pollutants (PM2.5, PM10, SO2, NO2, O3)",
        iconName = "Air",
        primaryParameter = "PM2.5 / PM10 Concentration",
        primaryUnit = "µg/m³"
    ),
    INDOOR_AIR(
        title = "Indoor Air Quality Monitoring",
        shortName = "Indoor Air (IAQ)",
        subtitle = "ASHRAE 62.1 & ISO 16000 Comfort & Health (CO2, TVOC, HCHO, ACH)",
        iconName = "MeetingRoom",
        primaryParameter = "CO₂ & TVOC Ventilation",
        primaryUnit = "ppm / ppb"
    ),
    WORKZONE_AIR(
        title = "Workzone Air Quality Monitoring",
        shortName = "Workzone Air",
        subtitle = "OSHA/NIOSH Occupational Hygiene & 8-hr Shift TWA Chemical Exposure",
        iconName = "Engineering",
        primaryParameter = "Respirable Dust & Chemical TWA",
        primaryUnit = "mg/m³"
    ),
    NOISE(
        title = "Acoustic & Noise Monitoring",
        shortName = "Noise Monitoring",
        subtitle = "IEC 61672 Type 1 Sound Pressure, Ldn, L10/L90 & Zone Compliance",
        iconName = "VolumeUp",
        primaryParameter = "Equivalent Sound Level (Leq)",
        primaryUnit = "dBA"
    ),
    STACK_EMISSION(
        title = "Stack Emission Monitoring",
        shortName = "Stack Monitoring",
        subtitle = "USEPA Method 5 / ISO 9096 Isokinetic Sampling & Pollutant Mass Rate",
        iconName = "CloudUpload",
        primaryParameter = "Particulate Matter & Emission Rate",
        primaryUnit = "mg/Nm³ / kg/h"
    ),
    FLUE_GAS(
        title = "Flue Gas Monitoring",
        shortName = "Flue Gas",
        subtitle = "Boiler & Turbine Combustion Analyzer (O2, CO, SO2, NOx, Excess Air, η)",
        iconName = "LocalFireDepartment",
        primaryParameter = "Combustion Efficiency & Ref O₂ Conc",
        primaryUnit = "mg/Nm³ @ Ref O₂"
    ),
    PAINT_BOOTH(
        title = "Paint Booth Monitoring",
        shortName = "Paint Booth",
        subtitle = "NFPA 33 & OSHA Ventilation Capture Velocity, Filter ΔP & VOC % LEL",
        iconName = "FormatPaint",
        primaryParameter = "Capture Velocity & VOC Load",
        primaryUnit = "m/s / kg/h"
    ),
    AIR_MICROBIOLOGY(
        title = "Air Microbiology Sampling & Analysis",
        shortName = "Air Microbiology",
        subtitle = "Andersen 6-Stage / Cascade Sieve Viable Bioaerosol Impaction",
        iconName = "Biotech",
        primaryParameter = "Viable Microbial Count (CFU)",
        primaryUnit = "CFU/m³"
    ),
    CLEAN_ROOM(
        title = "Cleanroom Validation Monitoring",
        shortName = "Cleanroom (ISO)",
        subtitle = "ISO 14644-1:2015 & EU GMP Airborne Particulate Class 1-9 & Grades A-D",
        iconName = "CleanHands",
        primaryParameter = "Particle Counts (≥0.5µm, ≥5.0µm)",
        primaryUnit = "particles/m³"
    ),
    LUX_MONITORING(
        title = "Lux & Illumination Monitoring",
        shortName = "Lux Monitoring",
        subtitle = "ISO 8995-1 / CIE S 008 Photopic Workplane Illuminance & Uniformity",
        iconName = "Lightbulb",
        primaryParameter = "Average Illuminance & Uniformity",
        primaryUnit = "Lux / U₀"
    ),
    WATER_MONITORING(
        title = "Multi-Parameter Water Monitoring",
        shortName = "Water Monitoring",
        subtitle = "Physicochemical & Effluent Matrix (pH, DO, Turbidity, BOD, COD, Metals)",
        iconName = "WaterDrop",
        primaryParameter = "Effluent BOD/COD & Water Quality Index",
        primaryUnit = "mg/L / WQI"
    )
}

/**
 * 8 Customer-Selectable Regulatory Standards & Guidelines:
 * - ISO: International Organization for Standardization
 * - AS_NZS: Standards Australia / Standards New Zealand
 * - USEPA: United States Environmental Protection Agency
 * - BS: British Standards (BSI / BS EN)
 * - EU: European Union Directives
 * - INDIAN: Central Pollution Control Board (CPCB) & Bureau of Indian Standards (BIS)
 * - CHINESE: Guobiao (GB) National Standards of China
 * - BRAZILIAN: CONAMA (Conselho Nacional do Meio Ambiente) & ABNT NBR
 */
enum class RegulatoryStandard(
    val code: String,
    val displayName: String,
    val region: String,
    val authority: String
) {
    ISO("ISO", "ISO (International)", "International", "International Organization for Standardization"),
    AS_NZS("AS/NZS", "AS/NZS (Australia & NZ)", "Australia & New Zealand", "Standards Australia / New Zealand"),
    USEPA("USEPA", "USEPA (United States)", "United States", "US Environmental Protection Agency"),
    BS("BS", "BS / BS EN (United Kingdom)", "United Kingdom", "British Standards Institution"),
    EU("EU", "EU Directives (European Union)", "European Union", "European Commission / EEA"),
    INDIAN("CPCB/IS", "Indian Standards (CPCB & BIS)", "India", "Central Pollution Control Board / BIS"),
    CHINESE("GB", "Chinese Standards (GB)", "China", "Ministry of Ecology and Environment / SAC"),
    BRAZILIAN("CONAMA", "Brazilian Standards (CONAMA/NBR)", "Brazil", "CONAMA / ABNT")
}

/**
 * Monitoring Timings / Durations:
 * - SPOT: Instantaneous or short-term spot grab measurement (5-15 min)
 * - ONE_HOUR: 1 Hour average (peak short-term regulatory benchmark)
 * - EIGHT_HOUR: 8 Hours Time-Weighted Average (TWA for occupational shift)
 * - TWENTY_FOUR_HOUR: 24 Hours continuous composite (ambient diurnal cycle)
 */
enum class MonitoringDuration(
    val code: String,
    val title: String,
    val durationHours: Double,
    val typicalApplication: String
) {
    SPOT("Spot", "Spot Measurement", 0.25, "Instantaneous grab, leak verification, screening"),
    ONE_HOUR("1-Hr", "1 Hour Average", 1.0, "Short-term peak exposure & industrial surge monitoring"),
    EIGHT_HOUR("8-Hr", "8 Hours TWA", 8.0, "Occupational workshift time-weighted average exposure"),
    TWENTY_FOUR_HOUR("24-Hr", "24 Hours Continuous", 24.0, "Diurnal ambient compliance & daily composite")
}

/**
 * Live compliance evaluation states
 */
enum class ComplianceStatus(val label: String, val badgeColorHex: Long) {
    COMPLIANT("COMPLIANT (PASS)", 0xFF0F6E43),
    WARNING("CAUTION / NEAR THRESHOLD", 0xFFE65100),
    EXCEEDED("NON-COMPLIANT (EXCEEDED)", 0xFFBA1A1A)
}

/**
 * Field parameter definition for dynamic form rendering
 */
data class MonitoringFieldDef(
    val id: String,
    val label: String,
    val unit: String,
    val defaultValue: String,
    val description: String,
    val isNumeric: Boolean = true,
    val minPlausible: Double? = null,
    val maxPlausible: Double? = null,
    val validationTooltip: String? = null
)

/**
 * Standard operating procedure details
 */
data class StandardProcedureDetails(
    val standardTitle: String,
    val regulatoryCode: String,
    val scope: String,
    val preSamplingProtocol: List<String>,
    val instrumentSetupAndLeakCheck: List<String>,
    val samplingExecutionProtocol: List<String>,
    val sampleHandlingAndPreservation: List<String>,
    val mathCalculationFormula: String
)

/**
 * Certified Instrument specifications & operating manual
 */
data class InstrumentManualDetails(
    val instrumentName: String,
    val makeAndModel: String,
    val operatingPrinciple: String,
    val measurementRange: String,
    val accuracyAndResolution: String,
    val calibrationProtocol: List<String>,
    val zeroAndSpanProcedure: List<String>,
    val maintenanceAndServicing: List<String>,
    val operatingEnvironmentLimits: String
)

/**
 * Benchmark threshold for standard comparison
 */
data class StandardBenchmarkThreshold(
    val standard: RegulatoryStandard,
    val duration: MonitoringDuration,
    val parameterName: String,
    val limitValue: Double,
    val unit: String,
    val citationRule: String,
    val advisoryNote: String
)

/**
 * Evaluated live calculation result
 */
data class LiveCalculationResult(
    val mainCalculatedValue: Double,
    val mainDisplayUnit: String,
    val formattedMainResult: String,
    val parameterEvaluated: String,
    val stepByStepFormulas: List<Pair<String, String>>,
    val complianceStatus: ComplianceStatus,
    val chosenStandard: RegulatoryStandard,
    val thresholdLimit: Double,
    val deltaPercent: Double,
    val complianceSummary: String,
    val legalCitation: String
)
