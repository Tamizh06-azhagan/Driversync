package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DriverPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewdriver)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        findViewById<Button>(R.id.viewProfileButton2).setOnClickListener {
            val intent = Intent(this, Profilepage::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.viewallbutton2).setOnClickListener(){
            val intent = Intent(this, ViewallPage::class.java)
            startActivity(intent)
        }

        val itemList = listOf("User name 1", "User name 2", "User name 3", "User name 4",)
        val adapter = DriverJobsAdapter(itemList)
        recyclerView.adapter = adapter
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        // Data for spinners
        val locations = listOf(
            "Choose City",
            "Chennai",
            "Kanchipuram",
            "Vellore",
            "Ranipet",
            "Chengalpet",
            "Villupuram",
            "Thiruvannamalai",
            "Bengaluru",
            "Tirupati",
            "Thiruvallur",
            "Pondicherry"
        )
        val days = (1..10).map { it.toString() }

        // Adapters for spinners
        val locationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        val daysAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        // Set adapters
        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()

        }
    }

}


class MainActivity : AppCompatActivity() {

    private val userId = 1 // Replace with dynamic user ID if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Format the selected date
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)

            // Show confirmation popup
            showConfirmationDialog(selectedDate)
        }
    }

    private fun showConfirmationDialog(selectedDate: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Availability")
        builder.setMessage("Are you available on $selectedDate?")
        builder.setPositiveButton("Yes") { _, _ ->
            updateAvailability(selectedDate, "Yes")
        }
        builder.setNegativeButton("No", null)
        builder.show()
    }

    private fun updateAvailability(date: String, availability: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.updateAvailability(userId, availability, date)

        call.enqueue(object : Callback<ApiResponse> {
            override fun onResponse(call: Call<ApiResponse>, response: Response<ApiResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        Toast.makeText(
                            this@MainActivity,
                            apiResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to update availability.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity,
                        "Response not successful: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<ApiResponse>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
