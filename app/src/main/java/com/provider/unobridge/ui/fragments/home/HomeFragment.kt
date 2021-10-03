package com.provider.unobridge.ui.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.StateData
import com.provider.unobridge.databinding.HomeFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModel
import com.provider.unobridge.ui.fragments.account.viewModel.AccountViewModelFactory
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class HomeFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: HomeFragmentBinding
    private val viewModelFactory: AccountViewModelFactory by instance()
    lateinit var mViewModel: AccountViewModel
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    val statesAdapter = StatesAdapter(R.layout.state_item)


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
        }
        setupObserver()
        setupAdapter()

        return mBinding.root
    }

    private fun setupObserver() {


    }

    fun setupAdapter() {
        val homeAdapter = StatesAdapter(R.layout.state_item)
        val list = ArrayList<StateData>()
        list.add(StateData(R.drawable.ic_sites, getString(R.string.sites)))
        list.add(StateData(R.drawable.ic_baseline_content_paste_24, getString(R.string.content)))
        list.add(StateData(R.drawable.ic_customers, getString(R.string.customers)))
        list.add(StateData(R.drawable.ic_orders, getString(R.string.orders)))
        list.add(StateData(R.drawable.ic_employee, getString(R.string.employees)))
        list.add(StateData(R.drawable.icreulatory, getString(R.string.regulartory)))
        list.add(
            StateData(
                R.drawable.ic_baseline_local_grocery_store_24,
                getString(R.string.store)
            )
        )
        list.add(StateData(R.drawable.ic_baseline_attach_money_24, getString(R.string.money)))
        mBinding.rvStates.adapter = homeAdapter
        homeAdapter.setNewItems(list)
        homeAdapter.listenerState = ClickHandler()

    }


    inner class ClickHandler : StatesAdapter.clickListenerState {
        override fun onClickState(stateName: String, position: Int) {
            findNavController().navigate(
                R.id.add_ride_fragment,
                bundleOf(getString(R.string.user_type) to stateName)
            )

        }
    }


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}