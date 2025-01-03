package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UserPage : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private var itemList: ArrayList<CarAvailableModule> = arrayListOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_user_page)


//        val itemList<CarAvailableModule> = listOf("car 1", "car 2", "car 3", "car 4")
        itemList.add(CarAvailableModule(R.drawable.carimage1, "Car 1"))
        itemList.add(CarAvailableModule(R.drawable.carimage1, "Car 2"))
        itemList.add(CarAvailableModule(R.drawable.carimage1, "Car 3"))
        itemList.add(CarAvailableModule(R.drawable.carimage1, "Car 4"))

        val adapter = ItemAdapter(itemList)
        val originSpinner: Spinner = findViewById(R.id.spinnerOrigin)
        val destinationSpinner: Spinner = findViewById(R.id.spinnerDestination)
        val daysSpinner: Spinner = findViewById(R.id.spinnerDays)

        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

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

        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }
    }
}