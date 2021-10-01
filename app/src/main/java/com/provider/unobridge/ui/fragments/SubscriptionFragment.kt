package com.provider.unobridge.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.databinding.SubscriptionFragmentBinding
import org.kodein.di.KodeinAware


class SubscriptionFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: SubscriptionFragmentBinding

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = SubscriptionFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }


        return mBinding.root
    }


    inner class ClickHandler {

        fun onSubscribe() {

        }

    }


}