package com.provider.unobridge.providers.firebasePhoneAuth

import arrow.optics.optics
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthProvider


@optics
sealed class CodeSentStatus {

    companion object
    @optics
    data class OnSuccess(
        var verificationId: String?,
        var token: PhoneAuthProvider.ForceResendingToken?
    ) : CodeSentStatus(){
        companion object
    }

    @optics
    data class OnFailure(var message: String) : CodeSentStatus(){
        companion object
    }
}

@optics
sealed class SignInResult {
    companion object

    @optics
    data class OnSuccess(
        val user: FirebaseUser?
    ) : SignInResult(){
        companion object
    }

    @optics
    data class OnFailure(var message: String) : SignInResult(){
        companion object
    }
}