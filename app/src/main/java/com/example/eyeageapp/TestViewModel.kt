package com.example.eyeageapp

import android.graphics.Bitmap
import androidx.camera.core.CameraInfo
import androidx.camera.core.ImageProxy
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.round
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class TestViewModel: ViewModel() {
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var screenDistance by mutableStateOf<Int?>(null)
        private set

    private val eyeAnalyzer = EyeAnalyzer()
    private var analyzeJob: Job? = null

    fun analyzeImage(bitmap: Bitmap, rotationDegrees: Int, cameraInfo: CameraInfo){

        if (analyzeJob?.isActive == true){
            analyzeJob!!.cancel()
            showErrorMessage("Do NOT Spam Button!")
            return
        }

        analyzeJob = viewModelScope.launch {
            try {
                val distance = eyeAnalyzer.analyzeBitmap(
                    bitmap,
                    rotationDegrees,
                    cameraInfo
                )
                screenDistance = round(distance).toInt()
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                showErrorMessage(e.message)
            }
        }
    }

    private var errorJob: Job? = null

    fun showErrorMessage(
        message: String?,
        duration: Duration = 3.seconds,
        defaultMessage: String = "Something went wrong!"
    ){

        if (errorJob?.isActive == true) return

        errorJob = viewModelScope.launch {
            try {
                errorMessage = message ?: defaultMessage
                delay(duration)
            }finally {
                errorMessage = null
            }
        }
    }
}