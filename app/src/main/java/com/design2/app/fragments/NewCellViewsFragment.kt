package com.design2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentNewCellBinding
import com.design2.app.databinding.ItemViewNewCellBinding

class NewCellViewsFragment : BaseFragment<FragmentNewCellBinding>()  {
    override fun inflateViewBinging(): FragmentNewCellBinding {
        return FragmentNewCellBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ItemsNewCellListAdapter()
        vb.rv.adapter = adapter
        adapter.dataset = listOf(
            NewCellItemData("Кошелёк О!Деньги", "https://minio.o.kg/catalog/logos/odengi.png"),
            NewCellItemData("Пополнение \nО!Деньги", "https://minio.o.kg/catalog/logos/elcart.png"),
            NewCellItemData("Перевод О!Деньги", "https://valuta.kg/uploads/b/baitushum/avat_bai_1cbf8.png"),
            NewCellItemData("Заголовок", "https://minio.o.kg/catalog/icons/light/gov_fines.png"),
            NewCellItemData("Народный.Бонусная карта", "https://minio.o.kg/catalog/logos/vostokelectro.png"),
            NewCellItemData("Перевод О!Деньги", "https://img2.freepng.ru/20180429/bzq/kisspng-advertising-publicity-marketing-computer-icons-bra-5ae5c4fa63ff38.1177983315250076104096.jpg"),
            NewCellItemData("Пополнение \nО!Деньги", "https://minio.o.kg/catalog/logos/optimabank.png"),
            NewCellItemData("Перевод О!Деньги", "https://minio.o.kg/catalog/logos/mbank_new.png"),
            NewCellItemData("Пополнение О!Деньги", "https://minio.o.kg/catalog/logos/odengi.png")
        )
    }

}

class ItemsNewCellListAdapter : RecyclerView.Adapter<NewCellViewVH>() {

    var dataset: List<NewCellItemData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewCellViewVH {
        val view = ItemViewNewCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NewCellViewVH(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: NewCellViewVH, position: Int) {
        holder.onBind(dataset[position])
    }

}

class NewCellViewVH(val vb: ItemViewNewCellBinding) : RecyclerView.ViewHolder(vb.root) {

    fun onBind(data: NewCellItemData) {
        vb.item.apply {
            setTitle(data.title)
            setIcon(data.icon)
            setChevron(kg.devcats.chili3.R.drawable.chili3_ic_chevron_right)
        }
    }
}

data class NewCellItemData(
    val title: String,
    val icon: String
)