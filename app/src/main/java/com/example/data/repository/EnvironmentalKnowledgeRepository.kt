package com.example.data.repository

import com.example.data.model.EnvironmentalDomain
import com.example.data.model.EnvironmentalTopic
import com.example.data.model.OngoingResearch
import com.example.data.model.QualityManagementSystem
import com.example.data.model.QualityParameterBand

object EnvironmentalKnowledgeRepository {

    val allTopics: List<EnvironmentalTopic> = listOf(
        // 1. AIR QUALITY
        EnvironmentalTopic(
            id = "air_quality",
            domain = EnvironmentalDomain.AIR_QUALITY,
            title = "Air Quality & Atmospheric Dynamics",
            subtitle = "Aerosol Microphysics, Photochemical Smog & Gas Speciation",
            levelBadge = "Basic to Advanced",
            basicDescription = "Air quality measures the cleanliness of atmospheric air based on suspended particulate matter (PM2.5 and PM10) and harmful gaseous emissions. Clean ambient air is fundamental to human respiratory health, metabolic longevity, and ecosystem stability.",
            advancedScience = "Advanced analysis inspects secondary organic aerosol (SOA) formation, volatile organic compound (VOC) photo-oxidation, tropospheric ozone (O3) catalytic cycles, and boundary-layer temperature inversions that trap reactive NOx and SO2 emissions. Laser optical particle counters and multi-wavelength spectrophotometry quantify particle size distribution down to ultrafine PM0.1.",
            primaryIndicators = listOf(
                QualityParameterBand("PM2.5 Fine Particulate", "0 - 15", "15", "µg/m³", "WHO AQG 2021 (24h)", 12.4f, 60f),
                QualityParameterBand("PM10 Coarse Particulate", "0 - 45", "45", "µg/m³", "WHO AQG 2021 (24h)", 28.0f, 100f),
                QualityParameterBand("Tropospheric Ozone (O3)", "0 - 100", "100", "µg/m³", "WHO 8-hour Mean", 64.0f, 180f),
                QualityParameterBand("Nitrogen Dioxide (NO2)", "0 - 25", "25", "µg/m³", "WHO 24-hour Mean", 18.2f, 80f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 14001:2015 & WHO Global AQG",
                certificationCode = "ISO 14001 / EN 15267",
                governingBody = "World Health Organization & ISO TC 146",
                scope = "Atmospheric surveillance, industrial chimney emissions, fugitive dust suppression, and urban continuous emission monitoring systems (CEMS).",
                complianceChecklist = listOf(
                    "Calibrate optical PM sensors every 90 days against gravimetric reference samplers",
                    "Maintain continuous automated telemetry reporting at 15-minute intervals",
                    "Enforce emergency traffic and industrial dampening when PM2.5 exceeds 75 µg/m³",
                    "Conduct biannual VOC canister speciation audits for industrial zones"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "NASA TEMPO Geostationary Hourly Atmospheric Pollution Tracking",
                    institution = "NASA Langley & Smithsonian Astrophysical Observatory",
                    leadLocation = "Global / North America",
                    stage = "Active Orbital Telemetry",
                    summary = "Utilizes ultraviolet/visible imaging spectrometers aboard geostationary satellites to quantify hourly daytime column variations of NO2, SO2, and formaldehyde with 2.1 km resolution.",
                    publicationOrRef = "NASA Earth Science / Nature Geoscience 2025"
                ),
                OngoingResearch(
                    title = "MOF Nano-Catalysts for Ambient Indoor VOC Catalytic Destruction",
                    institution = "MIT & Max Planck Institute for Polymer Research",
                    leadLocation = "Cambridge, USA & Mainz, Germany",
                    stage = "Laboratory to Pilot Scale",
                    summary = "Developing room-temperature metal-organic framework (MOF) filters that oxidize benzene, formaldehyde, and toluene into benign water and trace CO2 without toxic ozone generation.",
                    publicationOrRef = "Journal of the American Chemical Society 2025"
                ),
                OngoingResearch(
                    title = "Epigenetic DNA Methylation Biomarkers from Chronic Ultrafine PM Exposure",
                    institution = "Harvard T.H. Chan School of Public Health",
                    leadLocation = "Boston, USA",
                    stage = "Longitudinal Cohort",
                    summary = "Tracking epigenetic blood markers in 45,000 participants to identify reversible methylation signatures caused by sub-micron combustion particles.",
                    publicationOrRef = "The Lancet Planetary Health 2026"
                )
            ),
            defaultInputPrompt = "Enter PM2.5 concentration in µg/m³:",
            inputUnit = "µg/m³",
            thresholdGuideline = "WHO 24h limit: ≤15 µg/m³; Moderate: 15-35 µg/m³; Severe: >35 µg/m³"
        ),

        // 2. WATER QUALITY
        EnvironmentalTopic(
            id = "water_quality",
            domain = EnvironmentalDomain.WATER_QUALITY,
            title = "Water Quality & Aquatic Hydrochemistry",
            subtitle = "Dissolved Oxygen, Speciation, PFAS & Micro-Contaminants",
            levelBadge = "Basic to Advanced",
            basicDescription = "Water quality assesses chemical, physical, and biological characteristics against safety standards for drinking, agriculture, and aquatic ecosystem survival. Key fundamentals include pH, turbidity, salinity, and dissolved oxygen.",
            advancedScience = "Advanced limnology and hydrochemistry examine Biochemical Oxygen Demand (BOD5), Chemical Oxygen Demand (COD), Total Organic Carbon (TOC), nutrient eutrophication ratios (Redfield ratio N:P = 16:1), and persistent emerging contaminants including per- and polyfluoroalkyl substances (PFAS) and microplastic synthetic polymer fibers.",
            primaryIndicators = listOf(
                QualityParameterBand("Dissolved Oxygen (DO)", "6.5 - 9.5", "5.0", "mg/L", "EPA Aquatic Life Min", 7.8f, 12f),
                QualityParameterBand("Acidity / Alkalinity (pH)", "6.5 - 8.5", "6.5 - 8.5", "pH Units", "WHO Drinking Water", 7.4f, 14f),
                QualityParameterBand("Turbidity", "0 - 1.0", "4.0", "NTU", "WHO Potable Standard", 0.8f, 10f),
                QualityParameterBand("Biochemical Oxygen Demand (BOD5)", "0 - 2.0", "5.0", "mg/L", "EU Surface Water Quality", 1.6f, 15f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 24510 & US Clean Water Act NPDES",
                certificationCode = "ISO 24510 / ISO 24512",
                governingBody = "International Organization for Standardization & US EPA",
                scope = "Management of potable water supply services, municipal wastewater effluent discharge criteria, and catchment basin protection.",
                complianceChecklist = listOf(
                    "Standardized daily grab sampling and online spectro-turbidity monitoring",
                    "Inductively Coupled Plasma Mass Spectrometry (ICP-MS) tests for heavy metals (Pb, Cd, As)",
                    "Maintain minimum 0.2 mg/L free residual chlorine in municipal distribution lines",
                    "Conduct quarterly targeted LC-MS/MS screening for PFAS chain congeners (PFOA, PFOS)"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Boron-Doped Diamond Anode Electrochemical Mineralization of PFAS",
                    institution = "Stanford University & UC Berkeley Water Center",
                    leadLocation = "Palo Alto, USA",
                    stage = "Pilot Field Demonstration",
                    summary = "Utilizing electrified high-surface-area boron-doped diamond electrodes to cleave robust carbon-fluorine bonds in aqueous solutions, converting forever chemicals to fluoride ions and CO2 at ambient pressure.",
                    publicationOrRef = "Environmental Science & Technology 2025"
                ),
                OngoingResearch(
                    title = "Graphene-Oxide Sub-Nanometer Sieving Membranes for Desalination",
                    institution = "ETH Zurich & National University of Singapore",
                    leadLocation = "Zurich, Switzerland",
                    stage = "Pre-Commercial Testing",
                    summary = "Ultrathin 2D stacked laminar membranes capable of rejecting 99.8% of monovalent salts and 100% of microplastics with 300% higher water flux than traditional polyamide reverse osmosis.",
                    publicationOrRef = "Nature Nanotechnology 2026"
                ),
                OngoingResearch(
                    title = "Global Wastewater Genomic Surveillance for Antimicrobial Resistance (AMR)",
                    institution = "WHO / UNICEF / Karolinska Institute",
                    leadLocation = "Stockholm, Sweden & Global Sites",
                    stage = "Global Epidemiological Network",
                    summary = "Metagenomic shotgun sequencing of municipal sewage outfalls to detect emerging multi-drug resistant superbugs and antibiotic resistance genes 14 days prior to clinical hospital presentations.",
                    publicationOrRef = "The Lancet Infectious Diseases 2025"
                )
            ),
            defaultInputPrompt = "Enter Dissolved Oxygen (DO) in mg/L:",
            inputUnit = "mg/L",
            thresholdGuideline = "Healthy aquatic life: ≥6.5 mg/L; Stressed: 4.0-6.5 mg/L; Hypoxic dead zone: <2.0 mg/L"
        ),

        // 3. ANIMALS & BIODIVERSITY
        EnvironmentalTopic(
            id = "animals_biodiversity",
            domain = EnvironmentalDomain.ANIMALS_BIODIVERSITY,
            title = "Animals, Biodiversity & Ecosystem Resilience",
            subtitle = "Bioindicators, Keystone Species, eDNA & Habitat Corridors",
            levelBadge = "Basic to Advanced",
            basicDescription = "Biodiversity measures the variety of living organisms across terrestrial, aquatic, and aerial ecosystems. Wild animals and insects serve as foundational pollinators, seed dispersers, and sentinel organisms for global planetary health.",
            advancedScience = "Advanced conservation biology monitors trophic cascades, keystone species stability, meta-population genetics, and ecosystem services. Environmental DNA (eDNA) metabarcoding enables species inventory from water or soil samples without physical capture, while the Shannon-Wiener index (H') calculates structural community biodiversity.",
            primaryIndicators = listOf(
                QualityParameterBand("Shannon Diversity Index (H')", "2.5 - 4.5", "1.5", "Index Units", "Ecosystem Resilience Min", 3.2f, 5.0f),
                QualityParameterBand("Keystone Species Population Index", "90 - 110", "70", "% Baseline", "IUCN Living Planet Target", 88.0f, 120f),
                QualityParameterBand("Habitat Fragmentation Index", "0 - 15", "30", "% Degraded", "CBD 30x30 Target", 18.5f, 50f),
                QualityParameterBand("Pollinator Density", "15 - 35", "10", "Individuals/m²", "IPBES Pollinator Standard", 22.0f, 40f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "CBD Kunming-Montreal Global Biodiversity Framework",
                certificationCode = "CBD Target 3 (30x30) / CITES",
                governingBody = "United Nations Environment Programme (UNEP)",
                scope = "Conserving 30% of planetary terrestrial, freshwater, and marine areas by 2030, halting anthropogenic extinction, and restoring functional ecological corridors.",
                complianceChecklist = listOf(
                    "Maintain minimum 500-meter continuous wildlife movement corridors between fragmented reserves",
                    "Conduct seasonal eDNA water sampling to survey red-listed and invasive aquatic taxa",
                    "Mandate bird-safe architectural glass and acoustic buffering around flyway corridors",
                    "Enforce zero-pesticide buffer belts around vital native pollinator foraging zones"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Bioacoustic Foundation AI Transformers for Autonomous Rainforest Census",
                    institution = "University of Oxford & Max Planck Institute of Animal Behavior",
                    leadLocation = "Oxford, UK & Costa Rica",
                    stage = "Autonomous Edge Deployment",
                    summary = "Deploying solar-powered microphone arrays running self-supervised audio transformers that classify 1,400 animal and amphibian vocalizations simultaneously in real time.",
                    publicationOrRef = "Science Advances 2025"
                ),
                OngoingResearch(
                    title = "High-Throughput Airborne eDNA for Forest Canopy Vertebrate Tracking",
                    institution = "University of Copenhagen & York University",
                    leadLocation = "Copenhagen, Denmark",
                    stage = "Field Validation",
                    summary = "Vacuum air-sampling drones collecting cellular debris and genetic fragments directly from the atmosphere to map cryptic arboreal mammals and rare bats without invasive netting.",
                    publicationOrRef = "Current Biology 2025"
                ),
                OngoingResearch(
                    title = "Planetary Avian Migration Dynamics via Weather Radar Doppler Networks",
                    institution = "Cornell Lab of Ornithology",
                    leadLocation = "Ithaca, USA",
                    stage = "Continental Operational System",
                    summary = "Processing NEXRAD dual-polarization weather radar archives with computer vision to quantify night-migrating biomass fluxes and forecast collision threats with urban wind turbines.",
                    publicationOrRef = "Nature Ecology & Evolution 2026"
                )
            ),
            defaultInputPrompt = "Enter Shannon Diversity Index (H') or Species Count:",
            inputUnit = "Score / Count",
            thresholdGuideline = "High biodiversity: H' ≥ 3.0; Moderate: 2.0-3.0; Severe degradation: < 1.5"
        ),

        // 4. PHYSICAL QUALITIES & CLIMATE
        EnvironmentalTopic(
            id = "physical_qualities",
            domain = EnvironmentalDomain.PHYSICAL_QUALITIES,
            title = "Physical Qualities, Radiation & Microclimate",
            subtitle = "Albedo Flux, Urban Heat Island, Thermal Emissivity & Boundary Layers",
            levelBadge = "Basic to Advanced",
            basicDescription = "Physical environmental qualities govern the thermodynamics of ecosystems: ambient air temperature, relative humidity, surface albedo, solar radiation flux, and atmospheric barometric pressure.",
            advancedScience = "Advanced microclimatology analyzes surface energy budgets (Rn = H + LE + G), radiative forcing (W/m²), Planck blackbody thermal emission, and Urban Heat Island (UHI) microclimate loops caused by low-albedo asphalt and anthropogenic HVAC heat rejection.",
            primaryIndicators = listOf(
                QualityParameterBand("Surface Albedo Coefficient (α)", "0.35 - 0.85", "0.20", "Dimensionless (0-1)", "Cool Roof Standard", 0.42f, 1.0f),
                QualityParameterBand("Urban Heat Island (UHI) Delta", "0 - 1.5", "3.0", "°C Differential", "WMO Urban Climate Limit", 2.1f, 8.0f),
                QualityParameterBand("Solar Radiation Flux", "200 - 850", "1100", "W/m²", "Baseline Diurnal", 620f, 1200f),
                QualityParameterBand("Relative Atmospheric Humidity", "40 - 65", "85", "% RH", "Thermal Comfort Index", 54f, 100f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "WMO Climate Observation Standards (WMO-No. 8)",
                certificationCode = "WMO-No. 8 / ISO 14067",
                governingBody = "World Meteorological Organization & IPCC",
                scope = "Surface energy monitoring, radiative transfer validation, urban canopy heat mitigation, and high-precision weather station siting.",
                complianceChecklist = listOf(
                    "Position temperature sensors 1.5m to 2.0m above ground inside ventilated solar radiation shields",
                    "Mandate minimum Solar Reflectance Index (SRI) ≥ 78 for municipal commercial roofing",
                    "Integrate urban canopy permeable pavements with minimum 20% void ratio for evaporative cooling",
                    "Log downward shortwave and longwave net radiometer flux every 60 seconds"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Sub-Ambient Passive Daytime Radiative Cooling Nanopaint",
                    institution = "Purdue University & MIT Mechanical Engineering",
                    leadLocation = "West Lafayette & Cambridge, USA",
                    stage = "Commercial Formulation",
                    summary = "Engineered barium sulfate (BaSO4) nanocomposite coatings achieving 98.1% solar reflectance and high infrared emissivity through the atmospheric transparency window (8-13 µm), cooling surfaces 4.5°C below ambient air under direct noon sun.",
                    publicationOrRef = "ACS Applied Materials & Interfaces 2025"
                ),
                OngoingResearch(
                    title = "Marine Cloud Brightening & Tropospheric Micro-Droplet Physics",
                    institution = "Centre for Climate Repair, University of Cambridge",
                    leadLocation = "Cambridge, UK & Pacific Test Sites",
                    stage = "Controlled Simulation & Wave Basin",
                    summary = "Investigating optimal sub-micron sea-salt aerosol generator nozzles to increase stratocumulus cloud albedo without disrupting regional monsoonal precipitation patterns.",
                    publicationOrRef = "Atmospheric Chemistry and Physics 2026"
                ),
                OngoingResearch(
                    title = "Satellite Hyper-Resolution Thermal Land Surface Mapping",
                    institution = "European Space Agency (ESA) & NASA JPL (ECOSTRESS)",
                    leadLocation = "Noordwijk, Netherlands",
                    stage = "Orbital Radiometer Mission",
                    summary = "Producing 50-meter resolution diurnal thermal stress models over 200 megacities to predict localized microclimate heat mortality risks during extreme heatwaves.",
                    publicationOrRef = "Remote Sensing of Environment 2025"
                )
            ),
            defaultInputPrompt = "Enter Urban Heat Island Temperature Delta in °C:",
            inputUnit = "°C",
            thresholdGuideline = "Optimal microclimate: ≤1.5°C; Elevated heat stress: 1.5-3.5°C; Extreme hazard: >4.0°C"
        ),

        // 5. SOIL CONTROL & HEALTH
        EnvironmentalTopic(
            id = "soil_control",
            domain = EnvironmentalDomain.SOIL_CONTROL,
            title = "Soil Control, Pedology & Biogeochemistry",
            subtitle = "Microbiome Diversity, NPK Stoichiometry, Salinization & Biochar",
            levelBadge = "Basic to Advanced",
            basicDescription = "Soil health defines the capacity of living soil to sustain plant and animal productivity, maintain water quality, and promote terrestrial ecosystem resilience. Key elements include texture, organic carbon, moisture, and root aeration.",
            advancedScience = "Advanced pedology evaluates Cation Exchange Capacity (CEC), rhizosphere microbiome metabolic networks (arbuscular mycorrhizal fungi and plant growth-promoting rhizobacteria), soil electrical conductivity (EC in dS/m for salinization), heavy metal bioavailability, and biological carbon retention via recalcitrant pyrogenic carbon (biochar).",
            primaryIndicators = listOf(
                QualityParameterBand("Soil Organic Matter (SOM)", "3.5 - 6.0", "2.0", "% by Mass", "FAO Soil Health Minimum", 3.8f, 10.0f),
                QualityParameterBand("Soil pH Reaction", "6.2 - 7.2", "5.5 - 8.2", "pH Units", "Optimal Nutrient Availability", 6.8f, 14.0f),
                QualityParameterBand("Cation Exchange Capacity (CEC)", "15 - 30", "10", "meq/100g", "Fertility Benchmark", 19.5f, 40.0f),
                QualityParameterBand("Electrical Conductivity (EC)", "0.2 - 1.2", "2.0", "dS/m", "Salinization Threshold", 0.65f, 4.0f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 11260 / ISO 10390 & FAO Soil Charter",
                certificationCode = "ISO 11260 / USDA Soil Health Card",
                governingBody = "Food and Agriculture Organization (FAO) & USDA NRCS",
                scope = "Agricultural soil conservation, contaminated brownfield reclamation, erosion control, and Regenerative Soil Organic Carbon verification.",
                complianceChecklist = listOf(
                    "Maintain continuous living roots and organic ground cover on ≥80% of agricultural parcels",
                    "Conduct annual grid-based ICP-OES tests for extractable macronutrients (N, P, K, Ca, Mg, S)",
                    "Prevent soil compaction by limiting axle loads below 6 metric tonnes per wheel in wet fields",
                    "Enforce maximum permissible heavy metal limits (Cadmium ≤ 1.5 mg/kg, Lead ≤ 100 mg/kg)"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "CRISPR-Engineered Diazotrophic Endophytes for Cereal Nitrogen Fixation",
                    institution = "Wageningen University & UC Davis Plant Sciences",
                    leadLocation = "Wageningen, Netherlands",
                    stage = "Multi-Location Field Trials",
                    summary = "Developing symbiotic root-colonizing bacterial strains that actively fix atmospheric nitrogen directly into corn and wheat tissues, reducing synthetic chemical fertilizer requirements by 42%.",
                    publicationOrRef = "Nature Biotechnology 2025"
                ),
                OngoingResearch(
                    title = "Engineered Biochar-Mineral Complexes for Millennial Carbon Sequestration",
                    institution = "Max Planck Institute for Biogeochemistry & Cornell SIPS",
                    leadLocation = "Jena, Germany & Ithaca, USA",
                    stage = "Field Lysimeter Studies",
                    summary = "Co-pyrolyzing organic biomass with clay phyllosilicates to produce recalcitrant organo-mineral micro-aggregates that resist microbial oxidation for over 1,000 years in arable soils.",
                    publicationOrRef = "Global Change Biology 2026"
                ),
                OngoingResearch(
                    title = "Synchrotron Micro-XRF Speciation of Cadmium and Arsenic in Paddy Topsoils",
                    institution = "Chinese Academy of Sciences & Swiss Federal Institute (PSI)",
                    leadLocation = "Beijing, China & Villigen, Switzerland",
                    stage = "Advanced Analytical Chemistry",
                    summary = "Mapping sub-cellular root iron-plaque barriers with synchrotron X-ray beams to inhibit toxic heavy metal uptake into food grain crops under alternate wetting and drying regimes.",
                    publicationOrRef = "Environmental Science & Technology 2025"
                )
            ),
            defaultInputPrompt = "Enter Soil Organic Matter (SOM) in %:",
            inputUnit = "% SOM",
            thresholdGuideline = "High organic health: ≥4.0%; Adequate: 2.5-4.0%; Severely depleted: <2.0%"
        ),

        // 6. NOISE QUALITY & ACOUSTICS
        EnvironmentalTopic(
            id = "noise_quality",
            domain = EnvironmentalDomain.NOISE_QUALITY,
            title = "Noise Quality, Psychoacoustics & Soundscapes",
            subtitle = "Decibel Dosimetry, Infrasound, Acoustic Metamaterials & Soundscapes",
            levelBadge = "Basic to Advanced",
            basicDescription = "Noise pollution encompasses unwanted or harmful outdoor and indoor sound generated by transport infrastructure, industrial facilities, and urban human activities. Chronic noise triggers sleep disturbance, hypertension, and stress response.",
            advancedScience = "Advanced architectural and environmental acoustics evaluates A-weighted equivalent continuous sound level (Leq dBA), Day-Evening-Night Level (Lden), Zwicker psychoacoustic sharpness and roughness metrics, infrasound (<20 Hz) structural vibrations, and natural restorative soundscape Biophony-to-Anthrophony ratios.",
            primaryIndicators = listOf(
                QualityParameterBand("Daytime Equivalent Noise (Leq)", "40 - 55", "55", "dBA", "WHO Environmental Noise Limit", 51.5f, 90f),
                QualityParameterBand("Nighttime Noise (Lnight)", "30 - 40", "40", "dBA", "WHO Night Noise Guidelines", 38.0f, 75f),
                QualityParameterBand("Peak Impulse Sound (Lpeak)", "0 - 85", "85", "dBC", "Hearing Conservation Threshold", 74.0f, 120f),
                QualityParameterBand("Natural Soundscape Biophony Ratio", "65 - 100", "40", "% Natural", "Acoustic Ecology Benchmark", 68.0f, 100f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 1996-1/2:2016 & WHO Environmental Noise Guidelines",
                certificationCode = "ISO 1996 / Directive 2002/49/EC",
                governingBody = "International Organization for Standardization & WHO Europe",
                scope = "Environmental noise mapping, strategic noise action plans for agglomerations, and acoustic building insulation specifications.",
                complianceChecklist = listOf(
                    "Calibrate Type 1 / Class 1 acoustic sound level meters before and after every field measurement",
                    "Conduct 24-hour continuous microphone monitoring with 1/3-octave band frequency breakdown",
                    "Mandate porous asphalt road resurfacing reducing vehicular tire-road noise by 4-6 dBA",
                    "Enforce strict quiet zones and acoustic tranquility reserves around healthcare and educational facilities"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Sub-Wavelength Phononic Crystal Noise Barriers for Urban Highways",
                    institution = "Sorbonne University & Hong Kong University of Science and Technology",
                    leadLocation = "Paris, France & Hong Kong",
                    stage = "Full-Scale Roadway Demonstration",
                    summary = "Fabricating ventilated 3D acoustic metamaterial barriers that attenuate 24 dBA of low-frequency engine rumble while maintaining 60% aerodynamic transparency to prevent windstorm structural collapse.",
                    publicationOrRef = "Physical Review Applied 2025"
                ),
                OngoingResearch(
                    title = "Neuro-Cardiovascular Mechanisms of Nocturnal Traffic Noise Perturbations",
                    institution = "University Medical Center Mainz & Imperial College London",
                    leadLocation = "Mainz, Germany & London, UK",
                    stage = "Clinical Human Cohort",
                    summary = "Demonstrating that nocturnal noise events as low as 45 dBA induce acute endothelial dysfunction, systemic oxidative stress, and cortisol spikes via autonomic arousal independent of subjective awakening.",
                    publicationOrRef = "European Heart Journal 2025"
                ),
                OngoingResearch(
                    title = "Edge Deep Learning Soundscape De-mixing on Solar Sensor Pods",
                    institution = "National University of Singapore & Stanford AI Lab",
                    leadLocation = "Singapore",
                    stage = "IoT Field Network",
                    summary = "TinyML acoustic models running on 50mW microcontroller nodes that separate biophony (birds, crickets) from anthrophony (trucks, aircraft) to automate city tranquility indices.",
                    publicationOrRef = "IEEE Transactions on Multimedia 2026"
                )
            ),
            defaultInputPrompt = "Enter Equivalent Continuous Sound Level in dBA:",
            inputUnit = "dBA",
            thresholdGuideline = "Tranquil: ≤45 dBA; WHO residential limit: 53 dBA daytime, 45 dBA nighttime; Severe: >65 dBA"
        ),

        // 7. FOOD MICROBIOLOGY & SAFETY
        EnvironmentalTopic(
            id = "food_microbiology",
            domain = EnvironmentalDomain.FOOD_MICROBIOLOGY,
            title = "Food Microbiology, Pathogen Kinetics & Bio-Preservation",
            subtitle = "Water Activity (aw), Inactivation Kinetics, SERS & Bacteriocins",
            levelBadge = "Basic to Advanced",
            basicDescription = "Food microbiology studies beneficial, spoilage, and pathogenic microorganisms in food products. Strict cold-chain management and moisture control prevent the proliferation of foodborne illness vectors.",
            advancedScience = "Advanced food safety inspects thermal death kinetic models (D-value and z-value), water activity (aw) thermodynamic thresholds, biofilm formation on stainless steel contact surfaces, mycotoxin secondary fungal metabolites (Aflatoxin B1, Ochratoxin A), and biological preservation utilizing lactic acid bacteria bacteriocins.",
            primaryIndicators = listOf(
                QualityParameterBand("Water Activity (aw)", "0.0 - 0.60", "0.85", "aw (0 - 1.0)", "Bacterial Proliferation Bound", 0.58f, 1.0f),
                QualityParameterBand("Total Aerobic Colony Count", "0 - 1000", "10000", "CFU/g", "Codex Hygiene Standard", 850f, 50000f),
                QualityParameterBand("Cold Chain Holding Temperature", "1.0 - 4.0", "4.0", "°C", "HACCP Critical Limit", 2.8f, 12.0f),
                QualityParameterBand("Aflatoxin Total Contamination", "0 - 4.0", "10.0", "µg/kg (ppb)", "EU Food Safety Max", 1.2f, 25.0f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 22000:2018 & Codex Alimentarius HACCP",
                certificationCode = "ISO 22000 / FSSC 22000",
                governingBody = "Codex Alimentarius Commission (FAO/WHO) & US FDA",
                scope = "End-to-end food supply chain hygiene, hazard analysis critical control points (HACCP), and preventative sanitation verification.",
                complianceChecklist = listOf(
                    "Monitor and record digital cold-storage temperature probes continuously every 10 minutes",
                    "Perform monthly ATP bioluminescence swabs on sanitized food contact surfaces",
                    "Conduct zero-tolerance PCR assays for Listeria monocytogenes in ready-to-eat facilities",
                    "Enforce strict supplier certificates of analysis (CoA) for mycotoxin and heavy metal limits"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Surface-Enhanced Raman Spectroscopy (SERS) Microfluidics for 15-Minute Pathogen ID",
                    institution = "Cornell Food Science & Purdue Bioengineering",
                    leadLocation = "Ithaca & West Lafayette, USA",
                    stage = "Handheld Prototype Field TRL 7",
                    summary = "Fabricating gold nanoparticle microfluidic chips that optically capture and identify single Salmonella and E. coli O157:H7 cells in raw agricultural washes in under 15 minutes.",
                    publicationOrRef = "Analytical Chemistry 2025"
                ),
                OngoingResearch(
                    title = "Cold Atmospheric Plasma (CAP) Ionization for Non-Thermal Food Sterilization",
                    institution = "Wageningen Food Safety Research & Dublin Institute of Technology",
                    leadLocation = "Wageningen, Netherlands & Dublin, Ireland",
                    stage = "Pilot Industrial Conveyor",
                    summary = "Utilizing room-temperature ionized gas jets (reactive oxygen and nitrogen species) to achieve a 5-log reduction of surface bacterial endospores on fresh berries without heat damage or sensory degradation.",
                    publicationOrRef = "Innovative Food Science & Emerging Technologies 2025"
                ),
                OngoingResearch(
                    title = "Engineered Bacteriophage Cocktails Targeting Multi-Drug Resistant Biofilms",
                    institution = "ETH Zurich & Johns Hopkins Center for a Livable Future",
                    leadLocation = "Zurich, Switzerland & Baltimore, USA",
                    stage = "Pre-Clinical Validation",
                    summary = "Formulating CRISPR-potentiated lytic bacteriophage suspensions that dissolve persistent Pseudomonas and Listeria biofilms inside food processing pipe geometries.",
                    publicationOrRef = "Nature Food 2026"
                )
            ),
            defaultInputPrompt = "Enter Storage Temperature (°C) or Colony Count (CFU/g):",
            inputUnit = "°C / CFU",
            thresholdGuideline = "Safe cold chain: ≤4.0°C; Pathogen danger zone: 5°C - 60°C; Maximum limit: 10,000 CFU/g"
        ),

        // 8. POLLUTION TESTING
        EnvironmentalTopic(
            id = "pollution_testing",
            domain = EnvironmentalDomain.POLLUTION_TESTING,
            title = "Pollution Testing & Analytical Environmental Chemistry",
            subtitle = "Mass Spectrometry, Chromatography, qPCR & Detection Limits",
            levelBadge = "Basic to Advanced",
            basicDescription = "Pollution testing applies laboratory instrumentation and rapid field screening to identify and quantify chemical, biological, and radiological contaminants in soil, water, air, and tissue matrices.",
            advancedScience = "Advanced analytical chemistry deploys Gas Chromatography-Mass Spectrometry (GC-MS/MS), Ultra-High Performance Liquid Chromatography (UHPLC-Orbitrap), Inductively Coupled Plasma Mass Spectrometry (ICP-MS) for parts-per-trillion (ppt) trace metal detection, and high-throughput real-time quantitative PCR (qPCR) for pathogen copy enumeration.",
            primaryIndicators = listOf(
                QualityParameterBand("Analytical Limit of Detection (LOD)", "0.001 - 0.1", "0.5", "µg/L (ppb)", "Method Detection Benchmark", 0.04f, 1.0f),
                QualityParameterBand("Spike Recovery Precision Rate", "90 - 110", "80 - 120", "% Recovery", "EPA QC Mandate", 98.4f, 130f),
                QualityParameterBand("Calibration Curve Linearity (R²)", "0.995 - 1.000", "0.990", "Coefficient R²", "ISO 17025 Standard", 0.999f, 1.0f),
                QualityParameterBand("Sample Turnaround Time", "2 - 24", "48", "Hours", "Rapid Screening Target", 6.0f, 72f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO/IEC 17025:2017 & EPA SW-846 Compendium",
                certificationCode = "ISO/IEC 17025:2017",
                governingBody = "International Laboratory Accreditation Cooperation (ILAC) & US EPA",
                scope = "General requirements for the competence of testing and calibration laboratories, ensuring defensible, traceable environmental measurement data.",
                complianceChecklist = listOf(
                    "Include 1 procedural blank, 1 duplicate, and 1 matrix spike per batch of 20 analytical samples",
                    "Maintain National Institute of Standards and Technology (NIST) traceable calibration standards",
                    "Conduct blind inter-laboratory proficiency testing schemes twice annually",
                    "Ensure digital chain of custody (CoC) cryptography for all field grab samples"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Field-Deployable CRISPR-Cas12a Paper Biosensors for Sub-Picomolar Pesticides",
                    institution = "Harvard Wyss Institute & University of Tokyo",
                    leadLocation = "Boston, USA & Tokyo, Japan",
                    stage = "Field Prototype Validated",
                    summary = "Developing low-cost lateral flow test strips that employ programmable Cas12a endonuclease collateral cleavage to detect organophosphates and atrazine in under 10 minutes with smartphone camera readouts.",
                    publicationOrRef = "Nature Communications 2025"
                ),
                OngoingResearch(
                    title = "Quantum Cascade Laser (QCL) Multi-Gas Optical Cavity Spectrometers",
                    institution = "Princeton University & Max Born Institute",
                    leadLocation = "Princeton, USA & Berlin, Germany",
                    stage = "Field Instrument",
                    summary = "Utilizing mid-infrared optical cavities with 5-kilometer effective path lengths to detect trace fugitive methane and nitrous oxide leaks at parts-per-trillion sensitivity.",
                    publicationOrRef = "Optica 2025"
                ),
                OngoingResearch(
                    title = "Automated Extraction of Nanoplastics from Biological Tissues via Ultracentrifugation",
                    institution = "University of Vienna & Helmholtz Centre for Environmental Research (UFZ)",
                    leadLocation = "Vienna, Austria & Leipzig, Germany",
                    stage = "Method Standard Proposal",
                    summary = "Establishing standardized density gradient ultracentrifugation coupled to Pyrolysis-GC-MS to reliably quantify nanoplastics down to 20 nanometers in marine fauna and human bio-fluids.",
                    publicationOrRef = "Environmental Pollution 2026"
                )
            ),
            defaultInputPrompt = "Enter Analyte Concentration or Spike Recovery (%):",
            inputUnit = "ppb / %",
            thresholdGuideline = "Trace detection limit: <0.1 ppb; Acceptable recovery: 90-110%; Out of control: <80% or >120%"
        ),

        // 9. POLLUTION MONITORING
        EnvironmentalTopic(
            id = "pollution_monitoring",
            domain = EnvironmentalDomain.POLLUTION_MONITORING,
            title = "Pollution Monitoring, Telemetry & Sensor Networks",
            subtitle = "IoT Mesh Sondes, Satellite Tropospheric Columns & Edge ML",
            levelBadge = "Basic to Advanced",
            basicDescription = "Pollution monitoring involves systematic, repetitive measurement of ambient environmental conditions over time to track spatial gradients, seasonal trends, and regulatory compliance.",
            advancedScience = "Advanced telemetry integrates low-power wireless mesh networks (LoRaWAN / NB-IoT), Sentinel-5P TROPOMI satellite atmospheric column retrievals, Gaussian dispersion plume modeling (AERMOD / CALPUFF), and on-device machine learning calibration models that compensate for temperature and humidity drift on low-cost electrochemical sensors.",
            primaryIndicators = listOf(
                QualityParameterBand("Sensor Network Telemetry Uptime", "98.0 - 100.0", "95.0", "% Active Time", "EPA Data Quality Objective", 99.2f, 100f),
                QualityParameterBand("IoT Sensor Correlation with Reference Sampler", "0.85 - 0.98", "0.75", "Pearson r", "US EPA Sensor Guidebook", 0.91f, 1.0f),
                QualityParameterBand("Spatial Resolution Grid", "100 - 500", "2000", "Meters", "Hyperlocal Urban Grid", 250f, 3000f),
                QualityParameterBand("Data Latency Pipeline", "1 - 15", "60", "Seconds", "Real-Time Ingestion", 5.0f, 120f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "US EPA Air Sensor Guidebook & WMO GAW Data Protocol",
                certificationCode = "EPA-454/B-20-001 / ISO 14040",
                governingBody = "US Environmental Protection Agency & World Meteorological Organization",
                scope = "Quality assurance project plans (QAPP) for low-cost sensor deployments, automated outlier screening algorithms, and public data transparency protocols.",
                complianceChecklist = listOf(
                    "Co-locate IoT sensor nodes with reference federal equivalent method (FEM) stations for 30 days prior to deployment",
                    "Apply dynamic humidity and temperature correction polynomials trained on regional calibration datasets",
                    "Automate instantaneous flag filters for sensor baseline drift, negative values, and stuck outputs",
                    "Publish cryptographic open data APIs adhering to FAIR data principles (Findable, Accessible, Interoperable, Reusable)"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Spatio-Temporal Graph Neural Networks for 10-Meter Urban Air Pollution Forecasting",
                    institution = "University College London & Google Research Climate AI",
                    leadLocation = "London, UK & Mountain View, USA",
                    stage = "Active Production Deployment",
                    summary = "Fusing satellite radiometry, city camera traffic density, and municipal sensor feeds through spatial GNN architectures to deliver 24-hour predictive 10-meter street-canyon pollution maps.",
                    publicationOrRef = "Nature Machine Intelligence 2025"
                ),
                OngoingResearch(
                    title = "Autonomous Long-Endurance Aquatic Drone Sondes for Estuary Plumes",
                    institution = "Woods Hole Oceanographic Institution & MBARI",
                    leadLocation = "Woods Hole, USA",
                    stage = "Oceanographic Deployment",
                    summary = "Deploying wave-propelled autonomous surface vessels equipped with multiparameter spectrophotometric sondes that map river agricultural nutrient runoff plumes continuously for 6 months.",
                    publicationOrRef = "Frontiers in Marine Science 2025"
                ),
                OngoingResearch(
                    title = "Drone-Mounted Optical Emission Spectroscopy for Industrial Flare Combustion",
                    institution = "Fraunhofer Institute for Physical Measurement Techniques & Colorado State University",
                    leadLocation = "Freiburg, Germany & Fort Collins, USA",
                    stage = "Commercial Inspection Standard",
                    summary = "Flying automated hexacopters that measure flare flame radiant emission lines in real time to quantify unburned methane slip and volatile organic compound destruction efficiency.",
                    publicationOrRef = "Atmospheric Measurement Techniques 2026"
                )
            ),
            defaultInputPrompt = "Enter Telemetry Sensor Uptime (%) or Station Name:",
            inputUnit = "% Uptime",
            thresholdGuideline = "Production SLA: ≥98% uptime; Acceptable: 95-98%; Data invalidation risk: <95%"
        ),

        // 10. POLLUTION REMEDIATION
        EnvironmentalTopic(
            id = "pollution_remediation",
            domain = EnvironmentalDomain.POLLUTION_REMEDIATION,
            title = "Pollution Remediation, Bioremediation & Green Tech",
            subtitle = "Phytoremediation, Hydrocarbon Degraders, Nanoscale ZVI & DAC",
            levelBadge = "Basic to Advanced",
            basicDescription = "Pollution remediation removes, halts, or neutralizes hazardous contaminants from environmental media (soil, groundwater, sediment, surface water, and air) using physical, chemical, and biological technologies.",
            advancedScience = "Advanced remediation deploys hyperaccumulating phytoextraction plants (e.g. *Brassica juncea* for heavy metals), hydrocarbonoclastic bacterial consortia (*Pseudomonas putida*), in-situ zero-valent iron (nZVI) reductive dehalogenation of trichloroethylene, and industrial direct air carbon capture (DAC) paired with basaltic mineralization.",
            primaryIndicators = listOf(
                QualityParameterBand("Contaminant Mass Removal Efficiency", "85 - 99", "80", "% Total Mass Removed", "Superfund Record of Decision", 92.5f, 100f),
                QualityParameterBand("Bioavailability Reduction Rate", "75 - 95", "70", "% Inactivated", "Bio-Stabilization Target", 84.0f, 100f),
                QualityParameterBand("Remediation Carbon Intensity", "0 - 15", "50", "kg CO2e / m³ treated", "Sustainable Remediation (SuRF)", 11.2f, 100f),
                QualityParameterBand("Post-Remediation Soil Toxicity Index", "0 - 5", "10", "% Microtox Inhibition", "Safe Ecological Handover", 2.8f, 30f)
            ),
            qualityManagement = QualityManagementSystem(
                frameworkName = "ISO 18504:2017 & US EPA Superfund Remedial Standards",
                certificationCode = "ISO 18504 / ASTM E2876",
                governingBody = "International Organization for Standardization & US EPA OLEM",
                scope = "Sustainable remediation procedures for contaminated soil and groundwater, balancing net environmental benefit, energy footprint, and long-term stewardship.",
                complianceChecklist = listOf(
                    "Conduct pilot-scale treatability studies before full field mobilization",
                    "Perform quarterly down-gradient monitoring well sampling for secondary daughter products (e.g. vinyl chloride from TCE)",
                    "Implement closed-loop vapor recovery on all thermal desorption and air-sparging units",
                    "Conduct post-remediation ecotoxicity bioassays before lifting land-use covenant restrictions"
                )
            ),
            ongoingResearches = listOf(
                OngoingResearch(
                    title = "Synthetic Biology Consortia for Mixed Radiological & Hydrocarbon Waste Degradation",
                    institution = "Lawrence Berkeley National Laboratory & Oak Ridge National Lab",
                    leadLocation = "Berkeley & Oak Ridge, USA",
                    stage = "Bio-Containment Bioreactor",
                    summary = "Engineering radiation-resistant Deinococcus radiodurans and Pseudomonas consortia capable of degrading chlorinated solvents in high-gamma-radiation contaminated subterranean plumes.",
                    publicationOrRef = "Nature Microbiology 2025"
                ),
                OngoingResearch(
                    title = "Ocean Alkalinity Enhancement (OAE) via Coastal Olivine Weathering",
                    institution = "Woods Hole Oceanographic Institution & GEOMAR Helmholtz Centre",
                    leadLocation = "Woods Hole, USA & Kiel, Germany",
                    stage = "Controlled Mesocosm Trials",
                    summary = "Measuring dissolution kinetics of fast-weathering alkaline silicate minerals in wave breaker zones to safely neutralize ocean acidification and draw down atmospheric CO2 at gigatonne scales.",
                    publicationOrRef = "Science Advances 2026"
                ),
                OngoingResearch(
                    title = "Polysaccharide-Coated Nanoscale Zero-Valent Iron (nZVI) for In-Situ Aquifer Cleansing",
                    institution = "McGill University & University of Vienna Environmental Geosciences",
                    leadLocation = "Montreal, Canada & Vienna, Austria",
                    stage = "Deep Subsurface Field Trial",
                    summary = "Formulating biopolymer-stabilized iron nanoparticles that migrate through dense clay aquifers to rapidly dechlorinate persistent solvent plumes without clogging pore spaces.",
                    publicationOrRef = "Environmental Science: Nano 2025"
                )
            ),
            defaultInputPrompt = "Enter Contaminant Removal Efficiency (%) or Area Treated:",
            inputUnit = "% Removal",
            thresholdGuideline = "High remediation target: ≥90%; Moderate: 75-90%; Insufficient remediation: <70%"
        )
    )

    fun getTopicsForDomain(domain: EnvironmentalDomain): List<EnvironmentalTopic> {
        return if (domain == EnvironmentalDomain.ALL) {
            allTopics
        } else {
            allTopics.filter { it.domain == domain }
        }
    }

    fun getTopicById(id: String): EnvironmentalTopic? {
        return allTopics.find { it.id == id }
    }
}
