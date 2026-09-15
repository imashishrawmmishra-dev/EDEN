package com.example.data.repository

import com.example.data.model.AirPollutantType
import com.example.data.model.AirQualityHourlyPoint
import com.example.data.model.AirQualityStation

object AirQualityRepository {

    private val hourLabels = listOf(
        "00:00", "02:00", "04:00", "06:00", "08:00", "10:00",
        "12:00", "14:00", "16:00", "18:00", "20:00", "Now"
    )

    private fun generateHistory(values: List<Double>, pollutant: AirPollutantType): List<AirQualityHourlyPoint> {
        return values.mapIndexed { index, value ->
            AirQualityHourlyPoint(
                hourLabel = hourLabels.getOrElse(index) { "${index * 2}:00" },
                value = value,
                status = pollutant.evaluateStatus(value)
            )
        }
    }

    val defaultStations: List<AirQualityStation> = listOf(
        AirQualityStation(
            id = "station_downtown",
            name = "Urban Canopy Station Alpha",
            locationType = "Downtown Ambient Air Quality Zone",
            distanceKm = 1.2,
            pm25 = 11.4,
            pm10 = 28.2,
            no2 = 18.5,
            o3 = 52.0,
            lastUpdated = "Just now (Live Optical Sensor)",
            hourlyHistory = mapOf(
                AirPollutantType.PM25 to generateHistory(
                    listOf(7.8, 8.5, 9.2, 13.8, 15.2, 14.1, 12.0, 11.2, 13.6, 14.0, 12.5, 11.4),
                    AirPollutantType.PM25
                ),
                AirPollutantType.PM10 to generateHistory(
                    listOf(20.4, 22.1, 25.0, 34.5, 38.0, 32.4, 29.1, 27.5, 31.0, 33.2, 30.1, 28.2),
                    AirPollutantType.PM10
                ),
                AirPollutantType.NO2 to generateHistory(
                    listOf(12.5, 13.1, 14.0, 22.4, 24.8, 21.0, 17.5, 16.8, 22.1, 23.5, 20.2, 18.5),
                    AirPollutantType.NO2
                ),
                AirPollutantType.O3 to generateHistory(
                    listOf(32.0, 30.5, 28.0, 35.4, 48.0, 62.5, 68.0, 71.2, 64.0, 55.1, 48.0, 52.0),
                    AirPollutantType.O3
                )
            )
        ),
        AirQualityStation(
            id = "station_green_belt",
            name = "Eco-Corridor Botanical Reserve",
            locationType = "Biosphere Sanctuary Protected Node",
            distanceKm = 4.8,
            pm25 = 6.2,
            pm10 = 16.5,
            no2 = 9.4,
            o3 = 41.0,
            lastUpdated = "4 mins ago (Continuous Telemetry)",
            hourlyHistory = mapOf(
                AirPollutantType.PM25 to generateHistory(
                    listOf(4.5, 5.0, 5.2, 6.8, 7.5, 7.1, 6.0, 5.8, 6.5, 7.0, 6.4, 6.2),
                    AirPollutantType.PM25
                ),
                AirPollutantType.PM10 to generateHistory(
                    listOf(12.0, 13.2, 14.0, 18.1, 19.5, 18.0, 15.5, 15.0, 17.2, 18.0, 16.8, 16.5),
                    AirPollutantType.PM10
                ),
                AirPollutantType.NO2 to generateHistory(
                    listOf(7.1, 7.4, 8.0, 11.2, 12.0, 10.5, 9.0, 8.7, 10.2, 11.0, 9.8, 9.4),
                    AirPollutantType.NO2
                ),
                AirPollutantType.O3 to generateHistory(
                    listOf(28.0, 26.5, 25.0, 31.0, 39.5, 48.0, 52.0, 54.5, 49.0, 43.0, 38.5, 41.0),
                    AirPollutantType.O3
                )
            )
        ),
        AirQualityStation(
            id = "station_industrial_corridor",
            name = "Harbor Logistics & Energy Belt",
            locationType = "Industrial Maritime Air Monitor",
            distanceKm = 8.6,
            pm25 = 23.4,
            pm10 = 54.8,
            no2 = 32.6,
            o3 = 78.5,
            lastUpdated = "1 min ago (Laser Extinction + FTIR)",
            hourlyHistory = mapOf(
                AirPollutantType.PM25 to generateHistory(
                    listOf(16.0, 17.5, 19.2, 28.4, 31.5, 29.0, 24.2, 22.8, 27.5, 28.0, 25.1, 23.4),
                    AirPollutantType.PM25
                ),
                AirPollutantType.PM10 to generateHistory(
                    listOf(38.0, 42.1, 46.5, 66.0, 72.5, 64.0, 56.2, 53.0, 62.0, 65.5, 58.0, 54.8),
                    AirPollutantType.PM10
                ),
                AirPollutantType.NO2 to generateHistory(
                    listOf(22.0, 24.5, 26.0, 41.2, 45.0, 39.5, 33.0, 31.5, 38.2, 40.0, 35.5, 32.6),
                    AirPollutantType.NO2
                ),
                AirPollutantType.O3 to generateHistory(
                    listOf(45.0, 42.0, 40.0, 52.0, 70.0, 89.0, 96.5, 102.0, 92.0, 81.0, 72.0, 78.5),
                    AirPollutantType.O3
                )
            )
        )
    )
}
