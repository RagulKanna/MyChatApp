package com.example.chatapp.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatapp.R
import com.example.chatapp.model.Constant
import com.example.chatapp.model.SharedPreference
import com.example.chatapp.service.FireBaseService
import com.example.chatapp.viewmodel.SharedViewModel
import com.example.chatapp.viewmodel.SharedViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OtpVerificationPage : Fragment() {

    private lateinit var phoneNumber: String
    private lateinit var verificationId: String
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var progressBar: ProgressBar
    private lateinit var verifyOtpButton: Button
    private lateinit var resendOtp: TextView
    private lateinit var codeInput1: EditText
    private lateinit var codeInput2: EditText
    private lateinit var codeInput3: EditText
    private lateinit var codeInput4: EditText
    private lateinit var codeInput5: EditText
    private lateinit var codeInput6: EditText
    private lateinit var fireBaseService: FireBaseService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.otp_verification_page, container, false)
        fireBaseService = FireBaseService(requireContext())
        codeInput1 = view.findViewById(R.id.inputCode1)
        codeInput2 = view.findViewById(R.id.inputCode2)
        codeInput3 = view.findViewById(R.id.inputCode3)
        codeInput4 = view.findViewById(R.id.inputCode4)
        codeInput5 = view.findViewById(R.id.inputCode5)
        codeInput6 = view.findViewById(R.id.inputCode6)
        resendOtp = view.findViewById(R.id.resend_otp)
        verifyOtpButton = view.findViewById(R.id.verifyButton)
        progressBar = view.findViewById(R.id.progress_bar)
        progressBar.isIndeterminate = true
        progressBar.visibility = View.INVISIBLE
        sharedViewModel =
            ViewModelProvider(
                requireActivity(),
                SharedViewModelFactory()
            )[SharedViewModel::class.java]
        SharedPreference.initSharedPreference(requireContext())
        phoneNumber = SharedPreference.get(Constant.PHONE_NUMBER)
        verificationId = SharedPreference.get(Constant.VERIFICATION_ID)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onClickVerifyButton()
        setupOtpInputs()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun onClickVerifyButton() {
        verifyOtpButton.setOnClickListener {
            otpVerify(verificationId)
            onClickResendText()
        }
    }

    private fun onClickResendText() {
        resendOtp.setOnClickListener {
            fireBaseService.phoneVerification(requireActivity(), phoneNumber, sharedViewModel)
        }
    }

    private fun setupOtpInputs() {
        codeInput1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    codeInput2.requestFocus()
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        codeInput2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    codeInput3.requestFocus()
                codeInput2.setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                        if (p0.toString().trim().isEmpty() && keyCode == KeyEvent.KEYCODE_DEL) {
                            codeInput1.requestFocus()
                        }
                        return false
                    }
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        codeInput3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (!p0.toString().trim().isEmpty())
                    codeInput4.requestFocus()
                codeInput3.setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                        if (p0.toString().trim().isEmpty() && keyCode == KeyEvent.KEYCODE_DEL) {
                            codeInput2.requestFocus()
                        }
                        return false
                    }
                })
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
        codeInput4.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!p0.toString().trim().isEmpty())
                        codeInput5.requestFocus()
                    codeInput4.setOnKeyListener(object : View.OnKeyListener {
                        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                            if (p0.toString().trim().isEmpty() && keyCode == KeyEvent.KEYCODE_DEL) {
                                codeInput3.requestFocus()
                            }
                            return false
                        }
                    })
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })
        codeInput5.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!p0.toString().trim().isEmpty())
                        codeInput6.requestFocus()
                    codeInput5.setOnKeyListener(object : View.OnKeyListener {
                        override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                            if (p0.toString().trim().isEmpty() && keyCode == KeyEvent.KEYCODE_DEL) {
                                codeInput4.requestFocus()
                            }
                            return false
                        }
                    })
                }

                override fun afterTextChanged(p0: Editable?) {
                }
            })

        codeInput6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                codeInput6.setOnKeyListener(object : View.OnKeyListener {
                    override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                        if (keyCode == KeyEvent.KEYCODE_DEL && s.toString().trim().isEmpty()) {
                            codeInput5.requestFocus()
                        }
                        return false
                    }
                })
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun verifyValidOtp(): String? {
        if (codeInput1.text.toString().trim().isEmpty() || codeInput2.text.toString().trim()
                .isEmpty() ||
            codeInput3.text.toString().trim().isEmpty() || codeInput4.text.toString().trim()
                .isEmpty() ||
            codeInput5.text.toString().trim().isEmpty() || codeInput6.text.toString().trim()
                .isEmpty()
        ) {
            Toast.makeText(requireContext(), "Enter Valid Otp", Toast.LENGTH_SHORT).show()
        } else {
            return codeInput1.text.toString() + codeInput2.text.toString() +
                    codeInput3.text.toString() + codeInput4.text.toString() +
                    codeInput5.text.toString() + codeInput6.text.toString()
        }
        return null
    }

    private fun otpVerify(verificationId: String?) {
        progressBar.visibility = View.VISIBLE
        verifyOtpButton.visibility = View.INVISIBLE
        val otp = verifyValidOtp()
        val phoneAuthCredential: PhoneAuthCredential =
            PhoneAuthProvider.getCredential(verificationId!!, otp!!)
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
            .addOnSuccessListener {
                Toast.makeText(
                    requireContext(),
                    "Phone Number Verified Successfully",
                    Toast.LENGTH_SHORT
                )
                    .show()
                fireBaseService.checkForNewUser()
                sharedViewModel.gotoOtpPage(false)
                if (SharedPreference.get(Constant.CHECK_USER_FLAG) == "1") {
                    sharedViewModel.gotoChatListPage(true)
                } else {
                    sharedViewModel.gotoDetailsRegisterPage(true)
                }
            }.addOnFailureListener {
                progressBar.visibility = View.GONE
                verifyOtpButton.visibility = View.VISIBLE
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}