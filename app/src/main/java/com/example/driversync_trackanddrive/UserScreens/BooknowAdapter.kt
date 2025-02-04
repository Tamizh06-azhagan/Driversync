package com.example.driversync_trackanddrive.UserScreens

import android.app.Dialog
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
            showBookingDialog(item)
        }
    }

    override fun getItemCount(): Int = itemList.size

    // Function to show the booking pop-up
    private fun showBookingDialog(item: ViewAllModule) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.dialog) // Ensure XML file is correct

        // Get EditText inputs
        val etPickup: EditText = dialog.findViewById(R.id.etPickup)
        val etDrop: EditText = dialog.findViewById(R.id.etDrop)
        val btnConfirm: Button = dialog.findViewById(R.id.btnConfirm)
        val btnCancel: Button = dialog.findViewById(R.id.btnCancel)

        // Handle Confirm Booking
        btnConfirm.setOnClickListener {
            val pickupLocation = etPickup.text.toString().trim()
            val dropLocation = etDrop.text.toString().trim()

            if (pickupLocation.isEmpty() || dropLocation.isEmpty()) {
                Toast.makeText(context, "Please enter both locations", Toast.LENGTH_SHORT).show()
            } else {
                // Pass data to DriverAllBookingActivity
                val intent = Intent(context, DriverAllBookingActivity::class.java)
                intent.putExtra("pickup", pickupLocation)
                intent.putExtra("drop", dropLocation)
                context.startActivity(intent)

                dialog.dismiss() // Close the dialog
            }
        }

        // Handle Cancel
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
