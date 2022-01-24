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
import com.example.chatapp.adapter.MessageAdapter
import com.example.chatapp.model.*
import com.example.chatapp.viewmodel.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.material.imageview.ShapeableImageView
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class FireBaseService(private val context: Context) {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var userDatabaseReference: DatabaseReference
    private lateinit var databaseReference: DatabaseReference
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var userFileReference: StorageReference
    private lateinit var otherUserFileReference: StorageReference

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        fireStore = FirebaseFirestore.getInstance()
        userFileReference =
            FirebaseStorage.getInstance().getReference("ProfileImage/${firebaseAuth.uid}/")
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
                    SharedPreference.addString(Constant.PHONE_NUMBER, phoneNumber)
                    sharedViewModel.gotoRegisterPage(false)
                    sharedViewModel.gotoOtpPage(true)
                    super.onCodeSent(codeForUser, phoneAuthProvider)
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Log.d("Exception", "$p0")
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
                                Constant.FIREBASE_UID,
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
                            SharedPreference.addString(Constant.REGISTER_USERNAME, user.userName)
                            SharedPreference.addString(Constant.STATUS, user.status)
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


    fun addMessageToDatabase(senderRoom: String, receiverRoom: String, messageObject: Message) {
        databaseReference.child("chats").child(senderRoom).child("messages").push()
            .setValue(messageObject).addOnSuccessListener {
                databaseReference.child("chats").child(receiverRoom).child("messages").push()
                    .setValue(messageObject)
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
            val uploadId = imageUri.toString()
            SharedPreference.initSharedPreference(context)
            SharedPreference.addString(Constant.PROFILE_PICTURE, uploadId)
            progressBar.visibility = View.INVISIBLE
        }.addOnFailureListener {
            Toast.makeText(context, "Upload Failed", Toast.LENGTH_SHORT).show()
        }
    }

    fun retrieveImageForCurrentUser(
        context: Context,
        profilePhoto: ShapeableImageView,
    ) {
        userFileReference.downloadUrl.addOnSuccessListener(object :
            OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri?) {
                Glide.with(context).load(uri).into(profilePhoto)
            }
        }).addOnFailureListener {
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
                SharedPreference.addString(Constant.PROFILE_PICTURE, uri.toString())
                Glide.with(context).load(uri).into(profilePhoto)
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Image Error", Toast.LENGTH_SHORT).show()
        }
    }

    fun checkForNewUser() {
        val query = databaseReference.child("users").child(firebaseAuth.currentUser!!.uid)
        val eventListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    SharedPreference.initSharedPreference(context)
                    SharedPreference.addString(Constant.CHECK_USER_FLAG, "0")
                } else {
                    SharedPreference.initSharedPreference(context)
                    SharedPreference.addString(Constant.CHECK_USER_FLAG, "1")
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        }
        query.addValueEventListener(eventListener)
    }

    fun
}

