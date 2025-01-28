package com.example.driversync_trackanddrive

import FetchingPage
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var totAmt: TextView
    private var itemList: ArrayList<UserCarsModule> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_page)

        // Initialize total amount TextView
        totAmt = findViewById(R.id.Total)

        // Populate the car list
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 1"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 2"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 3"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 4"))

        // Set up RecyclerView with the adapter and listener
        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = UserCarsAdapter(itemList, this@UserPage)
        recyclerView.adapter = adapter

        // Set up buttons
        findViewById<Button>(R.id.viewProfileButton1).setOnClickListener {
            startActivity(Intent(this, Profilepage::class.java))
        }
        findViewById<Button>(R.id.viewallbutton11).setOnClickListener {
            startActivity(Intent(this, Booknow::class.java))
        }

        // Set up spinners and calendar
        setupSpinners()
        setupCalendar()
    }

    private fun setupSpinners() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        // Data for spinners
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

        // Set up adapters
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        val daysAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter

        // Set listeners for spinners
        originSpinner.onItemSelectedListener = createSpinnerListener()
        destinationSpinner.onItemSelectedListener = createSpinnerListener()
        daysSpinner.onItemSelectedListener = createSpinnerListener()
    }

    private fun createSpinnerListener(): AdapterView.OnItemSelectedListener {
        return object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                setupPriceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupCalendar() {
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val intent = Intent(this, DriverAvailable::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }

        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }
    }

    private fun setupPriceDetails() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

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
                        this@UserPage,
                        response.body()?.message ?: "Error fetching price",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                totAmt.text = ""
                Toast.makeText(
                    this@UserPage,
                    "Failed to fetch price: ${t.localizedMessage}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}
