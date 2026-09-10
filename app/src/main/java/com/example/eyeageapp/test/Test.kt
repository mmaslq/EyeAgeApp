package com.example.eyeageapp.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eyeageapp.R
import com.example.eyeageapp.info_camera.InfoCamera
import com.example.eyeageapp.info_camera.MeasurementsMenu
import com.example.eyeageapp.info_camera.NavigationEvent

@Composable
fun Test(
    testVM: TestViewModel,
    onEnd: (Long) -> Unit
){
    val testDescription = stringResource(R.string.test_description)


    val changingNums by testVM.changingNums.collectAsStateWithLifecycle()

    InfoCamera(
        infoCameraVM = testVM,
        onInfoClick = {
            testVM.showInfoMessage(
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
                    MeasurementsMenu(testVM.screenDistances,"cm")
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
    ){
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.25f).padding(30.dp),
            contentAlignment = Alignment.CenterEnd
        ){
            Box(modifier = Modifier.background(Color.White)){
                Text(
                    text = "Last changed number: ${testVM.lastChangedNum}",
                    fontSize = 15.sp
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ){
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .defaultMinSize(150.dp,150.dp)
                    .padding(10.dp),
                contentAlignment = Alignment.Center
            ){
                Text(
                    text = changingNums,
                    fontSize = mmToFixedSp(testVM.changingNumsSizeMm)
                )
            }
        }
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.End,
            modifier = Modifier.fillMaxSize().padding(horizontal = 35.dp, vertical = 5.dp)
        ){
            IconButton (
                onClick = {testVM.changeChangingNumsSize(0.1f)}
            ){
                Icon(
                    painter = painterResource(R.drawable.outline_keyboard_arrow_up_24),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )
            }
            Spacer(modifier = Modifier.padding(vertical = 25.dp, horizontal = 0.dp))
            IconButton (
                onClick = {testVM.changeChangingNumsSize(-0.1f)}
            ){
                Icon(
                    painter = painterResource(R.drawable.outline_keyboard_arrow_down_24),
                    contentDescription = null,
                    modifier = Modifier.size(55.dp)
                )
            }
        }
    }
}

@Composable
fun mmToFixedSp(hInMm: Float): TextUnit {
    val context = LocalContext.current
    val density = LocalDensity.current.density

    // 1. Get the real hardware physical xdpi from the screen driver
    val xdpi = context.resources.displayMetrics.xdpi

    // 2. Calculate true physical pixels required for hInMm
    val targetPx = (hInMm / 25.4f) * xdpi

    // 3. Convert physical pixels back to Compose sp (targetPx / density)
    return (targetPx / density).sp
}