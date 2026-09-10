package com.example.eyeageapp.test

import android.graphics.Bitmap
import androidx.camera.core.CameraInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.eyeageapp.EyeAgeAnalyzer
import com.example.eyeageapp.navigation.MainScreen
import com.example.eyeageapp.db.TestDao
import com.example.eyeageapp.db.TestMeasurement
import com.example.eyeageapp.db.TestResult
import com.example.eyeageapp.info_camera.InfoCameraViewModel
import com.example.eyeageapp.info_camera.NavigationEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random
import kotlin.time.Duration.Companion.seconds

class TestViewModel(
    private val testDao: TestDao,
    private val calibrationResultId: Long,
): InfoCameraViewModel() {

    val screenDistances = mutableStateListOf<Float>()

    var changingNumsSizeMm by mutableFloatStateOf(EyeAgeAnalyzer.DEFAULT_NUMS_HEIGHT_MM)
        private set

    var lastChangedNum by mutableStateOf<String>("012")

    val changingNums = flow<String>{
        var lastStr = "012"
        while(true){
            val strNums = getRandomStrNums()
            emit(strNums)
            lastChangedNum = lastStr
            lastStr = strNums
            delay(4.seconds)
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = lastChangedNum
    )

    private val eyeAnalyzer = EyeAnalyzer()

    override suspend fun onAnalyzingImageJob(
        bitmap: Bitmap,
        rotationDegrees: Int,
        cameraInfo: CameraInfo
    ) {
        val numHeight = changingNumsSizeMm
        val eyeDistance = testDao.getEyeDistance(calibrationResultId)

        val distance = eyeAnalyzer.analyzeBitmap(
            bitmap,
            rotationDegrees,
            cameraInfo,
            eyeDistance
        )

        sendEyeScreenMessage(distance,numHeight)
    }


    fun sendEyeScreenMessage(
        screenDistance: Float,
        numHeight: Float
    ){
        if (screenDistances.size == 2){
            val avgScreenDistance = (screenDistances.sum() + screenDistance) / 3.0f
            val eyeAgeRangeStr = EyeAgeAnalyzer.calcEyeAgeRange(
                avgScreenDistance,
                numHeight
            )

            showInfoMessage(
                message = """
                    Measured screen distance $screenDistance cm
                    Age range: $eyeAgeRangeStr years 
                """.trimIndent(),
                action = {
                    screenDistances.add(screenDistance)
                    insertDbData(
                        TestResult(
                            calibrationId = calibrationResultId,
                            screenDistance = avgScreenDistance,
                            numHeight = numHeight
                        )
                    )
                    screenDistances.clear()
                    NavigationEvent.ToHome(calibrationResultId)
                }
            )
        }else{
            showInfoMessage(
                message = "Measured screen distance $screenDistance cm",
                action = {screenDistances.add(screenDistance); null}
            )
        }
    }

    suspend fun insertDbData(testResult: TestResult) {
        val testId = testDao.insertTestResult(testResult)

        screenDistances.forEach {
            testDao.insertTestMeasurement(
                TestMeasurement(
                    testId = testId,
                    screenDistanceMeasurement = it
                )
            )
        }
    }

    fun changeChangingNumsSize(byMm: Float = 0.1f){
        changingNumsSizeMm += byMm
    }


    private fun getRandomStrNums(count: Int = 3): String{
        val strB = StringBuilder()
        for (i in 1..count){
            strB.append((Random.nextInt(10)))
        }
        return strB.toString()
    }

    companion object{

        fun Factory(testDao: TestDao): ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {

                    val savedStateHandle = this.createSavedStateHandle()

                    val route: MainScreen.Test = savedStateHandle.toRoute()
                    TestViewModel(
                        testDao,
                        route.calibrationResultId
                    )
                }
            }
        }
    }
}