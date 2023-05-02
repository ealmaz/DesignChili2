package com.design2.app.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.cardview.widget.CardView
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.view.shimmer.hideShimmer
import com.design2.chili2.view.shimmer.showShimmer
import com.design2.chili2.view.shimmer.startShimmering
import com.design2.chili2.view.shimmer.stopShimmering

class CardsFragment : BaseFragment<FragmentCardsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)



//        vb.expandable.setItems(listOf(
//            ExpandableItemData("Сумма к зачислению:", "Из нее - комиссии и сборы:", "8 000,00 <u>с</u>".parseAsHtml(), "150,00 <u>с</u>".parseAsHtml()),
//            ExpandableItemData("Сумма к зачислению:", "Из нее - комиссии и сборы:", "8 000,00 <u>с</u>".parseAsHtml(), "150,00 <u>с</u>".parseAsHtml()),
//            ExpandableItemData("Сумма к зачислению:", null, "8 000,00 <u>с</u>".parseAsHtml(), null),
//            ExpandableItemData("Сумма к зачислению:", null, "150,00 <u>с</u>".parseAsHtml()),
//            ExpandableItemData("Сумма к зачислению:", "Из нее - комиссии и сборы:", "8 000,00 <u>с</u>".parseAsHtml(), null),
//        ))


//        vb.expandable.showShimmer()
//        vb.expandable2.showShimmer()
//        vb.expandable3.showShimmer()
//        vb.expandable4.showShimmer()

        vb.startShimmer.setOnClickListener {
            vb.expandable.startShimmering()
        }

        vb.stopShimmer.setOnClickListener {
            vb.expandable.stopShimmering()
        }

    }

    override fun inflateViewBinging(): FragmentCardsBinding {
        return FragmentCardsBinding.inflate(layoutInflater)
    }
}