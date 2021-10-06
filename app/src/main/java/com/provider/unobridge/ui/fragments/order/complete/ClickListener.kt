package com.provider.unobridge.ui.fragments.order.complete

import android.view.View

interface ClickListener {

    fun onPaymentLink(view:View)
    fun onReceiveFeedback(view:View)
    fun onInvoice(view:View)
    fun onShareViaWhatsApp(view:View)

}