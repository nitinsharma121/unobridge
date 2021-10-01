package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.databinding.AddProfileImagesFragmentBinding
import org.kodein.di.KodeinAware


class AddProfileImageFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: AddProfileImagesFragmentBinding
    var requestCode: Int = 0

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = AddProfileImagesFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }

        if (arguments?.getInt(getString(R.string.request_code)) != null) {
            requestCode = arguments?.getInt(getString(R.string.request_code))!!
        }

        return mBinding.root
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickGallery() {
            dismiss()
            parentFragmentManager.setFragmentResult(
                getString(R.string.image_pick_mode),
                bundleOf(
                    getString(R.string.request_code) to requestCode,
                    getString(R.string.mode) to getString(R.string.gallery),
                )
            )
        }

        fun onClickCamera() {
            dismiss()
            parentFragmentManager.setFragmentResult(
                getString(R.string.image_pick_mode),
                bundleOf(
                    getString(R.string.request_code) to requestCode,
                    getString(R.string.mode) to getString(R.string.camera),
                )
            )
        }

    }


}