package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.Driver
import com.example.driversync_trackanddrive.response.GetAvailableDriversResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class UserDriverAvailableActivity : AppCompatActivity() {

    private lateinit var activity: AppCompatActivity

    private lateinit var dateButton: Button
    private var selectedDate: String? = null
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private lateinit var adapter: DriverAvailableListAdapter
    private lateinit var recyclerView : RecyclerView
    private lateinit var list:ArrayList<Driver>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_driver_availability) // Replace with your actual layout file
        recyclerView = findViewById(R.id.recyclerView)

        list = arrayListOf()

        activity = this@UserDriverAvailableActivity

        adapter = DriverAvailableListAdapter(list,activity)

        recyclerView.layoutManager = LinearLayoutManager(this@UserDriverAvailableActivity)
        recyclerView.adapter = adapter
        // Initialize the button
        dateButton = findViewById(R.id.datebutton)


        if (intent.hasExtra("selectedDate")){
            selectedDate = intent.getStringExtra("selectedDate")
            selectedDate?.let { fetchAvailableDrivers(it) }
        }

        // Get the current date
        val calendar = Calendar.getInstance()
//        selectedDate = dateFormat.format(calendar.time)
        dateButton.text = selectedDate

        // Fetch available drivers on page load
        fetchAvailableDrivers(selectedDate!!)

        // Set up a click listener for the button
        dateButton.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Update selectedDate
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    selectedDate = dateFormat.format(calendar.time)
                    dateButton.text = selectedDate

                    // Fetch available drivers for the new date
                    fetchAvailableDrivers(selectedDate!!)
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }

    private fun fetchAvailableDrivers(date: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.getAvailableDrivers(date)

        call.enqueue(object : Callback<GetAvailableDriversResponse> {
            override fun onResponse(call: Call<GetAvailableDriversResponse>, response: Response<GetAvailableDriversResponse>) {
                if (response.isSuccessful) {
                    val driverResponse = response.body()
                    if (driverResponse != null && driverResponse.status) {

                        list.addAll(driverResponse.drivers)
                        adapter.notifyDataSetChanged()

//                        Toast.makeText(this@UserDriverAvailableActivity, "Total Drivers: ${driverResponse.total_drivers}", Toast.LENGTH_SHORT).show()
                        // Update your UI with the driver list, e.g., using a RecyclerView
                    } else {
                        Toast.makeText(this@UserDriverAvailableActivity, "No drivers available.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@UserDriverAvailableActivity, "API Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GetAvailableDriversResponse>, t: Throwable) {
                Toast.makeText(this@UserDriverAvailableActivity, "Request failed: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
        findViewById<ImageButton>(R.id.backButtontamizh).setOnClickListener {
            finish()
        }
    }
}
