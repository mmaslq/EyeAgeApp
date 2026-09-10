package com.example.eyeageapp.info_camera

data class Info(
    val message: String,
    val title: String? = null,
    val action: (suspend () -> NavigationEvent?)? = null,
    val extra: Boolean = false
)
