package com.example.calculator

object EnvironmentalUnitConverter {

    // --- Air Velocity & Dimensions ---
    const val FT_PER_METER = 3.28084
    const val METERS_PER_FT = 1.0 / FT_PER_METER

    const val GRAINS_PER_DSCF_PER_MG_M3 = 0.000436996
    const val MG_M3_PER_GRAINS_DSCF = 2288.35

    const val CFM_PER_M3_PER_SEC = 2118.88
    const val LBS_PER_KG = 2.20462262185

    // --- Water Flow & Volume ---
    const val GALLONS_PER_M3 = 264.172052
    const val M3_PER_GALLON = 1.0 / GALLONS_PER_M3

    const val M3_PER_MGD = 3785.411784
    const val MGD_PER_M3 = 1.0 / M3_PER_MGD

    const val CU_FT_PER_M3 = 35.3146667
    const val M3_PER_CU_FT = 1.0 / CU_FT_PER_M3

    // Conversions for Air
    fun velocityToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * METERS_PER_FT else value
    }

    fun velocityToDisplay(valueMps: Double, isImperial: Boolean): Double {
        return if (isImperial) valueMps * FT_PER_METER else valueMps
    }

    fun diameterToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * METERS_PER_FT else value
    }

    fun diameterToDisplay(valueM: Double, isImperial: Boolean): Double {
        return if (isImperial) valueM * FT_PER_METER else valueM
    }

    fun concentrationToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * MG_M3_PER_GRAINS_DSCF else value
    }

    fun concentrationToDisplay(valueMgM3: Double, isImperial: Boolean): Double {
        return if (isImperial) valueMgM3 * GRAINS_PER_DSCF_PER_MG_M3 else valueMgM3
    }

    // Conversions for Water
    fun waterFlowToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * M3_PER_MGD else value
    }

    fun waterFlowToDisplay(valueM3Day: Double, isImperial: Boolean): Double {
        return if (isImperial) valueM3Day * MGD_PER_M3 else valueM3Day
    }

    fun tankVolumeToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * M3_PER_CU_FT else value
    }

    fun tankVolumeToDisplay(valueM3: Double, isImperial: Boolean): Double {
        return if (isImperial) valueM3 * CU_FT_PER_M3 else valueM3
    }

    // Conversions for Noise
    fun distanceToMetric(value: Double, isImperial: Boolean): Double {
        return if (isImperial) value * METERS_PER_FT else value
    }

    fun distanceToDisplay(valueM: Double, isImperial: Boolean): Double {
        return if (isImperial) valueM * FT_PER_METER else valueM
    }
}
