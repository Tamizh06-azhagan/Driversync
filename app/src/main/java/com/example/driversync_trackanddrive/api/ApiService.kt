package com.example.driversync_trackanddrive.api

import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("driver_sync_api/price.php")
    fun calculatePrice(
        @Field("origin") origin: String,
        @Field("destination") destination: String,
        @Field("days") days: Int
    ): Call<PriceResponse>
}
