package com.example.driversync_trackanddrive.UserScreens

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.driversync_trackanddrive.R
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class FetchingPage : AppCompatActivity() {

    private lateinit var backButton: ImageButton
    private lateinit var carImage: ImageView
    private lateinit var carName: TextView
    private lateinit var driverName: TextView
    private lateinit var carCondition: TextView
    private lateinit var contactNumber: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fetching_page)

        // Initialize views
        backButton = findViewById(R.id.backButton)
        carImage = findViewById(R.id.carImage)
        carName = findViewById(R.id.carName)
        driverName = findViewById(R.id.driverName)
        carCondition = findViewById(R.id.carCondition)
        contactNumber = findViewById(R.id.contactNumber)

        // Set up the back button
        backButton.setOnClickListener {
            finish() // Close the activity and return to the previous screen
        }

        // Get car ID passed from the UserPage
        val carId = intent.getIntExtra("car_id", -1)

        if (carId != -1) {
            fetchCarDetails(carId)
        } else {
            Toast.makeText(this, "Invalid car ID.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchCarDetails(carId: Int) {
        val url = "http://your_server_url/fetch_car_details.php?car_id=$carId" // Replace with your server URL
        val client = OkHttpClient()

        val request = Request.Builder().url(url).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@FetchingPage, "Failed to fetch car details.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseData = response.body?.string()
                    if (responseData != null) {
                        try {
                            val json = JSONObject(responseData)
                            val status = json.optBoolean("status", false)

                            if (status) {
                                val data = json.optJSONObject("data")
                                val imageUrl = data?.optString("image_path", "")
                                val name = data?.optString("name", "N/A")
                                val driver = data?.optString("driver_name", "N/A")
                                val condition = data?.optString("car_condition", "N/A")
                                val contact = data?.optString("contact_number", "N/A")

                                runOnUiThread {
                                    // Update the UI with fetched data
                                    loadImageFromUrl(imageUrl)
                                    carName.text = name
                                    driverName.text = driver
                                    carCondition.text = condition
                                    contactNumber.text = contact
                                }
                            } else {
                                val message = json.optString("message", "Something went wrong.")
                                runOnUiThread {
                                    Toast.makeText(this@FetchingPage, message, Toast.LENGTH_SHORT).show()
                                }
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            runOnUiThread {
                                Toast.makeText(this@FetchingPage, "Failed to parse data.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@FetchingPage, "Empty response from server.", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@FetchingPage, "Error: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    private fun loadImageFromUrl(imageUrl: String?) {
        if (imageUrl.isNullOrEmpty()) {
            carImage.setImageResource(R.drawable.carimage1) // Set placeholder image
            return
        }

        Executors.newSingleThreadExecutor().execute {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val inputStream = connection.inputStream
                val bitmap = BitmapFactory.decodeStream(inputStream)

                runOnUiThread {
                    carImage.setImageBitmap(bitmap)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    carImage.setImageResource(R.drawable.carimage1) // Fallback to placeholder if an error occurs
                }
            }
        }
    }
}
