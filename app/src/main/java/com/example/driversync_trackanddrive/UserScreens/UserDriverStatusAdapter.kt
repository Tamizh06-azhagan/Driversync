package com.example.driversync_trackanddrive.UserScreens

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.DriverScreens.DriverAllBookingActivity
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule

class UserDriverStatusAdapter(
    private val itemList: ArrayList<ViewAllModule>, val context: Context
) : RecyclerView.Adapter<UserDriverStatusAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.name)
        val imageView: ImageView = itemView.findViewById(R.id.profile)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_all_item_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        // Bind data to the views
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.imageRes)

        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context, DriverAllBookingActivity::class.java))
        }
    }


    override fun getItemCount(): Int = itemList.size
}
