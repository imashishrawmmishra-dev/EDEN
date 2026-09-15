package com.example.data.model

import androidx.compose.ui.graphics.Color

// ==========================================
// 1. WATER QUALITY TELEMETRY & STANDARDS
// ==========================================

enum class WaterParameterType(
    val code: String,
    val fullName: String,
    val unit: String,
    val standardLimit: Double,
    val standardReference: String,
    val category: String,
    val measurementMethod: String,
    val primarySource: String,
    val environmentalImpact: String
) {
    PH(
        code = "pH",
        fullName = "Hydrogen Ion Activity (pH)",
        unit = "pH",
        standardLimit = 8.5,
        standardReference = "WHO Drinking Water: 6.5 – 8.5",
        category = "Electrochemical Acidity/Alkalinity",
        measurementMethod = "Glass electrode potentiometry (ISO 10523)",
        primarySource = "Geological dissolution, industrial acid/base discharges, acid mine runoff",
        environmentalImpact = "Extreme pH mobilizes toxic heavy metals and disrupts aquatic enzyme activity."
    ),
    DISSOLVED_OXYGEN(
        code = "DO",
        fullName = "Dissolved Oxygen",
        unit = "mg/L",
        standardLimit = 5.0, // Minimum desirable threshold
        standardReference = "EPA Surface Water: ≥ 5.0 mg/L",
        category = "Aquatic Respiration Baseline",
        measurementMethod = "Luminescent optical sensor / Clark polarographic cell",
        primarySource = "Atmospheric re-aeration, aquatic plant photosynthesis",
        environmentalImpact = "Hypoxia (<3 mg/L) causes fish kills, benthic asphyxiation, and anaerobic odours."
    ),
    BOD(
        code = "BOD₅",
        fullName = "Biochemical Oxygen Demand (5-Day)",
        unit = "mg/L",
        standardLimit = 5.0,
        standardReference = "EU Water Framework: ≤ 5.0 mg/L (Effluent ≤ 25 mg/L)",
        category = "Biodegradable Organic Loading",
        measurementMethod = "5-day 20°C incubation respirometry (EPA 405.1)",
        primarySource = "Municipal sewage, food processing effluent, agricultural runoff",
        environmentalImpact = "Rapidly depletes receiving water dissolved oxygen, causing ecological dead zones."
    ),
    COD(
        code = "COD",
        fullName = "Chemical Oxygen Demand",
        unit = "mg/L",
        standardLimit = 50.0,
        standardReference = "EPA Wastewater Guideline: ≤ 50.0 mg/L",
        category = "Total Oxidizable Pollutants",
        measurementMethod = "Dichromate closed reflux spectrophotometry (ISO 15705)",
        primarySource = "Chemical manufacturing, refinery effluents, synthetic industrial waste",
        environmentalImpact = "Represents non-biodegradable refractory toxins persisting in aquatic food webs."
    ),
    TURBIDITY(
        code = "Turbidity",
        fullName = "Water Clarity / Suspended Colloids",
        unit = "NTU",
        standardLimit = 4.0,
        standardReference = "WHO Guideline: ≤ 1.0 NTU (Drinking) / ≤ 5.0 NTU (Surface)",
        category = "Optical Light Attenuation",
        measurementMethod = "Nephelometric 90° light scattering (ISO 7027)",
        primarySource = "Soil erosion, sediment runoff, algal blooms, colloidal discharges",
        environmentalImpact = "Blocks sunlight penetration, smothers gravel spawning beds, and harbors pathogens."
    ),
    NITRATE(
        code = "NO₃⁻",
        fullName = "Nitrate Nitrogen",
        unit = "mg/L",
        standardLimit = 10.0,
        standardReference = "EPA Drinking Water MCL: 10.0 mg/L",
        category = "Eutrophication Nutrient",
        measurementMethod = "Ion chromatography / UV spectrophotometry",
        primarySource = "Synthetic nitrogen fertilizers, septic seepage, livestock slurry",
        environmentalImpact = "Triggers toxic cyanobacterial blooms; induces methemoglobinemia in infants."
    );

    fun evaluateStatus(value: Double): ParameterStatus {
        return when (this) {
            PH -> {
                when {
                    value in 6.8..7.8 -> ParameterStatus.OPTIMAL
                    value in 6.5..8.5 -> ParameterStatus.COMPLIANT
                    value in 6.0..9.0 -> ParameterStatus.MODERATE
                    value in 5.0..9.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            DISSOLVED_OXYGEN -> {
                // For DO, higher is better
                when {
                    value >= 7.5 -> ParameterStatus.OPTIMAL
                    value >= 5.0 -> ParameterStatus.COMPLIANT
                    value >= 4.0 -> ParameterStatus.MODERATE
                    value >= 2.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            BOD -> {
                val ratio = value / standardLimit
                when {
                    ratio <= 0.6 -> ParameterStatus.OPTIMAL
                    ratio <= 1.0 -> ParameterStatus.COMPLIANT
                    ratio <= 1.5 -> ParameterStatus.MODERATE
                    ratio <= 2.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            COD -> {
                val ratio = value / standardLimit
                when {
                    ratio <= 0.6 -> ParameterStatus.OPTIMAL
                    ratio <= 1.0 -> ParameterStatus.COMPLIANT
                    ratio <= 1.5 -> ParameterStatus.MODERATE
                    ratio <= 2.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            TURBIDITY -> {
                val ratio = value / standardLimit
                when {
                    ratio <= 0.5 -> ParameterStatus.OPTIMAL
                    ratio <= 1.0 -> ParameterStatus.COMPLIANT
                    ratio <= 1.8 -> ParameterStatus.MODERATE
                    ratio <= 3.0 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            NITRATE -> {
                val ratio = value / standardLimit
                when {
                    ratio <= 0.5 -> ParameterStatus.OPTIMAL
                    ratio <= 1.0 -> ParameterStatus.COMPLIANT
                    ratio <= 1.5 -> ParameterStatus.MODERATE
                    ratio <= 2.0 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
        }
    }
}

// ==========================================
// 2. NOISE & ACOUSTICS TELEMETRY & STANDARDS
// ==========================================

enum class NoiseParameterType(
    val code: String,
    val fullName: String,
    val unit: String,
    val standardLimit: Double,
    val standardReference: String,
    val category: String,
    val measurementMethod: String,
    val primarySource: String,
    val healthImpact: String
) {
    LEQ_DAY(
        code = "Leq,day",
        fullName = "Daytime Equivalent Sound Level (07:00–22:00)",
        unit = "dB(A)",
        standardLimit = 55.0,
        standardReference = "WHO Guidelines: ≤ 55 dB(A) outdoor residential",
        category = "Community Ambient Acoustic Pressure",
        measurementMethod = "Class 1 integrating-averaging sound level meter (IEC 61672-1)",
        primarySource = "Urban traffic, commercial transit, HVAC chillers, construction machinery",
        healthImpact = "Speech interference, autonomic cardiovascular arousal, cognitive distraction."
    ),
    LEQ_NIGHT(
        code = "Leq,night",
        fullName = "Nighttime Equivalent Sound Level (22:00–07:00)",
        unit = "dB(A)",
        standardLimit = 45.0,
        standardReference = "WHO Night Noise Guidelines for Europe: ≤ 45 dB(A)",
        category = "Sleep Disturbance Acoustic Metric",
        measurementMethod = "Class 1 continuous logging sound meter (IEC 61672-1)",
        primarySource = "Night freight, 24/7 industrial blowers, rail corridors, aircraft overflights",
        healthImpact = "Sleep fragmentation, elevated nighttime cortisol, elevated hypertension risk."
    ),
    LMAX_PEAK(
        code = "Lmax",
        fullName = "Peak Maximum Acoustic Pressure",
        unit = "dB(A)",
        standardLimit = 85.0,
        standardReference = "OSHA Action Limit: 85 dB(A) / WHO Peak: 70 dB(A)",
        category = "Acute Transient Impulse Pressure",
        measurementMethod = "Fast response (125 ms) / Peak C-weighting detector",
        primarySource = "Hydraulic presses, sirens, metal stamping, engine backfires",
        healthImpact = "Acoustic trauma, temporary threshold shift (TTS), permanent hair cell damage."
    ),
    LDN_DAY_NIGHT(
        code = "Ldn",
        fullName = "Day-Night Sound Level (with 10 dB night penalty)",
        unit = "dB(A)",
        standardLimit = 55.0,
        standardReference = "EPA \"Levels Document\" Criterion: 55 dB(A)",
        category = "Cumulative 24-Hour Acoustic Dosage",
        measurementMethod = "Logarithmic energy integration with 10 dB night weighting",
        primarySource = "Integrated airport flight corridors, highway networks, mixed industrial belts",
        healthImpact = "High community annoyance, chronic stress response, long-term ischemic heart disease."
    );

    fun evaluateStatus(value: Double): ParameterStatus {
        val diff = value - standardLimit
        return when {
            diff <= -5.0 -> ParameterStatus.OPTIMAL
            diff <= 0.0 -> ParameterStatus.COMPLIANT
            diff <= 5.0 -> ParameterStatus.MODERATE
            diff <= 15.0 -> ParameterStatus.WARNING
            else -> ParameterStatus.CRITICAL
        }
    }
}

// ==========================================
// 3. SOIL & ECOLOGY TELEMETRY & STANDARDS
// ==========================================

enum class SoilParameterType(
    val code: String,
    val fullName: String,
    val unit: String,
    val standardLimit: Double,
    val standardReference: String,
    val category: String,
    val measurementMethod: String,
    val primarySource: String,
    val ecologicalImpact: String
) {
    SOIL_PH(
        code = "Soil pH",
        fullName = "Soil Reaction / Active Acidity",
        unit = "pH",
        standardLimit = 7.5,
        standardReference = "FAO Agronomic Optimum: 6.0 – 7.5",
        category = "Edaphic Chemical Equilibrium",
        measurementMethod = "1:2.5 soil-to-water suspension glass electrode (ISO 10390)",
        primarySource = "Parent rock mineralization, acid deposition, excessive ammoniacal fertilization",
        ecologicalImpact = "Governs nutrient bioavailability; acidic soils (<5.5) mobilize phytotoxic aluminum."
    ),
    MOISTURE(
        code = "Moisture",
        fullName = "Volumetric Water Content (VWC)",
        unit = "%",
        standardLimit = 35.0,
        standardReference = "USDA Soil Conservation: 25% – 45% Field Capacity",
        category = "Hydrological Edaphic State",
        measurementMethod = "Time-domain reflectometry (TDR) / Frequency-domain probe",
        primarySource = "Precipitation, capillary fringe rise, drip irrigation systems",
        ecologicalImpact = "Regulates root respiration, microbial nitrification, and plant transpiration."
    ),
    ORGANIC_MATTER(
        code = "SOM",
        fullName = "Soil Organic Matter",
        unit = "%",
        standardLimit = 4.0, // Minimum healthy threshold
        standardReference = "Regenerative Agriculture Standard: ≥ 4.0%",
        category = "Carbon Sink & Soil Biome Nutrition",
        measurementMethod = "Walkley-Black wet oxidation / Loss-on-ignition at 450°C",
        primarySource = "Crop residues, compost incorporation, cover crop root exudates",
        ecologicalImpact = "Enhances water retention capacity, builds aggregate stability, resists erosion."
    ),
    NITROGEN(
        code = "Available N",
        fullName = "Plant-Available Nitrogen (NO₃-N + NH₄-N)",
        unit = "mg/kg",
        standardLimit = 30.0,
        standardReference = "Agronomic Balanced Soil Test: 20 – 40 mg/kg",
        category = "Macronutrient Fertility Index",
        measurementMethod = "2M KCl extraction and automated flow colorimetry",
        primarySource = "Symbiotic biological fixation, organic mineralisation, mineral fertilizers",
        ecologicalImpact = "Critical for photosynthetic chlorophyll; excess leaches into groundwater aquifers."
    ),
    LEAD_HEAVY_METAL(
        code = "Pb (Lead)",
        fullName = "Total Soil Lead Contamination",
        unit = "mg/kg",
        standardLimit = 200.0,
        standardReference = "EPA Ecological Soil Screening Level: 200 mg/kg",
        category = "Anthropogenic Toxic Heavy Metal",
        measurementMethod = "Aqua regia microwave digestion + ICP-MS (EPA 3051A/6020B)",
        primarySource = "Legacy leaded fuel fallout, deteriorating lead paint, industrial smelter emissions",
        ecologicalImpact = "Binds persistently to clay-humus complexes; bioaccumulates up the terrestrial food chain."
    );

    fun evaluateStatus(value: Double): ParameterStatus {
        return when (this) {
            SOIL_PH -> {
                when {
                    value in 6.2..7.2 -> ParameterStatus.OPTIMAL
                    value in 6.0..7.8 -> ParameterStatus.COMPLIANT
                    value in 5.5..8.2 -> ParameterStatus.MODERATE
                    value in 4.8..8.8 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            MOISTURE -> {
                when {
                    value in 28.0..40.0 -> ParameterStatus.OPTIMAL
                    value in 22.0..48.0 -> ParameterStatus.COMPLIANT
                    value in 16.0..55.0 -> ParameterStatus.MODERATE
                    value in 10.0..65.0 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            ORGANIC_MATTER -> {
                // Higher SOM is better
                when {
                    value >= 5.0 -> ParameterStatus.OPTIMAL
                    value >= 4.0 -> ParameterStatus.COMPLIANT
                    value >= 2.5 -> ParameterStatus.MODERATE
                    value >= 1.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            NITROGEN -> {
                when {
                    value in 22.0..38.0 -> ParameterStatus.OPTIMAL
                    value in 18.0..45.0 -> ParameterStatus.COMPLIANT
                    value in 12.0..60.0 -> ParameterStatus.MODERATE
                    value in 8.0..75.0 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
            LEAD_HEAVY_METAL -> {
                val ratio = value / standardLimit
                when {
                    ratio <= 0.25 -> ParameterStatus.OPTIMAL
                    ratio <= 0.6 -> ParameterStatus.COMPLIANT
                    ratio <= 1.0 -> ParameterStatus.MODERATE
                    ratio <= 1.5 -> ParameterStatus.WARNING
                    else -> ParameterStatus.CRITICAL
                }
            }
        }
    }
}

// ==========================================
// UNIFIED PARAMETER STATUS & HISTORY MODELS
// ==========================================

enum class ParameterStatus(
    val label: String,
    val colorHex: Long,
    val description: String,
    val recommendation: String
) {
    OPTIMAL(
        label = "Pristine / Optimal",
        colorHex = 0xFF2E7D32,
        description = "Parameter values represent ideal environmental and health benchmarks.",
        recommendation = "Maintain current conservation practices and ongoing monitoring cadence."
    ),
    COMPLIANT(
        label = "Standard Compliant",
        colorHex = 0xFF0F6E43,
        description = "Meets regulatory and international health guidance criteria.",
        recommendation = "Safe for intended ecosystem and municipal uses under standard conditions."
    ),
    MODERATE(
        label = "Moderate / Elevated",
        colorHex = 0xFFF57F17,
        description = "Approaching upper allowable regulatory thresholds; early intervention advised.",
        recommendation = "Inspect upstream discharge vectors and calibrate telemetry sensors."
    ),
    WARNING(
        label = "Exceedance Alert",
        colorHex = 0xFFE65100,
        description = "Exceeds standard threshold limits; adverse environmental impact detected.",
        recommendation = "Trigger targeted mitigation controls and alert regional regulatory oversight."
    ),
    CRITICAL(
        label = "Hazardous / Severe",
        colorHex = 0xFFC62828,
        description = "Critical threshold violation with severe ecological and public health hazards.",
        recommendation = "Immediately halt unauthorized emissions and execute emergency remediation."
    );

    val color: Color get() = Color(colorHex)
}

data class EnvironmentalHourlyPoint(
    val hourLabel: String,
    val value: Double,
    val status: ParameterStatus
)

data class WaterMonitoringStation(
    val id: String,
    val name: String,
    val locationType: String,
    val distanceKm: Double,
    val ph: Double,
    val dissolvedOxygen: Double,
    val bod: Double,
    val cod: Double,
    val turbidity: Double,
    val nitrate: Double,
    val lastUpdated: String,
    val hourlyHistory: Map<WaterParameterType, List<EnvironmentalHourlyPoint>>
) {
    fun getValue(type: WaterParameterType): Double = when (type) {
        WaterParameterType.PH -> ph
        WaterParameterType.DISSOLVED_OXYGEN -> dissolvedOxygen
        WaterParameterType.BOD -> bod
        WaterParameterType.COD -> cod
        WaterParameterType.TURBIDITY -> turbidity
        WaterParameterType.NITRATE -> nitrate
    }

    val isExceeded: Boolean
        get() = ph < 6.5 || ph > 8.5 || dissolvedOxygen < 5.0 || bod > 5.0 || cod > 50.0 || turbidity > 4.0 || nitrate > 10.0

    fun getExceededParameters(): List<WaterParameterType> {
        val list = mutableListOf<WaterParameterType>()
        if (ph < 6.5 || ph > 8.5) list.add(WaterParameterType.PH)
        if (dissolvedOxygen < 5.0) list.add(WaterParameterType.DISSOLVED_OXYGEN)
        if (bod > 5.0) list.add(WaterParameterType.BOD)
        if (cod > 50.0) list.add(WaterParameterType.COD)
        if (turbidity > 4.0) list.add(WaterParameterType.TURBIDITY)
        if (nitrate > 10.0) list.add(WaterParameterType.NITRATE)
        return list
    }
}

data class NoiseMonitoringStation(
    val id: String,
    val name: String,
    val locationType: String,
    val distanceKm: Double,
    val leqDay: Double,
    val leqNight: Double,
    val lmaxPeak: Double,
    val ldnDayNight: Double,
    val lastUpdated: String,
    val hourlyHistory: Map<NoiseParameterType, List<EnvironmentalHourlyPoint>>
) {
    fun getValue(type: NoiseParameterType): Double = when (type) {
        NoiseParameterType.LEQ_DAY -> leqDay
        NoiseParameterType.LEQ_NIGHT -> leqNight
        NoiseParameterType.LMAX_PEAK -> lmaxPeak
        NoiseParameterType.LDN_DAY_NIGHT -> ldnDayNight
    }

    val isExceeded: Boolean
        get() = leqDay > 55.0 || leqNight > 45.0 || lmaxPeak > 85.0 || ldnDayNight > 55.0

    fun getExceededParameters(): List<NoiseParameterType> {
        val list = mutableListOf<NoiseParameterType>()
        if (leqDay > 55.0) list.add(NoiseParameterType.LEQ_DAY)
        if (leqNight > 45.0) list.add(NoiseParameterType.LEQ_NIGHT)
        if (lmaxPeak > 85.0) list.add(NoiseParameterType.LMAX_PEAK)
        if (ldnDayNight > 55.0) list.add(NoiseParameterType.LDN_DAY_NIGHT)
        return list
    }
}

data class SoilMonitoringStation(
    val id: String,
    val name: String,
    val locationType: String,
    val distanceKm: Double,
    val soilPh: Double,
    val moisturePercent: Double,
    val organicMatterPercent: Double,
    val availableNitrogen: Double,
    val leadHeavyMetal: Double,
    val lastUpdated: String,
    val hourlyHistory: Map<SoilParameterType, List<EnvironmentalHourlyPoint>>
) {
    fun getValue(type: SoilParameterType): Double = when (type) {
        SoilParameterType.SOIL_PH -> soilPh
        SoilParameterType.MOISTURE -> moisturePercent
        SoilParameterType.ORGANIC_MATTER -> organicMatterPercent
        SoilParameterType.NITROGEN -> availableNitrogen
        SoilParameterType.LEAD_HEAVY_METAL -> leadHeavyMetal
    }

    val isExceeded: Boolean
        get() = soilPh < 5.5 || soilPh > 8.0 || moisturePercent < 15.0 || organicMatterPercent < 2.5 || availableNitrogen > 45.0 || leadHeavyMetal > 200.0

    fun getExceededParameters(): List<SoilParameterType> {
        val list = mutableListOf<SoilParameterType>()
        if (soilPh < 5.5 || soilPh > 8.0) list.add(SoilParameterType.SOIL_PH)
        if (moisturePercent < 15.0) list.add(SoilParameterType.MOISTURE)
        if (organicMatterPercent < 2.5) list.add(SoilParameterType.ORGANIC_MATTER)
        if (availableNitrogen > 45.0) list.add(SoilParameterType.NITROGEN)
        if (leadHeavyMetal > 200.0) list.add(SoilParameterType.LEAD_HEAVY_METAL)
        return list
    }
}
