package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a node in the structured Environmental Knowledge Graph.
 * Rule 1: Never invent environmental data.
 * Rule 2: Never invent citations.
 * Rule 7: Use authoritative sources.
 */
@Entity(tableName = "knowledge_nodes")
data class KnowledgeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String, // Air Quality, Water, Climate, Carbon, Waste, Soil, LCA, Monitoring
    val summary: String,
    val scientificContext: String,
    val formulaOrStandard: String,
    val authoritativeSource: String,
    val sourceYear: Int,
    val authority: String, // EPA, WHO, IPCC, ISO, EEA, UNEP
    val evidenceTier: String, // Tier 1: Regulatory Standard, Tier 2: Peer-Reviewed, Tier 3: Scientific Manual
    val relatedConcepts: String, // Comma-separated
    val level1Basic: String,
    val level2Scientific: String,
    val level3Laboratory: String,
    val level4Engineering: String,
    val level5Regulatory: String
)

@Entity(tableName = "environmental_resources")
data class ResourceEntity(
    @PrimaryKey val id: String,
    val title: String,
    val authorOrOrganization: String,
    val year: Int,
    val topic: String,
    val subtopic: String,
    val resourceType: String, // Standard, Peer-Reviewed Paper, Guidelines, Dataset, Technical Report
    val url: String,
    val authority: String,
    val evidenceLevel: String, // Tier 1 (Govt/Standard), Tier 2 (Academic/Journal)
    val keyFindings: String
)

@Entity(tableName = "calculation_history")
data class CalculationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val calculatorType: String, // Carbon Scope 1/2/3, Air Stack, Water BOD/COD, Noise Decibel, LCA
    val inputDescription: String,
    val resultSummary: String,
    val units: String,
    val assumptions: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "monitoring_datapoints")
data class MonitoringEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val parameter: String, // PM2.5, PM10, SO2, NOx, BOD, COD, pH, DO
    val value: Double,
    val unit: String,
    val location: String,
    val standardLimit: Double,
    val standardSource: String, // e.g., "WHO 2021 Annual: 5 µg/m³", "EPA NAAQS 24hr: 35 µg/m³"
    val timestamp: Long = System.currentTimeMillis()
) {
    val isExceedance: Boolean
        get() = value > standardLimit
}

@Entity(tableName = "user_competencies")
data class CompetencyEntity(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val progressPercent: Int, // 0..100
    val targetLevel: Int, // 1..5
    val isCompleted: Boolean = false
)
