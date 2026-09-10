package com.example.eyeageapp.done_tests

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.eyeageapp.db.DoneTestsDao
import com.example.eyeageapp.navigation.DataScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

class DoneTestsViewModel(
    doneTestDao: DoneTestsDao,
    calibrationId: Long?
): ViewModel() {

    var calibrationId by mutableStateOf<Long?>(calibrationId)
        private set

    val calibrationStateFlowResults = doneTestDao.getCalibrationData().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5.seconds.inWholeMilliseconds),
        initialValue = emptyList()
    )

    fun updateCurCalibrationId(calibrationId: Long){
        this.calibrationId = calibrationId
    }




    companion object{
        fun Factory(
            doneTestDao: DoneTestsDao
        ): ViewModelProvider.Factory{
            return viewModelFactory {
                initializer {
                    val savedStateHandle = this.createSavedStateHandle()
                    val route: DataScreen.DoneTests = savedStateHandle.toRoute()

                    DoneTestsViewModel(doneTestDao, route.calibrationResultId)
                }
            }
        }
    }
}