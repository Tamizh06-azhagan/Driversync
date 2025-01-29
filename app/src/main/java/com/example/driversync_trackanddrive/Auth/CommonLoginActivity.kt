package com.example.driversync_trackanddrive.Auth

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driversync_trackanddrive.DriverScreens.DriverPage
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.UserScreens.UserPage
import com.google.android.material.textfield.TextInputEditText
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CommonLoginActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admlogin) // Replace with your XML layout name

        // Initialize UI elements
        val usernameEditText: EditText = findViewById(R.id.username)
        val passwordEditText: TextInputEditText = findViewById(R.id.passwrd)
        val loginButton: Button = findViewById(R.id.logbutton)
        val signupButton: Button = findViewById(R.id.sign)

        // Navigate to Signup Page
        signupButton.setOnClickListener {
            val intent = Intent(this, Signup::class.java)
            startActivity(intent)
        }

        // Handle Login Button Click
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(username, password)
            }
        }
    }

    // Function to login user using Retrofit
    private fun loginUser(username: String, password: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.loginUser(username, password)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null && loginResponse.status == "success") {
                        Toast.makeText(this@CommonLoginActivity, loginResponse.message, Toast.LENGTH_LONG).show()
                        Log.d("API_RESPONSE", "User Data: ${loginResponse.data}")

                        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("id",""+loginResponse.data?.id)
                        editor.apply()


                        if(loginResponse.data?.role=="User" ){
                            val intent = Intent(this@CommonLoginActivity, UserPage::class.java)
                            startActivity(intent)
                            finish()
                        }
                        if(loginResponse.data?.role=="Driver"){
                            val intent = Intent(this@CommonLoginActivity, DriverPage::class.java)
                            startActivity(intent)
                            finish()
                        }



                    }
                    else {
                        Toast.makeText(this@CommonLoginActivity, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@CommonLoginActivity, "Response not successful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@CommonLoginActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message.orEmpty())
            }
        })
    }

    // Handle Back Button Press
    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, Login::class.java) // Replace with the actual class for Login Page
        startActivity(intent)
        finish()
    }
}