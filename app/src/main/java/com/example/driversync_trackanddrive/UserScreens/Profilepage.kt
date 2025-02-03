package com.example.driversync_trackanddrive.UserScreens

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
import com.example.driversync_trackanddrive.response.ProfileResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Profilepage : AppCompatActivity() {

    private lateinit var tvName: TextView
    private lateinit var tvUsername: TextView
    private lateinit var ivProfileImage: ImageView
    private lateinit var logoutButton: Button
    private lateinit var backButton: ImageButton
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profilepage)

        // Initialize UI components
        tvName = findViewById(R.id.usern)
        tvUsername = findViewById(R.id.textView9)
        ivProfileImage = findViewById(R.id.imageProfile) // Ensure this ID exists in your XML
        logoutButton = findViewById(R.id.logoutbutton)
        backButton = findViewById(R.id.backButton3)

        // Retrieve user ID from SharedPreferences
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", null)

        // Fetch and display user profile if ID is available
        if (userId != null) {
            fetchUserProfile(userId)
        } else {
            Toast.makeText(this, "User ID not found!", Toast.LENGTH_SHORT).show()
        }

        // Handle logout button click
        logoutButton.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear() // Clear saved user data
            editor.apply()

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

                        // Load profile image using Glide
                        val imageUrl = "http://localhost/Driver_Sync_Api/" + userData.image_path
                        Glide.with(this@Profilepage)
                            .load(imageUrl)
                            .placeholder(R.drawable.pi) // Replace with your placeholder image
                            .error(R.drawable.pi) // Error fallback
                            .into(ivProfileImage)
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
