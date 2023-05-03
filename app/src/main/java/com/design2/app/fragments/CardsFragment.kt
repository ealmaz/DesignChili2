package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding

class CardsFragment : BaseFragment<FragmentCardsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
    }

    override fun inflateViewBinging(): FragmentCardsBinding {
        return FragmentCardsBinding.inflate(layoutInflater)
    }
}