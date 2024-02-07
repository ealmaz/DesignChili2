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

abstract class BaseAdapter<T>(
    private val context: Context,
    private val withShimmer: Boolean = true,
    private val loadingViewResId: Int? = null,
    private val loadingItemCount: Int = 3
) : RecyclerView.Adapter<BaseViewHolder<T?>>() {

    protected val items: MutableList<Pair<Int, T?>?> =
        if (withShimmer) MutableList(loadingItemCount) { VIEW_TYPE_LOADING to null }
        else mutableListOf()

    open fun setItems(newItems: List<T>) {
        items.clear()
        newItems.forEach {
            items.add(VIEW_TYPE_ITEM to it)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int = items[position]?.first ?: VIEW_TYPE_LOADING

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T?> =
        when (viewType) {
            VIEW_TYPE_LOADING -> createLoadingViewHolder(parent, loadingViewResId)
            else -> createItemViewHolder(parent, viewType)
        }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: BaseViewHolder<T?>, position: Int) {
        if (getItemViewType(position) == VIEW_TYPE_LOADING) {
            holder.onBind(null)
        } else {
            bindItemViewHolder(holder, position, items[position]?.second)
        }
    }

    abstract fun createItemViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T?>

    abstract fun bindItemViewHolder(holder: RecyclerView.ViewHolder, position: Int, item: T?)

    protected fun createLoadingViewHolder(
        parent: ViewGroup,
        loadingViewResId: Int?
    ): BaseViewHolder<T?> {
        val view = if (loadingViewResId != null) LayoutInflater.from(parent.context).inflate(
            loadingViewResId, parent, false
        ) else BaseCellView(context)
        return LoadingViewHolder(view)
    }

    class LoadingViewHolder<T>(private val view: View) : BaseViewHolder<T?>(view) {
        override fun onBind(item: T?, isLast: Boolean) {
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
    }

    companion object {
        private const val VIEW_TYPE_LOADING = 777
        private const val VIEW_TYPE_ITEM = 1
    }

}