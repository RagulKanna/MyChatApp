package com.example.chatapp.model

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.example.chatapp.R

class ImageAnimation {
    fun startAnimation(iconImageView: ImageView, context: Context) {
        val animation: Animation = AnimationUtils.loadAnimation(
            context, R.anim.fade_animation_for_splash_screen_icon
        )
        iconImageView.startAnimation(animation)
    }
}