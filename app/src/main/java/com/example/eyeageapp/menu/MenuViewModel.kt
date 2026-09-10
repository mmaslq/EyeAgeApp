package com.example.eyeageapp.menu

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import com.example.eyeageapp.navigation.MainScreen

class MenuViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val calibratedId: Long?

    init {
        val route: MainScreen.Home = savedStateHandle.toRoute()
        calibratedId = route.calibrationResultId
    }
}