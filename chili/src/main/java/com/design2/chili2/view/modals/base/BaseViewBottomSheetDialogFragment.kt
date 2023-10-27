package com.design2.chili2.view.modals.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.design2.chili2.databinding.ChiliViewBottomSheetBaseFragmentBinding

abstract class BaseViewBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var vb: ChiliViewBottomSheetBaseFragmentBinding

    override var topDrawableView: View? = null
    override var closeIconView: View? = null

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        vb = ChiliViewBottomSheetBaseFragmentBinding.inflate(inflater, container, false)
        vb.llContent.addView(createContentView(inflater, container))
        initViewVariables()
        return vb.root
    }

    private fun initViewVariables() {
        topDrawableView = vb.ivTopDrawable
    }

    abstract fun createContentView(inflater: LayoutInflater, @Nullable container: ViewGroup?): View

}