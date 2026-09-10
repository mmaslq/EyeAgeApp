package com.example.eyeageapp.db

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    foreignKeys = [
        ForeignKey(
            entity = TestResult::class,
            parentColumns = ["id"],
            childColumns = ["testId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["testId"])]
)
data class TestMeasurement(
    @PrimaryKey (autoGenerate = true)
    val id: Long = 0L,
    val testId: Long,
    val screenDistanceMeasurement: Float
)
