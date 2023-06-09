package com.design2.chili2.view.modals.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import com.design2.chili2.R

abstract class BaseViewBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    override var topDrawableView: View? = null
    override var closeIconView: View? = null

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.chili_view_bottom_sheet_base, container, false)
        val contentView = view.findViewById<LinearLayout>(R.id.ll_content)
        contentView.addView(createContentView(inflater, container))
        initViewVariables(view)
        return view
    }

    private fun initViewVariables(view: View) {
        topDrawableView = view.findViewById(R.id.iv_top_drawable)
        closeIconView = view.findViewById(R.id.iv_close)
    }

    abstract fun createContentView(inflater: LayoutInflater, @Nullable container: ViewGroup?): View

}