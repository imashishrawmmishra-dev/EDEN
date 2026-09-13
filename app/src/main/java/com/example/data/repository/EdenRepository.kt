package com.example.data.repository

import android.content.Context
import com.example.data.local.EdenDatabase
import com.example.data.local.EdenInitialData
import com.example.data.model.CalculationEntity
import com.example.data.model.CompetencyEntity
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class EdenRepository(context: Context) {
    private val db = EdenDatabase.getInstance(context)
    private val knowledgeDao = db.knowledgeDao()
    private val resourceDao = db.resourceDao()
    private val calculationDao = db.calculationDao()
    private val monitoringDao = db.monitoringDao()
    private val competencyDao = db.competencyDao()

    suspend fun ensureSeeded() = withContext(Dispatchers.IO) {
        val existing = knowledgeDao.getAllKnowledge().first()
        if (existing.isEmpty()) {
            knowledgeDao.insertAll(EdenInitialData.knowledgeNodes)
            resourceDao.insertAll(EdenInitialData.resources)
            competencyDao.insertAll(EdenInitialData.competencies)
            monitoringDao.insertAll(EdenInitialData.sampleMonitoringPoints)
        }
    }

    fun getAllKnowledge(): Flow<List<KnowledgeEntity>> = knowledgeDao.getAllKnowledge()

    fun searchKnowledge(query: String): Flow<List<KnowledgeEntity>> =
        if (query.isBlank()) knowledgeDao.getAllKnowledge() else knowledgeDao.searchKnowledge(query)

    fun getKnowledgeByCategory(category: String): Flow<List<KnowledgeEntity>> =
        if (category == "All") knowledgeDao.getAllKnowledge() else knowledgeDao.getKnowledgeByCategory(category)

    suspend fun getKnowledgeById(id: String): KnowledgeEntity? = knowledgeDao.getKnowledgeById(id)

    suspend fun findMatchingKnowledgeSync(query: String): List<KnowledgeEntity> = withContext(Dispatchers.IO) {
        val exactMatches = knowledgeDao.findMatchingSync(query)
        if (exactMatches.isNotEmpty()) return@withContext exactMatches

        val keywords = query.split(Regex("[^a-zA-Z0-9]+")).filter { it.length >= 3 }
        val results = mutableListOf<KnowledgeEntity>()
        for (kw in keywords) {
            val matches = knowledgeDao.findMatchingSync(kw)
            for (m in matches) {
                if (results.none { it.id == m.id }) {
                    results.add(m)
                    if (results.size >= 4) return@withContext results
                }
            }
        }
        results
    }

    fun getAllResources(): Flow<List<ResourceEntity>> = resourceDao.getAllResources()

    fun getResourcesByTopic(topic: String): Flow<List<ResourceEntity>> =
        if (topic == "All") resourceDao.getAllResources() else resourceDao.getResourcesByTopic(topic)

    fun getRecentCalculations(): Flow<List<CalculationEntity>> = calculationDao.getRecentCalculations()

    suspend fun saveCalculation(calc: CalculationEntity): Long = withContext(Dispatchers.IO) {
        calculationDao.insertCalculation(calc)
    }

    suspend fun deleteCalculation(id: Long) = withContext(Dispatchers.IO) {
        calculationDao.deleteCalculation(id)
    }

    suspend fun clearCalculations() = withContext(Dispatchers.IO) {
        calculationDao.clearHistory()
    }

    fun getAllMonitoringPoints(): Flow<List<MonitoringEntity>> = monitoringDao.getAllDataPoints()

    suspend fun addMonitoringPoint(point: MonitoringEntity): Long = withContext(Dispatchers.IO) {
        monitoringDao.insertDataPoint(point)
    }

    fun getAllCompetencies(): Flow<List<CompetencyEntity>> = competencyDao.getAllCompetencies()

    suspend fun updateCompetencyProgress(id: String, progress: Int, completed: Boolean) = withContext(Dispatchers.IO) {
        competencyDao.updateProgress(id, progress, completed)
    }
}
