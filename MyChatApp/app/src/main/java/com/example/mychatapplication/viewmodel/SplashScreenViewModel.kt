package com.example.mychatapplication.viewmodel

import android.content.Context
import android.os.Handler
import android.widget.ImageView
import androidx.lifecycle.ViewModel
import com.example.mychatapplication.model.ImageAnimation

class SplashScreenViewModel: ViewModel() {
    fun doAnimation(animation: ImageAnimation, iconImageView: ImageView, context: Context) {
        animation.startAnimation(iconImageView, context)
    }

    fun launchNextPage(sharedViewModel: SharedViewModel, handler: Handler) {
        handler.postDelayed({
            sharedViewModel.gotoSplashScreen(false)
            sharedViewModel.gotoRegisterPage(true)
        }, 3000)
    }
}