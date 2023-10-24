package com.design2.chili2.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewAdditionalDoubleButtonsBinding

class AdditionalDoubleButtonsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private lateinit var vb: ChiliViewAdditionalDoubleButtonsBinding

    init {
        initView(context)
        obtainAttributes(context, attrs)
    }

    private fun initView(context: Context) {
        vb = ChiliViewAdditionalDoubleButtonsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.AdditionalDoubleButtonsView).run {
            getString(R.styleable.AdditionalDoubleButtonsView_firstButtonText)?.let {
                setFirstButtonText(it)
            }
            getString(R.styleable.AdditionalDoubleButtonsView_secondButtonText)?.let {
                setSecondButtonText(it)
            }
            recycle()
        }
    }

    fun setFirstButtonText(text: String) {
        vb.btnFirst.text = text
    }
    fun setFirstButtonText(@StringRes textResId: Int) {
        vb.btnFirst.setText(textResId)
    }

    fun setSecondButtonText(text: String) {
        vb.btnSecond.text = text
    }
    fun setSecondButtonText(@StringRes textResId: Int) {
        vb.btnSecond.setText(textResId)
    }

    fun setFirstButtonOnClickListener(onClick: () -> Unit) {
        vb.btnFirst.setOnClickListener { onClick.invoke() }
    }

    fun setSecondButtonOnClickListener(onClick: () -> Unit) {
        vb.btnSecond.setOnClickListener { onClick.invoke() }
    }

    fun setIsButtonsEnabled(isEnabled: Boolean){
        vb.btnFirst.isEnabled = isEnabled
        vb.btnSecond.isEnabled = isEnabled
    }

}