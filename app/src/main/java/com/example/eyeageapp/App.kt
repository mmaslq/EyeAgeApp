package com.example.eyeageapp

import android.app.Application
import androidx.room.Room
import com.example.eyeageapp.db.EyeAgeCalcDatabase

class App: Application() {
    val db by lazy{
        Room.databaseBuilder(
            applicationContext,
            EyeAgeCalcDatabase::class.java,
            "eyeAgeCalc.db"
        ).build()
    }
}