package com.example.driversync_trackanddrive.DriverScreens

import DriverBookingListModel
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.UserScreens.MoreDetails

public class DriverAllBookingAdapter(
    private var itemList: ArrayList<DriverBookingListModel>, val context: Context
) : RecyclerView.Adapter<DriverAllBookingAdapter.ItemViewHolder>() {

    // ViewHolder class to hold references to views in the layout
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.name)
        val imageView: ImageView = itemView.findViewById(R.id.profile)

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_list_layout, parent, false)
        return ItemViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to the views
        holder.textView.text = "User: ${item.username}\nBooking ID: ${item.id}\nDate: ${item.dateofbooking}\nStatus: ${item.status}"
//        holder.imageView.setImageResource(item.i)

        // Set click listener for individual items
//        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context, MoreDetails::class.java))
//        }
    }

    fun updateBookings(newBookings: List<DriverBookingListModel>) {
        itemList = newBookings as ArrayList<DriverBookingListModel>
        notifyDataSetChanged()
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = itemList.size}