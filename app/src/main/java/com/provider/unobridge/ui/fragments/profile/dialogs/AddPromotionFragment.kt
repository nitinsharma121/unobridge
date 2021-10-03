package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.base.Utils
import com.provider.unobridge.databinding.AddPromotoinFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.PromotoinsData
import org.kodein.di.KodeinAware


class AddPromotionFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: AddPromotoinFragmentBinding
    var requestCode: Int = 0

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = AddPromotoinFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }


        mBinding.etExpiry.setOnClickListener {
            Utils.init.selectDate(
                requireContext(),
                "",
                mBinding.etExpiry,
                false
            )
        }
        return mBinding.root
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickAdd() {

            if (mBinding.etName.text.isNotEmpty() && mBinding.etExpiry.text.isNotEmpty()) {
                var data = PromotoinsData()
                data.expireDate = mBinding.etExpiry.text.toString()
                data.discountValue = mBinding.etName.text.toString()
                CalculatorDatabase.getDatabase(requireContext()).getPromotoinsDao()
                    .addPromotoin(data)

                setFragmentResult(getString(R.string.promotoin),Bundle())
                dismiss()
            }


        }

    }


}