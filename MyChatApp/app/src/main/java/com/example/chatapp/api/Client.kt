package com.example.chatapp.api


import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object Client {
    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit? {
        if (retrofit == null) {
            Retrofit.Builder().baseUrl("https://fcm.googleapis.com/fc/")
                .addConverterFactory(
                    ScalarsConverterFactory.create()
                )
                .build()
        }
        return retrofit
    }
}