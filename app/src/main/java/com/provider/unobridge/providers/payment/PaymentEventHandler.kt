package com.provider.unobridge.providers.payment

import com.razorpay.PaymentResultListener

class PaymentEventHandler : PaymentResultListener {

    private var paymentResultListener: PaymentResultListener? = null

    fun setPaymentResultListener(paymentResultListener: PaymentResultListener){
        this.paymentResultListener = paymentResultListener
    }

    override fun onPaymentSuccess(p0: String?) {
        paymentResultListener?.onPaymentSuccess(p0)
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        paymentResultListener?.onPaymentError(p0,p1)
    }

}