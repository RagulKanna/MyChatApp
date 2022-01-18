package com.example.mychatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel() : ViewModel() {

    private val _gotoRegisterPageStatus = MutableLiveData<Boolean>()
    val gotoRegisterPageStatus = _gotoRegisterPageStatus as LiveData<Boolean>

    private val _gotoSplashScreenStatus = MutableLiveData<Boolean>()
    val gotoSplashScreenStatus = _gotoSplashScreenStatus as LiveData<Boolean>

    private val _gotoHomePageStatus = MutableLiveData<Boolean>()
    val gotoHomePageStatus = _gotoHomePageStatus as LiveData<Boolean>

    private val _gotoOtpPageStatus = MutableLiveData<Boolean>()
    val gotoOtpPageStatus = _gotoOtpPageStatus as LiveData<Boolean>

    private val _gotoChatListPageStatus = MutableLiveData<Boolean>()
    val gotoChatListPageStatus = _gotoChatListPageStatus as LiveData<Boolean>

    fun gotoChatListPage(status: Boolean) {
        _gotoChatListPageStatus.value = status
    }
    fun gotoRegisterPage(status: Boolean) {
        _gotoRegisterPageStatus.value = status
    }

    fun gotoSplashScreen(status: Boolean) {
        _gotoSplashScreenStatus.value = status
    }

    fun gotoHomePage(status: Boolean) {
        _gotoHomePageStatus.value = status
    }

    fun gotoOtpPage(status: Boolean) {
        _gotoOtpPageStatus.value = status
    }
}