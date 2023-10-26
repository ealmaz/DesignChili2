package com.design2.chili2.view.modals.bottom_sheet

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetDetailedInfoBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment

class DetailedInfoBottomSheet private constructor(): BaseViewBottomSheetDialogFragment() {

    private lateinit var vb: ChiliViewBottomSheetDetailedInfoBinding

    private var text: String? = null
    private var textSpanned: Spanned? = null
    private var textResId: Int? = null

    private var iconRes: Int? = null

    private var primaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
    private var primaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

    override var hasCloseIcon: Boolean = true
    override var closeIconView: View? = null
    override var topDrawableVisible = true

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        vb = ChiliViewBottomSheetDetailedInfoBinding.inflate(inflater, container, false).apply {
            root.setBackgroundResource(R.drawable.chili_bg_rounded_bottom_sheet)
        }
        closeIconView = vb.ivDetailClose
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        text?.let { setMessage(it) }
        textSpanned?.let { setMessage(it) }
        textResId?.let { setMessage(it)}

        iconRes?.let { setIcon(it) }

        primaryButton?.let { setPrimaryButton(it.first, it.second) }
        primaryButtonRes?.let { setPrimaryButton(it.first, it.second) }
    }

    private fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    private fun setMessage(@StringRes resId: Int) {
        vb.tvText.apply {
            visible()
            setText(resId)
        }
    }

    private fun setMessage(message: String) {
        vb.tvText.apply {
            visible()
            text = message
        }
    }

    private fun setMessage(spanned: Spanned?) {
        vb.tvText.apply {
            text = spanned
            visible()
        }
    }

    private fun setPrimaryButton(@StringRes resId: Int, action: (DetailedInfoBottomSheet.() -> Unit)? = null) {
        vb.btnPrimary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@DetailedInfoBottomSheet) }
        }
    }

    private fun setPrimaryButton(text: String, action: (DetailedInfoBottomSheet.() -> Unit)? = null) {
        vb.btnPrimary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@DetailedInfoBottomSheet) }
        }
    }

    class Builder {
        private var text: String? = null
        private var textSpanned: Spanned? = null
        private var textResId: Int? = null

        private var iconRes: Int? = null
        private var hasCloseIcon: Boolean = true

        private var primaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
        private var primaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

        private var isHideable: Boolean = true

        fun setMessage(text: String): Builder {
            this.text = text
            return this
        }

        fun setMessage(textSpanned: Spanned): Builder {
            this.textSpanned = textSpanned
            return this
        }

        fun setMessage(textResId: Int): Builder {
            this.textResId = textResId
            return this
        }

        fun setIcon(iconRes: Int): Builder {
            this.iconRes = iconRes
            return this
        }

        fun setPrimaryButton(primaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>): Builder {
            this.primaryButton = primaryButton
            return this
        }

        fun setPrimaryButtonRes(primaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>): Builder {
            this.primaryButtonRes = primaryButtonRes
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun hideCloseIcon(): Builder {
            this.hasCloseIcon = false
            return this
        }

        fun build(): DetailedInfoBottomSheet {
            return DetailedInfoBottomSheet().apply {
                this.text = this@Builder.text
                this.textSpanned = this@Builder.textSpanned
                this.textResId = this@Builder.textResId
                this.iconRes = this@Builder.iconRes
                this.primaryButton = this@Builder.primaryButton
                this.primaryButtonRes = this@Builder.primaryButtonRes
                this.isHideable = this@Builder.isHideable
                this.hasCloseIcon = this@Builder.hasCloseIcon
            }
        }
    }
}