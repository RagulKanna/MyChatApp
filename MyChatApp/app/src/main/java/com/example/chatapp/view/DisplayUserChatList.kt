package com.example.chatapp.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.adapter.ChatListAdapter
import com.example.chatapp.model.Users
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory

class DisplayUserChatList : Fragment() {

    private lateinit var userListRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Users>
    private lateinit var sharedViewModel: SharedViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.display_chat_list, container, false)
        val fireBaseService = FireBaseService(requireContext())
        userListRecyclerView = view.findViewById(R.id.userListRecycler)
        userList = arrayListOf()
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory()
        )[SharedViewModel::class.java]
        fireBaseService.retrieveUserDataFromDataBase(
            userList,
            userListRecyclerView,
            sharedViewModel
        )
        return view
    }
}