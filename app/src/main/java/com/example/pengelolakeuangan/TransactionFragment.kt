package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TransactionFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_transaction, container, false)

        // Get the BottomNavigationView from the parent activity
        val bottomNavigationView: BottomNavigationView = activity?.findViewById(R.id.top_navigation)!!

        // Set the item selection listener
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transaction -> {
                    // Check if the current fragment is not already DailyFragment
                    if (childFragmentManager.findFragmentById(R.id.transaction_container) !is DailyFragment) {
                        replaceFragment(DailyFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.monthly -> {
                    // Check if the current fragment is not already MonthlyFragment
                    if (childFragmentManager.findFragmentById(R.id.transaction_container) !is MonthlyFragment) {
                        replaceFragment(MonthlyFragment())
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }

        // Set default fragment
        if (childFragmentManager.findFragmentById(R.id.transaction_container) == null) {
            replaceFragment(DailyFragment())
        }

        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager.beginTransaction()
            .replace(R.id.transaction_container, fragment)
            .commit()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
