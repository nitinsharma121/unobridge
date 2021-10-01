package com.provider.unobridge.ui.fragments.addRides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.AddRideFragmentBinding


class AddRideFragment : ScopedFragment() {
    lateinit var mBinding: AddRideFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mBinding = AddRideFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }
        return mBinding.root
    }


    inner class ClickHandler {
        fun onBackPress() {
            findNavController().navigateUp()
        }

    }


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}