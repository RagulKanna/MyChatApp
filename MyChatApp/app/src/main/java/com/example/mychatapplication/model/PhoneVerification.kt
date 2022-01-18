package com.example.mychatapplication.model

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mychatapplication.R
import com.example.mychatapplication.view.OtpVerificationPage
import com.example.mychatapplication.viewmodel.SharedViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
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
                    val bundle = Bundle()
                    bundle.putString("phoneNo", phoneNumber)
                    bundle.putString("verificationId", codeForUser)
                    val verifyOtp = OtpVerificationPage()
                    verifyOtp.arguments = bundle
                    sharedViewModel.gotoRegisterPage(false)
                    (activity as AppCompatActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, verifyOtp).commit()
                    super.onCodeSent(codeForUser, phoneAuthProvider)
                }

                override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {

                }

                override fun onVerificationFailed(p0: FirebaseException) {
                    Toast.makeText(context, "Verification Failed", Toast.LENGTH_SHORT).show()
                }

            })          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

}