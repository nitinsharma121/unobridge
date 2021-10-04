package com.provider.unobridge.ui.fragments.profile.dialogs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.base.Utils
import com.provider.unobridge.databinding.AddEmployeeFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.EmployeesData
import com.provider.unobridge.room.entities.OrdersData
import org.kodein.di.KodeinAware


class AddEmployeeFragment : BaseBottomSheetDialogFragment(), KodeinAware, View.OnTouchListener {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: AddEmployeeFragmentBinding
    var requestCode: Int = 0

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = AddEmployeeFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }


        getCustomersList()
        mBinding.etCustomer.setOnTouchListener(this)

        return mBinding.root
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        event?.let { motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                v?.let { view ->
                    Utils.init.hideKeyBoard(requireContext(), mBinding.root)

                    when (view.id) {
                        R.id.etCustomer -> (view as AutoCompleteTextView).showDropDown()
                    }
                }
            }
        }
        return false
    }

    fun getCustomersList() {
        var list = CalculatorDatabase.getDatabase(requireContext()).getCustomersDao().getCustomers()
        var customers = ArrayList<String>()
        for (item in list) {
            customers.add(item.name.toString())
        }
        mBinding.etCustomer.setAdapter(
            ArrayAdapter(
                requireContext(),
                R.layout.simple_spinner_dropdown_item,
                R.id.tvDropdown,
                customers

            )
        )
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickAdd() {
            if (mBinding.etName.text.isNotEmpty() && mBinding.etAadhar.text.isNotEmpty() && mBinding.etAddress.text.isNotEmpty() && mBinding.etCustomer.text.isNotEmpty() && mBinding.etContact.text.isNotEmpty()) {
                var ordersData = EmployeesData()
                ordersData.customer = mBinding.etCustomer.text.toString()
                ordersData.name = mBinding.etName.text.toString()
                ordersData.address = mBinding.etAddress.text.toString()
                ordersData.contactNo = mBinding.etContact.text.toString()
                ordersData.aadharNo = mBinding.etAadhar.text.toString()
                CalculatorDatabase.getDatabase(requireContext()).getEmployesDao().addEmployee(ordersData)
                setFragmentResult(getString(R.string.employees), Bundle())
                dismiss()


            }


        }

    }


}