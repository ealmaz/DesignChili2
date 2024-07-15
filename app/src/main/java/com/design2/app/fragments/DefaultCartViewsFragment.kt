package com.design2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentDefaultCartBinding
import com.design2.app.databinding.ItemViewNewCellBinding
import kotlin.random.Random

class DefaultCartViewsFragment: BaseFragment<FragmentDefaultCartBinding>() {
    override fun inflateViewBinging(): FragmentDefaultCartBinding {
        return FragmentDefaultCartBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemsDefaultCartListAdapter(listOf(
            DefaultCartItemData("Visa","···· 1234"),
            DefaultCartItemData("Visa o!Dengi","···· 12421"),
            DefaultCartItemData("Банковский счет","···· 2341"),
            DefaultCartItemData("ELCART ЭЛКАП","···· 1234"),
            DefaultCartItemData("Visa","···· 1234"),
            DefaultCartItemData("Visa o!Dengi","···· 12421"),
            DefaultCartItemData("Банковский счет","···· 2341"),
            DefaultCartItemData("ELCART ЭЛКАП","···· 1234"),
        )){selectedData ->

        }
        vb.rvDCart.adapter = adapter

    }
}

class ItemsDefaultCartListAdapter(
    var dataset: List<DefaultCartItemData>,
    val selectData: (DefaultCartItemData) -> Unit) : RecyclerView.Adapter<DefaultCartViewVH>() {

    /* = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }*/

    var selectedData: DefaultCartItemData ? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DefaultCartViewVH {
        val view = ItemViewNewCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DefaultCartViewVH(view){ it ->
            it.let { selectedData ->
                this.selectedData = selectedData
                this.selectData.invoke(selectedData)
                notifyDataSetChanged()
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: DefaultCartViewVH, position: Int) {
        holder.onBind(dataset[position], selectedData)
    }
}

class DefaultCartViewVH(
    val vb: ItemViewNewCellBinding,
    val selectData: (DefaultCartItemData) -> Unit) : RecyclerView.ViewHolder(vb.root) {
    fun onBind(data: DefaultCartItemData, selectedData: DefaultCartItemData?) {
        vb.item.apply {
            setTitle(data.title)
            setTitleTextAppearance(kg.devcats.chili3.R.style.Chili_H16_Primary_Bold)
            setSubtitle(data.subtitle)
            setSubtitleTextAppearance(com.design2.chili2.R.style.Chili_H9_Secondary)
            when{
                (data.id == selectedData?.id) -> {
                    setChevron(kg.devcats.chili3.R.drawable.chili3_ic_selected_radio_button)
                    setChevronColor(kg.devcats.chili3.R.color.radio_button_selected_border_color)
                }
                else -> {
                    setChevron(kg.devcats.chili3.R.drawable.chili3_ic_radiobutton)
                    setChevronColor(kg.devcats.chili3.R.color.radio_button_default_border_color)
                }
            }

            setOnClickListener {
                selectData.invoke(data)
            }
        }
    }
}

data class DefaultCartItemData(
    val title: String,
    val subtitle: String,
    val id: Int = Random.nextInt(1000)
)