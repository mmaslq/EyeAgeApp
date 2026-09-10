package com.example.eyeageapp

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eyeageapp.navigation.MainScreen
import com.example.eyeageapp.navigation.featureDataScreenGraph
import com.example.eyeageapp.navigation.featureMainScreenGraph
import com.example.eyeageapp.ui.theme.EyeAgeAppTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        (application as App).db
    }


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
                    startDestination = MainScreen.Home()
                ){
                    featureMainScreenGraph(
                        navController = navController,
                        db = db,
                        appContext = applicationContext
                    )
                    featureDataScreenGraph(
                        navController = navController,
                        db = db
                    )
                }
            }
        }
    }
}