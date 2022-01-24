package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.chatapp.R
import com.example.chatapp.adapter.TabPageAdapter
import com.example.chatapp.databinding.MainPageBinding
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.MainPageViewModel
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth

@Suppress("DEPRECATION")
class MainPage : Fragment() {

    private lateinit var binding: MainPageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mainPageViewModel: MainPageViewModel
    private lateinit var tab: TabLayout
    private lateinit var viewPager: ViewPager
    private lateinit var fireBaseService: FireBaseService
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.main_page, container, false)
        tab = binding.tabLayout
        viewPager = binding.viewpager
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        mainPageViewModel =
            ViewModelProvider(
                requireActivity(),
            )[MainPageViewModel::class.java]
        fireBaseService = FireBaseService(requireContext())
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setProfileImage()
        changeAdapter()
        showMenuList()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setProfileImage() {
        fireBaseService.retrieveImageForCurrentUser(requireContext(), binding.profilePhotoView)
    }

    private fun changeAdapter() {
        val adapter = TabPageAdapter(parentFragmentManager)
        adapter.addFragment(DisplayUserChatList(), "Chats")
        adapter.addFragment(DisplayGroupChatList(), "Groups")
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)
    }

    private fun showMenuList() {
        binding.profilePhotoView.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), binding.profilePhotoView)
            popupMenu.menuInflater.inflate(R.menu.profile_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.myProfile -> {
                        sharedViewModel.gotoChatListPage(false)
                        sharedViewModel.gotoUpdateUserDetailsPage(true)
                    }
                    R.id.logout -> {
                        FirebaseAuth.getInstance().signOut()
                        Toast.makeText(context, "Signed Out", Toast.LENGTH_SHORT).show()
                        SharedPreference.initSharedPreference(requireContext())
                        SharedPreference.clear()
                        sharedViewModel.gotoHomePage(true)
                    }
                }
                true
            }
            popupMenu.show()
        }
    }
}