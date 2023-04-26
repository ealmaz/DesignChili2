package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentMainBinding

class MainFragment : BaseFragment<FragmentMainBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.tvNews.setText("""
            * ExpandableContainer - раскрываемы контейнер на основе LinearLayout
            
            * Новые стили и атрибуты темы для Bottom NavigationBar 
        """.trimIndent())
    }

    override fun inflateViewBinging(): FragmentMainBinding {
        return FragmentMainBinding.inflate(layoutInflater)
    }
}