package com.provider.unobridge.ui.fragments.customizeWebsite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.base.Utils.Companion.openBrowser
import com.provider.unobridge.databinding.FragmentUpdateWebsiteBinding


class UpdateWebsiteFragment : Fragment() {
    private lateinit var mBinding: FragmentUpdateWebsiteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_update_website, container, false)
        setupWebView()
        mBinding.ivMenu.setOnClickListener {
            it.disableMultiTap()
            requireContext().openBrowser(getString(R.string.raju_electrician_website))
        }
        return mBinding.root
    }

    private fun setupWebView() {
        mBinding.vwWebsite.apply {
            loadUrl(getString(R.string.raju_electrician_website))
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }
}