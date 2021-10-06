package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentAddOfferBinding


class AddOfferFragment : BaseBottomSheetDialogFragment() {
    private lateinit var mBinding: FragmentAddOfferBinding
    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_offer, container, false)

        mBinding.ivBack.setOnClickListener {
            it.disableMultiTap()
            dismiss()
        }

        mBinding.btnAdd.setOnClickListener {
            it.disableMultiTap()
            dismiss()
        }
        return mBinding.root
    }
}