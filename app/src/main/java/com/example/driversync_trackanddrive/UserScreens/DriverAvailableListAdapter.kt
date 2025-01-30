package com.example.driversync_trackanddrive.UserScreens

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.response.Driver

public class DriverAvailableListAdapter(
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

        // Set click listener for individual items
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, MoreDetails::class.java))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = itemList.size
}
