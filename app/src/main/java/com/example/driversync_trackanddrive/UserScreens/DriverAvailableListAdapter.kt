package com.example.driversync_trackanddrive.UserScreens

import BookingdateResponse
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.response.Driver
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverAvailableListAdapter(
    private val itemList: ArrayList<Driver>, val context: Context
) : RecyclerView.Adapter<DriverAvailableListAdapter.ItemViewHolder>() {

    // ViewHolder class to hold references to views in the layout
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driverName: TextView = itemView.findViewById(R.id.driverName)
        val bookBtn: Button = itemView.findViewById(R.id.bookBtn)
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booknow_layout, parent, false)
        return ItemViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to the views
        holder.driverName.text = item.driver_name

        val sharedPreferences = context.getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val id = sharedPreferences.getString("id", null)

        holder.bookBtn.setOnClickListener {
            if (id != null) {
                insertbook(id, item.driver_id, item.availability_date, "pending")
            } else {
                Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
            }
        }

        // Set click listener for individual items
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, MoreDetails::class.java))
        }
    }

    // Insert booking details via Retrofit API
    private fun insertbook(id: String, driverId: Int, availabilityDate: String, status: String) {
        val call = RetrofitClient.instance.create(ApiService::class.java).insertBooking(id, driverId, availabilityDate, status)

        call.enqueue(object : Callback<BookingdateResponse> {
            override fun onResponse(call: Call<BookingdateResponse>, response: Response<BookingdateResponse>) {
                if (response.isSuccessful) {
                    val bookingResponse = response.body()
                    if (bookingResponse != null && bookingResponse.status == "success") {
                        // Booking inserted successfully, handle accordingly
                        Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Handle failure response
                        Toast.makeText(context, bookingResponse?.message ?: "Failed to insert booking", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Handle response failure (non-2xx responses)
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingdateResponse>, t: Throwable) {
                // Handle failure, e.g., no network or server issues
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = itemList.size
}
