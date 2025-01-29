package com.example.driversync_trackanddrive.DriverScreens

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.model.Car
import com.example.driversync_trackanddrive.model.CarResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsertCars : AppCompatActivity() {

    private lateinit var imageProfileInsert: ImageView
    private lateinit var driverName: EditText
    private lateinit var carCondition: EditText
    private lateinit var contactNumber: EditText
    private lateinit var saveButton: Button
    private val REQUEST_CODE_PICK_IMAGE = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_cars)

        // Initialize views
        val backButtonInsert = findViewById<ImageButton>(R.id.backButtoninsert)
        val viewInsert = findViewById<CardView>(R.id.viewinsert)
        imageProfileInsert = findViewById(R.id.imageProfileinsert)
        driverName = findViewById(R.id.driverName)
        carCondition = findViewById(R.id.carCondition)
        contactNumber = findViewById(R.id.contactNumber)
        saveButton = findViewById(R.id.saveButton)

        // Back button action
        backButtonInsert.setOnClickListener { finish() }

        // Open gallery when the card view or image is clicked
        viewInsert.setOnClickListener { openGallery() }
        imageProfileInsert.setOnClickListener { openGallery() }

        // Save button action
        saveButton.setOnClickListener { saveDetails() }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageProfileInsert.setImageURI(imageUri)
        }
    }

    private fun saveDetails() {
        val name = driverName.text.toString().trim()
        val condition = carCondition.text.toString().trim()
        val contact = contactNumber.text.toString().trim()

        if (name.isEmpty() || condition.isEmpty() || contact.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all details and select an image.", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        val id = sharedPreferences.getString("id", "")
        // Prepare the Car object
        val car = id?.let {
            Car(
                id = null,
                userid = it,  // Replace with dynamic user ID
                car_name = name,
                image_path = imageUri.toString(),
                condition = condition
            )
        }

        // Make the API call to save the car details
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        if (car != null) {
            apiService.insertCar(car).enqueue(object : Callback<CarResponse> {
                override fun onResponse(call: Call<CarResponse>, response: Response<CarResponse>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertCars, "Car details saved successfully!", Toast.LENGTH_SHORT).show()

                        // Navigate to DriverProfile screen after successful save
                        val intent = Intent(this@InsertCars, DriverProfile::class.java)
                        startActivity(intent)
                        finish() // Close this activity to prevent returning back to this screen
                    } else {
                        Toast.makeText(this@InsertCars, "Failed to save car details. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<CarResponse>, t: Throwable) {
                    Toast.makeText(this@InsertCars, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
