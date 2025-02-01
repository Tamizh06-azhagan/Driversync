package com.example.driversync_trackanddrive.DriverScreens

import DriverBookingListModel
import DriverBookingListResponse
import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverAllBookingActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: DriverAllBookingAdapter
    private var userId: Int = -1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_all_booking)

        // Retrieve user ID from SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        userId = sharedPreferences.getString("id", null)?.toIntOrNull() ?: -1

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = DriverAllBookingAdapter(arrayListOf(), this)
        recyclerView.adapter = adapter

        // Fetch bookings from the server
        fetchBookings(userId)

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }
    }

    private fun fetchBookings(userId: Int) {
        if (userId == -1) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.instance.create(ApiService::class.java)
            .fetchBookingDetails(userId.toString())
            .enqueue(object : Callback<DriverBookingListResponse> {
                override fun onResponse(
                    call: Call<DriverBookingListResponse>,
                    response: Response<DriverBookingListResponse>
                ) {
                    if (response.isSuccessful && response.body()?.status == true) {
                        val bookings = response.body()?.data ?: emptyList()
                        var list: ArrayList<DriverBookingListModel> = arrayListOf()

                        bookings.forEach { booking ->
                            if (booking.status.equals("pending")){
                                list.add(booking)
                            }
                        }
                        adapter.updateBookings(list)
                    } else {
                        Toast.makeText(this@DriverAllBookingActivity, "No bookings found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DriverBookingListResponse>, t: Throwable) {
                    Log.e("DriverPage", "Error: ${t.message}")
                    Toast.makeText(this@DriverAllBookingActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
