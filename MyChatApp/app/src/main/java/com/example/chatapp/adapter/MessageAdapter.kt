package com.example.chatapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.Constant
import com.example.chatapp.model.Message
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter(val context: Context, val messagelist: ArrayList<Message>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    val ITEM_RECEIVE = 1
    val ITEM_SENT = 2
    val fireBaseService = FireBaseService(context)

    class SenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val sendMessage: TextView = itemView.findViewById(R.id.send_Message)
        val profilePhoto: ShapeableImageView = itemView.findViewById(R.id.profilePhoto)
    }

    class ReceiverViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val receiveMessage: TextView = itemView.findViewById(R.id.receive_message)
        val profilePhoto: ShapeableImageView = itemView.findViewById(R.id.profilePhoto)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == 1) {
            val view =
                LayoutInflater.from(context).inflate(R.layout.receive_message_layout, parent, false)
            return ReceiverViewHolder(view)
        } else {
            val view =
                LayoutInflater.from(context).inflate(R.layout.send_message_layout, parent, false)
            return SenderViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messagelist[position]
        if (holder.javaClass == SenderViewHolder::class.java) {
            val viewHolder = holder as SenderViewHolder
            viewHolder.sendMessage.text = currentMessage.message
            fireBaseService.retrieveImageForCurrentUser(context, viewHolder.profilePhoto)
        } else {
            val viewHolder = holder as ReceiverViewHolder
            viewHolder.receiveMessage.text = currentMessage.message
            fireBaseService.retrieveImageForOtherUserInGroup(context, currentMessage, viewHolder.profilePhoto)
            Glide.with(context)
                .load(SharedPreference.get(Constant.OTHER_USER_PROFILE_PICTURE).toUri())
                .into(viewHolder.profilePhoto)
        }
    }

    override fun getItemCount(): Int {
        return messagelist.size
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messagelist[position]
        if (FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)) {
            return ITEM_SENT
        } else {
            return ITEM_RECEIVE
        }
    }
}
