package com.example.calculator

import java.util.Locale

/**
 * Unit systems and conversion utilities for Corporate GHG Inventory assessments.
 * Enables switching between Metric (kg, km, litres, tonnes) and Imperial (lbs, miles, gallons, short tons).
 */
enum class GhgUnitSystem(
    val displayName: String,
    val shortLabel: String,
    val weightUnit: String,
    val distanceUnit: String
) {
    METRIC("Metric (kg/km)", "Metric", "kg", "km"),
    IMPERIAL("Imperial (lbs/miles)", "Imperial", "lbs", "miles")
}

object GhgUnitConverter {
    // Physical conversion constants
    const val LITRES_PER_GALLON = 3.785411784
    const val CUFT_PER_M3 = 35.3146667
    const val LBS_PER_KG = 2.20462262
    const val KM_PER_MILE = 1.609344
    const val TONNE_KM_PER_TON_MILE = 1.459972
    const val SHORT_TONS_PER_METRIC_TONNE = 1.10231131
    const val LBS_PER_SHORT_TON = 2000.0

    /**
     * Retrieves the display unit for the given fuel type based on the active unit system.
     */
    fun getFuelUnit(fuelType: CarbonCalculatorEngine.FuelType, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> fuelType.unit // "litres", "m³", "kg"
            GhgUnitSystem.IMPERIAL -> when (fuelType) {
                CarbonCalculatorEngine.FuelType.DIESEL,
                CarbonCalculatorEngine.FuelType.PETROL,
                CarbonCalculatorEngine.FuelType.LPG -> "gallons"
                CarbonCalculatorEngine.FuelType.NATURAL_GAS -> "cu ft (ft³)"
                CarbonCalculatorEngine.FuelType.COAL -> "lbs"
            }
        }
    }

    /**
     * Converts a fuel quantity entered in the specified unit system into Metric units
     * required by the underlying GHG Protocol calculation engine.
     */
    fun convertFuelToMetric(
        quantity: Double,
        fuelType: CarbonCalculatorEngine.FuelType,
        fromSystem: GhgUnitSystem
    ): Double {
        if (fromSystem == GhgUnitSystem.METRIC) return quantity
        return when (fuelType) {
            CarbonCalculatorEngine.FuelType.DIESEL,
            CarbonCalculatorEngine.FuelType.PETROL,
            CarbonCalculatorEngine.FuelType.LPG -> quantity * LITRES_PER_GALLON
            CarbonCalculatorEngine.FuelType.NATURAL_GAS -> quantity / CUFT_PER_M3
            CarbonCalculatorEngine.FuelType.COAL -> quantity / LBS_PER_KG
        }
    }

    /**
     * Converts a fuel quantity in Metric units into the target unit system.
     */
    fun convertFuelFromMetric(
        quantity: Double,
        fuelType: CarbonCalculatorEngine.FuelType,
        toSystem: GhgUnitSystem
    ): Double {
        if (toSystem == GhgUnitSystem.METRIC) return quantity
        return when (fuelType) {
            CarbonCalculatorEngine.FuelType.DIESEL,
            CarbonCalculatorEngine.FuelType.PETROL,
            CarbonCalculatorEngine.FuelType.LPG -> quantity / LITRES_PER_GALLON
            CarbonCalculatorEngine.FuelType.NATURAL_GAS -> quantity * CUFT_PER_M3
            CarbonCalculatorEngine.FuelType.COAL -> quantity * LBS_PER_KG
        }
    }

    /**
     * Retrieves the transport/logistics activity unit.
     */
    fun getTransportUnit(mode: CarbonCalculatorEngine.TransportMode, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> mode.unit // "km", "passenger-km", "tonne-km"
            GhgUnitSystem.IMPERIAL -> when (mode) {
                CarbonCalculatorEngine.TransportMode.CAR_PETROL,
                CarbonCalculatorEngine.TransportMode.ELECTRIC_VEHICLE -> "miles"
                CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
                CarbonCalculatorEngine.TransportMode.FLIGHT_DOMESTIC,
                CarbonCalculatorEngine.TransportMode.FLIGHT_LONG_HAUL -> "passenger-miles"
                CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
                CarbonCalculatorEngine.TransportMode.MARITIME_FREIGHT -> "ton-miles"
            }
        }
    }

    /**
     * Converts transport/travel volume entered in the specified unit system into Metric units.
     */
    fun convertTransportToMetric(
        volume: Double,
        mode: CarbonCalculatorEngine.TransportMode,
        fromSystem: GhgUnitSystem
    ): Double {
        if (fromSystem == GhgUnitSystem.METRIC) return volume
        return when (mode) {
            CarbonCalculatorEngine.TransportMode.CAR_PETROL,
            CarbonCalculatorEngine.TransportMode.ELECTRIC_VEHICLE,
            CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
            CarbonCalculatorEngine.TransportMode.FLIGHT_DOMESTIC,
            CarbonCalculatorEngine.TransportMode.FLIGHT_LONG_HAUL -> volume * KM_PER_MILE
            CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            CarbonCalculatorEngine.TransportMode.MARITIME_FREIGHT -> volume * TONNE_KM_PER_TON_MILE
        }
    }

    /**
     * Converts transport/travel volume in Metric into the target unit system.
     */
    fun convertTransportFromMetric(
        volume: Double,
        mode: CarbonCalculatorEngine.TransportMode,
        toSystem: GhgUnitSystem
    ): Double {
        if (toSystem == GhgUnitSystem.METRIC) return volume
        return when (mode) {
            CarbonCalculatorEngine.TransportMode.CAR_PETROL,
            CarbonCalculatorEngine.TransportMode.ELECTRIC_VEHICLE,
            CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
            CarbonCalculatorEngine.TransportMode.FLIGHT_DOMESTIC,
            CarbonCalculatorEngine.TransportMode.FLIGHT_LONG_HAUL -> volume / KM_PER_MILE
            CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
            CarbonCalculatorEngine.TransportMode.MARITIME_FREIGHT -> volume / TONNE_KM_PER_TON_MILE
        }
    }

    /**
     * Format a fuel value for display with appropriate decimal places.
     */
    fun formatDisplayQuantity(value: Double): String {
        return if (value % 1.0 == 0.0) {
            value.toLong().toString()
        } else if (value < 10.0) {
            String.format(Locale.US, "%.2f", value)
        } else {
            String.format(Locale.US, "%.1f", value)
        }
    }

    /**
     * Helpful contextual unit conversion explanation hint for users.
     */
    fun getFuelConversionHint(fuelType: CarbonCalculatorEngine.FuelType, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> "Standard metric unit: ${fuelType.unit} (Factor: ${fuelType.factorKgCo2e} kg CO₂e/${fuelType.unit})"
            GhgUnitSystem.IMPERIAL -> when (fuelType) {
                CarbonCalculatorEngine.FuelType.DIESEL,
                CarbonCalculatorEngine.FuelType.PETROL,
                CarbonCalculatorEngine.FuelType.LPG -> "1 US gallon = 3.785 L • Factor: ~${String.format(Locale.US, "%.2f", fuelType.factorKgCo2e * LITRES_PER_GALLON)} kg CO₂e/gal"
                CarbonCalculatorEngine.FuelType.NATURAL_GAS -> "1 m³ = 35.31 cu ft • Factor: ~${String.format(Locale.US, "%.4f", fuelType.factorKgCo2e / CUFT_PER_M3)} kg CO₂e/cu ft"
                CarbonCalculatorEngine.FuelType.COAL -> "1 kg = 2.205 lbs • Factor: ~${String.format(Locale.US, "%.3f", fuelType.factorKgCo2e / LBS_PER_KG)} kg CO₂e/lb"
            }
        }
    }

    fun getTransportConversionHint(mode: CarbonCalculatorEngine.TransportMode, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> "Metric unit: ${mode.unit} (Factor: ${mode.factorKgCo2e} kg CO₂e/${mode.unit})"
            GhgUnitSystem.IMPERIAL -> when (mode) {
                CarbonCalculatorEngine.TransportMode.CAR_PETROL,
                CarbonCalculatorEngine.TransportMode.ELECTRIC_VEHICLE -> "1 mile = 1.609 km • Factor: ~${String.format(Locale.US, "%.3f", mode.factorKgCo2e * KM_PER_MILE)} kg CO₂e/mi"
                CarbonCalculatorEngine.TransportMode.TRAIN_COMMUTER,
                CarbonCalculatorEngine.TransportMode.FLIGHT_DOMESTIC,
                CarbonCalculatorEngine.TransportMode.FLIGHT_LONG_HAUL -> "1 passenger-mile = 1.609 p-km • Factor: ~${String.format(Locale.US, "%.3f", mode.factorKgCo2e * KM_PER_MILE)} kg CO₂e/p-mi"
                CarbonCalculatorEngine.TransportMode.ROAD_FREIGHT,
                CarbonCalculatorEngine.TransportMode.MARITIME_FREIGHT -> "1 short ton-mile = 1.460 t-km • Factor: ~${String.format(Locale.US, "%.3f", mode.factorKgCo2e * TONNE_KM_PER_TON_MILE)} kg CO₂e/ton-mi"
            }
        }
    }

    /**
     * Formats emission kg for display, highlighting primary unit with secondary conversion.
     */
    fun formatEmissionKg(kg: Double, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> String.format(Locale.US, "%,.1f kg CO₂e", kg)
            GhgUnitSystem.IMPERIAL -> {
                val lbs = kg * LBS_PER_KG
                String.format(Locale.US, "%,.1f lbs CO₂e (%,.1f kg)", lbs, kg)
            }
        }
    }

    /**
     * Formats macro emission tonnes for display.
     */
    fun formatMacroEmissionTonnes(tonnes: Double, unitSystem: GhgUnitSystem): String {
        return when (unitSystem) {
            GhgUnitSystem.METRIC -> String.format(Locale.US, "%.2f t CO₂e", tonnes)
            GhgUnitSystem.IMPERIAL -> {
                val usShortTons = tonnes * SHORT_TONS_PER_METRIC_TONNE
                val lbs = tonnes * 1000.0 * LBS_PER_KG
                if (usShortTons >= 1.0) {
                    String.format(Locale.US, "%.2f US tons CO₂e (%.2f t)", usShortTons, tonnes)
                } else {
                    String.format(Locale.US, "%,.0f lbs CO₂e (%.2f t)", lbs, tonnes)
                }
            }
        }
    }
}
