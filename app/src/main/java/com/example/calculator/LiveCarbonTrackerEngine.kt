package com.example.calculator

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object LiveCarbonTrackerEngine {

    /**
     * Transportation modes and verified emission factors (DEFRA 2023 / EPA GHG Hub).
     * Units: kg CO2e per passenger-kilometer (kg CO2e / km)
     */
    enum class LiveTransitMode(
        val displayName: String,
        val factorKgPerKm: Double,
        val speedRangeDescription: String,
        val iconType: String
    ) {
        WALKING("Walking / Running", 0.0, "0 - 7 km/h (Zero Direct Emissions)", "DirectionsWalk"),
        BICYCLE("Cycling / E-Bike", 0.005, "7 - 25 km/h (Minimal Lifecycle)", "PedalBike"),
        METRO_TRAIN("Metro / Electric Rail", 0.028, "20 - 120 km/h (High Efficiency Public Transit)", "Train"),
        BUS_TRANSIT("Public Bus / Coach", 0.089, "15 - 60 km/h (Shared Municipal Transit)", "DirectionsBus"),
        ELECTRIC_VEHICLE("Electric Vehicle (EV)", 0.053, "0 - 140 km/h (Grid Average US/EU)", "ElectricCar"),
        GASOLINE_CAR("Standard Gasoline Car", 0.171, "0 - 140 km/h (Average Internal Combustion)", "DirectionsCar"),
        DIESEL_SUV("SUV / Diesel Fleet", 0.224, "0 - 140 km/h (Heavy Diesel Light Duty)", "LocalShipping"),
        DOMESTIC_FLIGHT("Aviation / Flight", 0.246, "> 250 km/h (High Altitude Radiative Forcing)", "Flight")
    }

    /**
     * Regional grid carbon intensity factors for charging / power at this location.
     * Sources: EPA eGRID, European Environment Agency (EEA), Central Electricity Authority (CEA).
     */
    data class RegionalGridFactor(
        val regionName: String,
        val kgCo2PerKwh: Double,
        val cleanEnergyPercent: Int,
        val primarySource: String
    )

    fun determineRegionalGrid(lat: Double, lon: Double): RegionalGridFactor {
        // Spatial heuristic based on bounding coordinates
        return when {
            // India / South Asia (Lat 6 to 37, Lon 68 to 97)
            lat in 6.0..37.0 && lon in 68.0..97.0 -> RegionalGridFactor(
                regionName = "India / South Asia (CEA Grid)",
                kgCo2PerKwh = 0.716,
                cleanEnergyPercent = 23,
                primarySource = "Coal & Hydro blend (CEA 2023)"
            )
            // Europe (Lat 35 to 70, Lon -10 to 35)
            lat in 35.0..70.0 && lon in -10.0..35.0 -> RegionalGridFactor(
                regionName = "European Union (EEA Average)",
                kgCo2PerKwh = 0.231,
                cleanEnergyPercent = 64,
                primarySource = "Renewables, Nuclear & Gas (EEA 2023)"
            )
            // United States / North America (Lat 24 to 50, Lon -125 to -66)
            lat in 24.0..50.0 && lon in -125.0..-66.0 -> RegionalGridFactor(
                regionName = "North America (EPA eGRID Avg)",
                kgCo2PerKwh = 0.386,
                cleanEnergyPercent = 41,
                primarySource = "Natural Gas, Renewables & Nuclear"
            )
            // United Kingdom (Lat 49 to 60, Lon -8 to 2)
            lat in 49.0..60.0 && lon in -8.0..2.0 -> RegionalGridFactor(
                regionName = "United Kingdom (National Grid ESO)",
                kgCo2PerKwh = 0.162,
                cleanEnergyPercent = 58,
                primarySource = "Offshore Wind & Nuclear"
            )
            // Australia (Lat -45 to -10, Lon 112 to 154)
            lat in -45.0..-10.0 && lon in 112.0..154.0 -> RegionalGridFactor(
                regionName = "Australia (National Electricity Market)",
                kgCo2PerKwh = 0.590,
                cleanEnergyPercent = 35,
                primarySource = "Coal, Gas & Rooftop Solar"
            )
            // Default Global Average (IEA 2023)
            else -> RegionalGridFactor(
                regionName = "Global Grid Average (IEA Benchmark)",
                kgCo2PerKwh = 0.436,
                cleanEnergyPercent = 38,
                primarySource = "Global Mixed Fuel Matrix"
            )
        }
    }

    /**
     * Compute Haversine Great-Circle distance in kilometers between two GPS points
     */
    fun haversineDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in km
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2) * sin(dLat / 2) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return r * c
    }

    /**
     * Automatically suggest transit mode based on live speed in km/h
     */
    fun detectModeFromSpeed(speedKmh: Double): LiveTransitMode {
        return when {
            speedKmh < 1.0 -> LiveTransitMode.WALKING
            speedKmh <= 7.0 -> LiveTransitMode.WALKING
            speedKmh <= 25.0 -> LiveTransitMode.BICYCLE
            speedKmh <= 70.0 -> LiveTransitMode.GASOLINE_CAR
            speedKmh <= 160.0 -> LiveTransitMode.GASOLINE_CAR
            else -> LiveTransitMode.DOMESTIC_FLIGHT
        }
    }

    /**
     * Calculate carbon emitted for a distance and selected mode
     */
    fun calculateLiveTripEmission(distanceKm: Double, mode: LiveTransitMode): LiveTripCarbonSummary {
        val directEmissionsKg = distanceKm * mode.factorKgPerKm
        val baselineCarEmissionsKg = distanceKm * LiveTransitMode.GASOLINE_CAR.factorKgPerKm
        val netSavingsVsCarKg = (baselineCarEmissionsKg - directEmissionsKg).coerceAtLeast(0.0)
        val treesRequiredToOffset = directEmissionsKg / 21.77 // ~21.77 kg CO2 absorbed per mature urban tree/year

        return LiveTripCarbonSummary(
            distanceKm = distanceKm,
            mode = mode,
            carbonEmittedKg = directEmissionsKg,
            baselineCarKg = baselineCarEmissionsKg,
            carbonSavedVsCarKg = netSavingsVsCarKg,
            treesYearEquivalent = treesRequiredToOffset
        )
    }

    data class LiveTripCarbonSummary(
        val distanceKm: Double,
        val mode: LiveTransitMode,
        val carbonEmittedKg: Double,
        val baselineCarKg: Double,
        val carbonSavedVsCarKg: Double,
        val treesYearEquivalent: Double
    )
}
