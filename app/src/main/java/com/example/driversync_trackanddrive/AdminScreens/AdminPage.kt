package com.example.driversync_trackanddrive.AdminScreens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.UserScreens.CarAvailableModule
import com.example.driversync_trackanddrive.UserScreens.UserDriverAvailableActivity
import com.example.driversync_trackanddrive.UserScreens.Profilepage
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.DriverScreens.DriverAllBookingActivity
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPage : AppCompatActivity() {

    private val itemList: ArrayList<CarAvailableModule> = arrayListOf()
    private lateinit var totalAmt: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        // View Profile Button setup
        findViewById<Button>(R.id.viewProfileButton).setOnClickListener {
            val intent = Intent(this, Profilepage::class.java)
            startActivity(intent)
        }

        totalAmt = findViewById(R.id.TextTotalAmount)

        // View All Button setup
        findViewById<Button>(R.id.viewallbutton).setOnClickListener {
            val intent = Intent(this, DriverAllBookingActivity::class.java)
            startActivity(intent)
        }

        // Populate the item list
        setupItemList()

        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val adapter = ItemAdapter(this, itemList)
        recyclerView.adapter = adapter

        // Spinner setup
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        val locations = listOf(
            "Choose City", "Chennai", "Kanchipuram", "Vellore",
            "Chengalpet", "Villupuram", "Thiruvannamalai",
            "Bangalore", "Tirupati", "Pondicherry"
        )
        val days = (1..7).map { it.toString() }

        val locationAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            locations
        )
        val daysAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            days
        )

        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter

        // Back Button setup
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            onBackPressed()
        }

        // CalendarView setup
        findViewById<CalendarView>(R.id.calendarView).setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val intent = Intent(this, UserDriverAvailableActivity::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent)
        }

        // Setup spinner item selection listeners
        setupSpinnerListeners()
    }

    private fun setupSpinnerListeners() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        originSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                setupPriceDetails() // API call when item is selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        destinationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                setupPriceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        daysSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                setupPriceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupPriceDetails() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        val origin = originSpinner.selectedItem.toString()
        val destination = destinationSpinner.selectedItem.toString()
        val daysString = daysSpinner.selectedItem.toString()

        // Validate all spinner inputs
        if (origin != "Choose City" && destination != "Choose City" && daysString.isNotEmpty()) {
            val days = daysString.toIntOrNull()
            if (days != null && days > 0) {
                calculatePrice(origin, destination, days)
            } else {
                totalAmt.text = "Invalid days selected"
            }
        } else {
            totalAmt.text = "Please select valid options"
        }
    }

    private fun calculatePrice(origin: String, destination: String, days: Int) {
        val call = RetrofitClient.instance.create(ApiService::class.java).calculatePrice(origin, destination, days)

        call.enqueue(object : Callback<PriceResponse> {
            override fun onResponse(call: Call<PriceResponse>, response: Response<PriceResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        val priceResponse = response.body()

                        val pricePerDay = priceResponse?.price_per_day
                        val totalAmount = priceResponse?.total_amount

                        totalAmt.text = "₹ "+totalAmount.toString()
                    } else{
                        totalAmt.text = ""
                        Toast.makeText(this@AdminPage, "${response.body()?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<PriceResponse>, t: Throwable) {
                totalAmt.text = ""
                Toast.makeText(this@AdminPage, "Failed to fetch price: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                Log.d("TAG", "onFailure: "+t.message)
            }
        })
    }

    // Helper method to populate the item list
    private fun setupItemList() {
        itemList.apply {
            add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 1"))
            add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 2"))
            add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 3"))
            add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 4"))
        }
    }
}
