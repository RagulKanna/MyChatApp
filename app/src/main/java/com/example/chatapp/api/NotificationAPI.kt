package com.example.chatapp.api

import com.example.chatapp.model.Constant.CONTENT_TYPE
import com.example.chatapp.model.Constant.SERVER_KEY
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationAPI {

    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    fun postNotification(
        @Body messageBody: String
    ): Call<String>
}