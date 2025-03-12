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
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.updatePadding
import com.design2.chili2.R
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setRippleForeground
import com.design2.chili2.extensions.setupAsSecure
import com.facebook.shimmer.ShimmerFrameLayout

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
                getDimensionPixelSize(R.styleable.AdditionalTextWithIconCellView_additionalIconSize, -1).takeIf { it != -1 }?.let {
                    setupAdditionalIconSize(it, it)
                }

                getBoolean(R.styleable.AdditionalTextWithIconCellView_isAdditionalFieldShimmering, false).takeIf { it }?.let {
                    setIsAdditionalFieldShimmering(true)
                }
                recycle()
            }
    }

    private fun createShimmerForAdditionalText(): ShimmerFrameLayout {
        return context.createShimmerLayout().apply {
            addView(context.createShimmerView(R.dimen.view_46dp))
            vb.flEndPlaceHolder.addView(this)
        }
    }

    fun setIsAdditionalFieldShimmering(isShimmering: Boolean = false) {
        additionalText?.let { textView ->
            shimmeringPairs[textView] = if (isShimmering) {
                createShimmerForAdditionalText()
            } else null
        }
    }

    private fun inflateAdditionalViews() {
        this.additionalText = TextView(context).apply {
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
            textAlignment = TEXT_ALIGNMENT_TEXT_END
            shimmeringPairs[this] = null
        }
        this.additionalImage = ImageView(context).apply {
            shimmeringPairs[this] = null
        }
        with(vb.flEndPlaceHolder) {
            addView(LinearLayout(context).apply {
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                )
                gravity = Gravity.CENTER_VERTICAL
                orientation = LinearLayout.HORIZONTAL
                addView(this@AdditionalTextWithIconCellView.additionalText)
                addView(this@AdditionalTextWithIconCellView.additionalImage)
            })
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
        }
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
        with(vb.flEndPlaceHolder) {
            if (isVisible) setPadding(0, 0, 0, 0)
            else setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_12dp), 0)
        }
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

    fun setAdditionalIconClickListener(action: () -> Unit = {}) {
        additionalImage?.setOnSingleClickListener { action.invoke() }
    }

    fun setAdditionalTextPadding(left: Int, top: Int, right: Int, bottom: Int) {
        additionalText?.updatePadding(left, top, right, bottom)
    }

    fun setAdditionalIconSize(@DimenRes sizeDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(sizeDimenRes)
        val heightPx = resources.getDimensionPixelSize(sizeDimenRes)
        setupAdditionalIconSize(widthPx, heightPx)
    }

    fun setupAdditionalIconSize(widthPx: Int, heightPx: Int) {
        val params = additionalImage?.layoutParams
        params?.height = heightPx
        params?.width = widthPx
        additionalImage?.layoutParams = params
    }

    fun setAdditionalEndIconClickListener(action: () -> Unit) {
        additionalImage?.apply {
            setOnSingleClickListener { action.invoke() }
            setRippleForeground(isSurfaceClickable = true)
        }
    }

    fun setupAdditionalTextAsSecure() {
        additionalText?.setupAsSecure()
    }
}