package com.example.pengelolakeuangan

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var daerahAdapter: ArrayAdapter<String>

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
        val alertDialog = builder.setView(view).show() // Combine setView and show

        val signupBtn = view.findViewById<Button>(R.id.signupUser)
        val nameEdit = view.findViewById<EditText>(R.id.nameEdit)
        val spinner: Spinner = view.findViewById(R.id.optionsSpinner)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

        val defaultOption = "Pilih Daerah"
        val defaultList = ArrayList<String>().apply { add(defaultOption) }
        daerahAdapter =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, defaultList)
        daerahAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = daerahAdapter

        lifecycleScope.launch {
            try {
                val daerahList = MoneyService.getDaerah()
                val daerahNames = daerahList.map { it.nama_daerah }
                daerahAdapter.addAll(daerahNames)
                daerahAdapter.notifyDataSetChanged()

                Log.e("Login Activity", "succes fetching daerah: ${daerahNames}")

            } catch (e: Exception) {
                Log.e("Login Activity", "  Error fetching daerah: ${e.message}", e)
            }
        }

        signupBtn.setOnClickListener {
            val id_daerah = spinner.selectedItem.toString()
            val name = nameEdit.text.toString()
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val response = MoneyService.createUser(id_daerah, name, email, password)
                    withContext(Dispatchers.Main) {
                        if (response.isSuccessful) {
                            // Request berhasil
                            val userData = response.body()
                            Log.d("Login activity", "ini usernya : $userData , ")
                            alertDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Berhasil melakukan registrasi!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e("Login Activity", "Error: ${response.errorBody()?.string()}")
                            Toast.makeText(
                                this@LoginActivity,
                                "Gagal registrasi: ${
                                    response.errorBody()?.string() ?: "Unknown Error"
                                }",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    Log.e("Login activity", "error post user/ signup : ${e.message}")
                    Toast.makeText(
                        this@LoginActivity,
                        "Gagal registrasi: ${e.message ?: "Unknown Error"}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }
        }
    }

    private fun handleLoginDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_login, null)
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.setView(view).create() // Use create() instead of show()
        alertDialog.show()

        val loginBtn = view.findViewById<Button>(R.id.login)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            // Check if email and password are not empty
            if (email.isNotEmpty() && password.isNotEmpty()) {
                // Call the login function
                lifecycleScope.launch {
                    try {
                        val loginRequest = ApiService.LoginRequest(email, password)
                        val response = MoneyService.login(loginRequest)
                        if (response.isSuccessful) {
                            // Login successful, handle accordingly
                            val userData = response.body()
                            Log.d("Login activity", "ini usernya : $userData , ")
                            alertDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            // Login failed, handle accordingly
                            Log.e("Login Activity", "Error: ${response.errorBody()?.string()}")
                            Toast.makeText(
                                this@LoginActivity,
                                "Login failed: ${
                                    response.errorBody()?.string() ?: "Unknown Error"
                                }",
                                Toast.LENGTH_SHORT
                            ).show()
                            Log.e("Login activity", "error loginnya : ${response.body()}")

                        }
                    } catch (e: Exception) {
                        Log.e("Login activity", "error during login: ${e.message}")
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: ${e.message ?: "Unknown Error"}",
                            Toast.LENGTH_SHORT

                        )
                            .show()
                        Log.e("Login activity", "error server loginnya : ${e.message}")

                    }
                }
            } else {
                // Handle empty fields (show a toast, for example)
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            }

            // Dismiss the dialog after handling the login
            alertDialog.dismiss()
        }
    }
}
