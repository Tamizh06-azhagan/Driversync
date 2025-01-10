package com.example.driversync_trackanddrive.response

data class SignupResponse(
    val status: Boolean,
    val message: String
)
data class SignupRequest(
    val name: String,
    val username: String,
    val email: String,
    val password: String,
    val role: String
)
