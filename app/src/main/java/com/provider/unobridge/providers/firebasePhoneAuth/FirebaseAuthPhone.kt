package com.provider.unobridge.providers.firebasePhoneAuth

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.PhoneAuthCredential

interface FirebaseAuthPhone {
    var firebaseAuthResult: FirebaseAuthResult?
    fun disconnect()
    fun sendOtp(mobileNo: String, countryCode: String)
    fun signInUser(credentials: PhoneAuthCredential)
    fun setActivity(activity: AppCompatActivity)
}
