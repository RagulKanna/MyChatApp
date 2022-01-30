package com.example.chatapp.viewmodel

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModel
import com.example.chatapp.model.Constant
import com.example.chatapp.model.ImageAnimation
import com.example.chatapp.model.SharedPreference
import com.google.firebase.auth.FirebaseAuth

class SplashScreenViewModel : ViewModel() {
    fun doAnimation(animation: ImageAnimation, iconImageView: ImageView, context: Context) {
        animation.startAnimation(iconImageView, context)
    }

    fun launchNextPage(sharedViewModel: SharedViewModel, handler: Handler, context: Context) {
        handler.postDelayed({
            if (FirebaseAuth.getInstance().currentUser != null) {
                sharedViewModel.gotoChatListPage(true)
            } else {
                sharedViewModel.gotoSplashScreen(false)
                sharedViewModel.gotoRegisterPage(true)
            }
        }, 3000)
    }
}