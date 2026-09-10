package com.example.eyeageapp.calibration

import android.graphics.Bitmap
import androidx.camera.core.CameraInfo
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.eyeageapp.db.CalibrationDao
import com.example.eyeageapp.db.CalibrationMeasurement
import com.example.eyeageapp.db.CalibrationResult
import com.example.eyeageapp.info_camera.InfoCameraViewModel
import com.example.eyeageapp.info_camera.NavigationEvent

class CalibrationViewModel private constructor(
    private val calibrationDao: CalibrationDao,
    private val recModelHandler: RecModelHandler
): InfoCameraViewModel() {


    val eyeDistances = mutableStateListOf<Float>()

    val eyeGapAnalyzer = EyeGapAnalyzer()

    override suspend fun onAnalyzingImageJob(
        bitmap: Bitmap,
        rotationDegrees: Int,
        cameraInfo: CameraInfo
    ) {
        val eyeDistance = eyeGapAnalyzer.calculatePupillaryDistanceMm(
            recModelHandler,
            bitmap
        ).toFloat()

        sendEyeDistanceMessage(eyeDistance)
    }

    private fun sendEyeDistanceMessage(eyeDistance: Float){
        if (eyeDistances.size == 2){
            val avgEyeDistance = (eyeDistances.sum() + eyeDistance) / 30.0f

            showInfoMessage(
                message = """
                    Measured eye distance $eyeDistance mm
                    Avg eye distance  $avgEyeDistance cm
                """.trimIndent(),
                action = {
                    eyeDistances.add(eyeDistance)
                    val resultId = insertDbData(CalibrationResult(eyeDistance = avgEyeDistance))
                    eyeDistances.clear()
                    NavigationEvent.ToHome(resultId)
                },
            )
        }else{
            showInfoMessage(
                message = "Measured eye distance $eyeDistance mm",
                action = {eyeDistances.add(eyeDistance); null}
            )
        }
    }

    private suspend fun insertDbData(calibrationResult: CalibrationResult): Long{
        val id = calibrationDao.insertCalibrationResult(calibrationResult)

        eyeDistances.forEach {
            calibrationDao.insertCalibrationMeasurements(
                CalibrationMeasurement(
                    calibrationId = id,
                    eyeDistanceMeasurement = it/10.0f
                )
            )
        }
        return id
    }

    override fun onCleared() {
        recModelHandler.close()
    }

    companion object{
        fun Factory(
            calibrationDao: CalibrationDao,
            weightsHolder: RecModelHandler
        ): ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    CalibrationViewModel(
                        calibrationDao,
                        weightsHolder
                    )
                }
            }
        }
    }

}