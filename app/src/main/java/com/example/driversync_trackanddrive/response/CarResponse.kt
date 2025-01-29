package com.example.driversync_trackanddrive.model

data class CarResponse(
    val status: String,
    val data: ArrayList<Car>
)

data class Car(
    val id: String?,
    val userid: String,
    val car_name: String,
    val image_path: String,
    val condition: String
)
