package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule

class Booknow : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_all_booking)

        // Initialize the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for the adapter
        val userList = arrayListOf(
            ViewAllModule(R.drawable.baseline_person_24, "Driver 1"),
            ViewAllModule(R.drawable.baseline_person_24, "Driver 2"),
            ViewAllModule(R.drawable.baseline_person_24, "Driver 3"),
            ViewAllModule(R.drawable.baseline_person_24, "Driver 4")
        )

        // Set the adapter to the RecyclerView
        val adapter = BooknowAdapter(userList, this)
        recyclerView.adapter = adapter

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }

        // Optional: Handle search functionality
        val searchView: androidx.appcompat.widget.SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // Filter the list based on the query
                query?.let {
                    val filteredList = userList.filter { it.name.contains(query, true) }
                    recyclerView.adapter = BooknowAdapter(ArrayList(filteredList), this@Booknow)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Optionally, update the list dynamically as the user types
                return true
            }
        })
    }
}
