package com.example.driversync_trackanddrive.response

data class FetchResponse(
    val status: String,
    val data: List<Fetch>
)


data class Fetch(
    val id: String,
    val userid: String,
    val availability: String,
    val availability_date: String,
    val name: String,
    val username: String,
    val email: String,
    val role: String,
    val password: String,
    val contact_number: String,
    val image_path: String
)
