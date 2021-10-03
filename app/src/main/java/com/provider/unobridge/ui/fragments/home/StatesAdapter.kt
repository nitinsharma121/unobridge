package com.provider.unobridge.ui.fragments.home

import android.view.ViewGroup
import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.data.StateData
import com.provider.unobridge.databinding.StateItemBinding

/**
 * Created by Nitin SHarma on 9/12/2021.
 */
class StatesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<StateItemBinding, StateData>() {

    var listenerState: clickListenerState? = null

    override fun bind(holder: ViewHolder, item: StateData, position: Int) {
        val marginParams: ViewGroup.MarginLayoutParams =
            holder.binding.clView.layoutParams as ViewGroup.MarginLayoutParams
        if (position % 2 == 0) {
            marginParams.setMargins(0, 0, 40, 50)
        } else marginParams.setMargins(0, 0, 0, 50)

        holder.binding.ivPic.setImageResource(item.image)
        holder.binding.tvStateName.text = item.stateName
        holder.itemView.setOnClickListener {
            listenerState?.onClickState(item.stateName.toString(), position)
        }
    }

    interface clickListenerState {
        fun onClickState(stateName: String, position: Int)
    }
}