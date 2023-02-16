package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.DisplayGroupChatBinding
import com.example.chatapp.model.Constant
import com.example.chatapp.model.Message
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.GroupChatViewModel
import com.example.chatapp.viewmodel.GroupChatViewModelFactory
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory

class DisplayGroupChat : Fragment() {

    private lateinit var binding: DisplayGroupChatBinding
    private lateinit var groupChatViewModel: GroupChatViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var messageList: ArrayList<Message>
    private lateinit var firebaseService: FireBaseService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseService = FireBaseService(requireContext())
        binding =
            DataBindingUtil.inflate(inflater, R.layout.display_group_chat, container, false)
        groupChatViewModel = ViewModelProvider(
            requireActivity(),
            GroupChatViewModelFactory()
        )[GroupChatViewModel::class.java]
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory()
        )[SharedViewModel::class.java]
        messageList = arrayListOf()
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickFunction()
        setDataFromDataBase()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickFunction() {
        binding.sendBtn.setOnClickListener {
            if (binding.messageBox.text.isEmpty()) {
                Toast.makeText(context, "Enter message to send", Toast.LENGTH_SHORT).show()
            } else {
                val message = binding.messageBox.text.toString()
                val messageObject =
                    Message(message, SharedPreference.get(Constant.CURRENT_USER_FIREBASE_UID))
                firebaseService.addMessageToGroupDatabase(messageObject)
                firebaseService.sendNotificationToAll(
                    messageObject.message.toString(),
                    binding.name.text.toString()
                )
                binding.messageBox.setText("")
            }
        }
        binding.backButton.setOnClickListener {
            sharedViewModel.gotoGroupChatPage(false)
            sharedViewModel.gotoChatListPage(true)
        }
    }

    private fun setDataFromDataBase() {
        binding.name.text = SharedPreference.get(Constant.GROUP_NAME)
        Glide.with(requireContext())
            .load(SharedPreference.get(Constant.GROUP_PROFILE_PICTURE).toUri())
            .into(binding.profile)
        firebaseService.retrieveMessageFromGroupDatabase(messageList, binding.recyclerView)

    }
}