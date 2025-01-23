package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.response.LoginResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdmLogin : AppCompatActivity() {

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
                        Toast.makeText(this@AdmLogin, loginResponse.message, Toast.LENGTH_LONG).show()
                        Log.d("API_RESPONSE", "User Data: ${loginResponse.data}")

                        if(loginResponse.data?.role=="User" ){
                            val intent = Intent(this@AdmLogin, UserPage::class.java)
                            startActivity(intent)
                            finish()
                        }

                        // Navigate to Admin Dashboard
                        val intent = Intent(this@AdmLogin, AdminPage::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@AdmLogin, "Login failed: ${loginResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@AdmLogin, "Response not successful: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@AdmLogin, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
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
