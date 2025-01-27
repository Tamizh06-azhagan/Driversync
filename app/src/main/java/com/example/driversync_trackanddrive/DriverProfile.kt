package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DriverProfile : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile)

        // Find the logout button by its ID
        val logoutButton: Button = findViewById(R.id.logoutbutton1)

        // Set an OnClickListener for the button
        logoutButton.setOnClickListener {
            // Create an Intent to navigate to AdmLogin activity
            val intent = Intent(this, AdmLogin::class.java)
            startActivity(intent) // Start the AdmLogin activity
        }

        // Apply window insets for proper layout adjustment with system UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBarsInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + systemBarsInsets.top,
                view.paddingRight,
                view.paddingBottom + systemBarsInsets.bottom
            )
            insets
        }

        // Set up back button to close this activity and return to the previous screen
        findViewById<ImageButton>(R.id.backButton34).setOnClickListener {
            finish() // Close this activity and navigate back
        }

        // Find the FloatingActionButton and set up navigation
        val floatingActionButton: FloatingActionButton = findViewById(R.id.floatingActionButton)
        floatingActionButton.setOnClickListener {
            // Create an Intent to navigate to the AddDriverActivity
            val intent = Intent(this, InsertCars::class.java)
            startActivity(intent) // Start the AddDriverActivity
        }
    }
}