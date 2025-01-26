package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var adapter: UserCarsAdapter
    private var itemList: ArrayList<UserCarsModule> = arrayListOf()
    private lateinit var totAmt: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_page)
        // Initialize totalAmt
        totAmt = findViewById(R.id.Total)


        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 1"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 2"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 3"))
        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 4"))

        findViewById<Button>(R.id.viewProfileButton1).setOnClickListener {
            val intent = Intent(this, Profilepage::class.java)
            startActivity(intent)
        }

        findViewById<Button>(R.id.viewallbutton11).setOnClickListener() {
            val intent = Intent(this, Booknow::class.java)
            startActivity(intent)
        }

        // val adapter = ItemAdapter(itemList)
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        adapter = UserCarsAdapter(itemList, this)
        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

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

        // Adapters for spinners
        val locationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        val daysAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        // Set adapters
        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter

        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }
        findViewById<CalendarView>(R.id.calendarView).setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val intent = Intent(this, DriverAvailable::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent) // Start the DriverStatus activity with the selected date
        }
        // Setup spinner item selection listeners
        setupSpinnerListeners()
    }

    private fun setupSpinnerListeners() {
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        originSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                setupPriceDetails() // API call when item is selected
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        destinationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                setupPriceDetails()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        daysSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
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
                if (response.isSuccessful) {
                    if (response.body()?.status == true) {
                        val priceResponse = response.body()

                        val pricePerDay = priceResponse?.price_per_day
                        val totalAmount = priceResponse?.total_amount

                        totAmt.text = "₹ " + totalAmount.toString()
                    } else {
                        totAmt.text = ""
                        Toast.makeText(
                            this@UserPage,
                            "${response.body()?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
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

