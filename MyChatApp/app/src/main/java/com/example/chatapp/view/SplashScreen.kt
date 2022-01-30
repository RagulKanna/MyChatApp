package com.example.chatapp.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.model.ImageAnimation
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.example.chatapp.viewmodel.SplashScreenViewModel
import com.example.chatapp.viewmodel.SplashScreenViewModelFactory


@SuppressLint("CustomSplashScreen")
class SplashScreen : Fragment() {

    private lateinit var iconImageView: ImageView
    private lateinit var handler: Handler
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var splashScreenViewModel: SplashScreenViewModel
    private val animation = ImageAnimation()

    @Suppress("DEPRECATION")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.splash_screen, container, false)
        iconImageView = view.findViewById(R.id.splashImageIcon)
        handler = Handler()
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        splashScreenViewModel =
            ViewModelProvider(
                requireActivity(),
                SplashScreenViewModelFactory()
            )[SplashScreenViewModel::class.java]
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        splashScreenViewModel.doAnimation(animation, iconImageView, requireContext())
        splashScreenViewModel.launchNextPage(sharedViewModel, handler, requireContext())
        super.onViewCreated(view, savedInstanceState)
    }

}