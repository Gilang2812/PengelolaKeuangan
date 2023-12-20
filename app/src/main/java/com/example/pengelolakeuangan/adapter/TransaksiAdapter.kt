package com.example.pengelolakeuangan.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.EditPemasukanActivity
import com.example.pengelolakeuangan.EditPengeluaranActivity
import com.example.pengelolakeuangan.MainActivity
import com.example.pengelolakeuangan.R
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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

        holder.itemView.setOnClickListener {
            val  context = holder.itemView.context

            val intent: Intent = when (transaksi.id_jenis) {
                1 -> Intent(context, EditPemasukanActivity::class.java)
                2 -> Intent(context, EditPengeluaranActivity::class.java)
                else -> Intent(context, MainActivity::class.java)
            }

            intent.putExtra("transaksiId", transaksi.id_transaksi)

            // Format tanggal sesuai dengan "yyyy-MM-dd HH:mm"
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            intent.putExtra("tanggal", dateFormat.format(transaksi.tanggal))
            intent.putExtra("kategori", transaksi.Kategori?.nama ?: "Kategori Tidak Diketahui")
            intent.putExtra("aset", transaksi.Aset?.nama ?: "Aset Tidak Diketahui")
            intent.putExtra("total", transaksi.jumlah)
            intent.putExtra("note", transaksi.note)

            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return transaksiList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(transaksi: Transaksi) {
            val kategoriNama = transaksi.Kategori?.nama ?: "Kategori Tidak Diketahui"
            val asetNama = transaksi.Aset?.nama ?: "Aset Tidak Diketahui"
            val jenisTransaksiNama = transaksi.JenisTransaksi?.nama ?: "Jenis Transaksi Tidak Diketahui"

            // Format tanggal
            val dateFormat = SimpleDateFormat("dd.MM", Locale.getDefault())
            val tanggalFormatted = dateFormat.format(transaksi.tanggal)

            itemView.findViewById<TextView>(R.id.tanggal).text = tanggalFormatted
            itemView.findViewById<TextView>(R.id.listkategori).text = kategoriNama
            itemView.findViewById<TextView>(R.id.listaset).text = asetNama
            itemView.findViewById<TextView>(R.id.jenis).text = jenisTransaksiNama
            itemView.findViewById<TextView>(R.id.jumlah).text = transaksi.jumlah.toString()
        }

    }

}
data class TransaksiResponse(
    val data: TransaksiData,
    val success: String
)

data class TransaksiData(
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("updated_at") val updatedAt: Date,
    @SerializedName("id_transaksi") val idTransaksi: String,
    @SerializedName("id_user") val idUser: String,
    @SerializedName("id_kategori") val idKategori: String,
    @SerializedName("id_jenis") val idJenis: String,
    @SerializedName("id_aset") val idAset: String,
    @SerializedName("tanggal") val tanggal: Date,
    @SerializedName("jumlah") val jumlah: Int,
    @SerializedName("note") val note: String
)
data class Transaksi(
    @SerializedName("id_transaksi")
    val id_transaksi: String,
    val id_user: String,
    val id_kategori: String,
    val id_jenis: Int,
    val id_aset: String,
    val tanggal: Date,
    val jumlah: Int,
    val note: String,
    val created_at: Date,
    val updated_at: Date,
    val Kategori: Kategori?,
    val Aset: AsetList?,
    val JenisTransaksi: JenisTransaksi?
)
data class TransaksiRequest(
    val id_kategori: String,
    val id_jenis: String,
    val id_aset: String,
    val tanggal: String,
    val jumlah: Int,
    val note: String
)
data class UpdateTransaksiRequest(
    val id_kategori: String,
    val id_aset: String,
    val tanggal: String,
    val jumlah: Int,
    val note: String
)
data class Kategori(
    val nama: String
)

data class AsetList(
    val nama: String
)

data class JenisTransaksi(
    val nama: String
)
data class DeleteTransaksiResponse(
    val success: String
)




