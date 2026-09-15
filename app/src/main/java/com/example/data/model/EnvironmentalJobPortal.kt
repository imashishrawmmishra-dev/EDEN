package com.example.data.model

import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * Sector classifications for Environmental & Sustainability careers.
 */
enum class JobSectorType(val displayName: String, val badgeColorHex: Long) {
    ALL("All Sectors", 0xFF2E7D32),
    NGO("NGO & Non-Profit", 0xFF00897B),
    PUBLIC("Public & Government", 0xFF1565C0),
    PRIVATE("Private & Corporate", 0xFF6A1B9A)
}

/**
 * Platform category groupings requested by user.
 */
enum class JobPortalCategory(val title: String, val subtitle: String) {
    GLOBAL_AGGREGATOR("Global Aggregators", "Largest worldwide job aggregators & multi-industry networks"),
    MIDDLE_EAST_UAE("Middle East & UAE Specific Portals", "Leading regional networks across GCC, UAE & MENA"),
    TECH_STARTUP("Tech & Startup Niches", "ClimateTech, CleanTech, Environmental data & software roles"),
    REMOTE_FREELANCE("Remote & Freelance Markets", "Screened remote ESG consulting, LCA & freelance contracts"),
    GOVERNMENT_CHANNELS("Government Channels", "Official federal, state & public sector environmental positions"),
    NGO_IMPACT("NGO & Non-Profit Portals", "Global conservation, intergovernmental & environmental NGOs")
}

/**
 * Definition of a job searching website with direct deep-linking capabilities.
 */
data class EnvironmentalJobPortal(
    val id: String,
    val name: String,
    val category: JobPortalCategory,
    val sectorType: JobSectorType,
    val description: String,
    val websiteUrl: String,
    val queryUrlBuilder: (query: String, location: String) -> String,
    val accentColorHex: Long,
    val badgeLabel: String,
    val isPrimaryAggregator: Boolean = false,
    val tags: List<String> = emptyList()
) {
    fun buildSearchUrl(query: String, location: String = ""): String {
        return queryUrlBuilder(query.trim(), location.trim())
    }
}

/**
 * Curated environmental job opening across NGO, Public, and Private sectors.
 */
data class EnvironmentalJobOpening(
    val id: String,
    val title: String,
    val organization: String,
    val sectorType: JobSectorType,
    val portalId: String,
    val portalName: String,
    val location: String,
    val salaryEstimate: String,
    val employmentType: String,
    val summary: String,
    val requiredCompetencies: List<String>,
    val targetSearchQuery: String,
    val externalUrl: String
)

/**
 * Repository providing all environmental job portals and direct search URLs.
 */
object EnvironmentalJobRepository {

    private fun encode(str: String): String {
        return try {
            URLEncoder.encode(str, StandardCharsets.UTF_8.toString())
        } catch (_: Exception) {
            str.replace(" ", "+")
        }
    }

    /**
     * All job searching websites requested by the user, plus dedicated NGO & Public channels.
     */
    val portals: List<EnvironmentalJobPortal> = listOf(
        // ----------------------------------------------------
        // Global Aggregators across all industries
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "indeed",
            name = "Indeed",
            category = JobPortalCategory.GLOBAL_AGGREGATOR,
            sectorType = JobSectorType.PRIVATE,
            description = "The world's largest job aggregator across all industries.",
            websiteUrl = "https://www.indeed.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&l=${encode(loc)}" else ""
                "https://www.indeed.com/jobs?q=$qEnc$lEnc"
            },
            accentColorHex = 0xFF2164F3,
            badgeLabel = "World's Largest Aggregator",
            isPrimaryAggregator = true,
            tags = listOf("All Industries", "Instant Apply", "Salary Tools")
        ),
        EnvironmentalJobPortal(
            id = "linkedin",
            name = "LinkedIn",
            category = JobPortalCategory.GLOBAL_AGGREGATOR,
            sectorType = JobSectorType.PRIVATE,
            description = "Best for professional networking and direct recruiter outreach.",
            websiteUrl = "https://www.linkedin.com/jobs",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental sustainability" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&location=${encode(loc)}" else ""
                "https://www.linkedin.com/jobs/search/?keywords=$qEnc$lEnc"
            },
            accentColorHex = 0xFF0A66C2,
            badgeLabel = "Recruiter Outreach & Networking",
            isPrimaryAggregator = true,
            tags = listOf("Networking", "Recruiters", "Global Scale")
        ),
        EnvironmentalJobPortal(
            id = "ziprecruiter",
            name = "ZipRecruiter",
            category = JobPortalCategory.GLOBAL_AGGREGATOR,
            sectorType = JobSectorType.PRIVATE,
            description = "Great for fast, mobile-friendly applications.",
            websiteUrl = "https://www.ziprecruiter.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&location=${encode(loc)}" else ""
                "https://www.ziprecruiter.com/candidate/search?search=$qEnc$lEnc"
            },
            accentColorHex = 0xFF007A33,
            badgeLabel = "1-Click Mobile Apply",
            isPrimaryAggregator = true,
            tags = listOf("Quick Apply", "Alerts", "Direct AI Match")
        ),
        EnvironmentalJobPortal(
            id = "glassdoor",
            name = "Glassdoor",
            category = JobPortalCategory.GLOBAL_AGGREGATOR,
            sectorType = JobSectorType.PRIVATE,
            description = "Excellent for researching salaries and company reviews while searching.",
            websiteUrl = "https://www.glassdoor.com/Job/index.htm",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&locT=C&locId=0&locKeyword=${encode(loc)}" else ""
                "https://www.glassdoor.com/Job/jobs.htm?sc.keyword=$qEnc$lEnc"
            },
            accentColorHex = 0xFF0CAA41,
            badgeLabel = "Salary Benchmarks & Reviews",
            isPrimaryAggregator = true,
            tags = listOf("Salaries", "Company Ratings", "Interviews")
        ),
        EnvironmentalJobPortal(
            id = "careerbuilder",
            name = "CareerBuilder",
            category = JobPortalCategory.GLOBAL_AGGREGATOR,
            sectorType = JobSectorType.PRIVATE,
            description = "A trusted, long-standing database for corporate roles.",
            websiteUrl = "https://www.careerbuilder.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&location=${encode(loc)}" else ""
                "https://www.careerbuilder.com/jobs?keywords=$qEnc$lEnc"
            },
            accentColorHex = 0xFF0F52BA,
            badgeLabel = "Corporate Career Database",
            isPrimaryAggregator = true,
            tags = listOf("Corporate", "Enterprise", "Long-Standing")
        ),

        // ----------------------------------------------------
        // Middle East & UAE Specific Portals
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "bayt",
            name = "Bayt",
            category = JobPortalCategory.MIDDLE_EAST_UAE,
            sectorType = JobSectorType.PRIVATE,
            description = "The largest professional employment network in the Middle East.",
            websiteUrl = "https://www.bayt.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                "https://www.bayt.com/en/search-jobs/?keyword=$qEnc"
            },
            accentColorHex = 0xFF00838F,
            badgeLabel = "#1 Middle East Network",
            tags = listOf("GCC & MENA", "UAE", "Saudi Arabia", "Qatar")
        ),
        EnvironmentalJobPortal(
            id = "gulftalent",
            name = "GulfTalent",
            category = JobPortalCategory.MIDDLE_EAST_UAE,
            sectorType = JobSectorType.PRIVATE,
            description = "Extensively used by top companies across the GCC and UAE.",
            websiteUrl = "https://www.gulftalent.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                "https://www.gulftalent.com/jobs/search?keywords=$qEnc"
            },
            accentColorHex = 0xFFD84315,
            badgeLabel = "GCC & UAE Enterprise",
            tags = listOf("Top GCC Firms", "Masdar City", "Abu Dhabi", "Dubai")
        ),
        EnvironmentalJobPortal(
            id = "naukrigulf",
            name = "Naukrigulf",
            category = JobPortalCategory.MIDDLE_EAST_UAE,
            sectorType = JobSectorType.PRIVATE,
            description = "Highly popular for regional corporate and technical openings.",
            websiteUrl = "https://www.naukrigulf.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                "https://www.naukrigulf.com/environmental-jobs?keywords=$qEnc"
            },
            accentColorHex = 0xFF1976D2,
            badgeLabel = "Regional Technical Openings",
            tags = listOf("Technical Engineers", "EHS Officers", "Petrochemical")
        ),

        // ----------------------------------------------------
        // Tech & Startup Niches
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "wellfound",
            name = "Wellfound (AngelList)",
            category = JobPortalCategory.TECH_STARTUP,
            sectorType = JobSectorType.PRIVATE,
            description = "Exclusively for startup jobs with transparent salary and equity data.",
            websiteUrl = "https://wellfound.com/jobs",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "climate tech environmental" else "environmental $q")
                "https://wellfound.com/jobs?keyword=$qEnc"
            },
            accentColorHex = 0xFFE91E63,
            badgeLabel = "Startup Salary & Equity",
            tags = listOf("ClimateTech", "CleanTech Startups", "Equity Offered")
        ),
        EnvironmentalJobPortal(
            id = "dice",
            name = "Dice",
            category = JobPortalCategory.TECH_STARTUP,
            sectorType = JobSectorType.PRIVATE,
            description = "The leading board dedicated strictly to technology and engineering roles.",
            websiteUrl = "https://www.dice.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental engineer" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&location=${encode(loc)}" else ""
                "https://www.dice.com/jobs?q=$qEnc$lEnc"
            },
            accentColorHex = 0xFFE65100,
            badgeLabel = "Tech & Engineering Board",
            tags = listOf("Environmental Tech", "Data Systems", "IoT Monitoring")
        ),

        // ----------------------------------------------------
        // Remote & Freelance Markets
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "flexjobs",
            name = "FlexJobs",
            category = JobPortalCategory.REMOTE_FREELANCE,
            sectorType = JobSectorType.PRIVATE,
            description = "Hand-screened, scam-free remote and flexible corporate listings.",
            websiteUrl = "https://www.flexjobs.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                "https://www.flexjobs.com/search?search=$qEnc"
            },
            accentColorHex = 0xFF2E7D32,
            badgeLabel = "Scam-Free Remote Listings",
            tags = listOf("100% Remote", "Flexible Hours", "ESG Advisory")
        ),
        EnvironmentalJobPortal(
            id = "upwork",
            name = "Upwork",
            category = JobPortalCategory.REMOTE_FREELANCE,
            sectorType = JobSectorType.PRIVATE,
            description = "The premier global marketplace for freelance contract work.",
            websiteUrl = "https://www.upwork.com",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental consulting carbon" else "environmental $q")
                "https://www.upwork.com/nx/search/jobs/?q=$qEnc"
            },
            accentColorHex = 0xFF14A800,
            badgeLabel = "Global Freelance Contracts",
            tags = listOf("Consulting Contracts", "Hourly Projects", "ESG Reports")
        ),

        // ----------------------------------------------------
        // Government Channels (Public)
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "usajobs",
            name = "USAJOBS",
            category = JobPortalCategory.GOVERNMENT_CHANNELS,
            sectorType = JobSectorType.PUBLIC,
            description = "The official portal for US federal government positions.",
            websiteUrl = "https://www.usajobs.gov",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental protection specialist" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&l=${encode(loc)}" else ""
                "https://www.usajobs.gov/Search/Results?k=$qEnc$lEnc"
            },
            accentColorHex = 0xFF0D47A1,
            badgeLabel = "Official US Federal Portal",
            tags = listOf("EPA (Enviro Protection Agency)", "NOAA", "USGS", "Dept of Energy")
        ),

        // ----------------------------------------------------
        // NGO & Non-Profit Portals (Direct NGO Connect)
        // ----------------------------------------------------
        EnvironmentalJobPortal(
            id = "idealist",
            name = "Idealist",
            category = JobPortalCategory.NGO_IMPACT,
            sectorType = JobSectorType.NGO,
            description = "The world's largest NGO and non-profit social & environmental impact portal.",
            websiteUrl = "https://www.idealist.org",
            queryUrlBuilder = { q, loc ->
                val qEnc = encode(if (q.isBlank()) "environmental" else "environmental $q")
                val lEnc = if (loc.isNotBlank()) "&location=${encode(loc)}" else ""
                "https://www.idealist.org/en/jobs?q=$qEnc$lEnc"
            },
            accentColorHex = 0xFF00796B,
            badgeLabel = "#1 NGO & Social Impact",
            tags = listOf("Non-Profits", "Grassroots NGOs", "Global Impact")
        ),
        EnvironmentalJobPortal(
            id = "un_careers",
            name = "UN Careers / UNDP",
            category = JobPortalCategory.NGO_IMPACT,
            sectorType = JobSectorType.NGO,
            description = "United Nations Environment Programme (UNEP) & intergovernmental climate roles.",
            websiteUrl = "https://careers.un.org",
            queryUrlBuilder = { q, _ ->
                val qEnc = encode(if (q.isBlank()) "environment" else q)
                "https://careers.un.org/lbw/Home.aspx"
            },
            accentColorHex = 0xFF009EDB,
            badgeLabel = "UNEP & Intergovernmental",
            tags = listOf("UNEP", "UNDP", "SDG Climate Action", "Global Diplomatic")
        ),
        EnvironmentalJobPortal(
            id = "conservation_job_board",
            name = "Conservation Job Board",
            category = JobPortalCategory.NGO_IMPACT,
            sectorType = JobSectorType.NGO,
            description = "Dedicated global board for ecology, wildlife biology and conservation jobs.",
            websiteUrl = "https://www.conservationjobboard.com",
            queryUrlBuilder = { q, _ ->
                val qEnc = encode(if (q.isBlank()) "ecology conservation" else "conservation $q")
                "https://www.conservationjobboard.com/?q=$qEnc"
            },
            accentColorHex = 0xFF33691E,
            badgeLabel = "Ecology & Wildlife Conservation",
            tags = listOf("Field Biology", "Protected Reserves", "Biodiversity")
        )
    )

    /**
     * Popular environmental keywords for 1-click filtering.
     */
    val popularEnvironmentalKeywords: List<String> = listOf(
        "Environmental Engineer",
        "Sustainability Manager",
        "Carbon Accounting & ESG",
        "Air Quality Specialist",
        "Water Quality Scientist",
        "EIA Specialist (Impact Assessment)",
        "EHS Safety Officer",
        "GIS Environmental Analyst",
        "CleanTech Specialist",
        "Conservation Biologist"
    )

    /**
     * Curated active environmental openings across NGO, Public, and Private sectors.
     */
    val curatedOpenings: List<EnvironmentalJobOpening> = listOf(
        EnvironmentalJobOpening(
            id = "job-001",
            title = "Senior Industrial Emissions & Air Quality Engineer",
            organization = "Masdar Clean Energy / UAE Regional Projects",
            sectorType = JobSectorType.PRIVATE,
            portalId = "gulftalent",
            portalName = "GulfTalent / Bayt",
            location = "Abu Dhabi, UAE (Hybrid)",
            salaryEstimate = "AED 32,000 – 42,000 / month",
            employmentType = "Full-Time",
            summary = "Supervise continuous emission monitoring systems (CEMS), EPA Method 2 chimney flue gas velocity traverses, and regional air dispersal modeling.",
            requiredCompetencies = listOf("USEPA Method 2 & 5", "Isokinetic Sampling", "AERMOD Dispersion", "ISO 14001 EHS"),
            targetSearchQuery = "air quality engineer masdar uae",
            externalUrl = "https://www.gulftalent.com/jobs/search?keywords=environmental+engineer"
        ),
        EnvironmentalJobOpening(
            id = "job-002",
            title = "Environmental Protection Specialist (Air & Water Enforcement)",
            organization = "U.S. Environmental Protection Agency (EPA)",
            sectorType = JobSectorType.PUBLIC,
            portalId = "usajobs",
            portalName = "USAJOBS",
            location = "Washington, DC / Regional Field Offices",
            salaryEstimate = "$86,962 – $113,047 / year (GS-12)",
            employmentType = "Permanent Public Service",
            summary = "Perform regulatory audits under the Clean Air Act and Clean Water Act. Audit industrial compliance with National Ambient Air Quality Standards (NAAQS).",
            requiredCompetencies = listOf("Clean Air Act (CAA)", "Clean Water Act (CWA)", "Environmental Auditing", "Regulatory Enforcement"),
            targetSearchQuery = "environmental protection specialist EPA",
            externalUrl = "https://www.usajobs.gov/Search/Results?k=environmental+protection+specialist"
        ),
        EnvironmentalJobOpening(
            id = "job-003",
            title = "Climate Change Adaptation & Forest Conservation Lead",
            organization = "World Wildlife Fund (WWF) International",
            sectorType = JobSectorType.NGO,
            portalId = "idealist",
            portalName = "Idealist / UN Careers",
            location = "Geneva, Switzerland / Remote Field Work",
            salaryEstimate = "$75,000 – $92,000 / year",
            employmentType = "NGO Contract",
            summary = "Lead field-level biodiversity restoration programs, community carbon forestry offsets, and international REDD+ greenhouse gas accounting initiatives.",
            requiredCompetencies = listOf("REDD+ Carbon Accounting", "GIS Spatial Analysis", "Biodiversity Metrics", "NGO Grant Oversight"),
            targetSearchQuery = "conservation project manager WWF",
            externalUrl = "https://www.idealist.org/en/jobs?q=conservation+project+manager"
        ),
        EnvironmentalJobOpening(
            id = "job-004",
            title = "Global ESG & Scope 1/2/3 Corporate Carbon Auditor",
            organization = "Deloitte / ERM Environmental Resources Management",
            sectorType = JobSectorType.PRIVATE,
            portalId = "linkedin",
            portalName = "LinkedIn / Indeed",
            location = "New York / London / Dubai (Remote Friendly)",
            salaryEstimate = "$115,000 – $145,000 / year",
            employmentType = "Full-Time Corporate",
            summary = "Lead enterprise greenhouse gas inventories per GHG Protocol Corporate Standard and ISO 14064. Guide Fortune 500 supply-chain decarbonization pathways.",
            requiredCompetencies = listOf("GHG Protocol Corporate Standard", "ISO 14064", "Scope 1, 2 & 3 Auditing", "SBTi Verification"),
            targetSearchQuery = "carbon accounting auditor ESG",
            externalUrl = "https://www.linkedin.com/jobs/search/?keywords=carbon+accounting+auditor"
        ),
        EnvironmentalJobOpening(
            id = "job-005",
            title = "CleanTech Environmental IoT & Sensor Software Engineer",
            organization = "Watershed ClimateTech Solutions",
            sectorType = JobSectorType.PRIVATE,
            portalId = "wellfound",
            portalName = "Wellfound / Dice",
            location = "San Francisco, CA / Remote Global",
            salaryEstimate = "$140,000 – $180,000 / yr + 0.15% Equity",
            employmentType = "Tech Startup",
            summary = "Architect real-time telemetry pipelines connecting ambient air quality sensors, stack monitoring hardware, and automated emissions reporting dashboards.",
            requiredCompetencies = listOf("Environmental Telemetry", "Kotlin / Python", "IoT Protocols (MQTT)", "Emissions Calculation Engines"),
            targetSearchQuery = "climate tech software engineer",
            externalUrl = "https://wellfound.com/jobs?keyword=climate+tech"
        ),
        EnvironmentalJobOpening(
            id = "job-006",
            title = "Freelance Life Cycle Assessment (LCA) & EPD Specialist",
            organization = "Global Environmental Consultancy Consortium",
            sectorType = JobSectorType.PRIVATE,
            portalId = "upwork",
            portalName = "Upwork / FlexJobs",
            location = "100% Remote (Worldwide)",
            salaryEstimate = "$65 – $110 / hour",
            employmentType = "Freelance & Flexible Contract",
            summary = "Deliver cradle-to-gate Life Cycle Assessments and ISO 14040/44 Environmental Product Declarations (EPD) for manufacturing and clean energy clients.",
            requiredCompetencies = listOf("ISO 14040 / 14044 LCA", "SimaPro / openLCA", "EPD Generation", "Embodied Carbon Modeling"),
            targetSearchQuery = "life cycle assessment LCA freelance",
            externalUrl = "https://www.upwork.com/nx/search/jobs/?q=life+cycle+assessment+environmental"
        ),
        EnvironmentalJobOpening(
            id = "job-007",
            title = "Municipal Wastewater Treatment Plant Quality Specialist",
            organization = "Dubai Municipality / Public Works Authority",
            sectorType = JobSectorType.PUBLIC,
            portalId = "bayt",
            portalName = "Bayt / Naukrigulf",
            location = "Dubai, UAE",
            salaryEstimate = "AED 22,000 – 30,000 / month",
            employmentType = "Public Sector / Government Entity",
            summary = "Oversee continuous microbiological testing, effluent compliance (BOD, COD, Heavy Metals), and sludge biosolid recycling per WHO / EPA standards.",
            requiredCompetencies = listOf("APHA Standard Methods", "ISO 5667 Water Sampling", "BOD & COD Kinetics", "SCADA Operations"),
            targetSearchQuery = "water quality specialist dubai",
            externalUrl = "https://www.bayt.com/en/search-jobs/?keyword=water+quality+specialist"
        )
    )

    fun filterPortals(
        category: JobPortalCategory? = null,
        sectorType: JobSectorType? = null,
        query: String = ""
    ): List<EnvironmentalJobPortal> {
        return portals.filter { portal ->
            val matchCategory = category == null || portal.category == category
            val matchSector = sectorType == null || sectorType == JobSectorType.ALL || portal.sectorType == sectorType || portal.sectorType == JobSectorType.ALL
            val matchQuery = query.isBlank() ||
                    portal.name.contains(query, ignoreCase = true) ||
                    portal.description.contains(query, ignoreCase = true) ||
                    portal.tags.any { it.contains(query, ignoreCase = true) }
            matchCategory && matchSector && matchQuery
        }
    }
}
