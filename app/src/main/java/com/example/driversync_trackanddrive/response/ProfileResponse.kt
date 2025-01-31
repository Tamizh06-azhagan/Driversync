package com.example.driversync_trackanddrive.response

data class ProfileResponse(
    val status: Boolean,
    val message: String,
    val data: UserData?
)

data class User(
    val name: String,
    val username: String
)