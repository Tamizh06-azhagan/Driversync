package com.example.driversync_trackanddrive

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class AdmLogin : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admlogin) // Replace with your XML layout name

        // Initialize UI elements
        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: TextInputEditText = findViewById(R.id.passwrd)
        val loginButton: Button = findViewById(R.id.logbutton)

        // Handle Login Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (validateCredentials(username, password)) {
                // Navigate to Admin Dashboard (replace AdminDashboardActivity with your activity)
                val intent = Intent(this, AdminPage::class.java)
                startActivity(intent)
                finish()
            } else {
                // Show error message
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate credentials (replace with your own logic or database integration)
    private fun validateCredentials(username: String, password: String): Boolean {
        // Example validation logic
        return username == "admin" && password == "password"
    }

    // Handle Back Button Press
    override fun onBackPressed() {
        super.onBackPressed()
        // Navigate back to the Login Page (if needed)
        val intent = Intent(this, Login::class.java) // Replace with the actual class for Login Page
        startActivity(intent)
        finish()
    }
}
