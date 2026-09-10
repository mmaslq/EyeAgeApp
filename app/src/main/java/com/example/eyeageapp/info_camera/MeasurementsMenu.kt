package com.example.eyeageapp.info_camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eyeageapp.R

@Composable
fun MeasurementsMenu(
    data: MutableList<Float>,
    units: String
){
    if (data.isEmpty()){
        return
    }

    val scrollState = rememberScrollState()

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        Text(
            text = "Wykonane Pomiary",
            fontSize = 18.sp,
            modifier = Modifier.padding(8.dp)
        )

        Column(
            modifier = Modifier.verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            data.forEachIndexed { idx, fVal ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(3.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Pomiar ${idx + 1}, odległość: $fVal $units")
                    IconButton(onClick = {
                        data.removeAt(idx)
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.outline_cancel_24),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}