package com.example.driversync_trackanddrive

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminPage : AppCompatActivity() {


    private var itemList: ArrayList<CarAvailableModule> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_page)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Car 1"))
        itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Car 2"))
        itemList.add(CarAvailableModule(R.drawable.baseline_person_24, "Car 3"))
        val adapter = ItemAdapter(itemList)
        recyclerView.adapter = adapter
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        // Data for spinners
        val locations = listOf( "Choose City",
            "Chennai", "Kanchipuram", "Vellore", "Ranipet",
            "Chengalpet", "Villupuram", "Thiruvannamalai", "Bengaluru", "Tirupati","Thiruvallur","Pondicherry"
        )
        val days = (1..10).map { it.toString() }

        // Adapters for spinners
        val locationAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, locations)
        val daysAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, days)

        // Set adapters
        originSpinner.adapter = locationAdapter
        destinationSpinner.adapter = locationAdapter
        daysSpinner.adapter = daysAdapter

        findViewById<ImageButton>(R.id.backButton).setOnClickListener{
            finish()
        }

    }
}