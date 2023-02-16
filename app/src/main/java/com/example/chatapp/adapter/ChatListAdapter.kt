package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.Constant
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.model.Users
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.google.android.material.imageview.ShapeableImageView

class ChatListAdapter(
    val context: Context,
    private val userList: ArrayList<Users>,
    val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<ChatListAdapter.UserChatListViewHolder>() {

    val fireBaseService = FireBaseService(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserChatListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.display_chat_list_layout, parent, false)
        return UserChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserChatListViewHolder, position: Int) {
        val currentUser = userList[position]
        holder.userName.text = currentUser.userName
        holder.lastMessage.text = currentUser.lastMessage
        fireBaseService.retrieveImageForOtherUser(context, currentUser, holder.userProfilePhoto)
        holder.itemView.setOnClickListener {
            SharedPreference.initSharedPreference(context)
            SharedPreference.addString(Constant.CURRENT_RECEIVER_USER_ID, currentUser.uid)
            SharedPreference.addString(Constant.CURRENT_RECEIVER_USER_NAME, currentUser.userName)
            sharedViewModel.gotoChatListPage(false)
            sharedViewModel.gotoUserChatPage(true)
        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userName: TextView = itemView.findViewById(R.id.userNameInList)
        val lastMessage: TextView = itemView.findViewById(R.id.userLastMessage)
        val userProfilePhoto: ShapeableImageView = itemView.findViewById(R.id.profilePhoto)
    }

}