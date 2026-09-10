package com.example.eyeageapp.db

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DoneResultsDao {

    @Transaction
    @Query("SELECT * FROM TestResult WHERE calibrationId == :calibrationId ORDER BY id DESC")
    fun getResultsData(calibrationId: Long): Flow<List<ResultsData>>
}


data class ResultsData(
    @Embedded val result: TestResult,
    @Relation(
        parentColumn = "id",
        entityColumn = "testId"
    )
    val measurements: List<TestMeasurement>
)