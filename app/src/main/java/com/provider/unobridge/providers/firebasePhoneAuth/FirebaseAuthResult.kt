package com.provider.unobridge.providers.firebasePhoneAuth

interface FirebaseAuthResult {
    fun onCodeSentResult(status: CodeSentStatus)
    fun signInResult(result: SignInResult)
}