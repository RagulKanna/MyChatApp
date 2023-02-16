package com.example.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.chatapp.model.AuthListener
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.model.Users

class UserDetailsViewModel(val fireBaseService: FireBaseService) : ViewModel() {
    private val _registrationStatus = MutableLiveData<AuthListener>()
    val registrationStatus = _registrationStatus as LiveData<AuthListener>

    private val _updateUserDetailsStatus = MutableLiveData<AuthListener>()
    val updateUserDetailsStatus = _updateUserDetailsStatus as LiveData<AuthListener>

    fun registrationUserDetails(user: Users) {
        fireBaseService.addAndUpdateCurrentUserNameToDatabase(user) {
            _registrationStatus.value = it
        }
    }

    fun updateUserDetails(user: Users) {
        fireBaseService.addAndUpdateCurrentUserNameToDatabase(user) {
            _updateUserDetailsStatus.value = it
        }
    }
}