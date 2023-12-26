package com.example.pengelolakeuangan

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.pengelolakeuangan.databinding.ActivityKategoriBinding

class  activity_kategori : AppCompatActivity() {

    private lateinit var binding: ActivityKategoriBinding
    private lateinit var listViewKategori: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKategoriBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listViewKategori = binding.listViewKategori

        // Items for the ListView
        val categoryItems = arrayOf("Income Category Setting", "Expenses Category Setting")

        // Set up the ArrayAdapter
        val adapter = ArrayAdapter(this, R.layout.simple_list_item_1, categoryItems)

        val imageViewBack = binding.editBckinc

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
                    startActivity(Intent(this, ExpensesCategoryActivity::class.java))
                    // Handle Expenses Category Setting click
                    // Example: startActivity(Intent(this, ExpensesCategorySettingActivity::class.java))
                }
            }
        }

        // Set an OnClickListener for the ImageView
        imageViewBack.setOnClickListener {
            // Create an Intent to start the MoreFragment
            val intent = Intent(this@activity_kategori, MainActivity::class.java)
            intent.putExtra("fragment", "more") // You can pass any extra data if needed

            // Start the MainActivity and pass the intent to navigate to the MoreFragment
            startActivity(intent)
        }
    }
}

