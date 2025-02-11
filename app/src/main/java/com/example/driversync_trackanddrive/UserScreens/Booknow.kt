package com.example.driversync_trackanddrive.UserScreens

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driversync_trackanddrive.R
import com.example.driversync_trackanddrive.api.ApiService
import com.example.driversync_trackanddrive.api.RetrofitClient
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
    private val handler = Handler(Looper.getMainLooper())
    private var searchRunnable: Runnable? = null

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

        // Search functionality with debounce
        val searchView: SearchView = findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                searchRunnable?.let { handler.removeCallbacks(it) } // Remove previous call
                searchRunnable = Runnable { filterList(newText) }
                handler.postDelayed(searchRunnable!!, 300) // Delay execution
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
                    } ?: showToast("No drivers available")
                } else {
                    showToast("Failed to fetch drivers")
                }
            }

            override fun onFailure(call: Call<FetchResponse>, t: Throwable) {
                showToast("Network Error: ${t.message}")
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

    private fun showToast(message: String) {
        Toast.makeText(this@Booknow, message, Toast.LENGTH_SHORT).show()
    }
}
