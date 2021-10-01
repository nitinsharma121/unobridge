package com.provider.unobridge.providers.firebasePhoneAuth

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import java.util.concurrent.TimeUnit

 class FirebaseAuthPhoneImpl : FirebaseAuthPhone {
    var auth = FirebaseAuth.getInstance()
    lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    lateinit var baseActivity: AppCompatActivity
     override var firebaseAuthResult: FirebaseAuthResult? = null
     override fun disconnect() {

    }

    override fun sendOtp(mobileNo: String, countryCode: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("$countryCode $mobileNo") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(baseActivity)                 // Activity (for callback binding)
            .setCallbacks(callbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }


    override fun signInUser(credentials: PhoneAuthCredential) {
        auth.signInWithCredential(credentials)
            .addOnCompleteListener(baseActivity) { task ->
                if (task.isSuccessful) {
                    val user = task.result?.user
                    firebaseAuthResult?.signInResult(SignInResult.OnSuccess(user = user))

                } else {
                    firebaseAuthResult?.signInResult(SignInResult.OnFailure(message = "Login in Failed!!, Please try again"))

                }
            }
    }

    override fun setActivity(activity: AppCompatActivity) {
        baseActivity = activity
        auth.setLanguageCode("en")
        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {

            }

            override fun onVerificationFailed(e: FirebaseException) {
                firebaseAuthResult?.onCodeSentResult(CodeSentStatus.OnFailure(message = "Something went wrong!!, Please try again"))

            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                firebaseAuthResult?.onCodeSentResult(
                    CodeSentStatus.OnSuccess(
                        verificationId = verificationId,
                        token = token
                    )
                )
            }
        }
    }


}