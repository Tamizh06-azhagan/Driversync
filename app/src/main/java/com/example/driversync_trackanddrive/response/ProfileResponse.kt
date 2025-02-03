package com.example.driversync_trackanddrive.response

data class ProfileResponse(
    val status: Boolean,
    val message: String,
    val data: UserProfile
)

data class UserProfile(
    val name: String,
    val username: String,
    val image_path: String
)
