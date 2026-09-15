package com.example.data.repository

import com.example.data.model.EnvironmentalHourlyPoint
import com.example.data.model.NoiseMonitoringStation
import com.example.data.model.NoiseParameterType
import com.example.data.model.ParameterStatus
import com.example.data.model.SoilMonitoringStation
import com.example.data.model.SoilParameterType
import com.example.data.model.WaterMonitoringStation
import com.example.data.model.WaterParameterType

object EnvironmentalMonitoringRepository {

    private val hourLabels = listOf(
        "00:00", "02:00", "04:00", "06:00", "08:00", "10:00",
        "12:00", "14:00", "16:00", "18:00", "20:00", "Now"
    )

    private fun generateWaterHistory(values: List<Double>, param: WaterParameterType): List<EnvironmentalHourlyPoint> {
        return values.mapIndexed { index, value ->
            EnvironmentalHourlyPoint(
                hourLabel = hourLabels.getOrElse(index) { "${index * 2}:00" },
                value = value,
                status = param.evaluateStatus(value)
            )
        }
    }

    private fun generateNoiseHistory(values: List<Double>, param: NoiseParameterType): List<EnvironmentalHourlyPoint> {
        return values.mapIndexed { index, value ->
            EnvironmentalHourlyPoint(
                hourLabel = hourLabels.getOrElse(index) { "${index * 2}:00" },
                value = value,
                status = param.evaluateStatus(value)
            )
        }
    }

    private fun generateSoilHistory(values: List<Double>, param: SoilParameterType): List<EnvironmentalHourlyPoint> {
        return values.mapIndexed { index, value ->
            EnvironmentalHourlyPoint(
                hourLabel = hourLabels.getOrElse(index) { "${index * 2}:00" },
                value = value,
                status = param.evaluateStatus(value)
            )
        }
    }

    // ==========================================
    // DEFAULT WATER MONITORING STATIONS
    // ==========================================

    val defaultWaterStations: List<WaterMonitoringStation> = listOf(
        WaterMonitoringStation(
            id = "water_station_reservoir",
            name = "Municipal Reservoir Intake Alpha",
            locationType = "Protected Drinking Water Aqueduct Catchment",
            distanceKm = 2.4,
            ph = 7.3,
            dissolvedOxygen = 8.4,
            bod = 1.8,
            cod = 14.2,
            turbidity = 0.8,
            nitrate = 3.2,
            lastUpdated = "Just now (Automated Potentiometric Array)",
            hourlyHistory = mapOf(
                WaterParameterType.PH to generateWaterHistory(
                    listOf(7.2, 7.2, 7.3, 7.3, 7.4, 7.4, 7.3, 7.3, 7.3, 7.4, 7.3, 7.3),
                    WaterParameterType.PH
                ),
                WaterParameterType.DISSOLVED_OXYGEN to generateWaterHistory(
                    listOf(8.9, 9.1, 9.0, 8.8, 8.6, 8.5, 8.3, 8.2, 8.4, 8.5, 8.4, 8.4),
                    WaterParameterType.DISSOLVED_OXYGEN
                ),
                WaterParameterType.BOD to generateWaterHistory(
                    listOf(1.4, 1.5, 1.5, 1.6, 1.7, 1.9, 2.0, 1.9, 1.8, 1.8, 1.7, 1.8),
                    WaterParameterType.BOD
                ),
                WaterParameterType.COD to generateWaterHistory(
                    listOf(12.0, 12.5, 13.0, 13.5, 14.0, 15.2, 16.0, 15.5, 14.8, 14.5, 14.0, 14.2),
                    WaterParameterType.COD
                ),
                WaterParameterType.TURBIDITY to generateWaterHistory(
                    listOf(0.6, 0.6, 0.7, 0.7, 0.8, 0.9, 1.1, 1.0, 0.9, 0.8, 0.8, 0.8),
                    WaterParameterType.TURBIDITY
                ),
                WaterParameterType.NITRATE to generateWaterHistory(
                    listOf(2.8, 2.9, 3.0, 3.1, 3.2, 3.4, 3.5, 3.4, 3.3, 3.2, 3.2, 3.2),
                    WaterParameterType.NITRATE
                )
            )
        ),
        WaterMonitoringStation(
            id = "water_station_river_basin",
            name = "Riparian Catchment Confluence Beta",
            locationType = "Ecosystem Wetland & Surface Water Basin",
            distanceKm = 5.1,
            ph = 7.8,
            dissolvedOxygen = 6.2,
            bod = 4.2,
            cod = 28.5,
            turbidity = 3.6,
            nitrate = 7.8,
            lastUpdated = "6 mins ago (Multi-parameter Sonde)",
            hourlyHistory = mapOf(
                WaterParameterType.PH to generateWaterHistory(
                    listOf(7.5, 7.6, 7.6, 7.7, 7.8, 7.9, 8.0, 7.9, 7.8, 7.8, 7.7, 7.8),
                    WaterParameterType.PH
                ),
                WaterParameterType.DISSOLVED_OXYGEN to generateWaterHistory(
                    listOf(7.2, 7.0, 6.8, 6.5, 6.4, 6.3, 6.1, 6.0, 6.1, 6.2, 6.3, 6.2),
                    WaterParameterType.DISSOLVED_OXYGEN
                ),
                WaterParameterType.BOD to generateWaterHistory(
                    listOf(3.2, 3.4, 3.5, 3.8, 4.0, 4.4, 4.6, 4.5, 4.3, 4.2, 4.1, 4.2),
                    WaterParameterType.BOD
                ),
                WaterParameterType.COD to generateWaterHistory(
                    listOf(22.0, 23.5, 24.0, 26.0, 28.0, 30.5, 31.0, 29.8, 29.0, 28.5, 28.0, 28.5),
                    WaterParameterType.COD
                ),
                WaterParameterType.TURBIDITY to generateWaterHistory(
                    listOf(2.4, 2.6, 2.8, 3.2, 3.5, 4.1, 4.3, 4.0, 3.8, 3.7, 3.6, 3.6),
                    WaterParameterType.TURBIDITY
                ),
                WaterParameterType.NITRATE to generateWaterHistory(
                    listOf(6.2, 6.5, 6.8, 7.2, 7.5, 8.1, 8.4, 8.1, 7.9, 7.8, 7.7, 7.8),
                    WaterParameterType.NITRATE
                )
            )
        ),
        WaterMonitoringStation(
            id = "water_station_industrial_effluent",
            name = "Industrial Discharge Canal Gamma",
            locationType = "Manufacturing Estate Outfall Point",
            distanceKm = 9.3,
            ph = 8.9, // EXCEEDANCE: High alkalinity
            dissolvedOxygen = 3.8, // EXCEEDANCE: Hypoxic (< 5.0)
            bod = 9.4, // EXCEEDANCE: Elevated organic load (> 5.0)
            cod = 68.0, // EXCEEDANCE: Chemical load (> 50.0)
            turbidity = 6.2, // EXCEEDANCE: Turbid (> 4.0)
            nitrate = 14.5, // EXCEEDANCE: High nitrogen (> 10.0)
            lastUpdated = "2 mins ago (Spectrophotometric Online Monitor)",
            hourlyHistory = mapOf(
                WaterParameterType.PH to generateWaterHistory(
                    listOf(8.2, 8.4, 8.5, 8.7, 8.9, 9.1, 9.2, 9.1, 9.0, 8.9, 8.8, 8.9),
                    WaterParameterType.PH
                ),
                WaterParameterType.DISSOLVED_OXYGEN to generateWaterHistory(
                    listOf(4.8, 4.6, 4.4, 4.1, 3.9, 3.7, 3.5, 3.6, 3.7, 3.8, 3.9, 3.8),
                    WaterParameterType.DISSOLVED_OXYGEN
                ),
                WaterParameterType.BOD to generateWaterHistory(
                    listOf(6.8, 7.2, 7.6, 8.4, 9.2, 9.8, 10.2, 9.9, 9.6, 9.4, 9.2, 9.4),
                    WaterParameterType.BOD
                ),
                WaterParameterType.COD to generateWaterHistory(
                    listOf(52.0, 56.0, 59.0, 64.0, 68.0, 72.0, 74.5, 71.0, 69.5, 68.0, 67.0, 68.0),
                    WaterParameterType.COD
                ),
                WaterParameterType.TURBIDITY to generateWaterHistory(
                    listOf(4.2, 4.6, 5.1, 5.8, 6.4, 6.8, 7.0, 6.7, 6.5, 6.3, 6.2, 6.2),
                    WaterParameterType.TURBIDITY
                ),
                WaterParameterType.NITRATE to generateWaterHistory(
                    listOf(11.2, 11.8, 12.4, 13.5, 14.2, 15.0, 15.4, 14.9, 14.6, 14.5, 14.3, 14.5),
                    WaterParameterType.NITRATE
                )
            )
        )
    )

    // ==========================================
    // DEFAULT NOISE & ACOUSTICS STATIONS
    // ==========================================

    val defaultNoiseStations: List<NoiseMonitoringStation> = listOf(
        NoiseMonitoringStation(
            id = "noise_station_suburban",
            name = "Green Corridor Sanctuary Zone",
            locationType = "Low-Density Residential Acoustic Reserve",
            distanceKm = 3.6,
            leqDay = 46.2,
            leqNight = 38.5,
            lmaxPeak = 58.0,
            ldnDayNight = 48.0,
            lastUpdated = "Just now (Class 1 Sound Analyzer)",
            hourlyHistory = mapOf(
                NoiseParameterType.LEQ_DAY to generateNoiseHistory(
                    listOf(42.0, 41.5, 41.0, 44.0, 47.5, 48.0, 47.2, 46.5, 47.0, 47.5, 46.8, 46.2),
                    NoiseParameterType.LEQ_DAY
                ),
                NoiseParameterType.LEQ_NIGHT to generateNoiseHistory(
                    listOf(37.0, 36.5, 36.0, 37.5, 39.0, 40.0, 39.5, 39.0, 38.5, 38.0, 38.2, 38.5),
                    NoiseParameterType.LEQ_NIGHT
                ),
                NoiseParameterType.LMAX_PEAK to generateNoiseHistory(
                    listOf(52.0, 50.0, 49.0, 56.0, 61.0, 63.0, 60.5, 59.0, 60.0, 61.0, 58.5, 58.0),
                    NoiseParameterType.LMAX_PEAK
                ),
                NoiseParameterType.LDN_DAY_NIGHT to generateNoiseHistory(
                    listOf(44.0, 43.5, 43.0, 46.0, 49.0, 50.0, 49.2, 48.5, 48.8, 49.2, 48.4, 48.0),
                    NoiseParameterType.LDN_DAY_NIGHT
                )
            )
        ),
        NoiseMonitoringStation(
            id = "noise_station_commercial",
            name = "Downtown Transit Concourse",
            locationType = "Mixed Commercial / Arterial Corridor",
            distanceKm = 1.8,
            leqDay = 62.4, // EXCEEDANCE: Above WHO 55 dB(A)
            leqNight = 51.2, // EXCEEDANCE: Above WHO 45 dB(A)
            lmaxPeak = 76.5,
            ldnDayNight = 64.0, // EXCEEDANCE: Above EPA 55 dB(A)
            lastUpdated = "1 min ago (Outdoor Weatherproof Microphone)",
            hourlyHistory = mapOf(
                NoiseParameterType.LEQ_DAY to generateNoiseHistory(
                    listOf(54.0, 52.0, 51.0, 59.5, 64.0, 65.5, 64.2, 63.0, 64.8, 65.0, 63.5, 62.4),
                    NoiseParameterType.LEQ_DAY
                ),
                NoiseParameterType.LEQ_NIGHT to generateNoiseHistory(
                    listOf(48.0, 46.5, 45.0, 49.0, 52.5, 54.0, 53.0, 52.0, 51.5, 52.0, 51.0, 51.2),
                    NoiseParameterType.LEQ_NIGHT
                ),
                NoiseParameterType.LMAX_PEAK to generateNoiseHistory(
                    listOf(68.0, 66.0, 64.0, 75.0, 79.0, 81.5, 78.0, 76.0, 78.5, 80.0, 77.0, 76.5),
                    NoiseParameterType.LMAX_PEAK
                ),
                NoiseParameterType.LDN_DAY_NIGHT to generateNoiseHistory(
                    listOf(56.0, 54.5, 53.0, 61.0, 65.5, 67.0, 65.8, 64.5, 66.0, 66.5, 65.0, 64.0),
                    NoiseParameterType.LDN_DAY_NIGHT
                )
            )
        ),
        NoiseMonitoringStation(
            id = "noise_station_heavy_industry",
            name = "Freight Rail & Metallurgy Yard",
            locationType = "Heavy Industrial Logistics Perimeter",
            distanceKm = 7.4,
            leqDay = 73.8, // EXCEEDANCE
            leqNight = 64.5, // EXCEEDANCE
            lmaxPeak = 92.4, // EXCEEDANCE: OSHA action limit 85 dB(A) breached!
            ldnDayNight = 76.2, // EXCEEDANCE
            lastUpdated = "Just now (Industrial Telemetry Hub)",
            hourlyHistory = mapOf(
                NoiseParameterType.LEQ_DAY to generateNoiseHistory(
                    listOf(66.0, 64.0, 63.5, 71.0, 75.0, 76.5, 75.2, 74.0, 75.8, 76.0, 74.5, 73.8),
                    NoiseParameterType.LEQ_DAY
                ),
                NoiseParameterType.LEQ_NIGHT to generateNoiseHistory(
                    listOf(58.0, 56.5, 55.0, 61.0, 65.5, 67.0, 66.0, 65.0, 65.5, 66.0, 65.0, 64.5),
                    NoiseParameterType.LEQ_NIGHT
                ),
                NoiseParameterType.LMAX_PEAK to generateNoiseHistory(
                    listOf(82.0, 80.0, 78.0, 89.0, 94.5, 96.0, 93.0, 91.0, 93.5, 95.0, 93.0, 92.4),
                    NoiseParameterType.LMAX_PEAK
                ),
                NoiseParameterType.LDN_DAY_NIGHT to generateNoiseHistory(
                    listOf(68.0, 66.5, 65.0, 73.0, 77.5, 79.0, 77.8, 76.5, 78.0, 78.5, 77.0, 76.2),
                    NoiseParameterType.LDN_DAY_NIGHT
                )
            )
        )
    )

    // ==========================================
    // DEFAULT SOIL & ECOLOGY STATIONS
    // ==========================================

    val defaultSoilStations: List<SoilMonitoringStation> = listOf(
        SoilMonitoringStation(
            id = "soil_station_regenerative",
            name = "Regenerative Agroforestry Plot Alpha",
            locationType = "Zero-Tillage Organic Soil Sanctuary",
            distanceKm = 4.2,
            soilPh = 6.8,
            moisturePercent = 32.5,
            organicMatterPercent = 5.2,
            availableNitrogen = 28.4,
            leadHeavyMetal = 18.2,
            lastUpdated = "Just now (In-situ TDR & Ion Selective Probe)",
            hourlyHistory = mapOf(
                SoilParameterType.SOIL_PH to generateSoilHistory(
                    listOf(6.8, 6.8, 6.8, 6.8, 6.8, 6.9, 6.9, 6.8, 6.8, 6.8, 6.8, 6.8),
                    SoilParameterType.SOIL_PH
                ),
                SoilParameterType.MOISTURE to generateSoilHistory(
                    listOf(34.0, 33.8, 33.5, 33.0, 32.5, 32.0, 31.8, 31.5, 32.0, 32.2, 32.4, 32.5),
                    SoilParameterType.MOISTURE
                ),
                SoilParameterType.ORGANIC_MATTER to generateSoilHistory(
                    listOf(5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2, 5.2),
                    SoilParameterType.ORGANIC_MATTER
                ),
                SoilParameterType.NITROGEN to generateSoilHistory(
                    listOf(28.0, 28.1, 28.2, 28.3, 28.4, 28.6, 28.5, 28.4, 28.4, 28.4, 28.4, 28.4),
                    SoilParameterType.NITROGEN
                ),
                SoilParameterType.LEAD_HEAVY_METAL to generateSoilHistory(
                    listOf(18.0, 18.0, 18.1, 18.1, 18.2, 18.2, 18.2, 18.2, 18.2, 18.2, 18.2, 18.2),
                    SoilParameterType.LEAD_HEAVY_METAL
                )
            )
        ),
        SoilMonitoringStation(
            id = "soil_station_wetland",
            name = "Riparian Peatland & Wetland Reserve",
            locationType = "High-Carbon Boreal Swamp Biome",
            distanceKm = 6.9,
            soilPh = 5.8,
            moisturePercent = 48.0,
            organicMatterPercent = 8.5,
            availableNitrogen = 19.5,
            leadHeavyMetal = 12.0,
            lastUpdated = "8 mins ago (Continuous Capacitive Sensor)",
            hourlyHistory = mapOf(
                SoilParameterType.SOIL_PH to generateSoilHistory(
                    listOf(5.8, 5.8, 5.8, 5.8, 5.8, 5.9, 5.9, 5.8, 5.8, 5.8, 5.8, 5.8),
                    SoilParameterType.SOIL_PH
                ),
                SoilParameterType.MOISTURE to generateSoilHistory(
                    listOf(49.0, 48.8, 48.5, 48.2, 48.0, 47.8, 47.6, 47.5, 47.8, 48.0, 48.0, 48.0),
                    SoilParameterType.MOISTURE
                ),
                SoilParameterType.ORGANIC_MATTER to generateSoilHistory(
                    listOf(8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5, 8.5),
                    SoilParameterType.ORGANIC_MATTER
                ),
                SoilParameterType.NITROGEN to generateSoilHistory(
                    listOf(19.0, 19.1, 19.2, 19.3, 19.5, 19.6, 19.5, 19.4, 19.5, 19.5, 19.5, 19.5),
                    SoilParameterType.NITROGEN
                ),
                SoilParameterType.LEAD_HEAVY_METAL to generateSoilHistory(
                    listOf(12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0, 12.0),
                    SoilParameterType.LEAD_HEAVY_METAL
                )
            )
        ),
        SoilMonitoringStation(
            id = "soil_station_brownfield",
            name = "Urban Brownfield Remediation Site",
            locationType = "Industrial Post-Mining Redevelopment Lot",
            distanceKm = 11.2,
            soilPh = 4.8, // EXCEEDANCE: High acidity
            moisturePercent = 14.2, // EXCEEDANCE: Arid depleted
            organicMatterPercent = 1.8, // EXCEEDANCE: Severely degraded
            availableNitrogen = 54.0, // EXCEEDANCE: Fertilizer/chemical saturation
            leadHeavyMetal = 265.0, // EXCEEDANCE: Exceeds EPA 200 mg/kg threshold!
            lastUpdated = "3 mins ago (XRF Heavy Metal Analyzer)",
            hourlyHistory = mapOf(
                SoilParameterType.SOIL_PH to generateSoilHistory(
                    listOf(4.7, 4.7, 4.8, 4.8, 4.8, 4.9, 4.9, 4.8, 4.8, 4.8, 4.8, 4.8),
                    SoilParameterType.SOIL_PH
                ),
                SoilParameterType.MOISTURE to generateSoilHistory(
                    listOf(15.2, 15.0, 14.8, 14.5, 14.2, 14.0, 13.8, 13.9, 14.0, 14.1, 14.2, 14.2),
                    SoilParameterType.MOISTURE
                ),
                SoilParameterType.ORGANIC_MATTER to generateSoilHistory(
                    listOf(1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8, 1.8),
                    SoilParameterType.ORGANIC_MATTER
                ),
                SoilParameterType.NITROGEN to generateSoilHistory(
                    listOf(52.0, 52.5, 53.0, 53.5, 54.0, 54.8, 54.5, 54.2, 54.0, 54.0, 54.0, 54.0),
                    SoilParameterType.NITROGEN
                ),
                SoilParameterType.LEAD_HEAVY_METAL to generateSoilHistory(
                    listOf(260.0, 261.0, 262.0, 263.0, 265.0, 266.0, 265.5, 265.0, 265.0, 265.0, 265.0, 265.0),
                    SoilParameterType.LEAD_HEAVY_METAL
                )
            )
        )
    )
}
