package com.example.driversync_trackanddrive.UserScreens

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule
import com.example.driversync_trackanddrive.DriverScreens.DriverAllBookingActivity

class BooknowAdapter(
    private val itemList: ArrayList<ViewAllModule>, val context: Context
) : RecyclerView.Adapter<BooknowAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.name89)
        val imageView: ImageView = itemView.findViewById(R.id.profileui)
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

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DriverAllBookingActivity::class.java)
            intent.putExtra("item_name", item.name) // Example: Passing item data
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = itemList.size
}
