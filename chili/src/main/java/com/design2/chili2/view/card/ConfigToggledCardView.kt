package com.design2.chili2.view.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.model.Option
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewConfigToggleCardBinding

class ConfigToggledCardView : ConstraintLayout {

    private lateinit var vb: ChiliViewConfigToggleCardBinding

    constructor(context: Context) : super(context) {
        inflateViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateViews()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        inflateViews()
        obtainAttributes(attrs, defStyle)
    }

    private fun inflateViews() {
        vb = ChiliViewConfigToggleCardBinding.inflate(LayoutInflater.from(context))
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_CardViewStyle_SingleSelectedCard) {
        context?.obtainStyledAttributes(attrs, R.styleable.SingleSelectedCardView, R.attr.singleSelectedCardViewDefaultStyle, defStyle)?.run {
            getString(R.styleable.SingleSelectedCardView_title)?.let {
                setTitleText(it)
            }
            getString(R.styleable.SingleSelectedCardView_value)?.let {
                setSubtitle(it)
            }
            recycle()
        }
    }

    fun setTitleText(title: String) {
        vb.ticvView.setTitle(title)
    }

    fun setSubtitle(subtitle: String) {
        vb.ticvView.setSubtitle(subtitle)
    }

    fun setInfoBtnVisibilty(isVisible: Boolean) {
        vb.ticvView.setIsInfoButtonVisible(isVisible)
    }

    fun setInfoBtnClickListener(onClick: () -> Unit) {
        vb.ticvView.setInfoButtonClickListener(onClick)
    }

    fun setToggles(items: ArrayList<Option<*>>, listener: TitledTogglesAdapter.MultiCheckedListener) {
        val adapter = TitledTogglesAdapter(listener)
        vb.rvItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        vb.rvItems.adapter = adapter
        adapter.addItems(items)
    }

}

class TitledTogglesAdapter(private val listener: MultiCheckedListener) : RecyclerView.Adapter<TitledTogglesAdapter.TitledToggleVH>() {

    val items = ArrayList<Option<*>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TitledToggleVH {
        val view = TitledToggleCardView(parent.context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        return TitledToggleVH(view, listener)
    }

    override fun onBindViewHolder(holder: TitledToggleVH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addItems(items: ArrayList<Option<*>>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class TitledToggleVH(view: View, private val listener: MultiCheckedListener): RecyclerView.ViewHolder(view) {
        fun bind(item: Option<*>) {
            (itemView as TitledToggleCardView).apply {
                item.title?.let { setTitleText(it) }
                item.description?.let { setValueHtml(it) }
                setIsInfoButtonVisible(item.isInfoBtnVisible == true)
                setInfoButtonClickListener {
                    listener.onServiceInfoClicked(adapterPosition)
                }
                item.icons?.let { setIcons(it) }
                setUnavailable(item.isUnavailable)
                setOnCheckChangeListener { _, b ->
                    if (b) listener.onChecked(adapterPosition)
                    else listener.onUnchecked(adapterPosition)
                }
            }
        }
    }

    interface MultiCheckedListener {
        fun onChecked(position: Int)
        fun onUnchecked(position: Int)
        fun onServiceInfoClicked(position: Int)
    }
}
