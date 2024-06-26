package com.design2.app.fragments

import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentNativeShadowsBinding

class NativeShadowsFragment: BaseFragment<FragmentNativeShadowsBinding>() {

    override fun inflateViewBinging(): FragmentNativeShadowsBinding {
        return FragmentNativeShadowsBinding.inflate(layoutInflater)
    }

}