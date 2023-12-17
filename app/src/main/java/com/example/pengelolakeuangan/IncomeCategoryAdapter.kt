package com.example.pengelolakeuangan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class IncomeCategoryAdapter(private val kategoriList: MutableList<ApiService.Kategori>) :
    RecyclerView.Adapter<IncomeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemIncome: TextView = itemView.findViewById(R.id.textViewItemincome)
        val imageViewHapus: ImageView = itemView.findViewById(R.id.hapus)
        val imageViewEdit: ImageView = itemView.findViewById(R.id.edit)

        init {
            // Add click listener for the delete icon
            imageViewHapus.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val deletedKategori = kategoriList[position]

                    // Call the API to delete the category
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val response = MoneyService.deleteKategori(deletedKategori.id_kategori)

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    // If successful, remove the category from the dataset
                                    kategoriList.removeAt(position)
                                    notifyItemRemoved(position)
                                } else {
                                    // Handle the error, e.g., show a toast or log the error
                                    Log.e("IncomeCategoryAdapter", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            // Handle error, e.g., show a toast or log the error
                            Log.e("IncomeCategoryAdapter", "Error: ${e.message}", e)
                        }
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_kategori, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentKategori = kategoriList[position]

        // Tambahkan kondisi untuk memeriksa id_jenis
        if (currentKategori.id_jenis == "1") {
            holder.textViewItemIncome.text = currentKategori.nama
            holder.textViewItemIncome.visibility = View.VISIBLE
            holder.imageViewHapus.visibility = View.VISIBLE
            holder.imageViewEdit.visibility = View.VISIBLE
        } else {
            // Tampilkan teks lain atau kosong jika id_jenis tidak sesuai
            holder.textViewItemIncome.visibility = View.GONE
            holder.imageViewHapus.visibility = View.GONE
            holder.imageViewEdit.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }

    // Add a function to update the dataset and sort by id_jenis
    fun updateDataset(newKategoriList: List<ApiService.Kategori>) {
        // Remove items that are not id_jenis == "1"
        kategoriList.removeAll { it.id_jenis != "1" }
        // Add new items

        // Sort the list by id_jenis
        kategoriList.sortWith(compareByDescending { it.id_jenis == "1" })

        // Notify data set changed
        notifyDataSetChanged()
    }

}

