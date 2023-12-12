package com.example.pengelolakeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AsetAdapter(listAset: List<Aset>) : RecyclerView.Adapter<AsetAdapter.ViewHolder>() {

    private var listAset = emptyList<Aset>()

    fun setData(listAset: List<Aset>) {
        this.listAset = listAset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_aset_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aset = listAset[position]
        holder.bind(aset)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(aset: Aset) {
            itemView.findViewById<TextView>(R.id.aset_name).text = aset.nama
        }

        private fun formatTanggal(date: Date): String {
            val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
            return sdf.format(date)
        }
    }
}

data class Aset(
    val id_aset : String,
    val nama : String,
    val created_at: Date,
    val updated_at: Date
)