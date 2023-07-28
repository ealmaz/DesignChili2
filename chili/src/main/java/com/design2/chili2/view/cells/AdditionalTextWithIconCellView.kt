package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.R

class AdditionalTextWithIconCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.additionalTextWithIconCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_AdditionalTextWithIcon
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var additionalText: TextView? = null
    private var additionalImage: ImageView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateAdditionalViews()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.AdditionalTextWithIconCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.AdditionalTextWithIconCellView_additionalText)?.let {
                    setAdditionalText(it)
                }
                getResourceId(R.styleable.AdditionalTextWithIconCellView_additionalTextTextAppearance, -1).takeIf { it != -1 }?.let {
                    setAdditionalTextTextAppearance(it)
                }
                getResourceId(R.styleable.AdditionalTextWithIconCellView_additionalIcon, -1).takeIf { it != -1 }?.let {
                    setAdditionalIcon(it)
                }
                recycle()
            }
    }

    private fun inflateAdditionalViews() {
        this.additionalText = TextView(context).apply {
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
            textAlignment = TEXT_ALIGNMENT_TEXT_END
            shimmeringPairs[this] = null
        }
        this.additionalImage = ImageView(context).apply {
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
            shimmeringPairs[this] = null
        }
        view.flEndPlaceholder.addView(LinearLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT)
            gravity = Gravity.CENTER_VERTICAL
            orientation = LinearLayout.HORIZONTAL
            addView(this@AdditionalTextWithIconCellView.additionalText)
            addView(this@AdditionalTextWithIconCellView.additionalImage)
        })
    }

    fun setAdditionalText(text: String?) {
        additionalText?.text = text
    }

    fun setAdditionalText(spanned: Spanned) {
        additionalText?.text = spanned
    }

    fun setAdditionalText(@StringRes textResId: Int) {
        additionalText?.setText(textResId)
    }

    override fun setIsChevronVisible(isVisible: Boolean) {
        super.setIsChevronVisible(isVisible)
        if (isVisible) additionalImage?.setPadding(0, 0,0, 0)
        else additionalImage?.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int) {
        additionalText?.setTextAppearance(resId)
    }

    fun setAdditionalIcon(resId: Int) {
        additionalImage?.setImageResource(resId)
    }

    fun setAdditionalIcon(drawable: Drawable) {
        additionalImage?.setImageDrawable(drawable)
    }
}