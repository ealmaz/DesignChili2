package com.design2.chili2.view.common

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewAdditionalTextBinding

class AdditionalTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
) : LinearLayout(context, attrs) {

    private var vb: ChiliViewAdditionalTextBinding

    init {
        vb = ChiliViewAdditionalTextBinding.inflate(LayoutInflater.from(context), this, true)
        obtainAttributes(context, attrs)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?) {
        context.obtainStyledAttributes(attrs, R.styleable.AdditionalTextView).run {
            setText(getString(R.styleable.AdditionalTextView_android_text))
            getResourceId(R.styleable.AdditionalTextView_android_textAppearance, -1).takeIf { it != -1 }?.let {
                setTextAppearance(it)
            }
            recycle()
        }
    }

    fun setText(text: String?) {
        vb.tvText.text = text
    }

    fun setText(textResId: Int) {
        vb.tvText.setText(textResId)
    }

    fun setTextAppearance(@StyleRes resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.tvText.setTextAppearance(resId)
        } else {
            vb.tvText.setTextAppearance(context, resId)
        }
    }
}

