package com.example.solutions.model

enum class AudienceType(val label: String, val badge: String) {
    ALL("All Stakeholders", "General"),
    STUDENTS("Students & Scholars", "Academic"),
    PROFESSIONALS("Professionals & Auditors", "Industry"),
    RESEARCHERS("Researchers & Scientists", "R&D")
}

enum class EsgDomainCategory(val displayName: String) {
    GOVERNANCE_AND_FINANCE("ESG, Finance & Governance"),
    ASSESSMENT_AND_COMPLIANCE("Impact Assessment & EC"),
    CLIMATE_AND_CARBON("Carbon Markets & Climate Science"),
    HEALTH_SAFETY_AND_LAB("HSE & Environmental Laboratory"),
    AI_AND_MODELLING("AI, Machine Learning & Numerical Models"),
    WASTE_AND_CIRCULARITY("Solid & Hazardous Waste Science"),
    OCEAN_AND_ACOUSTICS("Acoustic Ecology & UN Research")
}

data class DomainArticle(
    val title: String,
    val sourceOrJournal: String,
    val year: String,
    val keyTakeaway: String,
    val url: String
)

data class DomainJob(
    val title: String,
    val salaryRange: String,
    val sector: String,
    val coreResponsibilities: String,
    val demandRating: String = "High Demand"
)

data class JobApproachGuide(
    val recommendedDegrees: List<String>,
    val essentialCertifications: List<String>,
    val handsOnPortfolioProjects: List<String>,
    val technicalInterviewFocus: String,
    val careerRoadmap: String
)

data class ToolSoftwareInfo(
    val name: String,
    val category: String,
    val description: String,
    val officialUrl: String
)

data class OfficialSiteLink(
    val organization: String,
    val label: String,
    val url: String
)

data class AudienceGuide(
    val headline: String,
    val keyConcepts: List<String>,
    val actionItems: List<String>,
    val recommendedReadings: String
)

data class EsgDomainItem(
    val id: String,
    val title: String,
    val shortName: String,
    val category: EsgDomainCategory,
    val summary: String,
    val requirement: String,
    val need: String,
    val purpose: String,
    val goal: String,
    val scope: String,
    val currentScenario: String,
    val futureOutlook: String,
    val necessity: String,
    val audienceGuides: Map<AudienceType, AudienceGuide>,
    val articles: List<DomainArticle>,
    val jobs: List<DomainJob>,
    val jobApproach: JobApproachGuide,
    val toolsAndSoftware: List<ToolSoftwareInfo>,
    val officialSites: List<OfficialSiteLink>,
    val jobSearchQuery: String,
    val askEdenPrompt: String,
    val relatedCalculatorType: String? = null,
    val relatedProcedureDomain: String? = null
)

data class SustainabilityFormulaItem(
    val id: String,
    val name: String,
    val formulaSymbolic: String,
    val description: String,
    val demandAndUse: String,
    val unit: String,
    val param1Label: String,
    val param1Default: Double,
    val param2Label: String,
    val param2Default: Double,
    val param3Label: String? = null,
    val param3Default: Double? = null,
    val calculate: (p1: Double, p2: Double, p3: Double?) -> Double,
    val interpretation: (result: Double) -> String
)

data class DownloadableResourceItem(
    val id: String,
    val title: String,
    val category: String,
    val fileExtension: String, // "PDF", "CSV", "PY", "TXT"
    val estimatedSize: String,
    val description: String,
    val targetAudience: String,
    val requiresOtp: Boolean = true,
    val contentGenerator: () -> String
)
