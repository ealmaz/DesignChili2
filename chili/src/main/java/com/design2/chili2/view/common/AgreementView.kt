package com.design2.chili2.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewAgreementBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.handleUrlClicks
import com.design2.chili2.extensions.visible

class AgreementView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private lateinit var vb: ChiliViewAgreementBinding

    init {
        initView(context)
        obtainAttributes(context, attrs)
    }

    private fun initView(context: Context) {
        vb = ChiliViewAgreementBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.AgreementView).run {
            getString(R.styleable.AgreementView_title)?.let { title -> setTitle(title) }
            setChecked(getBoolean(R.styleable.AgreementView_android_checked, false))
            setIsEditable(getBoolean(R.styleable.AgreementView_isEditable, true))
            recycle()
        }
    }

    fun setTitle(title: String) {
        vb.tvTitle.text = title.parseAsHtml()
    }

    fun onUrlClick(action: (url: String) -> (Unit)) {
        vb.tvTitle.handleUrlClicks { action.invoke(it) }
    }

    fun setChecked(isChecked: Boolean) {
        vb.cbAgreement.isChecked = isChecked
    }

    fun setTextAppearance(@StyleRes style:Int){
        vb.tvTitle.setTextAppearance(style)
    }
    fun setTextColorLink(@ColorRes color:Int){
        vb.tvTitle.setLinkTextColor(ContextCompat.getColor(context, color))
    }

    fun isCheckboxChecked(): Boolean = vb.cbAgreement.isChecked

    fun setOnCheckChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        vb.cbAgreement.setOnCheckedChangeListener(listener)
    }

    fun setIsEditable(isEditable: Boolean) {
        when (isEditable) {
            true -> editableMode()
            else -> nonEditableMode()
        }
    }

    fun setIsEnabled(isEnabled: Boolean) {
        vb.cbAgreement.isEnabled = isEnabled
    }

    fun hideCheckbox() {
        vb.cbAgreement.gone()
        vb.ivChecked.gone()
    }

    private fun editableMode() {
        vb.ivChecked.gone()
        vb.cbAgreement.visible()
    }

    private fun nonEditableMode() {
        vb.ivChecked.visible()
        vb.cbAgreement.gone()
    }
}