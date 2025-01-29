package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.ViewAllModule

class UserDriverStatus : AppCompatActivity() {

    private lateinit var adapter : UserDriverStatusAdapter
    private lateinit var list: ArrayList<ViewAllModule>

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_driver_status)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        list = ArrayList()
        adapter = UserDriverStatusAdapter(list,this)
        findViewById<RecyclerView>(R.id.recyclerView).layoutManager = LinearLayoutManager(this)
        findViewById<RecyclerView>(R.id.recyclerView).adapter = adapter


        findViewById<ImageButton>(R.id.backButton).setOnClickListener{
            finish()
        }



        if(getIntent().hasExtra("selectedDate")){
            findViewById<Button>(R.id.datebutton).text = intent.getStringExtra("selectedDate")
            Toast.makeText(this,intent.getStringExtra("selectedDate"),Toast.LENGTH_SHORT).show()
        }


        loadDrivers()
    }

    private fun loadDrivers() {

        list.add(ViewAllModule(R.drawable.baseline_person_24,"Driver 1"))
        list.add(ViewAllModule(R.drawable.baseline_person_24,"Driver 2"))
        list.add(ViewAllModule(R.drawable.baseline_person_24,"Driver 3"))
        list.add(ViewAllModule(R.drawable.baseline_person_24,"Driver 4"))
        list.add(ViewAllModule(R.drawable.baseline_person_24,"Driver 5"))

        adapter.notifyDataSetChanged()

    }
}