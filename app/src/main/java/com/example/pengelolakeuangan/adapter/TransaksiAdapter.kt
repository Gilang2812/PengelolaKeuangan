package com.example.pengelolakeuangan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.R
import java.util.Date

class TransaksiAdapter(transaksiList: List<Transaksi>) : RecyclerView.Adapter<TransaksiAdapter.ViewHolder>() {

    private var transaksiList = emptyList<Transaksi>()

    fun setData(transaksiList: List<Transaksi>) {
        this.transaksiList = transaksiList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val transaksi = transaksiList[position]
        holder.bind(transaksi)
    }

    override fun getItemCount(): Int {
        return transaksiList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaksi: Transaksi) {
            itemView.findViewById<TextView>(R.id.listpeng).text = transaksi.jumlah.toString()
        }
    }
}

data class Transaksi(
    val id_transaksi: String,
    val id_user: String,
    val id_kategori: String,
    val id_jenis: String,
    val id_aset: String,
    val tanggal: Date,
    val jumlah: Int,
    val note: String,
    val created_at: Date,
    val updated_at: Date
)