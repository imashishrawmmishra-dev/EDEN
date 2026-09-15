package com.example.data.model

import androidx.compose.ui.graphics.Color

enum class AirPollutantType(
    val code: String,
    val fullName: String,
    val unit: String,
    val whoGuideline24h: Double,
    val epaStandardLimit: Double,
    val category: String,
    val measurementMethod: String,
    val primarySource: String,
    val healthImpact: String
) {
    PM25(
        code = "PM2.5",
        fullName = "Fine Particulate Matter (≤2.5 µm)",
        unit = "µg/m³",
        whoGuideline24h = 15.0,
        epaStandardLimit = 35.0,
        category = "Combustion Aerosols",
        measurementMethod = "Beta-attenuation / Laser photometer",
        primarySource = "Vehicle exhaust, biomass burning, industrial combustion",
        healthImpact = "Penetrates alveoli and enters bloodstream; cardiovascular & respiratory risks."
    ),
    PM10(
        code = "PM10",
        fullName = "Coarse Particulate Matter (≤10 µm)",
        unit = "µg/m³",
        whoGuideline24h = 45.0,
        epaStandardLimit = 150.0,
        category = "Suspended Dust & Aerosols",
        measurementMethod = "Tapered element oscillating microbalance (TEOM)",
        primarySource = "Road dust, construction, mechanical abrasion, pollen & spores",
        healthImpact = "Irritates eyes, throat, and upper airways; exacerbates asthma."
    ),
    NO2(
        code = "NO₂",
        fullName = "Nitrogen Dioxide",
        unit = "µg/m³",
        whoGuideline24h = 25.0,
        epaStandardLimit = 100.0,
        category = "Reactive Trace Gas",
        measurementMethod = "Chemiluminescence analyzer (ISO 7996)",
        primarySource = "Internal combustion engines, fossil-fueled thermal generators",
        healthImpact = "Bronchial inflammation, airway hyper-responsiveness, precursor to ozone."
    ),
    O3(
        code = "O₃",
        fullName = "Ground-Level Ozone (Tropospheric)",
        unit = "µg/m³",
        whoGuideline24h = 100.0,
        epaStandardLimit = 140.0,
        category = "Photochemical Smog Oxidant",
        measurementMethod = "UV Photometric Absorption (254 nm)",
        primarySource = "Solar UV photo-oxidation of NOx + Volatile Organic Compounds (VOCs)",
        healthImpact = "Strong pulmonary oxidant; causes chest tightness, reduced lung capacity."
    );

    fun evaluateStatus(value: Double): AirQualityStatus {
        val ratio = value / whoGuideline24h
        return when {
            ratio <= 0.67 -> AirQualityStatus.EXCELLENT
            ratio <= 1.0 -> AirQualityStatus.GOOD
            ratio <= 1.5 -> AirQualityStatus.MODERATE
            ratio <= 2.5 -> AirQualityStatus.UNHEALTHY_SENSITIVE
            ratio <= 4.0 -> AirQualityStatus.UNHEALTHY
            else -> AirQualityStatus.HAZARDOUS
        }
    }
}

enum class AirQualityStatus(
    val label: String,
    val colorHex: Long,
    val description: String,
    val healthAdvice: String
) {
    EXCELLENT(
        label = "Excellent (Pristine)",
        colorHex = 0xFF2E7D32,
        description = "Concentration is far below WHO guidance limits.",
        healthAdvice = "Ideal conditions for all outdoor activities and natural ventilation."
    ),
    GOOD(
        label = "Good (WHO Compliant)",
        colorHex = 0xFF0F6E43,
        description = "Meets WHO 2021 24-hour health protection targets.",
        healthAdvice = "Air quality is satisfactory; safe for outdoor physical exertion."
    ),
    MODERATE(
        label = "Moderate (Acceptable)",
        colorHex = 0xFFF57F17,
        description = "Approaching or slightly exceeding stringent WHO targets.",
        healthAdvice = "Very sensitive individuals should consider taking rest breaks during outdoor exertion."
    ),
    UNHEALTHY_SENSITIVE(
        label = "Sensitive Caution",
        colorHex = 0xFFE65100,
        description = "Elevated pollutant level affecting vulnerable demographics.",
        healthAdvice = "Children, asthmatics, and older adults should limit intense outdoor workouts."
    ),
    UNHEALTHY(
        label = "Unhealthy",
        colorHex = 0xFFC62828,
        description = "Exceeds standard limits; general population may experience irritation.",
        healthAdvice = "Wear N95/FFP2 masks outdoors; activate indoor HEPA filtration."
    ),
    HAZARDOUS(
        label = "Hazardous Alert",
        colorHex = 0xFF880E4F,
        description = "Severe acute pollution event with severe health risks.",
        healthAdvice = "Remain indoors, seal windows, and run maximum air purification."
    );

    val color: Color get() = Color(colorHex)
}

data class AirQualityHourlyPoint(
    val hourLabel: String,
    val value: Double,
    val status: AirQualityStatus
)

data class AirQualityStation(
    val id: String,
    val name: String,
    val locationType: String,
    val distanceKm: Double,
    val pm25: Double,
    val pm10: Double,
    val no2: Double,
    val o3: Double,
    val lastUpdated: String,
    val hourlyHistory: Map<AirPollutantType, List<AirQualityHourlyPoint>>
) {
    fun getValue(type: AirPollutantType): Double = when (type) {
        AirPollutantType.PM25 -> pm25
        AirPollutantType.PM10 -> pm10
        AirPollutantType.NO2 -> no2
        AirPollutantType.O3 -> o3
    }

    fun isThresholdExceeded(type: AirPollutantType): Boolean = getValue(type) > type.whoGuideline24h

    val isPm25Exceeded: Boolean get() = pm25 > AirPollutantType.PM25.whoGuideline24h
    val isNo2Exceeded: Boolean get() = no2 > AirPollutantType.NO2.whoGuideline24h
    val isPm10Exceeded: Boolean get() = pm10 > AirPollutantType.PM10.whoGuideline24h
    val isO3Exceeded: Boolean get() = o3 > AirPollutantType.O3.whoGuideline24h

    val hasThresholdAlert: Boolean get() = isPm25Exceeded || isNo2Exceeded || isPm10Exceeded || isO3Exceeded

    fun getExceededPollutants(): List<AirPollutantType> {
        val list = mutableListOf<AirPollutantType>()
        if (isPm25Exceeded) list.add(AirPollutantType.PM25)
        if (isNo2Exceeded) list.add(AirPollutantType.NO2)
        if (isPm10Exceeded) list.add(AirPollutantType.PM10)
        if (isO3Exceeded) list.add(AirPollutantType.O3)
        return list
    }
}

data class AirThresholdNotification(
    val pollutant: AirPollutantType,
    val currentValue: Double,
    val safeLimit: Double,
    val message: String
)
