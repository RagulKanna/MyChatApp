package com.example.mychatapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.example.mychatapplication.R
import com.example.mychatapplication.adapter.TabPageAdapter
import com.example.mychatapplication.databinding.MainPageBinding
import com.example.mychatapplication.viewmodel.MainPageViewModel
import com.example.mychatapplication.viewmodel.SharedViewModel
import com.example.mychatapplication.viewmodel.SharedViewModelFactory
import com.google.android.material.tabs.TabLayout

@Suppress("DEPRECATION")
class MainPage : Fragment() {

    private lateinit var binding: MainPageBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var mainPageViewModel: MainPageViewModel
    private lateinit var tab: TabLayout
    private lateinit var viewPager: ViewPager

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
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        changeAdapter()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun changeAdapter() {
        val adapter = TabPageAdapter(parentFragmentManager)
        adapter.addFragment(DisplayUserChatList(), "Chats")
        adapter.addFragment(DisplayGroupChatList(), "Groups")
        viewPager.adapter = adapter
        tab.setupWithViewPager(viewPager)
    }
}