package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class DriLogin : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Ensure the correct layout file is used
        setContentView(R.layout.activity_dri_login)

        // Initialize UI elements
        val usernameEditText: EditText = findViewById(R.id.driuser)
        val passwordEditText: TextInputEditText = findViewById(R.id.dripass)
        val loginButton: Button = findViewById(R.id.driloginbutton)



        // Handle Login Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (validateCredentials(username, password)) {
                val intent = Intent(this,DriverPage::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to validate credentials
    private fun validateCredentials(username: String, password: String): Boolean {
        return username == "admin" && password == "password"
    }
}
