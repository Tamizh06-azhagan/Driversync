package com.example.driversync_trackanddrive.response

data class DriverProfileResponse(
    val status: Boolean,
    val message: String,
    val driver: Drive
)

data class Drive(
    val name: String,
    val username: String
)
