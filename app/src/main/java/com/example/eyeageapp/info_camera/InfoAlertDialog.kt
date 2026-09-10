package com.example.eyeageapp.info_camera

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties

@Composable
fun InfoAlertDialog(
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: (() -> Unit)? = null,
    title: String? = null,
    content: @Composable (() -> Unit)
){
    AlertDialog(
        title = {
            title?.let {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter
                ){
                    Text(text = it)
                }
            }
        },
        text = content,
        confirmButton = {
            Button(onClick = onSuccess){
                Text("Ok")
            }
        },
        dismissButton = {
            onDismiss?.let{
                Button(onClick = it){
                    Text("Cancel")
                }
            }
        },
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        ),
        onDismissRequest = {},
        modifier = modifier
    )
}