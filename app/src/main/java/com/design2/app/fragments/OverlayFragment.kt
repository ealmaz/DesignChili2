package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentOverlayBinding

class OverlayFragment(val overlayView: View) : BaseFragment<FragmentOverlayBinding>() {

    override fun inflateViewBinging(): FragmentOverlayBinding {
        return FragmentOverlayBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.fl.addView(
            overlayView.apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
            }
        )
    }
}