package com.example.pengelolakeuangan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IncomeCategoryAdapter(private val kategoriList: List<ApiService.Kategori>) :
    RecyclerView.Adapter<IncomeCategoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewItemIncome: TextView = itemView.findViewById(R.id.textViewItemincome)
        val imageViewHapus: ImageView = itemView.findViewById(R.id.hapus)
        //Tambahkan deklarasi properti ViewHolder lainnya jika ada
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_kategori, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentKategori = kategoriList[position]

        // Tambahkan kondisi untuk memeriksa id_jenis
        if (currentKategori.id_jenis == "2") {
            holder.textViewItemIncome.text = currentKategori.nama // Sembunyikan ImageView
        } else {
            // Tampilkan teks lain atau kosong jika id_jenis tidak sesuai
            holder.textViewItemIncome.visibility = View.GONE
            holder.imageViewHapus.visibility = View.GONE // Tampilkan ImageView
        }

        // ...
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }
}

