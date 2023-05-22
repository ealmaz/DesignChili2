package com.design2.chili2.view.modals.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.design2.chili2.R
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setHorizontalMargin

abstract class BaseFragmentBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private lateinit var llContent: LinearLayout

    override var topDrawableView: View? = null
    override var closeIconView: View? = null

    protected open var horizontalMargin: Int = 0
    protected open var bottomMargin: Int = 0

    @DrawableRes
    protected open var backgroundDrawable: Int = R.drawable.chili_bg_rounded_bottom_sheet

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.chili_view_bottom_sheet_base_fragment, container, false)
        initViewVariables(view)
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, createFragment())
            .commit()
        llContent = view.findViewById(R.id.ll_content)
        llContent.setHorizontalMargin(horizontalMargin)
        llContent.setBottomMargin(bottomMargin)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        llContent.setBackgroundResource(backgroundDrawable)
    }

    private fun initViewVariables(view: View) {
        topDrawableView = view.findViewById(R.id.iv_top_drawable)
        closeIconView = view.findViewById(R.id.iv_close)
    }

    abstract fun createFragment(): Fragment
}