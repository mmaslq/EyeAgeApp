package com.example.eyeageapp.done_tests

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eyeageapp.R

@Composable
fun DoneTests(
    doneTestsVM: DoneTestsViewModel,
    onNavigateBack: (Long) -> Unit,
    onCalibClick: (Long) -> Unit
){

    val calibrationData by doneTestsVM.calibrationStateFlowResults.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Calibration Results",
            fontSize = 38.sp,
            modifier = Modifier.padding(vertical = 35.dp,horizontal = 0.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(15.dp)) {

            items(calibrationData){ calibData ->
                val id = calibData.result.id
                val eyeDistance = calibData.result.eyeDistance
                val measurements = calibData.measurements.map { it.eyeDistanceMeasurement }
                val cornerRadius = 15.dp

                Column(
                    verticalArrangement = Arrangement.SpaceAround,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                        .clip(RoundedCornerShape(cornerRadius))
                        .border(
                            width = 3.dp,
                            color = Color.Black,
                            shape = RoundedCornerShape(cornerRadius)
                        )
                        .background(Color.DarkGray)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .padding(20.dp)
                            .background(Color.DarkGray)
                    ){
                        Text(
                            text = "Eye Distance: $eyeDistance cm",
                            fontSize = 20.sp,
                            modifier = Modifier.clickable(onClick = {
                                onCalibClick(id)
                            })
                        )

                        IconButton(onClick = {
                            doneTestsVM.updateCurCalibrationId(id)
                        }) {
                            Icon(
                                painter = painterResource(getCheckBoxPainterId(
                                    id == doneTestsVM.calibrationId
                                )),
                                contentDescription = null,
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.border(3.dp,Color.Gray),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        Text(
                            text = "Measurements: [cm]",
                            modifier = Modifier.padding(15.dp)
                        )
                        measurements.forEach {
                            Text(
                                text = it.toString(),
                                modifier = Modifier.padding(5.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    doneTestsVM.calibrationId?.let{ id ->
        BackHandler(enabled = true) {
            onNavigateBack(id)
        }
    }
}

fun getCheckBoxPainterId(checked: Boolean): Int{
    if (checked){
        return R.drawable.outline_check_box_24
    }
    return R.drawable.outline_check_box_outline_blank_24
}