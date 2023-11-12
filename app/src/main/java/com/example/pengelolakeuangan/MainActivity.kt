package com.example.pengelolakeuangan
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.transaction -> {
                    replaceFragment(DailyFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.bugdet -> {
                    replaceFragment(MonthlyFragment())
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }


        replaceFragment(DailyFragment())
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit()
    }
}
