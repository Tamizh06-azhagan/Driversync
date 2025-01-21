package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.SignupResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class Signup : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var selectedFile: File

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // Setup Spinner for Role Selection
        val roleSpinner: Spinner = findViewById(R.id.roleSpinner)
        val roles = arrayOf("User", "Driver")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            roles
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        roleSpinner.adapter = adapter

        // Handle Back Button
        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish() // Close the current activity
        }

        // Handle View Button for File Picker
        findViewById<View>(R.id.view).setOnClickListener {
            openFilePicker()
        }

        // Handle Signup Button Click
        findViewById<Button>(R.id.Usersignupbutton).setOnClickListener {
            if (::selectedFile.isInitialized) {
                registerUser()
            } else {
                Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(Intent.createChooser(intent, "Select Image"), 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            data?.data?.let { uri ->
                selectedImageUri = uri
                selectedFile = getFileFromUri(uri)
                findViewById<ImageView>(R.id.imageProfile).setImageURI(uri)
//                Toast.makeText(this, "Image Selected: ${getFileName(uri)}", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun registerUser() {
        // Retrieve form inputs
        val name = findViewById<EditText>(R.id.name).text.toString()
        val username = findViewById<EditText>(R.id.usernameuserbutton).text.toString()
        val email = findViewById<EditText>(R.id.emailbutton).text.toString()
        val password = findViewById<EditText>(R.id.passwrd).text.toString()
        val contactNumber = findViewById<EditText>(R.id.cnumber).text.toString()
        val role = findViewById<Spinner>(R.id.roleSpinner).selectedItem.toString()

        // Validate inputs
        if (name.isEmpty() || username.isEmpty() || email.isEmpty() ||
            password.isEmpty() || contactNumber.isEmpty()
        ) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Prepare image file
        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), selectedFile)
        val multipartBody = MultipartBody.Part.createFormData("image", selectedFile.name, requestFile)

        // Prepare other fields
        val nameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), name)
        val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
        val roleBody = RequestBody.create("text/plain".toMediaTypeOrNull(), role)
        val contactNumberBody = RequestBody.create("text/plain".toMediaTypeOrNull(), contactNumber)

        // Create API Service
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        val call = apiService.signupUser(
            nameBody,
            usernameBody,
            emailBody,
            passwordBody,
            roleBody,
            contactNumberBody,
            multipartBody
        )

        // Enqueue API call
        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    val signupResponse = response.body()
                    if (signupResponse != null) {
                        Toast.makeText(this@Signup, signupResponse.message, Toast.LENGTH_LONG).show()
                        Log.d("API_RESPONSE", "Message: ${signupResponse.message}")
                    } else {
                        Toast.makeText(this@Signup, "Something went wrong!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@Signup, "Signup failed: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Toast.makeText(this@Signup, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("API_ERROR", t.message.orEmpty())
            }
        })
    }

    private fun getFileFromUri(uri: Uri): File {
        // Retrieve the file extension from the URI
        val extension = getFileExtension(uri) ?: "tmp" // Default to "tmp" if no extension is found

        // Create a temporary file with the correct extension
        val tempFile = File.createTempFile("upload", ".$extension", cacheDir)

        // Write the content of the selected file into the temporary file
        contentResolver.openInputStream(uri)?.use { inputStream ->
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
        }

        return tempFile
    }

    private fun getFileExtension(uri: Uri): String? {
        return contentResolver.getType(uri)?.let { mimeType ->
            android.webkit.MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
        } ?: run {
            // Attempt to extract the extension directly from the file name if MIME type is unavailable
            val fileName = getFileName(uri)
            fileName?.substringAfterLast('.', "")
        }
    }

    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                name = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
            }
        }
        return name
    }

}
