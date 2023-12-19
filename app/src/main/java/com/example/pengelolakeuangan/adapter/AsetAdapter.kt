package com.example.pengelolakeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.R
import java.util.Date

class AsetAdapter(
    private var listAset: List<Aset>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<AsetAdapter.ViewHolder>() {

    fun setData(listAset: List<Aset>) {
        this.listAset = listAset
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_aset_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listAset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val aset = listAset[position]
        holder.bind(aset)
        holder.itemView.setOnClickListener {
            listener.onItemClick(aset.nama, aset.id_aset)
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val namaTextView: TextView = itemView.findViewById(R.id.aset_name)

        fun bind(aset: Aset) {
            namaTextView.text = aset.nama
        }
    }

    interface OnItemClickListener {
        fun onItemClick(nama: String, id_aset: String)
    }
}

data class Aset(
    val id_aset: String,
    val nama: String,
    val created_at: Date,
    val updated_at: Date
)
