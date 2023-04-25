package com.design2.app.fragments

import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentNavigationBarsBinding

class NavigationBarsFragment : BaseFragment<FragmentNavigationBarsBinding>() {
    override fun inflateViewBinging(): FragmentNavigationBarsBinding {
        return FragmentNavigationBarsBinding.inflate(layoutInflater)
    }
}