package com.example.driversync_trackanddrive.DriverScreens

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.driversync_trackanddrive.Auth.CommonLoginActivity
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.api.RetrofitClient.BASE_URL_IMAGE
import com.example.driversync_trackanddrive.response.DriverProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DriverProfile : AppCompatActivity() {

    private lateinit var tvDriverName: TextView
    private lateinit var tvDriverUsername: TextView
    private lateinit var ivDriverProfile: ImageView
    private lateinit var btnLogout: Button
    private lateinit var btnAddCar: ImageButton
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_profile)

        // Initialize UI components
        tvDriverName = findViewById(R.id.textViewName)
        tvDriverUsername = findViewById(R.id.textView99)
        ivDriverProfile = findViewById(R.id.imageProfile) // Ensure you have this ImageView in XML
        btnLogout = findViewById(R.id.logoutbutton1)
        btnAddCar = findViewById(R.id.addCarButton)

        // Retrieve SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val driverId = sharedPreferences.getString("id", null)

        if (!driverId.isNullOrEmpty()) {
            fetchDriverProfile(driverId)
        } else {
            Toast.makeText(this, "Driver ID not found", Toast.LENGTH_SHORT).show()
        }

        // Logout button functionality
        btnLogout.setOnClickListener {
            logout()
        }

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton34).setOnClickListener {
            finish()
        }

        // Add car button functionality
        btnAddCar.setOnClickListener {
            startActivity(Intent(this, InsertCars::class.java))
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

    private fun fetchDriverProfile(driverId: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.fetchDriverProfile(driverId)

        call.enqueue(object : Callback<DriverProfileResponse> {
            override fun onResponse(
                call: Call<DriverProfileResponse>,
                response: Response<DriverProfileResponse>
            ) {
                if (response.isSuccessful) {
                    val driverProfile = response.body()
                    if (driverProfile != null && driverProfile.status) {
                        tvDriverName.text = driverProfile.driver.name
                        tvDriverUsername.text = driverProfile.driver.username

                        // Load profile image using Glide
                        val imageUrl = BASE_URL_IMAGE + driverProfile.driver.image_path
                        Glide.with(this@DriverProfile)
                            .load(imageUrl)
                            .placeholder(R.drawable.pi) // Use a default profile image
                            .error(R.drawable.drop_down_ic) // Use the same default if an error occurs
                            .into(ivDriverProfile)
                    } else {
                        Toast.makeText(this@DriverProfile, "Failed to retrieve driver profile", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@DriverProfile, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DriverProfileResponse>, t: Throwable) {
                Toast.makeText(this@DriverProfile, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun logout() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()

        val intent = Intent(this, CommonLoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
