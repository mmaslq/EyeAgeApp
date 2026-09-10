package com.example.eyeageapp.menu

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
    onTest: (Long) -> Unit,
    onDoneTests: (Long?) -> Unit,
    onCalibration: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = onCalibration){
            Text("Kalibracja EAR")
        }
        Button(onClick = {onDoneTests(menuVM.calibratedId)}){
            Text("Moje testy")
        }
        menuVM.calibratedId?.let{ id ->
            Button(onClick = {onTest(id)}){
                Text("Wykonaj Test")
            }
        }
    }
}