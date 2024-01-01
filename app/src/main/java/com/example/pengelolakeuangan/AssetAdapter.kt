package com.example.pengelolakeuangan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.Aset

class AssetAdapter : RecyclerView.Adapter<AssetAdapter.AssetViewHolder>() {

    private var assets: MutableList<Aset> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AssetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_asset, parent, false)
        return AssetViewHolder(view)
    }

    override fun onBindViewHolder(holder: AssetViewHolder, position: Int) {
        val asset = assets[position]

        // Bind the asset data to the views in the ViewHolder
        holder.textViewAssetName.text = asset.nama
        // Add code to bind other asset information if needed
    }

    override fun getItemCount(): Int {
        return assets.size
    }

    fun setAssets(newAssets: List<Aset>) {
        assets = newAssets.toMutableList()
        notifyDataSetChanged()
    }

    fun addAsset(newAsset: Aset) {
        assets.add(newAsset)
        notifyItemInserted(assets.size - 1)
    }

    class AssetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewAssetName: TextView = itemView.findViewById(R.id.textViewAssetName)
        // Add other views here based on your item layout
    }
}

