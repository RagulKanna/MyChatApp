package com.example.mychatapplication.model

import android.content.Context
import android.content.SharedPreferences

object SharedPreference {
    private var sharedPreferences: SharedPreferences? = null
    fun initSharedPreference(context: Context) {
        sharedPreferences =
            context.getSharedPreferences("ChatSharedPreference", Context.MODE_PRIVATE)
    }

    fun addString(key: String, value: String) {
        val editor = sharedPreferences?.edit()
        if (editor != null) {
            editor.putString(key, value)
            editor.apply()
        }
    }

    fun get(key: String): String {
        return sharedPreferences?.getString(key, "").toString()
    }
}