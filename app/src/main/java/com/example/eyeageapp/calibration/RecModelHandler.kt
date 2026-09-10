package com.example.eyeageapp.calibration

import android.content.Context
import com.google.mediapipe.framework.image.MPImage
import com.google.mediapipe.tasks.core.BaseOptions
import com.google.mediapipe.tasks.vision.core.RunningMode
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarker
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult

class RecModelHandler(modelAssetPath: String, context: Context): AutoCloseable {


    private val baseOptionsBuilder = BaseOptions.builder()
        .setModelAssetPath(modelAssetPath)

    private val optionsBuilder = FaceLandmarker.FaceLandmarkerOptions.builder()
        .setBaseOptions(baseOptionsBuilder.build())
        .setMinFaceDetectionConfidence(0.5f)
        .setMinTrackingConfidence(0.5f)
        .setNumFaces(1)
        .setRunningMode(RunningMode.IMAGE)

    private var faceLandmarker: FaceLandmarker = FaceLandmarker.createFromOptions(
        context.applicationContext,
        optionsBuilder.build()
    )

    fun detect(mpImage: MPImage): FaceLandmarkerResult {
        return faceLandmarker.detect(mpImage)
    }


    override fun close() {
        faceLandmarker.close()
    }
}