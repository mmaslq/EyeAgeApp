package com.example.eyeageapp.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity (
    foreignKeys = [
        ForeignKey(
            entity = CalibrationResult::class,
            parentColumns = ["id"],
            childColumns = ["calibrationId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TestResult(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val calibrationId: Long,
    val screenDistance: Float,
    val numHeight: Float
)
