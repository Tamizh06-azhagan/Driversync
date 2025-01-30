package com.example.driversync_trackanddrive.UserScreens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingdetails.Booking
import com.example.driversync_trackanddrive.R
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class BookingStatusActivity : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var rvBookings: RecyclerView
    private lateinit var bookingsAdapter: BookingAdapter
    private val bookingsList = mutableListOf<Booking>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_booking_details)

        backButton = findViewById(R.id.backButton340)
        rvBookings = findViewById(R.id.rvBookings)

        // Set up RecyclerView
        bookingsAdapter = BookingAdapter(bookingsList)
        rvBookings.layoutManager = LinearLayoutManager(this)
        rvBookings.adapter = bookingsAdapter

        backButton.setOnClickListener {
            finish() // Go back to the previous activity
        }

        // Fetch booking details
        fetchBookingDetails()
    }

    private fun fetchBookingDetails() {
        val userId = intent.getIntExtra("USER_ID", -1) // Retrieve user ID from intent
        if (userId == -1) {
            Toast.makeText(this, "Invalid User ID", Toast.LENGTH_SHORT).show()
            finish()
        }

        val url = "http://localhost/driver_sync_api/bookingdetails.php"

        val client = OkHttpClient()
        val requestBody = FormBody.Builder()
            .add("userid", userId.toString())
            .build()

        val request = Request.Builder()
            .url(url)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@BookingStatusActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@BookingStatusActivity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                    return
                }

                try {
                    val jsonArray = JSONArray(responseBody)
                    bookingsList.clear()
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val booking = Booking(
                            jsonObject.getInt("booking_id"),
                            jsonObject.getString("drivername"),
                            jsonObject.getString("status")
                        )
                        bookingsList.add(booking)
                    }
                    runOnUiThread {
                        bookingsAdapter.notifyDataSetChanged()
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(this@BookingStatusActivity, "Error parsing data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}



// Example of navigation from another activity
fun navigateToBookingDetails(context: Context, userId: Int) {
    val intent = Intent(context, BookingStatusActivity::class.java)
    intent.putExtra("USER_ID", userId)
    context.startActivity(intent)
}
