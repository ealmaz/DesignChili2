package com.design2.app.fragments

import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentHighlighterviewBinding

class HighlightersFragment : BaseFragment<FragmentHighlighterviewBinding>() {

    override fun inflateViewBinging(): FragmentHighlighterviewBinding {
        return FragmentHighlighterviewBinding.inflate(layoutInflater)
    }

}