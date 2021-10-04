package com.provider.unobridge.ui.fragments.profile.dialogs

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemEmployeeBinding
import com.provider.unobridge.databinding.ItemOrderBinding
import com.provider.unobridge.room.entities.EmployeesData

class EmployeesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemEmployeeBinding, EmployeesData>() {


    override fun bind(holder: ViewHolder, item: EmployeesData, position: Int) {

        holder.binding.tvCustomerName.text = "Customer : ${item.customer}"
        holder.binding.tvUserName.text = "Employee Name : ${item.name}"
        holder.binding.tvAddress.text = "Address :  ${item.address}"
        holder.binding.tvContact.text = "Contact Number : ${item.contactNo}"
        holder.binding.tvAadhar.text = "Aadhar Number : ${item.aadharNo}"


    }

}