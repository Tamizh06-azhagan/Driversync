package com.example.driversync_trackanddrive

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

public class UserListAdapter(
    private val itemList: ArrayList<UserListModule>,val context: Context
) : RecyclerView.Adapter<UserListAdapter.ItemViewHolder>() {

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
        holder.textView.text = item.name
        holder.imageView.setImageResource(item.imageRes)

        // Set click listener for individual items
        holder.itemView.setOnClickListener {
            context.startActivity(Intent(context,MoreDetails::class.java))
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount(): Int = itemList.size}