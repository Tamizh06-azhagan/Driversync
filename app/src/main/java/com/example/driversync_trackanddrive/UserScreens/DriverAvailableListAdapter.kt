package com.example.driversync_trackanddrive.UserScreens

import BookingdateResponse
import android.app.AlertDialog
import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.Driver
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverAvailableListAdapter(
    private val itemList: ArrayList<Driver>,
    private val context: AppCompatActivity
) : RecyclerView.Adapter<DriverAvailableListAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.driverName)
        val imageView: ImageView = itemView.findViewById(R.id.profileui)
        val bookNowButton: Button = itemView.findViewById(R.id.bookBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booknow_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to views
        holder.textView.text = item.driver_name

        // Handle "Book Now" button click
        holder.bookNowButton.setOnClickListener {
            showBookingDialog(item,context)
        }
    }

    override fun getItemCount(): Int = itemList.size

    // ✅ Updated showBookingDialog() to include API Call
    private fun showBookingDialog(driver: Driver, context: Context) {
        val activity =context

        val builder = AlertDialog.Builder(activity)
        builder.setTitle("Booking Details")

        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.dialog, null)

        val pickupEditText = view.findViewById<EditText>(R.id.etPickup)
        val dropEditText = view.findViewById<EditText>(R.id.etDrop)

        builder.setView(view)
        builder.setPositiveButton("Confirm") { _, _ ->
            val pickup = pickupEditText.text.toString()
            val drop = dropEditText.text.toString()

            if (pickup.isNotEmpty() && drop.isNotEmpty()) {
                insertBooking(driver, pickup, drop)
            } else {
                Toast.makeText(activity, "Please fill both fields", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }

    // ✅ API Call for Booking Insertion
    private fun insertBooking(driver: Driver, pickup: String, drop: String) {
        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)

        if (userId == null) {
            Toast.makeText(context, "User ID not found", Toast.LENGTH_SHORT).show()
            return
        }

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.insertBookingDetails(userId, driver.driver_id, driver.availability_date, "confirmed", pickup, drop)

        call.enqueue(object : Callback<BookingdateResponse> {
            override fun onResponse(call: Call<BookingdateResponse>, response: Response<BookingdateResponse>) {
                if (response.isSuccessful) {
                    val bookingResponse = response.body()
                    if (bookingResponse != null && bookingResponse.status == "success") {
                        Toast.makeText(context, "Booking Confirmed!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, bookingResponse?.message ?: "Booking failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingdateResponse>, t: Throwable) {
                Toast.makeText(context, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
