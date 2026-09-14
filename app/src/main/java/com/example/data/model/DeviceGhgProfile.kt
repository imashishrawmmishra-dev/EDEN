package com.example.data.model

data class DeviceGhgProfile(
    val deviceType: String = "Smartphone", // Smartphone, Laptop, Desktop Workstation, Tablet, Server
    val make: String = "Android Device",
    val model: String = "Pixel / Galaxy Series",
    val powerWatts: Double = 5.0,
    val dailyScreenHours: Double = 6.0,
    val gridCarbonIntensityKgKwh: Double = 0.385, // US average ~ 0.385, Global ~ 0.440
    val embodiedCarbonKg: Double = 55.0 // Manufacturing life-cycle carbon
) {
    // Watts / 1000 * kg_per_kwh * 1000 = Watts * kg_per_kwh (g CO2e / hour)
    val operationalGramsPerHour: Double
        get() = (powerWatts / 1000.0) * gridCarbonIntensityKgKwh * 1000.0

    val dailyOperationalCarbonKg: Double
        get() = (powerWatts * dailyScreenHours / 1000.0) * gridCarbonIntensityKgKwh

    // Amortized over 3-year expected lifecycle (365 * 3 days)
    val dailyAmortizedEmbodiedKg: Double
        get() = embodiedCarbonKg / (365.0 * 3.0)

    val dailyTotalDeviceKg: Double
        get() = dailyOperationalCarbonKg + dailyAmortizedEmbodiedKg

    val annualTotalKg: Double
        get() = dailyTotalDeviceKg * 365.0

    val gramsPerSecond: Double
        get() = operationalGramsPerHour / 3600.0
}
