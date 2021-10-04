package com.provider.unobridge.ui.fragments.addRides

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.findNavController
import com.provider.unobridge.R
import com.provider.unobridge.base.ScopedFragment
import com.provider.unobridge.data.SitesData
import com.provider.unobridge.databinding.AddRideFragmentBinding
import com.provider.unobridge.room.CalculatorDatabase
import com.provider.unobridge.room.entities.OrdersData
import com.provider.unobridge.ui.fragments.profile.dialogs.*


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
            setupUI()
            setUPAdapters()
        }

        listenFragmentResult()

        return mBinding.root
    }

    fun listenFragmentResult() {
        setFragmentResultListener(
            getString(R.string.promotoin)
        ) { key, bundle ->
            setContentData()
        }

        setFragmentResultListener(
            getString(R.string.customers)
        ) { key, bundle ->
            setCustomersData()
        }


        setFragmentResultListener(
            getString(R.string.orders)
        ) { key, bundle ->
            seOrdersData()
        }

        setFragmentResultListener(
            getString(R.string.employees)
        ) { key, bundle ->
            setEmployeesData()
        }
    }

    fun setupUI() {
        when (arguments?.getString(getString(R.string.user_type))) {
            getString(R.string.sites) -> {
                mBinding.tvTitle.text = "List Of Sites"
            }
            getString(R.string.content) -> {
                mBinding.tvTitle.text = "List Of Content"

            }
            getString(R.string.customers) -> {
                mBinding.tvTitle.text = "List Of Customers"

            }
            getString(R.string.orders) -> {
                mBinding.tvTitle.text = "List Of Orders"

            }
            getString(R.string.employees) -> {
                mBinding.tvTitle.text = "List Of Employeess"
            }
            getString(R.string.regulartory) -> {
                mBinding.tvTitle.text = "List Of Regulatory"
            }
            getString(R.string.store) -> {
                mBinding.tvTitle.text = "List Of Stores"
            }
            getString(R.string.money) -> {
            }


        }
    }


    fun setContentData() {
        val promotoinsAdapter = PromotoinsAdapter(R.layout.item_contact)
        mBinding.rvData.adapter = promotoinsAdapter
        val list = CalculatorDatabase.getDatabase(requireContext()).getPromotoinsDao()
            .getPromotoins()

        if (list.isEmpty()) {
            mBinding.tvNoData.visibility = VISIBLE
            mBinding.rvData.visibility = GONE
        } else {
            mBinding.tvNoData.visibility = GONE
            mBinding.rvData.visibility = VISIBLE
            promotoinsAdapter.setNewItems(list)

        }
    }

    fun setCustomersData() {
        val customersAdapter = CustomersAdapter(R.layout.item_contact)
        mBinding.rvData.adapter = customersAdapter
        val list = CalculatorDatabase.getDatabase(requireContext()).getCustomersDao()
            .getCustomers()
        if (list.isEmpty()) {
            mBinding.tvNoData.visibility = VISIBLE
        } else {
            mBinding.tvNoData.visibility = GONE
            customersAdapter.setNewItems(list)

        }
    }

    fun setEmployeesData() {
        val customersAdapter = EmployeesAdapter(R.layout.item_employee)
        mBinding.rvData.adapter = customersAdapter
        val list = CalculatorDatabase.getDatabase(requireContext()).getEmployesDao()
            .getEmployees()
        if (list.isEmpty()) {
            mBinding.tvNoData.visibility = VISIBLE
        } else {
            mBinding.tvNoData.visibility = GONE
            customersAdapter.setNewItems(list)

        }
    }


    fun seOrdersData() {
        val customersAdapter = OrdersAdapter(R.layout.item_order)
        mBinding.rvData.adapter = customersAdapter
        customersAdapter.listener = this.ClickHandler()
        val list = CalculatorDatabase.getDatabase(requireContext()).getOrdersDao()
            .getOrders()
        if (list.isEmpty()) {
            mBinding.tvNoData.visibility = VISIBLE
        } else {
            mBinding.tvNoData.visibility = GONE
            customersAdapter.setNewItems(list)

        }
    }


    fun setUPAdapters() {
        when (arguments?.getString(getString(R.string.user_type))) {
            getString(R.string.sites) -> {
                val sitesAdapter = SitesAdapter(R.layout.site_item)
                mBinding.rvData.adapter = sitesAdapter
                val list = ArrayList<SitesData>()
                list.add(SitesData("Website 1", "https://abc.com/ui"))
                list.add(SitesData("Website 2", "https://xyz.com/ui"))
                sitesAdapter.setNewItems(list)

                sitesAdapter.listener = this.ClickHandler()


            }
            getString(R.string.content) -> {

                setContentData()

            }
            getString(R.string.customers) -> {
                setCustomersData()

            }
            getString(R.string.orders) -> {
                seOrdersData()

            }
            getString(R.string.employees) -> {
                setEmployeesData()
            }
            getString(R.string.regulartory) -> {
            }
            getString(R.string.store) -> {
            }
            getString(R.string.money) -> {
            }


        }
    }

    inner class ClickHandler : SitesAdapter.onClick, OrdersAdapter.onClick {
        fun onBackPress() {
            findNavController().navigateUp()
        }

        fun onClickAdd() {
            when (arguments?.getString(getString(R.string.user_type))) {
                getString(R.string.sites) -> {

                }
                getString(R.string.content) -> {
                    openContentPicker()
                }
                getString(R.string.customers) -> {
                    findNavController().navigate(R.id.add_customers_fragment)

                }
                getString(R.string.orders) -> {
                    findNavController().navigate(R.id.add_order_fragment)
                }
                getString(R.string.employees) -> {
                    findNavController().navigate(R.id.add_employee_fragment)
                }
                getString(R.string.regulartory) -> {
                }
                getString(R.string.store) -> {
                }
                getString(R.string.money) -> {
                }


            }
        }

        override fun onClickItem() {
            findNavController().navigate(R.id.website_view_fragment)
        }

        override fun onClickOrder(item: OrdersData, position: Int) {
            if (item.isActive == true) {
                findNavController().navigate(R.id.order_details_fragment, bundleOf(
                    getString(R.string.orders) to item
                ))

            }


        }

    }


    fun openContentPicker() {
        val dialog = Dialog(requireContext(), R.style.ThemeOverlay_AppCompat_Dialog_Alert)
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.content_picker_view, null)
        dialog.setContentView(view)
        var tvPromotions = view.findViewById<TextView>(R.id.tvAds)
        var tvJobs = view.findViewById<TextView>(R.id.tvJobs)
        tvPromotions.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.add_promotoin_fragment)
        }
        tvJobs.setOnClickListener {
            dialog.dismiss()
            findNavController().navigate(R.id.add_job_fragment)
        }
        dialog.show()
    }


    override fun stopBlur() {

    }


    override fun onDestroy() {
        super.onDestroy()
        hideProgress()
    }

}