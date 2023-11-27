package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.ApiService
import com.example.pengelolakeuangan.MyItemRecyclerViewAdapter
import com.example.pengelolakeuangan.placeholder.PlaceholderContent
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DailyFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var apiService: ApiService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_daily_list, container, false)

        recyclerView = view.findViewById(R.id.list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = MyItemRecyclerViewAdapter(PlaceholderContent.ITEMS)

        // Step 4: Initialize Retrofit
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY // Atur level log sesuai kebutuhan
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("http://localhost:3000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Initialize ApiService
        apiService = retrofit.create(ApiService::class.java)

        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener { showPopupMenu() }

        return view
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), fab)
        popupMenu.menuInflater.inflate(R.menu.menu_fab, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_pemasukan -> {
                    Snackbar.make(requireView(), "pemasukan clicked", Snackbar.LENGTH_SHORT).show()
                    // Make an API call using coroutines
                    GlobalScope.launch(Dispatchers.Main) {
                        try {
                            val user = apiService.getUser("Bearer YOUR_ACCESS_TOKEN")
                            // Use the user object as needed
                        } catch (e: Exception) {
                            // Handle error
                            Snackbar.make(requireView(), "Error: ${e.message}", Snackbar.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                R.id.menu_pengeluaran -> {
                    Snackbar.make(requireView(), "Pengeluaran clicked", Snackbar.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }

        popupMenu.show()
    }
}
