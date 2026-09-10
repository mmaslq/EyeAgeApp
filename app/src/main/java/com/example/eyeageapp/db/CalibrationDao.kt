package com.example.eyeageapp.db

import androidx.room.Dao
import androidx.room.Insert


@Dao
interface CalibrationDao{

    @Insert
    suspend fun insertCalibrationMeasurements(calibrationMeasurement: CalibrationMeasurement)

    @Insert
    suspend fun insertCalibrationResult(calibrationResult: CalibrationResult): Long

}
