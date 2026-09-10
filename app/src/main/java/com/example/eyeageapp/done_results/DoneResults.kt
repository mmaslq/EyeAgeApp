package com.example.eyeageapp.done_results

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.eyeageapp.EyeAgeAnalyzer

@Composable
fun DoneResults(
    doneResVM: DoneResultsViewModel
){
    val testResData by doneResVM.testStateFlowResults.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Test Results",
            fontSize = 38.sp,
            modifier = Modifier.padding(vertical = 35.dp,horizontal = 0.dp)
        )

        LazyColumn(modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(15.dp)) {

            items(testResData){ resData ->
                val screenDistance = resData.result.screenDistance
                val numHeight = resData.result.numHeight
                val measurements = resData.measurements.map { it.screenDistanceMeasurement }
                val cornerRadius = 15.dp
                val eyeAgeRangeStr = EyeAgeAnalyzer.calcEyeAgeRange(screenDistance,numHeight)

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
                    Text(
                        text = "Age range: $eyeAgeRangeStr years ",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                    Text(
                        text = "Eye Distance: $screenDistance cm",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
                    Text(
                        text = "Text Size: $numHeight mm",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 15.dp)
                    )
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
}