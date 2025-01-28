package com.example.driversync_trackanddrive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class BookingDetailsActivity : AppCompatActivity() {

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
            return
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
                    Toast.makeText(this@BookingDetailsActivity, "Failed to load data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                val responseBody = response.body?.string()
                if (!response.isSuccessful || responseBody.isNullOrEmpty()) {
                    runOnUiThread {
                        Toast.makeText(this@BookingDetailsActivity, "No data found", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(this@BookingDetailsActivity, "Error parsing data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }
}

// Define Booking Data Class
data class Booking(val id: Int, val driverName: String, val status: String)

// BookingAdapter Class
class BookingAdapter(private val bookings: List<Booking>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount() = bookings.size

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val driverName: TextView = itemView.findViewById(R.id.tvDriverName)
        private val status: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(booking: Booking) {
            driverName.text = booking.driverName
            status.text = booking.status.replaceFirstChar { it.uppercase() }
        }
    }
}

// Example of navigation from another activity
fun navigateToBookingDetails(context: Context, userId: Int) {
    val intent = Intent(context, BookingDetailsActivity::class.java)
    intent.putExtra("USER_ID", userId)
    context.startActivity(intent)
}
