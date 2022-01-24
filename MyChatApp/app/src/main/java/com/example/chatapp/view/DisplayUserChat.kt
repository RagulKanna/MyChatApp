package com.example.chatapp.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.DisplayUserChatPageBinding
import com.example.chatapp.model.Constant
import com.example.chatapp.model.Message
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.example.chatapp.viewmodel.UserChatViewModel
import com.example.chatapp.viewmodel.UserChatViewModelFactory


class DisplayUserChat : Fragment() {

    private lateinit var binding: DisplayUserChatPageBinding
    private lateinit var userChatViewModel: UserChatViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var messageList: ArrayList<Message>
    var receiverRoom: String? = null
    var senderRoom: String? = null
    private lateinit var firebaseService: FireBaseService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.display_user_chat_page, container, false)
        userChatViewModel = ViewModelProvider(
            requireActivity(),
            UserChatViewModelFactory()
        )[UserChatViewModel::class.java]
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory()
        )[SharedViewModel::class.java]
        senderRoom =
            SharedPreference.get(Constant.CURRENT_RECEIVER_USER_ID) + SharedPreference.get(Constant.FIREBASE_UID)
        receiverRoom =
            SharedPreference.get(Constant.FIREBASE_UID) + SharedPreference.get(Constant.CURRENT_RECEIVER_USER_ID)
        messageList = arrayListOf()
        firebaseService = FireBaseService(requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setDataFromDataBase()
        onClickFunction()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickFunction() {
        binding.sendBtn.setOnClickListener {
            val message = binding.messageBox.text.toString()
            val messageObject = Message(message, SharedPreference.get(Constant.FIREBASE_UID))
            firebaseService.addMessageToDatabase(senderRoom!!, receiverRoom!!, messageObject)
            binding.messageBox.setText("")
        }
        binding.backButton.setOnClickListener {
            sharedViewModel.gotoUserChatPage(false)
            sharedViewModel.gotoChatListPage(true)
        }
    }

    private fun setDataFromDataBase() {
        Log.d("senderRoom", "$senderRoom")
        Log.d("senderRoom", SharedPreference.get(Constant.CURRENT_RECEIVER_USER_NAME))
        binding.name.text = SharedPreference.get(Constant.CURRENT_RECEIVER_USER_NAME)
        Glide.with(requireContext()).load(SharedPreference.get(Constant.PROFILE_PICTURE).toUri())
            .into(binding.profile)
        firebaseService.retrieveMessageFromDatabase(senderRoom!!, messageList, binding.recyclerView)
    }
}