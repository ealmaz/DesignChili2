package com.design2.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.R
import com.design2.app.databinding.ItemDiscountCardBinding
import com.design2.chili2.view.card.IconHolderCardItemView

class SimpleDiscountCardRecyclerViewAdapter(private val context: Context,
//                                            private val onClick: (Any) -> Unit
) :
    RecyclerView.Adapter<SimpleDiscountCardRecyclerViewAdapter.ViewHolder>() {

    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vb = ItemDiscountCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(vb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        val url = "https://minio.o.kg/media-service/DiscountCard/narodny.png"
        if (!item.isNullOrEmpty()){
            holder.discountCard.apply {
                when(position % 3){
                    0 -> {
                        setTitle("Card Provider $position")
                        setIconWithoutPadding(url)
                    }
                    1 -> {
                        setTitle("Card Custom $position")
                        setIcon(R.drawable.ic_bonus_card_32dp, true)
                        setColor(com.design2.chili2.R.color.green_1)
                    }
                    2 -> {
                        setTitle("Card Custom $position")
                        setEmoji(String(Character.toChars(128515)))
                        setColor(com.design2.chili2.R.color.red_1)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(holderVb: ItemDiscountCardBinding) : RecyclerView.ViewHolder(holderVb.root) {
        val discountCard: IconHolderCardItemView = holderVb.card
    }

    // Method to update the dataset and refresh the adapter
    fun updateListWithIcons(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }
}
