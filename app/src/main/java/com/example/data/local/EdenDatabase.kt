package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CalculationEntity
import com.example.data.model.CompetencyEntity
import com.example.data.model.KnowledgeEntity
import com.example.data.model.MonitoringEntity
import com.example.data.model.ResourceEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        KnowledgeEntity::class,
        ResourceEntity::class,
        CalculationEntity::class,
        MonitoringEntity::class,
        CompetencyEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class EdenDatabase : RoomDatabase() {
    abstract fun knowledgeDao(): KnowledgeDao
    abstract fun resourceDao(): ResourceDao
    abstract fun calculationDao(): CalculationDao
    abstract fun monitoringDao(): MonitoringDao
    abstract fun competencyDao(): CompetencyDao

    companion object {
        @Volatile
        private var INSTANCE: EdenDatabase? = null

        fun getInstance(context: Context): EdenDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    EdenDatabase::class.java,
                    "eden_environmental_intelligence.db"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            // Populate database asynchronously on creation
                            INSTANCE?.let { database ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    database.knowledgeDao().insertAll(EdenInitialData.knowledgeNodes)
                                    database.resourceDao().insertAll(EdenInitialData.resources)
                                    database.competencyDao().insertAll(EdenInitialData.competencies)
                                    database.monitoringDao().insertAll(EdenInitialData.sampleMonitoringPoints)
                                }
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
