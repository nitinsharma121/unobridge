package com.provider.unobridge.providers.payment

import com.razorpay.PaymentResultListener

interface PaymentProvider {
    fun initPayment()
    fun startPayment(paymentParams: PaymentParams)
    fun clearUserData()
    fun setPaymentListener(paymentResultListener: PaymentResultListener)


}