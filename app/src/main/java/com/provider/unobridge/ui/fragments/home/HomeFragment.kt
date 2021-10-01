package com.provider.unobridge.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.databinding.HomeFragmentBinding
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModel
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class HomeFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: HomeFragmentBinding
    private val viewModelFactory: AccountViewModelFactory by instance()
    lateinit var mViewModel: AccountViewModel
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(AccountViewModel::class.java)
            mBinding = HomeFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
            mBinding.shimmerFrameLayout.startShimmer()
            mViewModel.getStatesList()
        }
        setupObserver()
        return mBinding.root
    }

    private fun setupObserver() {

        mViewModel.apply {
            statesList.observe(viewLifecycleOwner, Observer {

                mBinding.shimmerFrameLayout.stopShimmer()
                mBinding.shimmerFrameLayout.visibility = View.GONE
                mBinding.rvStates.visibility = View.VISIBLE

                mBinding.rvStates.adapter = statesAdapter
                it.data?.responseStates?.let { it1 -> statesAdapter.setNewItems(it1) }
                statesAdapter.listenerState = this@HomeFragment.ClickHandler()
            })
            showLoading.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    showProgress()
                } else hideProgress()
            })

            showMessage.observe(viewLifecycleOwner, Observer {
                hideProgress()
                if (!it.isNullOrEmpty()) {
                    showToast(it)
                }

            })
        }

    }


    inner class ClickHandler : StatesAdapter.clickListenerState {
        fun viewAllRides() {
            findNavController().navigate(
                R.id.rides_fragment,
                bundleOf(getString(R.string.state_name) to getString(R.string.all_states))
            )
        }

        override fun onClickState(stateName: String, position: Int) {
            findNavController().navigate(
                R.id.rides_fragment,
                bundleOf(getString(R.string.state_name) to "${stateName} Rides")
            )

        }

        fun goToAccount() {
            findNavController().navigate(R.id.account_fragment)
        }

        fun addRide() {
            findNavController().navigate(R.id.add_ride_fragment)
        }


    }


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}