package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.chatapp.R
import com.example.chatapp.model.Constant
import com.example.chatapp.model.Group
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.google.android.material.imageview.ShapeableImageView

class GroupListAdapter(
    val context: Context, private val groupList: ArrayList<Group>,
    val sharedViewModel: SharedViewModel
) :
    RecyclerView.Adapter<GroupListAdapter.GroupChatListViewHolder>() {
    val fireBaseService = FireBaseService(context)

    class GroupChatListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val groupName: TextView = itemView.findViewById(R.id.userNameInList)
        val lastMessage: TextView = itemView.findViewById(R.id.userLastMessage)
        val groupProfilePhoto: ShapeableImageView = itemView.findViewById(R.id.profilePhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupChatListViewHolder {
        val view =
            LayoutInflater.from(context).inflate(R.layout.display_chat_list_layout, parent, false)
        return GroupChatListViewHolder(view)
    }

    override fun onBindViewHolder(holder: GroupChatListViewHolder, position: Int) {
        val currentGroup = groupList[position]
        holder.groupName.text = currentGroup.groupName
        holder.lastMessage.text = currentGroup.lastMessage
        fireBaseService.retrieveImageForGroup(context, currentGroup, holder.groupProfilePhoto)
        holder.itemView.setOnClickListener {
            SharedPreference.initSharedPreference(context)
            SharedPreference.addString(Constant.GROUP_NAME, currentGroup.groupName)
            sharedViewModel.gotoChatListPage(false)
            sharedViewModel.gotoGroupChatPage(true)
        }
    }

    override fun getItemCount(): Int {
        return groupList.size
    }

}