package com.example.calculator

import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.PI

object CarbonCalculatorEngine {

    enum class FuelType(val displayName: String, val unit: String, val factorKgCo2e: Double, val authority: String) {
        DIESEL("Diesel Fuel", "litres", 2.687, "UK DEFRA / US EPA"),
        PETROL("Petrol / Gasoline", "litres", 2.315, "UK DEFRA / US EPA"),
        NATURAL_GAS("Natural Gas", "m³", 2.030, "IPCC / DEFRA"),
        LPG("Liquefied Petroleum Gas (LPG)", "litres", 1.555, "UK DEFRA"),
        COAL("Bituminous Coal", "kg", 2.420, "IPCC 2006 / EPA")
    }

    enum class GridRegion(val displayName: String, val factorKgCo2ePerKwh: Double) {
        GLOBAL_AVERAGE("Global Grid Average", 0.475),
        US_AVERAGE("US Grid Average (eGRID)", 0.386),
        EU_AVERAGE("EU Grid Average (EEA)", 0.230),
        HIGH_COAL("High-Coal Grid Mix", 0.750),
        HIGH_RENEWABLE("High-Renewable Grid Mix", 0.080)
    }

    enum class TransportMode(val displayName: String, val unit: String, val factorKgCo2e: Double) {
        CAR_PETROL("Passenger Car (Average)", "km", 0.170),
        ELECTRIC_VEHICLE("Electric Vehicle (Average Grid)", "km", 0.053),
        TRAIN_COMMUTER("Commuter Rail / Train", "passenger-km", 0.035),
        FLIGHT_DOMESTIC("Domestic Flight (<1000 km)", "passenger-km", 0.158),
        FLIGHT_LONG_HAUL("Long-haul Flight (>3700 km)", "passenger-km", 0.102),
        ROAD_FREIGHT("Heavy Goods Truck", "tonne-km", 0.105),
        MARITIME_FREIGHT("Container Ship", "tonne-km", 0.016)
    }

    data class CarbonInventoryResult(
        val scope1Kg: Double,
        val scope2Kg: Double,
        val scope3Kg: Double,
        val totalKg: Double,
        val totalTonnes: Double,
        val scope1Percent: Double,
        val scope2Percent: Double,
        val scope3Percent: Double,
        val dominantScope: String,
        val reductionScenarios: List<String>,
        val methodologyNote: String
    )

    fun calculateInventory(
        fuelType: FuelType,
        fuelQuantity: Double,
        electricityKwh: Double,
        gridRegion: GridRegion,
        transportMode: TransportMode,
        transportVolume: Double
    ): CarbonInventoryResult {
        val scope1 = fuelQuantity * fuelType.factorKgCo2e
        val scope2 = electricityKwh * gridRegion.factorKgCo2ePerKwh
        val scope3 = transportVolume * transportMode.factorKgCo2e
        val total = scope1 + scope2 + scope3
        val totalTonnes = total / 1000.0

        val s1Pct = if (total > 0) (scope1 / total) * 100.0 else 0.0
        val s2Pct = if (total > 0) (scope2 / total) * 100.0 else 0.0
        val s3Pct = if (total > 0) (scope3 / total) * 100.0 else 0.0

        val dominant = when {
            scope1 >= scope2 && scope1 >= scope3 -> "Scope 1 (Direct Fuel Combustion)"
            scope2 >= scope1 && scope2 >= scope3 -> "Scope 2 (Electricity Consumption)"
            else -> "Scope 3 (Value Chain / Logistics)"
        }

        val scenarios = mutableListOf<String>()
        if (s1Pct > 35) {
            scenarios.add("Scope 1 reduction: Transition stationary combustion to high-temperature industrial heat pumps or certified biomethane.")
        }
        if (s2Pct > 35) {
            scenarios.add("Scope 2 reduction: Procure on-site solar PV + battery storage or execute a Corporate Power Purchase Agreement (PPA) with EACs.")
        }
        if (s3Pct > 35) {
            scenarios.add("Scope 3 reduction: Shift freight from road to electrified rail; adopt multimodal logistics and supplier decarbonization covenants.")
        }
        if (scenarios.isEmpty()) {
            scenarios.add("Balanced footprint: Pursue ISO 50001 energy management certification and annual verified carbon disclosure.")
        }

        val note = "Deterministic calculation compliant with GHG Protocol Corporate Accounting Standard. " +
                "Factors: ${fuelType.displayName} = ${fuelType.factorKgCo2e} kg CO₂e/${fuelType.unit} (${fuelType.authority}); " +
                "Grid = ${gridRegion.factorKgCo2ePerKwh} kg CO₂e/kWh; Transport = ${transportMode.factorKgCo2e} kg CO₂e/${transportMode.unit}."

        return CarbonInventoryResult(
            scope1Kg = scope1,
            scope2Kg = scope2,
            scope3Kg = scope3,
            totalKg = total,
            totalTonnes = totalTonnes,
            scope1Percent = s1Pct,
            scope2Percent = s2Pct,
            scope3Percent = s3Pct,
            dominantScope = dominant,
            reductionScenarios = scenarios,
            methodologyNote = note
        )
    }
}

object AirCalculatorEngine {

    data class StackFlowResult(
        val ductAreaM2: Double,
        val flowRateM3Sec: Double,
        val flowRateM3Hour: Double,
        val emissionRateKgHour: Double,
        val emissionRateGSec: Double,
        val formulaExplanation: String
    )

    fun calculateStackFlow(
        velocityMPerSec: Double,
        diameterMeters: Double,
        pollutantConcMgPerM3: Double
    ): StackFlowResult {
        val area = PI * (diameterMeters / 2.0).pow(2.0)
        val flowM3Sec = velocityMPerSec * area
        val flowM3Hr = flowM3Sec * 3600.0
        val emissionKgHr = flowM3Hr * pollutantConcMgPerM3 * 1e-6
        val emissionGSec = emissionKgHr * 1000.0 / 3600.0

        val explanation = "Area A = π·(D/2)² = %.3f m². Flow Q = V·A = %.2f m³/s (%.1f m³/h). Emission Rate E = Q·C·10⁻⁶ = %.4f kg/h (%.4f g/s). Complies with EPA Method 2."
            .format(area, flowM3Sec, flowM3Hr, emissionKgHr, emissionGSec)

        return StackFlowResult(
            ductAreaM2 = area,
            flowRateM3Sec = flowM3Sec,
            flowRateM3Hour = flowM3Hr,
            emissionRateKgHour = emissionKgHr,
            emissionRateGSec = emissionGSec,
            formulaExplanation = explanation
        )
    }

    enum class GasPollutant(val displayName: String, val molarMass: Double) {
        SO2("Sulfur Dioxide (SO₂)", 64.06),
        NO2("Nitrogen Dioxide (NO₂)", 46.01),
        CO("Carbon Monoxide (CO)", 28.01),
        O3("Ozone (O₃)", 48.00)
    }

    fun convertMgToPpm(gas: GasPollutant, mgPerM3: Double, tempCelsius: Double = 25.0, pressureAtm: Double = 1.0): Double {
        // Ideal gas molar volume at T & P: V_m = (R * T) / P
        // At 25°C (298.15K) and 1 atm, V_m ≈ 24.45 L/mol
        val kelvin = tempCelsius + 273.15
        val molarVolume = (24.45 * (kelvin / 298.15)) / pressureAtm
        return (mgPerM3 * molarVolume) / gas.molarMass
    }

    fun convertPpmToMg(gas: GasPollutant, ppm: Double, tempCelsius: Double = 25.0, pressureAtm: Double = 1.0): Double {
        val kelvin = tempCelsius + 273.15
        val molarVolume = (24.45 * (kelvin / 298.15)) / pressureAtm
        return (ppm * gas.molarMass) / molarVolume
    }
}

object WaterCalculatorEngine {

    data class WaterLoadResult(
        val bodLoadKgDay: Double,
        val codLoadKgDay: Double,
        val populationEquivalent: Double,
        val hrtHours: Double,
        val bodCodRatio: Double,
        val biodegradabilityClass: String,
        val treatmentRecommendation: String
    )

    fun calculateWastewaterMetrics(
        flowM3Day: Double,
        bodMgL: Double,
        codMgL: Double,
        tankVolumeM3: Double = 0.0
    ): WaterLoadResult {
        // Load (kg/day) = Q (m³/day) * C (mg/L = g/m³) * 10⁻³
        val bodLoad = flowM3Day * bodMgL * 1e-3
        val codLoad = flowM3Day * codMgL * 1e-3
        // 1 PE = 0.06 kg BOD5/capita/day (standard EU/EPA secondary treatment design)
        val pe = if (bodLoad > 0) bodLoad / 0.06 else 0.0
        val hrt = if (flowM3Day > 0 && tankVolumeM3 > 0) (tankVolumeM3 / flowM3Day) * 24.0 else 0.0

        val ratio = if (codMgL > 0) bodMgL / codMgL else 0.0

        val (bioClass, reco) = when {
            ratio >= 0.5 -> Pair(
                "Readily Biodegradable (BOD/COD >= 0.5)",
                "Excellent candidate for conventional biological activated sludge (CAS), SBR, or MBR treatment."
            )
            ratio >= 0.2 -> Pair(
                "Moderately Biodegradable (0.2 <= BOD/COD < 0.5)",
                "Biological treatment feasible with acclimated biomass or extended aeration; pre-aeration recommended."
            )
            else -> Pair(
                "Recalcitrant / Toxic (BOD/COD < 0.2)",
                "Incompatible with standard biological digestion. Requires chemical coagulation, Fenton oxidation, or Ozone/UV AOP."
            )
        }

        return WaterLoadResult(
            bodLoadKgDay = bodLoad,
            codLoadKgDay = codLoad,
            populationEquivalent = pe,
            hrtHours = hrt,
            bodCodRatio = ratio,
            biodegradabilityClass = bioClass,
            treatmentRecommendation = reco
        )
    }
}

object NoiseCalculatorEngine {

    data class AcousticResult(
        val totalDecibels: Double,
        val dayNightLevelLdn: Double,
        val attenuatedDbAtDistance: Double,
        val complianceStatus: String
    )

    fun calculateAcoustics(
        sourcesDb: List<Double>,
        dayLeq: Double,
        nightLeq: Double,
        refDistanceM: Double,
        targetDistanceM: Double
    ): AcousticResult {
        // Sum of decibels: L_sum = 10 * log10(sum(10^(L_i / 10)))
        val sumLinear = sourcesDb.sumOf { 10.0.pow(it / 10.0) }
        val lTotal = if (sumLinear > 0) 10.0 * log10(sumLinear) else 0.0

        // L_dn = 10 * log10((15/24)*10^(L_day/10) + (9/24)*10^((L_night + 10)/10))
        val dayPart = (15.0 / 24.0) * 10.0.pow(dayLeq / 10.0)
        val nightPart = (9.0 / 24.0) * 10.0.pow((nightLeq + 10.0) / 10.0)
        val ldn = 10.0 * log10(dayPart + nightPart)

        // Point source distance attenuation: L2 = L1 - 20 * log10(r2 / r1)
        val distRatio = if (refDistanceM > 0 && targetDistanceM > 0) targetDistanceM / refDistanceM else 1.0
        val distAtten = lTotal - (20.0 * log10(distRatio))

        val status = when {
            ldn <= 55.0 -> "Compliant with WHO Residential Guidelines (<= 55 dBA)"
            ldn <= 65.0 -> "Moderate Exposure: Meets Commercial Zone Limits (<= 65 dBA)"
            else -> "Exceeds Environmental Noise Thresholds: Noise mitigation barriers required"
        }

        return AcousticResult(
            totalDecibels = lTotal,
            dayNightLevelLdn = ldn,
            attenuatedDbAtDistance = distAtten,
            complianceStatus = status
        )
    }
}
