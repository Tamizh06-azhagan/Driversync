package com.example.driversync_trackanddrive.response

data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData?
)

data class UserData(
    val id: String,
    val name: String,
    val username: String,
    val role: String,
    val email: String
)