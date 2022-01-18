package com.example.mychatapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapplication.view.OtpVerificationPage
import com.example.mychatapplication.view.RegisterPage
import com.example.mychatapplication.view.SplashScreen
import com.example.mychatapplication.viewmodel.SharedViewModel
import com.example.mychatapplication.viewmodel.SharedViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fragmentContainerView: FragmentContainerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentContainerView = findViewById(R.id.fragment_container)
        supportActionBar?.hide()
        sharedViewModel = ViewModelProvider(
            this,
            SharedViewModelFactory()
        )[SharedViewModel::class.java]
        atBegin()
        observeAppNav()
    }

    private fun atBegin() {
        sharedViewModel.gotoSplashScreen(true)
    }

    private fun observeAppNav() {
        sharedViewModel.gotoSplashScreenStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, SplashScreen()).commit()
                }
            }
        })

        sharedViewModel.gotoRegisterPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, RegisterPage()).commit()
                }
            }
        })

        sharedViewModel.gotoHomePageStatus.observe(this, Observer {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        })

        sharedViewModel.gotoOtpPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, OtpVerificationPage()).commit()
                }
            }
        })

        sharedViewModel.gotoChatListPageStatus.observe(this, Observer {
            if (it == true) {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.fragment_container, DisplayChatMembers()).commit()
                }
            }
        })
    }

}