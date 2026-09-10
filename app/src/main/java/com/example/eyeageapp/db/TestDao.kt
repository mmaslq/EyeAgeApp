package com.example.eyeageapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TestDao {
    @Insert
    suspend fun insertTestResult(testResult: TestResult): Long

    @Insert
    suspend fun insertTestMeasurement(testMeasurement: TestMeasurement)

    @Query("SELECT eyeDistance from CalibrationResult WHERE id == :id")
    suspend fun getEyeDistance(id: Long): Float
}