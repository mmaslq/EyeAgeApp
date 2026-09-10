package com.example.eyeageapp.calibration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.eyeageapp.R
import com.example.eyeageapp.info_camera.InfoCamera
import com.example.eyeageapp.info_camera.MeasurementsMenu
import com.example.eyeageapp.info_camera.NavigationEvent

@Composable
fun Calibration(
    calibrationVM: CalibrationViewModel,
    onEnd: (Long) -> Unit
){
    val testDescription = stringResource(R.string.calibration_description)

    InfoCamera(
        infoCameraVM = calibrationVM,
        onInfoClick = {
            calibrationVM.showInfoMessage(
                message = testDescription,
                title = "Instrukcja",
                extra = true
            )
        },
        infoContent = { info ->
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally,
            ){
                Text(text = info.message, modifier = Modifier.padding(15.dp))
                if (info.extra){
                    Spacer(modifier = Modifier.height(15.dp))
                    MeasurementsMenu(calibrationVM.eyeDistances,"mm")
                }
            }
        },
        onNavigationEvent = { navEvent ->
            when(navEvent){
                is NavigationEvent.ToHome -> {
                    onEnd(navEvent.id)
                }
            }
        }
    )
}