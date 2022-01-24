package com.example.chatapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.databinding.RegisterNumberPageBinding
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.RegisterViewModel
import com.example.chatapp.viewmodel.RegisterViewModelFactory
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory

class RegisterPage : Fragment() {

    private lateinit var fireBaseService: FireBaseService
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var binding: RegisterNumberPageBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var registerNumber: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_number_page, container, false)
        fireBaseService = FireBaseService(requireContext())
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        registerViewModel =
            ViewModelProvider(
                requireActivity(),
                RegisterViewModelFactory()
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
            fireBaseService.phoneVerification(requireActivity(),
                registerNumber,
                sharedViewModel,
            )
        }
    }

}
