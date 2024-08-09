package com.design2.app.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCommonBinding
import com.design2.app.databinding.ItemViewCellBinding
import kg.devcats.chili3.view.common.chips.SimpleTextChip

class CommonViewsFragment : BaseFragment<FragmentCommonBinding>() {

    override fun inflateViewBinging(): FragmentCommonBinding {
        return FragmentCommonBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        val adapter = ItemsListAdapter()
        vb.rv.adapter = adapter
        adapter.dataset = listOf(
            ItemData("Бишкек \nПетролеум", "Колонка №3, АИ 92 7 л.", "500,00 с", "Автоплатеж", "https://minio.o.kg/catalog/logos/odengi.png"),
            ItemData("Пополнение \nО!Деньги", "А******В Ы******И", "+ 500,00 с", " +1,00 Б ", "https://minio.o.kg/catalog/logos/elcart.png"),
            ItemData("Перевод О!Деньги", "996700223456", "- 500,00 с", "", "https://valuta.kg/uploads/b/baitushum/avat_bai_1cbf8.png"),
            ItemData("Заголовок", "Подзаголовок", "- 500,00 с", " +1,00 Б ", "https://minio.o.kg/catalog/icons/light/gov_fines.png"),
            ItemData("Народный.Бонусная карта", "А******В Ы******И", "+900", "", "https://minio.o.kg/catalog/logos/vostokelectro.png"),
            ItemData("Перевод О!Деньги", "Колонка №3, АИ 92 7 л.", "- 500,00 с", "+1,00 Б", "https://img2.freepng.ru/20180429/bzq/kisspng-advertising-publicity-marketing-computer-icons-bra-5ae5c4fa63ff38.1177983315250076104096.jpg"),
            ItemData("Пополнение \nО!Деньги", "А******В Ы******И", "- 500,00 с", "", "https://minio.o.kg/catalog/logos/optimabank.png"),
            ItemData("Перевод О!Деньги", "Подзаголовок", "- 500,00 с", "+1,00 Б", "https://minio.o.kg/catalog/logos/mbank_new.png"),
            ItemData("Пополнение О!Деньги", "Подзаголовок", "", "", "https://minio.o.kg/catalog/logos/odengi.png")
        )
        vb.swipeRefresh.run {
            setOnRefreshListener {
                postDelayed({ isRefreshing = false }, 3000)
            }
        }

        vb.av1.onUrlClick { Toast.makeText(context, "Вы нажали на ссылку $it", Toast.LENGTH_SHORT).show() }
        vb.av3.hideCheckbox()


        vb.chips.setItems(listOf(
            SimpleTextChip(1, "5 августа"),
            SimpleTextChip(2, "10 августа"),
            SimpleTextChip(3, "15 августа"),
            SimpleTextChip(4, "20 августа")
        ))

        vb.chips.setCheckedChangedListener {id, isSelected ->
            Toast.makeText(requireContext(), "$id - $isSelected", Toast.LENGTH_SHORT).show()
        }
    }
}

class ItemsListAdapter : RecyclerView.Adapter<CellViewVH>() {

    var dataset: List<ItemData> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CellViewVH {
        val view = ItemViewCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CellViewVH(view)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    override fun onBindViewHolder(holder: CellViewVH, position: Int) {
        holder.onBind(dataset[position])
    }

}

class CellViewVH(val vb: ItemViewCellBinding) : RecyclerView.ViewHolder(vb.root) {

    fun onBind(data: ItemData) {
        vb.item.apply {
            setTitle(data.title)
            setSubtitle(data.subtitle)
            setStatus(data.status)
            setValue(data.value)
            setIconUrl(data.icon)
        }
    }
}

data class ItemData(
    val title: String,
    val subtitle: String,
    val value: String,
    val status: String,
    val icon: String
)