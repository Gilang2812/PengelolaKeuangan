package com.example.pengelolakeuangan

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import com.example.pengelolakeuangan.adapter.LoginRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

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
        val alertDialog = builder.setView(view).show()

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

                Log.d("Login Activity", "Success fetching daerah: $daerahNames")

            } catch (e: Exception) {
                Log.e("Login Activity", "Error fetching daerah: ${e.message}", e)
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
                            val userData = response.body()
                            Log.d("Login activity", "User created: $userData")
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
                } catch (e: HttpException) {
                    Log.e("Login activity", "HTTP Error: ${e.code()} ${e.message()}")
                } catch (e: IOException) {
                    Log.e("Login activity", "Network Error: ${e.message}")
                } catch (e: Exception) {
                    Log.e("Login activity", "Error post user/signup: ${e.message}", e)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            this@LoginActivity,
                            "Gagal registrasi: ${e.message ?: "Unknown Error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun handleLoginDialog() {
        val view = layoutInflater.inflate(R.layout.fragment_login, null)
        val builder = AlertDialog.Builder(this)
        val alertDialog = builder.setView(view).create()
        alertDialog.show()

        val loginBtn = view.findViewById<Button>(R.id.login)
        val emailEdit = view.findViewById<EditText>(R.id.emailEdit)
        val passwordEdit = view.findViewById<EditText>(R.id.passwordEdit)

        loginBtn.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    try {
                        val token = retrieveTokenFromSharedPreferences()
                        if (token != null) {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            alertDialog.dismiss()
                            return@launch
                        }

                        val loginRequest = LoginRequest(email, password)
                        val response = MoneyService.login(loginRequest)
                        if (response.isSuccessful) {
                            val userData = response.body()
                            userData?.token?.let {
                                saveTokenToSharedPreferences(it)
                                Log.d("Login activity", "Received token: $it")
                            }

                            Log.d("Login activity", "User logged in: $userData")
                            alertDialog.dismiss()
                            Toast.makeText(
                                this@LoginActivity,
                                "Login successful!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Log.e("Login Activity", "Error: ${response.errorBody()?.string()}")
                            Toast.makeText(
                                this@LoginActivity,
                                "Login failed: ${
                                    response.errorBody()?.string() ?: "Unknown Error"
                                }",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    } catch (e: Exception) {
                        Log.e("Login activity", "Error during login: ${e.message}", e)
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed: ${e.message ?: "Unknown Error"}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } else {
                Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            }

            alertDialog.dismiss()
        }
    }

    private fun saveTokenToSharedPreferences(token: String) {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("user_token", token)
        editor.apply()
    }

    private fun retrieveTokenFromSharedPreferences(): String? {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("user_token", null)
    }
}
