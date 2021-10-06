package com.provider.unobridge.ui.fragments.companyDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.provider.unobridge.R
import com.provider.unobridge.databinding.FragmentCompanyDetailsBinding

class CompanyDetailsFragment : Fragment() {

    private lateinit var mBinding: FragmentCompanyDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_company_details, container, false)
        return mBinding.root
    }
}