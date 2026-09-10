package com.example.eyeageapp.done_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.toRoute
import com.example.eyeageapp.db.DoneResultsDao
import com.example.eyeageapp.navigation.DataScreen
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlin.time.Duration.Companion.seconds

class DoneResultsViewModel(
    doneResultsDao: DoneResultsDao,
    calibrationId: Long
): ViewModel() {

    val testStateFlowResults = doneResultsDao.getResultsData(calibrationId).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5.seconds.inWholeMilliseconds),
        initialValue = emptyList()
    )

    companion object{
        fun Factory(
            doneResultsDao: DoneResultsDao
        ): ViewModelProvider.Factory {
            return viewModelFactory {
                initializer{
                    val savedStateHandle = this.createSavedStateHandle()
                    val route: DataScreen.DoneResults = savedStateHandle.toRoute()
                    DoneResultsViewModel(doneResultsDao,route.calibrationId)
                }
            }
        }
    }
}