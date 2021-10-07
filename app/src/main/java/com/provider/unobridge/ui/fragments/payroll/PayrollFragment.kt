package com.provider.unobridge.ui.fragments.payroll

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentPayrollBinding


class PayrollFragment : Fragment() {

    private lateinit var mBinding: FragmentPayrollBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payroll, container, false)
        mBinding.btnSaveChanges.setOnClickListener {
            it.disableMultiTap()
            if (mBinding.btnSaveChanges.text == getString(R.string.save_changes))
                mBinding.btnSaveChanges.text = getString(R.string.txt_done)
            else findNavController().navigate(R.id.action_payrollFragment_to_summaryFragment)
        }
        return mBinding.root
    }


}