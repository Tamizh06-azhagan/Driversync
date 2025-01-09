package com.example.driversync_trackanddrive.response

data class PriceResponse(
    val status: Boolean,
    val message: String,
    val origin: String,
    val destination: String,
    val days: Int,
    val price_per_day: String,
    val total_amount: Int
)
