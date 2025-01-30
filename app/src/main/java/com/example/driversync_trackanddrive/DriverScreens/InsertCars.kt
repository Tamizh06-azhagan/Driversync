package com.example.driversync_trackanddrive.DriverScreens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.InsertResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class InsertCars : AppCompatActivity() {

    private lateinit var imageProfileInsert: ImageView
    private lateinit var driverName: EditText
    private lateinit var carCondition: EditText
    private lateinit var contactNumber: EditText
    private lateinit var saveButton: Button
    private val REQUEST_CODE_PICK_IMAGE = 100
    private var imageUri: Uri? = null
    private val REQUEST_CODE_STORAGE_PERMISSION = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_insert_cars)

        // Initialize views
        val backButtonInsert = findViewById<ImageButton>(R.id.backButtoninsert)
        val viewInsert = findViewById<CardView>(R.id.viewinsert)
        imageProfileInsert = findViewById(R.id.imageProfileinsert)
        driverName = findViewById(R.id.carName)
        carCondition = findViewById(R.id.carCondition)
        contactNumber = findViewById(R.id.contactNumber)
        saveButton = findViewById(R.id.saveButton)

        // Back button action
        backButtonInsert.setOnClickListener { finish() }

        // Open gallery when the card view or image is clicked
        viewInsert.setOnClickListener { checkAndRequestPermissions() }
        imageProfileInsert.setOnClickListener { checkAndRequestPermissions() }

        // Save button action
        saveButton.setOnClickListener { saveDetails() }
    }

    // Check and request permissions
    private fun checkAndRequestPermissions() {
        // Check if permission is granted for devices with SDK 23 or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val readPermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            val writePermission = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )

            if (readPermission != PackageManager.PERMISSION_GRANTED || writePermission != PackageManager.PERMISSION_GRANTED) {
                // Request permissions if not granted
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    REQUEST_CODE_STORAGE_PERMISSION
                )
            } else {
                // Permissions already granted, open gallery
                openGallery()
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // For Android 11 (API 30) and above, use the new permission model
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            startActivityForResult(intent, REQUEST_CODE_STORAGE_PERMISSION)
        } else {
            // For devices below SDK 23, permissions are granted at install time.
            openGallery()
        }
    }

    // Handle result of permission request
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, open gallery
                openGallery()
            } else {
                // Permission denied, show a message with more explanation
                Toast.makeText(this, "Storage permission is required to select an image.", Toast.LENGTH_LONG).show()
            }
        }
    }

    // Open gallery to select image
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE)
    }

    // Handle result of image selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imageProfileInsert.setImageURI(imageUri)
        }
    }

    // Save the car details to the server
    private fun saveDetails() {
        val name = driverName.text.toString().trim()
        val condition = carCondition.text.toString().trim()
        val contact = contactNumber.text.toString().trim()

        if (name.isEmpty() || condition.isEmpty() || contact.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill in all details and select an image.", Toast.LENGTH_SHORT).show()
            return
        }

        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val userId = sharedPreferences.getString("id", "") ?: ""

        // Use ContentResolver to get the file stream for the selected image URI
        val inputStream = contentResolver.openInputStream(imageUri!!)
        val file = File(filesDir, "uploaded_image.jpg")  // Save image to internal storage
        val outputStream = FileOutputStream(file)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Prepare request file
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        val userIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), userId)
        val carNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val conditionPart = RequestBody.create("text/plain".toMediaTypeOrNull(), condition)

        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.uploadCar(userIdPart, carNamePart, conditionPart, imagePart)
            .enqueue(object : Callback<InsertResponse> {
                override fun onResponse(
                    call: Call<InsertResponse>,
                    response: Response<InsertResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@InsertCars, "Car details saved successfully!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@InsertCars, DriverProfile::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@InsertCars, "Failed to save car details. Please try again.", Toast.LENGTH_SHORT).show()

                        val errorBody = response.errorBody()?.string()
                    }
                }

                override fun onFailure(call: Call<InsertResponse>, t: Throwable) {
                    Toast.makeText(this@InsertCars, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}

