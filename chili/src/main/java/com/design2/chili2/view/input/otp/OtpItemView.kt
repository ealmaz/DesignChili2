package com.design2.chili2.view.input.otp

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.design2.chili2.R

class OtpItemView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    init {
        textAlignment = TEXT_ALIGNMENT_CENTER
        setState(OtpItemState.INACTIVE)
    }

    fun setState(state: OtpItemState) {
        val background = when (state) {
            OtpItemState.INACTIVE -> R.drawable.chili_bg_input_otp_item_inactive
            OtpItemState.ACTIVE -> R.drawable.chili_bg_input_otp_item_active
            OtpItemState.ERROR -> R.drawable.chili_bg_input_otp_item_error
        }
        setBackgroundResource(background)
    }
}

enum class OtpItemState {
    INACTIVE, ACTIVE, ERROR
}