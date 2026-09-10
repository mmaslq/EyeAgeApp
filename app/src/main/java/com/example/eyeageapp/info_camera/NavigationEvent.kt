package com.example.eyeageapp.info_camera

interface NavigationEvent {
    data class ToHome(val id: Long): NavigationEvent
}