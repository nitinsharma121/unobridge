package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.databinding.AddJobFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.PromotoinsData
import org.kodein.di.KodeinAware


class AddJobFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: AddJobFragmentBinding
    var requestCode: Int = 0

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = AddJobFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }


        return mBinding.root
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickAdd() {

            /*if (mBinding.etJobTitle.text.isNotEmpty() && mBinding.etJobDescription.text.isNotEmpty()) {
                var data = PromotoinsData()
                data.description = mBinding.etJobDescription.text.toString()
                data.title = mBinding.etJobTitle.text.toString()
                data.type = "Job"
                CalculatorDatabase.getDatabase(requireContext()).getPromotoinsDao()
                    .addPromotoin(data)

                setFragmentResult(getString(R.string.promotoin), Bundle())
            }*/
            dismiss()


        }

    }


}