package com.example.driversync_trackanddrive

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.InsertResponse
import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverPage : AppCompatActivity() {

    lateinit var context: Context
    private var userId = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_page)

        context = this

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewdriver)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        val itemList = listOf("User name 1", "User name 2", "User name 3", "User name 4")
        val adapter = DriverJobsAdapter(itemList)
        recyclerView.adapter = adapter

        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            showConfirmationDialog(selectedDate, userId)
        }

        setupSpinners()
        setupNavigation()
    }

    private fun setupSpinners() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        val locations = listOf(
            "Choose City",
            "Chennai",
            "Kanchipuram",
            "Vellore",
            "Chengalpet",
            "Villupuram",
            "Thiruvannamalai",
            "Bengaluru",
            "Tirupati",
            "Pondicherry"
        )

        val days = (1..10).map { it.toString() }

        val locationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        val daysAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter
    }

    private fun setupNavigation() {
        findViewById<Button>(R.id.viewProfileButton2).setOnClickListener {
            startActivity(Intent(this, Profilepage::class.java))
        }

        findViewById<Button>(R.id.viewallbutton2).setOnClickListener {
            startActivity(Intent(this, ViewallPage::class.java))
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }
    }

    private fun showConfirmationDialog(selectedDate: String, userId: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Set Availability")
        builder.setMessage("Are you available on $selectedDate?")
        builder.setPositiveButton("Yes") { _, _ ->
            updateAvailability(selectedDate, "Yes", userId)
        }
        builder.setNegativeButton("No") { _, _ ->
            updateAvailability(selectedDate, "No", userId)
        }
        builder.show()
    }

    private fun updateAvailability(date: String, availability: String, userId: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.updateAvailability(userId, availability, date)

        call.enqueue(object : Callback<InsertResponse> {
            override fun onResponse(call: Call<InsertResponse>, response: Response<InsertResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse != null && apiResponse.status) {
                        Toast.makeText(
                            this@DriverPage,
                            apiResponse.message,
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@DriverPage,
                            "Failed to update availability.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        this@DriverPage,
                        "Response not successful: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<InsertResponse>, t: Throwable) {
                Toast.makeText(
                    this@DriverPage,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }

    private fun calculatePrice(origin: String, destination: String, days: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.calculatePrice(origin, destination, days)

        call.enqueue(object : Callback<PriceResponse> {
            override fun onResponse(call: Call<PriceResponse>, response: Response<PriceResponse>) {
                if (response.isSuccessful) {
                    val priceResponse = response.body()

                    if (priceResponse != null && priceResponse.status) {
                        val pricePerDay = priceResponse.price_per_day
                        val totalAmount = priceResponse.total_amount

                        findViewById<TextView>(R.id.drivertoamt).text = "₹ $totalAmount"
                    } else {
                        findViewById<TextView>(R.id.drivertoamt).text = ""
                        Toast.makeText(
                            this@DriverPage,
                            "${priceResponse?.message ?: "Unknown error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    findViewById<TextView>(R.id.drivertoamt).text = ""
                    Toast.makeText(
                        this@DriverPage,
                        "Failed to fetch price: ${response.code()}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                findViewById<TextView>(R.id.drivertoamt).text = ""
                Toast.makeText(
                    this@DriverPage,
                    "Error: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}