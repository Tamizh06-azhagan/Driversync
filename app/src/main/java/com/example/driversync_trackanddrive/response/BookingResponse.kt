package com.example.driversync_trackanddrive.network

data class BookingResponse(
    val status: Boolean,
    val message: String,
    val data: List<BookingData>
)

data class BookingData(
    val booking_id: Int,
    val drivername: String,
    val status: String
)
