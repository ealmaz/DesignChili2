package com.design2.chili2.view.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewInputUneditableBinding
import com.design2.chili2.extensions.getColorFromAttr

class UneditableInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.uneditableInputViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_InputViewStyle_Uneditable
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes)  {

    private lateinit var vb: ChiliViewInputUneditableBinding

    init {
        initView(context)
        obtainAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewInputUneditableBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context?.obtainStyledAttributes(attrs, R.styleable.UneditableInputView, defStyleAttr, defStyle)?.run {
            getString(R.styleable.UneditableInputView_android_hint)?.let { hint ->
                setHint(hint)
            }
            getString(R.styleable.UneditableInputView_android_text)?.let { text ->
                setText(text)
            }
            getResourceId(R.styleable.UneditableInputView_fieldBackground, R.color.gray_5).let {
                setupFieldBackground(it)
            }
            recycle()
        }
    }

    fun getInputText(): String {
        return when {
            !isInputEmpty() -> vb.tvInput.text.toString()
            else -> ""
        }
    }

    fun isInputEmpty() = vb.tvInput.currentTextColor == context.getColorFromAttr(R.attr.ChiliInputViewHintTextColor)

    fun setHint(hint: String?) {
        this.tag = hint
        vb.tvInput.hint = hint
        vb.tvInput.setHintTextColor(context.getColorFromAttr(R.attr.ChiliInputViewHintTextColor))
    }

    fun setText(label: String?) {
        vb.tvInput.run {
            text = label
            setTextColor(context.getColorFromAttr(R.attr.ChiliPrimaryTextColor))
        }
    }

    fun resetText() {
        setText("")
        setHint(tag.toString())
    }

    fun setupFieldBackground(resId: Int) {
        vb.tvInput.setBackgroundResource(resId)
    }
}