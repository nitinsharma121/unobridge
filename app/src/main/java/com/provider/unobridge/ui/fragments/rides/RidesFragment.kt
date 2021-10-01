package com.provider.unobridge.ui.fragments.rides

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.RideMessage
import com.provider.unobridge.databinding.RidesFragmentBinding


class RidesFragment : ScopedFragment() {
    lateinit var mBinding: RidesFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mBinding = RidesFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
            mBinding.tvStateTitle.text = arguments?.getString(getString(R.string.state_name))

            setUpAdapter()
        }
        return mBinding.root
    }


    private fun setUpAdapter() {
        // Initializing the ViewPagerAdapter
        // Initializing the ViewPagerAdapter
        var list = ArrayList<RideMessage>()
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))
        list.add(RideMessage(""))


        var ridesAdapter = RidesAdapter(R.layout.rides_message_item)
        ridesAdapter.setNewItems(list)
        mBinding.rvRides.adapter = ridesAdapter


    }

    inner class ClickHandler {

        fun backPress() {
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