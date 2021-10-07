package com.provider.unobridge.ui.fragments.dashboard

import android.view.View

interface ClickListener {
    fun onSetupComplete(view: View)
    fun previewWebsite(view: View)
    fun addOffer(view: View)
    fun uploadImage(view: View)
    fun addVideo(view: View)
}