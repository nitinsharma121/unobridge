package com.provider.unobridge.ui.fragments.googleSearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentGoogleSearchBinding


class GoogleSearchFragment : Fragment() {
    private lateinit var mBinding: FragmentGoogleSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_google_search, container, false)
        setupWebView()
        mBinding.vClickable.setOnClickListener {
            it.disableMultiTap()
            findNavController().navigate(R.id.googleSearchResultFragment)
        }
        return mBinding.root
    }

    private fun setupWebView() {
        mBinding.vwWebsite.apply {
            loadUrl("https://google.com")
            settings.javaScriptEnabled = true
            webViewClient = WebViewClient()
        }
    }
}