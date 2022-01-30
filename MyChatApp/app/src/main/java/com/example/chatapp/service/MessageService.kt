package com.example.chatapp.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.chatapp.R
import com.example.chatapp.model.Token
import com.example.chatapp.view.DisplayUserChatList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MessageService : FirebaseMessagingService() {

    val fireBaseService = FireBaseService(this)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        showNotification(
            remoteMessage
        )
    }
    private val firebaseUser = FirebaseAuth.getInstance().currentUser
    private val firebaseService = FireBaseService(this)

    override fun onNewToken(token: String) {
        var refreshToken: String = FirebaseMessaging.getInstance().token.toString()
        if (firebaseUser != null) {
            updateToken(refreshToken)
        }
        super.onNewToken(token)
    }

    fun updateToken(refreshToken: String) {
        firebaseService.updateToken(refreshToken)
    }

    private fun showNotification(remoteMessage: RemoteMessage) {
        val title: String = remoteMessage.data["title"].toString()
        val message: String = remoteMessage.data["message"].toString()
        val intent = Intent(this, DisplayUserChatList::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        var builder: NotificationCompat.Builder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.drawable.chat_application_icon)
                .setAutoCancel(true)
                .setVibrate(longArrayOf(1000, 1000))
                .setOnlyAlertOnce(true)
                .setContentIntent(pending)

        builder = builder.setContent(getRemoteView(title, message))
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        notificationManagerCompat.notify(0, builder.build())
    }

    @SuppressLint("RemoteViewLayout")
    private fun getRemoteView(title: String, message: String): RemoteViews {
        val remoteViews = RemoteViews("com.example.chatapp", R.layout.notification_layout)
        remoteViews.setTextViewText(R.id.title, title)
        remoteViews.setTextViewText(R.id.message, message)
        remoteViews.setImageViewResource(R.id.app_logo, R.drawable.chat_application_icon)
        return remoteViews
    }

    companion object {
        const val channelId = "notificationChannel"
        const val channelName = "com.example.chatapp"
    }
}