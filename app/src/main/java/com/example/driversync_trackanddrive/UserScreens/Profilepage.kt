package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.driversync_trackanddrive.Auth.CommonLoginActivity
import com.example.driversync_trackanddrive.R

class Profilepage : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)
        // Find the logout button by its ID
        val logoutButton: Button = findViewById(R.id.logoutbutton)

        // Set an OnClickListener for the button
        logoutButton.setOnClickListener {
            // Create an Intent to navigate to AdmLogin activity
            val intent = Intent(this, CommonLoginActivity::class.java)
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
        findViewById<ImageButton>(R.id.backButton3).setOnClickListener {
            finish() // Close this activity and navigate back
        }
    }
}
