package com.design2.chili2.view.cells

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.design2.chili2.R

class ToggleCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toggleCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_Toggle
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var switch: SwitchCompat? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateSwitch()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.ToggleCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.ToggleCellView_switch_text)?.let { setSwitchText(it) }
                getString(R.styleable.ToggleCellView_switch_textOn)?.let { setSwitchTextOn(it) }
                getString(R.styleable.ToggleCellView_switch_textOff)?.let { setSwitchTextOff(it) }
                getBoolean(R.styleable.ToggleCellView_isSwitchChecked, false).let {
                    setIsSwitchChecked(it)
                }
                getBoolean(R.styleable.ToggleCellView_isSwitchVisible, true).let {
                    setIsSwitchVisible(it)
                }
                recycle()
        }
    }

    private fun inflateSwitch() {
        this.switch = SwitchCompat(context)
        vb.flEndPlaceHolder.addView(switch)
    }

    fun setIsSwitchVisible(isVisible: Boolean) {
        switch?.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setIsSwitchChecked(isChecked: Boolean) {
        switch?.isChecked = isChecked
    }

    fun setOnCheckChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        switch?.setOnCheckedChangeListener(listener)
    }

    fun setSwitchText(text: String) {
        switch?.text = text
    }

    fun setSwitchTextOn(text: String) {
        switch?.apply {
            showText = true
            textOn = text
        }
    }

    fun setSwitchTextOff(text: String) {
        switch?.apply {
            showText = true
            textOff = text
        }
    }

    fun setThumbDrawable(@DrawableRes drawable: Int) {
        switch?.setThumbResource(drawable)
    }

    fun setTrackDrawable(@DrawableRes drawable: Int) {
        switch?.setTrackResource(drawable)
    }

    fun setSwitchSize(@DimenRes heightRes: Int, @DimenRes widthRes: Int) {
        switch?.apply {
            layoutParams.height = resources.getDimensionPixelSize(heightRes)
            layoutParams.width = resources.getDimensionPixelSize(widthRes)
        }
    }

    override fun onStartShimmer() {
        super.onStartShimmer()
        switch?.isEnabled = false
    }

    override fun onStopShimmer() {
        super.onStopShimmer()
        switch?.isEnabled = true
    }

    fun setSwitchNewStyle() {
        setSwitchSize(
            heightRes = R.dimen.view_24dp,
            widthRes = R.dimen.view_40dp
        )
        setTrackDrawable(R.drawable.chili_switch_track)
        setThumbDrawable(R.drawable.chili_switch_thumb)
        setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_10dp), 0)
    }
}