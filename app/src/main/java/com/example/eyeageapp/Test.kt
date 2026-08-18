package com.example.eyeageapp

import android.content.Context
import android.graphics.Bitmap
import androidx.camera.core.CameraInfo
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun Test(testVM: TestViewModel){

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // 1. OFFICIAL GOOGLE CONTROLLER (Handles binding & lifecycle automatically)
    val cameraController = remember {
        LifecycleCameraController(context).apply {
            cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA
            // Enable in-memory photo capture
            setEnabledUseCases(CameraController.IMAGE_CAPTURE)
            // Bind to lifecycle in 1 line
            bindToLifecycle(lifecycleOwner)
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
    ){
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                PreviewView(context).apply{
                    controller = cameraController
                }
            }
        )
        if (testVM.screenDistance != null){
            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "Last measured screen distance: ${testVM.screenDistance} cm")
            }
        }
        Box (
            modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f),
            contentAlignment = Alignment.BottomCenter
        ){
            if (testVM.errorMessage == null){
                Button(onClick = {
                    cameraController.triggerFrameCapture(
                        context,
                        {bitmap, rotationDegrees, cameraInfo ->
                            testVM.analyzeImage(
                                bitmap,
                                rotationDegrees,
                                cameraInfo)
                        },
                        {testVM.showErrorMessage(it.message)}
                    )
                }){
                    Text("Analyze Frame")
                }
            }else{
                Text(text = testVM.errorMessage!!)
            }
        }
    }
}

private fun CameraController.triggerFrameCapture(
    context: Context,
    onSuccess: (Bitmap, Int, CameraInfo) -> Unit,
    onError: (e: ImageCaptureException) -> Unit = {}
) {
    takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {

            override fun onCaptureSuccess(image: ImageProxy) {
                // Official CameraX method to get direct Bitmap
                val camInfo = cameraInfo
                if (camInfo == null){
                    onError(ImageCaptureException(
                        ImageCapture.ERROR_INVALID_CAMERA,
                        "Camera Info is null!",
                        null
                    ))
                }else{
                    onSuccess(image.toBitmap(),image.imageInfo.rotationDegrees,camInfo)
                }
                image.close() // ALWAYS close!
            }

            override fun onError(e: ImageCaptureException) {
                onError(e)
                e.printStackTrace()
            }
        }
    )
}