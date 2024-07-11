package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentButtonsBinding
import com.design2.chili2.extensions.setOnSingleClickListener

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
        vb.iconedButton.setOnSingleClickListener {
            println("Click")
        }
        vb.qab1.setOnSingleClickListener {
            Toast.makeText(context, "Quick Action Button 1 clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun inflateViewBinging(): FragmentButtonsBinding {
        return FragmentButtonsBinding.inflate(layoutInflater)
    }
}