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

        findViewById<ImageButton>(R.id.backButton2).setOnClickListener {
            finish() // Close the activity and return to the previous screen
        }
    }
}
