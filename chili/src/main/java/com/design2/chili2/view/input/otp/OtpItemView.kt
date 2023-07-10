package com.design2.chili2.view.input.otp

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.design2.chili2.R

class OtpItemView(context: Context, attrs: AttributeSet?) : AppCompatTextView(context, attrs) {

    private var currentState: OtpItemState = OtpItemState.INACTIVE

    init {
        textAlignment = TEXT_ALIGNMENT_CENTER
        setState(currentState)
    }

    fun setState(state: OtpItemState) {
        val background = when (state) {
            OtpItemState.INACTIVE -> R.drawable.chili_bg_input_otp_item_inactive
            OtpItemState.ACTIVE -> R.drawable.chili_bg_input_otp_item_active
            OtpItemState.ERROR -> getDrawableErrorState()
        }
        setBackgroundResource(background)
        currentState = state
    }

    private fun getDrawableErrorState(): Int {
        return when (currentState) {
            OtpItemState.ACTIVE -> R.drawable.chili_bg_input_otp_item_active_error
            else -> R.drawable.chili_bg_input_otp_item_error
        }
    }

}

enum class OtpItemState {
    INACTIVE, ACTIVE, ERROR
}