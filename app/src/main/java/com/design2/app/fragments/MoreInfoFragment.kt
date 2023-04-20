package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.base.ViewInfo
import com.design2.app.databinding.FragmentMoreInfoBinding

class MoreInfoFragment : BaseFragment<FragmentMoreInfoBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        val info = arguments?.getSerializable(INFO) as? ViewInfo ?: ViewInfo()
        vb.title.text = info.title
        vb.content.text = info.content
    }

    override fun inflateViewBinging(): FragmentMoreInfoBinding {
        return FragmentMoreInfoBinding.inflate(layoutInflater)
    }

    companion object {

        const val INFO = "info"

        fun create(info: ViewInfo): MoreInfoFragment {
            return  MoreInfoFragment().apply {
                arguments = bundleOf(INFO to info)
            }
        }
    }
}