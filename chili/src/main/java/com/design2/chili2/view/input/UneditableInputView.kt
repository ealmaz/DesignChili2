package com.design2.chili2.view.input

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.extensions.getColorFromAttr

class UneditableInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.uneditableInputViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_InputViewStyle_Uneditable
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes)  {

    lateinit var view: UneditableInputViewViewVariables

    init {
        initView(context)
        obtainAttributes(attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_input_uneditable, this)
        this.view = UneditableInputViewViewVariables(
            tvInput = view.findViewById(R.id.tv_input)
        )
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
            !isInputEmpty() -> view.tvInput.text.toString()
            else -> ""
        }
    }

    fun isInputEmpty() = view.tvInput.currentTextColor == context.getColorFromAttr(R.attr.ChiliInputViewHintTextColor)

    fun setHint(hint: String?) {
        this.tag = hint
        view.tvInput.hint = hint
        view.tvInput.setHintTextColor(context.getColorFromAttr(R.attr.ChiliInputViewHintTextColor))
    }

    fun setText(label: String?) {
        view.tvInput.run {
            text = label
            setTextColor(context.getColorFromAttr(R.attr.ChiliPrimaryTextColor))
        }
    }

    fun resetText() {
        setText("")
        setHint(tag.toString())
    }

    private fun setupFieldBackground(resId: Int) {
        view.tvInput.setBackgroundResource(resId)
    }
}

data class UneditableInputViewViewVariables(
    val tvInput: AppCompatTextView)
