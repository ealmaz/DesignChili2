package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.view.shimmer.startShimmering
import com.design2.chili2.view.shimmer.stopShimmering

class CardsFragment : BaseFragment<FragmentCardsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
    }

    override fun inflateViewBinging(): FragmentCardsBinding {
        return FragmentCardsBinding.inflate(layoutInflater)
    }

    override fun startShimmering() {
        super.startShimmering()
        vb.accent.startShimmering()
        vb.accent2.startShimmering()
        vb.accent3.startShimmering()
        vb.expandable.startShimmering()
        vb.expandable3.startShimmering()
        vb.expandable2.startShimmering()
        vb.cat1.startShimmering()
        vb.cat2.startShimmering()
        vb.cat3.startShimmering()
        vb.cat4.startShimmering()
        vb.simple.startShimmering()
        vb.simple2.startShimmering()
        vb.simple3.startShimmering()
        vb.bal1.startShimmering()
        vb.bal2.startShimmering()
    }

    override fun stopShimmering() {
        super.stopShimmering()
        vb.accent.stopShimmering()
        vb.accent2.stopShimmering()
        vb.accent3.stopShimmering()
        vb.expandable.stopShimmering()
        vb.expandable2.stopShimmering()
        vb.expandable3.stopShimmering()
        vb.cat1.stopShimmering()
        vb.cat2.stopShimmering()
        vb.cat3.stopShimmering()
        vb.cat4.stopShimmering()
        vb.simple.stopShimmering()
        vb.simple2.stopShimmering()
        vb.simple3.stopShimmering()
        vb.bal1.stopShimmering()
        vb.bal2.stopShimmering()
    }
}