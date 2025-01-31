package com.example.driversync_trackanddrive.DriverScreens

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.driversync_trackanddrive.Auth.CommonLoginActivity
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.DriverProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverProfile : AppCompatActivity() {

    private lateinit var driverNameTextView: TextView
    private lateinit var driverUsernameTextView: TextView
    private lateinit var logoutButton: Button
    private lateinit var circularImageButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile)

        // Find views
        driverNameTextView = findViewById(R.id.textViewName)
        driverUsernameTextView = findViewById(R.id.textView99)
        logoutButton = findViewById(R.id.logoutbutton1)
        circularImageButton = findViewById(R.id.addCarButton)

        // Retrieve driver_id from SharedPreferences
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val driverId = sharedPreferences.getString("id", null) // Default value is -1 if not found

        // If driver_id is valid, fetch the profile
        if (driverId != null) {
            fetchDriverProfile(driverId)
        } else {
            Toast.makeText(this, "Driver ID not found", Toast.LENGTH_SHORT).show()
        }

        // Logout button functionality
        logoutButton.setOnClickListener {
            val intent = Intent(this, CommonLoginActivity::class.java)
            startActivity(intent)
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton34).setOnClickListener {
            finish()  // Close this activity and navigate back
        }

        // Add car button functionality
        circularImageButton.setOnClickListener {
            val intent = Intent(this, InsertCars::class.java)
            startActivity(intent)
        }

        // Apply window insets for system UI
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
    }

    private fun fetchDriverProfile(driverId: String?) {
        val call = RetrofitClient.instance.create(ApiService::class.java).fetchDriverProfile(driverId)

        call.enqueue(object : Callback<DriverProfileResponse> {
            override fun onResponse(
                call: Call<DriverProfileResponse>,
                response: Response<DriverProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val driverProfile = response.body()
                    if (driverProfile != null && driverProfile.status) {
                        // Display driver details
                        driverNameTextView.text = driverProfile.driver.name
                        driverUsernameTextView.text = driverProfile.driver.username.toString()
                    } else {
                        Toast.makeText(this@DriverProfile, "Failed to retrieve driver profile", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DriverProfile, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DriverProfileResponse>, t: Throwable) {
                // Handle failure
                Toast.makeText(this@DriverProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
