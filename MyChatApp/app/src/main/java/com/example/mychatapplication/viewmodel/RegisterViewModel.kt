package com.example.mychatapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private var _onRegisterSubmitButtonClicked = MutableLiveData<Boolean>()
    val onRegisterSubmitButtonClicked = _onRegisterSubmitButtonClicked as LiveData<Boolean>

    fun onRegisterSubmitButtonClicked() {
        _onRegisterSubmitButtonClicked.value = true
    }

}