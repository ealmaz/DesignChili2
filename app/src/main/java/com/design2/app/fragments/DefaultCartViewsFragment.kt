package com.design2.app.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentDefaultCartBinding
import com.design2.chili2.view.cells.BaseCellView
import kotlin.random.Random

class DefaultCartViewsFragment : BaseFragment<FragmentDefaultCartBinding>() {

    private var titleResId: Int = -1
    private var title:String = ""
    private var subTitleResId: Int = -1
    private var subTitle:String = ""
    override fun inflateViewBinging(): FragmentDefaultCartBinding {
        return FragmentDefaultCartBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (title.isNotEmpty()) {
            setTitle(title)
        }
        if (titleResId != -1){
            setTitle(titleResId)
        }
        if (subTitle.isNotEmpty()) {
            setSubTitle(subTitle)
        }
        if (subTitleResId != -1){
            setSubTitle(subTitleResId)
        }
        val adapter = ItemsDefaultCartListAdapter(listOf(
            DefaultCartItemData("Visa","···· 1234"),
            DefaultCartItemData("Visa o!Dengi","···· 12421"),
            DefaultCartItemData("Банковский счет","···· 2341"),
            DefaultCartItemData("ELCART ЭЛКАП","···· 1234"),
            DefaultCartItemData("Visa","···· 1234"),
            DefaultCartItemData("Visa o!Dengi","···· 12421"),
            DefaultCartItemData("Банковский счет","···· 2341"),
            DefaultCartItemData("ELCART ЭЛКАП","···· 1234"),
        )){ selectedData ->

        }
        vb.rvDCart.adapter = adapter

    }

    fun setTitleValue(value: String) {
        title = value
    }

    fun setTitleResId(@StringRes id: Int) {
        titleResId = id
    }

    fun setSubTitleValue(value: String) {
        subTitle = value
    }

    fun setSubTitleResId(@StringRes id: Int) {
        subTitleResId = id
    }

    private fun setTitle(@StringRes id:Int){
        vb.tvTitle.setText(id)
    }

    private fun setTitle(title:String){
        vb.tvTitle.text = title
    }
    private fun setSubTitle(@StringRes id:Int){
        vb.tvSubtitle.setText(id)
    }

    private fun setSubTitle(subtitle:String){
        vb.tvSubtitle.text = subtitle
    }
}

class ItemsDefaultCartListAdapter(
    var dataset: List<DefaultCartItemData>,
    val selectData: (DefaultCartItemData) -> Unit
) : RecyclerView.Adapter<DefaultCartViewVH>() {

    var selectedData: DefaultCartItemData? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultCartViewVH {
        return DefaultCartViewVH.create(parent.context) { selectedData ->
            this.selectedData = selectedData
            this.selectData.invoke(selectedData)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: DefaultCartViewVH, position: Int) {
        holder.onBind(dataset[position], selectedData)
    }
}

class DefaultCartViewVH(
    val view: View,
    val onCellSelected: (DefaultCartItemData) -> Unit
) : RecyclerView.ViewHolder(view) {

    fun onBind(data: DefaultCartItemData, selectedData: DefaultCartItemData?) {
        (view as BaseCellView).apply {
            setTitle(data.title)
            setSubtitle(data.subtitle)
            when {
                (data.id == selectedData?.id) -> {
                    setChevron(kg.devcats.chili3.R.drawable.ic_selected_radio_button)
                    setChevronColor(kg.devcats.chili3.R.color.radio_button_selected_border_color)
                }

                else -> {
                    setChevron(kg.devcats.chili3.R.drawable.ic_radiobutton)
                    setChevronColor(kg.devcats.chili3.R.color.radio_button_default_border_color)
                }
            }

            setOnClickListener {
                onCellSelected.invoke(data)
            }
        }
    }

    companion object {
        fun create(context: Context, onSelect: (DefaultCartItemData) -> Unit): DefaultCartViewVH {
            val view = BaseCellView(context = context, ).apply {
                layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
                setTitleTextAppearance(kg.devcats.chili3.R.style.Chili_H16_Primary_Bold)
                setSubtitleTextAppearance(com.design2.chili2.R.style.Chili_H9_Secondary)
            }
            return DefaultCartViewVH(view) { onSelect.invoke(it) }
        }
    }
}

data class DefaultCartItemData(
    val title: String,
    val subtitle: String,
    val id: Int = Random.nextInt(1000)
)