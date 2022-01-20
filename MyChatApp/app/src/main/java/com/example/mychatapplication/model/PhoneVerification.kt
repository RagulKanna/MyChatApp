package com.example.mychatapplication.model

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.mychatapplication.viewmodel.SharedViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


@Suppress("DEPRECATION")
class PhoneVerification(private val activity: Activity, private val context: Context) {

    private lateinit var firebaseAuth: FirebaseAuth
    fun sendVerification(
        phoneNumber: String,
        sharedViewModel: SharedViewModel,
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
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
                    Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                }
            })
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}