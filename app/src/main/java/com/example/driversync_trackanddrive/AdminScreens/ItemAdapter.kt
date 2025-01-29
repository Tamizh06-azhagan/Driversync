package com.example.driversync_trackanddrive.AdminScreens

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
import com.example.driversync_trackanddrive.UserScreens.CarAvailableModule
import com.example.driversync_trackanddrive.UserScreens.MoreDetails

class ItemAdapter(val context: Context, private val itemList: ArrayList<CarAvailableModule>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val moredetails: Button = itemView.findViewById(R.id.moredetails)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_driver_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.textView.text =item.name
        holder.imageView.setImageResource(item.imageRes)

        holder.moredetails.setOnClickListener {
            val intent = Intent(context, MoreDetails::class.java)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}
