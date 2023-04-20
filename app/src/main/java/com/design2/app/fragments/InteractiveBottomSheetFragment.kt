package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentInteractiveBottomSheetBinding

class InteractiveBottomSheetFragment : BaseFragment<FragmentInteractiveBottomSheetBinding>() {

    override fun inflateViewBinging(): FragmentInteractiveBottomSheetBinding {
        return FragmentInteractiveBottomSheetBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        childFragmentManager.beginTransaction()
            .replace(R.id.container_2, CommonViewsFragment())
            .commit()
    }
}