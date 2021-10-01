package com.provider.unobridge.ui.fragments.rides

import com.provider.unobridge.base.BaseRecyclerAdapter
import com.provider.unobridge.data.RideMessage
import com.provider.unobridge.databinding.RidesMessageItemBinding

/**
 * Created by Nitin SHarma on 9/12/2021.
 */
class RidesAdapter(override val layoutId: Int) :
    BaseRecyclerAdapter<RidesMessageItemBinding, RideMessage>() {

    override fun bind(holder: ViewHolder, item: RideMessage, position: Int) {

    }
}