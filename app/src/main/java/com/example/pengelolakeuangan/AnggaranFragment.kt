package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.AnggaranAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch


class AnggaranFragment : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_anggaran, container, false)

        recyclerView = view.findViewById(R.id.list)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        lifecycleScope.launch {
            try {
                if (!token.isNullOrBlank()) {
                    // Call getTransaksi with Authorization header
                    val response = MoneyService.getAnggaran("Bearer $token")
                    val adapter = AnggaranAdapter(response)
                    adapter.setData(response)
                    recyclerView.adapter = adapter
                    Log.d("Anggaran Fragment", response.toString())
                } else {
                    Snackbar.make(requireView(), "Token not available", Snackbar.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("AnggaranFragment", "Error fetching anggaran: ${e.message}", e)
                Snackbar.make(requireView(), "Error di anggaran fragment: ${e.message}", Snackbar.LENGTH_SHORT).show()
            }
        }

        return  view

    }


}