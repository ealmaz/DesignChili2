package com.design2.chili2.view.modals.bottom_sheet_constructor

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.view.View.TEXT_ALIGNMENT_CENTER
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.GravityInt
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class UniversalBottomSheetConstructor(config: BottomSheetConfig, val context: Context) {

    val resultBS = ConfigurableBottomSheetFragment(config)

    fun block(@GravityInt gravity: Int, orientation: Int, action: Block.() -> Unit) {
        Block(context, gravity, orientation, resultBS).apply {
            action.invoke(this)
            resultBS.addViewBlock(this.build())
        }
    }

    fun build(): BottomSheetDialogFragment {
        return resultBS
    }

}

data class BottomSheetConfig(
    val isHideable: Boolean = true,
    val isTopDrawableVisible: Boolean = false,
    val implementCloseIcon: Boolean = true,
    val onDismissCallback: (() -> Unit)? = null,
    var state: Int = BottomSheetBehavior.STATE_EXPANDED
)

class Block(val context: Context, @GravityInt val gravity: Int, val orientation: Int, val bs: BottomSheetDialogFragment) {

    val result = LinearLayout(context).apply {
        gravity = this@Block.gravity
        orientation = this@Block.orientation
    }

    fun build(): LinearLayout {
        return result
    }


    fun image(@DrawableRes src: Int? = null, @DimenRes size: Int? = null, drawable: Drawable? = null, margins: Margins? = null, imageUrl: String? = null, placeholder: Drawable? = null) {
        val actualSize = getSizeInPxOrWrapContent(size)
        ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(actualSize, actualSize).apply {
                margins?.let { setMargins(it.left, it.top, it.right, it.bottom) }
            }
            src?.let { setImageResource(src) }
            imageUrl?.let { setImageByUrl(it, placeholder) }
            drawable?.let { setImageDrawable(drawable) }
            result.addView(this)
        }
    }

    fun text(@StringRes textRes: Int? = null, textCharSequence: CharSequence? = null, @StyleRes textAppearance: Int? = null, isCentered: Boolean = false, margins: Margins? = null) {
        TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                margins?.let { setMargins(it.left, it.top, it.right, it.bottom) }
            }
            if (isCentered) textAlignment = TEXT_ALIGNMENT_CENTER
            textRes?.let { setText(it) }
            textCharSequence?.let { text = textCharSequence }
            textAppearance?.let { setTextAppearance(textAppearance) }
            result.addView(this)
        }
    }

    fun button(@StringRes textRes: Int? = null, textCharSequence: CharSequence? = null, @StyleRes buttonStyle: Int = R.style.Chili_ButtonStyle_Primary, margins: Margins? = null, onClick: () -> Unit) {
        Button(context, null, 0, buttonStyle).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f).apply {
                margins?.let { setMargins(it.left, it.top, it.right, it.bottom) }
            }
            textRes?.let { setText(it) }
            textCharSequence?.let { text = textCharSequence }
            setOnSingleClickListener {
                onClick.invoke()
                bs.dismiss()
            }
            result.addView(this)
        }
    }

    fun customView(view: View) {
        result.addView(view)
    }


    private fun getSizeInPxOrWrapContent(@DimenRes size: Int?): Int {
        return when (size == null) {
            true -> LinearLayout.LayoutParams.WRAP_CONTENT
            else -> context.resources.getDimensionPixelSize(size)
        }
    }
}

data class Margins(val left: Int = 0, val top: Int = 0, val right: Int = 0, val bottom: Int = 0)

fun Context.buildBottomSheet(config: BottomSheetConfig, action: UniversalBottomSheetConstructor.() -> Unit): BottomSheetDialogFragment {
    val constructor = UniversalBottomSheetConstructor(config, this)
    action.invoke(constructor)
    return constructor.build()
}

