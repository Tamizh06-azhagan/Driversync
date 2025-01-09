package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewallPage : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_viewall_page)

        // Initialize the RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Sample data for the adapter
        val userList = arrayListOf(
            UserListModule(R.drawable.baseline_person_24, "User 1"),
            UserListModule(R.drawable.baseline_person_24, "User 2"),
            UserListModule(R.drawable.baseline_person_24, "User 3"),
            UserListModule(R.drawable.baseline_person_24, "User 4")
        )

        // Set the adapter to the RecyclerView
        val adapter = UserListAdapter(userList, this)
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
                    recyclerView.adapter = UserListAdapter(ArrayList(filteredList), this@ViewallPage)
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
