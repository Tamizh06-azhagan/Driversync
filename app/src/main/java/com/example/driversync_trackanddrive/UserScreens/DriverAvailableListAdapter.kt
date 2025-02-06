package com.example.driversync_trackanddrive.UserScreens

import BookingdateResponse
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val driverName: TextView = itemView.findViewById(R.id.driverName)
        val bookBtn: Button = itemView.findViewById(R.id.bookBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.booknow_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.driverName.text = item.driver_name

        val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)

        holder.bookBtn.setOnClickListener {
            if (userId != null) {
                showBookingDialog(userId.toInt(), item.driver_id, item.availability_date)
            } else {
                Toast.makeText(context, "User ID is missing", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressLint("MissingInflatedId")
    private fun showBookingDialog(userId: Int, driverId: Int, dateOfBooking: String) {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog, null)
        val etPickup = dialogView.findViewById<EditText>(R.id.etPickup)
        val etDrop = dialogView.findViewById<EditText>(R.id.etDrop)
        val btnBookNow = dialogView.findViewById<Button>(R.id.btnBookNow)
        val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)

        val dialog = AlertDialog.Builder(context)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        btnBookNow.setOnClickListener {
            val pickup = etPickup.text.toString().trim()
            val drop = etDrop.text.toString().trim()

            if (pickup.isEmpty() || drop.isEmpty()) {
                Toast.makeText(context, "Please enter both pickup & drop locations", Toast.LENGTH_SHORT).show()
            } else {
                dialog.dismiss()
                insertBooking(userId, driverId, dateOfBooking, "pending", pickup, drop)
            }
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun insertBooking(userId: Int, driverId: Int, dateOfBooking: String, status: String, pickup: String, drop: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.insertBookingDetails(userId, driverId, dateOfBooking, status, pickup, drop)

        call.enqueue(object : Callback<BookingdateResponse> {
            override fun onResponse(call: Call<BookingdateResponse>, response: Response<BookingdateResponse>) {
                if (response.isSuccessful) {
                    val bookingResponse = response.body()
                    if (bookingResponse != null && bookingResponse.status == "success") {
                        Toast.makeText(context, "Booking successful!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, bookingResponse?.message ?: "Failed to insert booking", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<BookingdateResponse>, t: Throwable) {
                Toast.makeText(context, "Network error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun getItemCount(): Int = itemList.size
}
