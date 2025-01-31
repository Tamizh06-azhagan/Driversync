package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.databinding.ActivityUserPageBinding
import com.example.driversync_trackanddrive.model.Car
import com.example.driversync_trackanddrive.model.CarResponse
import com.example.driversync_trackanddrive.response.PriceResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPage : AppCompatActivity() {

    lateinit var binding:ActivityUserPageBinding

    private lateinit var recyclerView: RecyclerView
    private lateinit var totAmt: TextView
    private var itemList: ArrayList<Car> = arrayListOf()
    private var carList: List<Car> = mutableListOf()
    lateinit var adapter: UserCarsAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityUserPageBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // Initialize total amount TextView
        totAmt = findViewById(R.id.Total)

//        // Populate the car list
//        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 1"))
//        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 2"))
//        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 3"))
//        itemList.add(UserCarsModule(R.drawable.carimage1, "Car 4"))

        // Set up RecyclerView with the adapter and listener
        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = UserCarsAdapter(itemList, this@UserPage)
        recyclerView.adapter = adapter

        // Set up buttons
        findViewById<Button>(R.id.viewProfileButton1).setOnClickListener {
            startActivity(Intent(this, Profilepage::class.java))
        }
        findViewById<Button>(R.id.viewallbutton11).setOnClickListener {
            startActivity(Intent(this, Booknow::class.java))
        }

        binding.viewdetailsbutton.setOnClickListener {
            val intent = Intent(this@UserPage,BookingStatusActivity::class.java)
            intent.putExtra("USER_ID",getSharedPreferences("MyPrefs", MODE_PRIVATE).getString("id",
                0.toString()
            ))
            startActivity(intent)
        }

        // Set up spinners and calendar
        setupSpinners()
        setupCalendar()
        fetchCars()
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
        val locationAdapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
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

    private fun setupCalendar() {
        val calendarView: CalendarView = findViewById(R.id.calendarView)
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val intent = Intent(this, UserDriverAvailableActivity::class.java)
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

    private fun fetchCars() {
        val call = RetrofitClient.instance.create(ApiService::class.java).getCars()

        call.enqueue(object : Callback<CarResponse> {
            override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                if (response.isSuccessful) {
                    val carResponse = response.body()
                    if (carResponse != null && carResponse.status == "success") {
                        // Handle successful response

                        itemList.clear()
                        itemList.addAll(carResponse.data)
                        adapter.notifyDataSetChanged()
//                        Toast.makeText(
//                            this@UserPage,
//                            "Fetched ${carList.size} cars.",
//                            Toast.LENGTH_SHORT
//                        ).show()
                    } else {
                        Toast.makeText(this@UserPage, "No cars found.", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    Toast.makeText(
                        this@UserPage,
                        "API error: ${response.message()}",
                        Toast.LENGTH_SHORT
                    ).show()

                    val errorBody = response.errorBody()

                    val str:String = errorBody?.string() ?: ""
                }
            }

            override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                Toast.makeText(
                    this@UserPage,
                    "Request failed: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()

                Log.d("TAG", "onFailure: "+t.message)
            }
        })
    }
}
