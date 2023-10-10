package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentButtonsBinding

class ButtonsFragment : BaseFragment<FragmentButtonsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.loader.setOnClickListener {
            vb.loader.setIsLoading(true)
        }
        vb.stopLoader.setOnClickListener {
            vb.loader.setIsLoading(false)
        }
        vb.iconedButton.setIcon(R.drawable.ic_market)
    }

    override fun inflateViewBinging(): FragmentButtonsBinding {
        return FragmentButtonsBinding.inflate(layoutInflater)
    }
}