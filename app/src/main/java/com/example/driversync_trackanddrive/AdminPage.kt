package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminPage : AppCompatActivity() {

    private val itemList: ArrayList<CarAvailableModule> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_page)

        findViewById<Button>(R.id.viewProfileButton).setOnClickListener {
            val intent = Intent(this,Profilepage::class.java)
            startActivity(intent)
        }

        // More Details button click
//        findViewById<ImageButton>(R.id.moredetails).setOnClickListener {
//            val intent = Intent(this, MoreDetails::class.java)
//            startActivity(intent) // Start the MoreDetails activity
//        }
        // Helper method to populate item list
        fun setupItemList() {
            itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 1"))
            itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 2"))
            itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 3"))
            itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Driver 4"))
        }

        // RecyclerView setup
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        setupItemList()
        val adapter = ItemAdapter(this,itemList)
        recyclerView.adapter = adapter

        // Spinner setup
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        val locations = listOf(
            "Choose City", "Chennai", "Kanchipuram", "Vellore",
            "Chengalpet", "Villupuram", "Thiruvannamalai",
            "Bengaluru", "Tirupati", "Pondicherry"
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
            onBackPressed() // Close the activity and return to the previous screen
        }

        // CalendarView setup
        findViewById<CalendarView>(R.id.calendarView).setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
            val intent = Intent(this, DriverStatus::class.java)
            intent.putExtra("selectedDate", selectedDate)
            startActivity(intent) // Start the DriverStatus activity with the selected date
        }
    }


}
