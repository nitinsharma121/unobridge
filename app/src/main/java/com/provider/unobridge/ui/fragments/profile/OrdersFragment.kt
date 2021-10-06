package com.provider.unobridge.ui.fragments.profile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.Prefs
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.base.Utils.Companion.disableMultiTap
import com.provider.unobridge.databinding.OrdersFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.OrdersData
import com.provider.unobridge.room.entities.ProfileData
import com.provider.unobridge.ui.activities.AuthHandlerActivity
import com.provider.unobridge.ui.activities.mainActivity.MainActivity
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModel
import com.provider.unobridge.ui.fragments.auth.viewModel.LoginViewModelFactory
import com.provider.unobridge.ui.fragments.profile.dialogs.OrdersAdapter
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance


class OrdersFragment : ScopedFragment(), KodeinAware {
    lateinit var mBinding: OrdersFragmentBinding
    override val kodein by lazy { (activity?.applicationContext as KodeinAware).kodein }
    private val viewModelFactory: LoginViewModelFactory by instance()
    lateinit var mViewModel: LoginViewModel
    var isEditingMode = true
    var profileData = ProfileData()
    var ordersAdapter = OrdersAdapter(R.layout.item_order)


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        if (!::mBinding.isInitialized) {
            mViewModel =
                ViewModelProvider(this, viewModelFactory).get(LoginViewModel::class.java)
            mBinding = OrdersFragmentBinding.inflate(inflater, container, false).apply {
                clickHandler = ClickHandler()
            }
        }

        setUpUI()

        return mBinding.root
    }

    private fun setUpUI() {
        val list = ArrayList<OrdersData>()
        mBinding.rvJobs.adapter = ordersAdapter
        for (i in 0..5) {
            list.add(OrdersData())
        }
        ordersAdapter.setNewItems(list)

    }


    suspend fun roomFun(): Long {
        return CalculatorDatabase.getDatabase(requireContext()).getProfileDao()
            .saveData(profileData)
    }

    private fun myFun() {
        lifecycleScope.launch { // coroutine on Main
            val data = roomFun() // coroutine on IO
            Log.e("Idddd", "${data}")
            Prefs.init().userId = data.toString()
            Prefs.init().isProfileCompleted = "1"
            (requireActivity() as AuthHandlerActivity).finish()
            startActivity(Intent(requireActivity(), MainActivity::class.java))

        }
    }

    inner class ClickHandler {
        fun addOrder(view: View) {
            view.disableMultiTap()
            findNavController().navigate(R.id.addOrderFragment)
        }

    }


    override fun stopBlur() {

    }

    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }


}