package com.example.driversync_trackanddrive.UserScreens

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule
import com.example.driversync_trackanddrive.DriverScreens.DriverAllBookingActivity

class BooknowAdapter(
    private val itemList: ArrayList<ViewAllModule>, val context: Context
) : RecyclerView.Adapter<BooknowAdapter.ItemViewHolder>() {

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

        // Bind data to the views
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.imageRes)

        // Handle "Book Now" button click
        holder.bookNowButton.setOnClickListener {
            showBookingDialog(item) //  Call the function here
        }
    }

    override fun getItemCount(): Int = itemList.size

    //  Function to show the booking pop-up dialog
    private fun showBookingDialog(item: ViewAllModule) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Booking Details")

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog, null)

        val pickupEditText = view.findViewById<EditText>(R.id.etPickup)
        val dropEditText = view.findViewById<EditText>(R.id.etDrop)

        builder.setView(view)
        builder.setPositiveButton("Confirm") { _, _ ->
            val pickup = pickupEditText.text.toString().trim()
            val drop = dropEditText.text.toString().trim()

            if (pickup.isNotEmpty() && drop.isNotEmpty()) {
                Toast.makeText(
                    context,
                    "Booking Confirmed\nPickup: $pickup\nDrop: $drop",
                    Toast.LENGTH_SHORT
                ).show()

                // Pass data to DriverAllBookingActivity
                val intent = Intent(context, DriverAllBookingActivity::class.java)
                intent.putExtra("pickup_location", pickup)
                intent.putExtra("drop_location", drop)
                context.startActivity(intent)
            } else {
                Toast.makeText(context, "Please fill both fields", Toast.LENGTH_SHORT).show()
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
