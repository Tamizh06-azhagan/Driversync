package com.example.driversync_trackanddrive.response

data class DriverProfileResponse(
    val status: Boolean,
    val message: String,
    val driver: DriverData
)

data class DriverData(
    val name: String,
    val username: String,
    val image_path: String
)
