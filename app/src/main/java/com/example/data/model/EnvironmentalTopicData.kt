package com.example.data.model

import androidx.compose.ui.graphics.Color
import java.util.UUID

/**
 * High-level category for Environmental Intelligence & Quality Management
 */
enum class EnvironmentalDomain(val title: String, val code: String) {
    ALL("All Domains", "ALL"),
    AIR_QUALITY("Air Quality", "AIR"),
    WATER_QUALITY("Water Quality", "WATER"),
    ANIMALS_BIODIVERSITY("Animals & Biodiversity", "BIO"),
    PHYSICAL_QUALITIES("Physical Qualities & Climate", "PHYS"),
    SOIL_CONTROL("Soil Control & Health", "SOIL"),
    NOISE_QUALITY("Noise Quality & Acoustics", "NOISE"),
    FOOD_MICROBIOLOGY("Food Microbiology", "FOOD"),
    POLLUTION_TESTING("Pollution Testing", "TEST"),
    POLLUTION_MONITORING("Pollution Monitoring", "MON"),
    POLLUTION_REMEDIATION("Pollution Remediation", "REM")
}

/**
 * Quality benchmark band representation for charts
 */
data class QualityParameterBand(
    val parameterName: String,
    val optimalRange: String,
    val standardLimit: String,
    val unit: String,
    val regulatoryStandard: String,
    val currentBaseline: Float,
    val maxChartScale: Float
)

/**
 * Ongoing global scientific research initiative
 */
data class OngoingResearch(
    val title: String,
    val institution: String,
    val leadLocation: String,
    val stage: String, // e.g. "Field Trials (TRL 7)", "Clinical/Ecological Cohort", "Active Breakthrough"
    val summary: String,
    val publicationOrRef: String
)

/**
 * Quality management framework details (ISO, WHO, EPA, etc.)
 */
data class QualityManagementSystem(
    val frameworkName: String,
    val certificationCode: String,
    val governingBody: String,
    val scope: String,
    val complianceChecklist: List<String>
)

/**
 * User-entered custom field reading or observation note
 */
data class TopicUserObservation(
    val id: String = UUID.randomUUID().toString(),
    val topicId: String,
    val timestamp: Long = System.currentTimeMillis(),
    val parameterValue: String,
    val location: String,
    val observationNotes: String,
    val complianceStatus: String // "Compliant", "Advisory", "Critical Exceedance"
)

/**
 * Comprehensive Environmental Intelligence Topic Entity
 */
data class EnvironmentalTopic(
    val id: String,
    val domain: EnvironmentalDomain,
    val title: String,
    val subtitle: String,
    val levelBadge: String, // "Basic to Advanced", "Advanced Analytics", etc.
    val basicDescription: String,
    val advancedScience: String,
    val primaryIndicators: List<QualityParameterBand>,
    val qualityManagement: QualityManagementSystem,
    val ongoingResearches: List<OngoingResearch>,
    val defaultInputPrompt: String,
    val inputUnit: String,
    val thresholdGuideline: String
)
