package com.example.eyeageapp.test

import android.graphics.Bitmap
import android.hardware.camera2.CameraCharacteristics
import androidx.annotation.OptIn
import androidx.camera.camera2.interop.Camera2CameraInfo
import androidx.camera.camera2.interop.ExperimentalCamera2Interop
import androidx.camera.core.CameraInfo
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceLandmark
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.math.hypot

class EyeAnalyzer {

    private val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_ALL)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
        .build()

    val detector = FaceDetection.getClient(options)

    suspend fun analyzeBitmap(
        bitmap: Bitmap,
        rotationDegrees: Int,
        cameraInfo: CameraInfo,
        avgEyeDistance: Float
    ): Float = withContext(Dispatchers.Default){

        val image = InputImage.fromBitmap(bitmap,rotationDegrees)

        val focalLen = calkFocalDistance(image.width,cameraInfo)

        val result = suspendCancellableCoroutine<Float> { continuation ->
            detector.process(image)
                .addOnSuccessListener { faces ->
                    if (continuation.isActive){
                        val distance = calcFaceDistanceCM(faces, focalLen, avgEyeDistance)
                        if (distance == null){
                            continuation.resumeWithException(Exception("Something went wrong!"))
                        }else{
                            continuation.resume(distance)
                        }
                    }
                }
                .addOnFailureListener { e ->
                    if (continuation.isActive){
                        continuation.resumeWithException(e)
                    }
                }
        }

        return@withContext result
    }

    private fun calcFaceDistanceCM(
        faces: List<Face>,
        focalLen: Float,
        avgEyeDistance: Float
    ): Float?{
        val face = faces.firstOrNull() ?: return null

        val leftEye = face.getLandmark(FaceLandmark.LEFT_EYE)?.position ?: return null
        val rightEye = face.getLandmark(FaceLandmark.RIGHT_EYE)?.position ?: return null

        val eyeDistancePixels = hypot(
            (rightEye.x - leftEye.x).toDouble(),
            (rightEye.y - leftEye.y).toDouble()
        ).toFloat()

        //val avgEyeDistance = 6.3f

        return avgEyeDistance * (focalLen / eyeDistancePixels)
    }

    @OptIn(ExperimentalCamera2Interop::class)
    private fun calkFocalDistance(imageWidthPx: Int,cameraInfo: CameraInfo): Float{
        val camera2Info = Camera2CameraInfo.from(cameraInfo)

        // 3. Query physical focal length in mm (e.g., 3.5 mm)
        val focalLengths = camera2Info.getCameraCharacteristic(
            CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS
        )
        val focalLengthMm = focalLengths?.firstOrNull() ?: 3.5f

        // 4. Query physical sensor width in mm (e.g., 4.8 mm)
        val sensorSize = camera2Info.getCameraCharacteristic(
            CameraCharacteristics.SENSOR_INFO_PHYSICAL_SIZE
        )
        val sensorWidthMm = sensorSize?.width ?: 4.8f

        // 5. Calculate focal length in pixels for ANY phone hardware!
        return (focalLengthMm / sensorWidthMm) * imageWidthPx
    }
}