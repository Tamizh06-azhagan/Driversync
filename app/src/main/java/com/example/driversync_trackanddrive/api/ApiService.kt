package com.example.driversync_trackanddrive.api

import com.example.driversync_trackanddrive.model.Car
import com.example.driversync_trackanddrive.model.CarResponse
import com.example.driversync_trackanddrive.model.TouchAvailabilityResponse
import com.example.driversync_trackanddrive.response.DriverInfoResponse
import com.example.driversync_trackanddrive.response.GetAvailableDriversResponse
import com.example.driversync_trackanddrive.response.InsertResponse
import com.example.driversync_trackanddrive.response.LoginResponse
import com.example.driversync_trackanddrive.response.PriceResponse
import com.example.driversync_trackanddrive.response.SignupResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface
ApiService {

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

    @FormUrlEncoded
    @POST("driver_sync_api/insertavailability.php")
    fun updateAvailability(
        @Field("userid") userId: Int,
        @Field("availability") availability: String,
        @Field(
            "availability" +
                    "_date"
        ) availabilityDate: String
    ): Call<InsertResponse>

    @FormUrlEncoded
    @POST("driver_sync_api/userlogin.php")
    fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("driver_sync_api/driverinfo.php") // Replace with your endpoint
    fun addDriverInfo(
        @Field("userid") userId: String,
        @Field("age") age: Int,
        @Field("experience_years") experienceYears: Int,
        @Field("contact_number") contactNumber: String
    ): Call<DriverInfoResponse>

    @FormUrlEncoded
    @POST("driver_sync_api/touchavailability.php")
    fun getAvailableDrivers(@Field("availability_date") availabilityDate: String): Call<GetAvailableDriversResponse>

    @GET("driver_sync_api/fetchcars.php")
    fun getCars(): Call<CarResponse>

    @Multipart
    @POST("driver_sync_api/cars.php")
    fun uploadCar(
        @Part("userid") userId: RequestBody,
        @Part("car_name") carName: RequestBody,
        @Part("condition") condition: RequestBody,
        @Part image: MultipartBody.Part
    ): Call<InsertResponse>
}




