package com.example.driversync_trackanddrive.response

data class GetAvailableDriversResponse(
    val status: Boolean,
    val message: String,
    val date: String,
    val total_drivers: Int,
    val drivers: List<Driver>
)

data class Driver(
    val availability_id: Int,
    val driver_id: Int,
    val driver_name: String,
    val driver_email: String,
    val driver_contact: Long,
    val availability_status: String,
    val availability_date: String
)