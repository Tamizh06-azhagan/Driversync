package com.example.driversync_trackanddrive.Unused

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driversync_trackanddrive.Auth.Signup
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.UserScreens.UserPage

class UsrLogin : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_usr_login)

        // Find the TextViews by their IDs
        val signupText: TextView = findViewById(R.id.signuptext)
        val userLoginButton: TextView = findViewById(R.id.userloginbutton)

        // Set a click listener for the signup TextView
        signupText.setOnClickListener {
            // Navigate to SignupPage
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        // Set a click listener for the login button
        userLoginButton.setOnClickListener {
            // Example credentials for validation (replace these with actual inputs from EditTexts)
            val username = "admin" // Replace with input from a username EditText
            val password = "password" // Replace with input from a password EditText

            if (validateCredentials(username, password)) {
                // Navigate to UserPage if credentials are valid
                val intent = Intent(this, UserPage::class.java)
                startActivity(intent)
            } else {
                // Handle invalid credentials (e.g., show a Toast)
                Toast.makeText(this, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate user credentials
    private fun validateCredentials(username: String, password: String): Boolean {
        // Replace this logic with actual validation (e.g., database check)
        return username == "admin" && password == "password"
    }
}