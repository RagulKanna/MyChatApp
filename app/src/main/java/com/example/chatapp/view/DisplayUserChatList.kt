package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.Users
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DisplayUserChatList : Fragment() {

    private lateinit var userListRecyclerView: RecyclerView
    private lateinit var userList: ArrayList<Users>
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fabButton: FloatingActionButton
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.display_chat_list, container, false)
        val fireBaseService = FireBaseService(requireContext())
        userListRecyclerView = view.findViewById(R.id.userListRecycler)
        fabButton = view.findViewById(R.id.addNewGroup)
        userList = arrayListOf()
        fabButton.hide()
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