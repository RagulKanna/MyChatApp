package com.example.chatapp.service

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.adapter.ChatListAdapter
import com.example.chatapp.adapter.GroupListAdapter
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.api.RetrofitInstance
import com.example.chatapp.model.*
import com.example.chatapp.viewmodel.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class FireBaseService(private val context: Context) {

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var userDatabaseReference: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var userFileReference: StorageReference
    private lateinit var groupFileReference: StorageReference
    private lateinit var otherUserFileReference: StorageReference

    init {
        fireStore = FirebaseFirestore.getInstance()
        userFileReference =
            FirebaseStorage.getInstance()
                .getReference("ProfileImage/${firebaseAuth.currentUser?.uid}/")
                .child("image")
        userDatabaseReference = FirebaseDatabase.getInstance().reference.child("users")
        databaseReference = FirebaseDatabase.getInstance().reference
    }


    fun phoneVerification(
        activity: Activity,
        phoneNumber: String,
        sharedViewModel: SharedViewModel,
    ) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91 $phoneNumber")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity)                 // Activity (for callback binding)
            .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onCodeSent(
                    codeForUser: String,
                    phoneAuthProvider: PhoneAuthProvider.ForceResendingToken
                ) {
                    SharedPreference.initSharedPreference(context)
                    SharedPreference.addString(Constant.VERIFICATION_ID, codeForUser)
                    SharedPreference.addString(Constant.CURRENT_PHONE_NUMBER, phoneNumber)
                    sharedViewModel.gotoRegisterPage(false)
                    sharedViewModel.gotoOtpPage(true)
                    super.onCodeSent(codeForUser, phoneAuthProvider)
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(context, "Verification Failed$p0", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun addAndUpdateCurrentUserNameToDatabase(user: Users, listener: (AuthListener) -> Unit) {
        if (firebaseAuth.currentUser != null) {
            databaseReference.child("users").child(firebaseAuth.currentUser!!.uid)
                .setValue(
                    Users(
                        user.PhoneNumber,
                        firebaseAuth.currentUser!!.uid,
                        user.userName,
                        user.status,
                        user.profile
                    )
                )
                .addOnCompleteListener(
                    OnCompleteListener {
                        if (it.isSuccessful) {
                            SharedPreference.addString(
                                Constant.CURRENT_USER_FIREBASE_UID,
                                firebaseAuth.currentUser!!.uid
                            )
                            listener(AuthListener(it.isSuccessful, "Registered Successfully"))
                        }
                    }
                )
        }
    }

    fun retrieveUserDataFromDataBase(
        userList: ArrayList<Users>,
        recyclerView: RecyclerView,
        sharedViewModel: SharedViewModel
    ) {
        databaseReference.child("users").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val user = postSnapshot.getValue(Users::class.java)
                    if (firebaseAuth.currentUser?.uid != user?.uid) {
                        userList.add(user!!)
                    } else {
                        SharedPreference.initSharedPreference(context)
                        if (user != null) {
                            SharedPreference.addString(Constant.CURRENT_USERNAME, user.userName)
                            SharedPreference.addString(Constant.CURRENT_USER_STATUS, user.status)
                            SharedPreference.addString(
                                Constant.OTHER_USER_PROFILE_PICTURE,
                                user.profile
                            )
                        }
                    }
                }
                val userListAdapter = ChatListAdapter(context, userList, sharedViewModel)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = userListAdapter
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    fun addMessageToDatabase(
        senderRoom: String,
        receiverRoom: String,
        messageObject: Message,
        receiver: String
    ) {
        databaseReference.child("chats").child(senderRoom).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                databaseReference.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject)
            }.addOnCompleteListener {
                databaseReference.child("users").child(firebaseAuth.currentUser!!.uid)
                    .child("lastMessage")
                    .setValue(messageObject.message)
                databaseReference.child("users").child(receiver).child("lastMessage")
                    .setValue(messageObject.message)
            }
    }

    fun retrieveMessageFromDatabase(
        senderRoom: String,
        messageList: ArrayList<Message>,
        recyclerView: RecyclerView
    ) {
        databaseReference.child("chats").child(senderRoom).child("messages")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapShot in snapshot.children) {
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    val userChatAdapter = MessageAdapter(context, messageList)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = userChatAdapter
                    recyclerView.scrollToPosition(messageList.size - 1)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun uploadFile(
        context: Context,
        imageUri: Uri,
        progressBar: ProgressBar
    ) {
        progressBar.visibility = View.VISIBLE
        userFileReference.putFile(imageUri).addOnSuccessListener {
            userFileReference.downloadUrl.addOnSuccessListener { uri ->
                SharedPreference.initSharedPreference(context)
                SharedPreference.addString(
                    Constant.CURRENT_USER_PROFILE_PICTURE,
                    uri.toString()
                )
                progressBar.visibility = View.INVISIBLE
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveImageForCurrentUser(
        context: Context,
        profilePhoto: ShapeableImageView,
    ) {
        userFileReference.downloadUrl.addOnSuccessListener { uri ->
            Glide.with(context).load(uri).into(profilePhoto)
            SharedPreference.initSharedPreference(context)
            SharedPreference.addString(Constant.CURRENT_USER_PROFILE_PICTURE, uri.toString())
        }.addOnFailureListener {
            Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveImageForOtherUser(
        context: Context,
        currentUser: Users,
        profilePhoto: ShapeableImageView
    ) {
        otherUserFileReference =
            FirebaseStorage.getInstance().getReference("ProfileImage/${currentUser.uid}/")
                .child("image")
        otherUserFileReference.downloadUrl.addOnSuccessListener { uri ->
            if (uri != null) {
                SharedPreference.initSharedPreference(context)
                SharedPreference.addString(Constant.OTHER_USER_PROFILE_PICTURE, uri.toString())
                Glide.with(context).load(uri).into(profilePhoto)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveImageForOtherUserInGroup(
        context: Context,
        currentUser: Message,
        profilePhoto: ShapeableImageView
    ) {
        otherUserFileReference =
            FirebaseStorage.getInstance().getReference("ProfileImage/${currentUser.senderId}/")
                .child("image")
        otherUserFileReference.downloadUrl.addOnSuccessListener { uri ->
            if (uri != null) {
                SharedPreference.initSharedPreference(context)
                SharedPreference.addString(Constant.OTHER_USER_PROFILE_PICTURE, uri.toString())
                Glide.with(context).load(uri).into(profilePhoto)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkForNewUser() {
        val query =
            databaseReference.child("users").child(firebaseAuth.currentUser?.uid!!)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val user = snapshot.getValue(Users::class.java)
                    SharedPreference.initSharedPreference(context)
                    SharedPreference.addString(Constant.CURRENT_USERNAME, user!!.userName)
                    SharedPreference.addString(Constant.CURRENT_USER_STATUS, user.status)
                    SharedPreference.addString(
                        Constant.CURRENT_USER_PROFILE_PICTURE,
                        user.profile
                    )
                    SharedPreference.addString(Constant.CHECK_USER_FLAG, "0")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        }
        query.addListenerForSingleValueEvent(eventListener)
    }

    fun updateToken(refreshToken: String) {
        val token = Token(refreshToken)
        databaseReference.child("tokens").child(firebaseAuth.currentUser?.uid!!)
            .setValue(token).addOnSuccessListener {
                SharedPreference.initSharedPreference(context)
                SharedPreference.addString(Constant.CURRENT_USER_FCM_TOKEN, refreshToken)
            }
    }

    fun setToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener {
            updateToken(it.result)
            FirebaseMessaging.getInstance().subscribeToTopic("myTopic")
            SharedPreference.initSharedPreference(context)
            SharedPreference.addString(Constant.CURRENT_USER_FCM_TOKEN, it.result)
        }
    }

    fun sendNotificationToOtherUser(receiver: String, message: String) {
        databaseReference.child("tokens").child(receiver).child("token")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userToken: String = snapshot.getValue(String::class.java).toString()
                    sendNotificationMessage(userToken, message)
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }

    private fun sendNotificationMessage(
        userToken: String,
        messageData: String
    ) {
        val notification = JSONObject()
        notification.put("title", SharedPreference.get(Constant.CURRENT_USERNAME))
        notification.put("body", messageData)
        val message = JSONObject()
        message.put("to", userToken)
        message.put("notification", notification)
        sendNotification(message.toString())
    }

    private fun sendNotification(notification: String) {
        RetrofitInstance.api.postNotification(notification)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    Log.d("TAG", "Response: ${response.code()}")
                    if (response.isSuccessful) {
                        try {
                            if (response.body() != null) {
                                val responseJson = JSONObject(response.body()!!)
                                val result = responseJson.getJSONArray("results")
                                Log.d("responseBody", response.body()!!)
                                if (responseJson.getInt("failure") == 1) {
                                    val error = result.get(0) as JSONObject
                                    Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
                                    return
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        Toast.makeText(context, "Notification sent", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT)
                            .show()

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })

    }


    fun createGroup(name: String, uri: Uri) {
        databaseReference.child("groups").child(name)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        Toast.makeText(context, "Group Name already exist", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        databaseReference.child("groups").child(name)
                            .setValue(Group(name, uri.toString()))
                            .addOnCompleteListener {
                                groupFileReference =
                                    FirebaseStorage.getInstance()
                                        .getReference("GroupImage/$name")
                                groupFileReference.child(name)
                                    .child("image").putFile(uri).addOnSuccessListener {
                                        Toast.makeText(
                                            context,
                                            "$name group created Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

    fun retrieveUserDataFromGroupDataBase(
        groupList: ArrayList<Group>,
        groupListRecyclerView: RecyclerView,
        sharedViewModel: SharedViewModel
    ) {
        databaseReference.child("groups").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                groupList.clear()
                for (postSnapshot in snapshot.children) {
                    val group = postSnapshot.getValue(Group::class.java)
                    groupList.add(group!!)
                    SharedPreference.initSharedPreference(context)
                    SharedPreference.addString(Constant.GROUP_NAME, group.groupName)
                    SharedPreference.addString(
                        Constant.GROUP_PROFILE_PICTURE,
                        group.groupProfileImage
                    )
                }

                val groupListAdapter = GroupListAdapter(context, groupList, sharedViewModel)
                groupListRecyclerView.layoutManager = LinearLayoutManager(context)
                groupListRecyclerView.adapter = groupListAdapter
                groupListRecyclerView.scrollToPosition(groupList.size - 1)

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun retrieveImageForGroup(
        context: Context,
        currentGroup: Group,
        userProfilePhoto: ShapeableImageView
    ) {
        groupFileReference =
            FirebaseStorage.getInstance().getReference("GroupImage/${currentGroup.groupName}")
                .child(currentGroup.groupName)
                .child("image")
        groupFileReference.downloadUrl.addOnSuccessListener { uri ->
            if (uri != null) {
                SharedPreference.initSharedPreference(context)
                SharedPreference.addString(Constant.GROUP_PROFILE_PICTURE, uri.toString())
                Glide.with(context).load(uri).into(userProfilePhoto)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun addMessageToGroupDatabase(messageObject: Message) {
        databaseReference.child("groupChat").child(SharedPreference.get(Constant.GROUP_NAME))
            .child("message").push().setValue(
                messageObject
            ).addOnCompleteListener {
                databaseReference.child("groups")
                    .child(SharedPreference.get(Constant.GROUP_NAME))
                    .child("lastMessage")
                    .setValue(messageObject.message)
            }
    }

    fun retrieveMessageFromGroupDatabase(
        messageList: ArrayList<Message>,
        recyclerView: RecyclerView
    ) {
        databaseReference.child("groupChat").child(SharedPreference.get(Constant.GROUP_NAME))
            .child("message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {
                    messageList.clear()
                    for (postSnapShot in snapshot.children) {
                        val message = postSnapShot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    val groupChatAdapter = MessageAdapter(context, messageList)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = groupChatAdapter
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    fun sendNotificationToAll(messageData: String, groupName: String) {
        val notification = JSONObject()
        notification.put("title", groupName)
        notification.put("body", messageData)
        val message = JSONObject()
        message.put("to", "/topics/all")
        message.put("notification", notification)
        sendNotification(message.toString())
    }
}








