package com.example.chatapp.view

import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.chatapp.R
import com.example.chatapp.model.Group
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.imageview.ShapeableImageView

class DisplayGroupChatList : Fragment() {

    private lateinit var groupListRecyclerView: RecyclerView
    private lateinit var groupList: ArrayList<Group>
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var fabButton: FloatingActionButton
    private lateinit var fireBaseService: FireBaseService
    private lateinit var addGroupIcon: ShapeableImageView
    private lateinit var imageUri: Uri
    private var uploadStatus = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.display_chat_list, container, false)
        fireBaseService = FireBaseService(requireContext())
        groupListRecyclerView = view.findViewById(R.id.userListRecycler)
        fabButton = view.findViewById(R.id.addNewGroup)
        groupList = arrayListOf()
        sharedViewModel = ViewModelProvider(
            requireActivity(),
            SharedViewModelFactory()
        )[SharedViewModel::class.java]
        fireBaseService.retrieveUserDataFromGroupDataBase(
            groupList,
            groupListRecyclerView,
            sharedViewModel
        )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickFab()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickFab() {
        fabButton.setOnClickListener {
            Toast.makeText(context, "working", Toast.LENGTH_SHORT).show()
            showDialog()
        }
    }

    private fun showDialog() {
        val dialogBox = Dialog(requireContext())
        dialogBox.setContentView(R.layout.add_group_layout)
        val groupName: EditText = dialogBox.findViewById(R.id.group_name_input)
        val createGroup: ImageButton = dialogBox.findViewById(R.id.add_Group)
        val cancelGroupCreation: ImageButton = dialogBox.findViewById(R.id.cancel_Group_Creation)
        addGroupIcon = dialogBox.findViewById(R.id.add_group_icon)
        addGroupIcon.setOnClickListener {
            openFileManager()
        }
        createGroup.setOnClickListener {
            if (groupName.text.isNotEmpty() && uploadStatus == 1) {
                val name = groupName.text.toString()
                fireBaseService.createGroup(name, imageUri)
                fireBaseService.retrieveUserDataFromGroupDataBase(
                    groupList,
                    groupListRecyclerView,
                    sharedViewModel
                )
                dialogBox.dismiss()
            } else {
                Toast.makeText(requireContext(), "Add details of group", Toast.LENGTH_SHORT).show()
            }
        }
        cancelGroupCreation.setOnClickListener {
            dialogBox.dismiss()
        }
        dialogBox.show()
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
            uploadStatus = 1
            Glide.with(this).load(imageUri).into(addGroupIcon)
        }
    }

    companion object {
        private const val PICTURE_REQUEST = 1
    }
}