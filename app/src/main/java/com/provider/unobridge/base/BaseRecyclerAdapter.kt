package com.provider.unobridge.base

import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.util.forEach
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerAdapter<T : ViewDataBinding, R> :
    RecyclerView.Adapter<BaseRecyclerAdapter<T, R>.ViewHolder>() {

    var list: ArrayList<R> = ArrayList()

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private var clickListeners = SparseArray<((position: Int, item: R) -> Unit)?>()

    abstract val layoutId: Int

    abstract fun bind(holder: ViewHolder, item: R, position: Int)

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val t: T = DataBindingUtil.inflate(inflater, layoutId, parent, false)
        return ViewHolder(t)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = list[position]
        holder.preBind(current)
        bind(holder, current, position)
    }

    fun addClickEventWithView(viewId: Int, onClickLambda: ((position: Int, item: R) -> Unit)?) {

        clickListeners.put(viewId, onClickLambda)
    }

    inner class ViewHolder(val binding: T) : RecyclerView.ViewHolder(binding.root) {

        fun preBind(element: R) {
            clickListeners.forEach { key, value ->
                binding.root.findViewById<View>(key)
                    .setOnClickListener { value?.invoke(adapterPosition, element) }
            }
        }
    }

    fun updateItem(newItem: R, predicate: (R) -> Boolean) {
        val targetPos = list.indexOfFirst(predicate)
        if (targetPos != -1) {
            list[targetPos] = newItem
            notifyItemChanged(targetPos)
        }
    }

    fun removeItem(predicate: (R) -> Boolean) {
        val targetPos = list.indexOfFirst(predicate)
        if (targetPos != -1) {
            list.removeAt(targetPos)
            notifyItemRemoved(targetPos)
        }
    }

    fun setNewItems(newItems: List<R>) {
        list.clear()
        list.addAll(newItems)
        notifyDataSetChanged()
    }

    fun addNewItems(newItems: List<R>) {
        val oldPos = list.size + 1
        list.addAll(newItems)
        notifyItemRangeInserted(oldPos, newItems.size)
    }

    fun removeItem(index: Int) {
        if (index != -1) {
            list.removeAt(index)
            notifyItemRemoved(index)
            notifyItemRangeChanged(index, list.size)
        }
    }

    fun addNewItems(newItems: List<R>, targetPos: Int) {
        list.addAll(targetPos, newItems)
        notifyItemRangeInserted(targetPos, newItems.size)
    }

    fun clearData() {
        val oldSize = list.size
        list.clear()
        notifyDataSetChanged()
    }

    fun clearOnlyList() {
        list.clear()
    }


}