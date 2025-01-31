package com.example.driversync_trackanddrive.DriverScreens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.InsertResponse
import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverPage : AppCompatActivity() {

    lateinit var context: Context
    private var userId = -1
    private lateinit var totAmt: TextView
    private lateinit var originSpinner: Spinner
    private lateinit var destinationSpinner: Spinner
    private lateinit var daysSpinner: Spinner

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_page)

        context = this

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        if (sharedPreferences.getString("id", null)?.toInt() != null) {
            userId = sharedPreferences.getString("id", null)?.toInt()!!
        }

        totAmt = findViewById(R.id.drivertoamt)

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
        originSpinner = findViewById(R.id.spinnerOrigin)
        destinationSpinner = findViewById(R.id.spinnerDestination)
        daysSpinner = findViewById(R.id.spinnerDays)

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

        // Set up listener for any spinner change
        val spinnerListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: android.view.View?,
                position: Int,
                id: Long
            ) {
                setupPriceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        originSpinner.onItemSelectedListener = spinnerListener
        destinationSpinner.onItemSelectedListener = spinnerListener
        daysSpinner.onItemSelectedListener = spinnerListener
    }

    private fun setupNavigation() {
        findViewById<Button>(R.id.viewProfileButton2).setOnClickListener {
            startActivity(Intent(this, DriverProfile::class.java))
        }

        findViewById<Button>(R.id.viewallbutton2).setOnClickListener {
            startActivity(Intent(this, DriverAllBookingActivity::class.java))
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
            override fun onResponse(
                call: Call<InsertResponse>,
                response: Response<InsertResponse>
            ) {
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

    private fun setupPriceDetails() {
        val origin = originSpinner.selectedItem.toString()
        val destination = destinationSpinner.selectedItem.toString()
        val daysString = daysSpinner.selectedItem.toString()

        // Validate inputs
        if (origin != "Choose City" && destination != "Choose City" && daysString.isNotEmpty()) {
            val days = daysString.toIntOrNull()
            if (days != null && days > 0) {
                calculatePrice(origin, destination, days)
            } else {
                totAmt.text = "Invalid days selected"
            }
        } else {
            totAmt.text = "Please select valid options"
        }
    }

    private fun calculatePrice(origin: String, destination: String, days: Int) {
        val call = RetrofitClient.instance.create(ApiService::class.java)
            .calculatePrice(origin, destination, days)

        call.enqueue(object : Callback<PriceResponse> {
            override fun onResponse(call: Call<PriceResponse>, response: Response<PriceResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val totalAmount = response.body()?.total_amount
                    totAmt.text = "₹ $totalAmount"
                } else {
                    totAmt.text = ""
                    Toast.makeText(
                        this@DriverPage,
                        response.body()?.message ?: "Error fetching price",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                totAmt.text = ""
                Toast.makeText(
                    this@DriverPage,
                    "Failed to fetch price: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
