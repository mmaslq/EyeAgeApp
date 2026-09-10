package com.example.eyeageapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CalibrationResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val eyeDistance: Float,
)
