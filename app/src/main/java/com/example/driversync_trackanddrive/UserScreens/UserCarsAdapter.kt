package com.example.driversync_trackanddrive.UserScreens

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.RetrofitClient.BASE_URL
import com.example.driversync_trackanddrive.api.RetrofitClient.BASE_URL_IMAGE
import com.example.driversync_trackanddrive.model.Car

class UserCarsAdapter(private val list:List<Car>, private val context: Context) : RecyclerView.Adapter<UserCarsAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cars_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]

        // Bind data to the views

        val imageUrl = BASE_URL_IMAGE + item.image_path

        holder.textView.text = item.car_name

        Glide.with(context)
            .load(imageUrl)
            .placeholder(R.drawable.carimage1)
            .error(R.drawable.carimage1)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context,FetchingPage::class.java)
            intent.putExtra("car_id",item.id)
            context.startActivity(intent)
        }

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView124)
        val imageView: ImageView = itemView.findViewById(R.id.imageViewcar)

    }

    override fun getItemCount(): Int {
       return list.size
    }
}