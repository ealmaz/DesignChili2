package com.design2.chili2.view.cells

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.View
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.core.view.get
import com.design2.chili2.R

class CheckBoxCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.checkBoxCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_CheckBox
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var checkBox: AppCompatCheckBox? = null
    private var endText: TextView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateCheckBox()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.CheckBoxCellView, defStyleAttr, defStyleRes)
            .run {
                getBoolean(R.styleable.CheckBoxCellView_isCheckBoxVisible, true).let {
                    setIsCheckBoxVisible(it)
                }
                getBoolean(R.styleable.CheckBoxCellView_isCheckBoxChecked, false).let {
                    setIsCheckBoxChecked(it)
                }
                getResourceId(R.styleable.CheckBoxCellView_checkBoxEndMargin, R.dimen.view_8dp).let {
                    setCheckBoxEndMargin(it)
                }
                recycle()
            }
    }

    private fun inflateCheckBox() {
        checkBox = AppCompatCheckBox(context)
        vb.flEndPlaceHolder.addView(LinearLayout(context).apply {
            this.orientation = LinearLayout.HORIZONTAL
        })
        (vb.flEndPlaceHolder[0] as LinearLayout).addView(checkBox)
    }

    fun insertEndText(text: String) {
        if (endText != null){
            setEndText(text)
            return
        }
        endText = TextView(context).apply {
            setTextAppearance(R.style.Chili_H7_Primary)
            setText(text)
        }
        (vb.flEndPlaceHolder[0] as LinearLayout).addView(endText, 0)
    }
    fun insertEndText(text: Spanned) {
        if (endText != null){
            setEndText(text)
            return
        }
        endText = TextView(context).apply {
            setTextAppearance(R.style.Chili_H7_Primary)
            setText(text)
        }
        (vb.flEndPlaceHolder[0] as LinearLayout).addView(endText, 0)
    }

    fun setEndText(text: String) {
        endText?.text = text
    }

    fun setEndText(text: Spanned) {
        endText?.text = text
    }

    fun setIsCheckBoxVisible(isVisible: Boolean) {
        checkBox?.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setIsCheckBoxChecked(isChecked: Boolean) {
        checkBox?.isChecked = isChecked
    }

    fun setOnCheckChangeListener(listener: (CompoundButton, Boolean) -> Unit) {
        checkBox?.setOnCheckedChangeListener(listener)
    }

    fun setCheckBoxEndMargin(@DimenRes endMarginRes: Int) {
        val endMarginPx = resources.getDimensionPixelSize(endMarginRes)
        val param = vb.flEndPlaceHolder.layoutParams as? MarginLayoutParams ?: return
        param.marginEnd = endMarginPx
        vb.flEndPlaceHolder.layoutParams = param
    }

    override fun onStartShimmer() {
        super.onStartShimmer()
        checkBox?.isEnabled = false
    }

    override fun onStopShimmer() {
        super.onStopShimmer()
        checkBox?.isEnabled = true
    }
}