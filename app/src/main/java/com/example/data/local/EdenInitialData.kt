package com.example.data.local

import com.example.data.model.CompetencyEntity
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity

object EdenInitialData {

    val knowledgeNodes = listOf(
        KnowledgeEntity(
            id = "bod_concept",
            title = "Biochemical Oxygen Demand (BOD)",
            category = "Water Intelligence",
            summary = "Measures the amount of dissolved oxygen consumed by aerobic microorganisms during the decomposition of organic matter in water.",
            scientificContext = "Organic matter (C_n H_a O_b N_c) is biochemically oxidized to CO2, H2O, and NH3 by heterotrophic bacteria: Organic + O2 -> CO2 + H2O + New Cells. The standard incubation test measures dissolved oxygen depletion after 5 days at 20°C in the dark to prevent photosynthetic oxygen production.",
            formulaOrStandard = "BOD₅ (mg/L) = (DO₀ - DO₅) / P, where P is the decimal volumetric dilution factor. Standard Method 5210 B.",
            authoritativeSource = "Standard Methods for the Examination of Water and Wastewater (APHA / AWWA / WEF)",
            sourceYear = 2023,
            authority = "APHA / EPA",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "COD, Dissolved Oxygen, Wastewater Treatment, Population Equivalent, Eutrophication",
            level1Basic = "BOD is an indicator of organic water pollution. High BOD means bacteria consume so much oxygen that fish and aquatic life suffocate.",
            level2Scientific = "BOD kinetics follow first-order reaction rates: BOD_t = L_0 * (1 - e^(-k*t)), where L_0 is ultimate carbonaceous BOD and k is deoxygenation rate constant (typically 0.15 - 0.23 day⁻¹ at 20°C).",
            level3Laboratory = "BOD₅ is tested in standard 300 mL glass bottles incubated in dark at 20 ± 1°C for 5 days. Initial and final DO are measured using polarographic membrane probe or Winkler titration. Nitrification inhibitor (TCMP) is added if nitrogenous BOD must be suppressed.",
            level4Engineering = "Used to size activated sludge aeration basins and trickling filters. Volumetric loading: L_v = (Q * S_0) / V (kg BOD/m³·day). Typical municipal raw sewage has 200-300 mg/L BOD₅; treated effluent target is < 20-30 mg/L.",
            level5Regulatory = "Enforced under US Clean Water Act NPDES permits (40 CFR Part 133) and EU Urban Wastewater Directive (91/271/EEC), requiring secondary treatment standards (typically 85% removal or <25 mg/L)."
        ),
        KnowledgeEntity(
            id = "cod_concept",
            title = "Chemical Oxygen Demand (COD)",
            category = "Water Intelligence",
            summary = "Total equivalent oxygen required to chemically oxidize organic and oxidizable inorganic matter using a strong chemical oxidant.",
            scientificContext = "While BOD only measures biologically degradable matter over 5 days, COD oxidizes virtually all organics (both biodegradable and refractory/toxic) within 2 hours using boiling potassium dichromate (K2Cr2O7) in 50% sulfuric acid with silver sulfate (Ag2SO4) catalyst and mercuric sulfate (HgSO4) to mask chloride interference.",
            formulaOrStandard = "Organic + Cr2O7²⁻ + 8H⁺ -> 2Cr³⁺ + 4H2O + CO2. Standard Method 5220 D / ISO 6060.",
            authoritativeSource = "Standard Methods for the Examination of Water and Wastewater / ISO",
            sourceYear = 2022,
            authority = "ISO / EPA",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "BOD, Biodegradability Index, Industrial Effluent, TOC (Total Organic Carbon)",
            level1Basic = "COD is a rapid chemical test (2 hours vs 5 days for BOD) showing the total chemical pollution in wastewater.",
            level2Scientific = "COD reflects both biodegradable and non-biodegradable carbonaceous compounds, as well as oxidizable inorganic ions (Fe²⁺, S²⁻, NO2⁻).",
            level3Laboratory = "Closed reflux colorimetric method: Sample digested with digestion solution containing K2Cr2O7 and Ag2SO4 at 150°C for 2 hours. Chromate reduction to Cr³⁺ measured spectrophotometrically at 600 nm.",
            level4Engineering = "The BOD₅/COD ratio is the key biodegradability metric: Ratio > 0.5 indicates readily biodegradable wastewater; 0.2 - 0.5 indicates moderate biodegradability; < 0.2 signifies recalcitrant/toxic substances requiring advanced oxidation (AOP) or chemical treatment.",
            level5Regulatory = "Mandated in industrial discharge permits. EU Industrial Emissions Directive (IED) BAT-AELs set typical COD discharge limits between 50 to 125 mg/L depending on sector."
        ),
        KnowledgeEntity(
            id = "pm25_concept",
            title = "Fine Particulate Matter (PM2.5)",
            category = "Air Quality Intelligence",
            summary = "Airborne microscopic particles with aerodynamic diameter less than or equal to 2.5 micrometers, penetrating deep into alveolar pulmonary tissue.",
            scientificContext = "PM2.5 comprises primary particles (elemental carbon, heavy metals, fly ash) and secondary aerosols formed photochemically from gaseous precursors (SO2 -> sulfates, NOx -> nitrates, VOCs -> secondary organic aerosols SOA). Their small size allows penetration into alveoli and capillary blood circulation.",
            formulaOrStandard = "Gravimetric Reference Method (40 CFR Part 50 App L); Beta Attenuation Monitor (BAM); Optical Particle Sizing.",
            authoritativeSource = "WHO Global Air Quality Guidelines / US EPA NAAQS",
            sourceYear = 2021,
            authority = "WHO / US EPA",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "PM10, AQI, Secondary Aerosols, Black Carbon, Pulmonary Morbidity",
            level1Basic = "Tiny airborne particles 30 times thinner than a human hair that enter deep into lungs and bloodstream, causing cardiac and respiratory disease.",
            level2Scientific = "Light scattering by PM2.5 falls within the Mie scattering regime (wavelength ~ 500 nm), causing visual haze and atmospheric radiative forcing.",
            level3Laboratory = "Reference method draws 16.67 L/min through a PM2.5 impactor jet onto PTFE membrane filter for 24 hours in conditioned balance room (20-23°C, 30-40% RH) with microgram balance.",
            level4Engineering = "Controlled using high-efficiency pulse-jet baghouse fabric filters (PTFE membrane sleeves, 99.9% efficiency), electrostatic precipitators (ESP), and wet scrubbers with venturi throats.",
            level5Regulatory = "WHO 2021 Guideline: 5 µg/m³ annual mean, 15 µg/m³ 24-hr mean. US EPA NAAQS (revised 2024): 9.0 µg/m³ annual mean, 35 µg/m³ 24-hr design value."
        ),
        KnowledgeEntity(
            id = "scope1_concept",
            title = "Scope 1 Direct GHG Emissions",
            category = "Carbon Intelligence",
            summary = "Direct greenhouse gas emissions from operations owned or controlled by the reporting organization.",
            scientificContext = "Scope 1 emissions encompass: stationary combustion (boilers, furnaces), mobile combustion (fleet vehicles), industrial process emissions (calcination in cement, blast furnace reduction in steel), and fugitive releases (HFC refrigerants, methane pipeline leaks). Stoichiometric combustion: C_x H_y + (x + y/4) O2 -> x CO2 + (y/2) H2O.",
            formulaOrStandard = "Emissions (kg CO₂e) = Activity Data (units) × Emission Factor (kg CO₂e/unit) × (1 - Oxidation Factor). GHG Protocol Corporate Standard.",
            authoritativeSource = "GHG Protocol Corporate Accounting and Reporting Standard / IPCC Guidelines for National GHG Inventories",
            sourceYear = 2023,
            authority = "GHG Protocol / IPCC",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "Scope 2, Scope 3, GHG Inventory, Emission Factors, GWP100",
            level1Basic = "Direct pollution coming straight from company-owned chimneys, engines, boilers, and vehicle exhaust pipes.",
            level2Scientific = "Calculated using IPCC Tier 1 (default emission factors), Tier 2 (country-specific fuel characteristics), or Tier 3 (facility-level continuous emission monitoring CEMS and fuel elemental analysis).",
            level3Laboratory = "Fuel gross calorific value (GCV/NCV) measured via bomb calorimeter (ASTM D5865); carbon content determined via ultimate elemental analysis (ASTM D5373).",
            level4Engineering = "Mitigated via fuel switching (coal to natural gas, or natural gas to biomethane/green hydrogen), electrification of industrial heat pumps, and carbon capture & storage (CCS).",
            level5Regulatory = "Required under EU ETS (Directive 2003/87/EC), US EPA Mandatory GHG Reporting Rule (40 CFR Part 98), and CSRD (Corporate Sustainability Reporting Directive ESRS E1)."
        ),
        KnowledgeEntity(
            id = "scope2_concept",
            title = "Scope 2 Indirect Electricity Emissions",
            category = "Carbon Intelligence",
            summary = "Indirect greenhouse gas emissions resulting from the generation of purchased electricity, steam, heating, or cooling consumed by the organization.",
            scientificContext = "Scope 2 exists physically at the power plant stack where electricity was produced, but is accounted for in the consumer's inventory because their demand drives generation. Accounting requires dual reporting: Location-Based (grid average emissions factor) and Market-Based (supplier-specific contractual instruments: EACs, RECs, PPAs).",
            formulaOrStandard = "Scope 2 (kg CO₂e) = Electricity Consumed (MWh) × Grid Emission Factor (kg CO₂e/MWh). GHG Protocol Scope 2 Guidance.",
            authoritativeSource = "GHG Protocol Scope 2 Guidance / IEA Emission Factors",
            sourceYear = 2022,
            authority = "GHG Protocol",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "Scope 1, Scope 3, Grid Emission Factor, PPA, Marginal Emissions Factor",
            level1Basic = "Emissions created at the power plant that generates the electricity your facility switches on and uses.",
            level2Scientific = "Distinguishes between average grid emission factors (OM/BM grid mix) and marginal emission factors (emissions caused by the next dispatchable power unit during load changes).",
            level3Laboratory = "Power meters certified to IEC 62053-22 Class 0.2S for revenue-grade electrical consumption tracking.",
            level4Engineering = "Decarbonized via on-site solar PV + battery storage (BESS), energy efficiency measures (VFD motors, LED, heat recovery), and off-site Corporate Power Purchase Agreements (CPPA).",
            level5Regulatory = "Mandatory disclosure under SEC climate rule, CSRD ESRS E1, CDP, and Science-Based Targets initiative (SBTi)."
        ),
        KnowledgeEntity(
            id = "scope3_concept",
            title = "Scope 3 Value Chain Emissions",
            category = "Carbon Intelligence",
            summary = "All other indirect emissions throughout the upstream and downstream value chain across 15 distinct categories.",
            scientificContext = "Scope 3 represents 70-90% of total carbon footprint for most companies. It spans 15 categories: 8 Upstream (Purchased goods & services, Capital goods, Fuel & energy activities, Upstream transport, Waste generated in operations, Business travel, Employee commuting, Upstream leased assets) and 7 Downstream (Downstream transport, Processing of sold products, Use of sold products, End-of-life treatment, Downstream leased assets, Franchises, Investments).",
            formulaOrStandard = "GHG Protocol Corporate Value Chain (Scope 3) Standard; ISO 14064-1:2018 Category 3-6.",
            authoritativeSource = "GHG Protocol / ISO 14064-1",
            sourceYear = 2023,
            authority = "GHG Protocol / ISO",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "Scope 1, Scope 2, Life Cycle Assessment, Supply Chain Decarbonization, Product Carbon Footprint",
            level1Basic = "All carbon emissions from your suppliers, product delivery, customer usage, and product disposal.",
            level2Scientific = "Quantified via hybrid life cycle assessment methods combining process-based LCA with environmentally extended input-output (EEIO) models.",
            level3Laboratory = "Supplier primary activity data collection through Supplier Environmental Data Sheets and certified EPDs (Environmental Product Declarations).",
            level4Engineering = "Interventions include circular product design, material lightweighting, recycled content substitution, and route optimization in multimodal freight logistics.",
            level5Regulatory = "Required by European CSRD (ESRS E1), California SB 253, and ISSB IFRS S2 standards with reasonable assurance phase-in."
        ),
        KnowledgeEntity(
            id = "lca_framework",
            title = "Life Cycle Assessment (LCA - ISO 14040/14044)",
            category = "LCA Intelligence",
            summary = "Methodology for compiling and evaluating the inputs, outputs, and potential environmental impacts of a product system throughout its entire life cycle.",
            scientificContext = "The four iterative phases: 1) Goal & Scope Definition (functional unit, system boundary: cradle-to-grave, cradle-to-gate), 2) Life Cycle Inventory (LCI - mass and energy balance of all materials, emissions, and waste), 3) Life Cycle Impact Assessment (LCIA - characterization into midpoint categories like GWP, acidification, eutrophication, and endpoint damage: human health, ecosystems), 4) Interpretation. Data quality tracking distinguishes measured -> calculated -> estimated -> assumed.",
            formulaOrStandard = "ISO 14040:2006 & ISO 14044:2006; ReCiPe 2016; EF 3.1.",
            authoritativeSource = "International Organization for Standardization (ISO) / European Commission JRC",
            sourceYear = 2021,
            authority = "ISO / JRC",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "Carbon Footprint, EPD, Circular Economy, ReCiPe 2016, Functional Unit",
            level1Basic = "Analyzing the complete environmental footprint of a product from raw material extraction to final disposal.",
            level2Scientific = "Impact score = Sum_i (LCI_i * Characterization Factor_i). For example, GWP100 characterizes CH4 with factor 27.9 and N2O with factor 273 relative to CO2.",
            level3Laboratory = "Testing chemical composition, heavy metal leaching (EN 12457), embodied energy, and primary material purity.",
            level4Engineering = "Used in eco-design: identifying environmental 'hotspots' in product manufacturing and running sensitivity scenarios on alternative materials.",
            level5Regulatory = "Basis for ISO 14025 Type III Environmental Product Declarations (EPD), EU Ecodesign for Sustainable Products Regulation (ESPR), and Product Environmental Footprint (PEF)."
        ),
        KnowledgeEntity(
            id = "stack_emissions",
            title = "Air Stack Emission & Dispersion",
            category = "Air Quality Intelligence",
            summary = "Quantification of industrial chimney pollutant mass rates, gas velocities, and atmospheric Gaussian plume dispersion.",
            scientificContext = "Stack gas velocity is measured via Pitot tube and differential pressure manometer according to EPA Method 2. Volumetric flow rate Q (m³/s) = Velocity (m/s) * Cross-sectional Duct Area (m²). Mass emission rate E (g/s) = Q * Concentration (mg/m³) * 10⁻³. Atmospheric dispersion uses the Gaussian plume model to calculate ground-level concentration: C(x,y,z) = (Q / (2*pi*u*sigma_y*sigma_z)) * exp(-y²/(2*sigma_y²)) * [exp(-(z-H)²/(2*sigma_z²)) + exp(-(z+H)²/(2*sigma_z²))].",
            formulaOrStandard = "EPA Method 1-5 (40 CFR Part 60); ISC3 / AERMOD dispersion model guidelines.",
            authoritativeSource = "US EPA Guidelines on Air Quality Models (40 CFR Part 51 App W)",
            sourceYear = 2023,
            authority = "US EPA",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "PM2.5, SO2, Isokinetic Sampling, AERMOD, Flue Gas Scrubbing",
            level1Basic = "Calculating how much smoke and pollutants leave a factory chimney and how wind spreads them across the city.",
            level2Scientific = "Requires isokinetic sampling: nozzle intake velocity must match duct gas velocity within 100 ± 10% to prevent inertial particle bias.",
            level3Laboratory = "Moisture determined gravimetrically using chilled impingers (EPA Method 4); molecular weight determined from CO2/O2/N2 Orsat analysis (EPA Method 3).",
            level4Engineering = "Designed with minimum stack exit velocity (typically > 15 m/s) and stack height > 2.5 times building height to avoid building downwash and cavity entrainment.",
            level5Regulatory = "Continuous Emission Monitoring Systems (CEMS) compliance certified under 40 CFR Part 60/75 with quarterly cylinder gas audits (CGA) and RATA (Relative Accuracy Test Audit)."
        ),
        KnowledgeEntity(
            id = "waste_hierarchy",
            title = "Waste Hierarchy & Circular Economy",
            category = "Waste Intelligence",
            summary = "Prioritized framework for sustainable resource and waste management: Prevention, Preparing for Reuse, Recycling, Recovery, Disposal.",
            scientificContext = "Established under EU Directive 2008/98/EC. Shifts linear 'take-make-dispose' model to circular regenerative cycles. Technical nutrients (metals, plastics) undergo closed-loop mechanical or chemical recycling; biological nutrients (food waste, sludge) undergo anaerobic digestion (producing biomethane CH4) and composting for soil conditioning.",
            formulaOrStandard = "EU Waste Framework Directive (2008/98/EC); ISO 59020 (Circular Economy).",
            authoritativeSource = "European Environment Agency (EEA) / Ellen MacArthur Foundation",
            sourceYear = 2022,
            authority = "EEA / ISO",
            evidenceTier = "Tier 1: Regulatory Standard",
            relatedConcepts = "Hazardous Waste, Plastic Recycling, Anaerobic Digestion, Extended Producer Responsibility (EPR)",
            level1Basic = "The golden order: 1. Don't produce waste, 2. Reuse items, 3. Recycle materials, 4. Recover energy, and only as a last resort 5. Landfill.",
            level2Scientific = "Landfilled organic waste undergoes anaerobic decomposition generating landfill gas (LFG: ~50% CH4, 50% CO2); fugitive methane has 28x higher warming potential than CO2.",
            level3Laboratory = "Toxicity Characteristic Leaching Procedure (TCLP - EPA Method 1311) to classify waste as hazardous vs non-hazardous prior to disposal.",
            level4Engineering = "Engineered sanitary landfills require composite liners (geomembrane HDPE + 0.6m compacted clay), leachate collection systems, and active gas extraction flaring systems.",
            level5Regulatory = "Basel Convention on transboundary movement of hazardous waste; Extended Producer Responsibility (EPR) laws mandating packaging take-back quotas."
        )
    )

    val resources = listOf(
        ResourceEntity(
            id = "res_who_aqg",
            title = "WHO Global Air Quality Guidelines: Particulate Matter, Ozone, Nitrogen Dioxide, Sulfur Dioxide and Carbon Monoxide",
            authorOrOrganization = "World Health Organization",
            year = 2021,
            topic = "Air Quality Intelligence",
            subtopic = "Health Criteria & Ambient Standards",
            resourceType = "Guidelines",
            url = "https://www.who.int/publications/i/item/9789240034228",
            authority = "WHO",
            evidenceLevel = "Tier 1: Regulatory / Global Standard",
            keyFindings = "Updated annual PM2.5 threshold to 5 µg/m³ and 24-hr to 15 µg/m³ based on systematic review of 500+ epidemiological studies demonstrating adverse vascular mortality even at low exposures."
        ),
        ResourceEntity(
            id = "res_ipcc_ar6",
            title = "IPCC Sixth Assessment Report (AR6): The Physical Science Basis",
            authorOrOrganization = "Intergovernmental Panel on Climate Change",
            year = 2021,
            topic = "Climate Intelligence",
            subtopic = "GHG Metrics & Global Warming Potentials",
            resourceType = "Technical Report",
            url = "https://www.ipcc.ch/report/ar6/wg1/",
            authority = "IPCC / WMO / UNEP",
            evidenceLevel = "Tier 1: Authoritative Scientific Consensus",
            keyFindings = "Updated 100-year GWP values: Fossil Methane GWP100 = 29.8 (27.2 non-fossil), Nitrous Oxide N2O GWP100 = 273; confirmed human influence has unequivocally warmed atmosphere, ocean, and land."
        ),
        ResourceEntity(
            id = "res_ghg_protocol",
            title = "A Corporate Accounting and Reporting Standard (Revised Edition)",
            authorOrOrganization = "World Resources Institute & WBCSD",
            year = 2015,
            topic = "Carbon Intelligence",
            subtopic = "GHG Accounting Standards",
            resourceType = "Standard",
            url = "https://ghgprotocol.org/corporate-standard",
            authority = "WRI / WBCSD",
            evidenceLevel = "Tier 1: Global Corporate Standard",
            keyFindings = "Defined operational boundaries for Scope 1 direct, Scope 2 indirect electricity, and Scope 3 value chain emissions with strict accounting principles: relevance, completeness, consistency, transparency, accuracy."
        ),
        ResourceEntity(
            id = "res_standard_methods_water",
            title = "Standard Methods for the Examination of Water and Wastewater (24th Edition)",
            authorOrOrganization = "APHA, AWWA, WEF",
            year = 2023,
            topic = "Water Intelligence",
            subtopic = "Analytical Laboratory Protocols",
            resourceType = "Standard",
            url = "https://www.standardmethods.org/",
            authority = "APHA / EPA",
            evidenceLevel = "Tier 1: Primary Laboratory Standard",
            keyFindings = "Definitive global standard for 5-day BOD (Method 5210 B), Closed Reflux COD (Method 5220 D), Total Suspended Solids (Method 2540 D), and Optical Dissolved Oxygen (Method 4500-O)."
        ),
        ResourceEntity(
            id = "res_iso_14040",
            title = "ISO 14040 / ISO 14044: Environmental Management — Life Cycle Assessment",
            authorOrOrganization = "International Organization for Standardization",
            year = 2020,
            topic = "LCA Intelligence",
            subtopic = "LCA Principles & Framework",
            resourceType = "Standard",
            url = "https://www.iso.org/standard/37456.html",
            authority = "ISO",
            evidenceLevel = "Tier 1: International Standard",
            keyFindings = "Governs the 4 phases of LCA: Goal & Scope, LCI, LCIA, and Interpretation. Mandates transparency in allocation procedures and tracking of data quality pedigree."
        ),
        ResourceEntity(
            id = "res_epa_cwa_npdes",
            title = "US EPA National Pollutant Discharge Elimination System (NPDES) Permit Writers' Manual",
            authorOrOrganization = "US Environmental Protection Agency",
            year = 2022,
            topic = "Water Intelligence",
            subtopic = "Discharge Limits & Compliance",
            resourceType = "Technical Report",
            url = "https://www.epa.gov/npdes",
            authority = "US EPA",
            evidenceLevel = "Tier 1: Federal Regulatory Standard",
            keyFindings = "Outlines secondary treatment limits for publicly owned treatment works (POTWs): 30-day average BOD5 <= 30 mg/L (or 85% removal), TSS <= 30 mg/L, pH 6.0 - 9.0."
        ),
        ResourceEntity(
            id = "res_drawdown_review",
            title = "Project Drawdown: 100 Technical Solutions for Climate Stabilization",
            authorOrOrganization = "Project Drawdown Research Consortium",
            year = 2023,
            topic = "Climate Intelligence",
            subtopic = "Gigaton Emission Reduction Modeling",
            resourceType = "Research Project",
            url = "https://drawdown.org/solutions",
            authority = "Project Drawdown",
            evidenceLevel = "Tier 1: Multi-Disciplinary Consensus",
            keyFindings = "Levelized cost analysis of 100 proven technologies (solar PV, regenerative agriculture, refrigerant management, onshore wind) capable of achieving global net-zero."
        ),
        ResourceEntity(
            id = "res_unep_emissions_gap",
            title = "UNEP Emissions Gap Report: Closing the 1.5°C Ambition Gap",
            authorOrOrganization = "United Nations Environment Programme (UNEP)",
            year = 2024,
            topic = "Climate Intelligence",
            subtopic = "Global Policy & Emissions Trajectories",
            resourceType = "Magazine & Report",
            url = "https://www.unep.org/resources/emissions-gap-report",
            authority = "UNEP",
            evidenceLevel = "Tier 1: Intergovernmental Assessment",
            keyFindings = "Annual global greenhouse gas emissions reached 57.1 Gt CO2e; highlights essential 42% reduction by 2030 across G20 industrial nations to preserve Paris goals."
        ),
        ResourceEntity(
            id = "res_circular_economy",
            title = "Towards the Circular Economy: Accelerating Economic and Industrial Decoupling",
            authorOrOrganization = "Ellen MacArthur Foundation & McKinsey & Company",
            year = 2022,
            topic = "Waste Intelligence",
            subtopic = "Industrial Symbiosis & Material Loops",
            resourceType = "Environmental Essay",
            url = "https://www.ellenmacarthurfoundation.org/",
            authority = "Ellen MacArthur Foundation",
            evidenceLevel = "Tier 2: Peer-Reviewed Economic Whitepaper",
            keyFindings = "Presents systemic framework replacing linear 'take-make-waste' with restorative loops, saving up to 700 billion USD annually in materials across consumer industries."
        ),
        ResourceEntity(
            id = "res_epa_ap42",
            title = "US EPA AP-42: Compilation of Air Pollutant Emission Factors (5th Edition)",
            authorOrOrganization = "US Environmental Protection Agency Office of Air Quality",
            year = 2023,
            topic = "Air Quality Intelligence",
            subtopic = "Industrial Source Emission Factors",
            resourceType = "Technical Theory",
            url = "https://www.epa.gov/air-emissions-factors-and-quantification/ap-42-compilation-air-emissions-factors",
            authority = "US EPA",
            evidenceLevel = "Tier 1: Regulatory Standard",
            keyFindings = "Deterministic emission factor database (A to E quality ratings) covering stationary combustion, chemical manufacturing, mineral products, and petroleum refining."
        ),
        ResourceEntity(
            id = "res_metcalf_eddy_study",
            title = "Biological Wastewater Treatment Kinetics and Activated Sludge Modeling",
            authorOrOrganization = "Metcalf & Eddy Environmental Engineering Research Group",
            year = 2023,
            topic = "Water Intelligence",
            subtopic = "Aeration Tank Hydraulics & HRT",
            resourceType = "Study Module",
            url = "https://www.wef.org/",
            authority = "Water Environment Federation",
            evidenceLevel = "Tier 1: Engineering Text & Module",
            keyFindings = "Comprehensive module on Monod kinetic parameters, mean cell residence time (MCRT / solids retention time), biological nutrient removal (BNR), and hydraulic retention time (HRT)."
        ),
        ResourceEntity(
            id = "res_nature_carbon_paper",
            title = "Nature Climate Change: Global Carbon Budget and Fossil Trajectories",
            authorOrOrganization = "Global Carbon Project & University of Exeter",
            year = 2024,
            topic = "Carbon Intelligence",
            subtopic = "Atmospheric Inversion & Carbon Sinks",
            resourceType = "Research Paper",
            url = "https://www.nature.com/nclimate/",
            authority = "Nature Publishing Group",
            evidenceLevel = "Tier 1: Peer-Reviewed Scientific Paper",
            keyFindings = "Quantifies remaining global carbon budget (~250 Gt CO2 for 50% chance of limiting warming to 1.5°C); monitors land and ocean sink efficiency variations."
        )
    )

    val competencies = listOf(
        CompetencyEntity(
            id = "comp_water_quality",
            title = "Water Quality & Wastewater Engineering",
            category = "Water Intelligence",
            progressPercent = 40,
            targetLevel = 5,
            isCompleted = false
        ),
        CompetencyEntity(
            id = "comp_carbon_accounting",
            title = "GHG Protocol & Carbon Accounting (Scope 1-3)",
            category = "Carbon Intelligence",
            progressPercent = 60,
            targetLevel = 5,
            isCompleted = false
        ),
        CompetencyEntity(
            id = "comp_air_dispersion",
            title = "Air Quality Standards & Stack Dispersion",
            category = "Air Quality Intelligence",
            progressPercent = 25,
            targetLevel = 4,
            isCompleted = false
        ),
        CompetencyEntity(
            id = "comp_lca_assessment",
            title = "Life Cycle Assessment (ISO 14040/44)",
            category = "LCA Intelligence",
            progressPercent = 15,
            targetLevel = 4,
            isCompleted = false
        ),
        CompetencyEntity(
            id = "comp_waste_compliance",
            title = "Hazardous Waste Classification & Circular Systems",
            category = "Waste Intelligence",
            progressPercent = 30,
            targetLevel = 3,
            isCompleted = false
        )
    )

    val sampleMonitoringPoints = listOf(
        MonitoringEntity(
            parameter = "PM2.5",
            value = 18.4,
            unit = "µg/m³",
            location = "Station 1 - Urban Center",
            standardLimit = 15.0,
            standardSource = "WHO 2021 24h Guideline: 15 µg/m³"
        ),
        MonitoringEntity(
            parameter = "PM10",
            value = 36.2,
            unit = "µg/m³",
            location = "Station 1 - Urban Center",
            standardLimit = 45.0,
            standardSource = "WHO 2021 24h Guideline: 45 µg/m³"
        ),
        MonitoringEntity(
            parameter = "SO2",
            value = 22.0,
            unit = "µg/m³",
            location = "Station 2 - Industrial Zone",
            standardLimit = 40.0,
            standardSource = "WHO 2021 24h Guideline: 40 µg/m³"
        ),
        MonitoringEntity(
            parameter = "NO2",
            value = 28.5,
            unit = "µg/m³",
            location = "Station 1 - Urban Center",
            standardLimit = 25.0,
            standardSource = "WHO 2021 24h Guideline: 25 µg/m³"
        ),
        MonitoringEntity(
            parameter = "BOD5",
            value = 16.5,
            unit = "mg/L",
            location = "Effluent Discharge Outfall #4",
            standardLimit = 30.0,
            standardSource = "EPA Secondary Treatment Limit: 30 mg/L"
        ),
        MonitoringEntity(
            parameter = "COD",
            value = 74.0,
            unit = "mg/L",
            location = "Effluent Discharge Outfall #4",
            standardLimit = 125.0,
            standardSource = "EU Urban Wastewater Limit: 125 mg/L"
        ),
        MonitoringEntity(
            parameter = "pH",
            value = 7.3,
            unit = "pH units",
            location = "River Monitoring Point Upstream",
            standardLimit = 8.5,
            standardSource = "EPA Aquatic Life Standard: 6.5 - 8.5"
        )
    )
}
