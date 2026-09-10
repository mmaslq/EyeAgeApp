package com.example.eyeageapp.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TestResult::class,
        TestMeasurement::class,
        CalibrationResult::class,
        CalibrationMeasurement::class
    ],
    version = 1
)
abstract class EyeAgeCalcDatabase: RoomDatabase() {
    abstract val calibrationDao: CalibrationDao
    abstract val testDao: TestDao
    abstract val doneTestsDao: DoneTestsDao
    abstract val doneResultsDao: DoneResultsDao
}