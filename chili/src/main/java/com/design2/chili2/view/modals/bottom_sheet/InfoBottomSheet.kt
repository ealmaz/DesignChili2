package com.design2.chili2.view.modals.bottom_sheet

import android.content.DialogInterface
import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment

class InfoBottomSheet private constructor(): BaseViewBottomSheetDialogFragment() {

    private lateinit var tvText: TextView
    private lateinit var ivIcon: ImageView
    private lateinit var btnPrimary: Button
    private lateinit var btnSecondary: Button

    private var text: String? = null
    private var textSpanned: Spanned? = null
    private var textResId: Int? = null

    private var iconRes: Int? = null
    private var iconUri: String? = null

    private var primaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>? = null
    private var secondaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>? = null
    private var primaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>? = null
    private var secondaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>? = null

    override var hasCloseIcon: Boolean = true
    override var closeIconView: View? = null

    private var dismissEvent: (() -> Unit)? = null

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val view = inflater.inflate(R.layout.chili_view_bottom_sheet_info, container, false).apply {
            setBackgroundResource(R.drawable.chili_bg_rounded_bottom_sheet)
        }
        closeIconView = view.findViewById(R.id.iv_info_close)
        tvText = view.findViewById(R.id.tv_text)
        ivIcon = view.findViewById(R.id.iv_icon)
        btnPrimary = view.findViewById(R.id.btn_primary)
        btnSecondary = view.findViewById(R.id.btn_secondary)
        return view
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
        text?.let { setMessage(it) }
        textSpanned?.let { setMessage(it) }
        textResId?.let { setMessage(it)}

        iconRes?.let { setIcon(it) }
        iconUri?.let { setIcon(it) }

        primaryButton?.let { setPrimaryButton(it.first, it.second) }
        secondaryButton?.let { setSecondaryButton(it.first, it.second) }
        primaryButtonRes?.let { setPrimaryButton(it.first, it.second) }
        secondaryButtonRes?.let { setSecondaryButton(it.first, it.second) }
    }

    private fun setIcon(@DrawableRes resId: Int) {
        ivIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    private fun setIcon(uri: String?){
        ivIcon.apply {
            setImageByUrl(uri)
            visible()
        }
    }

    private fun setMessage(@StringRes resId: Int) {
        tvText.apply {
            visible()
            setText(resId)
        }
    }

    private fun setMessage(message: String) {
        tvText.apply {
            visible()
            text = message
        }
    }

    private fun setMessage(spanned: Spanned?) {
        tvText.apply {
            text = spanned
            visible()
        }
    }

    private fun setPrimaryButton(@StringRes resId: Int, action: (InfoBottomSheet.() -> Unit)? = null) {
        btnPrimary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@InfoBottomSheet) }
        }
    }

    private fun setSecondaryButton(@StringRes resId: Int, action: (InfoBottomSheet.() -> Unit)? = null) {
        btnSecondary.apply {
            visible()
            setText(resId)
            setOnSingleClickListener { action?.invoke(this@InfoBottomSheet) }
        }
    }

    private fun setPrimaryButton(text: String, action: (InfoBottomSheet.() -> Unit)? = null) {
        btnPrimary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@InfoBottomSheet) }
        }
    }

    private fun setSecondaryButton(text: String, action: (InfoBottomSheet.() -> Unit)? = null) {
        btnSecondary.apply {
            visible()
            setText(text)
            setOnSingleClickListener { action?.invoke(this@InfoBottomSheet) }
        }
    }

    fun setDismissEvent(dismissEvent: (() -> Unit)?) {
        this.dismissEvent = dismissEvent
    }

    fun hideCloseIcon(){
        this.hasCloseIcon = false
    }

    class Builder {
        private var text: String? = null
        private var textSpanned: Spanned? = null
        private var textResId: Int? = null

        private var iconRes: Int? = null
        private var iconUri: String? = null
        private var hasCloseIcon: Boolean = true

        private var primaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>? = null
        private var secondaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>? = null
        private var primaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>? = null
        private var secondaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>? = null

        private var isHideable: Boolean = true

        private var dismissEvent: (() -> Unit)? = null

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

        fun setIcon(iconUri: String?): Builder {
            this.iconUri = iconUri
            return this
        }

        fun setPrimaryButton(primaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>): Builder {
            this.primaryButton = primaryButton
            return this
        }

        fun setSecondaryButton(secondaryButton: Pair<String, (InfoBottomSheet.() -> Unit)>): Builder {
            this.secondaryButton = secondaryButton
            return this
        }

        fun setPrimaryButtonRes(primaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>): Builder {
            this.primaryButtonRes = primaryButtonRes
            return this
        }

        fun setSecondaryButtonRes(secondaryButtonRes: Pair<Int, (InfoBottomSheet.() -> Unit)>): Builder {
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

        fun build(): InfoBottomSheet {
            return InfoBottomSheet().apply {
                this.text = this@Builder.text
                this.textSpanned = this@Builder.textSpanned
                this.textResId = this@Builder.textResId
                this.iconRes = this@Builder.iconRes
                this.iconUri = this@Builder.iconUri
                this.primaryButton = this@Builder.primaryButton
                this.secondaryButton = this@Builder.secondaryButton
                this.primaryButtonRes = this@Builder.primaryButtonRes
                this.secondaryButtonRes = this@Builder.secondaryButtonRes
                this.isHideable = this@Builder.isHideable
                this.dismissEvent = this@Builder.dismissEvent
                this.hasCloseIcon = this@Builder.hasCloseIcon
            }
        }
    }
}