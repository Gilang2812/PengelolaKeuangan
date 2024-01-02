package com.example.pengelolakeuangan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.Aset
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Date

class AssetActivity2 : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var assetAdapter: AssetAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asset)

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.listAset)
        assetAdapter = AssetAdapter()

        // Set up RecyclerView with a LinearLayoutManager
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = assetAdapter
        val imageViewBack = findViewById<ImageView>(R.id.edit_bckinc)

        imageViewBack.setOnClickListener {
            val intent = Intent(this@AssetActivity2, MainActivity::class.java)
            startActivity(intent)
        }
        val tambahAsetButton = findViewById<FloatingActionButton>(R.id.tambahaset)

        tambahAsetButton.setOnClickListener {
            handleTambahAsetDialog()
        }

        // Assume you have a user token for authorization
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        // Fetch assets in the background using coroutines
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val assets = MoneyService.getAset("Bearer $token")

                // Update the adapter with the asset information
                assetAdapter.setAssets(assets)
            } catch (e: Exception) {
                // Handle the error (e.g., display an error message)
                // You can display an error message or handle it based on your requirements
                e.printStackTrace()
            }
        }
    }


    private fun handleTambahAsetDialog() {
        val view = layoutInflater.inflate(R.layout.activity_tambahaset, null)
        val namaAsetEdit = view.findViewById<EditText>(R.id.namaAsetEdit)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(this).toString()

        AlertDialog.Builder(this)
            .setView(view)
            .setPositiveButton("Tambah") { _, _ ->
                val newNamaAsetText = namaAsetEdit.text.toString().trim()

                if (newNamaAsetText.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val newAset = Aset(
                                id_aset = "", // Let the server generate the ID
                                nama = newNamaAsetText,
                                created_at = Date(),
                                updated_at = Date()
                            )

                            val response = MoneyService.createAset(newAset, "Bearer $token")

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    // Update the adapter with the new asset
                                    assetAdapter.addAsset(newAset)
                                } else {
                                    Log.e("AssetActivity2", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("AssetActivity2", "Error: ${e.message}", e)
                        }
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

}
