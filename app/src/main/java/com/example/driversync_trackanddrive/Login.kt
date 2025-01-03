package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Use your XML layout file name here

        // Initialize buttons
        val gotoAdminLoginButton: Button = findViewById(R.id.goto_alogin)
        val gotoUserLoginButton: Button = findViewById(R.id.goto_ulogin)
        val gotoDriverLoginButton: Button = findViewById(R.id.goto_dlogin)

        // Set click listener for Admin Login button
        gotoAdminLoginButton.setOnClickListener {
            val intent = Intent(this, AdmLogin::class.java)
            startActivity(intent)
        }

        // Set click listener for User Login button
        gotoUserLoginButton.setOnClickListener {
            val intent = Intent(this,UsrLogin::class.java)
            startActivity(intent)
        }

        // Set click listener for Driver Login button
        gotoDriverLoginButton.setOnClickListener {
            val intent = Intent(this,DriLogin::class.java)
            startActivity(intent)
        }
    }
}
