package com.example.pengelolakeuangan

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.Aset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.content.Context

import java.util.Date

class AssetAdapter : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    private var assets: MutableList<Aset> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assets[position]

        holder.textViewAssetName.text = asset.nama

        // Handle edit button click
        holder.imageViewEdit.setOnClickListener {
            // Open a dialog for updating the asset
            handleEditAsetDialog(holder.itemView.context, position, asset)
        }
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    fun setAssets(newAssets: List<Aset>) {
        assets = newAssets.toMutableList()
        notifyDataSetChanged()
    }

    private fun handleEditAsetDialog(context: Context, position: Int, asset: Aset) {
        val view = LayoutInflater.from(context).inflate(R.layout.activity_editaset, null)
        val namaAsetEdit = view.findViewById<EditText>(R.id.editNamaAset)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(context).toString()

        namaAsetEdit.setText(asset.nama)

        AlertDialog.Builder(context)
            .setView(view)
            .setPositiveButton("Update") { _, _ ->
                val updatedNamaAsetText = namaAsetEdit.text.toString().trim()

                if (updatedNamaAsetText.isNotEmpty()) {
                    GlobalScope.launch(Dispatchers.IO) {
                        try {
                            val updatedAset = asset.copy(nama = updatedNamaAsetText)

                            val response = MoneyService.updateAset(asset.id_aset, updatedAset, "Bearer $token")

                            withContext(Dispatchers.Main) {
                                if (response.isSuccessful) {
                                    // Update the adapter with the updated asset
                                    updateAsset(position, updatedAset)
                                } else {
                                    Log.e("AssetAdapter", "Error: ${response.message()}")
                                }
                            }
                        } catch (e: Exception) {
                            Log.e("AssetAdapter", "Error: ${e.message}", e)
                        }
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun updateAsset(position: Int, updatedAsset: Aset) {
        assets[position] = updatedAsset
        notifyItemChanged(position)
    }

    fun addAsset(newAsset: Aset) {
        assets.add(newAsset)
        notifyItemInserted(assets.size - 1)
    }

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAssetName: TextView = itemView.findViewById(R.id.textViewAssetName)
        val imageViewEdit: ImageView = itemView.findViewById(R.id.edit) // Add this line
        // Add other views here based on your item layout
    }
}
