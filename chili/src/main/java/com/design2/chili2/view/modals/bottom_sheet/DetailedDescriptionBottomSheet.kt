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
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliDetailedDescBottomSheetBinding
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment

class DetailedDescriptionBottomSheet private constructor(): BaseViewBottomSheetDialogFragment() {

    private lateinit var vb: ChiliDetailedDescBottomSheetBinding

    private var descriptionText: String? = null
    private var descriptionTextSpanned: Spanned? = null
    private var descriptionTextResId: Int? = null
    private var descTextTextAppearance: Int? = null
    private var descriptionTextMaxLines: Int = 4

    private var headerText: String? = null
    private var headerTextSpanned: Spanned? = null
    private var headerTextAppearance: Int? = null

    private var subtitleText: String? = null
    private var subtitleTextSpanned: Spanned? = null
    private var subtitleTextAppearance: Int? = null

    private var iconRes: Int? = null
    private var iconUri: String? = null

    private var iconSizeDimenRes: Int? = null

    private var primaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
    private var secondaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
    private var primaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
    private var secondaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>? = null

    override var hasCloseIcon: Boolean = true
    override var closeIconView: View? = null

    private var dismissEvent: (() -> Unit)? = null

    override var isDraggable: Boolean = false
    override var isHideable: Boolean = false

    override var innerTopDrawableVisible = true
    override var innerTopDrawableView: View? = null

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        vb = ChiliDetailedDescBottomSheetBinding.inflate(inflater, container, false).apply {
            root.setBackgroundResource(R.drawable.chili_bg_rounded_bottom_sheet)
        }
        closeIconView = vb.ivInfoClose
        innerTopDrawableView = vb.ivTopDrawable

        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    override fun onDismiss(dialog: DialogInterface) {
        dismissEvent?.invoke()
        super.onDismiss(dialog)
    }

    private fun setupViews() {
        vb.tvDescription.maxLines = descriptionTextMaxLines
        descriptionText?.let { setDescription(it) }
        descriptionTextSpanned?.let { setDescription(it) }
        descriptionTextResId?.let { setDescription(it)}
        descTextTextAppearance?.let { vb.tvDescription.setTextAppearance(it) }

        headerText?.let { setHeaderText(it) }
        headerTextSpanned?.let { setHeaderText(it) }
        headerTextAppearance?.let { vb.tvHeader.setTextAppearance(it) }

        subtitleText?.let { setSubtitleText(it) }
        subtitleTextSpanned?.let { setSubtitleText(it) }
        subtitleTextAppearance?.let { vb.tvSubtitle.setTextAppearance(it) }

        iconSizeDimenRes?.let {
            vb.ivIcon.apply {
                val size = context.resources.getDimensionPixelSize(it)
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    gravity = Gravity.CENTER
                }
                requestLayout()
            }
        }

        iconRes?.let { setIcon(it) }
        iconUri?.let { setIcon(it) }

        primaryButton?.let { setPrimaryButton(it.first, it.second) }
        secondaryButton?.let { setSecondaryButton(it.first, it.second) }
        primaryButtonRes?.let { setPrimaryButton(it.first, it.second) }
        secondaryButtonRes?.let { setSecondaryButton(it.first, it.second) }
    }

    private fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    private fun setIcon(uri: String?){
        vb.ivIcon.apply {
            setImageByUrl(uri)
            visible()
        }
    }

    private fun setHeaderText(text: String?) {
        vb.tvHeader.setTextOrHide(text)
    }

    private fun setHeaderText(spanned: Spanned?) {
        vb.tvHeader.setTextOrHide(spanned)
    }

    private fun setSubtitleText(text: String?) {
        vb.tvSubtitle.setTextOrHide(text)
    }

    private fun setSubtitleText(spanned: Spanned?) {
        vb.tvSubtitle.setTextOrHide(spanned)
    }

    private fun setDescription(@StringRes resId: Int) {
        vb.tvDescription.apply {
            visible()
            setText(resId)
        }
    }

    private fun setDescription(description: String) {
        vb.tvDescription.apply {
            visible()
            text = description
        }
    }

    private fun setDescription(spanned: Spanned?) {
        vb.tvDescription.apply {
            text = spanned
            visible()
        }
    }

    private fun setPrimaryButton(@StringRes resId: Int, action: (DetailedDescriptionBottomSheet.() -> Unit)? = null) {
        vb.btnPrimary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@DetailedDescriptionBottomSheet) }
        }
    }

    private fun setSecondaryButton(@StringRes resId: Int, action: (DetailedDescriptionBottomSheet.() -> Unit)? = null) {
        vb.btnSecondary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@DetailedDescriptionBottomSheet) }
        }
    }

    private fun setPrimaryButton(text: String, action: (DetailedDescriptionBottomSheet.() -> Unit)? = null) {
        vb.btnPrimary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@DetailedDescriptionBottomSheet) }
        }
    }

    private fun setSecondaryButton(text: String, action: (DetailedDescriptionBottomSheet.() -> Unit)? = null) {
        vb.btnSecondary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@DetailedDescriptionBottomSheet) }
        }
    }

    fun setDismissEvent(dismissEvent: (() -> Unit)?) {
        this.dismissEvent = dismissEvent
    }

    fun hideCloseIcon(){
        this.hasCloseIcon = false
    }

    class Builder {
        private var descriptionText: String? = null
        private var descriptionTextSpanned: Spanned? = null
        private var descriptionTextResId: Int? = null
        private var descriptionTextMaxLines: Int? = null
        private var descTextTextAppearance: Int? = null

        private var headerText: String? = null
        private var headerTextSpanned: Spanned? = null
        private var headerTextAppearance: Int? = null

        private var subtitleText: String? = null
        private var subtitleTextSpanned: Spanned? = null
        private var subtitleTextAppearance: Int? = null

        private var iconRes: Int? = null
        private var iconUri: String? = null
        private var hasCloseIcon: Boolean = true
        private var iconSizeDimenRes: Int? = null

        private var primaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
        private var secondaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
        private var primaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>? = null
        private var secondaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>? = null

        private var isHideable: Boolean = true

        private var dismissEvent: (() -> Unit)? = null

        fun setDescription(text: String): Builder {
            this.descriptionText = text
            return this
        }

        fun setDescription(textSpanned: Spanned): Builder {
            this.descriptionTextSpanned = textSpanned
            return this
        }

        fun setDescription(textResId: Int): Builder {
            this.descriptionTextResId = textResId
            return this
        }

        fun setDescriptionTextAppearance(@StyleRes textAppearance: Int): Builder {
            this.descTextTextAppearance = textAppearance
            return this
        }

        fun setIcon(iconRes: Int): Builder {
            this.iconRes = iconRes
            return this
        }

        fun setIcon(iconUri: String?): Builder {
            this.iconUri = iconUri
            return this
        }

        fun setPrimaryButton(primaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>): Builder {
            this.primaryButton = primaryButton
            return this
        }

        fun setSecondaryButton(secondaryButton: Pair<String, (DetailedDescriptionBottomSheet.() -> Unit)>): Builder {
            this.secondaryButton = secondaryButton
            return this
        }

        fun setPrimaryButtonRes(primaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>): Builder {
            this.primaryButtonRes = primaryButtonRes
            return this
        }

        fun setSecondaryButtonRes(secondaryButtonRes: Pair<Int, (DetailedDescriptionBottomSheet.() -> Unit)>): Builder {
            this.secondaryButtonRes = secondaryButtonRes
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun setDismissEvent(dismissEvent: () -> Unit): Builder {
            this.dismissEvent = dismissEvent
            return this
        }

        fun hideCloseIcon(): Builder{
            this.hasCloseIcon = false
            return this
        }

        fun setIconSizeDimenRes(@DimenRes iconSizeDimenRes: Int): Builder {
            this.iconSizeDimenRes = iconSizeDimenRes
            return this
        }

        fun setDescTextMaxLines(maxLines: Int): Builder {
            this.descriptionTextMaxLines = maxLines
            return this
        }

        fun setHeaderText(headerText: String): Builder {
            this.headerText = headerText
            return this
        }

        fun setHeaderText(spanned: Spanned): Builder {
            this.headerTextSpanned = spanned
            return this
        }

        fun setHeaderTextAppearance(@StyleRes headerTextAppearance: Int): Builder {
            this.headerTextAppearance = headerTextAppearance
            return this
        }

        fun setSubtitleText(subtitleText: String): Builder {
            this.subtitleText = subtitleText
            return this
        }

        fun setSubtitleText(spanned: Spanned): Builder {
            this.subtitleTextSpanned = spanned
            return this
        }

        fun setSubtitleTextAppearance(@StyleRes subtitleTextAppearance: Int): Builder {
            this.subtitleTextAppearance = subtitleTextAppearance
            return this
        }

        fun build(): DetailedDescriptionBottomSheet {
            return DetailedDescriptionBottomSheet().apply {
                this.descriptionText = this@Builder.descriptionText
                this.descriptionTextSpanned = this@Builder.descriptionTextSpanned
                this.descriptionTextResId = this@Builder.descriptionTextResId
                this.descTextTextAppearance = this@Builder.descTextTextAppearance
                this.iconRes = this@Builder.iconRes
                this.iconUri = this@Builder.iconUri
                this.iconSizeDimenRes = this@Builder.iconSizeDimenRes
                this.primaryButton = this@Builder.primaryButton
                this.secondaryButton = this@Builder.secondaryButton
                this.primaryButtonRes = this@Builder.primaryButtonRes
                this.secondaryButtonRes = this@Builder.secondaryButtonRes
                this.isHideable = this@Builder.isHideable
                this.dismissEvent = this@Builder.dismissEvent
                this.hasCloseIcon = this@Builder.hasCloseIcon
                this.headerText = this@Builder.headerText
                this.headerTextSpanned = this@Builder.headerTextSpanned
                this.headerTextAppearance = this@Builder.headerTextAppearance
                this.subtitleText = this@Builder.subtitleText
                this.subtitleTextSpanned = this@Builder.subtitleTextSpanned
                this.subtitleTextAppearance = this@Builder.subtitleTextAppearance
                this@Builder.descriptionTextMaxLines?.let { this.descriptionTextMaxLines = it }
            }
        }
    }
}