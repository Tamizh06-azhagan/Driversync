package com.example.driversync_trackanddrive

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class Signup : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Reference the Spinner
        val roleSpinner: Spinner = findViewById(R.id.roleSpinner)

        // Define the options programmatically
        val roles = arrayOf("User", "Driver")

        // Create an ArrayAdapter
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, // Default layout for spinner items
            roles
        )

        // Specify the layout for the dropdown view
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Attach the adapter to the spinner
        roleSpinner.adapter = adapter

        // Handle back button click
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Close the current activity and return to the previous one
        }
    }
}

