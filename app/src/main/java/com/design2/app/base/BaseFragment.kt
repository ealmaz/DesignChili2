package com.design2.app.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.design2.app.MainActivity
import com.design2.chili2.view.container.ExpandableContainer

abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    protected lateinit var vb: VB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        vb = inflateViewBinging()
        return vb.root
    }

    abstract fun inflateViewBinging(): VB

    fun openFragment(fragment: Fragment) {
        (requireActivity() as MainActivity).openFragment(fragment)
    }

    open fun startShimmering() {}
    open fun stopShimmering() {}

    open fun setAllContainersIsExpanded(isExpanded: Boolean) {
        vb.root.setIsExpandedPeriodically(isExpanded)
    }

    private fun View.setIsExpandedPeriodically(isExpanded: Boolean) {
        when (this) {
            is ExpandableContainer -> this.setIsExpanded(isExpanded)
            is ViewGroup -> children.forEach { it.setIsExpandedPeriodically(isExpanded) }
            else -> return
        }
    }
}