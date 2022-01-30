package com.example.chatapp.view

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.databinding.UserDetailsPageBinding
import com.example.chatapp.model.Constant
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.model.Users
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.example.chatapp.viewmodel.UserDetailsViewModel
import com.example.chatapp.viewmodel.UserDetailsViewModelFactory
import kotlin.properties.Delegates

class UserDetailsPage : Fragment() {

    private lateinit var binding: UserDetailsPageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var userDetailsViewModel: UserDetailsViewModel
    private lateinit var imageUri: Uri
    private lateinit var fireBaseService: FireBaseService
    private lateinit var user: Users
    private var upload_done by Delegates.notNull<Int>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fireBaseService = FireBaseService(requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.user_details_page, container, false)
        upload_done = 0
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        userDetailsViewModel =
            ViewModelProvider(
                requireActivity(),
                UserDetailsViewModelFactory(fireBaseService)
            )[UserDetailsViewModel::class.java]
        binding.progressBar.visibility = View.INVISIBLE
        binding.backButton.visibility = View.INVISIBLE
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkExistingUser()
        onProfilePictureUpload()
        onClickSubmit()
    }

    private fun onProfilePictureUpload() {
        binding.profilePhoto.setOnClickListener {
            openFileManager()
        }
    }

    private fun addToSharedPreference() {
        val userName = binding.userName.text.toString()
        val status = binding.status.text.toString()
        SharedPreference.initSharedPreference(requireContext())
        SharedPreference.addString(Constant.CURRENT_USERNAME, userName)
        SharedPreference.addString(Constant.CURRENT_USER_STATUS, status)
    }

    private fun onClickSubmit() {
        binding.submitButton.setOnClickListener {
            addToSharedPreference()
            if (upload_done == 1) {
                fireBaseService.uploadFile(requireContext(), imageUri, binding.progressBar)
            }
            user = Users(
                SharedPreference.get(Constant.CURRENT_PHONE_NUMBER),
                SharedPreference.get(Constant.CURRENT_USER_FIREBASE_UID),
                SharedPreference.get(Constant.CURRENT_USERNAME),
                SharedPreference.get(Constant.CURRENT_USER_STATUS),
                SharedPreference.get(Constant.CURRENT_USER_PROFILE_PICTURE)
            )
            userDetailsViewModel.registrationUserDetails(user)
            userDetailsViewModel.registrationStatus.observe(viewLifecycleOwner, Observer {
                if (it.status) {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
                    sharedViewModel.gotoChatListPage(true)
                } else {
                    Toast.makeText(requireContext(), "Registration Failed", Toast.LENGTH_SHORT)
                        .show()
                }
            })
        }
    }

    @Suppress("DEPRECATION")
    private fun openFileManager() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, PICTURE_REQUEST)
    }

    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICTURE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            upload_done = 1
            Glide.with(this).load(imageUri).into(binding.profilePhoto)
        }
    }

    private fun checkExistingUser() {
        if (SharedPreference.get(Constant.CHECK_USER_FLAG) == "0") {
            binding.userName.setText(SharedPreference.get(Constant.CURRENT_USERNAME))
            binding.status.setText(SharedPreference.get(Constant.CURRENT_USER_STATUS))
            Glide.with(requireContext())
                .load(SharedPreference.get(Constant.CURRENT_USER_PROFILE_PICTURE).toUri())
                .into(binding.profilePhoto)

        }
    }

    companion object {
        private val PICTURE_REQUEST = 1
    }
}