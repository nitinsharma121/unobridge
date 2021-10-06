package com.provider.unobridge.ui.fragments.order.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.FragmentAddOrderBinding


class AddOrderFragment : Fragment() {
    private lateinit var mBinding: FragmentAddOrderBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_order, container, false)
        mBinding.btnSaveChanges.setOnClickListener {
            it.disableMultiTap()
            findNavController().navigate(R.id.completeOrderFragment)
        }
        return mBinding.root
    }
}