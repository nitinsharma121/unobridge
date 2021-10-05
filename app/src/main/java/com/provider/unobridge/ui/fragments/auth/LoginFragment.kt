package com.provider.unobridge.ui.fragments.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.FragmentLoginBinding


/**
 * Created by Nitin SHarma on 9/4/2021.
 */
class LoginFragment : ScopedFragment() {

    lateinit var mBinding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        mBinding = FragmentLoginBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        hideKeyboard(mBinding.root)
        return mBinding.root
    }

    inner class ClickHandler {
        fun getStarted() {
            findNavController().popBackStack()
            findNavController().navigate(R.id.enter_mobile_fragment)
        }
    }

    override fun stopBlur() {

    }
}