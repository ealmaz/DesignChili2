package com.design2.chili2.view.modals.bottom_sheet

import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetDetailedInfoBinding
import com.design2.chili2.extensions.isVisible
import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment

class DetailedInfoBottomSheet private constructor(): BaseViewBottomSheetDialogFragment() {

    private lateinit var vb: ChiliViewBottomSheetDetailedInfoBinding

    private var text: String? = null
    private var textSpanned: Spanned? = null
    private var textResId: Int? = null

    private var titleTextCentered: Boolean? = null
    private var textCentered: Boolean? = null

    private var title: String? = null
    private var titleSpanned: Spanned? = null
    private var titleResId: Int? = null

    private var iconSizeDimenRes: Int? = null
    private var iconRes: Int? = null

    private var primaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
    private var primaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

    private var secondaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
    private var secondaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

    private var onDismissCallback: (() -> Unit)? = null


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
        title?.let {
            vb.tvTitle.visible()
            vb.tvTitle.text = it
        }

        titleSpanned?.let {
            vb.tvTitle.visible()
            vb.tvTitle.text = it
        }

        titleResId?.let {
            vb.tvTitle.visible()
            vb.tvTitle.setText(it)
        }

        titleTextCentered?.let {
            if (it) vb.tvTitle.gravity = Gravity.CENTER
        }

        textCentered?.let {
            if (it) vb.tvText.gravity = Gravity.CENTER
        }

        text?.let { setMessage(it) }
        textSpanned?.let { setMessage(it) }
        textResId?.let { setMessage(it)}


        iconRes?.let { setIcon(it) }
        iconSizeDimenRes?.let {
            vb.ivIcon.apply {
                val size = context.resources.getDimensionPixelSize(it)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.CENTER
                }
                requestLayout()
            }
        }

        primaryButton?.let { setPrimaryButton(it.first, it.second) }
        primaryButtonRes?.let { setPrimaryButton(it.first, it.second) }

        secondaryButton?.let { setSecondaryButton(it.first, it.second) }
        secondaryButtonRes?.let { setSecondaryButton(it.first, it.second) }

        updateMessageMargin()
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

    private fun updateMessageMargin() = with(vb) {
        if(!btnPrimary.isVisible() && !btnSecondary.isVisible()) {
            tvText.setBottomMargin(0)
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


    private fun setSecondaryButton(@StringRes resId: Int, action: (DetailedInfoBottomSheet.() -> Unit)? = null) {
        vb.btnSecondary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@DetailedInfoBottomSheet) }
        }
    }

    private fun setSecondaryButton(text: String, action: (DetailedInfoBottomSheet.() -> Unit)? = null) {
        vb.btnSecondary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@DetailedInfoBottomSheet) }
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke()
    }

    class Builder {
        private var text: String? = null
        private var textSpanned: Spanned? = null
        private var textResId: Int? = null

        private var title: String? = null
        private var titleSpanned: Spanned? = null
        private var titleResId: Int? = null

        private var isTopDrawableVisible: Boolean = true

        private var iconSizeDimenRes: Int? = null
        private var iconRes: Int? = null
        private var hasCloseIcon: Boolean = true

        private var primaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
        private var primaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

        private var secondaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>? = null
        private var secondaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>? = null

        private var titleTextCentered: Boolean? = null
        private var textCentered: Boolean? = null

        private var isHideable: Boolean = true
        private var isDraggable: Boolean = true

        private var onDismissCallback: (() -> Unit)? = null

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

        fun setTitleCentered(titleCentered: Boolean): Builder {
            this.titleTextCentered = titleCentered
            return this
        }

        fun setTextCentered(textCentered: Boolean): Builder {
            this.textCentered = textCentered
            return this
        }

        fun setTitle(text: String): Builder {
            this.title = text
            return this
        }

        fun setTitle(textSpanned: Spanned): Builder {
            this.titleSpanned = textSpanned
            return this
        }

        fun setTitle(textResId: Int): Builder {
            this.titleResId = textResId
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

        fun setSecondaryButton(secondaryButton: Pair<String, (DetailedInfoBottomSheet.() -> Unit)>): Builder {
            this.secondaryButton = secondaryButton
            return this
        }

        fun setIconSizeDimenRes(@DimenRes iconSizeDimenRes: Int): Builder {
            this.iconSizeDimenRes = iconSizeDimenRes
            return this
        }

        fun setSecondaryButtonRes(secondaryButtonRes: Pair<Int, (DetailedInfoBottomSheet.() -> Unit)>): Builder {
            this.secondaryButtonRes = secondaryButtonRes
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun setIsDraggable(isDraggable: Boolean): Builder {
            this.isDraggable = isDraggable
            return this
        }

        fun hideCloseIcon(): Builder {
            this.hasCloseIcon = false
            return this
        }

        fun setIsTopDrawableVisible(isVisible: Boolean): Builder {
            this.isTopDrawableVisible = isVisible
            return this
        }

        fun setOnDismissCallback(callback: () -> Unit): Builder {
            this.onDismissCallback = callback
            return this
        }

        fun build(): DetailedInfoBottomSheet {
            return DetailedInfoBottomSheet().apply {
                this.text = this@Builder.text
                this.textSpanned = this@Builder.textSpanned
                this.textResId = this@Builder.textResId
                this.title = this@Builder.title
                this.titleSpanned = this@Builder.titleSpanned
                this.titleResId = this@Builder.titleResId
                this.iconSizeDimenRes = this@Builder.iconSizeDimenRes
                this.iconRes = this@Builder.iconRes
                this.primaryButton = this@Builder.primaryButton
                this.primaryButtonRes = this@Builder.primaryButtonRes
                this.secondaryButton = this@Builder.secondaryButton
                this.secondaryButtonRes = this@Builder.secondaryButtonRes
                this.isHideable = this@Builder.isHideable
                this.isDraggable = this@Builder.isDraggable
                this.hasCloseIcon = this@Builder.hasCloseIcon
                this.topDrawableVisible = this@Builder.isTopDrawableVisible
                this.textCentered = this@Builder.textCentered
                this.titleTextCentered = this@Builder.titleTextCentered
                this.onDismissCallback = this@Builder.onDismissCallback
            }
        }
    }
}