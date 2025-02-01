package com.example.driversync_trackanddrive.UserScreens

import DetailsResponse
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.network.BookingData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BookingStatusActivity : AppCompatActivity() {

    // UI Components
    private lateinit var backButton: ImageButton
    private lateinit var rvBookings: RecyclerView

    // Adapter and Data
    private lateinit var bookingsAdapter: BookingAdapter
    private val bookingsList = mutableListOf<BookingData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking_details)

        // Initialize UI Components
        backButton = findViewById(R.id.backButton340)
        rvBookings = findViewById(R.id.rvBookings)

        // Set up RecyclerView
        bookingsAdapter = BookingAdapter(bookingsList)
        rvBookings.layoutManager = LinearLayoutManager(this)
        rvBookings.adapter = bookingsAdapter

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close the activity
        }

        // Fetch booking details
        fetchBookingDetails()
    }

    private fun fetchBookingDetails() {
        val userId = intent.getStringExtra("USER_ID") ?: return

        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getBookingDetails(userId).enqueue(object : Callback<DetailsResponse> {
            override fun onResponse(
                call: Call<DetailsResponse>,
                response: Response<DetailsResponse>
            ) {
                if (response.isSuccessful && response.body()?.status == true) {
                    bookingsList.clear()
                    response.body()?.bookings?.forEach { booking ->
                        bookingsList.add(BookingData(booking.booking_id, booking.drivername, booking.status))
                    }
                    bookingsAdapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@BookingStatusActivity, "No bookings found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DetailsResponse>, t: Throwable) {
                Toast.makeText(this@BookingStatusActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message.orEmpty())
            }
        })
    }
}
