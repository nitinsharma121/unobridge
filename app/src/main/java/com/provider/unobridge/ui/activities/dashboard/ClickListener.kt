package com.provider.unobridge.ui.activities.dashboard

import android.view.View

interface ClickListener {
    fun openMenu(view: View)
    fun closeMenu(view: View)
    fun redirect(id: Int?)
    fun openWebsite(view: View)
    fun openGoogleSearch(view: View)
}