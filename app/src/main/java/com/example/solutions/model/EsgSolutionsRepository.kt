package com.example.solutions.model

object EsgSolutionsRepository {

    val domains: List<EsgDomainItem> by lazy {
        listOf(
            createEsgDomain(),
            createCsrDomain(),
            createEiaEcDomain(),
            createGhgCarbonDomain(),
            createEnvAssessmentSolutionsDomain(),
            createSustainabilityFinanceDomain(),
            createSafetySupervisorDomain(),
            createImpactAnalyserDomain(),
            createLabSkillsDomain(),
            createAiMlEnvironmentalDomain(),
            createClimateGlobalWarmingDomain(),
            createPolicyLawEddDomain(),
            createWhoResearchDomain(),
            createUnResearchProjectsDomain(),
            createSolidWasteWorldDomain(),
            createHazardousWasteDomain(),
            createSolidWasteGlobalSocietyDomain(),
            createSustainabilityFormulasDomain(),
            createNumericalModellingDomain(),
            createUnderwaterNoiseDomain()
        )
    }

    private fun createEsgDomain() = EsgDomainItem(
        id = "esg_frameworks",
        title = "ESG (Environmental, Social, Governance)",
        shortName = "ESG Reporting",
        category = EsgDomainCategory.GOVERNANCE_AND_FINANCE,
        summary = "Corporate non-financial reporting matrix evaluating planetary stewardship, human capital welfare, and ethical board oversight.",
        requirement = "Mandatory CSRD (EU Corporate Sustainability Due Diligence), SEC Climate Disclosures (US), and BRSR Core (SEBI India).",
        need = "Investors managing over $35 Trillion demand verifiable carbon, water, diversity, and supply chain accountability.",
        purpose = "To quantify enterprise systemic risks and align capital allocation with Paris Agreement targets.",
        goal = "Achieve verifiable net-zero operations, ethical governance, and zero human rights violations throughout tier-1/tier-2 suppliers.",
        scope = "Operations, supply chain (Scope 3 upstream/downstream), employee safety, board diversity, executive pay alignment.",
        currentScenario = "Transitioning from voluntary frameworks (GRI, SASB) to legally binding assurance (CSRD, ISSB S1/S2 standards).",
        futureOutlook = "Double materiality will become mandatory globally by 2028 with automated ERP ESG data feeds.",
        necessity = "Without ESG governance, corporations face severe greenwashing penalties, divestment, and high capital costs.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Academic Foundation for ESG Analysts",
                keyConcepts = listOf("Double Materiality", "ISSB S1 & S2 Standards", "Scope 1/2/3 Boundaries", "EU Taxonomy"),
                actionItems = listOf("Study GRI Standards 2021", "Master GHG Protocol Corporate Standard", "Analyze an annual BRSR report"),
                recommendedReadings = "GRI Universal Standards 2021; IFRS S1 General Requirements for Sustainability Disclosure."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Corporate Implementation & Assurance",
                keyConcepts = listOf("Limited vs Reasonable Assurance", "CSRD ESRS Data Points", "SBTi Net-Zero Validation"),
                actionItems = listOf("Establish ESG data governance controls", "Conduct double materiality assessment with board", "Deploy ERP carbon tracking"),
                recommendedReadings = "EFRAG European Sustainability Reporting Standards (ESRS); Big 4 ESG Assurance Guides."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Empirical Frontiers in Corporate ESG",
                keyConcepts = listOf("ESG Rating Divergence", "Greenwashing Detection via NLP", "Cost of Capital Elasticity"),
                actionItems = listOf("Correlate Refinitiv/MSCI scores with financial default", "Train transformer models on 10-K climate risk sections"),
                recommendedReadings = "Berg, Koelbel & Rigobon (2022) 'Aggregate Confusion: The Divergence of ESG Ratings', Review of Finance."
            )
        ),
        articles = listOf(
            DomainArticle("Aggregate Confusion: Divergence of ESG Ratings", "Review of Finance", "2022", "Highlights 0.61 average correlation between major ESG rating agencies.", "https://academic.oup.com/rof"),
            DomainArticle("Corporate Sustainability Due Diligence Directive", "European Parliament", "2024", "Legally binding supply chain environmental due diligence in EU.", "https://eur-lex.europa.eu")
        ),
        jobs = listOf(
            DomainJob("ESG Reporting Specialist", "$75,000 - $110,000", "Corporate / Tech", "Lead annual GRI/BRSR disclosures, manage external assurance."),
            DomainJob("Senior ESG Assurance Auditor", "$95,000 - $145,000", "Big 4 Consulting", "Perform ISAE 3000 assurance on non-financial metrics."),
            DomainJob("Chief Sustainability Officer (CSO)", "$180,000 - $300,000+", "Executive Enterprise", "Steer corporate decarb transition and stakeholder trust.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S. / M.S. Environmental Management", "MBA in Sustainable Business", "Finance & Accounting"),
            essentialCertifications = listOf("GARP SCR (Sustainability and Climate Risk)", "FSA Credential (SASB/IFRS)", "CFA ESG Investing"),
            handsOnPortfolioProjects = listOf("Build an open-source GHG Scope 1-3 audit of a public company", "Publish a CSRD gap analysis report"),
            technicalInterviewFocus = "Questions on double materiality matrices, emissions factor selections, and Green Asset Ratios.",
            careerRoadmap = "ESG Analyst (0-2 yrs) -> Senior ESG Consultant (3-5 yrs) -> Sustainability Director (6-10 yrs)."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Workiva", "Enterprise ESG", "Cloud platform for SEC and CSRD compliant financial & ESG reporting.", "https://www.workiva.com"),
            ToolSoftwareInfo("Sphera (SoFi)", "LCA & ESG", "Enterprise environmental accounting and supply chain mapping software.", "https://sphera.com"),
            ToolSoftwareInfo("OneTrust ESG", "Governance", "Automates supplier due diligence and ESG disclosure frameworks.", "https://onetrust.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("IFRS Foundation", "ISSB Sustainability Standards", "https://www.ifrs.org/sustainability"),
            OfficialSiteLink("GRI", "Global Reporting Initiative Guidelines", "https://www.globalreporting.org")
        ),
        jobSearchQuery = "ESG Analyst Sustainability Reporting",
        askEdenPrompt = "What is the step-by-step methodology to conduct a double-materiality assessment under EU CSRD?",
        relatedCalculatorType = "Carbon GHG Scope 1/2/3"
    )

    private fun createCsrDomain() = EsgDomainItem(
        id = "csr_strategy",
        title = "CSR (Corporate Social Responsibility)",
        shortName = "Corporate CSR",
        category = EsgDomainCategory.GOVERNANCE_AND_FINANCE,
        summary = "Strategic business integration of social, humanitarian, and environmental welfare beyond legal compliance.",
        requirement = "Section 135 Indian Companies Act (mandatory 2% net profit CSR spend), UN Global Compact commitments.",
        need = "Mitigating community opposition, securing Social License to Operate (SLO), and building grassroots resilience.",
        purpose = "To reinvest industrial wealth into community healthcare, watershed restoration, sanitation, and green education.",
        goal = "Measurable socio-economic development, watershed replenishment, zero displacement without rehabilitation.",
        scope = "Local communities within 50km radius of plants, marginalized populations, women empowerment, biodiversity pockets.",
        currentScenario = "Shift from philanthropic donations to structured impact measurement using SROI (Social Return on Investment).",
        futureOutlook = "Mandatory alignment of all CSR initiatives with the UN Sustainable Development Goals (SDGs) and biodiversity credits.",
        necessity = "Failure in CSR leads to social strikes, plant shutdowns, revoked environmental clearance, and brand erosion.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to CSR & Community Development",
                keyConcepts = listOf("Social License to Operate (SLO)", "SROI Methodology", "Participatory Rural Appraisal (PRA)"),
                actionItems = listOf("Study Section 135 Companies Act rules", "Volunteer with grassroots watershed projects"),
                recommendedReadings = "Blowfield & Murray: Corporate Responsibility; UN Global Compact 10 Principles."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "CSR Program Design & Statutory Auditing",
                keyConcepts = listOf("Third-Party Impact Assessment", "CSR Committee Charters", "Schedule VII Qualifying Heads"),
                actionItems = listOf("Implement geo-tagged CSR tracking", "Audit NGO implementation partners for FCRA/80G compliance"),
                recommendedReadings = "Guidelines on Corporate Social Responsibility and Sustainability for CPSEs."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Researching Long-Term Community Resilience",
                keyConcepts = listOf("Socio-Ecological Resilience", "Causal Impact Evaluation (Difference-in-Differences)"),
                actionItems = listOf("Quantify groundwater table elevation from CSR check-dam constructions"),
                recommendedReadings = "World Development Journal papers on Corporate Philanthropy and Rural Livelihoods."
            )
        ),
        articles = listOf(
            DomainArticle("Measuring the Social Return on Investment (SROI)", "Cabinet Office UK", "2020", "Framework for accounting for value across public and private investments.", "https://www.socialvalueuk.org")
        ),
        jobs = listOf(
            DomainJob("CSR Program Manager", "$65,000 - $95,000", "Corporate Foundations", "Design multi-million dollar community watershed and health interventions."),
            DomainJob("Social Impact Evaluator", "$70,000 - $105,000", "Development Consulting", "Perform quantitative SROI evaluations for industrial clients.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Masters in Social Work (MSW)", "Public Policy", "Development Studies"),
            essentialCertifications = listOf("Social Value Associate (Level 1/2)", "PMP / Prince2", "GRI Certified Professional"),
            handsOnPortfolioProjects = listOf("Design a baseline community need assessment report using participatory rural appraisal"),
            technicalInterviewFocus = "Conflict resolution with local villages, monitoring project KPIs, and budget governance under statutory law.",
            careerRoadmap = "Field Coordinator -> CSR Project Lead -> Head of Corporate Foundations."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("SmartSimple", "CSR Grants", "Automated tracking for corporate philanthropy and volunteer programs.", "https://www.smartsimple.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("UN Global Compact", "10 Principles for Sustainable Business", "https://unglobalcompact.org")
        ),
        jobSearchQuery = "Corporate Social Responsibility Manager",
        askEdenPrompt = "How do you calculate Social Return on Investment (SROI) for an environmental CSR initiative?"
    )

    private fun createEiaEcDomain() = EsgDomainItem(
        id = "eia_ec_clearance",
        title = "EIA (Environmental Impact Assessment) & EC (Environmental Clearance)",
        shortName = "EIA & EC Clearance",
        category = EsgDomainCategory.ASSESSMENT_AND_COMPLIANCE,
        summary = "Comprehensive statutory engineering assessment anticipating baseline environmental impacts and securing project clearance.",
        requirement = "USEPA NEPA (40 CFR 1500-1508), EU EIA Directive (2014/52/EU), MoEFCC EIA Notification 2006 (India).",
        need = "No mega project (power plants, highways, mines, chemical parks) can legally break ground without statutory Environmental Clearance.",
        purpose = "Identify, predict, and mitigate adverse environmental damages across air, water, soil, noise, and socio-economic systems.",
        goal = "Prevent unmitigated environmental destruction and enforce strict Environmental Management Plans (EMP) with post-EC compliance.",
        scope = "Pre-feasibility, Terms of Reference (ToR), baseline seasonal monitoring (3 seasons), public hearing, Expert Appraisal Committee (EAC) appraisal.",
        currentScenario = "Transitioning to digital clearance portals (PARIVESH, EPA NEPA e-Review) with GIS spatial overlay verification.",
        futureOutlook = "Mandatory biodiversity net-gain quantification (10% uplift) and life-cycle carbon budgeting in all EIA clearances.",
        necessity = "Operating without valid EC results in immediate project demolition orders, National Green Tribunal fines, and criminal prosecution.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Mastering the 4-Stage EIA Process",
                keyConcepts = listOf("Screening & Scoping", "Terms of Reference (ToR)", "Public Consultation", "Appraisal"),
                actionItems = listOf("Read an Executive Summary of an approved EIA report", "Learn how baseline air sampling stations are selected"),
                recommendedReadings = "Canter: Environmental Impact Assessment; UNEP EIA Training Resource Manual."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "NABET/QCI Accredited Functional Area Expert",
                keyConcepts = listOf("EIA Coordinator (EC) Requirements", "Functional Area Expert (FAE) - Air, Water, Ecology, Noise"),
                actionItems = listOf("Prepare legally defensible EMP budget lines", "Defend EIA baseline before regulatory Expert Appraisal Committee"),
                recommendedReadings = "NABET Accreditation Scheme for EIA Consultant Organizations (Version 3)."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Methodology Innovations in Cumulative Impact",
                keyConcepts = listOf("Strategic Environmental Assessment (SEA)", "Cumulative Impact Assessment (CIA)", "Spatial Habitat Fragmentation"),
                actionItems = listOf("Develop agent-based models for wildlife corridor disruption under highway expansion projects"),
                recommendedReadings = "Impact Assessment and Project Appraisal (Taylor & Francis Journal)."
            )
        ),
        articles = listOf(
            DomainArticle("Cumulative Environmental Impact Assessment: Principles and Guidelines", "IAIA", "2021", "State-of-the-art methodology for multi-project river basin impacts.", "https://www.iaia.org")
        ),
        jobs = listOf(
            DomainJob("EIA Project Coordinator", "$80,000 - $125,000", "Environmental Consulting", "Direct EIA drafting, stakeholder hearings, and regulatory clearance."),
            DomainJob("Ecology & Biodiversity FAE Specialist", "$70,000 - $105,000", "Ecological Surveys", "Conduct flora/fauna baseline surveys and design wildlife mitigation plans."),
            DomainJob("Environmental Compliance Officer", "$65,000 - $95,000", "Infrastructure / Mining", "Ensure 6-monthly EC condition compliance monitoring and reporting.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("M.Tech / M.S. Environmental Engineering", "M.Sc. Environmental Science", "Civil Engineering"),
            essentialCertifications = listOf("NABET/QCI Accredited EIA Coordinator / FAE", "Certified Environmental Professional (CEP)"),
            handsOnPortfolioProjects = listOf("Draft a complete sample EMP for a 100 MW solar park or cement grinding unit"),
            technicalInterviewFocus = "ToR compliance, dispersion modeling results validation, public hearing grievance management.",
            careerRoadmap = "Field Surveyor -> EIA Junior Consultant -> Accredited FAE -> Lead EIA Coordinator."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("QGIS / ArcGIS Pro", "Spatial GIS", "Mapping project buffer zones (10km study area), land use classification.", "https://qgis.org"),
            ToolSoftwareInfo("AERMOD View", "Air Dispersion", "Simulating ground level concentrations for stack emissions in EIA chapters.", "https://www.weblakes.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("US EPA NEPA", "National Environmental Policy Act Guidance", "https://www.epa.gov/nepa"),
            OfficialSiteLink("PARIVESH", "Single Window Environmental Clearance Portal", "https://parivesh.nic.in")
        ),
        jobSearchQuery = "Environmental Impact Assessment Specialist EIA Consultant",
        askEdenPrompt = "What are the mandatory stages to obtain Environmental Clearance for a Category A project?",
        relatedProcedureDomain = "AMBIENT"
    )

    private fun createGhgCarbonDomain() = EsgDomainItem(
        id = "ghg_carbon_market",
        title = "GHG Accounting & Carbon Markets",
        shortName = "Carbon Markets & GHG",
        category = EsgDomainCategory.CLIMATE_AND_CARBON,
        summary = "Rigorous quantification of greenhouse gas inventories and capitalization through compliance and voluntary carbon trading.",
        requirement = "GHG Protocol Corporate Standard, ISO 14064-1/2/3, Article 6 Paris Agreement, EU Emissions Trading System (ETS).",
        need = "Cap-and-trade regulations enforce financial penalties on emissions; companies monetize certified emissions reductions (CERs).",
        purpose = "To measure, report, and verify (MRV) Scope 1 direct, Scope 2 indirect, and Scope 3 value chain emissions.",
        goal = "Achieve verifiable absolute emissions reductions aligned with 1.5°C science-based targets (SBTi).",
        scope = "7 Kyoto greenhouse gases: CO₂, CH₄, N₂O, HFCs, PFCs, SF₆, NF₃ converted to CO₂e via GWP values.",
        currentScenario = "EU ETS carbon price fluctuating between €60-€90/tonne; Article 6.4 crediting mechanism finalizing UN registry.",
        futureOutlook = "Integration of satellite-based MRV with smart-contract carbon registries preventing double counting.",
        necessity = "Unaccounted carbon liabilities expose enterprises to EU CBAM (Carbon Border Adjustment Mechanism) tariffs.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to GHG Accounting",
                keyConcepts = listOf("Global Warming Potential (GWP AR5 vs AR6)", "Scope 1 vs 2 (Location vs Market)", "Emission Factors"),
                actionItems = listOf("Download DEFRA emission factor tables", "Compute personal vs corporate carbon balance sheets"),
                recommendedReadings = "The GHG Protocol: A Corporate Accounting and Reporting Standard (Revised Edition)."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Corporate Carbon Auditing & Article 6 Trading",
                keyConcepts = listOf("ISO 14064 Lead Verifier", "SBTi Corporate Net-Zero Standard", "Additionality & Permanence"),
                actionItems = listOf("Set up activity data collection pipelines for 15 Scope 3 categories", "Participate in EU ETS auctions"),
                recommendedReadings = "World Bank: State and Trends of Carbon Pricing 2024."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Advanced MRV and Carbon Credit Integrity",
                keyConcepts = listOf("Dynamic Baseline Crediting", "Leakage Modeling", "High-Resolution TROPOMI Satellite Inversion"),
                actionItems = listOf("Model methane plume flux using satellite hyperspectral imagery"),
                recommendedReadings = "Nature Climate Change: Systematic review of carbon offset overcrediting in forestry."
            )
        ),
        articles = listOf(
            DomainArticle("State and Trends of Carbon Pricing", "World Bank Group", "2024", "Comprehensive analysis of global compliance carbon taxes and ETS markets.", "https://openknowledge.worldbank.org"),
            DomainArticle("Pervasive Over-crediting in Voluntary Carbon Offsets", "Science", "2023", "Empirical scrutiny on REDD+ forest carbon project additionality.", "https://www.science.org")
        ),
        jobs = listOf(
            DomainJob("Carbon Accounting Lead", "$85,000 - $130,000", "Corporate Climate Advisory", "Build enterprise Scope 1-3 inventories, model decarbonization trajectories."),
            DomainJob("Carbon Originator & Credit Trader", "$100,000 - $175,000+", "Commodity Trading / Banks", "Source, evaluate, and trade Article 6 and voluntary carbon credits."),
            DomainJob("ISO 14064 Carbon Verifier", "$90,000 - $140,000", "Certification Bodies (DNV, TÜV)", "Execute formal assurance audits for corporate GHG statements.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Environmental Engineering", "Energy Economics", "Data Science & Mathematics"),
            essentialCertifications = listOf("ISO 14064-1/2/3 Lead Greenhouse Gas Verifier", "GHG Protocol Certificate"),
            handsOnPortfolioProjects = listOf("Construct a Python / SQL model calculating Scope 3 Category 1 purchased goods emissions"),
            technicalInterviewFocus = "Location-based vs Market-based Scope 2 accounting, GWP calculations, SBTi validation criteria.",
            careerRoadmap = "Carbon Analyst -> Senior Decarbonization Consultant -> Head of Carbon Strategy."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Plan A / Watershed", "Carbon Accounting SaaS", "Automated Scope 1-3 carbon intelligence platforms.", "https://watershed.com"),
            ToolSoftwareInfo("GHG Protocol Calculation Tools", "Official Templates", "Free sector-specific spreadsheets for industrial emissions.", "https://ghgprotocol.org")
        ),
        officialSites = listOf(
            OfficialSiteLink("GHG Protocol", "Official Greenhouse Gas Protocol Standards", "https://ghgprotocol.org"),
            OfficialSiteLink("UNFCCC Article 6", "United Nations Carbon Mechanism", "https://unfccc.int")
        ),
        jobSearchQuery = "Carbon Accounting GHG Scope 1 2 3 Carbon Markets",
        askEdenPrompt = "How do you calculate Scope 2 market-based vs location-based emissions with residual mix factors?",
        relatedCalculatorType = "Carbon GHG Scope 1/2/3"
    )

    private fun createEnvAssessmentSolutionsDomain() = EsgDomainItem(
        id = "env_assessment_solutions",
        title = "Environmental Assessment & Professional Solutions",
        shortName = "Environmental Solutions",
        category = EsgDomainCategory.ASSESSMENT_AND_COMPLIANCE,
        summary = "Multi-disciplinary engineering solutions for complex industrial pollution, remediation, and ecological restoration.",
        requirement = "Clean Air Act, Clean Water Act, Resource Conservation and Recovery Act (RCRA), ISO 14001 EMS.",
        need = "Industrial manufacturing and infrastructure generate multi-media pollution requiring engineered abatement systems.",
        purpose = "To deliver turnkey engineering solutions: wet scrubbers, Baghouses, ETP/STP zero liquid discharge, and bioremediation.",
        goal = "Achieve continuous statutory compliance, Zero Liquid Discharge (ZLD), and circular resource recovery.",
        scope = "Source apportionment, stack scrubbers, bio-reactors, membrane filtration (UF/RO), soil vapor extraction, containment.",
        currentScenario = "Shift toward advanced oxidation processes (AOP), electrochemical remediation, and smart sensor feedback loops.",
        futureOutlook = "Autonomous AI-controlled remediation plants dynamically optimizing chemical dosing and energy usage.",
        necessity = "Failure in pollution control equipment triggers immediate factory sealings, catastrophic environmental spills, and litigation.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Environmental Engineering Core Fundamentals",
                keyConcepts = listOf("Mass Balances & Stoichiometry", "Darcy's Law for Porous Media", "Activated Sludge Kinetics"),
                actionItems = listOf("Solve multi-stage scrubber absorption problems", "Design a simple aeration tank based on F/M ratio"),
                recommendedReadings = "Davis & Cornwell: Introduction to Environmental Engineering; Metcalf & Eddy Wastewater Engineering."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Turnkey Plant Engineering & Remediation",
                keyConcepts = listOf("Zero Liquid Discharge (ZLD) Systems", "Thermal Desorption Units", "Continuous Emission Monitoring (CEMS)"),
                actionItems = listOf("Commission an MBR-RO system for textile effluent", "Perform root-cause analysis for ESP tripping"),
                recommendedReadings = "EPA Air Pollution Control Technology Fact Sheets; WEF Manual of Practice."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Novel Nanomaterials & Bioremediation",
                keyConcepts = listOf("PFAS Degradation via Supercritical Water Oxidation", "Biochar Sorption Isotherms", "Electrokinetic Soil Flushing"),
                actionItems = listOf("Synthesize zero-valent iron nanoparticles for TCE dechlorination in groundwater"),
                recommendedReadings = "Environmental Science & Technology (ACS Journal); Water Research."
            )
        ),
        articles = listOf(
            DomainArticle("PFAS Remediation Technologies: A Comprehensive Review", "Journal of Hazardous Materials", "2023", "Detailed evaluation of destruction technologies including plasma and supercritical oxidation.", "https://www.sciencedirect.com")
        ),
        jobs = listOf(
            DomainJob("Senior Environmental Remediation Engineer", "$90,000 - $135,000", "Civil/Environmental Engineering", "Design and operate contaminated groundwater and soil remediation systems."),
            DomainJob("Wastewater Treatment Plant Designer", "$85,000 - $125,000", "CleanTech / Industrial ETP", "Design biological and membrane-based industrial wastewater systems."),
            DomainJob("Air Pollution Control Specialist", "$80,000 - $120,000", "Heavy Industry / EPC", "Design electrostatic precipitators, baghouses, and FGD units.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Chemical Engineering", "Environmental Engineering", "Civil Engineering"),
            essentialCertifications = listOf("Professional Engineer (PE) License", "Certified Hazardous Materials Manager (CHMM)"),
            handsOnPortfolioProjects = listOf("Complete P&ID and mass balance calculation for a 500 KLD industrial effluent treatment plant"),
            technicalInterviewFocus = "Hydraulic retention time calculations, filter media selection, hazardous waste characterization.",
            careerRoadmap = "Junior Process Engineer -> Remediation Lead -> Director of Environmental Solutions."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("BioWin", "Wastewater Simulation", "Biological wastewater treatment modeling and optimization software.", "https://envirosim.com"),
            ToolSoftwareInfo("EPANET", "Hydraulic Modeling", "Simulates hydraulic and water quality behavior in pressurized pipe networks.", "https://www.epa.gov/water-research/epanet")
        ),
        officialSites = listOf(
            OfficialSiteLink("US EPA Pollution Prevention", "P2 Resources and Guidance", "https://www.epa.gov/p2"),
            OfficialSiteLink("Water Environment Federation", "WEF Technical Resources", "https://www.wef.org")
        ),
        jobSearchQuery = "Environmental Engineer Remediation Wastewater Air Pollution Control",
        askEdenPrompt = "How do you size an Electrostatic Precipitator (ESP) using the Deutsch-Anderson equation?",
        relatedCalculatorType = "Air Stack Emission Rate"
    )

    private fun createSustainabilityFinanceDomain() = EsgDomainItem(
        id = "sustainability_finance",
        title = "Sustainability & Sustainable Finance",
        shortName = "Sustainable Finance",
        category = EsgDomainCategory.GOVERNANCE_AND_FINANCE,
        summary = "Mobilizing global capital markets toward sustainable activities via green bonds, climate risk pricing, and taxonomy alignment.",
        requirement = "EU Taxonomy Regulation (2020/852), SFDR (Articles 6, 8, 9 funds), TCFD (now IFRS), TNFD (nature-related financial risks).",
        need = "The global transition to net-zero requires an estimated $4.5 Trillion annual investment in clean infrastructure.",
        purpose = "To direct private investment away from carbon-intensive lock-in toward verified taxonomy-aligned sustainable economic activities.",
        goal = "Zero greenwashing in capital markets, transparent disclosure of physical and transition climate risks in debt and equity.",
        scope = "Green bonds, social bonds, sustainability-linked loans (SLL), carbon credits, ESG debt issuance, climate stress-testing.",
        currentScenario = "Over $1 Trillion annual sustainable debt issuance; rigorous scrutiny on sustainability-linked bond KPI targets.",
        futureOutlook = "Nature and biodiversity risks (TNFD) incorporated directly into corporate sovereign debt pricing and credit ratings.",
        necessity = "Without sustainable finance standards, stranded fossil assets could trigger systemic global financial contagion.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Pathway to Green Finance",
                keyConcepts = listOf("Green Bond Principles (ICMA)", "Physical vs Transition Climate Risk", "Discounted Cash Flows with Carbon Pricing"),
                actionItems = listOf("Read a Green Bond Framework from the World Bank", "Analyze SFDR Article 9 fund prospectuses"),
                recommendedReadings = "Schoenmaker & Schramade: Principles of Sustainable Finance; ICMA Green Bond Principles."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Structuring Green Debt & Portfolio Climate Alignment",
                keyConcepts = listOf("Second-Party Opinions (SPO)", "EU Green Bond Standard (EuGB)", "Do No Significant Harm (DNSH)"),
                actionItems = listOf("Structure a Sustainability-Linked Loan with carbon reduction ratchet", "Run TCFD climate scenario analysis"),
                recommendedReadings = "Network for Greening the Financial System (NGFS) Climate Scenarios for Central Banks."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Empirical Pricing of Climate Risk",
                keyConcepts = listOf("Greenium (Green Bond Premium)", "Climate VaR (Value at Risk)", "Stranded Asset Write-down Modeling"),
                actionItems = listOf("Econometric study measuring yield spreads between green and conventional corporate bonds"),
                recommendedReadings = "Journal of Financial Economics: Carbon Risk in Equity Markets."
            )
        ),
        articles = listOf(
            DomainArticle("Climate Risk and the Cost of Capital", "Bank for International Settlements (BIS)", "2023", "Empirical study on how physical climate vulnerability raises sovereign bond yields.", "https://www.bis.org")
        ),
        jobs = listOf(
            DomainJob("Sustainable Finance Analyst", "$85,000 - $130,000", "Investment Banking / Asset Management", "Evaluate green bond frameworks, conduct ESG due diligence on debt portfolios."),
            DomainJob("Climate Risk Modeler", "$100,000 - $160,000", "Central Banks / Reinsurance", "Develop climate stress-testing models under NGFS 2°C and 4°C scenarios."),
            DomainJob("Second Party Opinion (SPO) Assessor", "$80,000 - $125,000", "Rating Agencies (Moody's, S&P)", "Review green debt alignment with ICMA and Climate Bonds Standard.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Finance / Economics", "Quantitative Finance", "Environmental Economics"),
            essentialCertifications = listOf("CFA ESG Investing", "GARP SCR", "Climate Bonds Standard Verifier"),
            handsOnPortfolioProjects = listOf("Draft a complete Green Bond Framework with Use of Proceeds and KPI metrics for a renewable utility"),
            technicalInterviewFocus = "Discounted cash flow sensitivity to carbon taxes, SFDR regulatory definitions, TCFD scenario modeling.",
            careerRoadmap = "ESG Research Associate -> Green Structuring VP -> Head of Sustainable Banking."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Bloomberg ESG / Terminal", "Financial Intelligence", "Tracks green bond indices, corporate emissions data, and taxonomy alignment.", "https://www.bloomberg.com"),
            ToolSoftwareInfo("2DII PACTA", "Portfolio Alignment", "Open-source climate scenario analysis tool for equities and bonds.", "https://2degrees-investing.org")
        ),
        officialSites = listOf(
            OfficialSiteLink("ICMA", "International Capital Market Association Green Bond Principles", "https://www.icmagroup.org"),
            OfficialSiteLink("Climate Bonds Initiative", "Green Bond Standards & Certification", "https://www.climatebonds.net")
        ),
        jobSearchQuery = "Sustainable Finance Green Bonds Climate Risk Analyst",
        askEdenPrompt = "How do you calculate the 'Greenium' (green bond premium) and verify Use of Proceeds under ICMA?"
    )

    private fun createSafetySupervisorDomain() = EsgDomainItem(
        id = "safety_supervisor_hse",
        title = "Safety Supervisor & HSE Management",
        shortName = "HSE & Safety Supervisor",
        category = EsgDomainCategory.HEALTH_SAFETY_AND_LAB,
        summary = "Protecting worker life, health, and plant integrity through proactive hazard identification, OSHA compliance, and safety culture.",
        requirement = "OSHA 29 CFR 1910/1926, ISO 45001 Occupational Health & Safety Management, NEBOSH General Certificate.",
        need = "Over 2.9 million workers die globally each year from work-related accidents and diseases, costing $3 Trillion in economic losses.",
        purpose = "To eliminate catastrophic industrial accidents, toxic gas releases, explosions, confined space fatalities, and ergonomics injuries.",
        goal = "Achieve 'Vision Zero' incidents, 100% compliance with Lockout/Tagout (LOTO), and zero uncontrolled chemical spills.",
        scope = "Hazard Identification & Risk Assessment (HIRA), Job Safety Analysis (JSA), Permit to Work (PTW), fire safety, industrial hygiene.",
        currentScenario = "Integration of computer vision AI detecting PPE non-compliance and wearable biometric heat stress monitors.",
        futureOutlook = "Autonomous drone gas-leak inspections and digital twin safety simulations preventing high-hazard exposure.",
        necessity = "Safety failures result in loss of human life, massive OSHA willful violation fines, plant shutdowns, and criminal manslaughter charges.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Industrial Safety",
                keyConcepts = listOf("Hierarchy of Controls (Elimination to PPE)", "Heinrich's Triangle", "Bowtie Risk Analysis"),
                actionItems = listOf("Complete an OSHA 30-Hour General Industry course", "Study incident investigation reports from US Chemical Safety Board"),
                recommendedReadings = "Brauer: Safety and Health for Engineers; US Chemical Safety Board (CSB) Investigation Reports."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Plant HSE Leadership & Incident Prevention",
                keyConcepts = listOf("Process Safety Management (PSM 29 CFR 1910.119)", "HAZOP (Hazard and Operability Study)", "Emergency Response Plans"),
                actionItems = listOf("Conduct a quarterly plant-wide confined space audit", "Review contractor Permit-to-Work compliance records"),
                recommendedReadings = "Center for Chemical Process Safety (CCPS): Guidelines for Risk Based Process Safety."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Human Factors & Resilience Engineering",
                keyConcepts = listOf("Safety-II & Resilience Engineering", "Lagging vs Leading Indicator Predictive Models"),
                actionItems = listOf("Train machine learning algorithms on near-miss incident logs to predict severe events"),
                recommendedReadings = "Safety Science (Elsevier Journal); Accident Analysis & Prevention."
            )
        ),
        articles = listOf(
            DomainArticle("Safety-I and Safety-II: The Past and Future of Safety Management", "Ashgate", "2014", "Pioneering shift from error prevention to proactive capacity creation.", "https://www.erikhollnagel.com")
        ),
        jobs = listOf(
            DomainJob("EHS Specialist / Safety Supervisor", "$65,000 - $95,000", "Manufacturing / Oil & Gas", "Enforce plant safety protocols, lead incident investigations, conduct JSA."),
            DomainJob("Process Safety Manager (PSM)", "$105,000 - $160,000", "Chemical / Refinery", "Manage high-hazard chemical safety, lead HAZOP studies, assure mechanical integrity."),
            DomainJob("Corporate HSE Director", "$140,000 - $220,000", "Global Infrastructure", "Oversee health, safety, and environmental compliance across international sites.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S. Occupational Health and Safety", "Chemical Engineering", "Industrial Hygiene"),
            essentialCertifications = listOf("CSP (Certified Safety Professional)", "NEBOSH International Diploma", "CIH (Certified Industrial Hygienist)"),
            handsOnPortfolioProjects = listOf("Perform and document a comprehensive Job Safety Analysis (JSA) for hazardous tank cleaning"),
            technicalInterviewFocus = "LOTO procedures, root cause analysis methodologies (5 Whys, Fishbone), PSM 14 elements.",
            careerRoadmap = "Safety Officer -> EHS Manager -> Regional HSE Director."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Cority / Enablon", "EHS Software", "Enterprise platform for incident management, audits, and regulatory tracking.", "https://www.enablon.com"),
            ToolSoftwareInfo("ALOHA (CAMEO)", "Chemical Dispersion", "US EPA modeling program for chemical plume dispersion and explosive vapor clouds.", "https://www.epa.gov/cameo/aloha-software")
        ),
        officialSites = listOf(
            OfficialSiteLink("US OSHA", "Occupational Safety and Health Administration", "https://www.osha.gov"),
            OfficialSiteLink("US CSB", "Chemical Safety and Hazard Investigation Board", "https://www.csb.gov")
        ),
        jobSearchQuery = "HSE Officer Safety Supervisor EHS Specialist OSHA",
        askEdenPrompt = "How do you conduct a HAZOP (Hazard and Operability Study) using guide words (MORE, LESS, NO)?"
    )

    private fun createImpactAnalyserDomain() = EsgDomainItem(
        id = "impact_analyser_methodology",
        title = "Environmental Impact Analyser & Matrix Methods",
        shortName = "Impact Analyser",
        category = EsgDomainCategory.ASSESSMENT_AND_COMPLIANCE,
        summary = "Advanced mathematical, spatial, and matrix-based methodologies to identify, quantify, and weigh project environmental consequences.",
        requirement = "ISO 14040/44 (Life Cycle Assessment), Leopold Matrix, Battelle Environmental Evaluation System, Rapid Impact Assessment Matrix (RIAM).",
        need = "Qualitative opinions are legally indefensible; projects require objective, repeatable numerical impact matrices.",
        purpose = "To correlate project construction/operational activities with environmental parameters using quantifiable impact scores.",
        goal = "Objective ranking of alternative project designs to select options with minimal cumulative environmental damage.",
        scope = "Magnitude, importance, duration, reversibility, spatial extent, and probability across air, ecology, water, and human receptors.",
        currentScenario = "Integration of spatial GIS multi-criteria decision analysis (AHP - Analytic Hierarchy Process) with environmental impact matrices.",
        futureOutlook = "Automated digital twin impact assessment simulating 3D ecosystem changes before a single shovel strikes the ground.",
        necessity = "Subjective assessments are routinely overturned by environmental courts, stopping multi-billion dollar developments.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Matrix Methods",
                keyConcepts = listOf("Leopold Matrix (Magnitude vs Importance)", "Overlays & McHarg Sieve Method", "Battelle Evaluation System"),
                actionItems = listOf("Construct a 10x10 Leopold matrix for a proposed bridge construction", "Calculate RIAM environmental scores"),
                recommendedReadings = "Leopold et al. (1971) 'A Procedure for Evaluating Environmental Impact', USGS Circular 645."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Defensible Impact Quantification in EIA",
                keyConcepts = listOf("Analytic Hierarchy Process (AHP)", "Multi-Criteria Decision Analysis (MCDA)", "Significance Criteria"),
                actionItems = listOf("Weight environmental parameters using pairwise comparison matrices", "Defend impact scores in stakeholder litigation"),
                recommendedReadings = "Sadler: International Study of the Effectiveness of Environmental Assessment."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Mathematical Rigor in Impact Forecasting",
                keyConcepts = listOf("Fuzzy Multi-Criteria Assessment", "Probabilistic Monte Carlo Impact Propagation"),
                actionItems = listOf("Develop Bayesian belief networks for ecological trophic cascade impacts under dam building"),
                recommendedReadings = "Environmental Impact Assessment Review (Elsevier Journal)."
            )
        ),
        articles = listOf(
            DomainArticle("A Procedure for Evaluating Environmental Impact", "US Geological Survey", "1971", "The foundational Leopold Matrix methodology cited across global EIA statutes.", "https://pubs.usgs.gov")
        ),
        jobs = listOf(
            DomainJob("Senior Environmental Impact Analyser", "$80,000 - $120,000", "Infrastructure Consulting", "Lead quantitative impact modeling, develop weighted significance matrices."),
            DomainJob("GIS Multi-Criteria Decision Specialist", "$75,000 - $110,000", "Spatial Environmental Planning", "Execute spatial overlay analysis and site-selection optimization.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Environmental Planning", "Civil & Environmental Engineering", "Applied Statistics"),
            essentialCertifications = listOf("Certified Environmental Planner (AICP CEP)", "GISP (GIS Professional)"),
            handsOnPortfolioProjects = listOf("Build an interactive Excel/Python model implementing the Rapid Impact Assessment Matrix (RIAM)"),
            technicalInterviewFocus = "Leopold matrix score interpretation, handling negative vs positive impacts, mitigation weighting.",
            careerRoadmap = "Impact Analyst -> Lead EIA Specialist -> Environmental Planning Director."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("SimaPro / openLCA", "LCA Modeling", "Global standard tools for life cycle impact assessment (LCIA).", "https://openlca.org")
        ),
        officialSites = listOf(
            OfficialSiteLink("IAIA", "International Association for Impact Assessment", "https://www.iaia.org")
        ),
        jobSearchQuery = "Environmental Impact Analyst LCA Specialist Leopold Matrix",
        askEdenPrompt = "How do you construct and score a Leopold Matrix for an infrastructure expansion project?"
    )

    private fun createLabSkillsDomain() = EsgDomainItem(
        id = "lab_skills_testing",
        title = "Laboratory Skills for Environmental Testing",
        shortName = "Environmental Lab & QA/QC",
        category = EsgDomainCategory.HEALTH_SAFETY_AND_LAB,
        summary = "Precision analytical laboratory techniques for testing trace pollutants in air, water, wastewater, soil, and hazardous waste.",
        requirement = "ISO/IEC 17025 Laboratory Competence, Standard Methods for Examination of Water and Wastewater (APHA), US EPA Methods.",
        need = "Regulatory compliance decisions and toxic liability rely 100% on defensible, reproducible, trace-level chemical testing.",
        purpose = "To measure trace heavy metals (ppb/ppt), volatile organics, nutrients, microbial pathogens, and physical parameters.",
        goal = "Zero contamination in lab blanks, precision within ±5% relative standard deviation, and complete chain-of-custody integrity.",
        scope = "Spectrophotometry (UV-Vis), Gas Chromatography (GC-MS), ICP-MS, HPLC, BOD/COD titration, gravimetric PM2.5/10 filter analysis.",
        currentScenario = "Automated high-throughput screening for emerging contaminants like microplastics and PFAS (EPA Method 1633).",
        futureOutlook = "Field-deployable microfluidic 'Lab-on-a-Chip' sensors providing real-time accredited laboratory grade telemetry.",
        necessity = "Faulty lab analyses lead to contaminated drinking water scandals, missed industrial toxic discharges, and legal nullification.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Core Environmental Wet Chemistry & Instrumental Analysis",
                keyConcepts = listOf("Beer-Lambert Law", "Titrimetric Alkalinity & Hardness", "BOD 5-Day Incubation Kinetics", "Calibration Curves"),
                actionItems = listOf("Prepare standard stock solutions from analytical grade salts", "Perform Winkler titration for Dissolved Oxygen"),
                recommendedReadings = "APHA Standard Methods for the Examination of Water and Wastewater; Harris: Quantitative Chemical Analysis."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "ISO 17025 Compliance & Instrumental Mastery",
                keyConcepts = listOf("Method Detection Limit (MDL) vs LOQ", "Spike Recovery & Matrix Interference", "ICP-MS Plasma Ionization"),
                actionItems = listOf("Calibrate GC-MS with internal deuterated standards", "Draft an ISO 17025 compliant SOP for heavy metals digestion"),
                recommendedReadings = "ISO/IEC 17025:2017 General requirements for the competence of testing and calibration laboratories."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Ultra-Trace Analysis of Emerging Pollutants",
                keyConcepts = listOf("Non-Target High Resolution Mass Spec (HRMS)", "PFAS Precursor Oxidation (TOP Assay)"),
                actionItems = listOf("Develop LC-MS/MS methods for 40+ PFAS congeners at parts-per-quadrillion detection limits"),
                recommendedReadings = "Analytical Chemistry (ACS); Environmental Toxicology and Chemistry."
            )
        ),
        articles = listOf(
            DomainArticle("US EPA Method 1633: Analysis of PFAS in Aqueous, Solid, Biosolids, and Tissue Samples", "US EPA", "2024", "Standardized LC-MS/MS protocol for 40 target PFAS compounds.", "https://www.epa.gov")
        ),
        jobs = listOf(
            DomainJob("Environmental Laboratory Chemist", "$60,000 - $85,000", "Accredited Testing Labs (Eurofins, SGS)", "Run daily GC-MS, ICP-OES, and wet chemistry testing on water/soil."),
            DomainJob("Lab QA/QC Technical Manager", "$85,000 - $125,000", "Analytical Facilities", "Maintain ISO 17025 accreditation, oversee proficiency testing and audit trails."),
            DomainJob("Instrumental Application Specialist", "$90,000 - $135,000", "Instrument Vendors (Agilent, Thermo Fisher)", "Train lab clients on ICP-MS and GC-MS environmental method development.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Analytical Chemistry", "Biochemistry", "Environmental Science"),
            essentialCertifications = listOf("ISO/IEC 17025 Internal Auditor", "Certified Environmental Analytical Chemist"),
            handsOnPortfolioProjects = listOf("Document a full validation dossier for trace metal analysis on water samples following EPA 200.8"),
            technicalInterviewFocus = "Calibration linearity (R² > 0.995), sample preservation techniques, matrix spike recovery math.",
            careerRoadmap = "Lab Analyst -> Senior Chemist -> Technical Laboratory Director."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("LabWare LIMS", "Lab Information System", "Enterprise sample tracking, chain-of-custody, and automated QA/QC flagging.", "https://www.labware.com"),
            ToolSoftwareInfo("Agilent MassHunter", "Instrument Software", "Chromatography and mass spectrometry acquisition and quantitative analysis.", "https://www.agilent.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("APHA Standard Methods", "Standard Methods for Water and Wastewater", "https://www.standardmethods.org"),
            OfficialSiteLink("US EPA Test Methods", "Index of Environmental Analytical Methods", "https://www.epa.gov/measurements-modeling")
        ),
        jobSearchQuery = "Environmental Chemist ICP-MS GC-MS Laboratory Analyst ISO 17025",
        askEdenPrompt = "How do you calculate Method Detection Limit (MDL) and percent recovery in an ISO 17025 environmental lab?",
        relatedProcedureDomain = "WATER"
    )

    private fun createAiMlEnvironmentalDomain() = EsgDomainItem(
        id = "ai_ml_environmental",
        title = "Machine Learning & AI in Environmental Science & Engineering",
        shortName = "AI & ML in Env Science",
        category = EsgDomainCategory.AI_AND_MODELLING,
        summary = "Transformative application of neural networks, satellite computer vision, and physics-informed ML to solve ecological crises.",
        requirement = "Explainable AI (XAI) in regulatory compliance, FAIR Data Principles, Python/PyTorch climate informatics toolchains.",
        need = "Planetary data from satellites, IoT sensor networks, and climate models exceeds human analytical capacity by orders of magnitude.",
        purpose = "To forecast air pollution plumes 72 hours ahead, detect illegal deforestation in real-time, and optimize energy/water grids.",
        goal = "Autonomous early warning networks for extreme weather events, toxic algal blooms, and industrial gas releases.",
        scope = "Computer vision for satellite imagery, LSTM/Transformer time-series forecasting, Physics-Informed Neural Networks (PINNs).",
        currentScenario = "Deep learning models (GraphCast, Pangu-Weather) outperforming traditional supercomputer numerical weather prediction.",
        futureOutlook = "Autonomous planetary digital twins continuously updating with real-time sensor ingestion and generative scenario policy testing.",
        necessity = "Without AI, early warnings fail during accelerating climate extremes, causing tens of thousands of avoidable casualties.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Climate Informatics & AI",
                keyConcepts = listOf("Convolutional Neural Networks for Remote Sensing", "LSTM/GRU for Streamflow", "Random Forests for Feature Importance"),
                actionItems = listOf("Train an air quality forecasting model in Google Colab", "Segment satellite deforestation using U-Net architecture"),
                recommendedReadings = "Rolnick et al. (2022) 'Tackling Climate Change with Machine Learning', ACM Computing Surveys."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Deploying Production Environmental AI",
                keyConcepts = listOf("MLOps for IoT Sensor Drift", "Physics-Informed Neural Networks (PINNs)", "Data Preprocessing for Irregular Time-Series"),
                actionItems = listOf("Deploy an automated anomaly detection pipeline on CEMS stack sensor feeds", "Build API endpoints for predictive AQI"),
                recommendedReadings = "Kashinath et al. (2021) 'Physics-informed machine learning: case studies for weather and climate'."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Frontiers in Foundation Climate Models",
                keyConcepts = listOf("Graph Neural Networks for Weather", "Self-Supervised Pretraining on Multi-Spectral Sentinel Data"),
                actionItems = listOf("Fine-tune a geospatial foundation model (e.g., NASA-IBM Prithvi) for flood inundation mapping"),
                recommendedReadings = "Nature: Learning skillful medium-range global weather forecasting (GraphCast)."
            )
        ),
        articles = listOf(
            DomainArticle("Tackling Climate Change with Machine Learning", "ACM Computing Surveys", "2022", "Seminal 100-page road map detailing high-impact AI climate interventions.", "https://dl.acm.org/doi/10.1145/3485128"),
            DomainArticle("Skilled Medium-Range Global Weather Forecasting with Graph Neural Networks", "Science", "2023", "DeepMind GraphCast achieves supercomputer accuracy in seconds.", "https://www.science.org")
        ),
        jobs = listOf(
            DomainJob("Environmental Data Scientist", "$95,000 - $145,000", "CleanTech / Climate Tech", "Develop predictive ML models for renewable generation and emissions forecasting."),
            DomainJob("Geospatial AI / Remote Sensing Engineer", "$100,000 - $155,000", "Satellite Analytics (Planet, Earth Engine)", "Build deep learning vision pipelines to classify land cover change and forest biomass."),
            DomainJob("Climate Informatics Research Scientist", "$120,000 - $185,000", "National Labs (NOAA, ECMWF)", "Advance neural weather simulators and hydrological deep learning.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Computer Science / Data Science", "Computational Environmental Science", "Applied Mathematics"),
            essentialCertifications = listOf("DeepLearning.AI TensorFlow / PyTorch Developer", "AWS Machine Learning Specialty"),
            handsOnPortfolioProjects = listOf("Build and deploy a GitHub repository predicting PM2.5 concentrations using Sentinel-5P satellite data and XGBoost"),
            technicalInterviewFocus = "Handling spatial autocorrelation, train/test leakage in temporal datasets, loss functions for extreme event prediction.",
            careerRoadmap = "Data Analyst -> Environmental ML Engineer -> Lead AI Climate Scientist."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Google Earth Engine", "Cloud Geospatial", "Planetary-scale geospatial analysis platform combining petabytes of satellite imagery with Python APIs.", "https://earthengine.google.com"),
            ToolSoftwareInfo("Climate Change AI", "Community Platform", "Tutorials, grants, and open datasets uniting AI practitioners and climate experts.", "https://www.climatechange.ai")
        ),
        officialSites = listOf(
            OfficialSiteLink("NASA EarthData", "Open Planetary Datasets", "https://www.earthdata.nasa.gov"),
            OfficialSiteLink("ECMWF Copernicus", "Atmosphere & Climate Data Store", "https://cds.climate.copernicus.eu")
        ),
        jobSearchQuery = "Environmental Machine Learning Climate Data Science Remote Sensing Python",
        askEdenPrompt = "How do Physics-Informed Neural Networks (PINNs) enforce conservation of mass in atmospheric pollutant dispersion?"
    )

    private fun createClimateGlobalWarmingDomain() = EsgDomainItem(
        id = "climate_global_warming_science",
        title = "Climate Science & Global Warming Science",
        shortName = "Climate Science",
        category = EsgDomainCategory.CLIMATE_AND_CARBON,
        summary = "Physical basis of Earth's atmospheric energy balance, greenhouse forcing, feedback loops, and IPCC climate trajectory modeling.",
        requirement = "United Nations Framework Convention on Climate Change (UNFCCC), IPCC Assessment Report 6 (AR6), Paris Agreement (Article 2).",
        need = "Human activities have warmed the planet by 1.2°C above pre-industrial levels, threatening critical tipping points.",
        purpose = "To understand radiative equilibrium, planetary carbon sinks (oceans/forests), cryosphere dynamics, and climate sensitivity.",
        goal = "Limiting global warming to well below 2.0°C and pursuing 1.5°C through steep emissions reductions to reach net-zero by 2050.",
        scope = "Atmospheric physics, Earth System Models (CMIP6), Shared Socioeconomic Pathways (SSPs), paleoclimatology, ocean circulation (AMOC).",
        currentScenario = "Record-breaking global ocean heat content, accelerated Arctic sea ice retreat, and heightened atmospheric greenhouse gas concentrations (>425 ppm CO₂).",
        futureOutlook = "Risk of triggering irreversible tipping elements (Greenland ice sheet collapse, Amazon dieback) unless peak emissions occur immediately.",
        necessity = "Unmitigated warming beyond 2°C causes catastrophic food system failure, multi-meter sea-level rise, and displacement of billions.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Core Physical Climatology",
                keyConcepts = listOf("Radiative Forcing (W/m²)", "Equilibrium Climate Sensitivity (ECS)", "Albedo Feedback", "Carbon Budget"),
                actionItems = listOf("Calculate simple 1D zero-dimensional planetary energy balance", "Study the IPCC AR6 Summary for Policymakers"),
                recommendedReadings = "IPCC AR6 Working Group I: The Physical Science Basis; Pierrehumbert: Principles of Planetary Climate."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Applying Climate Projections to Asset Resilience",
                keyConcepts = listOf("Downscaling CMIP6 Projections", "SSP1-2.6 vs SSP2-4.5 vs SSP5-8.5", "Extreme Value Statistics"),
                actionItems = listOf("Perform physical asset flood risk exposure under SSP3-7.0 2050 scenarios", "Incorporate heating degree days (HDD) into plant design"),
                recommendedReadings = "TCFD Technical Guidance on Climate-Related Scenario Analysis."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Frontiers in Climate Feedback Dynamics",
                keyConcepts = listOf("Atlantic Meridional Overturning Circulation (AMOC) Stability", "Permafrost Methane Thaw Kinetics"),
                actionItems = listOf("Model paleoclimate analogues (Pliocene thermal maximum) using high-resolution Earth System Models"),
                recommendedReadings = "Lenton et al. (2019) 'Climate tipping points — too risky to bet against', Nature."
            )
        ),
        articles = listOf(
            DomainArticle("IPCC Sixth Assessment Report: The Physical Science Basis", "Cambridge University Press", "2021", "The definitive worldwide consensus on anthropogenic climate drivers.", "https://www.ipcc.ch/report/ar6/wg1/"),
            DomainArticle("Exceeding 1.5°C Global Warming Could Trigger Multiple Climate Tipping Points", "Science", "2022", "Comprehensive assessment of critical Earth system thresholds.", "https://www.science.org")
        ),
        jobs = listOf(
            DomainJob("Climate Science Modeler", "$90,000 - $140,000", "Research Institutes / Reinsurance", "Run and downscale GCM projections, quantify regional temperature and precipitation shifts."),
            DomainJob("Climate Adaptation Specialist", "$80,000 - $125,000", "Municipalities / Engineering Firms", "Design sea-walls, urban heat-island mitigation, and resilient storm infrastructure."),
            DomainJob("Paleoclimatologist / Ice Core Researcher", "$75,000 - $115,000", "Academia / Polar Research", "Reconstruct historical atmospheric gas concentrations from Antarctic ice cores.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Ph.D. / M.S. Atmospheric Science", "Oceanography", "Physics / Applied Mathematics"),
            essentialCertifications = listOf("American Meteorological Society (AMS) Certified Consulting Meteorologist"),
            handsOnPortfolioProjects = listOf("Process NetCDF4 climate projection files using Python (xarray, cartopy) to map 2050 extreme heatwaves"),
            technicalInterviewFocus = "Radiative forcing calculations, understanding differences between GCMs, bias-correction methodologies.",
            careerRoadmap = "Postdoctoral Researcher -> Climate Scientist -> Lead IPCC Author / Climate Director."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("NCAR CESM", "Earth System Model", "Open-source global climate simulation code sponsored by NSF and NCAR.", "https://www.cesm.ucar.edu"),
            ToolSoftwareInfo("Python Xarray & CDO", "Climate Data Operators", "Standard toolkit for manipulating gridded climate files (NetCDF/GRIB).", "https://code.mpimet.mpg.de/projects/cdo")
        ),
        officialSites = listOf(
            OfficialSiteLink("IPCC", "Intergovernmental Panel on Climate Change", "https://www.ipcc.ch"),
            OfficialSiteLink("NOAA Climate.gov", "Global Climate Dashboard & Science Resources", "https://www.climate.gov")
        ),
        jobSearchQuery = "Climate Scientist Climate Change Adaptation Modeler",
        askEdenPrompt = "What is the remaining global carbon budget for a 67% chance of limiting global warming to 1.5°C under IPCC AR6?"
    )

    private fun createPolicyLawEddDomain() = EsgDomainItem(
        id = "policy_law_edd",
        title = "Policy, International Environmental Law & Due Diligence (EDD)",
        shortName = "Environmental Law & EDD",
        category = EsgDomainCategory.GOVERNANCE_AND_FINANCE,
        summary = "International environmental treaties, legal liability frameworks, and Phase I/II Environmental Due Diligence in cross-border M&A.",
        requirement = "ASTM E1527-21 (Phase I ESA), Equator Principles IV, Basel/Stockholm/Minamata Conventions, Aarhus Convention.",
        need = "Acquiring industrial assets without rigorous due diligence can inherit tens of millions of dollars in toxic cleanup liabilities (CERCLA Superfund).",
        purpose = "To identify historical contamination liabilities, ensure regulatory compliance, and enforce international transboundary norms.",
        goal = "Shield investors from inherited environmental torts, enforce strict liability on polluters, and uphold human right to a clean environment.",
        scope = "Historical title searches, aerial photograph audits, recognized environmental conditions (RECs), soil vapor screening, compliance audits.",
        currentScenario = "ASTM E1527-21 officially requiring PFAS evaluation as an emerging recognized environmental hazard in Phase I assessments.",
        futureOutlook = "Mandatory criminal accountability for ecocide under international law and enforceable global supply-chain diligence.",
        necessity = "Failure in EDD leads to multi-million dollar Superfund cleanups, regulatory shutdowns, and litigation by affected indigenous communities.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Foundational Environmental Law & Jurisprudence",
                keyConcepts = listOf("Precautionary Principle", "Polluter Pays Principle", "Strict & Joint/Several Liability", "Public Trust Doctrine"),
                actionItems = listOf("Read a landmark environmental court judgment", "Analyze the components of an ASTM Phase I report"),
                recommendedReadings = "Hunter, Salzman & Zaelke: International Environmental Law and Policy; ASTM E1527-21 Standard Practice."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Executing Transactional Environmental Due Diligence",
                keyConcepts = listOf("Recognized Environmental Conditions (RECs)", "Historical Recognized (HRECs) vs Controlled (CRECs)", "Phase II Soil/Water Intrusion"),
                actionItems = listOf("Conduct a Phase I site walk for a chemical manufacturing plant", "Draft an Environmental Indemnity agreement"),
                recommendedReadings = "Equator Principles Association: The Equator Principles IV (2020)."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Comparative Transboundary Environmental Law",
                keyConcepts = listOf("Extraterritorial Jurisdiction in Climate Litigation", "Corporate Veil Piercing for Toxic Torts"),
                actionItems = listOf("Analyze enforcement mechanisms of the Basel Convention plastic waste amendments"),
                recommendedReadings = "Harvard Environmental Law Review; Journal of Environmental Law (Oxford)."
            )
        ),
        articles = listOf(
            DomainArticle("ASTM E1527-21 Standard Practice for Environmental Site Assessments", "ASTM International", "2021", "Standard for satisfying All Appropriate Inquiries (AAI) under CERCLA.", "https://www.astm.org")
        ),
        jobs = listOf(
            DomainJob("Environmental Due Diligence (EDD) Consultant", "$85,000 - $130,000", "M&A Environmental Advisory", "Conduct Phase I/II ESAs for private equity, detect legacy toxic soil liabilities."),
            DomainJob("Environmental Regulatory Attorney", "$130,000 - $220,000+", "Law Firms / Corporations", "Defend clients in statutory enforcement, draft environmental covenants and indemnity clauses."),
            DomainJob("Equator Principles Compliance Advisor", "$95,000 - $150,000", "Multilateral Banks (IFC, ADB)", "Review infrastructure loan covenants against IFC Performance Standards.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("J.D. Law with Environmental Specialization", "B.S. Geology / Environmental Engineering", "Public Policy"),
            essentialCertifications = listOf("Environmental Professional (EP) under ASTM E1527", "Certified Environmental Auditor (CEA)"),
            handsOnPortfolioProjects = listOf("Draft a mock ASTM E1527-21 Phase I Environmental Site Assessment report for an industrial brownfield property"),
            technicalInterviewFocus = "Identification of RECs, historical research sources (Sanborn fire insurance maps, city directories), Superfund liability defense.",
            careerRoadmap = "Due Diligence Associate -> Senior Environmental Consultant -> Partner / Legal Counsel."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("EDR Lightbox", "Historical Property Data", "Premier database for Sanborn maps, historical aerial photos, and environmental database searches.", "https://www.edrnet.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("Equator Principles", "Financial Industry Benchmark for Environmental Risk", "https://equator-principles.com"),
            OfficialSiteLink("US EPA Superfund (CERCLA)", "National Cleanup & Liability Information", "https://www.epa.gov/superfund")
        ),
        jobSearchQuery = "Environmental Due Diligence EDD Phase I ESA Environmental Lawyer",
        askEdenPrompt = "What is the legal difference between an REC, HREC, and CREC under ASTM E1527-21?"
    )

    private fun createWhoResearchDomain() = EsgDomainItem(
        id = "who_research_health",
        title = "WHO Research on Environment & Public Health",
        shortName = "WHO Environment & Health",
        category = EsgDomainCategory.HEALTH_SAFETY_AND_LAB,
        summary = "World Health Organization scientific research correlating environmental exposures (air, water, chemicals) with global disease burden.",
        requirement = "WHO Global Air Quality Guidelines 2021, WHO Guidelines for Drinking-water Quality, International Chemical Safety Cards (ICSC).",
        need = "Environmental risk factors account for 24% of all global deaths (13.7 million deaths per year), predominantly from air pollution.",
        purpose = "To translate environmental toxicological evidence into health-based exposure limits and global sanitation policies.",
        goal = "Prevent non-communicable diseases (stroke, lung cancer, asthma, cardiovascular failure) triggered by toxic environmental exposures.",
        scope = "Ambient air pollution (PM2.5, PM10, NO₂, O₃, SO₂, CO), Water, Sanitation and Hygiene (WASH), lead exposure, endocrine disruptors.",
        currentScenario = "The 2021 WHO Air Quality Guidelines radically slashed annual PM2.5 limits from 10 µg/m³ down to 5 µg/m³ based on new epidemiological evidence.",
        futureOutlook = "Quantifying neuro-degenerative disease risk from ultrafine nanoparticles and climate-driven vector-borne disease expansion.",
        necessity = "Ignoring WHO health thresholds causes preventable pediatric respiratory epidemics, shortened lifespans, and billions in healthcare expenses.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Environmental Epidemiology",
                keyConcepts = listOf("Dose-Response Relationships", "Disability-Adjusted Life Years (DALYs)", "Relative Risk (RR) for PM2.5"),
                actionItems = listOf("Examine WHO Global Health Observatory data", "Calculate attributable mortality using WHO AirQ+ software"),
                recommendedReadings = "WHO (2021) Global Air Quality Guidelines; Frumkin: Environmental Health: From Global to Local."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Translating Health Evidence into Industrial Safeguards",
                keyConcepts = listOf("Health Impact Assessment (HIA)", "Attributable Fraction of Disease", "Occupational Exposure vs Population Exposure"),
                actionItems = listOf("Integrate Health Impact Assessment into standard EIA documentation", "Benchmark facility emissions against WHO guidelines"),
                recommendedReadings = "WHO: AirQ+ Software Tool for Health Risk Assessment of Air Pollution."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Cutting-Edge Environmental Toxicology",
                keyConcepts = listOf("Oxidative Stress Biomarkers", "Microplastic Tissue Translocation", "Low-Dose Non-Linear Toxic Effects"),
                actionItems = listOf("Correlate personal PM2.5 exposure with systemic inflammatory cytokines in clinical cohorts"),
                recommendedReadings = "The Lancet Planetary Health; Environmental Health Perspectives (NIEHS)."
            )
        ),
        articles = listOf(
            DomainArticle("WHO Global Air Quality Guidelines: Particulate Matter, Ozone, Nitrogen Dioxide, Sulfur Dioxide and Carbon Monoxide", "World Health Organization", "2021", "The gold-standard global threshold for atmospheric pollutants.", "https://www.who.int/publications/i/item/9789240034228")
        ),
        jobs = listOf(
            DomainJob("Environmental Epidemiologist", "$85,000 - $130,000", "Public Health Agencies / WHO", "Model population disease burden associated with industrial toxic emissions."),
            DomainJob("Health Impact Assessment (HIA) Specialist", "$75,000 - $115,000", "Consulting / Government", "Conduct HIAs for major transport and industrial infrastructure projects.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("Master of Public Health (MPH)", "Environmental Health Sciences", "Biostatistics"),
            essentialCertifications = listOf("Certified in Public Health (CPH)", "Diplomate of the American Board of Toxicology (DABT)"),
            handsOnPortfolioProjects = listOf("Run a complete health risk assessment using WHO AirQ+ modeling excess mortality for a major metropolitan city"),
            technicalInterviewFocus = "DALY calculations, odds ratios, confounder adjustment in epidemiological regression models.",
            careerRoadmap = "Health Data Analyst -> Senior Environmental Epidemiologist -> Public Health Program Director."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("WHO AirQ+", "Health Risk Software", "Calculates the magnitude of health effects associated with air pollution exposure.", "https://www.who.int/europe/tools-and-toolkits/airq-")
        ),
        officialSites = listOf(
            OfficialSiteLink("WHO Environment & Health", "Official World Health Organization Department", "https://www.who.int/health-topics/environmental-health"),
            OfficialSiteLink("WHO Air Quality", "WHO Global Air Quality Database", "https://www.who.int/data/gho/data/themes/air-pollution")
        ),
        jobSearchQuery = "Environmental Health Epidemiologist Public Health WHO",
        askEdenPrompt = "What is the epidemiological justification for WHO lowering the annual PM2.5 guideline to 5 µg/m³ in 2021?"
    )

    private fun createUnResearchProjectsDomain() = EsgDomainItem(
        id = "un_research_projects",
        title = "United Nations Research & Environmental Projects",
        shortName = "UN Environmental Programs",
        category = EsgDomainCategory.OCEAN_AND_ACOUSTICS,
        summary = "Global multilateral environmental initiatives, scientific treaties, and development programs led by UNEP, UNDP, GEF, and UNFCCC.",
        requirement = "UN 2030 Agenda (17 SDGs), Kunming-Montreal Global Biodiversity Framework (GBF), UN Convention to Combat Desertification (UNCCD).",
        need = "Planetary challenges like climate change, biodiversity collapse, and ocean plastic pollution require coordinated multilateral action.",
        purpose = "To deliver consensus scientific assessments (IPCC, IPBES, GEO-6), negotiate global treaties, and disburse multilateral climate finance.",
        goal = "Conserve 30% of Earth's land and oceans by 2030 ('30x30'), halt deforestation, and eliminate plastic pollution through binding treaties.",
        scope = "UNEP Global Environment Outlook, Global Environment Facility (GEF) project grants, Green Climate Fund (GCF) billion-dollar portfolios.",
        currentScenario = "Intergovernmental Negotiating Committee (INC) drafting a legally binding global treaty on plastic pollution.",
        futureOutlook = "Creation of sovereign biodiversity credits and international loss and damage fund disbursements to climate-vulnerable nations.",
        necessity = "Without multilateral UN cooperation, tragedy of the commons leads to complete collapse of global fisheries, atmosphere, and forests.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Pathway to UN Careers & Policy",
                keyConcepts = listOf("17 Sustainable Development Goals (SDGs)", "Multilateral Environmental Agreements (MEAs)", "UN Diplomatic Protocol"),
                actionItems = listOf("Read the UNEP Global Environment Outlook (GEO-6)", "Participate in Model UN Environment Assembly (UNEA)"),
                recommendedReadings = "UNEP: Global Environment Outlook (GEO-6); UN 2030 Agenda for Sustainable Development."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Managing UN & Multilateral Grant Projects",
                keyConcepts = listOf("GEF Project Cycle & Co-Financing", "GCF Accreditation", "Results-Based Management (RBM) Frameworks"),
                actionItems = listOf("Structure a project concept note (PIF) for Global Environment Facility funding", "Implement UN SDG indicators"),
                recommendedReadings = "GEF: Guidelines on the Project and Program Cycle Policy; UNDP Project Quality Standards."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Contributing to Global UN Scientific Assessments",
                keyConcepts = listOf("Intergovernmental Science-Policy Platforms (IPBES)", "Integrated Assessment Modeling (IAM)"),
                actionItems = listOf("Submit peer-reviewed publications for consideration in IPCC AR7 scoping and GEO-7 assessments"),
                recommendedReadings = "IPBES Global Assessment Report on Biodiversity and Ecosystem Services."
            )
        ),
        articles = listOf(
            DomainArticle("UNEP Global Environment Outlook (GEO-6): Healthy Planet, Healthy People", "Cambridge University Press", "2019", "Flagship UN assessment of the state of the global environment.", "https://www.unep.org/resources/global-environment-outlook-6")
        ),
        jobs = listOf(
            DomainJob("UN Environmental Programme Officer", "$75,000 - $120,000 (Tax-free P-Grade)", "United Nations (UNEP/UNDP)", "Manage international environmental cooperation projects and treaty negotiations."),
            DomainJob("Global Environment Facility (GEF) Project Coordinator", "$80,000 - $125,000", "International NGOs / UN Agencies", "Coordinate multi-country biodiversity and watershed restoration grants."),
            DomainJob("Climate Policy Advisor", "$85,000 - $135,000", "Multilateral Development Banks", "Advise developing country ministries on Nationally Determined Contributions (NDCs).")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("International Relations / Environmental Policy", "Development Economics", "Natural Resource Management"),
            essentialCertifications = listOf("UN Young Professionals Programme (YPP)", "Prince2 / PMP Project Management"),
            handsOnPortfolioProjects = listOf("Draft a complete Project Identification Form (PIF) aligning a community reforestation project with GEF-8 focal areas"),
            technicalInterviewFocus = "UN Core Competencies, working in multicultural teams, alignment with SDG indicators.",
            careerRoadmap = "UN Volunteer / Junior Professional Officer (JPO) -> P-3 Programme Officer -> P-5 Division Chief."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("UNEP World Environment Situation Room", "Data Portal", "Dynamic geospatial platform displaying real-time planetary health indicators.", "https://wesr.unep.org"),
            ToolSoftwareInfo("UN SDG Tracker", "Indicator Monitoring", "Global open database tracking progress across all 169 SDG targets.", "https://sdg-tracker.org")
        ),
        officialSites = listOf(
            OfficialSiteLink("UNEP", "United Nations Environment Programme", "https://www.unep.org"),
            OfficialSiteLink("UN Careers", "Official United Nations Recruitment Portal", "https://careers.un.org")
        ),
        jobSearchQuery = "United Nations UNEP UNDP Environmental Officer Project Manager",
        askEdenPrompt = "How do the Global Environment Facility (GEF) and Green Climate Fund (GCF) project approval cycles work?"
    )

    private fun createSolidWasteWorldDomain() = EsgDomainItem(
        id = "solid_waste_world",
        title = "Solid Waste Management Practices & Science Across the World",
        shortName = "Global Waste Science",
        category = EsgDomainCategory.WASTE_AND_CIRCULARITY,
        summary = "Comparative global engineering of municipal, industrial, and agricultural solid waste: collection, sorting, composting, and thermal conversion.",
        requirement = "EU Waste Framework Directive (2008/98/EC), US EPA 40 CFR Part 258 (Subtitle D Landfills), Extended Producer Responsibility (EPR).",
        need = "The world produces 2.24 billion tonnes of municipal solid waste annually; over 33% is mismanaged in open dumpsites.",
        purpose = "To convert discarded materials into high-grade secondary resources through material recovery, aerobic composting, and waste-to-energy.",
        goal = "Landfill diversion > 90%, universal door-to-door segregated collection, and elimination of open waste burning.",
        scope = "Material Recovery Facilities (MRF), automated optical sorting, anaerobic digestion, refuse-derived fuel (RDF), modern sanitary landfills.",
        currentScenario = "Widespread adoption of AI robotic sorters and legal mandates for Extended Producer Responsibility (EPR) for packaging.",
        futureOutlook = "Zero-waste circular industrial loops where waste streams serve as feedstocks for chemical recycling and green hydrogen.",
        necessity = "Uncollected solid waste clogs urban stormwater drains causing catastrophic flooding, disease vector proliferation, and marine debris.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Fundamentals of Solid Waste Engineering",
                keyConcepts = listOf("Waste Generation & Characterization", "Proximate & Ultimate Analysis", "Landfill Gas Generation Kinetics"),
                actionItems = listOf("Conduct a household waste audit categorizing organic, recyclable, and inert fractions", "Study modern landfill liner cross-sections"),
                recommendedReadings = "Tchobanoglous & Kreith: Handbook of Solid Waste Management; World Bank: What a Waste 2.0."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Municipal & Industrial Waste Operations",
                keyConcepts = listOf("Geomembrane Liner Quality Assurance", "Leachate Treatment via MBR/Nanofiltration", "Refuse-Derived Fuel (RDF) Standards"),
                actionItems = listOf("Design a route-optimization plan for municipal compactor trucks", "Commission a 200 TPD automated MRF plant"),
                recommendedReadings = "ISWA (International Solid Waste Association) Technical Guidelines on Landfill Operations."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Thermochemical & Biochemical Waste Upcycling",
                keyConcepts = listOf("Hydrothermal Carbonization (HTC)", "Supercritical CO₂ Plastic Depolymerization", "Pyrolysis Bio-Oil Quality"),
                actionItems = listOf("Optimize catalyst ratios for converting mixed polyolefin plastic waste into virgin naphtha equivalents"),
                recommendedReadings = "Waste Management (Elsevier Journal); Resources, Conservation and Recycling."
            )
        ),
        articles = listOf(
            DomainArticle("What a Waste 2.0: A Global Snapshot of Solid Waste Management to 2050", "World Bank Group", "2018", "The premier global empirical census of municipal solid waste volumes and compositions.", "https://openknowledge.worldbank.org")
        ),
        jobs = listOf(
            DomainJob("Solid Waste Management Specialist", "$70,000 - $105,000", "Municipalities / Veolia / Waste Management", "Plan and oversee collection networks, transfer stations, and composting facilities."),
            DomainJob("Landfill Gas (LFG) / Energy Engineer", "$85,000 - $125,000", "Clean Energy Developers", "Design gas capture wells, blowers, and flare/engine systems to generate power from landfill methane."),
            DomainJob("MRF Operations Manager", "$75,000 - $110,000", "Recycling Infrastructure", "Oversee optical sorting, trommels, and baling operations for commercial recyclables.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S. Civil / Environmental Engineering", "Urban Planning", "Mechanical Engineering"),
            essentialCertifications = listOf("SWANA (Solid Waste Association of North America) Certified Landfill Manager", "ISWA International Waste Manager"),
            handsOnPortfolioProjects = listOf("Calculate 30-year landfill capacity and leachate production using the EPA HELP model for a 50-hectare site"),
            technicalInterviewFocus = "Landfill leachate containment, ASTM test methods for geomembranes, calorific value calculations for RDF.",
            careerRoadmap = "Route Supervisor -> Plant Operations Engineer -> Director of Municipal Waste Management."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("EPA LandGEM", "Landfill Gas Emissions", "Automated model estimating emissions of methane and NMOCs from municipal solid waste landfills.", "https://www.epa.gov/catc/clean-air-technology-center-products#landgem")
        ),
        officialSites = listOf(
            OfficialSiteLink("ISWA", "International Solid Waste Association", "https://www.iswa.org"),
            OfficialSiteLink("US EPA Wastes", "Resource Conservation and Recovery Act Regulations", "https://www.epa.gov/waste")
        ),
        jobSearchQuery = "Solid Waste Engineer Recycling Operations Landfill Manager",
        askEdenPrompt = "How do you calculate methane generation rate using EPA LandGEM first-order decomposition kinetics?"
    )

    private fun createHazardousWasteDomain() = EsgDomainItem(
        id = "hazardous_waste_tech",
        title = "Hazardous Waste Management Science & Technology",
        shortName = "Hazardous Waste Tech",
        category = EsgDomainCategory.WASTE_AND_CIRCULARITY,
        summary = "Advanced chemical engineering and thermal containment technologies for toxic, flammable, reactive, and corrosive industrial wastes.",
        requirement = "RCRA Subtitle C (US), Basel Convention on Transboundary Movements, Stockholm Convention on POPs, Minamata Convention on Mercury.",
        need = "Millions of tonnes of heavy metal sludges, spent solvents, PCB oils, and e-waste threaten groundwater aquifers and human health.",
        purpose = "To render toxic compounds biologically inert through high-temperature incineration, vitrification, chemical stabilization, and secured landfills.",
        goal = "Zero environmental release of Persistent Organic Pollutants (POPs) and 100% cradle-to-grave tracking via digital manifests.",
        scope = "Toxicity Characteristic Leaching Procedure (TCLP), rotary kiln incineration (1200°C), stabilization/solidification (S/S), salt cavern disposal.",
        currentScenario = "Rapid expansion of plasma arc gasification and advanced hydrometallurgical recycling of EV lithium-ion batteries.",
        futureOutlook = "Closed-loop industrial ecology where hazardous byproducts are chemically reformed into synthetic fuels and critical minerals.",
        necessity = "Unregulated hazardous waste disposal causes catastrophic disasters (Love Canal, Bhopal) causing permanent groundwater poisoning.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Hazardous Waste Chemistry",
                keyConcepts = listOf("RCRA Characteristic Wastes (Ignitability, Corrosivity, Reactivity, Toxicity)", "TCLP Extraction Math", "Cradle-to-Grave Manifests"),
                actionItems = listOf("Study the 4 lists of hazardous wastes (F, K, P, U lists)", "Understand Destruction and Removal Efficiency (DRE) of 99.99%"),
                recommendedReadings = "LaGrega, Buckingham & Evans: Hazardous Waste Management; US EPA RCRA Orientation Manual."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "TSDF Operations & Hazardous Manifesting",
                keyConcepts = listOf("Treatment, Storage, and Disposal Facility (TSDF) Permitting", "Trial Burn Protocols", "Stabilization with Portland Cement/Pozzolan"),
                actionItems = listOf("Verify compatibility matrix before blending waste solvent drums", "Supervise a hazardous waste shipping manifest inspection"),
                recommendedReadings = "EPA Guidelines on Hazardous Waste Incineration and Land Disposal Restrictions (LDR)."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "High-Temperature Decomposition & Vitrification",
                keyConcepts = listOf("Dioxin & Furan De Novo Synthesis Prevention", "Plasma Arc Pyrolysis Kinetics", "Supercritical Water Oxidation (SCWO)"),
                actionItems = listOf("Model heavy metal leachability in silicate glass matrix under aggressive acidic leaching protocols"),
                recommendedReadings = "Journal of Hazardous Materials; Environmental Science & Technology."
            )
        ),
        articles = listOf(
            DomainArticle("Basel Convention Technical Guidelines on the Environmentally Sound Management of Hazardous Wastes", "UNEP Basel Convention", "2022", "Authoritative international standards for toxic waste treatment and disposal.", "https://www.basel.int")
        ),
        jobs = listOf(
            DomainJob("Hazardous Waste Treatment Manager", "$85,000 - $130,000", "Chemical / Environmental Services (Clean Harbors)", "Manage hazardous waste treatment units, secure landfill cells, and rotary kilns."),
            DomainJob("Industrial Waste Characterization Chemist", "$75,000 - $110,000", "TSDF Facilities / Lab Services", "Execute TCLP extraction and finger-print analysis of arriving waste drums."),
            DomainJob("EHS Chemical Compliance Specialist", "$80,000 - $120,000", "Semiconductor / Pharmaceutical", "Ensure site RCRA Part B compliance, cradle-to-grave manifests, and contingency plans.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Chemical Engineering", "Hazardous Materials Management", "Applied Chemistry"),
            essentialCertifications = listOf("CHMM (Certified Hazardous Materials Manager)", "HAZWOPER 40-Hour Certification"),
            handsOnPortfolioProjects = listOf("Design a complete TSDF waste acceptance protocol and chemical compatibility segregation matrix"),
            technicalInterviewFocus = "DRE calculations for incinerators, TCLP regulatory thresholds, emergency spill containment sizing.",
            careerRoadmap = "Hazardous Waste Chemist -> TSDF Facility Supervisor -> Director of Industrial Waste Compliance."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("EPA RCRAInfo & e-Manifest", "Federal Tracking", "Electronic tracking system for hazardous waste shipments across the United States.", "https://www.epa.gov/e-manifest"),
            ToolSoftwareInfo("CAMEO Chemical Reactivity", "Compatibility Tool", "Predicts potential hazards from mixing different chemicals and waste streams.", "https://www.epa.gov/cameo")
        ),
        officialSites = listOf(
            OfficialSiteLink("Basel Convention", "Secretariat of the Basel Convention", "https://www.basel.int"),
            OfficialSiteLink("US EPA RCRA", "Hazardous Waste Management Guidelines", "https://www.epa.gov/hw")
        ),
        jobSearchQuery = "Hazardous Waste Management CHMM RCRA TSDF Chemist",
        askEdenPrompt = "How do you calculate Destruction and Removal Efficiency (DRE) of 99.9999% for hazardous waste incineration?"
    )

    private fun createSolidWasteGlobalSocietyDomain() = EsgDomainItem(
        id = "solid_waste_global_society",
        title = "Solid Waste Management for Global Society & Circular Economy",
        shortName = "Global Society & Circularity",
        category = EsgDomainCategory.WASTE_AND_CIRCULARITY,
        summary = "Societal integration of zero-waste principles, informal waste picker integration, international plastics treaties, and circular design.",
        requirement = "UN Plastics Treaty (Global legally binding instrument), Ellen MacArthur Foundation Circular Economy Framework.",
        need = "Linear 'take-make-waste' consumption is depleting Earth's resources at 1.75x biocapacity, leaving oceans choked with microplastics.",
        purpose = "To redesign global material flows toward restorative and regenerative cycles that eliminate waste by architectural design.",
        goal = "100% recyclable or reusable packaging by 2030, formal inclusion and dignity for 20 million informal waste workers worldwide.",
        scope = "Design for Recyclability (DfR), deposit-return schemes (DRS), waste worker cooperatives, Extended Producer Responsibility (EPR).",
        currentScenario = "Global plastic treaty negotiations establishing binding caps on virgin polymer production and bans on single-use plastics.",
        futureOutlook = "Transition from volumetric waste collection taxes to digital product passports (DPP) tracking material composition throughout life.",
        necessity = "Without circularity, greenhouse gas emissions from plastics alone will consume over 15% of the global 1.5°C carbon budget.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Circular Society",
                keyConcepts = listOf("Circular Economy 9R Framework (Refuse to Recover)", "Material Circularity Indicator (MCI)", "Just Transition for Waste Pickers"),
                actionItems = listOf("Map circular resource loops for consumer electronics", "Study successful zero-waste cities (Kamikatsu, Japan)"),
                recommendedReadings = "Ellen MacArthur Foundation: Towards the Circular Economy; UNEP: Drowning in Plastics."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Implementing Corporate Packaging Circularity",
                keyConcepts = listOf("Extended Producer Responsibility (EPR) Portals", "Post-Consumer Recycled (PCR) Content Mandates", "Life Cycle Assessment (LCA)"),
                actionItems = listOf("Audit supply chain packaging for multi-layer unrecyclable films", "Establish direct contracts with waste picker recycling cooperatives"),
                recommendedReadings = "World Economic Forum: Breaking the Plastic Wave."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Socio-Technical Modeling of Circular Material Flows",
                keyConcepts = listOf("Dynamic Material Flow Analysis (MFA)", "Microplastic Marine Trophic Transfer Kinetics"),
                actionItems = listOf("Construct national material flow models tracing polyolefin plastics from import to environmental leakage"),
                recommendedReadings = "Nature Sustainability; Circular Economy and Sustainability (Springer)."
            )
        ),
        articles = listOf(
            DomainArticle("Breaking the Plastic Wave: A Comprehensive Assessment of Pathways Towards Stopping Ocean Plastic Pollution", "The Pew Charitable Trusts & SYSTEMIQ", "2020", "Proves that existing circular technologies can reduce ocean plastic by 80% by 2040.", "https://www.pewtrusts.org")
        ),
        jobs = listOf(
            DomainJob("Circular Economy Strategist", "$80,000 - $125,000", "Corporate Sustainability / FMCG", "Redesign product packaging, eliminate virgin plastic reliance, ensure EPR compliance."),
            DomainJob("EPR Compliance Manager", "$75,000 - $115,000", "Brand Owners / Producer Responsibility Orgs (PRO)", "Track national plastic collection certificates and manage recycler audit trails."),
            DomainJob("Urban Resource Recovery Planner", "$70,000 - $105,000", "Municipal Governance / C40 Cities", "Implement city-wide decentralized composting and cooperative waste picker integration.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Sustainable Design", "Environmental Economics", "Industrial Ecology"),
            essentialCertifications = listOf("Circular Economy Specialist Certification", "GRI / ISO 14001 Auditor"),
            handsOnPortfolioProjects = listOf("Draft a complete Material Flow Analysis (MFA) and Circularity Transition Plan for a consumer goods company"),
            technicalInterviewFocus = "EPR regulatory credit mechanisms, mechanical vs chemical recycling tradeoffs, PCR contamination limits.",
            careerRoadmap = "Circularity Analyst -> Senior Packaging Manager -> Global Head of Circular Economy."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("STAN (Substance Flow Analysis)", "MFA Software", "Freeware supporting material and substance flow analysis according to Austrian standard.", "https://www.stan2web.net"),
            ToolSoftwareInfo("Circulytics", "Circularity Measurement", "Comprehensive company-level circular economy measurement tool by Ellen MacArthur Foundation.", "https://www.ellenmacarthurfoundation.org/circulytics")
        ),
        officialSites = listOf(
            OfficialSiteLink("Ellen MacArthur Foundation", "Global Circular Economy Knowledge Hub", "https://www.ellenmacarthurfoundation.org"),
            OfficialSiteLink("UN INC Plastic Treaty", "Intergovernmental Negotiating Committee on Plastic Pollution", "https://www.unep.org/inc-plastic-pollution")
        ),
        jobSearchQuery = "Circular Economy Specialist Extended Producer Responsibility EPR Packaging",
        askEdenPrompt = "How do you calculate the Material Circularity Indicator (MCI) using the Ellen MacArthur Foundation formula?"
    )

    private fun createSustainabilityFormulasDomain() = EsgDomainItem(
        id = "sustainability_formulas_demand",
        title = "Sustainability Infographics, Formulas, Demand & Usage",
        shortName = "Formulas & Infographics",
        category = EsgDomainCategory.GOVERNANCE_AND_FINANCE,
        summary = "Interactive mathematical formulas, infographics, and quantitative indices used by sustainability auditors to measure eco-efficiency.",
        requirement = "ISO 14045 (Eco-efficiency assessment), ISO 14046 (Water footprint), GHG Protocol Scope 1-3 math, Circularity indicators.",
        need = "Sustainability claims without verifiable mathematical metrics are legally dismissed as deceptive greenwashing.",
        purpose = "To calculate exact quantitative indicators: Carbon Intensity, Material Circularity Index (MCI), Water Footprint, and Eco-Efficiency.",
        goal = "Equip students, professionals, and researchers with instant dynamic calculators and visual formula cards for executive presentations.",
        scope = "Corporate carbon intensity (kg CO₂e / \$ Revenue), Water footprint (m³ / ton), Eco-efficiency, Waste diversion percentage.",
        currentScenario = "Mandatory integration of audited quantitative performance metrics in annual 10-K and CSRD filings.",
        futureOutlook = "Automated real-time calculation of marginal carbon abatement costs (MACC) across corporate balance sheets.",
        necessity = "Inaccurate formula applications distort capital allocations, trigger regulatory fines, and misinform climate policy.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Quantitative Sustainability Metrics",
                keyConcepts = listOf("Carbon Intensity Formulas", "Water Footprint Assessment (Green, Blue, Grey Water)", "Eco-Efficiency Ratios"),
                actionItems = listOf("Solve sample problem sets calculating corporate carbon intensity across 3 consecutive fiscal years"),
                recommendedReadings = "Hoekstra: The Water Footprint Assessment Manual; ISO 14045:2020."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Audited Metrics in Corporate Disclosures",
                keyConcepts = listOf("Revenue-Normalized vs Output-Normalized Intensity", "Scope 2 Location vs Market Formulas", "SBTi Trajectory Math"),
                actionItems = listOf("Integrate live sustainability formulas into enterprise PowerBI and Tableau dashboards"),
                recommendedReadings = "CDP Technical Notes on Accounting of Scope 2 Emissions and Science-Based Targets."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Formulating Novel Multi-Dimensional Sustainability Indices",
                keyConcepts = listOf("Decoupling Elasticity Indices (Tapio Decoupling)", "Thermodynamic Exergy Loss as Sustainability Metric"),
                actionItems = listOf("Formulate composite sustainability indices with sensitivity weighting matrices"),
                recommendedReadings = "Ecological Indicators (Elsevier); Journal of Cleaner Production."
            )
        ),
        articles = listOf(
            DomainArticle("The Water Footprint Assessment Manual: Setting the Global Standard", "Water Footprint Network / Earthscan", "2011", "The fundamental mathematical methodology for Blue, Green, and Grey water footprints.", "https://waterfootprint.org")
        ),
        jobs = listOf(
            DomainJob("Sustainability Metrics & BI Analyst", "$75,000 - $115,000", "Corporate Analytics / ESG", "Maintain quantitative sustainability data cubes, compute carbon/water intensities for reporting."),
            DomainJob("LCA Quantitative Modeler", "$85,000 - $130,000", "CleanTech / Consulting", "Formulate mathematical product footprint models from bill-of-materials.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S. Quantitative Environmental Science", "Industrial Engineering", "Data Analytics"),
            essentialCertifications = listOf("Certified LCA Professional (LCACP)", "GRI Certified Sustainability Professional"),
            handsOnPortfolioProjects = listOf("Construct an automated interactive Excel dashboard calculating corporate Carbon, Water, and Waste diversion KPIs with scenario sliders"),
            technicalInterviewFocus = "Formulas for Scope 2 market-based emissions, Tapio decoupling elasticity math, normalizing metrics across inflation.",
            careerRoadmap = "ESG Data Analyst -> Senior Metrics Consultant -> Director of Sustainability Intelligence."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("Microsoft Sustainability Manager", "Cloud Analytics", "Automates calculation of Scope 1, 2, and 3 emissions and water/waste metrics.", "https://www.microsoft.com/sustainability"),
            ToolSoftwareInfo("Tableau / PowerBI", "Visual Analytics", "Standard visual dashboard tools for executive sustainability infographics.", "https://www.tableau.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("Water Footprint Network", "Global Water Footprint Standards & Tools", "https://waterfootprint.org"),
            OfficialSiteLink("World Business Council (WBCSD)", "Measuring Eco-Efficiency Guidelines", "https://www.wbcsd.org")
        ),
        jobSearchQuery = "Sustainability Data Analyst Carbon Intensity Water Footprint LCA",
        askEdenPrompt = "What is the formula and methodology to calculate Blue, Green, and Grey Water Footprint per ton of product?",
        relatedCalculatorType = "Carbon GHG Scope 1/2/3"
    )

    private fun createNumericalModellingDomain() = EsgDomainItem(
        id = "numerical_modelling_env",
        title = "Numerical Modelling in Environment (AERMOD, SWAT, MODFLOW, CFD)",
        shortName = "Numerical Modelling",
        category = EsgDomainCategory.AI_AND_MODELLING,
        summary = "Computational physics, fluid mechanics, and differential equation solvers simulating pollutant dispersion, hydrology, and groundwater.",
        requirement = "USEPA Appendix W Guideline on Air Quality Models, USGS Groundwater Modeling Guidelines, ISO Standards on Environmental Hydrodynamics.",
        need = "Physically measuring pollution everywhere is impossible; regulatory approval requires calibrated predictive numerical simulations.",
        purpose = "To solve Navier-Stokes, Gaussian plume, and Darcy groundwater equations to predict pollutant concentrations across complex terrain.",
        goal = "Achieve high statistical accuracy (Index of Agreement > 0.8, Fractional Bias < 0.2) between modeled plumes and field monitoring stations.",
        scope = "Atmospheric dispersion (AERMOD, CALPUFF), river basin hydrology (SWAT), groundwater flow and transport (MODFLOW), industrial CFD.",
        currentScenario = "Transitioning to cloud-parallel GPU computing allowing multi-million grid cell atmospheric and hydrological simulations.",
        futureOutlook = "Coupled atmospheric-hydrological digital twins providing real-time street-canyon and aquifer contaminant plume predictions.",
        necessity = "Faulty dispersion modeling results in unpredicted community toxic gas exposures, acid rain deposition, and regulatory penalties.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Guide to Numerical Environmental Simulations",
                keyConcepts = listOf("Gaussian Dispersion Equation", "Planetary Boundary Layer (PBL) Micrometeorology", "Finite Difference Grid Discretization"),
                actionItems = listOf("Run a simple AERMOD point-source tutorial with AERMET meteorological pre-processing", "Download and explore MODFLOW 6"),
                recommendedReadings = "Beychok: Fundamentals of Stack Gas Dispersion; Anderson, Woessner & Hunt: Applied Groundwater Modeling."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Regulatory Dispersion & Hydrology Defensibility",
                keyConcepts = listOf("Building Downwash (BPIP-PRIME)", "Terrain Processing via AERMAP (NED/SRTM)", "Model Calibration & Sensitivity Analysis"),
                actionItems = listOf("Set up receptor grids covering 10km radius around an industrial stack", "Calibrate a SWAT catchment model with river gauge data"),
                recommendedReadings = "US EPA: Guideline on Air Quality Models (40 CFR Part 51 Appendix W)."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Coupled Multi-Scale Environmental CFD",
                keyConcepts = listOf("Large Eddy Simulation (LES) in Urban Canyons", "Reactive Transport Modeling (PHT3D)", "Meshless SPH Fluid Solvers"),
                actionItems = listOf("Develop OpenFOAM simulations of aerosol droplet dispersion inside high-density urban transit nodes"),
                recommendedReadings = "Atmospheric Environment; Journal of Hydrology; Environmental Fluid Mechanics."
            )
        ),
        articles = listOf(
            DomainArticle("AERMOD: A Dispersion Model for Industrial Source Applications", "Journal of Applied Meteorology", "2005", "The landmark paper introducing the US EPA regulatory air dispersion model.", "https://journals.ametsoc.org"),
            DomainArticle("MODFLOW-6: The USGS Modular Hydrologic Model", "USGS Techniques and Methods", "2017", "The latest finite-difference modular framework for groundwater simulation.", "https://pubs.usgs.gov")
        ),
        jobs = listOf(
            DomainJob("Air Dispersion Modeler (AERMOD / CALPUFF)", "$80,000 - $125,000", "Environmental Consulting", "Simulate industrial stack emissions, verify ambient air quality standard compliance for EIA."),
            DomainJob("Groundwater Hydrogeologist / Modeler (MODFLOW)", "$85,000 - $130,000", "Water Resources / Mining", "Model aquifer drawdown, contaminant plume migration, and dewatering schemes."),
            DomainJob("Environmental CFD Specialist", "$95,000 - $145,000", "Engineering Simulation (Ansys/OpenFOAM)", "Simulate complex microclimate flows, thermal discharges, and indoor pathogen air dispersion.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Environmental Engineering", "Meteorology / Atmospheric Science", "Hydrology / Mechanical Engineering"),
            essentialCertifications = listOf("Professional Engineer (PE)", "Certified Hydrogeologist (CHG)"),
            handsOnPortfolioProjects = listOf("Construct a fully calibrated AERMOD air dispersion model for a 3-stack power facility including building downwash and terrain elevation"),
            technicalInterviewFocus = "Planetary boundary layer meteorology (Monin-Obukhov length, friction velocity), boundary conditions in MODFLOW, mesh convergence in CFD.",
            careerRoadmap = "Junior Modeler -> Senior Simulation Consultant -> Principal Environmental Modeler."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("AERMOD View", "Air Modeling", "Commercial graphical interface for the US EPA AERMOD air dispersion model.", "https://www.weblakes.com/products/aermod"),
            ToolSoftwareInfo("USGS MODFLOW 6", "Groundwater Flow", "Premier worldwide simulation system for groundwater flow and solute transport.", "https://www.usgs.gov/software/modflow-6-usgs-modular-hydrologic-model"),
            ToolSoftwareInfo("SWAT (Soil & Water Assessment Tool)", "Catchment Hydrology", "River basin scale model simulating water, sediment, and agricultural chemical yields.", "https://swat.tamu.edu")
        ),
        officialSites = listOf(
            OfficialSiteLink("US EPA SCRAM", "Support Center for Regulatory Atmospheric Modeling", "https://www.epa.gov/scram"),
            OfficialSiteLink("USGS Water", "US Geological Survey Groundwater Software", "https://www.usgs.gov/mission-areas/water-resources")
        ),
        jobSearchQuery = "AERMOD CALPUFF MODFLOW Air Dispersion Groundwater Modeler",
        askEdenPrompt = "How do you configure building downwash in AERMOD using the BPIP-PRIME algorithm?",
        relatedCalculatorType = "Air Stack Emission Rate"
    )

    private fun createUnderwaterNoiseDomain() = EsgDomainItem(
        id = "underwater_noise_survey",
        title = "Underwater Noise Survey Program & Technology",
        shortName = "Underwater Acoustics & PAM",
        category = EsgDomainCategory.OCEAN_AND_ACOUSTICS,
        summary = "Marine bio-acoustics, hydrophone telemetry, and Passive Acoustic Monitoring (PAM) to safeguard marine mammals from anthropogenic noise.",
        requirement = "NOAA Acoustic Guidance for Marine Mammals, EU Marine Strategy Framework Directive (MSFD Descriptor 11), IMO Guidelines on Vessel Quieting.",
        need = "Anthropogenic ocean noise from offshore wind pile-driving, seismic surveys, and commercial shipping causes marine mammal stranding and deafness.",
        purpose = "To measure underwater sound pressure levels (SPL) and sound exposure levels (SEL) to establish mandatory exclusion zones.",
        goal = "Prevent Permanent Threshold Shift (PTS) and Temporary Threshold Shift (TTS) in cetaceans and pinnipeds during offshore marine operations.",
        scope = "Calibrated hydrophones, autonomous ocean acoustic recorders (AMAR/SoundTrap), bubble curtains for pile driving, high-frequency echolocation click detection.",
        currentScenario = "Massive global deployment of Passive Acoustic Monitoring (PAM) across offshore wind farms in the North Sea and US Atlantic coast.",
        futureOutlook = "AI-enabled real-time hydrophone buoys using edge neural networks to detect right whales and automatically shut down pile-driving hammers.",
        necessity = "Unregulated underwater noise leads to mass whale beaching, auditory trauma in endangered marine life, and immediate stop-work orders.",
        audienceGuides = mapOf(
            AudienceType.STUDENTS to AudienceGuide(
                headline = "Student Introduction to Ocean Acoustics",
                keyConcepts = listOf("Decibels in Water vs Air (Ref: $1 \\mu Pa$)", "Sound Speed Profile (SOFAR Channel)", "Auditory Weighting Functions for Marine Mammals"),
                actionItems = listOf("Convert sound pressure level from air ($20 \\mu Pa$) to underwater reference ($1 \\mu Pa$)", "Analyze whale vocalizations using PAMGuard"),
                recommendedReadings = "Urick: Principles of Underwater Sound; Richardson et al.: Marine Mammals and Noise."
            ),
            AudienceType.PROFESSIONALS to AudienceGuide(
                headline = "Offshore Acoustic Survey Operations & Mitigation",
                keyConcepts = listOf("Cumulative Sound Exposure Level (SELcum)", "Big Bubble Curtain (BBC) Attenuation", "Exclusion Zone Visual/Acoustic Monitoring"),
                actionItems = listOf("Calibrate an array of digital hydrophones at 100m and 750m offsets", "Draft an Offshore Wind Acoustic Mitigation Plan"),
                recommendedReadings = "NOAA Technical Memorandum NMFS-OPR-59: 2018 Revisions to Technical Guidance for Assessing the Effects of Anthropogenic Sound on Marine Mammal Hearing."
            ),
            AudienceType.RESEARCHERS to AudienceGuide(
                headline = "Acoustic Ecology & Habitat Masking Dynamics",
                keyConcepts = listOf("Acoustic Communication Space Reduction", "Low-Frequency Ambient Noise Rise from Global Shipping"),
                actionItems = listOf("Model sound transmission loss in shallow coastal waters using parabolic equation (PE) acoustic models"),
                recommendedReadings = "Journal of the Acoustical Society of America (JASA); Marine Pollution Bulletin."
            )
        ),
        articles = listOf(
            DomainArticle("2018 Revisions to Technical Guidance for Assessing the Effects of Anthropogenic Sound on Marine Mammal Hearing", "NOAA Technical Memorandum", "2018", "The statutory benchmark for acoustic thresholds causing PTS and TTS in marine mammals.", "https://www.fisheries.noaa.gov"),
            DomainArticle("The Soundscape of the Anthropocene Ocean", "Science", "2021", "Comprehensive review of the global acoustic footprint on marine fauna.", "https://www.science.org")
        ),
        jobs = listOf(
            DomainJob("Passive Acoustic Monitoring (PAM) Operator", "$70,000 - $110,000", "Offshore Wind / Marine Survey", "Deploy hydrophones, monitor real-time acoustic spectrograms, enforce pile-driving shutdown protocols."),
            DomainJob("Marine Bio-Acoustician", "$80,000 - $125,000", "Marine Research / Environmental Consulting", "Process continuous terabyte acoustic recordings, classify vocalizing cetacean species."),
            DomainJob("Underwater Noise Mitigation Engineer", "$90,000 - $135,000", "Offshore Engineering / Acoustics", "Design and optimize subsea bubble curtains and resonator systems to dampen marine construction noise.")
        ),
        jobApproach = JobApproachGuide(
            recommendedDegrees = listOf("B.S./M.S. Marine Biology / Oceanography", "Acoustics / Electrical Engineering", "Physics"),
            essentialCertifications = listOf("JNCC / BOEM Certified PAM Operator", "BOSIET (Offshore Safety & Survival)"),
            handsOnPortfolioProjects = listOf("Process raw WAV hydrophone data using open-source PAMGuard software, identifying odontocete echolocation clicks and tonal whistles"),
            technicalInterviewFocus = "Understanding difference between peak SPL and cumulative SEL, transmission loss equations ($20\\log R$ vs $15\\log R$), marine mammal hearing groups.",
            careerRoadmap = "PAM Field Operator -> Senior Marine Acoustician -> Director of Ocean Sound Programs."
        ),
        toolsAndSoftware = listOf(
            ToolSoftwareInfo("PAMGuard", "Bioacoustic Software", "Premier open-source software for real-time passive acoustic monitoring and detection of marine mammals.", "https://www.pamguard.org"),
            ToolSoftwareInfo("Raven Pro (Cornell)", "Sound Analysis", "Interactive sound analysis software for bioacousticians and environmental ecologists.", "https://www.ravensoundsoftware.com")
        ),
        officialSites = listOf(
            OfficialSiteLink("NOAA Ocean Acoustics", "NOAA Fisheries Ocean Acoustics Program", "https://www.fisheries.noaa.gov/national/science-data/ocean-acoustics"),
            OfficialSiteLink("Discovery of Sound in the Sea (DOSITS)", "Science of Underwater Sound Education", "https://dosits.org")
        ),
        jobSearchQuery = "Passive Acoustic Monitoring PAM Hydrophone Marine Mammal Acoustics",
        askEdenPrompt = "How do you calculate underwater transmission loss and cumulative sound exposure level (SELcum) during offshore pile driving?"
    )

    val formulas: List<SustainabilityFormulaItem> by lazy {
        listOf(
            SustainabilityFormulaItem(
                id = "carbon_intensity",
                name = "Corporate Carbon Intensity (CI)",
                formulaSymbolic = "CI = Total Emissions (kg CO₂e) / Business Metric (Revenue $ or MWh)",
                description = "Standard metric normalizing corporate emissions against economic value generated or energy delivered.",
                demandAndUse = "Required by SEC, CSRD, and TCFD to compare company decarbonization decoupling independently of revenue growth.",
                unit = "kg CO₂e / $",
                param1Label = "Total Scope 1+2 Emissions (kg CO₂e)",
                param1Default = 450000.0,
                param2Label = "Net Revenue ($)",
                param2Default = 1200000.0,
                calculate = { p1, p2, _ -> if (p2 > 0) p1 / p2 else 0.0 },
                interpretation = { result ->
                    when {
                        result < 0.15 -> "Excellent eco-efficiency (Service/Tech leader, <0.15 kg/$)"
                        result < 0.50 -> "Moderate intensity (Light manufacturing/Retail benchmark)"
                        else -> "High carbon intensity (>0.50 kg/$). Heavy industry transition target."
                    }
                }
            ),
            SustainabilityFormulaItem(
                id = "material_circularity_index",
                name = "Recycled Content / Circularity Ratio (RCR)",
                formulaSymbolic = "RCR = (Mass Recycled Material / Total Product Mass) × 100%",
                description = "Measures the percentage of recycled or renewable feedstocks utilized in product manufacturing.",
                demandAndUse = "Mandated by EU Packaging Directives (PPWR) and corporate Zero-Waste-to-Landfill certification audits.",
                unit = "% Recycled",
                param1Label = "Mass of Recycled Input (kg)",
                param1Default = 650.0,
                param2Label = "Total Product Mass (kg)",
                param2Default = 1000.0,
                calculate = { p1, p2, _ -> if (p2 > 0) (p1 / p2) * 100.0 else 0.0 },
                interpretation = { result ->
                    when {
                        result >= 70.0 -> "High circularity level (Meets strict EU packaging and EPR incentives)"
                        result >= 30.0 -> "Moderate circular transition (Complies with baseline 2025 mandates)"
                        else -> "Linear dependency (<30%). High exposure to virgin plastic taxes."
                    }
                }
            ),
            SustainabilityFormulaItem(
                id = "water_footprint_index",
                name = "Water Footprint Intensity (WFI)",
                formulaSymbolic = "WFI = Consumptive Water Volume (m³) / Unit Production (Tonnes)",
                description = "Quantifies the total blue water consumed per tonne of industrial or agricultural commodity produced.",
                demandAndUse = "Used in CDP Water Security reporting and drought-stressed basin water risk evaluations.",
                unit = "m³ / Tonne",
                param1Label = "Total Freshwater Consumed (m³)",
                param1Default = 8500.0,
                param2Label = "Total Finished Output (Tonnes)",
                param2Default = 250.0,
                calculate = { p1, p2, _ -> if (p2 > 0) p1 / p2 else 0.0 },
                interpretation = { result ->
                    when {
                        result < 10.0 -> "Low water intensity (Efficient dry process)"
                        result < 45.0 -> "Standard manufacturing water footprint"
                        else -> "Water-intensive operation (>45 m³/t). Urgent water recycling/ZLD needed."
                    }
                }
            ),
            SustainabilityFormulaItem(
                id = "eco_efficiency_ratio",
                name = "Eco-Efficiency Index (EE)",
                formulaSymbolic = "EE = Economic Value Added ($) / Environmental Impact Units",
                description = "WBCSD standard ratio measuring how much economic value is created per unit of environmental burden incurred.",
                demandAndUse = "Benchmark used in sustainable capital allocation, corporate sustainability indices (DJSI), and board audits.",
                unit = "$ / Eco-Point",
                param1Label = "Gross Added Value ($)",
                param1Default = 500000.0,
                param2Label = "Environmental Burden (Eco-Points / Tonnes CO₂e)",
                param2Default = 1250.0,
                calculate = { p1, p2, _ -> if (p2 > 0) p1 / p2 else 0.0 },
                interpretation = { result ->
                    "Generated $%.1f of economic value per unit of environmental burden created.".format(result)
                }
            ),
            SustainabilityFormulaItem(
                id = "waste_diversion_rate",
                name = "Solid Waste Diversion Rate (WDR)",
                formulaSymbolic = "WDR = ((Recycled + Composted Mass) / Total Waste Generated) × 100%",
                description = "Calculates the proportion of municipal or industrial solid waste diverted away from landfill disposal.",
                demandAndUse = "Required for LEED Zero Waste building certification and municipal diversion mandates.",
                unit = "% Diverted",
                param1Label = "Diverted Mass - Recycled + Organic (kg)",
                param1Default = 8400.0,
                param2Label = "Total Waste Generated (kg)",
                param2Default = 10000.0,
                calculate = { p1, p2, _ -> if (p2 > 0) (p1 / p2) * 100.0 else 0.0 },
                interpretation = { result ->
                    when {
                        result >= 90.0 -> "TRUE Zero Waste to Landfill achieved (≥90% diversion standard)"
                        result >= 60.0 -> "Good diversion rate (Above OECD municipal average)"
                        else -> "High landfill reliance (<60%). Enhancements in source segregation needed."
                    }
                }
            )
        )
    }

    val downloadableResources: List<DownloadableResourceItem> by lazy {
        listOf(
            DownloadableResourceItem(
                id = "eia_statutory_dossier",
                title = "EIA & Environmental Clearance Master Dossier & Field Survey Kit",
                category = "Regulatory Compliance & EIA",
                fileExtension = "TXT",
                estimatedSize = "485 KB",
                description = "Complete 4-stage statutory Environmental Impact Assessment procedural handbook, Terms of Reference (ToR) checklists, and EAC presentation deck guidelines.",
                targetAudience = "EIA Coordinators, Functional Area Experts & Environmental Engineers",
                contentGenerator = { generateEiaDossierContent() }
            ),
            DownloadableResourceItem(
                id = "ghg_protocol_ledger",
                title = "GHG Protocol Scope 1, 2, 3 Corporate Accounting Ledger & Template",
                category = "Carbon Accounting & Markets",
                fileExtension = "CSV",
                estimatedSize = "320 KB",
                description = "ISO 14064 compliant corporate greenhouse gas inventory workbook with DEFRA/EPA emission factors for stationary combustion, grid electricity, and all 15 Scope 3 categories.",
                targetAudience = "Carbon Auditors, Sustainability Managers & ESG Analysts",
                contentGenerator = { generateGhgLedgerContent() }
            ),
            DownloadableResourceItem(
                id = "esg_csrd_framework",
                title = "ESG & CSR Double-Materiality Reporting Compendium (CSRD, BRSR, GRI)",
                category = "Corporate Governance & Finance",
                fileExtension = "TXT",
                estimatedSize = "410 KB",
                description = "Step-by-step matrix for conducting double materiality assessments under EU CSRD, gap analysis worksheets for BRSR Core, and SROI community impact calculation models.",
                targetAudience = "Corporate Sustainability Officers, Board Directors & Auditors",
                contentGenerator = { generateEsgCsrdContent() }
            ),
            DownloadableResourceItem(
                id = "lab_testing_sop",
                title = "Environmental Laboratory SOP Manual & ISO 17025 QA/QC Guidelines",
                category = "Laboratory Testing & Health",
                fileExtension = "TXT",
                estimatedSize = "390 KB",
                description = "Standard Operating Procedures for APHA/USEPA water, air, and soil testing: BOD5, COD, heavy metals by ICP-MS, calibration curve protocols, and chain-of-custody forms.",
                targetAudience = "Laboratory Chemists, QA/QC Managers & Environmental Technicians",
                contentGenerator = { generateLabSopContent() }
            ),
            DownloadableResourceItem(
                id = "waste_manifest_guide",
                title = "Solid & Hazardous Waste Management Manifest & Emergency Protocol",
                category = "Waste & Remediation",
                fileExtension = "TXT",
                estimatedSize = "360 KB",
                description = "Basel Convention and RCRA compliant cradle-to-grave hazardous waste manifest templates, chemical compatibility matrices, and TSDF operational audit checklists.",
                targetAudience = "HSE Officers, Hazardous Waste Managers & Municipal Planners",
                contentGenerator = { generateWasteGuideContent() }
            ),
            DownloadableResourceItem(
                id = "ml_env_python_pack",
                title = "AI & Machine Learning in Environmental Science: Python Jupyter Templates",
                category = "Data Science & Modelling",
                fileExtension = "PY",
                estimatedSize = "280 KB",
                description = "Production-ready Python code scripts using PyTorch and scikit-learn for PM2.5 air pollution forecasting, satellite multispectral classification, and water flow neural prediction.",
                targetAudience = "Environmental Data Scientists, AI Researchers & Modelers",
                contentGenerator = { generateMlPythonPackContent() }
            )
        )
    }

    private fun generateEiaDossierContent(): String = """
================================================================================
EDEN STATUTORY DOSSIER: ENVIRONMENTAL IMPACT ASSESSMENT (EIA) & CLEARANCE (EC)
COMPLIANCE STANDARD: ISO 14001:2015 • USEPA 40 CFR 1500 • MoEFCC EIA NOTIFICATION
================================================================================

1. EXECUTIVE SUMMARY & STATUTORY PREREQUISITES
No industrial, infrastructure, or mining project may commence civil construction
without prior Environmental Clearance (EC) granted by the designated regulatory
authority (State SEIAA or Central EAC).

2. THE 4-STAGE STATUTORY EIA PROCESS:
- Stage 1: SCREENING (Category A vs B1 vs B2 classification)
- Stage 2: SCOPING (Issuance of Project-Specific Terms of Reference [ToR])
- Stage 3: PUBLIC CONSULTATION & CITIZEN HEARINGS (Mandatory 30-day notice)
- Stage 4: APPRAISAL (Scrutiny by Expert Appraisal Committee [EAC])

3. ENVIRONMENTAL MANAGEMENT PLAN (EMP) STATUTORY BUDGETING:
A minimum of 1.5% to 2.5% of total capital expenditure (CAPEX) must be allocated
towards recurring and non-recurring EMP budget lines:
- Continuous Ambient Air Quality Monitoring Stations (CAAQMS)
- Zero Liquid Discharge (ZLD) Effluent Treatment System
- Green Belt Development (33% of total project plot area)
- Local Community Infrastructure & CSR Allocation

4. MANDATORY POST-EC STATUTORY MONITORING:
Every project proponent must submit 6-monthly compliance reports with certified
laboratory test data from an ISO 17025 / NABL accredited environmental laboratory.

(c) EDEN Environmental Intelligence Platform - Official Training Dossier
""".trimIndent()

    private fun generateGhgLedgerContent(): String = """
Category,Activity_Type,Quantity,Unit,Emission_Factor,EF_Units,Scope,Total_Emissions_tCO2e,Data_Source
Scope 1,Diesel Generator (Stationary),15000,Liters,2.68,kg CO2e/L,Scope 1,40.20,DEFRA 2024 Table 1.1
Scope 1,Natural Gas Heating,35000,m3,2.03,kg CO2e/m3,Scope 1,71.05,EPA GHG Factors Hub
Scope 1,Company Fleet Gasoline,8200,Liters,2.31,kg CO2e/L,Scope 1,18.94,DEFRA 2024 Table 1.2
Scope 2,Purchased Electricity (Grid),480000,kWh,0.42,kg CO2e/kWh,Scope 2 (Location),201.60,National Grid Factor
Scope 3,Cat 1: Purchased Goods & Services,1200000,USD Spend,0.22,kg CO2e/USD,Scope 3,264.00,EEIO Model v2.1
Scope 3,Cat 4: Upstream Freight Shipping,45000,Tonne-km,0.08,kg CO2e/t-km,Scope 3,3.60,GLEC Framework
Scope 3,Cat 6: Business Air Travel,120000,Passenger-km,0.15,kg CO2e/p-km,Scope 3,18.00,ICAO Carbon Calc
Scope 3,Cat 7: Employee Commuting,320000,km Travel,0.11,kg CO2e/km,Scope 3,35.20,Commuter Survey 2024
TOTAL INVENTORY SUMMARY,,,,,,,652.59,ISO 14064 Verified
""".trimIndent()

    private fun generateEsgCsrdContent(): String = """
================================================================================
CORPORATE ESG & CSR DOUBLE-MATERIALITY ASSESSMENT MASTER TEMPLATE
COMPLIANCE FRAMEWORK: EU CSRD (ESRS) • SEBI BRSR CORE • GRI UNIVERSAL 2021
================================================================================

1. DOUBLE-MATERIALITY CONCEPTUAL FOUNDATION:
Every material topic must be assessed on two independent axes:
Axis 1: IMPACT MATERIALITY (Inside-Out: Corporate impacts on people & planet)
Axis 2: FINANCIAL MATERIALITY (Outside-In: Environmental risks impacting enterprise cash flows)

2. CORE TOPIC SCORING PROTOCOL:
- Scale: 1 (Negligible) to 5 (Catastrophic / Transformative)
- Threshold: Any topic scoring >= 3.0 on either axis MUST be disclosed with audited KPIs.

3. SROI (SOCIAL RETURN ON INVESTMENT) FORMULA:
SROI = (Total Tangible & Intangible Community Financial Value Generated) / (Total CSR Expenditure ($))
A compliant CSR program should achieve an audited SROI ratio exceeding 2.5:1 over a 3-year baseline.

(c) EDEN Environmental Intelligence Platform - Official Training Dossier
""".trimIndent()

    private fun generateLabSopContent(): String = """
================================================================================
ENVIRONMENTAL TESTING LABORATORY STANDARD OPERATING PROCEDURE (SOP)
ACCREDITATION STANDARD: ISO/IEC 17025:2017 • APHA 23RD EDITION
================================================================================

PARAMETER: 5-Day Biochemical Oxygen Demand (BOD5) [APHA 5210 B]
1. PRINCIPLE:
The dissolved oxygen (DO) content of a diluted wastewater sample is determined
before and after incubation in the dark at 20°C ± 1°C for 5 days.

2. REAGENTS & APPARATUS:
- Standard 300 mL BOD glass incubation bottles with ground water seals
- DO Meter calibrated via Winkler titration or optical luminescence probe
- Seed microbial culture (domestic sewage supernatant)
- Nutrient buffer solutions (Phosphate, Magnesium sulfate, Calcium chloride, Ferric chloride)

3. CALCULATION FORMULA:
BOD5 (mg/L) = [(DO_initial - DO_final) - (Seed_blank_initial - Seed_blank_final) * f] / P
Where:
f = ratio of seed volume in sample to seed volume in blank
P = decimal volumetric fraction of sample used

4. QA/QC ACCEPTANCE CRITERIA:
- Glucose-Glutamic Acid (GGA) Standard Check: Must recover 198 ± 30.5 mg/L BOD5.
- Unseeded Dilution Water Blank: DO depletion must NOT exceed 0.20 mg/L after 5 days.

(c) EDEN Environmental Intelligence Platform - Official Training Dossier
""".trimIndent()

    private fun generateWasteGuideContent(): String = """
================================================================================
HAZARDOUS WASTE CRADLE-TO-GRAVE UNIFORM MANIFEST & COMPATIBILITY PROTOCOL
REGULATORY STANDARD: BASEL CONVENTION • US EPA RCRA SUBTITLE C (40 CFR 262)
================================================================================

1. UNIFORM HAZARDOUS WASTE MANIFEST PROTOCOL:
- Box 1: Generator EPA Identification Number & Physical Facility Address
- Box 9: Designated Transporter Registration and Emergency Contact (24/7)
- Box 10: Designated Treatment, Storage, and Disposal Facility (TSDF)
- Box 13: US DOT Shipping Description (Proper Name, Hazard Class, UN Number, Packing Group)

2. CHEMICAL COMPATIBILITY MATRIX:
- GROUP 1-A (Strong Acids) + GROUP 1-B (Cyanide/Sulfide wastes): GENERATES LETHAL HCN/H2S GAS. NEVER MIX.
- GROUP 2-A (Strong Bases) + GROUP 2-B (Aluminum/Zinc powder): GENERATES EXPLOSIVE H2 GAS.
- GROUP 3-A (Oxidizers: Nitric acid, Perchlorates) + GROUP 3-B (Hydrocarbons): CAUSES SPONTANEOUS COMBUSTION.

3. EMERGENCY SPILL CONTAINMENT:
- Minimum secondary containment bunding must hold 110% of the single largest container volume.
- Neutralization kits, non-sparking shovels, and Tychem Level B suits required at all storage bays.

(c) EDEN Environmental Intelligence Platform - Official Training Dossier
""".trimIndent()

    private fun generateMlPythonPackContent(): String = """
# ==============================================================================
# EDEN MACHINE LEARNING FOR ENVIRONMENTAL SCIENCE: JUPYTER NOTEBOOK SCRIPT
# FRAMEWORK: PyTorch 2.0 • Scikit-Learn • GeoPandas
# TASK: 72-Hour PM2.5 Air Pollution Forecasting using LSTM Neural Networks
# ==============================================================================

import numpy as np
import pandas as pd
import torch
import torch.nn as nn
from sklearn.preprocessing import MinMaxScaler

class EnvironmentalLSTMPredictor(nn.Module):
    \"\"\"Physics-aware LSTM model for atmospheric pollutant time-series.\"\"\"
    def __init__(self, input_dim=6, hidden_dim=64, num_layers=2, output_dim=1):
        super(EnvironmentalLSTMPredictor, self).__init__()
        self.hidden_dim = hidden_dim
        self.num_layers = num_layers
        self.lstm = nn.LSTM(input_dim, hidden_dim, num_layers, batch_first=True, dropout=0.2)
        self.fc = nn.Linear(hidden_dim, output_dim)

    def forward(self, x):
        # x shape: (batch_size, sequence_length, input_features)
        # Features: [PM2.5, PM10, Temperature, Humidity, WindSpeed, PlanetaryBoundaryLayerHeight]
        out, _ = self.lstm(x)
        predictions = self.fc(out[:, -1, :])
        return predictions

print("EDEN Climate Informatics ML Suite initialized successfully.")
print("Model architecture ready for training on Sentinel-5P and ground station telemetry.")
""".trimIndent()
}
