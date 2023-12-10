package com.example.pengelolakeuangan

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesUtil {

    fun retrieveTokenFromSharedPreferences(context: Context): String? {
          val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

           return sharedPreferences.getString("user_token", null)
    }

 }
