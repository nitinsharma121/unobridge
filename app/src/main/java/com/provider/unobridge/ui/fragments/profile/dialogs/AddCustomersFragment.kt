package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.databinding.AddCustomersFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.CustomersData
import org.kodein.di.KodeinAware


class AddCustomersFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: AddCustomersFragmentBinding
    var requestCode: Int = 0
    val homeAdapter = CustomersAdapter(R.layout.item_contact)


    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = AddCustomersFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }

        setUpAdapter()

        return mBinding.root
    }

    fun setUpAdapter() {
        val list = ArrayList<CustomersData>()
        list.add(CustomersData(0, "Mahesh", "78529615433", true))
        list.add(CustomersData(0, "Hitesh", "7519615433"))
        list.add(CustomersData(0, "Ravi", "994453232654"))
        list.add(CustomersData(0, "Mitesh", "8523694107"))
        list.add(CustomersData(0, "Kailash", "6589741230"))

        mBinding.rvCustomers.adapter = homeAdapter
        homeAdapter.setNewItems(list)
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickAdd() {
            for (element in homeAdapter.list) {
                if (element.isSelected) {
                    var customerData = CustomersData()
                    customerData.mobileNo = element.mobileNo
                    customerData.name = element.name
                    CalculatorDatabase.getDatabase(requireContext()).getCustomersDao()
                        .addCustomer(customerData)
                    break
                }
            }
            setFragmentResult(getString(R.string.customers), Bundle())
            dismiss()
        }

    }


}