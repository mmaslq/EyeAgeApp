package com.example.eyeageapp.calibration

import android.graphics.Bitmap
import com.google.mediapipe.framework.image.BitmapImageBuilder
import com.google.mediapipe.tasks.components.containers.NormalizedLandmark
import com.google.mediapipe.tasks.vision.facelandmarker.FaceLandmarkerResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.pow
import kotlin.math.sqrt

class EyeGapAnalyzer() {


    suspend fun calculatePupillaryDistanceMm(recModelHandler: RecModelHandler, bitmap: Bitmap): Double = withContext(Dispatchers.Default) {

        val mpImage = BitmapImageBuilder(bitmap).build()

        val result: FaceLandmarkerResult = recModelHandler.detect(mpImage)

        // Sprawdzenie czy AI wykryło twarz i czy posiada wymagane punkty (478 punktów z tęczówkami)
        if (result.faceLandmarks().isNullOrEmpty() || result.faceLandmarks()[0].size < 478) {
            throw Exception("Face or Iris isn't visible")
        }

        val landmarks = result.faceLandmarks()[0]


        val leftPupil = landmarks[468]          // Centrum lewej źrenicy
        val leftIrisLeft = landmarks[469]       // Lewa krawędź lewej tęczówki
        val leftIrisRight = landmarks[471]      // Prawa krawędź lewej tęczówki


        val rightPupil = landmarks[473]         // Centrum prawej źrenicy
        val rightIrisLeft = landmarks[474]      // Lewa krawędź prawej tęczówki
        val rightIrisRight = landmarks[476]     // Prawa krawędź prawej tęczówki


        val imageWidth = bitmap.width.toDouble()
        val imageHeight = bitmap.height.toDouble()

        val realPDMm = calcRealPDMm(
            imageWidth,
            imageHeight,
            leftPupil,
            leftIrisLeft,
            leftIrisRight,
            rightPupil,
            rightIrisLeft,
            rightIrisRight
        )

        return@withContext realPDMm
    }

    fun calcRealPDMm(
        imageWidth: Double,
        imageHeight: Double,
        leftPupil: NormalizedLandmark,
        leftIrisLeft: NormalizedLandmark,
        leftIrisRight: NormalizedLandmark,
        rightPupil: NormalizedLandmark,
        rightIrisLeft: NormalizedLandmark,
        rightIrisRight: NormalizedLandmark
    ): Double{

        val pdInPixels = calcPDInPx(
            imageWidth,
            imageHeight,
            leftPupil,
            rightPupil
        )

        val averageIrisWidthPx = calcAverageIrisWidthPx(
            imageWidth,
            imageHeight,
            leftIrisLeft,
            leftIrisRight,
            rightIrisLeft,
            rightIrisRight
        )

        // 7. Przeliczenie pikseli na milimetry (Kalibracja AI)
        // Biologiczna stała: średnia szerokość ludzkiej tęczówki to 11.7 milimetra
        val irisRealSizeMm = 11.7
        val mmPerPixel = irisRealSizeMm / averageIrisWidthPx

        // Rzeczywista odległość między źrenicami (PD) w mm
        val realPDMm = pdInPixels * mmPerPixel

        return realPDMm
    }

    fun calcAverageIrisWidthPx(
        imageWidth: Double,
        imageHeight: Double,
        leftIrisLeft: NormalizedLandmark,
        leftIrisRight: NormalizedLandmark,
        rightIrisLeft: NormalizedLandmark,
        rightIrisRight: NormalizedLandmark
    ): Double{

        val leftIrisWidthPx = sqrt(
            ((leftIrisRight.x() - leftIrisLeft.x()) * imageWidth).pow(2) +
                    ((leftIrisRight.y() - leftIrisLeft.y()) * imageHeight).pow(2)
        )
        val rightIrisWidthPx = sqrt(
            ((rightIrisRight.x() - rightIrisLeft.x()) * imageWidth).pow(2) +
                    ((rightIrisRight.y() - rightIrisLeft.y()) * imageHeight).pow(2)
        )

        val averageIrisWidthPx = (leftIrisWidthPx + rightIrisWidthPx) / 2.0

        return averageIrisWidthPx
    }

    fun calcPDInPx(
        imageWidth: Double,
        imageHeight: Double,
        leftPupil: NormalizedLandmark,
        rightPupil: NormalizedLandmark
    ): Double{

        // MediaPipe zwraca znormalizowane współrzędne (0.0 do 1.0), musimy je przeliczyć na piksele
        val lpX = leftPupil.x() * imageWidth
        val lpY = leftPupil.y() * imageHeight
        val rpX = rightPupil.x() * imageWidth
        val rpY = rightPupil.y() * imageHeight

        return sqrt((rpX - lpX).pow(2) + (rpY - lpY).pow(2))
    }

}