package com.example.monitoring.data

import com.example.monitoring.model.InstrumentManualDetails
import com.example.monitoring.model.MonitoringDuration
import com.example.monitoring.model.MonitoringFieldDef
import com.example.monitoring.model.PollutionMonitoringDomain
import com.example.monitoring.model.RegulatoryStandard
import com.example.monitoring.model.StandardBenchmarkThreshold
import com.example.monitoring.model.StandardProcedureDetails

object PollutionMonitoringRepository {

    /**
     * Field Data Sheet definitions for each monitoring domain
     */
    fun getFieldDefinitions(domain: PollutionMonitoringDomain): List<MonitoringFieldDef> {
        return when (domain) {
            PollutionMonitoringDomain.AMBIENT_AIR -> listOf(
                MonitoringFieldDef("initial_flow", "Initial Sampler Flow Rate", "L/min", "16.67", "Calibrated critical orifice/rotameter flow rate at sampling start"),
                MonitoringFieldDef("final_flow", "Final Sampler Flow Rate", "L/min", "16.65", "Flow rate verified prior to sampling shutdown"),
                MonitoringFieldDef("duration_minutes", "Sampling Elapsed Time", "min", "1440", "Total minutes sampled (e.g. 15 for Spot, 60 for 1-hr, 480 for 8-hr, 1440 for 24-hr)"),
                MonitoringFieldDef("initial_filter_wt", "Initial Filter Tare Weight (W₁)", "mg", "145.230", "Conditioned PTFE/Glass fiber filter weight in desiccator"),
                MonitoringFieldDef("final_filter_wt", "Final Filter Gross Weight (W₂)", "mg", "145.885", "Post-exposure filter weight after 24h temperature/RH equilibrium"),
                MonitoringFieldDef("ambient_temp", "Ambient Temperature (Tₐ)", "°C", "26.5", "Average atmospheric temperature during sampling cycle"),
                MonitoringFieldDef("ambient_pressure", "Atmospheric Barometric Pressure (Pₐ)", "mmHg", "758.0", "Local barometric station pressure")
            )

            PollutionMonitoringDomain.INDOOR_AIR -> listOf(
                MonitoringFieldDef("co2_measured", "Indoor Carbon Dioxide (CO₂)", "ppm", "820", "NDIR sensor reading at 1.1m breathing zone height"),
                MonitoringFieldDef("ambient_co2", "Outdoor Reference CO₂ Level", "ppm", "415", "Fresh outdoor air baseline reading"),
                MonitoringFieldDef("tvoc_measured", "Total VOC Concentration", "µg/m³", "245", "Photoionization Detector (PID) isobutylene equivalent"),
                MonitoringFieldDef("hcho_measured", "Formaldehyde (HCHO)", "ppm", "0.035", "Electrochemical / spectrophotometric MBTH reading"),
                MonitoringFieldDef("indoor_temp", "Indoor Air Temperature", "°C", "23.2", "Comfort zone temperature"),
                MonitoringFieldDef("indoor_rh", "Relative Humidity", "%", "48.5", "Indoor hygrometer reading"),
                MonitoringFieldDef("room_volume", "Enclosed Zone Volume", "m³", "350", "Length × Width × Ceiling Height of occupied space")
            )

            PollutionMonitoringDomain.WORKZONE_AIR -> listOf(
                MonitoringFieldDef("pump_flow", "Personal Air Sampler Flow Rate", "L/min", "2.00", "Calibrated Gillian/SKC constant-flow personal pump"),
                MonitoringFieldDef("shift_hours", "Shift Sampling Duration", "hours", "8.0", "Total operator shift exposure time"),
                MonitoringFieldDef("cyclone_filter_gain", "Cassette Filter Mass Gain (ΔW)", "mg", "1.650", "Tare vs post-weigh difference for respirable fraction (d₅₀=4µm)"),
                MonitoringFieldDef("target_contaminant", "Contaminant Monitored", "text", "Respirable Silica & Particulate", "Chemical / Dust agent assessed against PEL/TLV", isNumeric = false),
                MonitoringFieldDef("wbgt_temp", "Wet Bulb Globe Temperature (WBGT)", "°C", "27.8", "Occupational thermal stress factor"),
                MonitoringFieldDef("workstation_id", "Shopfloor Location / Worker Tag", "text", "Welding & Sandblasting Bay 3", "Operator identification for OSHA compliance log", isNumeric = false)
            )

            PollutionMonitoringDomain.NOISE -> listOf(
                MonitoringFieldDef("leq_day", "Daytime Equivalent Sound Level (L_day)", "dBA", "68.4", "A-weighted energy-equivalent sound level (06:00 to 22:00)"),
                MonitoringFieldDef("leq_night", "Nighttime Equivalent Sound Level (L_night)", "dBA", "56.2", "A-weighted energy-equivalent sound level (22:00 to 06:00)"),
                MonitoringFieldDef("l_max", "Maximum Sound Level (L_max)", "dBA", "79.8", "Fast time-weighted peak level recorded during period"),
                MonitoringFieldDef("l_min", "Minimum Sound Level (L_min)", "dBA", "47.1", "Lowest ambient sound pressure level recorded"),
                MonitoringFieldDef("l_10", "Statistical Level L₁₀", "dBA", "71.5", "Level exceeded for 10% of sampling period (traffic peaks)"),
                MonitoringFieldDef("l_90", "Statistical Level L₉₀ (Background)", "dBA", "51.0", "Level exceeded for 90% of sampling period (ambient background)"),
                MonitoringFieldDef("distance_from_source", "Distance from Boundary / Source", "m", "15.0", "Measurement distance as specified in ISO 1996 survey protocol")
            )

            PollutionMonitoringDomain.STACK_EMISSION -> listOf(
                MonitoringFieldDef("velocity_head", "Average Pitot Velocity Head (ΔP)", "mmH2O", "16.8", "Type-S Pitot tube differential pressure across stack traverse points"),
                MonitoringFieldDef("pitot_cp", "Pitot Tube Coefficient (Cp)", "dimensionless", "0.84", "Wind-tunnel calibrated coefficient"),
                MonitoringFieldDef(
                    id = "stack_temp",
                    label = "Stack Flue Gas Temperature (Ts)",
                    unit = "°C",
                    defaultValue = "165.0",
                    description = "Flue gas thermocouple reading in duct",
                    isNumeric = true,
                    minPlausible = -50.0,
                    maxPlausible = 1200.0,
                    validationTooltip = "EPA Method 2 Section 4.3 Validation Criteria: Stack flue gas temperature (Ts) measured with calibrated Type-K/Type-S thermocouple. Physically plausible bounds: -50.0°C to 1,200.0°C (-58°F to 2,192°F). Typical industrial boiler exhaust ranges from 80°C to 450°C. Values < -50°C are physically implausible for exhaust gas; values > 1,200°C exceed uncooled probe thermocouple calibration limits."
                ),
                MonitoringFieldDef(
                    id = "stack_diameter",
                    label = "Internal Stack Duct Diameter (D)",
                    unit = "m",
                    defaultValue = "2.10",
                    description = "Inner duct dimension at sampling port elevation",
                    isNumeric = true,
                    minPlausible = 0.10,
                    maxPlausible = 15.0,
                    validationTooltip = "EPA Method 1 & 2 Section 11 Validation Criteria: Circular duct inside diameter (D) at sampling elevation. Physically plausible bounds: 0.10 m to 15.00 m (0.33 ft to 49.20 ft). Ducts < 0.10 m cannot physically accommodate standard Type-S Pitot traverse probes without aerodynamic wall blockage; stacks > 15 m exceed physical dimensions of industrial exhaust chimneys."
                ),
                MonitoringFieldDef("orifice_deltah", "Meter Orifice Differential (ΔH)", "mmH2O", "34.5", "Calibrated dual-inclined manometer reading"),
                MonitoringFieldDef("gas_meter_volume", "Dry Gas Meter Sampled Volume (Vm)", "m³", "1.450", "Actual volume registered on calibrated DGM"),
                MonitoringFieldDef("gas_meter_temp", "Average Dry Gas Meter Temperature (Tm)", "°C", "32.0", "Average of DGM inlet and outlet temperatures"),
                MonitoringFieldDef("filter_tare_wt", "Filter Tare Weight (W₁)", "mg", "482.10", "Thimble / quartz fiber filter conditioned dry weight"),
                MonitoringFieldDef("filter_gross_wt", "Filter Gross Weight (W₂)", "mg", "554.80", "Oven-dried 105°C filter particulate catch"),
                MonitoringFieldDef("moisture_collected", "Condensed Water in Impingers (Vlc)", "mL", "45.0", "Silica gel weight gain + condensate water")
            )

            PollutionMonitoringDomain.FLUE_GAS -> listOf(
                MonitoringFieldDef("o2_pct", "Flue Gas Oxygen (O₂)", "%", "4.8", "Paramagnetic / electrochemical dry basis concentration"),
                MonitoringFieldDef("co_ppm", "Carbon Monoxide (CO)", "ppm", "42.0", "NDIR / electrochemical flue gas reading"),
                MonitoringFieldDef("co2_pct", "Carbon Dioxide (CO₂)", "%", "12.4", "Direct NDIR concentration or stoichiometric carbon calculation"),
                MonitoringFieldDef("so2_ppm", "Sulfur Dioxide (SO₂)", "ppm", "125.0", "NDIR / pulsed UV flue gas concentration"),
                MonitoringFieldDef("nox_ppm", "Nitrogen Oxides (NOx as NO₂)", "ppm", "168.0", "Chemiluminescent / heated electrochemical reading"),
                MonitoringFieldDef("flue_temp", "Flue Gas Exhaust Temperature", "°C", "172.0", "Thermocouple probe at breeching point"),
                MonitoringFieldDef("ambient_air_temp", "Combustion Air Intake Temperature", "°C", "28.0", "Burner intake ambient temperature"),
                MonitoringFieldDef("fuel_type", "Primary Fuel Type", "text", "Bituminous Coal", "Combustion calculation matrix (Coal, Gas, Fuel Oil, Biomass)", isNumeric = false)
            )

            PollutionMonitoringDomain.PAINT_BOOTH -> listOf(
                MonitoringFieldDef("face_velocity", "Average Filter Face Velocity", "m/s", "0.52", "Thermal anemometer average across intake filter face (min 0.5 m/s crossdraft)"),
                MonitoringFieldDef("booth_area", "Cross-Sectional Booth Face Area", "m²", "14.5", "Width × Height of air intake cross section"),
                MonitoringFieldDef("voc_concentration", "Exhaust VOC Concentration (as Xylene)", "ppm", "18.5", "Flame Ionization Detector (FID) / photoionization reading"),
                MonitoringFieldDef("filter_delta_p", "Filter Bank Differential Pressure (ΔP)", "Pa", "145.0", "Magnehelic gauge reading across dry arrestor filters"),
                MonitoringFieldDef("overspray_rate", "Coating Consumption Application Rate", "kg/h", "12.5", "Fluid delivery rate to paint spray guns"),
                MonitoringFieldDef("solids_pct", "Paint Volatile Solvent Fraction", "%", "42.0", "Solvent portion volatilizing during spray operations")
            )

            PollutionMonitoringDomain.AIR_MICROBIOLOGY -> listOf(
                MonitoringFieldDef("sampler_flow", "Impactor Sampler Flow Rate", "L/min", "28.3", "Standard 1.0 CFM calibrated flow through 400 precision orifices"),
                MonitoringFieldDef("sampling_time", "Impaction Duration", "min", "15.0", "Agar exposure interval (typically 5 to 15 minutes)"),
                MonitoringFieldDef("raw_colony_count", "Observed Agar Colony Count (r)", "CFU", "34", "Uncorrected visible colony forming units after incubation"),
                MonitoringFieldDef("impactor_holes", "Sieve Stage Orifice Count (N)", "holes", "400", "Andersen stage hole matrix (standard 400 holes)"),
                MonitoringFieldDef("incubation_temp", "Incubation Temperature", "°C", "35.0", "Standard 35°C for Total Bacterial Count, 25°C for Fungi/Yeast"),
                MonitoringFieldDef("incubation_hours", "Incubation Period", "hours", "48.0", "48h for bacteria (SDA/TSA agar), 120h for mould")
            )

            PollutionMonitoringDomain.CLEAN_ROOM -> listOf(
                MonitoringFieldDef("particles_05", "Particle Count ≥ 0.5 µm (raw)", "counts", "1240", "Optical particle counter cumulative count in 1 m³ sample"),
                MonitoringFieldDef("particles_10", "Particle Count ≥ 1.0 µm (raw)", "counts", "320", "Particle counter cumulative count in 1 m³ sample"),
                MonitoringFieldDef("particles_50", "Particle Count ≥ 5.0 µm (raw)", "counts", "18", "Particle counter cumulative count in 1 m³ sample"),
                MonitoringFieldDef("sample_volume_liters", "Sample Volume per Location", "Liters", "1000", "ISO 14644-1:2015 minimum sample volume per point"),
                MonitoringFieldDef("room_floor_area", "Cleanroom Net Floor Area", "m²", "64.0", "Area determining minimum sampling locations: N_L = ceil(sqrt(A))"),
                MonitoringFieldDef("airflow_velocity", "HEPA Unidirectional Air Velocity", "m/s", "0.45", "Laminar airflow velocity below terminal HEPA ceiling filters")
            )

            PollutionMonitoringDomain.LUX_MONITORING -> listOf(
                MonitoringFieldDef("e_center", "Center Working Plane Illuminance", "Lux", "520", "CIE photopic lux meter reading at primary workstation"),
                MonitoringFieldDef("e_point_1", "Quadrant 1 Perimeter Reading", "Lux", "460", "North-East grid measurement point"),
                MonitoringFieldDef("e_point_2", "Quadrant 2 Perimeter Reading", "Lux", "490", "South-East grid measurement point"),
                MonitoringFieldDef("e_point_3", "Quadrant 3 Perimeter Reading", "Lux", "430", "South-West grid measurement point"),
                MonitoringFieldDef("e_point_4", "Quadrant 4 Perimeter Reading", "Lux", "475", "North-West grid measurement point"),
                MonitoringFieldDef("workplane_height", "Working Plane Elevation", "m", "0.85", "Standard desk/benchtop working plane height (typically 0.75-0.85m)"),
                MonitoringFieldDef("daylight_available", "Daylight Penetration Contribution", "%", "15.0", "Percentage of natural daylight component during survey")
            )

            PollutionMonitoringDomain.WATER_MONITORING -> listOf(
                MonitoringFieldDef("water_ph", "Hydrogen Ion Activity (pH)", "pH units", "7.35", "Standard glass electrode pH reading calibrated with pH 4.01, 7.00, 10.01"),
                MonitoringFieldDef("dissolved_oxygen", "Dissolved Oxygen (DO)", "mg/L", "6.80", "Optical luminescent / polarographic membrane probe"),
                MonitoringFieldDef("turbidity_ntu", "Nephelometric Turbidity", "NTU", "3.20", "ISO 7027 90° scattered light nephelometer"),
                MonitoringFieldDef("conductivity_us", "Electrical Conductivity (EC)", "µS/cm", "480", "Temperature-compensated (25°C) 4-electrode conductivity cell"),
                MonitoringFieldDef("bod_5", "Biochemical Oxygen Demand (BOD₅)", "mg/L", "18.5", "5-day 20°C standard incubation depletion test"),
                MonitoringFieldDef("cod_cr", "Chemical Oxygen Demand (COD)", "mg/L", "46.0", "Closed reflux titrimetric potassium dichromate digestion"),
                MonitoringFieldDef("total_suspended_solids", "Total Suspended Solids (TSS)", "mg/L", "22.0", "Gravimetric 0.45 µm glass-fiber dried at 105°C"),
                MonitoringFieldDef("total_dissolved_solids", "Total Dissolved Solids (TDS)", "mg/L", "310.0", "Gravimetric 180°C filtrate / conductivity factor"),
                MonitoringFieldDef("heavy_metals_lead", "Heavy Metal Lead (Pb)", "mg/L", "0.004", "Inductively Coupled Plasma (ICP-MS) / AAS hydride reading")
            )
        }
    }

    /**
     * Complete Standard Operating Procedure (SOP) manual for each monitoring domain
     */
    fun getStandardProcedure(domain: PollutionMonitoringDomain): StandardProcedureDetails {
        return when (domain) {
            PollutionMonitoringDomain.AMBIENT_AIR -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Ambient Air Particulate Sampling (PM2.5 / PM10)",
                regulatoryCode = "USEPA 40 CFR Part 50 App. L / ISO 12341:2014 / IS 5182 (Part 23 & 24) / AS/NZS 3580.9.9",
                scope = "Determination of particulate matter PM2.5 and PM10 in ambient outdoor air using low-volume gravimetric samplers.",
                preSamplingProtocol = listOf(
                    "Inspect 47mm PTFE or Quartz fiber filters under grazing illumination for pinholes, tears, or creases.",
                    "Equilibrate filters in a temperature (20.0 ± 1.0°C) and humidity (35.0 ± 5.0% RH) controlled desiccator weighing chamber for at least 24 hours.",
                    "Perform dual weighings on a certified 6-figure microbalance (resolution 0.001 mg); replicate tare weights must agree within ±0.005 mg.",
                    "Store conditioned filters in sealed antistatic petri-dish slides labeled with barcoded UID."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Mount the ambient air sampler head 2.0 to 4.0 meters above local ground level, clear of aerodynamic obstructions (at least 20m from tree dripline).",
                    "Install the size-selective PM2.5 impactor with fresh silicone oil coating on the impaction plate.",
                    "Perform mandatory initial leak check: seal inlet with calibrated plug; flow must drop below 0.1 L/min within 30 seconds.",
                    "Verify electronic mass flow controller calibration using a NIST-traceable primary flow standard (e.g. Bios DryCal)."
                ),
                samplingExecutionProtocol = listOf(
                    "Record initial flow rate (Q₁), ambient temperature, barometric pressure, and timer hour meter reading.",
                    "Run continuous sampling for designated duration: Spot (15-min screening), 1-Hour (peak industrial surveillance), 8-Hour (sub-diurnal), or 24-Hour (diurnal regulatory composite).",
                    "Record hourly flow rate deviations; total volumetric flow must stay within ±5% of 16.67 L/min nominal rate.",
                    "At sampling termination, record final flow rate (Q₂), elapsed time (min), and ambient meteorological logs."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Using Teflon-coated tweezers, remove the exposed filter from the sampling cassette inside a clean transport box.",
                    "Place filter back into barcoded petri-slide, seal with Parafilm, and transport to laboratory in a chilled cooler (< 4°C).",
                    "Re-equilibrate in climate chamber (20°C, 35% RH) for 24 hours before post-weighing on the same microbalance."
                ),
                mathCalculationFormula = "C = (W₂ - W₁) × 10⁶ / V_std  |  V_std = V_actual × (P_a / 760) × (298.15 / (273.15 + T_a))"
            )

            PollutionMonitoringDomain.INDOOR_AIR -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Indoor Air Quality (IAQ) & Ventilation Assessment",
                regulatoryCode = "ASHRAE Standard 62.1 / ISO 16000 Parts 1-6 / EN 16798-1 / GB/T 18883 / IS 16650",
                scope = "Evaluation of indoor air thermal comfort, ventilation effectiveness (Air Changes per Hour ACH), and hazardous contaminants (CO2, TVOC, HCHO).",
                preSamplingProtocol = listOf(
                    "Calibrate photoionization detector (PID) with 100 ppm isobutylene calibration gas; calibrate NDIR CO2 sensor with 1000 ppm certified span gas.",
                    "Ensure indoor doors and windows have remained closed in normal operational state for at least 12 hours prior to testing.",
                    "Identify representative breathing zone sampling stations (1.1m to 1.7m above finished floor, away from direct human breath plume and HVAC supply diffusers)."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Place multi-parameter indoor environmental monitor on a tripod at the center of the occupied room.",
                    "Allow 15 minutes warm-up time for NDIR and electrochemical sensors to reach thermal stabilization.",
                    "Measure outdoor reference air baseline levels simultaneously to calculate indoor-outdoor gradient."
                ),
                samplingExecutionProtocol = listOf(
                    "Continuous data logging across required timing modes: Spot (15-min walk-through), 1-Hour peak occupancy check, or 8-Hour workshift monitoring.",
                    "Log CO2 (ppm), TVOC (µg/m³), Formaldehyde (ppm), Temperature (°C), and Relative Humidity (%) at 1-minute intervals.",
                    "Execute CO2 tracer gas decay test or mass balance equation to calculate actual Air Changes per Hour (ACH)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "If Tenax TA sorbent tubes or DNPH cartridges were collected for thermal desorption GC-MS / HPLC, seal immediately in glass jars with activated carbon.",
                    "Transport cartridges refrigerated (< 4°C) to accredited analytical lab within 7 days."
                ),
                mathCalculationFormula = "ACH = (60 / t) × ln((C₀ - C_ambient) / (C_t - C_ambient))  |  ΔCO₂ = CO₂_indoor - CO₂_ambient"
            )

            PollutionMonitoringDomain.WORKZONE_AIR -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Occupational Hygiene Workzone Air Quality Monitoring",
                regulatoryCode = "OSHA Technical Manual (OTM) Section II / NIOSH Manual of Analytical Methods (NMAM) / BS EN 689 / ISO 13137",
                scope = "Assessment of chemical vapors, hazardous dusts, and respirable silica exposure in industrial worker breathing zones.",
                preSamplingProtocol = listOf(
                    "Inspect personal sampling pump diaphragms, battery voltage, and electronic pulsator damping.",
                    "Calibrate personal sampling pump with a primary calibrator (Bios Defender/DryCal) with representative sampling cassette inline at 2.0 L/min ± 2%.",
                    "Condition 37mm PVC membrane filters for gravimetric particulate sampling (tare weighed to 0.001 mg)."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Attach sampling cyclone (e.g. Higgins-Dewell or Dorr-Oliver 4µm cut-point) securely to the worker's lapel within the 30cm hemispherical breathing zone.",
                    "Position flexible Tygon tubing along worker's back to prevent snagging during industrial movement.",
                    "Perform initial flow verification; zero electrochemical multi-gas sensor in certified zero air."
                ),
                samplingExecutionProtocol = listOf(
                    "Start pump simultaneously with start of worker shift; log worker activity, ventilation hood status, and PPE usage.",
                    "Conduct periodic spot audits every 2 hours to confirm cyclone orientation (upright) and flow rate integrity.",
                    "Record total elapsed time (min) at the conclusion of the 8-hour shift or designated occupational task."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Remove cassette immediately after pump shut-off; cap inlet and outlet with red vinyl end-caps.",
                    "Include field blank cassette (opened for 10 seconds at site, not exposed to flow) per 10 active samples.",
                    "Ship upright in padded transport case to industrial hygiene laboratory for gravimetric / XRD analysis."
                ),
                mathCalculationFormula = "TWA_8hr = Σ(C_i × T_i) / 480 min  |  C_mg/m3 = (ΔW_mg / (Q_L/min × Time_min)) × 1000"
            )

            PollutionMonitoringDomain.NOISE -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Environmental & Industrial Noise Survey",
                regulatoryCode = "ISO 1996-1 & 2 / IEC 61672-1 Class 1 / BS 4142 / EPA Levels Document / Indian CPCB Noise Rules / AS/NZS 2107",
                scope = "Quantification of ambient sound pressure levels, equivalent continuous sound level (Leq), statistical metrics (L10, L90), and Day-Night Ldn.",
                preSamplingProtocol = listOf(
                    "Equip Type 1 / Class 1 Sound Level Meter with a 1/2-inch free-field prepolarized condenser microphone and windscreen.",
                    "Verify battery voltage and clear internal micro-SD data memory.",
                    "Perform acoustic field calibration using a certified Class 1 sound calibrator (94.0 dB ± 0.2 dB at 1000 Hz); adjust instrument calibration trim if deviation exceeds ±0.1 dB."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Mount sound level meter on a tripod at a height of 1.2 to 1.5 meters above ground level.",
                    "Orient microphone toward sound source or at grazing incidence as required by survey protocol.",
                    "Maintain a minimum clearance of 3.5 meters from sound-reflecting walls, buildings, or large reflective barriers.",
                    "Ensure local wind speed is below 5.0 m/s (measure with vane anemometer); reject data during rain or heavy precipitation."
                ),
                samplingExecutionProtocol = listOf(
                    "Select frequency weighting 'A' and dynamic time weighting 'Fast' (125 ms) or 'Slow' (1000 ms) per regulatory standard.",
                    "Initiate continuous integration for the scheduled duration: Spot (15-min survey), 1-Hour (peak traffic), or 24-Hour (diurnal Day-Night assessment).",
                    "Continuously log broadband Leq, Lmax, Lmin, L10, L50, L90, and 1/3-octave band spectra.",
                    "Annotate extraneous acoustic events (aircraft overflights, emergency sirens, construction blasts) in field log."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Perform post-survey acoustic calibration verification (must agree within ±0.3 dB of initial calibration).",
                    "Export timestamped acoustic time-history logs (.wav and .csv format) to secure server."
                ),
                mathCalculationFormula = "L_dn = 10 log₁₀ [ (15/24) 10^(L_day/10) + (9/24) 10^((L_night + 10)/10) ]  |  L_corr = 10 log₁₀(10^(L_meas/10) - 10^(L_bg/10))"
            )

            PollutionMonitoringDomain.STACK_EMISSION -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Isokinetic Stack Particulate Sampling & Emission Rate",
                regulatoryCode = "USEPA 40 CFR Part 60 App. A Method 5 / ISO 9096:2017 / BS EN 13284-1 / IS 11255 (Part 1)",
                scope = "Determination of particulate matter mass concentration and emission rate from stationary industrial duct/chimney stacks.",
                preSamplingProtocol = listOf(
                    "Condition quartz fiber thimbles / 82mm filters at 105°C for 2 hours, desiccate for 2 hours, and weigh on 5-decimal analytical balance.",
                    "Inspect Type-S Pitot tube, stack thermocouple, and borosilicate sampling nozzle for physical nicking or deformation.",
                    "Determine stack duct internal diameter, number of sampling traverse ports (minimum 2 ports at 90°), and traverse points per EPA Method 1 (at least 8 diameters downstream and 2 diameters upstream of flow disturbance)."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Assemble the sampling train: calibrated buttonhook nozzle, heated probe liner (120 ± 14°C), heated filter holder (120 ± 14°C), chilled impinger ice bath (4 impingers: 2 with 100mL H2O, 1 dry, 1 with 200g silica gel), umbilical line, and dry gas meter control console.",
                    "Perform pre-test vacuum leak check: pull 15 in. Hg (380 mmHg) vacuum; leakage rate must not exceed 0.00057 m³/min (0.02 CFM) or 4% of average sampling rate.",
                    "Perform Pitot line blowback leak check: pressurize Pitot line to 3 in. H2O; manometer must remain stable for 15 seconds."
                ),
                samplingExecutionProtocol = listOf(
                    "Insert probe into stack port to first traverse point; align nozzle directly into gas stream.",
                    "At each point, read velocity head (ΔP), calculate required orifice pressure differential (ΔH) for exact isokinetic flow rate, adjust control valve, and sample for equal time interval (e.g. 5 minutes per point across 12 traverse points = 60 min total).",
                    "Continuously maintain probe and filter oven at 120°C to prevent moisture condensation on filter substrate.",
                    "At sampling completion, perform post-test leak check at maximum vacuum observed during test run."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Carefully recover filter into Petri slide container; quantitatively brush and acetone-rinse probe nozzle, liner, and front half of filter holder into clean glass container (Container 2).",
                    "Measure volumetric water condensate in impingers (nearest 1.0 mL) and weigh silica gel (nearest 0.5 g) for flue gas moisture calculation.",
                    "Evaporate acetone rinse in fume hood at ambient temperature; dry filter at 105°C; weigh to determine total particulate catch (Mn)."
                ),
                mathCalculationFormula = "v_s = K_p C_p √(ΔP · T_s / (P_s · M_s))  |  C_s = M_n / V_m(std)  |  E = C_s × Q_std × 10⁻⁶ kg/h  |  Isokinetic I = 90% to 110%"
            )

            PollutionMonitoringDomain.FLUE_GAS -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Combustion Flue Gas Analysis & Efficiency",
                regulatoryCode = "USEPA Method 3A/6C/7E / BS EN 15267 / EU MCPD 2015/2193 / GB 13271 / CPCB Norms",
                scope = "Continuous measurement of O2, CO, CO2, SO2, NOx in combustion exhaust and calculation of excess air, combustion efficiency, and reference O2 normalized concentrations.",
                preSamplingProtocol = listOf(
                    "Inspect multi-gas analyzer probe, heated sample line, thermoelectric gas chiller (condensate separator), and PTFE inline particulate filter.",
                    "Perform zero calibration using certified Zero Nitrogen (99.999% N2).",
                    "Perform span calibration using certified protocol cylinder gases for O2 (ambient 20.9%), CO (200 ppm), SO2 (200 ppm), and NO (200 ppm); span drift must be < ±2% of span."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Insert stainless steel / Inconel sampling probe into duct at centroid of flue gas stream.",
                    "Verify airtight seal at sampling flange using high-temperature silicone cone or packing gland.",
                    "Perform sampling line vacuum leak check: cap probe tip; flow rate must drop to zero on rotameter within 10 seconds."
                ),
                samplingExecutionProtocol = listOf(
                    "Ensure thermoelectric cooler cools gas below 4°C to strip all moisture prior to electrochemical / NDIR sensor cells.",
                    "Log gas concentrations, stack temperature, and intake ambient air temperature continuously.",
                    "Execute measurements across required duration: Spot (15-min steady-state), 1-Hour average combustion cycle, or 8-Hour continuous operation.",
                    "Calculate Excess Air factor (λ), combustion losses via Siegert formula, and normalized emissions."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Flush analyzer with fresh ambient air for 10 minutes until O2 returns to 20.9% and CO/SO2/NOx drop to zero before powering down.",
                    "Drain condensate water reservoir and verify filter dryness."
                ),
                mathCalculationFormula = "Excess Air λ = 20.9 / (20.9 - O₂)  |  C_ref = C_meas × (20.9 - O₂_ref) / (20.9 - O₂_meas)  |  η = 100 - Siegert_losses"
            )

            PollutionMonitoringDomain.PAINT_BOOTH -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Paint Booth Ventilation, Capture Velocity & VOC Emission",
                regulatoryCode = "NFPA 33 Standard for Spray Application / OSHA 1910.107 / EPA Method 25A / EU SED 1999/13/EC / IS 14444",
                scope = "Validation of spray booth airflow capture velocity, filter resistance (ΔP), flammable vapor % LEL safety, and VOC mass discharge.",
                preSamplingProtocol = listOf(
                    "Inspect booth intake pre-filters, exhaust dry overspray fiberglass arrestors, and paint sludge water curtain (if wet scrubber type).",
                    "Calibrate thermal hot-wire anemometer against wind-tunnel standard.",
                    "Zero and span Flame Ionization Detector (FID) or PID organic vapor analyzer with certified propane/toluene gas standards."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Subdivide paint booth face opening into equal grid areas (maximum 0.5m × 0.5m per grid zone per ACGIH guidelines).",
                    "Ensure spray guns are non-operational during baseline capture velocity measurement.",
                    "Inspect Magnehelic differential pressure gauge lines for dust clogs or kinks."
                ),
                samplingExecutionProtocol = listOf(
                    "Measure linear face air velocity at centroid of each grid point; calculate average face velocity across cross-draft (min 0.5 m/s) or downdraft (min 0.25 m/s) booth.",
                    "Initiate spray painting simulation with typical solvent coatings; monitor VOC concentration in exhaust duct.",
                    "Confirm VOC concentration remains strictly below 25% of the Lower Explosive Limit (LEL) for solvent mixture.",
                    "Calculate volumetric exhaust ventilation flow (Q) and total hourly VOC mass discharge rate."
                ),
                sampleHandlingAndPreservation = listOf(
                    "If charcoal sorbent tubes (SKC Anasorb CSC) were sampled for speciated VOC analysis via GC-FID, cap tubes, wrap in foil, and refrigerate (< 4°C).",
                    "Document booth differential pressure across filter bank to track replacement threshold (typically > 250 Pa)."
                ),
                mathCalculationFormula = "Q = v_avg × Area × 3600 m³/h  |  VOC_kg/h = Q × VOC_mg/m³ × 10⁻⁶  |  % LEL = (VOC_ppm / LEL_ppm) × 100%"
            )

            PollutionMonitoringDomain.AIR_MICROBIOLOGY -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Bioaerosol Sieve Impactor Sampling & Viable CFU Analysis",
                regulatoryCode = "ISO 14698-1 & 2 / USP <797> & <1116> / WHO Bioaerosol Guidelines / EU GMP Annex 1 / ACGIH Bioaerosols",
                scope = "Quantification of viable airborne bacteria, fungi, and actinomycetes in cleanrooms, pharmaceutical plants, and indoor hospital environments.",
                preSamplingProtocol = listOf(
                    "Prepare sterile 90mm Petri dishes containing Tryptic Soy Agar (TSA) with lecithin & polysorbate 80 (for bacteria) and Sabouraud Dextrose Agar (SDA) with chloramphenicol (for fungi).",
                    "Autoclave Andersen sieve impactor stages and sampling head at 121°C for 20 minutes; allow to cool in laminar flow hood.",
                    "Swab impactor interior with 70% sterile isopropyl alcohol (IPA) between sampling runs; allow IPA to fully evaporate."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Mount biological cascade sieve impactor on tripod at height of 1.0 to 1.5 meters (breathing zone / process height).",
                    "Calibrate vacuum suction pump to precisely 28.3 L/min (1.0 CFM) using a calibrated mass flow meter or rotameter.",
                    "Using sterile aseptic technique (gloves, mask), unwrap agar plate, position on impactor stage, and secure sieve cover."
                ),
                samplingExecutionProtocol = listOf(
                    "Operate impactor for scheduled duration: Spot (2 to 5 minutes in high bioburden zones), 15 minutes (standard cleanroom sample = 424.5 Liters), or sequential interval runs for shift monitoring.",
                    "Aerosol particles accelerate through the 400 precision orifices and impact directly onto the nutrient agar surface.",
                    "Carefully remove plate, replace Petri lid, and seal perimeter with sterile laboratory tape; label with location, date, flow, and run ID."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Transport plates inverted in insulated containers to microbiology laboratory within 2 hours.",
                    "Incubate bacterial TSA plates at 30°C to 35°C for 48 hours; incubate fungal SDA plates at 20°C to 25°C for 5 to 7 days.",
                    "Count visible colonies; apply Feller Positive-Hole Statistical Correction for multiple particles traversing the same orifice."
                ),
                mathCalculationFormula = "Pr = N × [1/N + 1/(N-1) + ... + 1/(N-r+1)]  |  CFU/m³ = (Pr × 1000) / (Flow_L/min × Time_min)"
            )

            PollutionMonitoringDomain.CLEAN_ROOM -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Airborne Particulate Cleanroom Classification & Validation",
                regulatoryCode = "ISO 14644-1:2015 / ISO 14644-2:2015 / EU GMP Annex 1 (2022) / US FDA Aseptic Processing Guide / WHO GMP",
                scope = "Classification of cleanroom airborne particulate cleanliness (Classes ISO 1 through ISO 9) and EU GMP Grades (A, B, C, D) at rest and in operation.",
                preSamplingProtocol = listOf(
                    "Verify cleanroom HVAC system has been running uninterrupted with stable temperature, relative humidity, and positive room pressurization (typically +10 to +15 Pa) for at least 24 hours.",
                    "Confirm HEPA/ULPA filter integrity testing (DOP/PAO aerosol challenge < 0.01% leakage) has been certified within 12 months.",
                    "Calculate minimum number of sampling locations: N_L = ceil(sqrt(Area_m²)) as required by ISO 14644-1:2015 Table A.1."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Employ a certified discrete airborne Optical Particle Counter (OPC) calibrated per ISO 21501-4 with flow rate of 28.3 L/min (1.0 CFM) or 50.0 L/min.",
                    "Perform zero-count test: attach zero-count absolute HEPA filter to inlet nozzle; counter must register ≤ 1 count per 5 minutes for particles ≥ 0.5 µm.",
                    "Position isokinetic sampling inlet probe facing directly into the unidirectional airflow stream (or vertical in turbulent rooms) at working plane height."
                ),
                samplingExecutionProtocol = listOf(
                    "Sample minimum volume V_s per location: V_s = (20 / C_limit) × 1000 Liters, with an absolute minimum of 2.0 Liters (minimum 1000 Liters for Grade A/ISO 5).",
                    "Record cumulative counts at size channels: ≥ 0.1 µm, ≥ 0.3 µm, ≥ 0.5 µm, ≥ 1.0 µm, and ≥ 5.0 µm.",
                    "Calculate average particle concentration (particles/m³) at each sampling point across the facility grid.",
                    "Determine ISO cleanliness class limit: C_max = 10^N × (0.1 / D)^2.08, where N is ISO class number and D is particle diameter (µm)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Review 95% Upper Confidence Limit (UCL) for rooms with 2 to 9 sampling locations.",
                    "Generate formal Cleanroom Validation Certificate detailing operational state: 'As-built', 'At-rest', or 'In-operation'."
                ),
                mathCalculationFormula = "C_max = 10^N × (0.1 / D)^2.08  |  N_L = ⌈√Area⌉  |  UCL_95% = Mean + t_0.95 × (StdDev / √m)"
            )

            PollutionMonitoringDomain.LUX_MONITORING -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Workplace & Industrial Illumination / Lux Survey",
                regulatoryCode = "ISO 8995-1:2002 (CIE S 008/E:2001) / EN 12464-1 / IESNA Lighting Handbook / IS 3646 / AS/NZS 1680 / GB 50034",
                scope = "Measurement of interior lighting levels, average maintained illuminance (Em), illuminance uniformity ratio (U0 = Emin / Eavg), and glare mitigation.",
                preSamplingProtocol = listOf(
                    "Ensure all artificial light fixtures (LED, fluorescent, high-bay HID) have been powered on and illuminated for at least 20 minutes (1 hour for discharge lamps) for thermal stabilization.",
                    "Confirm photometer lux sensor is clean, free of dust scratches, and has a valid factory calibration certificate traceable to NIST / PTB.",
                    "Establish survey grid points per EN 12464-1 based on room geometry (grid spacing p = 0.2 × 5^(log10 d))."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Employ a cosine-corrected and color-corrected (CIE photopic V(λ) curve) Class A or Class B digital lux meter (DIN 5032).",
                    "Mount sensor on a horizontal photometer stand at standard working plane elevation (0.85m above floor, or floor level for corridors/stairwells).",
                    "Surveyor must stand away from sensor and avoid casting shadows or wearing highly reflective white lab coats during measurement."
                ),
                samplingExecutionProtocol = listOf(
                    "Conduct measurements during nighttime or with window blinds closed to isolate electric lighting contribution, or measure total daylight + electric illumination as required.",
                    "Record illuminance readings (Lux) at center, perimeter zones, and individual task desks.",
                    "Calculate Average Illuminance: E_avg = (1/n) × Σ E_i.",
                    "Calculate Illuminance Uniformity: U₀ = E_min / E_avg (standard requires U₀ ≥ 0.4 to 0.7 for comfortable sustained visual tasks)."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Cross-reference calculated E_avg against regulatory task requirements (e.g. 500 Lux for general offices, 750 Lux for technical drawing, 200 Lux for warehouses).",
                    "Map lux distribution contours to detect dark spots or excessive luminance contrast causing eye strain."
                ),
                mathCalculationFormula = "E_avg = (1/n) × Σ E_i  |  Uniformity U₀ = E_min / E_avg  |  Diversity Ratio = E_min / E_max"
            )

            PollutionMonitoringDomain.WATER_MONITORING -> StandardProcedureDetails(
                standardTitle = "Standard Operating Procedure for Physicochemical & Industrial Wastewater Quality Monitoring",
                regulatoryCode = "ISO 5667 Water Sampling Series / APHA Standard Methods for Examination of Water & Wastewater (24th Ed.) / USEPA 40 CFR 136 / IS 3025 / GB 3838 / CONAMA 357",
                scope = "Field sampling, on-site telemetry, and analytical quantification of physicochemical, biochemical, and toxicological parameters in water and effluents.",
                preSamplingProtocol = listOf(
                    "Calibrate field pH meter with fresh NIST buffers (pH 4.01, 7.00, 10.01) with temperature slope compensation.",
                    "Calibrate optical dissolved oxygen probe in 100% water-saturated air chamber at ambient barometric pressure.",
                    "Calibrate nephelometric turbidity meter with primary Formazin standards (0.1, 20, 100, 800 NTU).",
                    "Decontaminate sampling bailers/bottles: wash with non-phosphate detergent, rinse with 1:1 HNO3 for metals, followed by deionized water rinse."
                ),
                instrumentSetupAndLeakCheck = listOf(
                    "Collect representative composite or grab water sample from centroid of river/effluent stream, avoiding surface scum and bottom sediment.",
                    "Rinse sample collection bucket three times with target water prior to sample gathering.",
                    "Immediately measure unstable field parameters: Temperature, pH, Dissolved Oxygen (DO), Conductivity (EC), and Turbidity."
                ),
                samplingExecutionProtocol = listOf(
                    "Fill BOD bottles (300 mL glass with ground-glass stoppers) taking care to exclude all air bubbles; incubate at 20°C for 5 days.",
                    "Preserve heavy metal aliquots (Pb, Cr, Cd, Cu, Ni) by acidifying immediately with trace-metal grade HNO3 to pH < 2.0.",
                    "Preserve COD and ammonia nitrogen aliquots with concentrated H2SO4 to pH < 2.0 and refrigerate (< 4°C).",
                    "Measure Total Suspended Solids (TSS) by filtering 1000 mL sample through dried 0.45 µm glass-fiber filter followed by drying at 105°C."
                ),
                sampleHandlingAndPreservation = listOf(
                    "Package sample bottles in sealed polyethylene bags inside insulated ice chests with frozen gel packs (< 4°C).",
                    "Ensure Chain of Custody (CoC) documents accompany samples to ISO 17025 accredited laboratory within maximum holding times (24h for BOD, 6 months for metals)."
                ),
                mathCalculationFormula = "BOD₅ = (DO_initial - DO_day5) × Dilution_factor  |  WQI = Σ(W_i × q_i) / Σ W_i  |  DO% = (DO_meas / DO_sat) × 100%"
            )
        }
    }

    /**
     * Certified Instrument Manual, Working Principle, and Specifications for each domain
     */
    fun getInstrumentManual(domain: PollutionMonitoringDomain): InstrumentManualDetails {
        return when (domain) {
            PollutionMonitoringDomain.AMBIENT_AIR -> InstrumentManualDetails(
                instrumentName = "High-Precision PM2.5/PM10 Ambient Air Sampler & Beta-Gauge Monitor",
                makeAndModel = "Thermo Fisher Partisol 2025i / Met One BAM-1020 / Tisch Enviro-Sampler",
                operatingPrinciple = "Aerodynamic Impaction & Gravimetric Beta Attenuation: Sample air is drawn through an omni-directional inlet at 16.67 L/min (1 m³/h). A greased impaction stage separates particles larger than aerodynamic diameter 2.5 µm. Sub-2.5 µm particulates are collected onto a 47mm filter tape where C-14 beta ray attenuation measures true dry mass.",
                measurementRange = "0.0 to 10,000.0 µg/m³ PM2.5 / PM10 (Accuracy: ± 2.0 µg/m³; Lower Detection Limit: 0.1 µg/m³)",
                accuracyAndResolution = "Resolution: 0.1 µg/m³; Volumetric Flow Accuracy: ± 1% of setpoint via electronic mass flow controller",
                calibrationProtocol = listOf(
                    "Perform monthly 3-point calibration of mass flow controller against NIST-traceable Bios DryCal standard.",
                    "Perform temperature sensor 2-point calibration in water ice bath (0°C) and ambient certified thermometer.",
                    "Perform barometric pressure transducer calibration against certified mercurial or digital barometer standard."
                ),
                zeroAndSpanProcedure = listOf(
                    "Zero Check: Insert HEPA zero-filter assembly onto sample inlet for 4 hours; mean recorded concentration must be 0.0 ± 1.5 µg/m³.",
                    "Span Check: Insert calibrated standard zero and mass attenuation foil sheets of known areal density (approx 1.2 mg/cm²); verified response must be within ±2% of certified value."
                ),
                maintenanceAndServicing = listOf(
                    "Clean PM2.5 impactor well and re-coat impaction plate with 0.5 mL pure silicone oil every 14 days.",
                    "Replace sample intake internal O-rings every 6 months to guarantee vacuum integrity.",
                    "Service rotary vane vacuum pump and replace carbon vanes every 10,000 operational hours."
                ),
                operatingEnvironmentLimits = "Ambient Temperature: -30°C to +50°C; Relative Humidity: 0% to 100% non-condensing; Weatherproof NEMA 4X / IP65 enclosure."
            )

            PollutionMonitoringDomain.INDOOR_AIR -> InstrumentManualDetails(
                instrumentName = "Multi-Gas IAQ Monitor & Photoionization Detector (PID)",
                makeAndModel = "TSI Q-Trak XP 7585 / GrayWolf AdvancedSense Pro IAQ / Ion Science Tiger",
                operatingPrinciple = "Non-Dispersive Infrared (NDIR) for CO2: Infrared light at 4.26 µm wavelength is absorbed by CO2 molecules; attenuation follows Beer-Lambert law. 10.6 eV UV Lamp Photoionization (PID) ionizes volatile organic compounds (TVOCs) into electron-ion pairs collected at internal electrometer plates.",
                measurementRange = "CO2: 0 to 10,000 ppm; TVOC: 0.001 to 20,000 ppm; HCHO: 0 to 5.0 ppm; Temp: -10°C to +60°C; RH: 5% to 95%",
                accuracyAndResolution = "CO2: ± (2% of reading + 50 ppm); TVOC: ± 3% of reading; HCHO: ± 0.005 ppm; Temp: ± 0.3°C; RH: ± 2.0%",
                calibrationProtocol = listOf(
                    "Perform 2-point CO2 calibration using certified 99.999% Zero Nitrogen and 1000 ppm balance air calibration gas.",
                    "Perform PID VOC calibration using 100 ppm Isobutylene span cylinder with PTFE demand flow regulator.",
                    "Perform humidity calibration in sealed calibration chamber with saturated lithium chloride (11.3% RH) and sodium chloride (75.3% RH) salts."
                ),
                zeroAndSpanProcedure = listOf(
                    "Zero Calibration: Connect Zero Carbon filter / pure N2 bag to inlet; wait 180 seconds until stable; execute Zero Command on touchscreen.",
                    "Span Calibration: Introduce 100 ppm isobutylene; verify display reads 100.0 ± 1.0 ppm equivalent."
                ),
                maintenanceAndServicing = listOf(
                    "Clean PID 10.6 eV UV lamp window with HPLC-grade anhydrous methanol using lint-free cotton swab every 100 operating hours.",
                    "Replace inlet PTFE 0.45 µm particulate filter disk whenever discolored or flow drop exceeds 10%.",
                    "Recalibrate electrochemical HCHO and CO sensors annually at factory service center."
                ),
                operatingEnvironmentLimits = "Temperature: 0°C to +50°C; Relative Humidity: 10% to 90% non-condensing; Storage: -20°C to +60°C."
            )

            PollutionMonitoringDomain.WORKZONE_AIR -> InstrumentManualDetails(
                instrumentName = "Programmable Constant-Flow Personal Sampling Pump & Cyclone Head",
                makeAndModel = "SKC AirChek Touch / Gilian GilAir Plus / Casella Apex2 Pro",
                operatingPrinciple = "Microprocessor-Controlled Diaphragm Pumping: Dual-head diaphragm pump with electronic internal flow sensor and automatic backpressure compensation maintains constant volumetric flow (0.02 to 5.0 L/min) within ± 2% up to 50 inches H2O backpressure. GS-3 or HD cyclone utilizes centrifugal sedimentation to separate non-respirable particles, transmitting respirable dust (50% cut point at 4.0 µm) onto 37mm filter.",
                measurementRange = "Flow Range: 0.020 to 5.000 L/min; Cumulative Volume: 0 to 99,999 Liters; Run Time: up to 24 hours on internal Li-ion battery",
                accuracyAndResolution = "Flow Accuracy: ± 2% of set point; Timer Accuracy: ± 0.05%; Backpressure Compensation: up to 50 in. H2O at 2 L/min",
                calibrationProtocol = listOf(
                    "Calibrate before and after every field sampling shift using a NIST-traceable primary piston calibrator (e.g. Bios Defender).",
                    "Connect loaded filter cassette and cyclone in-line between pump and primary calibrator.",
                    "Take average of 5 calibration flow cycles; calibrate pump trim until measured flow exactly matches 2.000 L/min."
                ),
                zeroAndSpanProcedure = listOf(
                    "Pressure Transducer Zero: Vent pump inlet to atmospheric pressure; execute electronic zero routine in configuration menu.",
                    "Leak Check: Plug inlet tightly while pump is operating; pump must stall or trigger low-flow fault alarm within 3 seconds, proving no internal valve leakage."
                ),
                maintenanceAndServicing = listOf(
                    "Disassemble and clean conductive plastic cyclone body in warm ultrasonic detergent bath every 40 hours of dust sampling.",
                    "Inspect internal stainless steel inlet filter mesh; replace if clogged.",
                    "Cycle and recharge lithium-ion battery pack using intelligent multi-station dock."
                ),
                operatingEnvironmentLimits = "Intrinsically Safe (ATEX / IECEx Zone 0, Class I, Div 1); Operating Temp: -10°C to +45°C; RH: 0% to 95% non-condensing."
            )

            PollutionMonitoringDomain.NOISE -> InstrumentManualDetails(
                instrumentName = "Class 1 Precision Integrating Sound Level Meter & 1/1 & 1/3 Octave Real-Time Analyzer",
                makeAndModel = "Brüel & Kjær 2250 / Larson Davis SoundTrack LxT / NTi Audio XL2 / Rion NL-52",
                operatingPrinciple = "Precision Prepolarized Condenser Microphony: Incident sound pressure waves deflect a 5.0 µm ultra-thin nickel/titanium diaphragm across a quartz backplate, modulating electrostatic capacitance. A low-noise preamplifier converts charge to AC voltage. 24-bit dual delta-sigma ADCs digitize signal for digital real-time filtering conforming to IEC 61672-1 Class 1 and IEC 61260 Class 1 octave filters.",
                measurementRange = "16.0 dBA to 140.0 dBA (Peak range up to 143 dB Peak C); Frequency Range: 6.3 Hz to 20 kHz",
                accuracyAndResolution = "Class 1 IEC 61672 Accuracy (± 0.7 dB at reference 1 kHz); Dynamic Range: > 120 dB; Resolution: 0.1 dB",
                calibrationProtocol = listOf(
                    "Annual laboratory acoustic calibration traceable to PTB / NIST standards in anechoic chamber.",
                    "Field calibration before and after every survey using an IEC 60942 Class 1 acoustic calibrator generating 94.0 dB ± 0.2 dB at 1000 Hz.",
                    "Apply automated atmospheric pressure correction factor based on internal barometer."
                ),
                zeroAndSpanProcedure = listOf(
                    "Acoustic Calibrator Coupling: Seat 1/2-inch microphone firmly into calibrator cavity; power on calibrator at 94.0 dB.",
                    "Execute Calibrate Command: Instrument auto-adjusts microphone sensitivity (e.g. 50.0 mV/Pa); record sensitivity drift in log (must be < 0.2 dB)."
                ),
                maintenanceAndServicing = listOf(
                    "Store microphone cartridge with protective desiccant cap when not in use to avoid moisture corrosion on gold/nickel diaphragm.",
                    "Inspect windscreen for tear or degradation; replace windscreen if wet or deformed.",
                    "Test microphone preamplifier polarization and internal heating element (for condensation prevention) quarterly."
                ),
                operatingEnvironmentLimits = "Temperature: -10°C to +50°C; Barometric Pressure: 65 to 108 kPa; Humidity: up to 98% RH; IP54 weather resistance."
            )

            PollutionMonitoringDomain.STACK_EMISSION -> InstrumentManualDetails(
                instrumentName = "Automated Isokinetic Stack Gas & Particulate Sampling Train",
                makeAndModel = "Apex Instruments Method 5 Deluxe / CleanAir Express Isokinetic / TCR Tecora Isostack",
                operatingPrinciple = "Differential Pitot & Constant-Rate Proportional Vacuum Isokinetic Suction: A Type-S Pitot tube senses stack gas velocity head (ΔP). The dry gas meter (DGM) control unit calculates isokinetic nozzle flow matching stack gas streamline velocity exactly: v_nozzle = v_stack. Particulate is captured on heated quartz filter (120°C); condensable moisture and acidic gases are captured in a 4-impinger chilled condensing train.",
                measurementRange = "Stack Gas Velocity: 2.0 to 45.0 m/s; Temperature: 0°C to 1200°C (Inconel thermocouple); Particulate: 0.5 to 50,000 mg/Nm³",
                accuracyAndResolution = "Isokinetic Sampling Accuracy: 90% to 110% per EPA Method 5; Flow Measurement Resolution: 0.0001 m³; ΔP Resolution: 0.1 mm H2O",
                calibrationProtocol = listOf(
                    "Calibrate dry gas meter (DGM) calibration factor (Y) against secondary wet test meter or calibrated critical orifice (Y = 0.99 to 1.01).",
                    "Calibrate dual-inclined liquid manometer against micromanometer standard.",
                    "Calibrate Type-S Pitot tube coefficient (Cp = 0.840) in calibrated EPA wind tunnel.",
                    "Calibrate nozzle internal diameters with 3-axis micrometer (0.01 mm precision) across 3 angles."
                ),
                zeroAndSpanProcedure = listOf(
                    "Leak Check Probe Train: Seal nozzle tip with silicone stopper; pull 380 mmHg vacuum on control console; leak rate must remain ≤ 0.00057 m³/min.",
                    "Manometer Zero: Level dual-inclined manometer with internal spirit level; vent both taps to air; adjust fluid meniscus to zero mark exactly."
                ),
                maintenanceAndServicing = listOf(
                    "Clean and acid-wash Pyrex/borosilicate glass probe liners and impingers between tests to prevent sample contamination.",
                    "Replace dry gas meter internal synthetic diaphragm gaskets annually.",
                    "Inspect heated umbilical sample line (PTFE sample core, thermocouple wires, Pitot lines) for kinks, chafing, or electrical insulation wear."
                ),
                operatingEnvironmentLimits = "Console Operating Temp: -10°C to +45°C; Stack Flue Temp: Up to 1200°C; Dust explosion safe rated components."
            )

            PollutionMonitoringDomain.FLUE_GAS -> InstrumentManualDetails(
                instrumentName = "Industrial Portable Multi-Gas Combustion Efficiency & Flue Gas Analyzer",
                makeAndModel = "Testo 350 Industrial / E Instruments E8500 Plus / MRU Vario Plus",
                operatingPrinciple = "Peltier Gas Conditioning & NDIR / Long-Life Electrochemical Cells: Flue gas is aspirated through a heated probe into an integrated Peltier thermoelectric cooler that flash-condenses water vapor to 3°C, eliminating SO2 washout. O2 is measured via paramagnetic / zirconium cell; CO, CO2, SO2, NOx (NO + NO2) are analyzed via high-linearity NDIR and temperature-compensated electrochemical cells.",
                measurementRange = "O2: 0 to 25.0%; CO: 0 to 10,000 ppm (with dilution to 40,000 ppm); CO2: 0 to 20.0%; SO2: 0 to 5000 ppm; NOx: 0 to 3000 ppm; Flue Temp: -40°C to +1000°C",
                accuracyAndResolution = "O2: ± 0.2% vol; CO: ± 2% of reading; SO2/NOx: ± 3% of reading; Temp: ± 0.5°C; Resolution: 0.1 ppm / 0.01% vol",
                calibrationProtocol = listOf(
                    "Perform factory multi-point linearization calibration every 12 months with certificate.",
                    "Field span gas calibration using EPA Protocol 1 certified standard gases (NO, SO2, CO balance N2) with gas dilution manifold.",
                    "Calibrate thermoelectric chiller temperature controller to maintain condensate separator at 3.5 ± 0.5°C."
                ),
                zeroAndSpanProcedure = listOf(
                    "Fresh Air Automatic Zero: Analyzer draws fresh ambient air for 180 seconds upon startup; auto-calibrates O2 to 20.90% and zeros CO, SO2, NOx cells.",
                    "Span Calibration: Feed 500 ppm CO span gas at 1.0 L/min regulated flow; verify analyzer reading settles at 500 ± 5 ppm."
                ),
                maintenanceAndServicing = listOf(
                    "Replace internal inline particulate filter and coalescing filter whenever discoloration is visible.",
                    "Empty automatic peristaltic condensate drain pump tube; verify smooth pump roller rotation.",
                    "Replace depleted electrochemical sensor cells (typically 2 to 3 years service life)."
                ),
                operatingEnvironmentLimits = "Operating Temp: -5°C to +45°C; Flue Gas Temp: Up to 1000°C (ceramic probe up to 1800°C); Rugged shock-resistant housing."
            )

            PollutionMonitoringDomain.PAINT_BOOTH -> InstrumentManualDetails(
                instrumentName = "Omni-Directional Thermal Anemometer & Flame Ionization VOC Analyzer",
                makeAndModel = "TSI VelociCalc 9565 / Testo 405i / J.U.M. Engineering FID 3-300A / RAE Systems ppbRAE 3000",
                operatingPrinciple = "Hot-Wire Thermal Anemometry & Flame Ionization Detection (FID): A miniature platinum thin-film hot-wire resistance sensor is electrically heated; cooling by passing airflow directly correlates with face air velocity (King's Law). For VOC emission, a hydrogen flame ionizes volatile hydrocarbons; electrical current proportional to total carbon count is measured by a sensitive electrometer.",
                measurementRange = "Air Velocity: 0.00 to 30.00 m/s; VOC: 0.1 to 10,000 ppm (as Carbon equivalent / Toluene / Xylene); Differential Pressure: -1250 to +1250 Pa",
                accuracyAndResolution = "Air Velocity: ± 3% of reading or ± 0.015 m/s; VOC: ± 1% of full scale; Pressure: ± 1% of reading; Resolution: 0.01 m/s / 0.1 Pa",
                calibrationProtocol = listOf(
                    "Perform annual wind-tunnel calibration of thermal anemometer probe traceable to NIST / AMCA standards.",
                    "Calibrate FID analyzer using certified Zero Air (THC < 0.1 ppm) and 50 ppm Methane / 20 ppm Propane balance air calibration cylinder.",
                    "Verify Magnehelic differential pressure gauges with inclined liquid water manometer standard."
                ),
                zeroAndSpanProcedure = listOf(
                    "Zero Air Setup: Introduce hydrocarbon-free synthetic zero air into FID inlet; adjust zero potentiometer until baseline registers 0.0 ppm.",
                    "Span Setup: Feed 20.0 ppm propane span gas; adjust span potentiometer until meter reads 20.0 ppm."
                ),
                maintenanceAndServicing = listOf(
                    "Keep thermal anemometer sensor protective shield closed when transporting to prevent mechanical damage to microscopic sensor wires.",
                    "Clean FID burner nozzle and ignition electrode periodically; verify ultra-pure hydrogen (99.999%) fuel supply pressure.",
                    "Inspect paint booth exhaust arrestor filters daily; replace filters when differential pressure exceeds 250 Pa (1.0 in. H2O)."
                ),
                operatingEnvironmentLimits = "Operating Temp: 0°C to +50°C; Intrinsic safety approvals for hazardous spray zone monitoring (Class I, Div 1 / Zone 1)."
            )

            PollutionMonitoringDomain.AIR_MICROBIOLOGY -> InstrumentManualDetails(
                instrumentName = "Single-Stage & 6-Stage Viable Cascade Microbial Bioaerosol Impactor",
                makeAndModel = "Thermo / Andersen N6 Single-Stage Viable Sampler / Tisch 6-Stage Bioaerosol Impactor",
                operatingPrinciple = "Inertial Sieve Impaction onto Agar: Air is drawn at calibrated volumetric flow rate (28.3 L/min = 1.0 CFM) through 400 precision micro-orifices (diameter 0.25 mm). Airborne viable microorganisms acquire sufficient inertia to impact onto the agar nutrient surface directly beneath each orifice, while smaller non-viable particles remain suspended in the exhaust air stream.",
                measurementRange = "0 to 40,000 CFU/m³ (Statistical Positive-Hole Feller correction matrix applies from 1 to 400 colonies per plate)",
                accuracyAndResolution = "Flow Rate: 28.3 L/min ± 2% via calibrated vacuum pump; Cut-off diameter d50 = 0.65 µm (Stage 6) to 7.0 µm (Stage 1)",
                calibrationProtocol = listOf(
                    "Calibrate vacuum sampling pump every 6 months using a primary mass flow meter or bell prover.",
                    "Verify orifice plate dimensions using optical comparator to detect orifice burrs, corrosion, or clogging.",
                    "Inspect silicone stage gaskets to prevent inter-stage bypass leakage."
                ),
                zeroAndSpanProcedure = listOf(
                    "Field Sterility Negative Control: Place an unexposed nutrient agar plate into the sampling train for 15 minutes with pump off; plate must yield 0 CFU upon incubation.",
                    "Flow Verification: Attach secondary rotameter/DryCal inline prior to sampling; adjust precision needle valve until floating ball centers at 28.3 L/min line."
                ),
                maintenanceAndServicing = listOf(
                    "Autoclave aluminum / stainless steel impactor body parts at 121°C for 20 minutes before each sampling campaign.",
                    "Clean orifice plates in ultrasonic bath with non-corrosive enzymatic detergent followed by deionized water rinse.",
                    "Replace diaphragm valves and vacuum hose fittings annually."
                ),
                operatingEnvironmentLimits = "Operating Temp: 5°C to +40°C; Humidity: 20% to 90% non-condensing; Compatible with cleanroom Grade A isolators."
            )

            PollutionMonitoringDomain.CLEAN_ROOM -> InstrumentManualDetails(
                instrumentName = "High-Flow Airborne Optical Particle Counter (OPC) compliant with ISO 21501-4",
                makeAndModel = "Particle Measuring Systems (PMS) Lasair III 310C / Beckman Coulter MET ONE 3400+ / TSI AeroTrak 9310",
                operatingPrinciple = "Laser Light Scattering (Mie Scattering): Aerosol sample is drawn into an optical flow cell illuminated by a high-stability laser diode (780 nm). Airborne particles traversing the laser beam scatter light in all directions. Parabolic collection mirrors focus scattered photons onto a high-speed photodiode. Pulse amplitude corresponds strictly to particle diameter per Mie scattering theory.",
                measurementRange = "0.3 µm to 25.0 µm particle size range across 6 user-configurable channels (0.3, 0.5, 1.0, 3.0, 5.0, 10.0 µm); Flow: 28.3 L/min (1.0 CFM) or 50.0 L/min",
                accuracyAndResolution = "Counting Efficiency: 50% ± 20% at most sensitive threshold (0.3/0.5 µm); 100% ± 10% at > 1.5× threshold (ISO 21501-4 compliant)",
                calibrationProtocol = listOf(
                    "Annual factory calibration using certified Polystyrene Latex Spheres (PSL) traceable to NIST per ISO 21501-4.",
                    "Verify laser beam intensity, photodiode pulse height analyzer, and internal vacuum blower flow rate.",
                    "Record voltage calibration curves for each discrete particle size channel."
                ),
                zeroAndSpanProcedure = listOf(
                    "Zero Count Verification (Mandatory before cleanroom audit): Attach 0.1 µm absolute HEPA zero-filter to inlet; sample continuously for 15 minutes; total registered counts must be ≤ 1 count per 5 minutes for ≥ 0.5 µm.",
                    "Flow Rate Check: Measure volumetric intake with a calibrated mass flow meter; flow must stay within 28.3 L/min ± 5%."
                ),
                maintenanceAndServicing = listOf(
                    "Wipe stainless steel 316L housing with 70% IPA cleanroom wipes daily.",
                    "Purge optical chamber with filtered clean air after sampling in contaminated environments before powering off.",
                    "Replace internal HEPA exhaust filter every 12 months to prevent particle exhaust into cleanroom."
                ),
                operatingEnvironmentLimits = "Cleanroom compatible (ISO Class 1 to 9); Operating Temp: 10°C to +35°C; RH: up to 90% non-condensing."
            )

            PollutionMonitoringDomain.LUX_MONITORING -> InstrumentManualDetails(
                instrumentName = "Precision Digital Cosine & Color-Corrected Illuminance Meter (Photometer)",
                makeAndModel = "Konica Minolta T-10A / Testo 540 / Hagner E4-X / Gossen MAVOLUX 5032B USB",
                operatingPrinciple = "Silicon Photodiode with Photopic & Cosine Correction: Light energy striking a high-linearity planar silicon photodiode generates a photocurrent directly proportional to illuminance. A custom glass optical filter adapts spectral response to match the CIE Standard Photopic Spectral Luminous Efficiency curve V(λ) (f1' error < 3.0%). A diffusing domed opal window achieves cosine correction for light incident at oblique angles.",
                measurementRange = "0.01 to 400,000 Lux (0.001 to 40,000 Foot-Candles) with auto-ranging across 5 decades",
                accuracyAndResolution = "Class B DIN 5032 Part 7 / Class 1 JIS C 1609 Accuracy: ± 2.0% of reading ± 1 digit; Resolution: 0.01 Lux",
                calibrationProtocol = listOf(
                    "Perform bi-annual laboratory photometric calibration on optical bench with certified Tungsten Filament Standard Lamp at 2856 K color temperature (CIE Illuminant A).",
                    "Verify V(λ) spectral matching index (f1' < 3.0%) and cosine response error (f2 < 1.5%).",
                    "Calibrate linearity across full 5-decade dynamic range."
                ),
                zeroAndSpanProcedure = listOf(
                    "Auto-Zero Routine: Slide protective lens cap over receptor head; press Zero Button; internal circuit zeroes amplifier offset voltage to 0.00 Lux.",
                    "Sensitivity Check: Test against reference calibrated photometer under stabilized incandescent luminaire."
                ),
                maintenanceAndServicing = listOf(
                    "Inspect diffuser window for fingerprints, dust, or scratches; clean only with optical lens paper moistened with isopropyl alcohol.",
                    "Keep receptor head covered with protective opaque cap when not actively taking measurements.",
                    "Store photometer in dry foam-padded carrying case to prevent mechanical shock."
                ),
                operatingEnvironmentLimits = "Operating Temp: -10°C to +40°C; Relative Humidity: 0% to 85% non-condensing; USB and Bluetooth data logging."
            )

            PollutionMonitoringDomain.WATER_MONITORING -> InstrumentManualDetails(
                instrumentName = "Multi-Parameter Water Quality Field Sonde & Spectrophotometer",
                makeAndModel = "YSI ProDSS Multi-Parameter Field Sonde / Hach HQ40D / Hanna HI98194",
                operatingPrinciple = "Optical Luminescence Dissolved Oxygen & Glass Membrane Potentiometry: Dissolved oxygen is measured via optical lifetime quenching of a ruthenium-based fluorophore by O2 molecules (Stern-Volmer equation). pH is measured by potentiometric glass bulb hydrogen ion exchange. Conductivity utilizes a 4-electrode graphite cell with automatic temperature compensation to 25°C. Turbidity uses 90° nephelometric near-IR LED scattering (ISO 7027).",
                measurementRange = "pH: 0.00 to 14.00; DO: 0.0 to 50.0 mg/L (0 to 500% sat); EC: 0 to 200,000 µS/cm; Turbidity: 0.0 to 4000 NTU; Temp: -5°C to +50°C",
                accuracyAndResolution = "pH: ± 0.02 pH units; DO: ± 0.1 mg/L; EC: ± 0.5% of reading; Turbidity: ± 2% of reading; Temp: ± 0.1°C",
                calibrationProtocol = listOf(
                    "Calibrate pH electrode with fresh 3-point buffers (pH 4.01, 7.00, 10.01); verify electrode slope is between 95% and 102%.",
                    "Calibrate DO optical cap in water-saturated air chamber at local barometric pressure.",
                    "Calibrate conductivity sensor with standard KCl solution (e.g. 1413 µS/cm at 25°C).",
                    "Calibrate turbidity optical sensor with StablCal Formazin standards (0.1, 20, 100, 800 NTU)."
                ),
                zeroAndSpanProcedure = listOf(
                    "Zero Calibration DO: Submerge probe in freshly prepared 2% sodium sulfite (Na2SO3) oxygen scavenger solution; reading must drop to 0.00 mg/L.",
                    "Span Calibration DO: Hold probe in 100% humidity calibration sleeve; input barometric pressure; auto-calibrate to 100.0% saturation."
                ),
                maintenanceAndServicing = listOf(
                    "Hydrate pH glass bulb in 3M KCl electrode storage solution; never store in deionized or distilled water.",
                    "Replace optical DO sensor cap every 12 to 18 months when luminescent sensing coating degrades.",
                    "Clean conductivity electrodes with warm water and soft brush; inspect rubber O-rings on waterproof cable connections."
                ),
                operatingEnvironmentLimits = "Submersible IP68 waterproof down to 100 meters depth; Operating Temp: -5°C to +50°C; Impact-resistant polyurethane sonde body."
            )
        }
    }

    /**
     * Complete International Standards & Guidelines Database for all 11 domains
     * Supported: ISO, AS/NZS, USEPA, BS, EU, Indian (CPCB/BIS), Chinese (GB), Brazilian (CONAMA)
     * For 4 durations: Spot, 1-Hour, 8-Hour, 24-Hour
     */
    fun getStandardBenchmark(
        domain: PollutionMonitoringDomain,
        standard: RegulatoryStandard,
        duration: MonitoringDuration
    ): StandardBenchmarkThreshold {
        return when (domain) {
            PollutionMonitoringDomain.AMBIENT_AIR -> when (standard) {
                RegulatoryStandard.ISO -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 50.0, "µg/m³", "ISO 12341 Screening Cap", "Short-term spot threshold guideline")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 40.0, "µg/m³", "ISO/WHO Air Quality Guideline (1-hr)", "Peak hourly vigilance standard")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 25.0, "µg/m³", "ISO 12341 TWA Recommended", "Sub-diurnal occupational and public protection")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 15.0, "µg/m³", "WHO/ISO Global Air Quality Guideline 2021", "24-hour mean guideline for human health protection")
                }
                RegulatoryStandard.USEPA -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 70.0, "µg/m³", "USEPA Air Quality Index (AQI) Unhealthy Tier", "Short-term spike indicator")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 55.0, "µg/m³", "USEPA NowCast 1-Hour Algorithm Threshold", "Real-time automated reporting trigger")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 45.0, "µg/m³", "USEPA Regional Advisory Benchmark", "Working day ambient exposure guide")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 35.0, "µg/m³", "USEPA NAAQS 40 CFR Part 50.18 (98th percentile)", "National Ambient Air Quality Standard 24-hour limit")
                }
                RegulatoryStandard.AS_NZS -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 50.0, "µg/m³", "AS/NZS 3580.9.9 Screening Alert", "Australian NEPM temporary alert")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 40.0, "µg/m³", "AS/NZS State Environment Protection Policy", "1-Hour investigative peak standard")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 30.0, "µg/m³", "AS/NZS Workshift Surrounding Exposure", "8-Hour average ambient guideline")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 25.0, "µg/m³", "National Environment Protection Measure (NEPM) 2021", "Australian standard 24-hour limit (target 20 µg/m³)")
                }
                RegulatoryStandard.BS -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 50.0, "µg/m³", "DEFRA Daily Air Quality Index Band 4", "High pollution warning band")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 40.0, "µg/m³", "BS EN 12341 UK Air Quality Objectives", "Hourly surveillance notification level")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 28.0, "µg/m³", "UK Environment Act 2021 TWA Guideline", "8-Hour reference benchmark")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 20.0, "µg/m³", "UK Air Quality Standards Regulations 2023", "UK binding ambient 24-hour objective")
                }
                RegulatoryStandard.EU -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 50.0, "µg/m³", "EU Ambient Air Quality Directive Alert Level", "Short-term public information trigger")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 40.0, "µg/m³", "EU Directive 2008/50/EC 1-Hour Monitoring", "Urban station hourly vigilance limit")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 25.0, "µg/m³", "EU Clean Air for Europe (CAFE) Working TWA", "8-Hour diurnal exposure threshold")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 25.0, "µg/m³", "EU Directive 2008/50/EC / Revised 2024 (15 µg/m³)", "Legally binding 24-hour threshold across member states")
                }
                RegulatoryStandard.INDIAN -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 90.0, "µg/m³", "CPCB NAQI Moderate-to-Poor Threshold", "Instantaneous field screening indicator")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 80.0, "µg/m³", "CPCB National Air Quality Index (NAQI) Hourly", "Continuous Ambient Air Quality Monitoring (CAAQMS)")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 70.0, "µg/m³", "BIS IS 5182 (Part 24) Sub-Day Guideline", "Industrial zone 8-hour benchmark")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 60.0, "µg/m³", "CPCB National Ambient Air Quality Standards (NAAQS) 2009", "Mandatory 24-hour ambient limit for Residential/Rural/Industrial")
                }
                RegulatoryStandard.CHINESE -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 115.0, "µg/m³", "GB 3095 Heavy Pollution Alert", "Municipal blue-sky screening alert")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 85.0, "µg/m³", "HJ 633-2012 AQI 1-Hour Calculation Matrix", "Automated station 1-hour index benchmark")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 65.0, "µg/m³", "GB 3095-2012 Class II Sub-Daily Guideline", "Industrial cluster working exposure")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 75.0, "µg/m³", "GB 3095-2012 Ambient Air Quality Standard (Class II)", "Chinese national 24-hour limit for residential & industrial areas")
                }
                RegulatoryStandard.BRAZILIAN -> when (duration) {
                    MonitoringDuration.SPOT -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 80.0, "µg/m³", "CONAMA Alert Stage (Atenção)", "Emergency civil defense alert")
                    MonitoringDuration.ONE_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 65.0, "µg/m³", "CETESB São Paulo Air Quality Matrix", "Hourly warning threshold")
                    MonitoringDuration.EIGHT_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 50.0, "µg/m³", "CONAMA Resolution Intermediate Standard PI-2", "8-Hour regional monitoring target")
                    MonitoringDuration.TWENTY_FOUR_HOUR -> StandardBenchmarkThreshold(standard, duration, "PM2.5", 60.0, "µg/m³", "CONAMA Resolution 491/2018 (Final standard 20 µg/m³)", "Brazilian federal legal standard 24-hour PM2.5 limit")
                }
            }

            PollutionMonitoringDomain.INDOOR_AIR -> when (standard) {
                RegulatoryStandard.ISO, RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 1000.0, "ppm", "ISO 16000 / EN 16798-1 Category II", "Acceptable indoor comfort threshold (fresh air min 10 L/s/person)")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 1000.0, "ppm", "ASHRAE Standard 62.1-2022 Ventilation", "Ventilation rate adequacy ceiling (< 700 ppm above outdoor ambient)")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 850.0, "ppm", "AS 1668.2 Mechanical Ventilation for Buildings", "High indoor air quality objective")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 1000.0, "ppm", "IS 16650:2017 & Indian Green Building Council (IGBC)", "Acceptable indoor commercial workplace CO2")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 1000.0, "ppm", "GB/T 18883-2022 Standards for Indoor Air Quality", "Mandatory indoor air quality limit for occupied spaces")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Indoor CO₂", 1000.0, "ppm", "ANVISA Resolution RE 09/2003", "Brazilian Sanitary Agency indoor air quality standard")
            }

            PollutionMonitoringDomain.WORKZONE_AIR -> when (standard) {
                RegulatoryStandard.USEPA, RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 5.0, "mg/m³", "OSHA 29 CFR 1910.1000 Table Z-1 / ACGIH TLV", "8-hour Permissible Exposure Limit for respirable fraction")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 3.0, "mg/m³", "Safe Work Australia Workplace Exposure Standards (WES)", "8-hour TWA occupational health standard")
                RegulatoryStandard.BS, RegulatoryStandard.EU -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 4.0, "mg/m³", "HSE EH40/2005 Workplace Exposure Limits (UK)", "8-hour TWA statutory limit")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 5.0, "mg/m³", "Indian Factories Act 1948 Second Schedule", "Permissible levels of chemical substances in work environment")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 4.0, "mg/m³", "GBZ 2.1-2019 Occupational Exposure Limits", "Permissible concentration-time weighted average (PC-TWA)")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Respirable Dust TWA", 5.0, "mg/m³", "NR-15 Norma Regulamentadora Anexo 12", "Brazilian Ministry of Labor occupational tolerance limit")
            }

            PollutionMonitoringDomain.NOISE -> when (standard) {
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 65.0, "dBA", "ISO 1996-1 Environmental Acoustic Criteria", "Daytime industrial boundary noise threshold")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 70.0, "dBA", "USEPA Levels Document (550/9-74-004)", "Protection of public health against hearing loss in workzone")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 65.0, "dBA", "AS/NZS 2107 Acoustics in Buildings", "Maximum recommended equivalent sound level")
                RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 65.0, "dBA", "BS 4142:2014 Commercial Sound Rating", "Rating sound level threshold at commercial receptor boundary")
                RegulatoryStandard.EU -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 65.0, "dBA", "EU Environmental Noise Directive 2002/49/EC", "Industrial agglomeration daytime noise ceiling")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 75.0, "dBA", "CPCB Noise Pollution (Regulation & Control) Rules 2000", "Industrial Area Daytime Limit (75 dBA Day / 70 dBA Night)")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 65.0, "dBA", "GB 3096-2008 Environmental Quality Standard for Noise", "Class 3 Industrial area daytime limit")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Sound Pressure Level (Leq)", 70.0, "dBA", "ABNT NBR 10151:2019 Acoustic Assessment", "Daytime limit for predominantly industrial zones")
            }

            PollutionMonitoringDomain.STACK_EMISSION -> when (standard) {
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 50.0, "mg/Nm³", "USEPA NSPS 40 CFR Part 60 Subpart Da", "Boiler stationary source emission limit")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 20.0, "mg/Nm³", "EU Industrial Emissions Directive (IED 2010/75/EU)", "Best Available Techniques (BAT) AEL standard for large combustion")
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 30.0, "mg/Nm³", "ISO 9096 Stationary Source Guideline", "International benchmark emission cap")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 50.0, "mg/Nm³", "Australian National Clean Air Agreement", "Industrial duct particulate emission standard")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 50.0, "mg/Nm³", "MoEFCC / CPCB Thermal Power & Boiler Standards 2015", "Mandatory stack emission limit for industrial boilers (50 mg/Nm³)")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 10.0, "mg/Nm³", "GB 13271 / Ultra-Low Emission Standard", "Ultra-low emission mandatory standard for coal/gas boilers")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Particulate Concentration", 70.0, "mg/Nm³", "CONAMA Resolution 382/2006 & 436/2011", "National emission limits for fixed sources")
            }

            PollutionMonitoringDomain.FLUE_GAS -> when (standard) {
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 100.0, "ppm", "USEPA Boiler MACT 40 CFR Part 63", "Carbon monoxide combustion efficiency ceiling")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 80.0, "ppm", "EU Medium Combustion Plant Directive (MCPD 2015/2193)", "Combustion CO standard at reference O2")
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 100.0, "ppm", "ISO 12039 Flue Gas Performance", "International benchmark for industrial burner optimization")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 120.0, "ppm", "AS 1375 SAA Industrial Fuel-Fired Appliance Code", "Flue gas carbon monoxide limit")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 150.0, "ppm", "CPCB Boiler Standards & IS 11255", "Industrial flue gas CO guideline limit")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 50.0, "ppm", "GB 13271-2014 Emission Standard of Air Pollutants for Boilers", "Boiler exhaust CO cap")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "CO Concentration (@ Ref O₂)", 150.0, "ppm", "CONAMA Resolution 436/2011", "Industrial furnace combustion standard")
            }

            PollutionMonitoringDomain.PAINT_BOOTH -> when (standard) {
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "NFPA 33 / OSHA 1910.107(b)(5)", "Minimum average crossdraft face velocity (100 FPM = 0.51 m/s)")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "EN 12215 Spray Booth Safety Standard", "Airflow velocity across working opening")
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "ISO 14444 Industrial Coating Application", "International paint booth capture standard")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "AS/NZS 4114 Spray Painting Booths", "Mandatory minimum face velocity")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "Indian Factories Act Model Rule 68", "Safety face velocity for flammable spray processes")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "GB 14444-2000 Safety of Spray Booths", "Minimum exhaust capture velocity")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Minimum Capture Face Velocity", 0.50, "m/s", "ABNT NBR 15742 Paint Booth Ventilation", "Brazilian technical standard face velocity")
            }

            PollutionMonitoringDomain.AIR_MICROBIOLOGY -> when (standard) {
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 100.0, "CFU/m³", "ISO 14698-1 Bio-contamination Control (Cleanroom Grade C)", "Intermediate pharmaceutical cleanroom threshold")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 10.0, "CFU/m³", "EU GMP Annex 1:2022 Grade B Limit", "Aseptic cleanroom Grade B in-operation limit (< 10 CFU/m³)")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 10.0, "CFU/m³", "USP <797> Sterile Compounding ISO 7", "Compounding clean area bioburden action level")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 100.0, "CFU/m³", "AS/NZS 2243.3 Safety in Microbiological Laboratories", "Airborne viable bioburden limit")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 100.0, "CFU/m³", "Schedule M Good Manufacturing Practices (CDSCO)", "Sterile pharmaceutical processing area viable count")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 100.0, "CFU/m³", "Chinese Pharmacopoeia ChP 2020 Clean Area", "Grade C microbiological air action limit")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Viable Microorganisms", 100.0, "CFU/m³", "ANVISA RDC 301/2019 Brazilian GMP", "Sanitary airborne bioburden threshold")
            }

            PollutionMonitoringDomain.CLEAN_ROOM -> when (standard) {
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "ISO 14644-1:2015 Class 7 (at-rest)", "Cleanroom classification standard limit for Class 7")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "EU GMP Annex 1 Grade C (at-rest)", "Sterile manufacturing Grade C threshold")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "US FDA Aseptic Processing (Class 10,000)", "Federal Standard 209E Class 10,000 equivalent")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "AS 1386 Cleanrooms Class 3500", "Australian cleanroom airborne particle standard")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "Revised Schedule M (Drugs & Cosmetics Rules) Grade C", "Indian pharmaceutical cleanroom standard")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "GB 50073-2013 Code for Design of Clean Room", "Class 7 airborne particulate limit")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Particles ≥ 0.5 µm", 352000.0, "particles/m³", "ABNT NBR ISO 14644-1 / ANVISA RDC 301", "Brazilian cleanroom classification ceiling")
            }

            PollutionMonitoringDomain.LUX_MONITORING -> when (standard) {
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "ISO 8995-1 / CIE S 008 (Office Working Plane)", "Standard visual task maintained illuminance")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "EN 12464-1 Light & Lighting of Work Places", "General office writing, typing, and reading standard")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "IESNA Lighting Handbook (Task Category E)", "Illuminating Engineering Society workplace standard")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 400.0, "Lux", "AS/NZS 1680.1 Interior Workplace Lighting", "Australian maintained illuminance recommendation")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "BIS IS 3646 (Part 1) & NBC 2016 Part 8", "Indian building code illumination standard for offices")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "GB 50034-2013 Architectural Lighting Standard", "Commercial building working plane illuminance")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Maintained Illuminance (Em)", 500.0, "Lux", "ABNT NBR ISO/CIE 8995-1", "Brazilian interior workplace lighting regulation")
            }

            PollutionMonitoringDomain.WATER_MONITORING -> when (standard) {
                RegulatoryStandard.ISO -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 30.0, "mg/L", "ISO Water Quality Discharge Guideline", "Treated effluent general discharge limit")
                RegulatoryStandard.USEPA -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 30.0, "mg/L", "USEPA NPDES Secondary Treatment 40 CFR 133", "30-day average wastewater discharge ceiling")
                RegulatoryStandard.EU, RegulatoryStandard.BS -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 25.0, "mg/L", "EU Urban Waste Water Treatment Directive (91/271/EEC)", "Strict municipal WWTP effluent limit")
                RegulatoryStandard.AS_NZS -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 20.0, "mg/L", "ANZECC & ARMCANZ Australian Water Quality Guidelines", "Environmental discharge protection threshold")
                RegulatoryStandard.INDIAN -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 30.0, "mg/L", "CPCB General Standards for Discharge of Environmental Pollutants (Schedule VI)", "Inland surface water discharge standard (BOD 30 mg/L, COD 250 mg/L)")
                RegulatoryStandard.CHINESE -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 20.0, "mg/L", "GB 18918-2002 Discharge Standard for Municipal WWTP", "Class 1A effluent standard")
                RegulatoryStandard.BRAZILIAN -> StandardBenchmarkThreshold(standard, duration, "Biochemical Oxygen Demand (BOD₅)", 60.0, "mg/L", "CONAMA Resolution 430/2011 Article 16", "Condition for effluent discharge into water bodies (min 60% removal)")
            }
        }
    }
}
