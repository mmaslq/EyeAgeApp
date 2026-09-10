package com.example.eyeageapp.info_camera

import android.graphics.Bitmap
import androidx.camera.core.CameraInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

abstract class InfoCameraViewModel: ViewModel() {

    var hasRunInitialSetup = false
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var infoMessage by mutableStateOf<Info?>(null)
        private set

    private var analyzeJob: Job? = null

    internal fun analyzeImage(bitmap: Bitmap, rotationDegrees: Int, cameraInfo: CameraInfo){

        if (analyzeJob?.isActive == true){
            showErrorMessage("Please Wait!")
            return
        }

        analyzeJob = viewModelScope.launch {
            try {
                onAnalyzingImageJob(bitmap,rotationDegrees,cameraInfo)
            } catch (e: Exception) {
                showErrorMessage(e.message)
            }
        }
    }

    abstract suspend fun onAnalyzingImageJob(
        bitmap: Bitmap,
        rotationDegrees: Int,
        cameraInfo: CameraInfo
    )


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

    fun showInfoMessage(
        message: String,
        action: (suspend () -> NavigationEvent?)? = null,
        title: String? = null,
        extra: Boolean = false
    ){
        infoMessage = Info(message,title,action,extra)
    }

    private fun hideInfoMessage(){
        infoMessage = null
    }

    fun onInfoClosedSuccess(
        info: Info,
        onNavigationEvent: ((NavigationEvent) -> Unit) = {}
    ){
        hideInfoMessage()
        viewModelScope.launch {
            val navEvent = info.action?.invoke()

            navEvent?.let{
                onNavigationEvent(it)
            }
        }
    }

    fun onInfoClosedDismiss(){
        hideInfoMessage()
    }

    fun onEndInitialSetup(){
        hasRunInitialSetup = true
    }

}