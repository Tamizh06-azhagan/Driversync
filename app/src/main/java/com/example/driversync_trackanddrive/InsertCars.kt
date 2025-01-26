package com.example.driversync_trackanddrive

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

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

        // Here, you can save the details to a database or send them to a server.
        Toast.makeText(this, "Details saved successfully!", Toast.LENGTH_SHORT).show()
    }
}
