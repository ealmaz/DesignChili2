package com.design2.chili2.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setHorizontalMargin
import com.design2.chili2.view.cells.BaseCellView
import com.design2.chili2.view.shimmer.startShimmering

abstract class BaseAdapter<T>(private val context: Context, private val loadingViewResId: Int? = null, private val loadingItemCount: Int = 3): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    protected val items: MutableList<Pair<Int, T?>?> = MutableList(loadingItemCount) { VIEW_TYPE_LOADING to null }

    open fun setItems(newItems: List<T>) {
        items.clear()
        newItems.forEach {
            items.add(VIEW_TYPE_ITEM to it)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = items[position]?.first ?: VIEW_TYPE_LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when(viewType) {
        VIEW_TYPE_LOADING -> createLoadingViewHolder(parent, loadingViewResId)
        else -> createItemViewHolder(parent)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            bindLoadingViewHolder(holder.itemView)
        } else {
            bindItemViewHolder(holder, position, items[position]?.second)
        }
    }

    abstract fun createItemViewHolder(parent: ViewGroup): RecyclerView.ViewHolder

    abstract fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: T?)

    protected open fun bindLoadingViewHolder(view: View) {
        view.apply {
            layoutParams = ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setHorizontalMargin(resources.getDimension(R.dimen.padding_16dp).toInt())
            setBottomMargin(resources.getDimension(R.dimen.padding_8dp).toInt())
        }
        if (view is BaseCellView) {
            view.apply {
                setIsChevronVisible(false)
                setDividerVisibility(false)
                startShimmering()
            }
        }
    }

    protected fun createLoadingViewHolder(parent: ViewGroup, loadingViewResId: Int?): RecyclerView.ViewHolder {
        val view = if (loadingViewResId != null) LayoutInflater.from(parent.context).inflate(
            loadingViewResId, parent, false
        ) else BaseCellView(context)
        return object : RecyclerView.ViewHolder(view) {}
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 777
        private const val VIEW_TYPE_ITEM = 1
    }

}