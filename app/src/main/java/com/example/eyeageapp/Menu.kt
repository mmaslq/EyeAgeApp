package com.example.eyeageapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun Menu(
    menuVM: MenuViewModel,
    onTest: () -> Unit,
    onHistory: () -> Unit,
    onCalibration: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        if (!menuVM.calibrated){
            Button(onClick = onCalibration){
                Text("Kalibracja EAR")
            }
        }

        Button(onClick = onHistory){
            Text("Moje testy")
        }

        if (menuVM.calibrated){
            Button(onClick = onTest){
                Text("Wykonaj Test")
            }
        }
    }
}