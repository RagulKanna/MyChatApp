package com.example.chatapp.viewmodel

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

    private val _gotoUserChatListPageStatus = MutableLiveData<Boolean>()
    val gotoUserChatListPageStatus = _gotoUserChatListPageStatus as LiveData<Boolean>

    private val _gotoUpdateUserDetailsPageStatus = MutableLiveData<Boolean>()
    val gotoUpdateUserDetailsPageStatus = _gotoUpdateUserDetailsPageStatus as LiveData<Boolean>

    private val _gotoDetailsRegisterPageStatus = MutableLiveData<Boolean>()
    val gotoDetailsRegisterPageStatus = _gotoDetailsRegisterPageStatus as LiveData<Boolean>

    private val _gotoUserChatPageStatus = MutableLiveData<Boolean>()
    val gotoUserChatPageStatus = _gotoUserChatPageStatus as LiveData<Boolean>

    fun gotoDetailsRegisterPage(status: Boolean) {
        _gotoDetailsRegisterPageStatus.value = status
    }

    fun gotoUpdateUserDetailsPage(status: Boolean) {
        _gotoUpdateUserDetailsPageStatus.value = status
    }

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

    fun gotoUserChatPage(status: Boolean) {
        _gotoUserChatPageStatus.value = status
    }
}