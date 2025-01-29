package com.example.driversync_trackanddrive.UserScreens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.model.Car

class UserCarsAdapter(private val list:List<Car>, private val context: Any) : RecyclerView.Adapter<UserCarsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cars_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Bind data to the views
        holder.textView.text = item.car_name
//        holder.imageView.setImageResource(item.imageRes)

        // Set click listener for individual items
//        holder.itemView.setOnClickListener {
//            context.startActivity(Intent(context,MoreDetails::class.java))
//        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView124)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewcar)

    }

    override fun getItemCount(): Int {
       return list.size
    }
}