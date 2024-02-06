package com.design2.chili2.view.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseViewHolder<in T>(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {

    abstract fun onBind(item: T, isLast: Boolean = false)

    class DiffCallback<T>(private var oldList: MutableList<Pair<T, Any>>,
                          private var newList: MutableList<Pair<T, Any>>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].second == newList[newItemPosition].second
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].first == newList[newItemPosition].first
        }
    }
}