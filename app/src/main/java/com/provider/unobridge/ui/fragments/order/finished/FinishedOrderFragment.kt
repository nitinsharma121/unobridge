package com.provider.unobridge.ui.fragments.order.finished

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.databinding.FragmentFinishedOrderBinding


class FinishedOrderFragment : Fragment() {
    private lateinit var mBinding: FragmentFinishedOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_finished_order, container, false)
        mBinding.ivBody.setOnClickListener {
            findNavController().navigateUp()
        }
        return mBinding.root
    }
}