package com.example.driversync_trackanddrive.UserScreens

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
import com.example.driversync_trackanddrive.response.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profilepage : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var logoutButton: Button
    private lateinit var backButton: ImageButton

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)

        // Initialize UI components
        tvName = findViewById(R.id.usern)
        tvUsername = findViewById(R.id.textView9)
        logoutButton = findViewById(R.id.logoutbutton)
        backButton = findViewById(R.id.backButton3)

        // Fetch and display user profile

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val userId = sharedPreferences.getString("id",null)

        if (userId != null) {
            fetchUserProfile(userId)
        }

        // Handle logout button click
        logoutButton.setOnClickListener {
            val intent = Intent(this, CommonLoginActivity::class.java)
            startActivity(intent)
            finish() // Close ProfilePage activity
        }

        // Handle back button click
        backButton.setOnClickListener {
            finish() // Close this activity and navigate back
        }

        // Apply window insets for proper layout adjustment
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

    private fun fetchUserProfile(userId: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getUserProfile(userId).enqueue(object : Callback<ProfileResponse> {
            override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
                if (response.isSuccessful && response.body()?.status == true) {
                    val userData = response.body()?.data
                    if (userData != null) {
                        tvName.text = userData.name
                        tvUsername.text = userData.username
                    }
                } else {
                    Toast.makeText(this@Profilepage, "User not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Toast.makeText(this@Profilepage, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
