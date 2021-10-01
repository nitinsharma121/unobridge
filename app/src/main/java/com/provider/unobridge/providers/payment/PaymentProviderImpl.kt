package com.provider.unobridge.providers.payment

import android.app.Activity
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.provider.unobridge.R
import com.provider.unobridge.ui.activities.MainActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class PaymentProviderImpl(private val context: Context, private val activity: Activity) :
    PaymentProvider {


    override fun setPaymentListener(paymentResultListener: PaymentResultListener) {
        (activity as MainActivity).paymentEventHandler.setPaymentResultListener(
            paymentResultListener
        )
    }

    override fun initPayment() {
        Checkout.preload(context)
    }

    override fun startPayment(paymentParams: PaymentParams) {
        val co = Checkout()
        co.setKeyID(context.getString(R.string.razor_pay_key))

        try {
            Log.e("PaymentParams", JSONObject(Gson().toJson(paymentParams)).toString())
            co.open(activity, JSONObject(Gson().toJson(paymentParams)))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun clearUserData() {
        Checkout.clearUserData(context);
    }

}

