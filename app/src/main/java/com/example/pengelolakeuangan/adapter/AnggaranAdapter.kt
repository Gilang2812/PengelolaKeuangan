package com.example.pengelolakeuangan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.MainActivity
import com.example.pengelolakeuangan.R
import com.google.gson.annotations.SerializedName
import java.util.*

class AnggaranAdapter(private var listAnggaran: List<Anggaran>) : RecyclerView.Adapter<AnggaranAdapter.AnggaranViewHolder>() {

    fun setData(listAnggaran: List<Anggaran>) {
        this.listAnggaran = listAnggaran
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnggaranViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_anggaran_list, parent, false)
        return AnggaranViewHolder(view)
    }

    override fun onBindViewHolder(holder: AnggaranViewHolder, position: Int) {
        val anggaran = listAnggaran[position]
        holder.bind(anggaran)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MainActivity::class.java)

            intent.putExtra("idAnggaran", anggaran.idAnggaran)
            intent.putExtra("nama", anggaran.nama)
            intent.putExtra("nilai", anggaran.nilai)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listAnggaran.size
    }

    class AnggaranViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nomor: TextView = itemView.findViewById(R.id.nomor)
        private val namaTextView: TextView = itemView.findViewById(R.id.namaAnggaran)
        private val nilaiTextView: TextView = itemView.findViewById(R.id.jumlah)

        fun bind(anggaran: Anggaran) {
            nomor.text = (adapterPosition + 1).toString()
            namaTextView.text = anggaran.nama
            nilaiTextView.text = anggaran.nilai.toString()
        }
    }
}

data class Anggaran(
    @SerializedName("id_anggaran")
    val idAnggaran: String,
    @SerializedName("id_user")
    val idUser: String,
    val nama: String,
    val nilai: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String
)
