package com.provider.unobridge.ui.fragments.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.NotificationsFragmentBinding


class NotificationsFragment : ScopedFragment() {
    lateinit var mBinding: NotificationsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mBinding = NotificationsFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }
        return mBinding.root
    }


    inner class ClickHandler


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}