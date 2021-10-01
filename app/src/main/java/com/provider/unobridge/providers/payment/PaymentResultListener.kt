package com.provider.unobridge.providers.payment

interface PaymentResultListener {
    fun onSuccess(result: String?)
    fun onFailure(result: String?)
}