package com.provider.unobridge.ui.fragments.googleSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.base.Utils.Companion.openBrowser
import com.provider.unobridge.databinding.FragmentGoogleSeachResultBinding
import com.provider.unobridge.databinding.FragmentGoogleSearchBinding


class GoogleSearchResultFragment : Fragment() {
    private lateinit var mBinding: FragmentGoogleSeachResultBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_google_seach_result,
                container,
                false
            )
        mBinding.ivMock1.setOnClickListener {
            it.disableMultiTap()
            requireContext().openBrowser(getString(R.string.raju_electrician_website))
        }
        return mBinding.root
    }

}