package com.example.pengelolakeuangan.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.ApiService
import com.example.pengelolakeuangan.MoneyService
import com.example.pengelolakeuangan.R
import com.example.pengelolakeuangan.SharedPreferencesUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ExpensesCategoryAdapter(private val kategoriList: MutableList<ApiService.Kategori>) :
    RecyclerView.Adapter<ExpensesCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemExpense: TextView = itemView.findViewById(R.id.textViewItemExpense)
        val imageViewHapus: ImageView = itemView.findViewById(R.id.hapus)
        val imageViewEdit: ImageView = itemView.findViewById(R.id.edit)

        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(itemView.context)

        init {
            // Add click listener for the delete icon
            imageViewHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedKategori = kategoriList[position]

                    // Call the API to delete the category
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val response = MoneyService.deleteKategori(
                                deletedKategori.id_kategori,
                                "Bearer $token"
                            )

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    // If successful, remove the category from the dataset
                                    kategoriList.removeAt(position)
                                    notifyItemRemoved(position)
                                } else {
                                    // Handle the error, e.g., show a toast or log the error
                                    Log.e("ExpensesCategoryAdapter", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            // Handle error, e.g., show a toast or log the error
                            Log.e("ExpensesCategoryAdapter", "Error: ${e.message}", e)
                        }
                    }
                }
            }

            // Add click listener for the edit icon
            imageViewEdit.setOnClickListener {
                // Call the function to handle the edit dialog
                handleEditDialog()
            }
        }

        // Function to handle the edit dialog
        private fun handleEditDialog() {
            val view = LayoutInflater.from(itemView.context).inflate(R.layout.activity_tambahexpenses, null)
            val kategoriEdit = view.findViewById<EditText>(R.id.kategoriEdit)

            // Get the position of the clicked item
            val position = adapterPosition

            // Check if the position is valid
            if (position != RecyclerView.NO_POSITION) {
                val selectedKategori = kategoriList[position]

                // Set the initial text of the EditText to the current category name
                kategoriEdit.setText(selectedKategori.nama)

                AlertDialog.Builder(itemView.context)
                    .setView(view)
                    .setPositiveButton("Edit") { _, _ ->
                        // Handle the edit action here
                        val editedText = kategoriEdit.text.toString().trim()

                        // Check if the edited text is different from the current category name
                        if (editedText != selectedKategori.nama) {
                            // Call the API to update the category name
                            GlobalScope.launch(Dispatchers.IO) {
                                try {
                                    val response = MoneyService.updateKategori(
                                        selectedKategori.id_kategori,
                                        ApiService.Kategori(
                                            id_kategori = selectedKategori.id_kategori,
                                            id_jenis = selectedKategori.id_jenis,
                                            nama = editedText,
                                            created_at = selectedKategori.created_at,
                                            updated_at = selectedKategori.updated_at
                                        ),
                                        "Bearer $token"
                                    )

                                    withContext(Dispatchers.Main) {
                                        if (response.isSuccessful) {
                                            // If successful, update the category name in the dataset
                                            selectedKategori.nama = editedText
                                            notifyItemChanged(position)
                                        } else {
                                            // Handle the error, e.g., show a toast or log the error
                                            Log.e("ExpensesCategoryAdapter", "Error: ${response.message()}")
                                        }
                                    }
                                } catch (e: Exception) {
                                    // Handle error, e.g., show a toast or log the error
                                    Log.e("ExpensesCategoryAdapter", "Error: ${e.message}", e)
                                }
                            }
                        }
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_kategoriexp, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentKategori = kategoriList[position]

        // Tambahkan kondisi untuk memeriksa id_jenis
        if (currentKategori.id_jenis == "2") {
            holder.textViewItemExpense.text = currentKategori.nama
            holder.textViewItemExpense.visibility = View.VISIBLE
            holder.imageViewHapus.visibility = View.VISIBLE
            holder.imageViewEdit.visibility = View.VISIBLE
        } else {
            // Tampilkan teks lain atau kosong jika id_jenis tidak sesuai
            holder.textViewItemExpense.visibility = View.GONE
            holder.imageViewHapus.visibility = View.GONE
            holder.imageViewEdit.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }

    // Add a function to update the dataset and sort by id_jenis
    fun updateDataset(newKategoriList: List<ApiService.Kategori>) {
        // Remove items that are not id_jenis == "2" (adjust based on your requirements)
        kategoriList.removeAll { it.id_jenis != "2" }
        // Add new items

        // Sort the list by id_jenis
        kategoriList.sortWith(compareByDescending { it.id_jenis == "2" })

        // Notify data set changed
        notifyDataSetChanged()
    }
}
