package com.design2.chili2.view.modals.bottom_sheet

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetDescriptionBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment

class DescriptionBottomSheet : BaseViewBottomSheetDialogFragment() {

    private lateinit var vb : ChiliViewBottomSheetDescriptionBinding

    private var title: String? = null
    private var titleSpanned: Spanned? = null
    private var titleResId: Int? = null

    private var description: String? = null
    private var descriptionSpanned: Spanned? = null
    private var descriptionResId: Int? = null

    private var descriptionSecondary: String? = null
    private var descriptionSecondarySpanned: Spanned? = null
    private var descriptionSecondaryResId: Int? = null

    private var iconRes: Int? = null

    private var secondaryButton: Pair<String, (DescriptionBottomSheet.() -> Unit)>? = null
    private var secondaryButtonRes: Pair<Int, (DescriptionBottomSheet.() -> Unit)>? = null

    override var topDrawableVisible = true
    override var hasCloseIcon: Boolean = false
    override var closeIconView: View? = null

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        vb = ChiliViewBottomSheetDescriptionBinding.inflate(inflater, container, false).apply { 
            root.setBackgroundResource(R.drawable.chili_bg_rounded_bottom_sheet)
        }
        closeIconView = vb.ivDescClose
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        title?.let { setTitle(it) }
        titleSpanned?.let { setTitle(it) }
        titleResId?.let { setTitle(it) }

        description?.let { setDescription(it) }
        descriptionSpanned?.let { setDescription(it) }
        descriptionResId?.let { setDescription(it) }

        descriptionSecondary?.let { setDescriptionSecondary(it) }
        descriptionSecondarySpanned?.let { setDescriptionSecondary(it) }
        descriptionSecondaryResId?.let { setDescriptionSecondary(it) }

        iconRes?.let { setIcon(it) }

        secondaryButton?.let { setSecondaryButton(it.first, it.second) }
        secondaryButtonRes?.let { setSecondaryButton(it.first, it.second) }
    }

    private fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    private fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.apply {
            visible()
            setText(resId)
        }
    }

    private fun setTitle(message: String) {
        vb.tvTitle.apply {
            visible()
            text = message
        }
    }

    private fun setTitle(spanned: Spanned?) {
        vb.tvTitle.apply {
            text = spanned
            visible()
        }
    }

    private fun setDescription(@StringRes resId: Int) {
        vb.tvDesc.apply {
            visible()
            setText(resId)
        }
    }

    private fun setDescription(message: String) {
        vb.tvDesc.apply {
            visible()
            text = message
        }
    }

    private fun setDescription(spanned: Spanned?) {
        vb.tvDesc.apply {
            text = spanned
            visible()
        }
    }

    private fun setDescriptionSecondary(@StringRes resId: Int) {
        vb.tvDescSecondary.apply {
            visible()
            setText(resId)
        }
    }

    private fun setDescriptionSecondary(message: String) {
        vb.tvDescSecondary.apply {
            visible()
            text = message
        }
    }

    private fun setDescriptionSecondary(spanned: Spanned?) {
        vb.tvDescSecondary.apply {
            text = spanned
            visible()
        }
    }

    private fun setSecondaryButton(
        @StringRes resId: Int,
        action: (DescriptionBottomSheet.() -> Unit)? = null
    ) {
        vb.btnSecondary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@DescriptionBottomSheet) }
        }
    }

    private fun setSecondaryButton(
        text: String,
        action: (DescriptionBottomSheet.() -> Unit)? = null
    ) {
        vb.btnSecondary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@DescriptionBottomSheet) }
        }
    }

    class Builder {
        private var title: String? = null
        private var titleSpanned: Spanned? = null
        private var titleResId: Int? = null

        private var description: String? = null
        private var descriptionSpanned: Spanned? = null
        private var descriptionResId: Int? = null

        private var descriptionSecondary: String? = null
        private var descriptionSecondarySpanned: Spanned? = null
        private var descriptionSecondaryResId: Int? = null

        private var iconRes: Int? = null
        private var hasCloseIcon: Boolean = false

        private var secondaryButton: Pair<String, (DescriptionBottomSheet.() -> Unit)>? = null
        private var secondaryButtonRes: Pair<Int, (DescriptionBottomSheet.() -> Unit)>? = null

        private var isHideable: Boolean = true

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setTitle(titleSpanned: Spanned): Builder {
            this.titleSpanned = titleSpanned
            return this
        }

        fun setTitle(titleResId: Int): Builder {
            this.titleResId = titleResId
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setDescription(descriptionSpanned: Spanned): Builder {
            this.descriptionSpanned = descriptionSpanned
            return this
        }

        fun setDescription(descriptionResId: Int): Builder {
            this.descriptionResId = descriptionResId
            return this
        }

        fun setDescriptionSecondary(descriptionSecondary: String): Builder {
            this.descriptionSecondary = descriptionSecondary
            return this
        }

        fun setDescriptionSecondary(descriptionSecondarySpanned: Spanned): Builder {
            this.descriptionSecondarySpanned = descriptionSecondarySpanned
            return this
        }

        fun setDescriptionSecondary(descriptionSecondaryResId: Int): Builder {
            this.descriptionSecondaryResId = descriptionSecondaryResId
            return this
        }

        fun setIcon(iconRes: Int): Builder {
            this.iconRes = iconRes
            return this
        }

        fun setSecondaryButton(secondaryButton: Pair<String, (DescriptionBottomSheet.() -> Unit)>): Builder {
            this.secondaryButton = secondaryButton
            return this
        }

        fun setSecondaryButtonRes(secondaryButtonRes: Pair<Int, (DescriptionBottomSheet.() -> Unit)>): Builder {
            this.secondaryButtonRes = secondaryButtonRes
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun showCloseIcon(): Builder {
            this.hasCloseIcon = true
            return this
        }

        fun build(): DescriptionBottomSheet {
            return DescriptionBottomSheet().apply {
                this.title = this@Builder.title
                this.titleSpanned = this@Builder.titleSpanned
                this.titleResId = this@Builder.titleResId
                this.description = this@Builder.description
                this.descriptionSpanned = this@Builder.descriptionSpanned
                this.descriptionResId = this@Builder.descriptionResId
                this.descriptionSecondary = this@Builder.descriptionSecondary
                this.descriptionSecondarySpanned = this@Builder.descriptionSecondarySpanned
                this.descriptionSecondaryResId = this@Builder.descriptionSecondaryResId
                this.iconRes = this@Builder.iconRes
                this.secondaryButton = this@Builder.secondaryButton
                this.secondaryButtonRes = this@Builder.secondaryButtonRes
                this.isHideable = this@Builder.isHideable
                this.hasCloseIcon = this@Builder.hasCloseIcon
            }
        }
    }

}