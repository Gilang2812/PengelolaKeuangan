package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class activity_kategori : AppCompatActivity() {

    private lateinit var listViewKategori: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kategori)

        listViewKategori = findViewById(R.id.listView_kategori)

        // Items for the ListView
        val categoryItems = arrayOf("Income Category Setting", "Expenses Category Setting")

        // Set up the ArrayAdapter
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categoryItems)

        // Set the adapter to the ListView
        listViewKategori.adapter = adapter

        // Set item click listener
        listViewKategori.setOnItemClickListener { _, _, position, _ ->
            // Handle item click here

            // Add more specific actions based on the selected item if needed
            when (position) {
                0 -> {
                    startActivity(Intent(this, IncomeCategoryActivity::class.java))

                    // Handle Income Category Setting click
                    // Example: startActivity(Intent(this, IncomeCategorySettingActivity::class.java))
                }
                1 -> {
                    startActivity(Intent(this, ExpensescategoryActivity::class.java))
                    // Handle Expenses Category Setting click
                    // Example: startActivity(Intent(this, ExpensesCategorySettingActivity::class.java))
                }
            }
        }
    }
}