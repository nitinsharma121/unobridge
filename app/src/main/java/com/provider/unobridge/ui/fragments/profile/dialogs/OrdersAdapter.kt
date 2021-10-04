package com.provider.unobridge.ui.fragments.profile.dialogs

import android.view.View.GONE
import android.view.View.VISIBLE
import com.provider.unobridge.R
import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.databinding.ItemOrderBinding
import com.provider.unobridge.room.entities.OrdersData

class OrdersAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<ItemOrderBinding, OrdersData>() {

    var listener:onClick?=null

    override fun bind(holder: ViewHolder, item: OrdersData, position: Int) {

        holder.binding.tvCustomerName.text = "Customer Name : ${item.customerName}"
        holder.binding.tvEndDate.text = "End Date : ${item.endDate}"
        holder.binding.tvPrice.text = "Price : ₹ ${item.orderPrice}"

        holder.binding.tvPaymentLink.text = "Payment Link: ${item.paymentLink}"
        holder.binding.tvReviewLink.text = "Review Link: ${item.reviewFeedbackLink}"
        holder.binding.tvInvoiceLink.text = "Invoice Link: ${item.invoiceLink}"


        if (item.isActive == true) {
            holder.binding.ivDone.setImageResource(R.drawable.green_circle)
            holder.binding.tvIsAcive.visibility = VISIBLE
            holder.binding.tvPaymentLink.visibility = GONE
            holder.binding.tvInvoiceLink.visibility = GONE
            holder.binding.tvReviewLink.visibility = GONE
        } else {
            holder.binding.ivDone.setImageResource(R.drawable.ic_baseline_done_24)
            holder.binding.tvIsAcive.visibility = GONE
            holder.binding.tvPaymentLink.visibility = VISIBLE
            holder.binding.tvInvoiceLink.visibility = VISIBLE
            holder.binding.tvReviewLink.visibility = VISIBLE
        }

        holder.itemView.setOnClickListener{
            listener?.onClickOrder(item, position)
        }


    }

    interface onClick{
        fun onClickOrder(item:OrdersData,position: Int)
    }

}