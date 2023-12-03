package com.example.pengelolakeuangan

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var daerahAdapter: ArrayAdapter<ApiService.Daerah>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        findViewById<Button>(R.id.login).setOnClickListener {
            handleLoginDialog()
        }

        findViewById<Button>(R.id.signup).setOnClickListener {
            handleSignupDialog()
        }
    }

    private fun handleSignupDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_signup, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()

        val signupBtn = view.findViewById<Button>(R.id.signup) // Ganti dengan ID yang sesuai
        val nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        val spinner: Spinner = view.findViewById(R.id.optionsSpinner)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

        daerahAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, listOf())

        daerahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = daerahAdapter

        signupBtn.setOnClickListener {
            val map = HashMap<String, String>()
            map["name"] = nameEdit.text.toString()
            map["email"] = emailEdit.text.toString()
            map["password"] = passwordEdit.text.toString()

            lifecycleScope.launch {
                try {
                    val daerahList = MoneyService.getDaerah()
                    daerahAdapter.clear()
                    daerahAdapter.addAll(daerahList)
                    daerahAdapter.notifyDataSetChanged()
                } catch (e: Exception) {
                    // Handle the error
                    e.printStackTrace()
                }
            }
        }
    }

    private fun handleLoginDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_login, null)
        val builder = AlertDialog.Builder(this)
        builder.setView(view).show()

        val loginBtn = view.findViewById<Button>(R.id.login) // Ganti dengan ID yang sesuai
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

        loginBtn.setOnClickListener {
            val map = HashMap<String, String>()
            map["email"] = emailEdit.text.toString()
            map["password"] = passwordEdit.text.toString()

            // Tambahkan logika untuk memproses data login di sini
        }
    }
}