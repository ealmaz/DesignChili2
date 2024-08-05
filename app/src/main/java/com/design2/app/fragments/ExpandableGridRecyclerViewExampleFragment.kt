package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentExpandableGridRecyclerViewExamplesBinding
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import kg.devcats.chili3.model.ExpandableGridItem
import kg.devcats.chili3.view.container.ExpandableGridRecyclerView

class ExpandableGridRecyclerViewExampleFragment : BaseFragment<FragmentExpandableGridRecyclerViewExamplesBinding>(),
    ExpandableGridRecyclerView.Listener {

    private var items = List(8) {
        ExpandableGridItem(
            null,
            "Бонусы ${it + 1}",
            "https://minio.o.kg/media-service/DiscountCard/narodny.png",
            "https://somedeeplink ${it + 1}"
        )
    }

    override fun inflateViewBinging(): FragmentExpandableGridRecyclerViewExamplesBinding {
        return FragmentExpandableGridRecyclerViewExamplesBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.rvItems.setListener(this)
    }

    override fun startShimmering() {
        super.startShimmering()
        vb.root.startGroupShimmering()
    }

    override fun stopShimmering() {
        super.stopShimmering()
        vb.root.stopGroupShimmering()
        vb.rvItems.setItems(items)
    }

    override fun onItemClick(deeplink: String?) {
        Toast.makeText(requireContext(), deeplink, Toast.LENGTH_SHORT).show()
    }

}