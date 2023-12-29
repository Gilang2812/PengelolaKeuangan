package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AssetActivity : Fragment() {
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_asset, container, false)
        val token = SharedPreferencesUtil.retrieveTokenFromSharedPreferences(requireContext())

        return view
    }
}