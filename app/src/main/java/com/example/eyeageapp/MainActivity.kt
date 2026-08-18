package com.example.eyeageapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eyeageapp.ui.theme.EyeAgeAppTheme

class MainActivity : ComponentActivity() {

    private val requestNotificationLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (!isGranted){
            finishAffinity()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationLauncher.launch(Manifest.permission.CAMERA)

        setContent {
            EyeAgeAppTheme {
                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = MainScreen.Home.name
                ){
                    composable(MainScreen.Home.name){
                        val menuVM: MenuViewModel = viewModel()
                        Menu(
                            menuVM = menuVM,
                            onTest = {navController.navigate(MainScreen.Test.name)},
                            onCalibration = {},
                            onHistory = {}
                        )
                    }

                    composable(MainScreen.Test.name){
                        val testVM: TestViewModel = viewModel()
                        Test(
                            testVM = testVM
                        )
                    }
                }
            }
        }
    }
}