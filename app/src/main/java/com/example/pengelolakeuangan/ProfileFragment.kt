package com.example.pengelolakeuangan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val btnLogout = view.findViewById<Button>(R.id.btnLogout)
        val btnLocation = view.findViewById<Button>(R.id.location)

        btnLogout.setOnClickListener {
            logout()
        }

        btnLocation.setOnClickListener {
            location(it) // Pass the view as an argument
        }
        return view
    }

    private fun logout() {
        val sharedPreferences: SharedPreferences =
            requireActivity().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.remove("user_token")
        editor.apply()

        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


    fun location(view: View) {
        val intent = Intent(requireContext(), LocationActivity::class.java)
        startActivity(intent)
    }

}
