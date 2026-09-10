package com.example.eyeageapp.navigation

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eyeageapp.db.EyeAgeCalcDatabase
import com.example.eyeageapp.done_results.DoneResults
import com.example.eyeageapp.done_results.DoneResultsViewModel
import com.example.eyeageapp.done_tests.DoneTests
import com.example.eyeageapp.done_tests.DoneTestsViewModel
import kotlinx.serialization.Serializable

sealed interface DataScreen {
    @Serializable
    data class DoneTests(val calibrationResultId: Long? = null): MainScreen
    @Serializable
    data class DoneResults(val calibrationId: Long): DataScreen
}


fun NavGraphBuilder.featureDataScreenGraph(
    navController: NavController,
    db: EyeAgeCalcDatabase
){
    composable<DataScreen.DoneTests>{
        val doneTestVM: DoneTestsViewModel = viewModel(
            factory = DoneTestsViewModel.Factory(db.doneTestsDao)
        )

        DoneTests(
            doneTestsVM = doneTestVM,
            onNavigateBack = { id ->
                navController.navigate(MainScreen.Home(id))
            },
            onCalibClick = { id ->
                navController.navigate(DataScreen.DoneResults(id))
            }
        )
    }

    composable<DataScreen.DoneResults>{
        val doneResults: DoneResultsViewModel = viewModel(
            factory = DoneResultsViewModel.Factory(db.doneResultsDao)
        )

        DoneResults(doneResVM = doneResults)
    }
}