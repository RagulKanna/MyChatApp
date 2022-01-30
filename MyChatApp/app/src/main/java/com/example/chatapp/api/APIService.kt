package com.example.chatapp.api

import com.example.chatapp.model.MyResponse
import com.example.chatapp.model.Sender
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST

interface APIService {
    /*@Headers(
        "Authorization: key=AAAAS_KzHmA:APA91bGVc58FUC1pnDhG_kTkKnJJIhv_OYgLX9EtOj9yyphD8QJaZ2v83F6usdgaGkW0c0etiYoQShD7A_4fDBkPlDCQR4NZZAH00EkK0ZoQCozW-D1bZBtbkEQB77Q0a_Aa-lDy2R5B",
        "Content-Type: application/json"
    )*/
    @POST("send")
    fun sendNotification(@HeaderMap headers: HashMap<String,String>, @Body notification: Sender): Call<String>
}