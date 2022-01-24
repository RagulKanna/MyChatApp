package com.example.chatapp.service

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MessageService: FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "Token: $token")

    }

    override fun onMessageReceived(messaage: RemoteMessage) {
        super.onMessageReceived(messaage)
        Log.d("FCM", "Message: ${messaage.notification?.body}")
    }
}