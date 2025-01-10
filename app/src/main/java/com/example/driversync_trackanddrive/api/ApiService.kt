package com.example.driversync_trackanddrive.api

import com.example.driversync_trackanddrive.response.PriceResponse
import com.example.driversync_trackanddrive.response.SignupRequest
import com.example.driversync_trackanddrive.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiService {

    @FormUrlEncoded
    @POST("driver_sync_api/price.php")
    fun calculatePrice(
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("days") days: Int
    ): Call<PriceResponse>

    @Multipart
    @POST("driver_sync_api/signup.php")
    fun signupUser(
        @Part("name") name: RequestBody,
        @Part("username") username: RequestBody,
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("role") role: RequestBody,
        @Part("contact_number") contactNumber: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<SignupResponse>

}


