package com.example.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.service.FireBaseService

class UserDetailsViewModelFactory(val fireBaseService: FireBaseService) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserDetailsViewModel(fireBaseService) as T
    }
}