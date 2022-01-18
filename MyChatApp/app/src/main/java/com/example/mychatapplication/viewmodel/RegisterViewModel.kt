package com.example.mychatapplication.viewmodel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mychatapplication.model.CountryCodeSpinnerObject

class RegisterViewModel : ViewModel() {
    private lateinit var countryCode: ArrayList<String>

    private var _onRegisterSubmitButtonClicked = MutableLiveData<Boolean>()
    val onRegisterSubmitButtonClicked = _onRegisterSubmitButtonClicked as LiveData<Boolean>

    fun onRegisterSubmitButtonClicked() {
        _onRegisterSubmitButtonClicked.value = true
    }

}