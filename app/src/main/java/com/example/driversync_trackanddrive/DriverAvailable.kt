package com.example.driversync_trackanddrive

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class DriverAvailable : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_page) // Replace with your actual layout file

        // Initialize the button
        val dateButton: Button = findViewById(R.id.datebutton) // Replace with the ID of your date button

        // Get the current date
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
        dateButton.text = dateFormat.format(calendar.time)

        // Set up a click listener for the button
        dateButton.setOnClickListener {
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            // Show the DatePickerDialog
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    // Update the button text with the selected date
                    calendar.set(selectedYear, selectedMonth, selectedDay)
                    dateButton.text = dateFormat.format(calendar.time)
                },
                year, month, day
            )
            datePickerDialog.show()
        }
    }
}
