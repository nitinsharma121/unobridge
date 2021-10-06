package com.provider.unobridge.ui.fragments.customizeWebsite

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentCustomizeWebsiteBinding


class CustomizeWebsiteFragment : Fragment() {
    private lateinit var mBinding: FragmentCustomizeWebsiteBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_customize_website, container, false)
        setupWebView()
        mBinding.ivMenu.setOnClickListener {
            it.disableMultiTap()
            val url = getString(R.string.raju_electrician_website)
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            startActivity(i)
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