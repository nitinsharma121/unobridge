package com.provider.unobridge.ui.fragments.finance

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.base.Utils.Companion.openBrowser
import com.provider.unobridge.databinding.FragmentFinanceBinding


class FinanceFragment : Fragment() {
private lateinit var mBinding:FragmentFinanceBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_finance, container, false)
        mBinding.ivLoan.setOnClickListener {
            it.disableMultiTap()
            requireContext().openBrowser("https://www.yesbank.in/business-banking/msme-loans")
        }
        return mBinding.root
    }

}