package com.example.driversync_trackanddrive.api

import okhttp3.OkHttpClient


import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    const val BASE_URL = " https://c929-14-139-187-225.ngrok-free.app/"
    const val BASE_URL_IMAGE = " https://c929-14-139-187-225.ngrok-free.app/driver_sync_api/"

    private val httpClient = OkHttpClient.Builder()
        .apply {
            // Set up logging interceptor for debugging
            val loggingInterceptor = HttpLoggingInterceptor()
            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            addInterceptor(loggingInterceptor)

            connectTimeout(60, TimeUnit.SECONDS) // Connection timeout
            readTimeout(60, TimeUnit.SECONDS)    // Read timeout
            writeTimeout(60, TimeUnit.SECONDS)   // Write timeout
        }
        .build()

    val instance: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)  // Set custom OkHttpClient
            .build()


    }

}



