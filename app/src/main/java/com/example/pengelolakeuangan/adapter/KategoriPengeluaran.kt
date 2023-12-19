package com.example.pengelolakeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.AddPengeluaranActivity
import com.example.pengelolakeuangan.R
import com.google.gson.annotations.SerializedName

class KategoriPengeluaran(
        private val kategoriPengeluaranList: List<KatPengeluaran>,
        private val activity: AddPengeluaranActivity
) : RecyclerView.Adapter<KategoriPengeluaran.ViewHolder>() {

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private val nameTextView: TextView = itemView.findViewById(R.id.aset_name)

                fun bind(katPenge: KatPengeluaran) {
                        nameTextView.text = katPenge.nama
                }


        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val itemView = inflater.inflate(R.layout.fragment_aset_item_list, parent, false)
                return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val currentKategori = kategoriPengeluaranList[position]

                holder.bind(currentKategori)
                holder.itemView.setOnClickListener {
                        activity.onKategoriItemClick(currentKategori.nama)
                }
        }

        override fun getItemCount(): Int {
                return kategoriPengeluaranList.size
        }
}

data class KatPengeluaran(
        @SerializedName("id_kategori")
        val idKategori: String,
        @SerializedName("id_user")
        val idUser: String,
        @SerializedName("id_jenis")
        val idJenis: String,
        @SerializedName("nama")
        val nama: String,
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("updated_at")
        val updatedAt: String
)
