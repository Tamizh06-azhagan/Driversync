package com.example.driversync_trackanddrive.UserScreens

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.model.BookingResponse
import com.example.driversync_trackanddrive.network.BookingData

class BookingAdapter(private val bookings: List<BookingData>) :
    RecyclerView.Adapter<BookingAdapter.BookingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_booking, parent, false)
        return BookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val booking = bookings[position]
        holder.bind(booking)
    }

    override fun getItemCount() = bookings.size

    class BookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val driverName: TextView = itemView.findViewById(R.id.tvDriverName)
        private val status: TextView = itemView.findViewById(R.id.tvStatus)

        fun bind(booking: BookingData) {
            driverName.text = booking.drivername
            status.text = booking.status.capitalize()
        }
    }
}
