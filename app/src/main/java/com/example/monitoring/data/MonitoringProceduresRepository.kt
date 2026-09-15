package com.example.monitoring.data

import com.example.monitoring.model.FdsInstrumentInfo
import com.example.monitoring.model.FdsProjectInfo
import com.example.monitoring.model.FdsSamplingRunRow
import com.example.monitoring.model.FieldDataSheetTemplate
import com.example.monitoring.model.MonitoringProcedureDoc
import com.example.monitoring.model.MonitoringProcedureDomain
import com.example.monitoring.model.RegulatoryBenchmarkRow

object MonitoringProceduresRepository {

    fun getProcedure(domain: MonitoringProcedureDomain): MonitoringProcedureDoc {
        return when (domain) {
            MonitoringProcedureDomain.AMBIENT -> MonitoringProcedureDoc(
                domain = domain,
                title = "Standardized Operating Procedure for Ambient Air Quality Monitoring (Particulate & Criteria Gases)",
                regulatoryCode = "SOP-ENV-AAQ-001 (ISO 12341 / USEPA 40 CFR 50 / CPCB NAAQS)",
                isoStandardRef = "ISO 12341:2023 (PM10 & PM2.5 Gravimetric Standard Reference Method)",
                usepaStandardRef = "USEPA 40 CFR Part 50 (Appendix B, J, and L Reference Methods)",
                localStandardRef = "CPCB National Ambient Air Quality Standards (NAAQS Notification 2009) & IS 5182",
                scopeAndObjective = "Determination of ambient suspended particulate matter (PM2.5 and PM10) and gaseous pollutants in ambient outdoor atmosphere over 24-hour composite and short-term interval cycles to determine regulatory air shed compliance.",
                principleOfMethod = "Ambient air is drawn through an aerodynamic size-selective inertial impactor or cyclone at a calibrated constant volumetric flow rate (16.67 L/min for low-volume samplers or 1.13 m³/min for high-volume samplers). Particulates are collected on pre-weighed, conditioned 47mm PTFE or quartz fiber micro-filter membranes. The total volume sampled is normalized to standard temperature (25°C/298K) and pressure (760 mmHg/101.3 kPa). Net mass gain determined by ultra-microbalance determines mass concentration.",
                requiredEquipmentAndReagents = listOf(
                    "Low-Volume Gravimetric Air Sampler with PM2.5 and PM10 WINS or VSCC size-selective inlets (Flow: 16.67 ± 0.8 L/min).",
                    "PTFE Membrane Filters (47mm, 2.0 µm pore size with polymethylpentene support ring) or Quartz Microfiber Filters (8x10 inch for HVAS).",
                    "Analytical Microbalance with sensitivity of 0.001 mg (1.0 µg) equipped with static discharge polonium-210 neutralizer.",
                    "Controlled Temperature & Humidity Conditioning Chamber (Temperature: 20.0 ± 1.0°C; Relative Humidity: 35 ± 5% RH).",
                    "Primary Calibrated Flow Orifice Transfer Standard (DeltaCal / BIOS Defender) traceable to NIST/NPL.",
                    "Barometer (calibrated to ±1 mmHg) and Thermocouple Digital Sensor (accuracy ±0.5°C)."
                ),
                preSamplingVerificationAndLeakCheck = listOf(
                    "Inspect sampling head and impactor nozzle jet for particulate buildup; clean with isopropanol and dry with zero gas.",
                    "Perform Pre-Sampling System Leak Check: Close sample inlet adapter; apply pump vacuum. Pressure drop must not exceed 5.0 mmHg (0.7 kPa) over a 60-second dwell period.",
                    "Calibrate flow rate against NIST-traceable primary flow calibrator; measured flow must be within ±2.0% of nominal 16.67 L/min.",
                    "Verify electronic timer clock synchronization against UTC standard within ±1 minute.",
                    "Examine conditioned filter membrane under 10x magnification for pinholes, tears, creases, or electrostatic charge prior to loading."
                ),
                samplingExecutionProtocol = listOf(
                    "Transfer conditioned filter from Petri-slide cassette to sampler filter holder using clean Teflon-coated forceps.",
                    "Record initial sampler timer reading (min), dry gas meter / volumetric totalizer (m³), and calibrated initial flow rate.",
                    "Configure automated 24-hour sampling timer (00:00 to 24:00) or 8-hour continuous cycle.",
                    "Monitor and log ambient barometric pressure and ambient dry-bulb temperature at minimum 4-hour intervals.",
                    "At sampling completion, record elapsed operating time, final indicated flow rate, and error flags (power interruption, low flow)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Carefully retrieve filter cassette using clean Teflon forceps; inspect for uncollected bypass dust or edge chafing.",
                    "Seal filter inside labeled antistatic conductive Petri-dish holder; store horizontally with exposed face upright.",
                    "Transport in passive cooler container with cold ice packs at 4 ± 2°C; ensure temperature does not exceed 10°C during transit.",
                    "Equilibrate filter inside weighing chamber at 20 ± 1°C and 35 ± 5% RH for minimum 24 hours prior to post-gravimetric weighing."
                ),
                qaQcProtocols = listOf(
                    "Field Blanks: Minimum 1 field blank filter per 10 sample runs (tare weight difference must be ≤ 5.0 µg).",
                    "Laboratory Weighing Duplicates: Re-weigh minimum 10% of filters; tolerance must be within ± 3.0 µg.",
                    "Collocated Sampling Precision: Minimum 15% collocated twin sampler deployment with Relative Percent Difference (RPD) < 10%.",
                    "Sampler Flow Rate Drift: Final flow rate must not drift more than ± 5.0% from initial calibrated flow."
                ),
                governingFormulas = listOf(
                    "Standard Sampled Volume (V_std)" to "V_std = V_actual × (P_actual / 760.0) × (298.15 / (T_actual + 273.15))",
                    "PM Mass Catch (ΔW)" to "ΔW (µg) = (Final Filter Mass W₂ (mg) - Initial Tare Mass W₁ (mg)) × 1000",
                    "Particulate Concentration (C_std)" to "C_std (µg/m³) = (ΔW - Blank Catch) / V_std",
                    "Flow Drift Rate (%)" to "Drift (%) = ((Final Flow - Initial Flow) / Initial Flow) × 100"
                ),
                regulatoryBenchmarks = listOf(
                    RegulatoryBenchmarkRow("Particulate Matter PM2.5", "15 µg/m³ (WHO) / 20 µg/m³ (ISO)", "35 µg/m³", "60 µg/m³", "µg/m³", "24-Hour Average"),
                    RegulatoryBenchmarkRow("Particulate Matter PM10", "45 µg/m³ (WHO) / 50 µg/m³ (ISO)", "150 µg/m³", "100 µg/m³", "µg/m³", "24-Hour Average"),
                    RegulatoryBenchmarkRow("Sulfur Dioxide (SO₂)", "40 µg/m³ (WHO 24h)", "75 ppb (~196 µg/m³)", "80 µg/m³", "µg/m³", "24-Hour Average"),
                    RegulatoryBenchmarkRow("Nitrogen Dioxide (NO₂)", "25 µg/m³ (WHO 24h)", "100 ppb (~188 µg/m³)", "80 µg/m³", "µg/m³", "24-Hour Average"),
                    RegulatoryBenchmarkRow("Carbon Monoxide (CO)", "4.0 mg/m³ (ISO/WHO)", "9.0 ppm (~10.0 mg/m³)", "2.0 mg/m³ (8h)", "mg/m³", "8-Hour Average"),
                    RegulatoryBenchmarkRow("Ground-level Ozone (O₃)", "100 µg/m³ (ISO/WHO)", "70 ppb (~137 µg/m³)", "100 µg/m³", "µg/m³", "8-Hour Average")
                )
            )

            MonitoringProcedureDomain.INDOOR -> MonitoringProcedureDoc(
                domain = domain,
                title = "Standardized Operating Procedure for Indoor Air Quality (IAQ) & Thermal Comfort Monitoring",
                regulatoryCode = "SOP-ENV-IAQ-002 (ISO 16000 / ASHRAE 62.1 & 55 / OSHA IAQ)",
                isoStandardRef = "ISO 16000-1 (General strategy), ISO 16000-2 (CO2/VOC), ISO 16000-3 (Formaldehyde)",
                usepaStandardRef = "USEPA Indoor Air Quality Tools for Schools & Building Air Quality Protocol",
                localStandardRef = "EN 16798-1 (Indoor Environmental Input Parameters) / ISHRAE IEQ Standard / OSHA 1910",
                scopeAndObjective = "Evaluation of indoor air contaminants, occupant metabolic respiration (CO₂ ventilation differential), Total Volatile Organic Compounds (TVOC), formaldehyde (HCHO), and thermal comfort parameters (Temperature, RH, Air Velocity) to assess ventilation adequacy and sick building syndrome risk.",
                principleOfMethod = "Direct continuous monitoring using Non-Dispersive Infrared (NDIR) sensors for Carbon Dioxide (CO₂), Photoionization Detectors (PID) calibrated to isobutylene for TVOCs, electrochemical/spectrophotometric sensors for HCHO, and precision thermo-anemometers for indoor air velocity. Sampling conducted at breathing zone elevation under normal occupancy conditions.",
                requiredEquipmentAndReagents = listOf(
                    "Multi-Gas IAQ Monitor equipped with dual-beam NDIR CO₂ sensor (0-5000 ppm, accuracy ±30 ppm + 3%).",
                    "Photoionization Detector (PID) with 10.6 eV lamp for TVOC measurement (0-20,000 ppb, 1 ppb resolution).",
                    "Formaldehyde Electrochemical / Chemical MBTH Spectrophotometer (0-5.00 ppm, limit of detection 0.005 ppm).",
                    "Omni-directional Hot-wire Anemometer for low velocity drafts (0.05 to 5.0 m/s, accuracy ±0.03 m/s).",
                    "Span Gas Standards: 1000 ppm CO₂ in balance N₂; 10.0 ppm Isobutylene span gas; Zero air canister (99.999% purity)."
                ),
                preSamplingVerificationAndLeakCheck = listOf(
                    "Confirm HVAC ventilation system has been operating in standard operational mode for minimum 2 hours prior to survey.",
                    "Perform Zero Calibration: Introduce hydrocarbon-free ultra-pure zero air to PID and NDIR sensors for 3 minutes.",
                    "Perform Span Calibration: Introduce 1000 ppm CO₂ standard; verify indicated reading stabilizes within ±2.0% of cylinder value.",
                    "Perform Isobutylene Span check on PID: Confirm RF response factor = 1.0; adjust span calibration gain if drift > 5%.",
                    "Position monitoring sensor probe on tripod at 1.1 to 1.5 m above floor level, at least 1.0 m from walls and windows."
                ),
                samplingExecutionProtocol = listOf(
                    "Measure outdoor baseline ambient CO₂ concentration 15 minutes before indoor monitoring (typically 410-430 ppm).",
                    "Establish indoor monitoring grid: 1 point per 200 m² floor area or 1 station per HVAC ventilation zone.",
                    "Record continuous parameters at 1-minute logging intervals for representative 8-hour occupancy work shift.",
                    "Count and record occupied workstation density, HVAC damper positions, and window/door openings during each interval.",
                    "Note specific emission sources: photocopying machines, newly painted partitions, solvent cleaners, or carpets."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Download and verify digital instrument data logging time stamps immediately upon completing measurement cycle.",
                    "If sorbent tubes (Tenax TA or Carbotrap) were deployed for GC-MS verification, cap tubes immediately with brass end caps.",
                    "Transport active sorbent tubes in airtight PTFE bags with cold packs at < 4°C to accredited analytical laboratory."
                ),
                qaQcProtocols = listOf(
                    "Pre- and post-monitoring calibration drift must not exceed ± 3.0% for CO₂ and ± 5.0% for PID VOC.",
                    "Indoor relative humidity sensor verified with saturated salt chambers (LiCl 11.3% RH and NaCl 75.3% RH).",
                    "Trip blanks deployed with thermal desorption tubes must show non-detect (< 0.1 µg/tube) for Target VOC analytes."
                ),
                governingFormulas = listOf(
                    "Ventilation CO₂ Differential (ΔCO₂)" to "ΔCO₂ (ppm) = Indoor CO₂ Measured - Outdoor Baseline CO₂",
                    "Estimated Outdoor Air Supply per Person (V_p)" to "V_p (L/s/person) = (Respiration Generation Rate G / ΔCO₂) × 10⁶",
                    "Air Changes per Hour (ACH)" to "ACH = (Total Outdoor Air Flow Rate Q (m³/h)) / Enclosed Room Volume V (m³)",
                    "Occupant Thermal Dissatisfaction (PPD %)" to "PPD = 100 - 95 × exp(- (0.03353 × PMV⁴ + 0.2179 × PMV²))"
                ),
                regulatoryBenchmarks = listOf(
                    RegulatoryBenchmarkRow("Carbon Dioxide (CO₂)", "800 - 1000 ppm (ASHRAE/ISO)", "1000 ppm max (Ventilation adequacy)", "1000 ppm (ISHRAE)", "ppm", "Continuous / 8h"),
                    RegulatoryBenchmarkRow("Total VOCs (TVOC)", "< 300 µg/m³ (ISO 16000)", "< 500 µg/m³ (LEED / WELL)", "< 300 µg/m³", "µg/m³", "8-Hour Average"),
                    RegulatoryBenchmarkRow("Formaldehyde (HCHO)", "0.08 ppm (100 µg/m³ WHO)", "0.027 ppm (LEED limit)", "0.08 ppm", "ppm", "30-min / 8-hour"),
                    RegulatoryBenchmarkRow("Carbon Monoxide (CO)", "6.0 ppm (WHO 24h)", "9.0 ppm (ASHRAE 62.1)", "2.0 ppm", "ppm", "8-Hour Average"),
                    RegulatoryBenchmarkRow("Indoor Temperature", "20.0 - 24.0 °C (ISO 7730)", "68 - 75 °F (20 - 24 °C)", "23 - 26 °C (Energy code)", "°C", "Continuous"),
                    RegulatoryBenchmarkRow("Relative Humidity", "30 - 60% (ASHRAE 55)", "30 - 60% (CIBSE / EPA)", "40 - 65%", "%", "Continuous")
                )
            )

            MonitoringProcedureDomain.NOISE -> MonitoringProcedureDoc(
                domain = domain,
                title = "Standardized Operating Procedure for Environmental & Industrial Noise Survey",
                regulatoryCode = "SOP-ENV-NOI-003 (ISO 1996-2 / USEPA Noise Control Act / CPCB Noise Rules)",
                isoStandardRef = "ISO 1996-1:2016 (Basic quantities) & ISO 1996-2:2017 (Determination of sound levels)",
                usepaStandardRef = "USEPA Guidelines on Noise Measurement (EPA 550/9-74-004) & OSHA 1910.95",
                localStandardRef = "CPCB Noise Pollution (Regulation and Control) Rules 2000 & IS 9989 / BS 4142",
                scopeAndObjective = "Assessment of ambient community sound pressure levels, industrial boundary fence line noise, and plant occupational noise exposure to establish compliance with statutory day/night decibel limits and prevent auditory degradation.",
                principleOfMethod = "Continuous precision acoustic logging using an IEC 61672-1 Class 1 Sound Level Meter (SLM) equipped with an omni-directional free-field prepolarized condenser microphone. Measurements are executed with frequency A-weighting (dBA) matching human auditory sensitivity, Fast/Slow dynamic time response, logging energy-equivalent continuous sound level (Leq), statistical percentiles (L10, L50, L90), and day-night sound level (Ldn).",
                requiredEquipmentAndReagents = listOf(
                    "IEC 61672 Class 1 Precision Sound Level Meter (Dynamic Range 25 - 140 dBA, Type Approved).",
                    "1/2-inch Free-field Prepolarized Condenser Microphone with preamplifier.",
                    "Acoustic Sound Calibrator conforming to IEC 60942 Class 1 (94.00 dB and 114.00 dB @ 1000 Hz, accuracy ±0.2 dB).",
                    "Open-pore Polyurethane Foam Windscreen (minimum 90mm diameter) for microphone protection against wind turbulence.",
                    "Non-metallic Tripod with adjustable mounting height (1.2 to 1.5 m above ground plane).",
                    "Handheld Digital Anemometer for surface wind speed verification (< 5.0 m/s threshold)."
                ),
                preSamplingVerificationAndLeakCheck = listOf(
                    "Inspect microphone diaphragm for particulate contamination, physical indentations, or moisture condensation.",
                    "Fit microphone into Class 1 Acoustic Calibrator; activate 94.0 dB @ 1 kHz signal.",
                    "Log pre-monitoring acoustic calibration value; measured sensitivity must agree within ±0.3 dB of certified reference.",
                    "Verify wind speed at survey location using anemometer; abort outdoor monitoring if wind speed exceeds 5.0 m/s.",
                    "Position tripod at 1.2 to 1.5 m above ground level, oriented at 45° elevation toward the dominant source, ≥ 3.5 m away from any sound-reflecting walls or façades."
                ),
                samplingExecutionProtocol = listOf(
                    "Install polyurethane foam windscreen over microphone throughout all outdoor measurement sessions.",
                    "Configure instrument parameters: Frequency weighting = 'A', Time weighting = 'Fast' (125 ms) or 'Slow' (1.0 s).",
                    "Set logging interval to 1.0 second, with automated summary integration at 15-minute intervals.",
                    "Execute daytime monitoring (06:00 to 22:00) and nighttime monitoring (22:00 to 06:00) as mandated by statutory rules.",
                    "Annotate intermittent non-target extraneous noise events (passing aircraft, sirens, rail pass-bys, barking dogs)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Perform post-monitoring acoustic calibration check immediately after completing survey.",
                    "If calibration drift exceeds ± 0.5 dB, all measurements taken since the previous valid calibration are invalid.",
                    "Download tamper-evident uncompressed time-history .WAV audio and decibel logging files to secure archive."
                ),
                qaQcProtocols = listOf(
                    "Pre- and post-calibration drift must be ≤ 0.3 dB (mandatory under ISO 1996-2 Section 5.3).",
                    "Annual laboratory calibration of Sound Level Meter and Acoustic Calibrator to ISO/IEC 17025 accredited standards.",
                    "Eliminate readings if ambient precipitation (rain/hail) occurs during recording session."
                ),
                governingFormulas = listOf(
                    "Equivalent Continuous Sound Level (Leq)" to "Leq = 10 × log₁₀ ( (1 / T) × ∑ [ 10^(Lᵢ / 10) × Δtᵢ ] )",
                    "Day-Night Sound Level (L_dn)" to "L_dn = 10 × log₁₀ ( (16/24) × 10^(L_day / 10) + (8/24) × 10^((L_night + 10) / 10) )",
                    "Community Rating Level (L_r)" to "L_r = L_Aeq,T + K_I (impulsive penalty) + K_T (tonal penalty)",
                    "Distance Sound Attenuation (Point Source)" to "L₂ = L₁ - 20 × log₁₀(r₂ / r₁) - A_atm - A_ground"
                ),
                regulatoryBenchmarks = listOf(
                    RegulatoryBenchmarkRow("Industrial Zone (Day / Night)", "70 / 70 dBA (ISO/WHO)", "70 / 70 dBA (USEPA Level)", "75 / 70 dBA (CPCB Rules)", "dBA", "Day (06-22h) / Night (22-06h)"),
                    RegulatoryBenchmarkRow("Commercial Zone (Day / Night)", "65 / 55 dBA (ISO/WHO)", "60 / 50 dBA", "65 / 55 dBA", "dBA", "Day (06-22h) / Night (22-06h)"),
                    RegulatoryBenchmarkRow("Residential Zone (Day / Night)", "50 / 40 dBA (WHO Guidelines)", "55 / 45 dBA (USEPA Guideline)", "55 / 45 dBA (CPCB Rules)", "dBA", "Day (06-22h) / Night (22-06h)"),
                    RegulatoryBenchmarkRow("Silence / Hospital Zone", "45 / 35 dBA (ISO/WHO)", "45 / 35 dBA", "50 / 40 dBA (CPCB Rules)", "dBA", "Day (06-22h) / Night (22-06h)"),
                    RegulatoryBenchmarkRow("Occupational Noise Exposure", "85 dBA (ISO 1999 8-hr)", "90 dBA PEL / 85 dBA Action (OSHA)", "85 dBA (Factories Act 8h)", "dBA", "8-Hour Shift TWA")
                )
            )

            MonitoringProcedureDomain.STACK -> MonitoringProcedureDoc(
                domain = domain,
                title = "Standardized Operating Procedure for Isokinetic Stack & Flue Gas Emission Monitoring",
                regulatoryCode = "SOP-ENV-STK-004 (USEPA Methods 1-5 / ISO 9096 / BS EN 13284-1)",
                isoStandardRef = "ISO 9096:2017 (Manual determination of mass concentration of particulate matter)",
                usepaStandardRef = "USEPA 40 CFR Part 60 Appendix A: Method 1 (Traverse), Method 2 (Velocity), Method 3 (Gas MW), Method 4 (Moisture), Method 5 (Particulates)",
                localStandardRef = "CPCB Guidelines on Methodologies for Source Emission Monitoring / BS EN 13284-1 / AS 4323.2",
                scopeAndObjective = "Determination of flue gas velocity, static pressure, moisture content, volumetric flow rate, particulate matter (PM) mass concentration (mg/Nm³), and hourly mass emission rate (kg/h) from industrial boilers, furnaces, and incinerators under strictly isokinetic sampling conditions.",
                principleOfMethod = "Gas is extracted isokinetically from the duct through a calibrated sharp-edged stainless steel or borosilicate nozzle at identical velocity to the surrounding gas stream (90% <= I <= 110%). Particulate matter is captured on a heated quartz thimble or filter maintained at 120 ± 14°C to prevent moisture condensation. Condensed water vapor is trapped in an ice-cooled impinger train for moisture calculation. Gas flow velocity head is measured via an S-type Pitot tube connected to an inclined manometer.",
                requiredEquipmentAndReagents = listOf(
                    "USEPA Method 5 Automated / Modular Isokinetic Stack Sampler Console with calibrated Dry Gas Meter (DGM).",
                    "Type-S Pitot Tube with NIST-calibrated coefficient Cp = 0.84 ± 0.02, rigidly affixed to heated sampling probe.",
                    "Calibrated Dual-Inclined Fluid Manometer (scale resolution 0.1 mm H₂O / 1.0 Pa).",
                    "Heated Borosilicate Glass / SS316 Sampling Probe with precision temperature controller (maintained at 120 ± 14°C).",
                    "Pre-weighed High-Purity Quartz Fiber Filters / Thimbles (porosity 0.3 µm, heated at 105°C and desiccated 24h).",
                    "Four-Impinger Condenser Train chilled in crushed ice bath with calibrated silica gel for moisture trapping.",
                    "Flue Gas Combustion Analyzer (NDIR/paramagnetic) for %O₂, %CO₂, %CO, and %N₂ molecular weight determination."
                ),
                preSamplingVerificationAndLeakCheck = listOf(
                    "Verify sampling port location meets the 8D/2D rule (minimum 8 duct diameters downstream and 2 diameters upstream from disturbances).",
                    "Calculate duct traverse sampling points based on USEPA Method 1 (minimum 12 points for circular ducts > 0.6m diameter).",
                    "Perform Pre-Test Sampling Train Leak Check: Plug nozzle tip; pull vacuum of 380 mmHg (15 in. Hg); leakage rate must not exceed 0.02 CFM (0.57 L/min).",
                    "Perform Pitot Line Leak Check: Blow into Pitot impact opening until 3 in. H₂O registers; seal; pressure must hold steady for 15 seconds.",
                    "Check impinger train initial weights and silica gel color (active cobalt blue; replace if pink/saturated)."
                ),
                samplingExecutionProtocol = listOf(
                    "Traverse probe across all designated sampling grid points, dwelling for identical time per point (e.g. 5 minutes per point).",
                    "Continuously adjust console pump orifice differential (ΔH) to match Pitot velocity head (ΔP) to maintain isokinetic ratio (I%) within 90 - 110%.",
                    "Record Pitot ΔP, stack gas temperature (Ts), orifice ΔH, DGM sampled volume (Vm), and DGM temperatures (Tm) at each point.",
                    "Maintain heated probe and filter box temperatures at 120 ± 14°C throughout the test run to prevent acid gas condensation.",
                    "Perform Post-Test Sampling Train Leak Check at maximum vacuum reached during the run (leakage must be ≤ 0.02 CFM)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Allow sampling train to cool; seal nozzle opening with clean polyethylene cap.",
                    "Carefully dismantle quartz thimble/filter into clean glass Petri container; use acetone rinse for probe liner catch (Container 2).",
                    "Measure impinger condensate volume in graduated cylinder (± 1 mL) and weigh silica gel to determine moisture catch (Vlc).",
                    "Evaporate acetone probe wash in fume hood at ambient temperature; dry filter in oven at 105°C for 2 hours; desiccate for 24h prior to gravimetric weighing."
                ),
                qaQcProtocols = listOf(
                    "Isokinetic Ratio Validation: Sampling run is legally invalid if Isokinetic Ratio (I%) falls outside 90.0% to 110.0%.",
                    "Acetone Field Reagent Blank: Run 200 mL of reagent-grade acetone through identical container; blank residue must be < 0.001 mg/mL.",
                    "Pitot Tube Inspection: Angle of nozzle face must align parallel to gas flow axis within ± 10 degrees."
                ),
                governingFormulas = listOf(
                    "Stack Gas Velocity (v_s)" to "v_s = K_p × C_p × √(ΔP × T_s / (P_s × M_s)) [where K_p = 34.97]",
                    "Dry Standard Sample Volume (V_m(std))" to "V_m(std) = V_m × Y × (T_std / T_m) × (P_bar + ΔH/13.6) / P_std",
                    "Flue Gas Moisture Fraction (B_ws)" to "B_ws = V_w(std) / (V_m(std) + V_w(std))",
                    "Isokinetic Ratio (I %)" to "I = (100 × T_s × [K₄ × V_lc + (V_m × Y / T_m) × (P_bar + ΔH/13.6)]) / (60 × θ × v_s × P_s × A_n)",
                    "Particulate Concentration (C_s)" to "C_s (mg/Nm³) = (Total Mass Catch ΔW (mg) / V_m(std)) × (273.15 / 298.15)",
                    "Mass Emission Rate (E_pm)" to "E_pm (kg/h) = C_s (mg/Nm³) × Q_std (Nm³/h) × 10⁻⁶"
                ),
                regulatoryBenchmarks = listOf(
                    RegulatoryBenchmarkRow("Particulate Matter (Boiler/Furnace)", "20 - 50 mg/Nm³ (ISO/EU IED)", "30 - 50 mg/Nm³ (USEPA NSPS)", "30 - 50 mg/Nm³ (CPCB Norms)", "mg/Nm³", "Dry, ref 6% or 11% O₂"),
                    RegulatoryBenchmarkRow("Sulfur Dioxide (SO₂)", "200 mg/Nm³ (EU Industrial)", "400 mg/Nm³ (USEPA NSPS)", "200 - 600 mg/Nm³ (CPCB)", "mg/Nm³", "Dry basis @ ref O₂"),
                    RegulatoryBenchmarkRow("Nitrogen Oxides (NOx as NO₂)", "200 - 300 mg/Nm³ (EU/ISO)", "200 - 450 mg/Nm³ (USEPA)", "300 - 450 mg/Nm³ (CPCB)", "mg/Nm³", "Dry basis @ ref O₂"),
                    RegulatoryBenchmarkRow("Carbon Monoxide (CO)", "100 mg/Nm³ (ISO/EU)", "150 mg/Nm³ (USEPA NSPS)", "150 mg/Nm³ (CPCB)", "mg/Nm³", "Hourly Average"),
                    RegulatoryBenchmarkRow("Isokinetic Compliance Window", "95 - 115% (ISO 9096)", "90 - 110% (USEPA Method 5)", "90 - 110% (CPCB Guidelines)", "%", "Per Traverse Run")
                )
            )

            MonitoringProcedureDomain.WATER -> MonitoringProcedureDoc(
                domain = domain,
                title = "Standardized Operating Procedure for Water & Wastewater Quality Monitoring",
                regulatoryCode = "SOP-ENV-WTR-005 (ISO 5667 / USEPA 40 CFR 136 / APHA 24th Ed.)",
                isoStandardRef = "ISO 5667-1 (Sampling design), ISO 5667-3 (Sample preservation), ISO 5667-6 (Rivers), ISO 10523 (pH)",
                usepaStandardRef = "USEPA Clean Water Act 40 CFR Part 136 (Guidelines Establishing Test Procedures for Pollutants)",
                localStandardRef = "CPCB General Standards for Discharge of Environmental Pollutants (Schedule VI, EPA 1986) & IS 10500",
                scopeAndObjective = "Field sampling, on-site physicochemical parameter measurement, sample preservation, and chain of custody documentation for industrial effluent, municipal sewage treatment outlets, surface water bodies, and groundwater aquifers.",
                principleOfMethod = "Representative grab or composite sampling using depth-integrated Van Dorn or peristaltic samplers. In-situ measurement of unstable parameters (Temperature, pH, Dissolved Oxygen, Electrical Conductivity, Turbidity) via calibrated multi-parameter sensors. Chemical preservation with analytical grade reagents immediately following collection to inhibit biological degradation and adsorption prior to laboratory analysis.",
                requiredEquipmentAndReagents = listOf(
                    "Multi-Parameter Field Water Quality Meter with optical luminescent DO, flat-surface pH, and 4-electrode graphite EC probe.",
                    "Borosilicate Amber Glass Bottles (1000 mL) with PTFE-lined caps for Oil & Grease, Hydrocarbons, and Pesticides.",
                    "High-Density Polyethylene (HDPE) Bottles (1000 mL) pre-washed with 1:1 HNO₃ for Trace Heavy Metals.",
                    "Winkler DO Glass Bottles (300 mL) with ground-glass stoppers for biochemical oxygen demand titration.",
                    "Preservation Reagents: Ultrapure Concentrated Nitric Acid (HNO₃, 65%), Concentrated Sulfuric Acid (H₂SO₄, 98%), Sodium Hydroxide (NaOH).",
                    "Insulated Field Coolers with sealed frozen gel packs maintained at 4 ± 2°C.",
                    "Standard Buffer Calibration Solutions: pH 4.01, 7.00, and 10.01; EC standard 1413 µS/cm; 0.0 mg/L sodium sulfite DO standard."
                ),
                preSamplingVerificationAndLeakCheck = listOf(
                    "Perform 3-point calibration of pH electrode using fresh NIST buffers (pH 4.01, 7.00, 10.01); slope must be between 95% and 105%.",
                    "Calibrate optical Dissolved Oxygen (DO) probe in 100% water-saturated air chamber at ambient barometric pressure.",
                    "Calibrate Electrical Conductivity cell against 1413 µS/cm KCl standard; verify temperature compensation set to 25.0°C.",
                    "Inspect all sample containers for cap integrity, cleanliness, and chemical pre-wash certifications.",
                    "Rinse sampling bailer/bucket three times with source water prior to composite filling (except for oil/grease samples)."
                ),
                samplingExecutionProtocol = listOf(
                    "Sample from the center of water channel/stream at 60% of total depth (avoid skimming floating debris or disturbing bed silt).",
                    "For industrial effluent discharges, record instantaneous flow rate using calibrated V-notch weir or ultrasonic flow meter.",
                    "Immerse multi-parameter probe; allow reading to stabilize for minimum 60 seconds; log in-situ Temp, pH, DO, EC, and Turbidity.",
                    "Fill specific bottles without headspace for volatile organics (VOCs) and biochemical oxygen demand (BOD).",
                    "Collect separate grab sample for Oil & Grease directly in amber glass bottle; never pre-rinse bottle with sample water."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Heavy Metals: Acidify with conc. HNO₃ to pH < 2.0 immediately upon collection (verified with narrow-range pH paper).",
                    "Chemical Oxygen Demand (COD) & Ammonia: Acidify with conc. H₂SO₄ to pH < 2.0; store at 4°C.",
                    "Biochemical Oxygen Demand (BOD₅): Cool immediately to 4 ± 2°C without freezing; laboratory incubation must commence within 24 hours.",
                    "Microbiology (Total/Fecal Coliforms): Collect in sterile bottles containing sodium thiosulfate (if chlorinated); deliver to lab within 6 hours.",
                    "Seal each bottle with tamper-evident security tape; sign and cross-reference on Chain of Custody (CoC) manifest."
                ),
                qaQcProtocols = listOf(
                    "Field Blanks: 1 field blank filled with ASTM Type II deionized water per 10 samples processed.",
                    "Field Duplicate: Collect collocated duplicate from identical discharge flow; Relative Percent Difference (RPD) must be < 15%.",
                    "Holding Time Adherence: Strictly observe statutory holding times (BOD: 48h; COD: 28 days; Metals: 6 months)."
                ),
                governingFormulas = listOf(
                    "Biochemical Oxygen Demand (BOD₅)" to "BOD₅ (mg/L) = (Initial Dissolved Oxygen D₁ - Final DO after 5 days D₂) × Dilution Factor P",
                    "Chemical Oxygen Demand (COD)" to "COD (mg/L) = ((Volume blank V_b - Volume sample V_s) × M_FAS × 8000) / Sample Volume (mL)",
                    "Biodegradability Index" to "Biodegradability Index = BOD₅ / COD [Ratio > 0.4 indicates biologically treatable waste]",
                    "Total Dissolved Solids (TDS approx.)" to "TDS (mg/L) ≈ Electrical Conductivity (µS/cm) × 0.65",
                    "NSF Water Quality Index (WQI)" to "WQI = ∑ (Sub-index qᵢ × Weighting Factor wᵢ)"
                ),
                regulatoryBenchmarks = listOf(
                    RegulatoryBenchmarkRow("pH Value", "6.5 - 8.5 (ISO/WHO)", "6.0 - 9.0 (USEPA NPDES)", "5.5 - 9.0 (CPCB Inland Surface)", "pH units", "Instantaneous Grab"),
                    RegulatoryBenchmarkRow("Biochemical Oxygen Demand (BOD₅)", "< 5.0 mg/L (River Class A)", "30 mg/L (USEPA Secondary STP)", "30 mg/L (CPCB General Standard)", "mg/L", "5-Day @ 20°C"),
                    RegulatoryBenchmarkRow("Chemical Oxygen Demand (COD)", "< 20 mg/L (Surface water)", "125 mg/L (USEPA/EU Urban STP)", "250 mg/L (CPCB Inland Discharge)", "mg/L", "Standard 2h digestion"),
                    RegulatoryBenchmarkRow("Total Suspended Solids (TSS)", "< 25 mg/L (ISO/EU WFD)", "30 mg/L (USEPA 30-day avg)", "100 mg/L (CPCB Discharge Standard)", "mg/L", "Gravimetric 105°C"),
                    RegulatoryBenchmarkRow("Dissolved Oxygen (DO)", "> 6.0 mg/L (ISO/WHO)", "> 5.0 mg/L (USEPA Warmwater)", "> 5.0 mg/L (CPCB Class C/SW)", "mg/L", "In-situ field reading"),
                    RegulatoryBenchmarkRow("Oil & Grease", "< 1.0 mg/L (ISO Drinking/River)", "10 mg/L (USEPA NPDES typical)", "10 mg/L (CPCB Schedule VI)", "mg/L", "Gravimetric Extraction")
                )
            )
        }
    }

    fun getFieldDataSheetTemplate(domain: MonitoringProcedureDomain): FieldDataSheetTemplate {
        return when (domain) {
            MonitoringProcedureDomain.AMBIENT -> FieldDataSheetTemplate(
                domain = domain,
                formTitle = "AMBIENT AIR QUALITY MONITORING FIELD DATA SHEET",
                documentNumber = "EDEN-QMS-FDS-AAQ-01 / Rev 4.2",
                accreditationNotice = "ISO/IEC 17025:2017 & USEPA 40 CFR 50 COMPLIANT FIELD AUDIT RECORD",
                defaultProjectInfo = FdsProjectInfo(
                    facilityName = "Apex Petrochemical Complex",
                    siteLocation = "Plot 42-B, Industrial Estate, Sector 7",
                    stationId = "AAQ-STATION-NORTH-01",
                    monitoringDate = "15-Sep-2026",
                    technicianName = "Er. Nathan Reed, Environmental Specialist",
                    reviewerName = "Dr. Maya Lin, QA/QC Technical Manager",
                    ambientTemp = "26.5",
                    barometricPressure = "758.0",
                    relativeHumidity = "48.0",
                    weatherCondition = "Clear sky, Wind NW 2.2 m/s, No rain"
                ),
                defaultInstrumentInfo = FdsInstrumentInfo(
                    instrumentModel = "Thermo Fisher Partisol 2025i FRM Sampler",
                    serialNumber = "TF-2025I-8841",
                    calibrationCertNumber = "CAL-NIST-2026-9942",
                    calibrationValidUntil = "30-Nov-2026",
                    preCalReading = "16.67 L/min",
                    postCalReading = "16.65 L/min",
                    calibrationDrift = "0.12% (< 2.0% allowable)"
                ),
                preSamplingChecklist = listOf(
                    "Impactor Jet & WINS well inspected, cleaned, and oiled with silicone.",
                    "Pre-sampling vacuum leak check passed (< 5 mmHg drop over 60s).",
                    "Flow calibration verified at 16.67 L/min (± 0.8 L/min).",
                    "Conditioned 47mm PTFE filter inspected for flaws and tare weight logged.",
                    "Electronic clock and 24-hour timer synchronized to UTC standard."
                ),
                runTableHeaders = listOf("Run Interval", "Filter ID", "Initial Flow (L/min)", "Final Flow (L/min)", "Run Time (min)", "Status"),
                sampleRunData = listOf(
                    FdsSamplingRunRow("00:00 - 08:00", "FLT-PM25-881", "16.67", "16.66", "480", "Normal - Valid"),
                    FdsSamplingRunRow("08:00 - 16:00", "FLT-PM25-882", "16.66", "16.65", "480", "Normal - Valid"),
                    FdsSamplingRunRow("16:00 - 24:00", "FLT-PM25-883", "16.65", "16.64", "480", "Normal - Valid"),
                    FdsSamplingRunRow("24-Hr Composite", "FLT-PM25-COMP", "16.67", "16.65", "1440", "Certified 24h Run")
                ),
                calculations = listOf(
                    "Total Volume Sampled (Actual V_a)" to "24.00 m³ (1440 min × 16.66 L/min avg)",
                    "Volume Corrected to Standard NTP (V_std)" to "23.78 Nm³ (298.15K, 760 mmHg)",
                    "Filter Tare Weight (W₁)" to "145.230 mg (Desiccator conditioned 24h)",
                    "Filter Gross Weight (W₂)" to "145.885 mg (Post-exposure conditioned)",
                    "Net Particulate Mass Catch (ΔW)" to "0.655 mg (655.0 µg net catch)",
                    "Calculated PM2.5 Concentration" to "27.54 µg/m³ (NTP Standardized)"
                ),
                measuredResult = "27.54 µg/m³ (PM2.5 24-Hour Average)",
                regulatoryVerdict = "COMPLIANT with USEPA NAAQS (35 µg/m³) and CPCB NAAQS (60 µg/m³). Exceeds WHO 2021 Guideline (15 µg/m³).",
                isCompliant = true
            )

            MonitoringProcedureDomain.INDOOR -> FieldDataSheetTemplate(
                domain = domain,
                formTitle = "INDOOR AIR QUALITY (IAQ) & VENTILATION FIELD DATA SHEET",
                documentNumber = "EDEN-QMS-FDS-IAQ-02 / Rev 3.1",
                accreditationNotice = "ISO 16000 & ASHRAE 62.1/55 CONFORMANT INDOOR AUDIT CERTIFICATE",
                defaultProjectInfo = FdsProjectInfo(
                    facilityName = "Global Horizon Financial Tower",
                    siteLocation = "Floor 14, Trading Floor & Conference Wing",
                    stationId = "IAQ-ZONE-14A",
                    monitoringDate = "15-Sep-2026",
                    technicianName = "Kavita Nair, Senior Industrial Hygienist",
                    reviewerName = "Marcus Vance, CIH Technical Reviewer",
                    ambientTemp = "23.2",
                    barometricPressure = "759.2",
                    relativeHumidity = "48.5",
                    weatherCondition = "Indoor Controlled HVAC Zone (VAV System Active)"
                ),
                defaultInstrumentInfo = FdsInstrumentInfo(
                    instrumentModel = "TSI Q-Trak IAQ Multi-Gas Monitor 7575",
                    serialNumber = "TSI-7575-10492",
                    calibrationCertNumber = "CAL-TSI-2026-0391",
                    calibrationValidUntil = "15-Jan-2027",
                    preCalReading = "0 ppm (Zero Air) / 1000 ppm CO₂",
                    postCalReading = "1002 ppm CO₂",
                    calibrationDrift = "0.20% (< 3.0% acceptable)"
                ),
                preSamplingChecklist = listOf(
                    "HVAC air handling unit running on standard occupancy schedule for ≥ 2 hours.",
                    "NDIR sensor zeroed with 99.999% ultra-pure nitrogen gas.",
                    "CO₂ span verified with certified 1000 ppm reference cylinder.",
                    "PID sensor calibrated with 10.0 ppm isobutylene balance air.",
                    "Sampling probe elevated to 1.1 - 1.5m breathing zone, ≥ 1m from doors."
                ),
                runTableHeaders = listOf("Time Interval", "CO₂ (ppm)", "TVOC (µg/m³)", "HCHO (ppm)", "Temp (°C)", "RH (%)"),
                sampleRunData = listOf(
                    FdsSamplingRunRow("09:00 - 11:00", "740", "185", "0.022", "23.1", "47.8"),
                    FdsSamplingRunRow("11:00 - 13:00", "865", "230", "0.031", "23.4", "49.0"),
                    FdsSamplingRunRow("13:00 - 15:00", "915", "265", "0.038", "23.6", "49.5"),
                    FdsSamplingRunRow("15:00 - 17:00", "835", "215", "0.028", "23.2", "48.2")
                ),
                calculations = listOf(
                    "Outdoor Baseline CO₂ Level" to "415 ppm (Measured on fresh air intake)",
                    "Average Indoor CO₂ Concentration" to "838.75 ppm (8-Hour Time-Weighted Average)",
                    "Ventilation Differential (ΔCO₂)" to "423.75 ppm (< 700 ppm ASHRAE threshold)",
                    "Effective Outdoor Air Flow Rate" to "11.2 L/s/person (Complies with ASHRAE 62.1 min 8.5 L/s)",
                    "Average TVOC Concentration" to "223.75 µg/m³ (< 300 µg/m³ ISO 16000 target)",
                    "Average Formaldehyde (HCHO)" to "0.030 ppm (37 µg/m³, within 0.08 ppm limit)"
                ),
                measuredResult = "838.8 ppm CO₂ | 223.8 µg/m³ TVOC | 23.3°C Temp",
                regulatoryVerdict = "COMPLIANT with ASHRAE 62.1-2022, ISO 16000, and LEED v4.1 Enhanced Indoor Air Quality requirements.",
                isCompliant = true
            )

            MonitoringProcedureDomain.NOISE -> FieldDataSheetTemplate(
                domain = domain,
                formTitle = "ACOUSTIC & NOISE LEVEL SURVEY FIELD DATA SHEET",
                documentNumber = "EDEN-QMS-FDS-NOI-03 / Rev 2.5",
                accreditationNotice = "ISO 1996-2 & IEC 61672 CLASS 1 SOUND SURVEY COMPLIANCE LOG",
                defaultProjectInfo = FdsProjectInfo(
                    facilityName = "Southern Industrial Logistics Hub",
                    siteLocation = "Western Perimeter Boundary (Adjacent to Residential Buffer)",
                    stationId = "NOISE-BOUNDARY-WEST-02",
                    monitoringDate = "15-Sep-2026",
                    technicianName = "Suresh Pillai, Certified Acoustical Engineer",
                    reviewerName = "Dr. Maya Lin, QA/QC Technical Manager",
                    ambientTemp = "25.0",
                    barometricPressure = "760.5",
                    relativeHumidity = "52.0",
                    weatherCondition = "Wind speed 1.8 m/s (Anemometer verified < 5 m/s), No rain"
                ),
                defaultInstrumentInfo = FdsInstrumentInfo(
                    instrumentModel = "Brüel & Kjær Type 2250 Sound Level Meter (Class 1)",
                    serialNumber = "BK-2250-30194",
                    calibrationCertNumber = "CAL-NPL-2026-5510",
                    calibrationValidUntil = "12-Dec-2026",
                    preCalReading = "94.00 dB (Pre-test Acoustic Calibrator check)",
                    postCalReading = "94.05 dB (Post-test calibration check)",
                    calibrationDrift = "+0.05 dB (Allowable limit ≤ 0.3 dB)"
                ),
                preSamplingChecklist = listOf(
                    "Microphone diaphragm clean; free-field polyurethane foam windscreen installed.",
                    "Pre-survey acoustic calibration checked at 94.0 dB @ 1 kHz; drift ≤ 0.3 dB.",
                    "Tripod set at 1.5 m elevation, ≥ 3.5 m from reflective structures.",
                    "Surface wind speed checked with anemometer (1.8 m/s; < 5.0 m/s threshold).",
                    "A-frequency and Fast time weightings selected with 1.0s logging interval."
                ),
                runTableHeaders = listOf("Monitoring Period", "Leq (dBA)", "Lmax (dBA)", "Lmin (dBA)", "L10 (dBA)", "L90 (dBA)"),
                sampleRunData = listOf(
                    FdsSamplingRunRow("Day (06:00 - 10:00)", "64.2", "76.4", "48.5", "67.8", "51.2"),
                    FdsSamplingRunRow("Day (10:00 - 14:00)", "67.8", "81.2", "50.1", "71.4", "53.5"),
                    FdsSamplingRunRow("Day (14:00 - 18:00)", "66.5", "79.0", "49.2", "69.8", "52.0"),
                    FdsSamplingRunRow("Day (18:00 - 22:00)", "65.1", "77.5", "48.0", "68.2", "50.8"),
                    FdsSamplingRunRow("Night (22:00 - 02:00)", "54.8", "68.2", "44.1", "57.5", "46.2"),
                    FdsSamplingRunRow("Night (02:00 - 06:00)", "52.4", "63.5", "42.0", "55.1", "44.5")
                ),
                calculations = listOf(
                    "Integrated Daytime Leq (06:00 to 22:00)" to "66.12 dBA (A-weighted energy average)",
                    "Integrated Nighttime Leq (22:00 to 06:00)" to "53.76 dBA (A-weighted energy average)",
                    "Day-Night Sound Level (L_dn)" to "66.52 dBA (Calculated with +10 dB night penalty)",
                    "Background Sound Level (L90)" to "51.88 dBA Day / 45.35 dBA Night",
                    "Peak Extraneous Noise Factor" to "L10 - L90 = 15.92 dBA (Reflects intermittent heavy haul trucks)"
                ),
                measuredResult = "66.12 dBA Day / 53.76 dBA Night (Leq)",
                regulatoryVerdict = "COMPLIANT with Commercial Zone limits (65 dBA Day / 55 dBA Night). COMPLIANT with Industrial Zone limits (75/70 dBA). Near-threshold for mixed residential border.",
                isCompliant = true
            )

            MonitoringProcedureDomain.STACK -> FieldDataSheetTemplate(
                domain = domain,
                formTitle = "ISOKINETIC STACK EMISSION MONITORING FIELD DATA SHEET",
                documentNumber = "EDEN-QMS-FDS-STK-04 / Rev 5.0",
                accreditationNotice = "USEPA METHOD 5 & ISO 9096 SOURCE EMISSION COMPLIANCE SHEET",
                defaultProjectInfo = FdsProjectInfo(
                    facilityName = "Torrent Thermal Power Generating Station",
                    siteLocation = "Boiler Unit 3 Flue Gas Stack Sampling Elevation (+65m)",
                    stationId = "STACK-UNIT-3-PORT-A",
                    monitoringDate = "15-Sep-2026",
                    technicianName = "Er. Vikram Sengupta, Lead Emission Testing Officer",
                    reviewerName = "Dr. Maya Lin, QA/QC Technical Manager",
                    ambientTemp = "32.0",
                    barometricPressure = "754.5",
                    relativeHumidity = "55.0",
                    weatherCondition = "Stack Gas Temp: 165°C, Duct Static Pressure: -12.4 mm H₂O"
                ),
                defaultInstrumentInfo = FdsInstrumentInfo(
                    instrumentModel = "Apex Instruments Method 5 Isokinetic Sampling Console",
                    serialNumber = "APX-M5-4491",
                    calibrationCertNumber = "CAL-EPA-2026-7819",
                    calibrationValidUntil = "28-Feb-2027",
                    preCalReading = "Pitot Cp = 0.840 | DGM Y = 1.002",
                    postCalReading = "Pre-leak check: 0.008 CFM @ 15 in. Hg (Pass)",
                    calibrationDrift = "Post-leak check: 0.010 CFM @ 12 in. Hg (Pass < 0.02 CFM)"
                ),
                preSamplingChecklist = listOf(
                    "Port dimensions verified: 8 stack diameters downstream, 2 diameters upstream from bend.",
                    "12 Traverse points marked on probe according to USEPA Method 1 layout.",
                    "S-type Pitot tube lines leak checked at 3.0 in. H₂O for 15 seconds (zero drop).",
                    "Sampling train leak checked at 15 in. Hg (leak rate = 0.008 CFM; < 0.02 CFM limit).",
                    "Heated probe and filter oven set and stabilized at 120 ± 14°C."
                ),
                runTableHeaders = listOf("Point ID", "ΔP (mm H₂O)", "Ts (°C)", "DGM Vm (m³)", "ΔH (mm H₂O)", "Tm (°C)"),
                sampleRunData = listOf(
                    FdsSamplingRunRow("Traverse Pt 1", "15.8", "164.2", "0.125", "32.5", "31.5"),
                    FdsSamplingRunRow("Traverse Pt 2", "16.5", "165.0", "0.250", "34.0", "32.0"),
                    FdsSamplingRunRow("Traverse Pt 3", "17.2", "165.5", "0.380", "35.5", "32.2"),
                    FdsSamplingRunRow("Traverse Pt 4", "16.8", "165.2", "0.505", "34.8", "32.5"),
                    FdsSamplingRunRow("Traverse Pt 5", "16.2", "164.8", "0.630", "33.5", "32.8"),
                    FdsSamplingRunRow("Traverse Pt 6", "17.0", "165.4", "0.760", "35.0", "33.0")
                ),
                calculations = listOf(
                    "Average Stack Velocity Head (ΔP)" to "16.58 mm H₂O (0.653 in. H₂O)",
                    "Average Stack Gas Velocity (v_s)" to "19.45 m/s (Pitot Cp = 0.84, Mw = 29.4 g/mol)",
                    "Total Volumetric Stack Flow Rate" to "242,500 Nm³/h (Standard Dry Basis @ 273K)",
                    "DGM Sampled Volume (V_m(std))" to "1.412 Nm³ (Corrected for temperature and orifice ΔH)",
                    "Total Particulate Mass Catch (Filter + Wash)" to "52.4 mg (Thimble gross 554.8 mg - tare 502.4 mg)",
                    "Isokinetic Sampling Ratio (I %)" to "99.4% (COMPLIANT within mandatory 90 - 110% window)",
                    "Particulate Concentration (C_s)" to "37.11 mg/Nm³ (Dry standard gas)",
                    "Normalized Particulate Conc (@ 6% O₂)" to "34.20 mg/Nm³ (Stack O₂ = 4.8%)",
                    "Mass Emission Rate of Particulates" to "9.00 kg/h (216.0 kg/day)"
                ),
                measuredResult = "34.20 mg/Nm³ @ 6% Ref O₂ | Isokinetic: 99.4%",
                regulatoryVerdict = "COMPLIANT with CPCB/USEPA NSPS limit (50 mg/Nm³). Legally valid test with 99.4% isokinetic ratio.",
                isCompliant = true
            )

            MonitoringProcedureDomain.WATER -> FieldDataSheetTemplate(
                domain = domain,
                formTitle = "WATER & EFFLUENT QUALITY MONITORING FIELD DATA SHEET",
                documentNumber = "EDEN-QMS-FDS-WTR-05 / Rev 3.4",
                accreditationNotice = "ISO 5667 & USEPA 40 CFR 136 ACCREDITED WATER SAMPLING CERTIFICATE",
                defaultProjectInfo = FdsProjectInfo(
                    facilityName = "GreenRiver Industrial Common Effluent Treatment Plant (CETP)",
                    siteLocation = "Treated Effluent Final Discharge Outfall Channel 01",
                    stationId = "WTR-CETP-OUTFALL-01",
                    monitoringDate = "15-Sep-2026",
                    technicianName = "Dr. Robert Vance, Lead Environmental Chemist",
                    reviewerName = "Dr. Maya Lin, QA/QC Technical Manager",
                    ambientTemp = "27.0",
                    barometricPressure = "758.0",
                    relativeHumidity = "62.0",
                    weatherCondition = "Flow Rate: 4,200 m³/day, Effluent clear with faint amber hue"
                ),
                defaultInstrumentInfo = FdsInstrumentInfo(
                    instrumentModel = "YSI ProDSS Multi-Parameter Water Quality Meter",
                    serialNumber = "YSI-PDSS-7721",
                    calibrationCertNumber = "CAL-WT-2026-1180",
                    calibrationValidUntil = "20-Jan-2027",
                    preCalReading = "pH 7.00 @ 25°C | EC 1413 µS/cm | DO 100% Sat",
                    postCalReading = "pH 7.02 | EC 1415 µS/cm | DO 99.6% Sat",
                    calibrationDrift = "pH drift +0.02 | DO drift -0.4% (Within ±2.0% tolerance)"
                ),
                preSamplingChecklist = listOf(
                    "pH 3-point calibration completed (buffers 4.01, 7.00, 10.01; slope = 98.4%).",
                    "Optical DO sensor calibrated in air-saturated water chamber.",
                    "EC sensor verified with 1413 µS/cm standard (temp compensation @ 25°C).",
                    "Acid-preserved HDPE bottles for trace metals (conc. HNO₃ pH < 2.0).",
                    "Amber glass bottles for Oil & Grease filled without headspace."
                ),
                runTableHeaders = listOf("Sample ID / Time", "Temp (°C)", "pH", "DO (mg/L)", "EC (µS/cm)", "Turbidity (NTU)"),
                sampleRunData = listOf(
                    FdsSamplingRunRow("GRAB-01 (08:00)", "26.8", "7.35", "5.8", "1240", "6.2"),
                    FdsSamplingRunRow("GRAB-02 (12:00)", "27.4", "7.42", "5.5", "1285", "6.8"),
                    FdsSamplingRunRow("GRAB-03 (16:00)", "27.2", "7.38", "5.6", "1260", "6.5"),
                    FdsSamplingRunRow("COMPOSITE-24H", "27.1", "7.38", "5.63", "1261.7", "6.5")
                ),
                calculations = listOf(
                    "Average In-situ pH" to "7.38 (Optimal discharge range: 6.5 - 8.5)",
                    "Dissolved Oxygen (DO)" to "5.63 mg/L (> 5.0 mg/L statutory minimum)",
                    "Electrical Conductivity (EC)" to "1261.7 µS/cm (Est. TDS ≈ 820 mg/L)",
                    "Biochemical Oxygen Demand (BOD₅)" to "18.5 mg/L (Standard 5-day @ 20°C incubation)",
                    "Chemical Oxygen Demand (COD)" to "74.0 mg/L (Open reflux K₂Cr₂O₇ method)",
                    "Biodegradability Index (BOD/COD)" to "0.25 (Indicates highly stabilized tertiary treated effluent)",
                    "Total Suspended Solids (TSS)" to "22.4 mg/L (Oven dried 105°C gravimetric)",
                    "Oil & Grease" to "< 2.0 mg/L (Below detection threshold)"
                ),
                measuredResult = "BOD₅: 18.5 mg/L | COD: 74.0 mg/L | TSS: 22.4 mg/L | pH: 7.38",
                regulatoryVerdict = "COMPLIANT with CPCB Schedule VI General Standards (BOD < 30, COD < 250, TSS < 100 mg/L) and USEPA NPDES Secondary Standards.",
                isCompliant = true
            )
        }
    }
}
