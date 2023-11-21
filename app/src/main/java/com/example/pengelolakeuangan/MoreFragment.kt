package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment

class MoreFragment : Fragment() {

    private lateinit var listView: ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_more, container, false)
        listView = view.findViewById(R.id.listView)

        // Items for the menu
        val menuItems = arrayOf("Category", "Asset")

        // Set up the ArrayAdapter
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, menuItems)

        // Set the adapter to the ListView
        listView.adapter = adapter

        // Set item click listener
        listView.setOnItemClickListener { _, _, position, _ ->
            // Handle item click here
            when (position) {
                0 -> {
                    // Handle Menu 1 click (Category)
                    val intent = Intent(activity, activity_kategori::class.java)
                    startActivity(intent)
                }
                1 -> {
                    // Handle Menu 2 click (Asset)
                    // Implement handling for Asset click if needed
                }
            }
        }

        return view
    }
}
