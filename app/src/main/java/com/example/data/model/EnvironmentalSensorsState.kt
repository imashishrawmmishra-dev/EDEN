package com.example.data.model

data class EnvironmentalSensorsState(
    val indoorTempC: Double = 22.4,
    val outdoorTempC: Double = 28.6,
    val indoorHumidityRh: Double = 47.5,
    val outdoorHumidityRh: Double = 58.0,
    val locationName: String = "Local Environmental Station",
    // WHO 2021 Global Air Quality Guidelines
    val pm25: Double = 11.4, // WHO 24-hr guideline = 15 µg/m³
    val pm10: Double = 28.2, // WHO 24-hr guideline = 45 µg/m³
    val no2: Double = 18.5,  // WHO 24-hr guideline = 25 µg/m³
    val so2: Double = 8.4,   // WHO 24-hr guideline = 40 µg/m³
    val o3: Double = 52.0,   // WHO 8-hr guideline = 100 µg/m³
    val co: Double = 0.9     // WHO 24-hr guideline = 4 mg/m³
) {
    val indoorTempF: Double
        get() = (indoorTempC * 9.0 / 5.0) + 32.0

    val outdoorTempF: Double
        get() = (outdoorTempC * 9.0 / 5.0) + 32.0

    val whoAqiStatus: String
        get() = when {
            pm25 <= 15.0 && pm10 <= 45.0 && no2 <= 25.0 -> "Good (WHO Compliant)"
            pm25 <= 25.0 && pm10 <= 65.0 -> "Moderate Air Quality"
            pm25 <= 35.0 -> "Unhealthy for Sensitive Groups"
            pm25 <= 50.0 -> "Unhealthy"
            else -> "Hazardous (Exceeds WHO 2021 Limits)"
        }

    val whoAdvisory: String
        get() = when {
            pm25 <= 15.0 -> "Air quality conforms to WHO 2021 health standards. Safe for outdoor ventilation and physical activity."
            pm25 <= 25.0 -> "Acceptable air quality. Sensitive individuals should consider reducing prolonged outdoor exertion."
            pm25 <= 35.0 -> "Particulate levels elevated. Children, elderly, and those with respiratory conditions should wear masks outdoors."
            else -> "High pollutant burden. Minimize outdoor exposure and activate indoor HEPA filtration."
        }
}
