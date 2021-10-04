package com.provider.unobridge.ui.fragments.profile.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResult
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseBottomSheetDialogFragment
import com.provider.unobridge.databinding.OrderDetailsFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.OrdersData
import org.kodein.di.KodeinAware


class OrderDetailsFragment : BaseBottomSheetDialogFragment(), KodeinAware {

    override val kodein by lazy { (context?.applicationContext as KodeinAware).kodein }
    private lateinit var mBinding: OrderDetailsFragmentBinding
    var requestCode: Int = 0
    var ordersData = OrdersData()

    override val isFullScreen: Boolean
        get() = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.BottomSheetStyle)
        mBinding = OrderDetailsFragmentBinding.inflate(inflater, container, false).apply {
            clickHandler = ClickHandler()
        }
        mBinding.ivBack.setOnClickListener {
            dismiss()
        }

        ordersData = arguments?.get(getString(R.string.orders)) as OrdersData
        ordersData?.let {
            mBinding.tvCustomerName.text = "Customer Name : ${ordersData.customerName}"
            mBinding.tvEndDate.text = "End Date : ${ordersData.endDate}"
            mBinding.tvPrice.text = "Price : ₹ ${ordersData.orderPrice}"
        }

        return mBinding.root
    }


    inner class ClickHandler {
        var bundle = Bundle()

        fun onClickAdd() {
            ordersData.isActive = false
            ordersData.paymentLink = "https://tvpaymentt/ll"
            ordersData.reviewFeedbackLink = "https://feedback/ll"
            ordersData.invoiceLink = "https://invoice/ll"
            CalculatorDatabase.getDatabase(requireContext()).getOrdersDao()
                .addOrder(ordersData)
            setFragmentResult(getString(R.string.orders), Bundle())
            dismiss()


        }

    }


}