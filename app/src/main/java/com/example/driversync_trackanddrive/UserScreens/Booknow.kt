package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
import com.example.driversync_trackanddrive.response.Driver
import androidx.appcompat.widget.SearchView
import com.example.driversync_trackanddrive.response.Fetch
import com.example.driversync_trackanddrive.response.FetchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Booknow : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BooknowAdapter
    private val userList = ArrayList<Fetch>()
    private val filteredList = ArrayList<Fetch>()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_all_booking)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = BooknowAdapter(filteredList, this)
        recyclerView.adapter = adapter

        fetchDrivers()

        // Back button functionality
        findViewById<ImageButton>(R.id.backButton1).setOnClickListener {
            finish()
        }

        // Search functionality
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }
        })
    }

    private fun fetchDrivers() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getAvailableDrivers().enqueue(object : Callback<FetchResponse> {
            override fun onResponse(call: Call<FetchResponse>, response: Response<FetchResponse>) {
                if (response.isSuccessful && response.body()?.status == "success") {
                    userList.clear()
                    response.body()?.data?.let {
                        userList.addAll(it)
                        filteredList.addAll(it)
                        adapter.notifyDataSetChanged()
                    }
                } else {
                    Toast.makeText(this@Booknow, "Failed to fetch drivers", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FetchResponse>, t: Throwable) {
                Toast.makeText(this@Booknow, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun filterList(query: String?) {
        filteredList.clear()
        if (query.isNullOrEmpty()) {
            filteredList.addAll(userList)
        } else {
            val filtered = userList.filter { it.name.contains(query, ignoreCase = true) }
            filteredList.addAll(filtered)
        }
        adapter.notifyDataSetChanged()
    }
}
