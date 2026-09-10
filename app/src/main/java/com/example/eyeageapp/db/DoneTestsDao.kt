package com.example.eyeageapp.db

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Query
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface DoneTestsDao {

    @Transaction
    @Query("SELECT * FROM CalibrationResult ORDER BY id DESC")
    fun getCalibrationData(): Flow<List<CalibrationData>>
}

data class CalibrationData(
    @Embedded val result: CalibrationResult,
    @Relation(
        parentColumn = "id",
        entityColumn = "calibrationId"
    )
    val measurements: List<CalibrationMeasurement>
)
