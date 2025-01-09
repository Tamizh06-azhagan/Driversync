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