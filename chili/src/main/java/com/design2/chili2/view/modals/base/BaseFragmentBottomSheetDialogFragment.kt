package com.design2.chili2.view.modals.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetBaseFragmentBinding
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setHorizontalMargin

abstract class BaseFragmentBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var vb: ChiliViewBottomSheetBaseFragmentBinding

    override var topDrawableView: View? = null
    override var newTopDrawableView: View? = null
    override var closeIconView: View? = null

    protected open var horizontalMargin: Int = 0
    protected open var bottomMargin: Int = 0

    @DrawableRes
    protected open var backgroundDrawable: Int = R.drawable.chili_bg_rounded_bottom_sheet

    @DrawableRes
    protected open var closeIconDrawable: Int = R.drawable.chili_ic_clear


    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        vb = ChiliViewBottomSheetBaseFragmentBinding.inflate(inflater, container, false)
        initViewVariables()
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, createFragment())
            .commit()
        vb.llContent.setHorizontalMargin(horizontalMargin)
        vb.llContent.setBottomMargin(bottomMargin)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        vb.llContent.setBackgroundResource(backgroundDrawable)
        vb.ivClose.setBackgroundResource(closeIconDrawable)
    }

    private fun initViewVariables() {
        topDrawableView = vb.ivTopDrawable
        newTopDrawableView = vb.ivNewTopDrawable
        closeIconView = vb.ivClose
    }

    abstract fun createFragment(): Fragment
}