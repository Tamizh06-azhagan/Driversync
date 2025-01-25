package com.example.driversync_trackanddrive

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.response.DriverInfoResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Adddriverdetails : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adddriverdetails) // Replace with your XML layout



        val ageEditText: EditText = findViewById(R.id.textView16)
        val experienceEditText: EditText = findViewById(R.id.textView17)
        val contactNumberEditText: EditText = findViewById(R.id.textView67)
        val submitButton: Button = findViewById(R.id.submitbutton)

        submitButton.setOnClickListener {
            val userId =""+intent.getStringExtra("id")
            val age = ageEditText.text.toString().trim().toIntOrNull()
            val experienceYears = experienceEditText.text.toString().trim().toIntOrNull()
            val contactNumber = contactNumberEditText.text.toString().trim()

            if (userId.isEmpty() || age == null || experienceYears == null || contactNumber.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Call the API
            addDriverInfo(userId, age, experienceYears, contactNumber)
        }
    }

    private fun addDriverInfo(userId: String, age: Int, experienceYears: Int, contactNumber: String) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.addDriverInfo(userId, age, experienceYears, contactNumber)

        call.enqueue(object : Callback<DriverInfoResponse> {
            override fun onResponse(call: Call<DriverInfoResponse>, response: Response<DriverInfoResponse>) {
                if (response.isSuccessful) {
                    val driverInfoResponse = response.body()
                    if (driverInfoResponse != null && driverInfoResponse.status) {
                        Toast.makeText(this@Adddriverdetails, driverInfoResponse.message, Toast.LENGTH_LONG).show()
                        Log.d("API_RESPONSE", "UserID: ${driverInfoResponse.userid}")
                        val intent = Intent(this@Adddriverdetails, DriverPage::class.java)
                        startActivity(intent)
                        finish()  //
                    } else {
                        Toast.makeText(this@Adddriverdetails, "Failed: ${driverInfoResponse?.message}", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Adddriverdetails, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DriverInfoResponse>, t: Throwable) {
                Toast.makeText(this@Adddriverdetails, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message.orEmpty())
            }
        })
    }
}
