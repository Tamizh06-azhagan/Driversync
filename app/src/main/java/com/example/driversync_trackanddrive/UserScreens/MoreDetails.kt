package com.example.driversync_trackanddrive.UserScreens

import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.driversync_trackanddrive.R

class MoreDetails : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_more_details)

        // Enable edge-to-edge display
        WindowInsetsControllerCompat(window, window.decorView).apply {
            isAppearanceLightNavigationBars = true
            isAppearanceLightStatusBars = true
        }

        // Apply insets to the root view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemInsets = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                view.paddingLeft,
                view.paddingTop + systemInsets.top,
                view.paddingRight,
                view.paddingBottom + systemInsets.bottom
            )
            WindowInsetsCompat.CONSUMED
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton2).setOnClickListener {
            finish() // Close the activity and return to the previous screen
        }
    }
}
