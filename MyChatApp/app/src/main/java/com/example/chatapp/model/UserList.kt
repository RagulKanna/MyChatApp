package com.example.chatapp.model

import android.net.Uri

data class UserList(var userName: String, var lastMessage: String, var userProfilePhoto: Uri, var userID: String)
