package com.example.mychatapplication.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mychatapplication.R
import com.example.mychatapplication.databinding.RegisterPageBinding
import com.example.mychatapplication.model.PhoneVerification
import com.example.mychatapplication.viewmodel.RegisterViewModel
import com.example.mychatapplication.viewmodel.SharedViewModel
import com.example.mychatapplication.viewmodel.SharedViewModelFactory

class RegisterPage : Fragment() {

    private lateinit var phoneVerification: PhoneVerification
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: RegisterPageBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var registerNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        phoneVerification = PhoneVerification(requireActivity(), requireContext())
        binding = DataBindingUtil.inflate(inflater, R.layout.register_page, container, false)
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        registerViewModel =
            ViewModelProvider(
                requireActivity(),
            )[RegisterViewModel::class.java]
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onclickFunction()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onclickFunction() {
        binding.submitMobileNumber.setOnClickListener {
            registerNumber = binding.registerMobileNumberInput.text.toString()
            sharedViewModel.gotoRegisterPage(false)
            phoneVerification.sendVerification(
                registerNumber,
                sharedViewModel,
            )
        }
    }
}
