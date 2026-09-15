package com.example.monitoring.engine

import com.example.monitoring.data.PollutionMonitoringRepository
import com.example.monitoring.model.ComplianceStatus
import com.example.monitoring.model.LiveCalculationResult
import com.example.monitoring.model.MonitoringDuration
import com.example.monitoring.model.PollutionMonitoringDomain
import com.example.monitoring.model.RegulatoryStandard
import java.util.Locale
import kotlin.math.ln
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

object PollutionCalculationEngine {

    /**
     * Compute live calculations and compare against the user's selected standard & duration
     */
    fun calculate(
        domain: PollutionMonitoringDomain,
        standard: RegulatoryStandard,
        duration: MonitoringDuration,
        inputs: Map<String, String>
    ): LiveCalculationResult {
        val benchmark = PollutionMonitoringRepository.getStandardBenchmark(domain, standard, duration)

        return when (domain) {
            PollutionMonitoringDomain.AMBIENT_AIR -> calculateAmbientAir(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.INDOOR_AIR -> calculateIndoorAir(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.WORKZONE_AIR -> calculateWorkzoneAir(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.NOISE -> calculateNoise(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.STACK_EMISSION -> calculateStackEmission(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.FLUE_GAS -> calculateFlueGas(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.PAINT_BOOTH -> calculatePaintBooth(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.AIR_MICROBIOLOGY -> calculateAirMicrobiology(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.CLEAN_ROOM -> calculateCleanroom(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.LUX_MONITORING -> calculateLux(benchmark.limitValue, inputs, standard, duration)
            PollutionMonitoringDomain.WATER_MONITORING -> calculateWater(benchmark.limitValue, inputs, standard, duration)
        }
    }

    private fun calculateAmbientAir(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val q1 = inputs["initial_flow"]?.toDoubleOrNull() ?: 16.67
        val q2 = inputs["final_flow"]?.toDoubleOrNull() ?: 16.65
        val timeMin = inputs["duration_minutes"]?.toDoubleOrNull() ?: (duration.durationHours * 60.0)
        val w1 = inputs["initial_filter_wt"]?.toDoubleOrNull() ?: 145.230
        val w2 = inputs["final_filter_wt"]?.toDoubleOrNull() ?: 145.885
        val ta = inputs["ambient_temp"]?.toDoubleOrNull() ?: 26.5
        val pa = inputs["ambient_pressure"]?.toDoubleOrNull() ?: 758.0

        val qAvg = (q1 + q2) / 2.0
        val vAct = (qAvg * timeMin) / 1000.0 // m³ actual
        val vStd = vAct * (pa / 760.0) * (298.15 / (273.15 + ta)) // m³ at NTP (25°C, 1 atm)
        val deltaWmg = max(0.0, w2 - w1) // mg
        val concentrationUgM3 = if (vStd > 0.0) (deltaWmg * 1000.0) / vStd else 0.0

        val deltaPercent = if (limit > 0.0) ((concentrationUgM3 - limit) / limit) * 100.0 else 0.0
        val status = evaluateStatusHigherIsWorse(concentrationUgM3, limit)

        val steps = listOf(
            "Average Flow Rate (Q_avg)" to String.format(Locale.US, "(%.2f + %.2f) / 2 = %.2f L/min", q1, q2, qAvg),
            "Actual Volume Sampled (V_a)" to String.format(Locale.US, "(%.2f L/min × %.0f min) / 1000 = %.3f m³", qAvg, timeMin, vAct),
            "Standard Volume (V_std @ NTP)" to String.format(Locale.US, "%.3f × (%.1f / 760) × (298.15 / %.1f) = %.3f Nm³", vAct, pa, 273.15 + ta, vStd),
            "Filter Net Mass Catch (ΔW)" to String.format(Locale.US, "%.3f mg - %.3f mg = %.3f mg (%.1f µg)", w2, w1, deltaWmg, deltaWmg * 1000.0),
            "Particulate Concentration (C)" to String.format(Locale.US, "(%.3f mg × 1000) / %.3f Nm³ = %.1f µg/m³", deltaWmg, vStd, concentrationUgM3)
        )

        return LiveCalculationResult(
            mainCalculatedValue = concentrationUgM3,
            mainDisplayUnit = "µg/m³",
            formattedMainResult = String.format(Locale.US, "%.1f µg/m³", concentrationUgM3),
            parameterEvaluated = "PM2.5 Ambient Concentration",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(concentrationUgM3, limit, deltaPercent, "PM2.5 concentration", "µg/m³", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} ${duration.title} limit (${String.format(Locale.US, "%.0f", limit)} µg/m³)"
        )
    }

    private fun calculateIndoorAir(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val co2 = inputs["co2_measured"]?.toDoubleOrNull() ?: 820.0
        val ambCo2 = inputs["ambient_co2"]?.toDoubleOrNull() ?: 415.0
        val tvoc = inputs["tvoc_measured"]?.toDoubleOrNull() ?: 245.0
        val hcho = inputs["hcho_measured"]?.toDoubleOrNull() ?: 0.035
        val temp = inputs["indoor_temp"]?.toDoubleOrNull() ?: 23.2
        val rh = inputs["indoor_rh"]?.toDoubleOrNull() ?: 48.5
        val volume = inputs["room_volume"]?.toDoubleOrNull() ?: 350.0

        val deltaCo2 = max(0.0, co2 - ambCo2)
        // Estimated outdoor air ventilation rate per occupant (L/s/person) via Persily equation
        val estVentilationRate = if (deltaCo2 > 0) (0.005 / (deltaCo2 / 1_000_000.0)) * 1000.0 else 25.0
        val estAch = (estVentilationRate * 3.6 * 10) / volume

        val deltaPercent = ((co2 - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(co2, limit)

        val steps = listOf(
            "Indoor - Outdoor Gradient (ΔCO₂)" to String.format(Locale.US, "%.0f ppm - %.0f ppm = %.0f ppm", co2, ambCo2, deltaCo2),
            "Est. Outdoor Air Supply Rate" to String.format(Locale.US, "%.1f L/s per occupant (ASHRAE min: 10 L/s)", estVentilationRate),
            "Estimated Air Changes / Hour (ACH)" to String.format(Locale.US, "%.2f ACH across %.0f m³ room envelope", estAch, volume),
            "TVOC & HCHO Status" to String.format(Locale.US, "TVOC: %.0f µg/m³ (Limit: 500), HCHO: %.3f ppm (Limit: 0.08)", tvoc, hcho),
            "Indoor Comfort Index" to String.format(Locale.US, "Temp: %.1f °C, RH: %.1f%% (ASHRAE 55 thermal comfort)", temp, rh)
        )

        return LiveCalculationResult(
            mainCalculatedValue = co2,
            mainDisplayUnit = "ppm",
            formattedMainResult = String.format(Locale.US, "%.0f ppm CO₂", co2),
            parameterEvaluated = "Indoor CO₂ Ventilation Level",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(co2, limit, deltaPercent, "Indoor CO₂ concentration", "ppm", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} (${String.format(Locale.US, "%.0f", limit)} ppm)"
        )
    }

    private fun calculateWorkzoneAir(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val flow = inputs["pump_flow"]?.toDoubleOrNull() ?: 2.0
        val shiftHours = inputs["shift_hours"]?.toDoubleOrNull() ?: 8.0
        val deltaW = inputs["cyclone_filter_gain"]?.toDoubleOrNull() ?: 1.650
        val target = inputs["target_contaminant"] ?: "Respirable Silica / Dust"

        val volumeM3 = (flow * shiftHours * 60.0) / 1000.0
        val actualConc = if (volumeM3 > 0) deltaW / volumeM3 else 0.0
        val twa8hr = actualConc * (shiftHours / 8.0)

        val deltaPercent = ((twa8hr - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(twa8hr, limit)

        val steps = listOf(
            "Air Volume Sampled (V)" to String.format(Locale.US, "(%.2f L/min × %.1f h × 60 min) / 1000 = %.3f m³", flow, shiftHours, volumeM3),
            "Cassette Filter Dust Mass (ΔW)" to String.format(Locale.US, "%.3f mg respirable fraction", deltaW),
            "Measured Concentration (C)" to String.format(Locale.US, "%.3f mg / %.3f m³ = %.2f mg/m³", deltaW, volumeM3, actualConc),
            "8-Hour Equivalent TWA" to String.format(Locale.US, "%.2f mg/m³ × (%.1f h / 8.0 h) = %.2f mg/m³", actualConc, shiftHours, twa8hr),
            "Target Hazard Monitored" to target
        )

        return LiveCalculationResult(
            mainCalculatedValue = twa8hr,
            mainDisplayUnit = "mg/m³",
            formattedMainResult = String.format(Locale.US, "%.2f mg/m³ TWA", twa8hr),
            parameterEvaluated = "Occupational 8-hr Shift TWA",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(twa8hr, limit, deltaPercent, "Respirable dust TWA", "mg/m³", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} PEL/WES (${String.format(Locale.US, "%.1f", limit)} mg/m³)"
        )
    }

    private fun calculateNoise(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val lDay = inputs["leq_day"]?.toDoubleOrNull() ?: 68.4
        val lNight = inputs["leq_night"]?.toDoubleOrNull() ?: 56.2
        val lMax = inputs["l_max"]?.toDoubleOrNull() ?: 79.8
        val lMin = inputs["l_min"]?.toDoubleOrNull() ?: 47.1
        val l10 = inputs["l_10"]?.toDoubleOrNull() ?: 71.5
        val l90 = inputs["l_90"]?.toDoubleOrNull() ?: 51.0

        // Day-Night level with 10 dB night penalty: Ldn = 10*log10( (15/24)*10^(Lday/10) + (9/24)*10^((Lnight+10)/10) )
        val dayTerm = (15.0 / 24.0) * 10.0.pow(lDay / 10.0)
        val nightTerm = (9.0 / 24.0) * 10.0.pow((lNight + 10.0) / 10.0)
        val ldn = 10.0 * log10(dayTerm + nightTerm)

        val deltaPercent = ((lDay - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(lDay, limit)

        val steps = listOf(
            "Daytime Energy Equivalent (L_day)" to String.format(Locale.US, "%.1f dBA (06:00 to 22:00)", lDay),
            "Nighttime Energy Equivalent (L_night)" to String.format(Locale.US, "%.1f dBA (22:00 to 06:00)", lNight),
            "Day-Night Sound Level (L_dn)" to String.format(Locale.US, "10 × log₁₀[15/24·10^%.1f + 9/24·10^%.1f] = %.1f dBA", lDay / 10.0, (lNight + 10.0) / 10.0, ldn),
            "Statistical Levels" to String.format(Locale.US, "L₁₀ (Peak): %.1f dBA | L₉₀ (Background): %.1f dBA", l10, l90),
            "Dynamic Range" to String.format(Locale.US, "L_max: %.1f dBA | L_min: %.1f dBA (Span: %.1f dB)", lMax, lMin, lMax - lMin)
        )

        return LiveCalculationResult(
            mainCalculatedValue = lDay,
            mainDisplayUnit = "dBA",
            formattedMainResult = String.format(Locale.US, "%.1f dBA Leq", lDay),
            parameterEvaluated = "Equivalent Sound Pressure (Leq)",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(lDay, limit, deltaPercent, "A-weighted sound pressure Leq", "dBA", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Noise Limits (${String.format(Locale.US, "%.0f", limit)} dBA)"
        )
    }

    private fun calculateStackEmission(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val deltaP = inputs["velocity_head"]?.toDoubleOrNull() ?: 16.8
        val cp = inputs["pitot_cp"]?.toDoubleOrNull() ?: 0.84
        val ts = inputs["stack_temp"]?.toDoubleOrNull() ?: 165.0
        val diam = inputs["stack_diameter"]?.toDoubleOrNull() ?: 2.10
        val vm = inputs["gas_meter_volume"]?.toDoubleOrNull() ?: 1.450
        val tm = inputs["gas_meter_temp"]?.toDoubleOrNull() ?: 32.0
        val w1 = inputs["filter_tare_wt"]?.toDoubleOrNull() ?: 482.10
        val w2 = inputs["filter_gross_wt"]?.toDoubleOrNull() ?: 554.80

        val area = Math.PI * (diam / 2.0).pow(2.0)
        // Stack velocity vs = 4.07 * Cp * sqrt(deltaP * (Ts + 273.15) / Ms)
        val stackVelocity = 4.07 * cp * sqrt((deltaP * (ts + 273.15)) / 29.0)
        val qActual = stackVelocity * area * 3600.0 // m³/h
        val qStd = qActual * (298.15 / (ts + 273.15)) * (755.0 / 760.0) // Nm³/h

        val vmStd = vm * (298.15 / (tm + 273.15)) * (755.0 / 760.0)
        val deltaW = max(0.0, w2 - w1)
        val concMgNm3 = if (vmStd > 0) deltaW / vmStd else 0.0
        val massEmissionRateKgH = (concMgNm3 * qStd) / 1_000_000.0

        val deltaPercent = ((concMgNm3 - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(concMgNm3, limit)

        val steps = listOf(
            "Internal Stack Duct Area (A_s)" to String.format(Locale.US, "π × (%.2f m / 2)² = %.2f m²", diam, area),
            "Stack Flue Gas Velocity (v_s)" to String.format(Locale.US, "4.07 × %.2f × √(%.1f × %.1f / 29.0) = %.2f m/s", cp, deltaP, ts + 273.15, stackVelocity),
            "Standard Flue Gas Flow (Q_std)" to String.format(Locale.US, "%.0f m³/h actual -> %.0f Nm³/h (dry NTP)", qActual, qStd),
            "Sampled Standard DGM Volume" to String.format(Locale.US, "%.3f m³ actual -> %.3f Nm³", vm, vmStd),
            "Particulate Catch & Concentration" to String.format(Locale.US, "%.1f mg / %.3f Nm³ = %.1f mg/Nm³", deltaW, vmStd, concMgNm3),
            "Mass Emission Rate (E)" to String.format(Locale.US, "%.1f mg/Nm³ × %.0f Nm³/h / 10⁶ = %.2f kg/h", concMgNm3, qStd, massEmissionRateKgH)
        )

        return LiveCalculationResult(
            mainCalculatedValue = concMgNm3,
            mainDisplayUnit = "mg/Nm³",
            formattedMainResult = String.format(Locale.US, "%.1f mg/Nm³", concMgNm3),
            parameterEvaluated = "Stack Particulate Concentration",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(concMgNm3, limit, deltaPercent, "Stack particulate emission", "mg/Nm³", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Stationary Source Limit (${String.format(Locale.US, "%.0f", limit)} mg/Nm³)"
        )
    }

    private fun calculateFlueGas(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val o2 = inputs["o2_pct"]?.toDoubleOrNull() ?: 4.8
        val co = inputs["co_ppm"]?.toDoubleOrNull() ?: 42.0
        val co2 = inputs["co2_pct"]?.toDoubleOrNull() ?: 12.4
        val so2 = inputs["so2_ppm"]?.toDoubleOrNull() ?: 125.0
        val nox = inputs["nox_ppm"]?.toDoubleOrNull() ?: 168.0
        val tFlue = inputs["flue_temp"]?.toDoubleOrNull() ?: 172.0
        val tAmb = inputs["ambient_air_temp"]?.toDoubleOrNull() ?: 28.0

        val excessAir = if (20.9 - o2 > 0.1) 20.9 / (20.9 - o2) else 1.0
        // Standard Reference O2 is typically 6.0% for coal/biomass or 3.0% for natural gas
        val refO2 = 6.0
        val coNormalized = if (20.9 - o2 > 0.1) co * ((20.9 - refO2) / (20.9 - o2)) else co

        // Siegert formula: Loss = 0.65 * (Tflue - Tamb) / CO2%
        val siegertLoss = if (co2 > 0.5) (0.65 * (tFlue - tAmb)) / co2 else 10.0
        val efficiency = max(0.0, 100.0 - siegertLoss)

        val deltaPercent = ((coNormalized - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(coNormalized, limit)

        val steps = listOf(
            "Excess Air Factor (λ)" to String.format(Locale.US, "20.9 / (20.9 - %.1f%% O₂) = %.2f (%.1f%% Excess Air)", o2, excessAir, (excessAir - 1.0) * 100.0),
            "CO Normalized to 6% Ref O₂" to String.format(Locale.US, "%.1f ppm × (20.9 - 6.0)/(20.9 - %.1f) = %.1f ppm", co, o2, coNormalized),
            "SO₂ & NOx Normalized (6% O₂)" to String.format(Locale.US, "SO₂: %.1f ppm | NOx: %.1f ppm", so2 * ((20.9 - 6.0) / (20.9 - o2)), nox * ((20.9 - 6.0) / (20.9 - o2))),
            "Combustion Efficiency (η)" to String.format(Locale.US, "100 - [0.65 × (%.0f - %.0f) / %.1f] = %.1f%%", tFlue, tAmb, co2, efficiency)
        )

        return LiveCalculationResult(
            mainCalculatedValue = coNormalized,
            mainDisplayUnit = "ppm @ 6% O₂",
            formattedMainResult = String.format(Locale.US, "%.1f ppm CO", coNormalized),
            parameterEvaluated = "Flue Gas Normalized CO",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(coNormalized, limit, deltaPercent, "Flue gas CO (normalized)", "ppm", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Flue Gas Norm (${String.format(Locale.US, "%.0f", limit)} ppm)"
        )
    }

    private fun calculatePaintBooth(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val velocity = inputs["face_velocity"]?.toDoubleOrNull() ?: 0.52
        val area = inputs["booth_area"]?.toDoubleOrNull() ?: 14.5
        val vocPpm = inputs["voc_concentration"]?.toDoubleOrNull() ?: 18.5
        val deltaP = inputs["filter_delta_p"]?.toDoubleOrNull() ?: 145.0
        val overspray = inputs["overspray_rate"]?.toDoubleOrNull() ?: 12.5
        val solventPct = inputs["solids_pct"]?.toDoubleOrNull() ?: 42.0

        val exhaustAirflow = velocity * area * 3600.0 // m³/h
        val vocMassRate = overspray * (solventPct / 100.0) // kg/h
        // Assuming Xylene LEL = 10,000 ppm
        val pctLel = (vocPpm / 10000.0) * 100.0

        // For capture velocity, lower than limit is BAD
        val deltaPercent = ((velocity - limit) / limit) * 100.0
        val status = if (velocity >= limit) ComplianceStatus.COMPLIANT else if (velocity >= limit * 0.9) ComplianceStatus.WARNING else ComplianceStatus.EXCEEDED

        val steps = listOf(
            "Exhaust Ventilation Airflow (Q)" to String.format(Locale.US, "%.2f m/s × %.1f m² × 3600 = %.0f m³/h", velocity, area, exhaustAirflow),
            "VOC Volatile Emission Rate" to String.format(Locale.US, "%.1f kg/h paint × %.0f%% solvent = %.2f kg/h VOC", overspray, solventPct, vocMassRate),
            "Explosion Safety Index (% LEL)" to String.format(Locale.US, "%.1f ppm / 10,000 ppm = %.2f%% LEL (NFPA 33 limit: < 25%%)", vocPpm, pctLel),
            "Filter Bank Differential Pressure" to String.format(Locale.US, "%.0f Pa (Replace filters at > 250 Pa)", deltaP)
        )

        return LiveCalculationResult(
            mainCalculatedValue = velocity,
            mainDisplayUnit = "m/s",
            formattedMainResult = String.format(Locale.US, "%.2f m/s", velocity),
            parameterEvaluated = "Paint Booth Face Capture Velocity",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = if (velocity >= limit) {
                "COMPLIANT: Measured face velocity of ${String.format(Locale.US, "%.2f", velocity)} m/s exceeds minimum ${standard.displayName} requirement of ${String.format(Locale.US, "%.2f", limit)} m/s."
            } else {
                "EXCEEDED / INSUFFICIENT: Measured face velocity ${String.format(Locale.US, "%.2f", velocity)} m/s is below mandated ${standard.displayName} minimum of ${String.format(Locale.US, "%.2f", limit)} m/s."
            },
            legalCitation = "Evaluated against ${standard.displayName} Paint Booth Ventilation Standard (${String.format(Locale.US, "%.2f", limit)} m/s minimum)"
        )
    }

    private fun calculateAirMicrobiology(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val flow = inputs["sampler_flow"]?.toDoubleOrNull() ?: 28.3
        val timeMin = inputs["sampling_time"]?.toDoubleOrNull() ?: 15.0
        val r = (inputs["raw_colony_count"]?.toDoubleOrNull() ?: 34.0).roundToInt()
        val n = (inputs["impactor_holes"]?.toDoubleOrNull() ?: 400.0).roundToInt()

        // Feller Positive-Hole Statistical Correction
        // Pr = N * [ 1/N + 1/(N-1) + ... + 1/(N-r+1) ] or approximation: Pr = N * ln(N / (N - r))
        val pr = if (r in 1 until n) {
            n * ln(n.toDouble() / (n - r).toDouble())
        } else if (r >= n) {
            n * 2.5
        } else {
            0.0
        }

        val volumeM3 = (flow * timeMin) / 1000.0
        val cfuPerM3 = if (volumeM3 > 0) pr / volumeM3 else 0.0

        val deltaPercent = ((cfuPerM3 - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(cfuPerM3, limit)

        val steps = listOf(
            "Sampled Air Volume (V)" to String.format(Locale.US, "(%.1f L/min × %.1f min) / 1000 = %.3f m³", flow, timeMin, volumeM3),
            "Observed Colony Count (r)" to "$r CFU on agar plate",
            "Feller Statistical Hole Correction (Pr)" to String.format(Locale.US, "%d holes × ln(%d / (%d - %d)) = %.1f corrected CFU", n, n, n, r, pr),
            "Airborne Viable Bioburden" to String.format(Locale.US, "%.1f CFU / %.3f m³ = %.1f CFU/m³", pr, volumeM3, cfuPerM3)
        )

        return LiveCalculationResult(
            mainCalculatedValue = cfuPerM3,
            mainDisplayUnit = "CFU/m³",
            formattedMainResult = String.format(Locale.US, "%.1f CFU/m³", cfuPerM3),
            parameterEvaluated = "Airborne Viable Bioburden",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(cfuPerM3, limit, deltaPercent, "Viable bioburden", "CFU/m³", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Bio-contamination Standard (${String.format(Locale.US, "%.0f", limit)} CFU/m³)"
        )
    }

    private fun calculateCleanroom(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val p05 = inputs["particles_05"]?.toDoubleOrNull() ?: 1240.0
        val p10 = inputs["particles_10"]?.toDoubleOrNull() ?: 320.0
        val p50 = inputs["particles_50"]?.toDoubleOrNull() ?: 18.0
        val sampleVolL = inputs["sample_volume_liters"]?.toDoubleOrNull() ?: 1000.0
        val area = inputs["room_floor_area"]?.toDoubleOrNull() ?: 64.0

        val conc05PerM3 = p05 * (1000.0 / sampleVolL)
        val conc50PerM3 = p50 * (1000.0 / sampleVolL)
        val minSamplePoints = sqrt(area).roundToInt()

        // Calculated ISO Class N: N = log10(C) - 2.08 * log10(0.1 / 0.5)
        // Since (0.1/0.5)^2.08 = 0.2^2.08 = 0.0347
        val calculatedIsoClass = if (conc05PerM3 > 0) {
            log10(conc05PerM3 * 0.0347)
        } else {
            1.0
        }

        val deltaPercent = ((conc05PerM3 - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(conc05PerM3, limit)

        val steps = listOf(
            "Minimum Sampling Points (N_L)" to String.format(Locale.US, "⌈√%.0f m²⌉ = %d locations per ISO 14644-1", area, minSamplePoints),
            "Particles ≥ 0.5 µm Concentration" to String.format(Locale.US, "%.0f counts / (%.0f L / 1000) = %.0f particles/m³", p05, sampleVolL, conc05PerM3),
            "Particles ≥ 5.0 µm Concentration" to String.format(Locale.US, "%.0f counts / (%.0f L / 1000) = %.0f particles/m³", p50, sampleVolL, conc50PerM3),
            "Derived Cleanroom Class" to String.format(Locale.US, "ISO Class %.1f (EU GMP Grade C equivalent)", max(1.0, calculatedIsoClass))
        )

        return LiveCalculationResult(
            mainCalculatedValue = conc05PerM3,
            mainDisplayUnit = "particles/m³ (≥0.5µm)",
            formattedMainResult = String.format(Locale.US, "%.0f counts/m³", conc05PerM3),
            parameterEvaluated = "Airborne Particle Concentration (≥0.5µm)",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(conc05PerM3, limit, deltaPercent, "Particle count (≥0.5µm)", "particles/m³", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Cleanroom Class Benchmark (${String.format(Locale.US, "%,.0f", limit)} particles/m³)"
        )
    }

    private fun calculateLux(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val e0 = inputs["e_center"]?.toDoubleOrNull() ?: 520.0
        val e1 = inputs["e_point_1"]?.toDoubleOrNull() ?: 460.0
        val e2 = inputs["e_point_2"]?.toDoubleOrNull() ?: 490.0
        val e3 = inputs["e_point_3"]?.toDoubleOrNull() ?: 430.0
        val e4 = inputs["e_point_4"]?.toDoubleOrNull() ?: 475.0

        val readings = listOf(e0, e1, e2, e3, e4)
        val eAvg = readings.average()
        val eMin = readings.minOrNull() ?: 0.0
        val eMax = readings.maxOrNull() ?: 0.0
        val uniformityU0 = if (eAvg > 0) eMin / eAvg else 0.0

        // For Lux, lower than limit is BAD
        val deltaPercent = ((eAvg - limit) / limit) * 100.0
        val status = if (eAvg >= limit && uniformityU0 >= 0.40) {
            ComplianceStatus.COMPLIANT
        } else if (eAvg >= limit * 0.85) {
            ComplianceStatus.WARNING
        } else {
            ComplianceStatus.EXCEEDED
        }

        val steps = listOf(
            "Average Working Plane Illuminance" to String.format(Locale.US, "(%.0f + %.0f + %.0f + %.0f + %.0f) / 5 = %.1f Lux", e0, e1, e2, e3, e4, eAvg),
            "Illuminance Uniformity Ratio (U₀)" to String.format(Locale.US, "E_min (%.0f) / E_avg (%.1f) = %.2f (Standard min: 0.40)", eMin, eAvg, uniformityU0),
            "Diversity Ratio (E_min / E_max)" to String.format(Locale.US, "%.0f / %.0f = %.2f", eMin, eMax, if (eMax > 0) eMin / eMax else 0.0)
        )

        return LiveCalculationResult(
            mainCalculatedValue = eAvg,
            mainDisplayUnit = "Lux",
            formattedMainResult = String.format(Locale.US, "%.0f Lux", eAvg),
            parameterEvaluated = "Average Maintained Illuminance",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = if (eAvg >= limit && uniformityU0 >= 0.40) {
                "COMPLIANT: Measured average illuminance of ${String.format(Locale.US, "%.0f", eAvg)} Lux satisfies the ${standard.displayName} requirement of ${String.format(Locale.US, "%.0f", limit)} Lux with good uniformity (U₀ = ${String.format(Locale.US, "%.2f", uniformityU0)})."
            } else {
                "INSUFFICIENT ILLUMINANCE: Measured average ${String.format(Locale.US, "%.0f", eAvg)} Lux falls below the ${standard.displayName} minimum of ${String.format(Locale.US, "%.0f", limit)} Lux or fails uniformity."
            },
            legalCitation = "Evaluated against ${standard.displayName} Task Lighting Norm (${String.format(Locale.US, "%.0f", limit)} Lux maintained)"
        )
    }

    private fun calculateWater(
        limit: Double,
        inputs: Map<String, String>,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): LiveCalculationResult {
        val ph = inputs["water_ph"]?.toDoubleOrNull() ?: 7.35
        val doMg = inputs["dissolved_oxygen"]?.toDoubleOrNull() ?: 6.80
        val turbidity = inputs["turbidity_ntu"]?.toDoubleOrNull() ?: 3.20
        val ec = inputs["conductivity_us"]?.toDoubleOrNull() ?: 480.0
        val bod = inputs["bod_5"]?.toDoubleOrNull() ?: 18.5
        val cod = inputs["cod_cr"]?.toDoubleOrNull() ?: 46.0
        val tss = inputs["total_suspended_solids"]?.toDoubleOrNull() ?: 22.0

        val bodCodRatio = if (cod > 0) bod / cod else 0.4
        // Simplified Brown NSF Water Quality Index
        val wqiScore = max(0.0, min(100.0, 100.0 - (0.4 * bod + 0.1 * turbidity + 0.05 * tss + max(0.0, 6.0 - doMg) * 8.0)))

        val deltaPercent = ((bod - limit) / limit) * 100.0
        val status = evaluateStatusHigherIsWorse(bod, limit)

        val steps = listOf(
            "Effluent BOD₅ Concentration" to String.format(Locale.US, "%.1f mg/L (Limit: %.1f mg/L)", bod, limit),
            "Biodegradability Index (BOD/COD)" to String.format(Locale.US, "%.1f / %.1f = %.2f (Readily biodegradable: 0.4 to 0.6)", bod, cod, bodCodRatio),
            "Physicochemical Stability" to String.format(Locale.US, "pH: %.2f (Standard: 6.5 - 8.5) | DO: %.2f mg/L | Turbidity: %.1f NTU", ph, doMg, turbidity),
            "Overall Water Quality Index (WQI)" to String.format(Locale.US, "%.1f / 100 (%s)", wqiScore, if (wqiScore >= 80) "Good" else if (wqiScore >= 60) "Fair" else "Degraded")
        )

        return LiveCalculationResult(
            mainCalculatedValue = bod,
            mainDisplayUnit = "mg/L BOD₅",
            formattedMainResult = String.format(Locale.US, "%.1f mg/L", bod),
            parameterEvaluated = "Effluent BOD₅",
            stepByStepFormulas = steps,
            complianceStatus = status,
            chosenStandard = standard,
            thresholdLimit = limit,
            deltaPercent = deltaPercent,
            complianceSummary = buildSummary(bod, limit, deltaPercent, "Effluent BOD₅", "mg/L", standard, duration),
            legalCitation = "Evaluated against ${standard.displayName} Effluent Discharge Standard (${String.format(Locale.US, "%.0f", limit)} mg/L)"
        )
    }

    private fun evaluateStatusHigherIsWorse(value: Double, limit: Double): ComplianceStatus {
        return when {
            value <= limit -> ComplianceStatus.COMPLIANT
            value <= limit * 1.15 -> ComplianceStatus.WARNING
            else -> ComplianceStatus.EXCEEDED
        }
    }

    private fun buildSummary(
        value: Double,
        limit: Double,
        deltaPercent: Double,
        name: String,
        unit: String,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): String {
        return if (value <= limit) {
            String.format(
                Locale.US,
                "COMPLIANT (PASS): Measured %s of %.1f %s is within the %s %s statutory threshold of %.1f %s (%.1f%% safety margin).",
                name, value, unit, standard.displayName, duration.title, limit, unit, -deltaPercent
            )
        } else {
            String.format(
                Locale.US,
                "NON-COMPLIANT (EXCEEDED): Measured %s of %.1f %s exceeds the %s %s regulatory limit of %.1f %s by +%.1f%%.",
                name, value, unit, standard.displayName, duration.title, limit, unit, deltaPercent
            )
        }
    }
}
