package com.example.eyeageapp

object EyeAgeAnalyzer {

    const val DEFAULT_NUMS_HEIGHT_MM = 1.0f

    fun calcEyeAgeRange(screenDistanceCm: Float, numHeightMm: Float): String{
        val visus = calcVisus(screenDistanceCm, numHeightMm)
        return getAgeRange(visus)
    }

    fun calcVisus(readingDistanceCm: Float, letterHeightMm: Float): Float {
        // Standard: litera 1.0 mm widziana z 40 cm = Visus 1.0
        val standardHeightMm = 1.0f
        val standardDistanceCm = 40.0f

        // Oblicz, z jakiej odległości ta litera powinna być widziana
        val expectedDistanceCm = (letterHeightMm / standardHeightMm) * standardDistanceCm

        // Visus = odległość czytania / oczekiwana odległość
        return readingDistanceCm / expectedDistanceCm
    }

    fun getAgeRange(visus: Float): String {
        return when {
            visus >= 1.0f -> "20–39"
            visus >= 0.8f -> "40–49"
            visus >= 0.6f -> "50–59"
            visus >= 0.4f -> "60–79"
            else -> "80+"
        }
    }

}