package com.example.pengelolakeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.PemasukanActivity
import com.example.pengelolakeuangan.R
import com.example.pengelolakeuangan.adapter.KategoriAdapter.ViewHolder
import com.google.gson.annotations.SerializedName

class KategoriAdapter(private val kategoriList: List<KategoriPemasukan>, private val activity: PemasukanActivity) :
    RecyclerView.Adapter<ViewHolder>() {
    interface OnItemClickListener {
        fun onItemClick(nama: String, idKategori: String)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaTextView: TextView = itemView.findViewById(R.id.aset_name)
        fun bind(kategori: KategoriPemasukan) {
            namaTextView.text = kategori.nama
        }

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    activity?.onKategoriItemClick(kategoriList[position].nama, )
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.fragment_aset_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return kategoriList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val kategori = kategoriList[position]
        holder.bind(kategori)
        holder.itemView.setOnClickListener {
            activity.onKategoriItemClick(kategori.nama)
        }
    }
}

data class KategoriPemasukan(
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
