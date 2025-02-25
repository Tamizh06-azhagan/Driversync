import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.InsertResponse
import com.google.android.gms.common.api.Response
import retrofit2.Call
import retrofit2.Callback

class DriverJobsAdapter(
    private val context: Context,
    private var bookingList: List<DriverBookingListModel>
) : RecyclerView.Adapter<DriverJobsAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.textView)
        val acceptButton: Button = itemView.findViewById(R.id.driveracceptbutton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_driverpage_layout, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val booking = bookingList[position]

        // Display username and booking details
        holder.textView.text =
            "User: ${booking.username}\nBooking ID: ${booking.id}\nDate: ${booking.dateofbooking}\nStatus: ${booking.status}"

        // Handle Accept button click
        holder.acceptButton.setOnClickListener {
            updateBookingStatus(booking.id, "Accepted")
        }

    }

    override fun getItemCount(): Int {
        return bookingList.size
    }

    //    // Function to update bookings dynamically
    fun updateBookings(newBookings: List<DriverBookingListModel>) {
        bookingList = newBookings
        notifyDataSetChanged()
    }

    //
    private fun updateBookingStatus(bookingId: Int, newStatus: String) {

        RetrofitClient.instance.create(ApiService::class.java).updateBookingStatus(bookingId,newStatus).enqueue(object :Callback<UpdateBookingStatusResponse>{
            override fun onResponse(
                call: Call<UpdateBookingStatusResponse>,
                response: retrofit2.Response<UpdateBookingStatusResponse>
            ) {
                if (response.isSuccessful){
                    Toast.makeText(context,response.body()?.message,Toast.LENGTH_SHORT).show()
                } else{
                    val errorBody = response.errorBody()?.string()
                    Toast.makeText(context,response.message(),Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<UpdateBookingStatusResponse>, t: Throwable) {
                Toast.makeText(context,t.message,Toast.LENGTH_SHORT).show()
            }

        })

    }
}
