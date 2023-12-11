package com.example.pengelolakeuangan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pengelolakeuangan.adapter.User
import kotlinx.coroutines.launch

class MonthlyFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_monthly_list, container, false)
        recyclerView = view.findViewById(R.id.list)

        lifecycleScope.launch {
            try {
                // Perform your network request here
                val response = MoneyService.getUser()
                val userList = response
                val adapter = UserAdapter(userList)
                recyclerView.adapter = adapter
            } catch (e: Exception) {
                // Handle the exception
                e.printStackTrace()
            }
        }

        return view
    }

    class UserAdapter(private val userList: List<User>) :
        RecyclerView.Adapter<UserAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_monthly, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val user = userList[position]
            holder.bind(user)
        }

        override fun getItemCount(): Int {
            return userList.size
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            fun bind(user: User) {
                itemView.findViewById<TextView>(R.id.listpeng).text = user.id_user

            }
        }
    }
}
