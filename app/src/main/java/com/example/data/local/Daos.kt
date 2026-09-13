package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CalculationEntity
import com.example.data.model.CompetencyEntity
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeDao {
    @Query("SELECT * FROM knowledge_nodes ORDER BY title ASC")
    fun getAllKnowledge(): Flow<List<KnowledgeEntity>>

    @Query("SELECT * FROM knowledge_nodes WHERE title LIKE '%' || :query || '%' OR summary LIKE '%' || :query || '%' OR relatedConcepts LIKE '%' || :query || '%'")
    fun searchKnowledge(query: String): Flow<List<KnowledgeEntity>>

    @Query("SELECT * FROM knowledge_nodes WHERE category = :category ORDER BY title ASC")
    fun getKnowledgeByCategory(category: String): Flow<List<KnowledgeEntity>>

    @Query("SELECT * FROM knowledge_nodes WHERE id = :id LIMIT 1")
    suspend fun getKnowledgeById(id: String): KnowledgeEntity?

    @Query("SELECT * FROM knowledge_nodes WHERE title LIKE '%' || :keyword || '%' OR summary LIKE '%' || :keyword || '%' LIMIT 4")
    suspend fun findMatchingSync(keyword: String): List<KnowledgeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(nodes: List<KnowledgeEntity>)
}

@Dao
interface ResourceDao {
    @Query("SELECT * FROM environmental_resources ORDER BY year DESC")
    fun getAllResources(): Flow<List<ResourceEntity>>

    @Query("SELECT * FROM environmental_resources WHERE topic = :topic ORDER BY year DESC")
    fun getResourcesByTopic(topic: String): Flow<List<ResourceEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(resources: List<ResourceEntity>)
}

@Dao
interface CalculationDao {
    @Query("SELECT * FROM calculation_history ORDER BY timestamp DESC")
    fun getRecentCalculations(): Flow<List<CalculationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCalculation(calc: CalculationEntity): Long

    @Query("DELETE FROM calculation_history WHERE id = :id")
    suspend fun deleteCalculation(id: Long)

    @Query("DELETE FROM calculation_history")
    suspend fun clearHistory()
}

@Dao
interface MonitoringDao {
    @Query("SELECT * FROM monitoring_datapoints ORDER BY timestamp DESC")
    fun getAllDataPoints(): Flow<List<MonitoringEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDataPoint(data: MonitoringEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(points: List<MonitoringEntity>)
}

@Dao
interface CompetencyDao {
    @Query("SELECT * FROM user_competencies ORDER BY category ASC, title ASC")
    fun getAllCompetencies(): Flow<List<CompetencyEntity>>

    @Query("UPDATE user_competencies SET progressPercent = :progress, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateProgress(id: String, progress: Int, isCompleted: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(competencies: List<CompetencyEntity>)
}
