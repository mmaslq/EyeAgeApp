package com.example.eyeageapp.navigation

import android.content.Context
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.eyeageapp.R
import com.example.eyeageapp.calibration.Calibration
import com.example.eyeageapp.calibration.CalibrationViewModel
import com.example.eyeageapp.calibration.RecModelHandler
import com.example.eyeageapp.db.EyeAgeCalcDatabase
import com.example.eyeageapp.menu.Menu
import com.example.eyeageapp.menu.MenuViewModel
import com.example.eyeageapp.test.Test
import com.example.eyeageapp.test.TestViewModel
import kotlinx.serialization.Serializable

sealed interface MainScreen {
    @Serializable
    data class Home(val calibrationResultId: Long? = null): MainScreen

    @Serializable
    data object Calibration: MainScreen

    @Serializable
    data class Test(val calibrationResultId: Long): MainScreen
}

fun NavGraphBuilder.featureMainScreenGraph(
    navController: NavController,
    db: EyeAgeCalcDatabase,
    appContext: Context
){
    composable<MainScreen.Home>{
        val menuVM: MenuViewModel = viewModel()
        Menu(
            menuVM = menuVM,
            onTest = { id ->
                navController.navigate(MainScreen.Test(id))
            },
            onCalibration = {navController.navigate(MainScreen.Calibration)},
            onDoneTests = { id ->
                navController.navigate(DataScreen.DoneTests(id))
            }
        )
    }

    composable<MainScreen.Test>{
        val testVM: TestViewModel = viewModel(
            factory = TestViewModel.Factory(db.testDao)
        )
        Test(
            testVM = testVM,
            onEnd = {navController.navigate(MainScreen.Home(it))}
        )
    }

    composable<MainScreen.Calibration>{
        val calibrationVM: CalibrationViewModel = viewModel(
            factory = CalibrationViewModel.Factory(
                db.calibrationDao,
                RecModelHandler(
                    stringResource(R.string.rec_model_path),
                    appContext
                )
            )
        )
        Calibration(
            calibrationVM = calibrationVM,
            onEnd = { id ->
                navController.navigate(MainScreen.Home(id))
            }
        )
    }
}