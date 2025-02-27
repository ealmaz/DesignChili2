package com.design2.chili2.view.cells

import android.content.Context
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.get
import com.design2.chili2.R
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupAsSecure
import com.facebook.shimmer.ShimmerFrameLayout

class AdditionalTextCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.additionalTextCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_AdditionalText
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var additionalTextContainer: LinearLayout? = null
    private var additionalText: TextView? = null
    private var additionalSubText: TextView? = null
    private var additionalEndIconRight: ImageView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateAdditionalText()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.AdditionalTextCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.AdditionalTextCellView_additionalText)?.let {
                    setAdditionalText(it)
                }
                getResourceId(R.styleable.AdditionalTextCellView_additionalTextTextAppearance, -1).takeIf { it != -1 }?.let {
                    setAdditionalTextTextAppearance(it)
                }
                getString(R.styleable.AdditionalTextCellView_additionalSubText).let {
                    setAdditionalSubText(it)
                }
                getResourceId(R.styleable.AdditionalTextCellView_additionalSubTextTextAppearance, -1).takeIf { it != -1 }?.let {
                    setAdditionalSubTextTextAppearance(it)
                }
                getBoolean(R.styleable.AdditionalTextCellView_additionalTextAsSecure, false).takeIf { it }?.let {
                    setupAdditionalTextAsSecure()
                }
                getResourceId(R.styleable.AdditionalTextCellView_additionalEndIcon, -1).takeIf { it != -1 }?.let {
                    setAdditionalEndIcon(it)
                }
                recycle()
            }
    }

    private fun inflateAdditionalText() {
        additionalText = createTextView().also {
            shimmeringPairs[it] = createShimmerForAdditionalText()
            additionalEndIconRight?.let {

            }
        }
        additionalSubText = createTextView().apply { setPadding(0, 4.dp, 0,0) }
        additionalEndIconRight = ImageView(context).apply {
            layoutParams = LinearLayout.LayoutParams(24.dp, 24.dp).apply {
                marginStart = 8.dp
            }
            visibility = GONE
        }
        additionalTextContainer = LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(additionalText)
                addView(additionalSubText)
            })
            addView(additionalEndIconRight)
            setPadding(0, 0, 12.dp, 0)
        }
        vb.flEndPlaceHolder.addView(additionalTextContainer)
    }

    private fun createTextView(): TextView {
        return TextView(context).apply {
            textAlignment = TEXT_ALIGNMENT_TEXT_END
            maxEms = 8
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
    }

    private fun createShimmerForAdditionalText(): ShimmerFrameLayout {
        val shimmerLayout = context.createShimmerLayout {
            setPadding(resources.getDimensionPixelSize(R.dimen.padding_8dp), 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
        }
        shimmerLayout.addView(context.createShimmerView(R.dimen.view_46dp))
        vb.flEndPlaceHolder.addView(shimmerLayout)
        return shimmerLayout
    }

    override fun setupIconShimmer() {
        super.setupIconShimmer()
        vb.viewTitleShimmer[0].layoutParams = vb.viewTitleShimmer[0].layoutParams.apply {
            width = 150.dp
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

    fun setAdditionalSubText(text: CharSequence?) {
        additionalSubText?.setTextOrHide(text)
    }

    fun setAdditionalSubText(text: Int?) {
        additionalSubText?.setTextOrHide(text)
    }

    fun setAdditionalSubTextTextAppearance(resId: Int) {
        additionalSubText?.setTextAppearance(resId)
    }

    override fun setIsChevronVisible(isVisible: Boolean) {
        super.setIsChevronVisible(isVisible)
        if (isVisible) additionalTextContainer?.setPadding(0, 0,0, 0)
        else additionalTextContainer?.setPadding(0, 0, 12.dp, 0)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int) {
        additionalText?.setTextAppearance(resId)
    }

    fun setAdditionalTextWidth(@DimenRes dimenResId: Int){
        additionalText?.width = resources.getDimensionPixelSize(dimenResId)
    }

    fun setAdditionalTextMaxLines(maxLines: Int){
        additionalText?.maxLines = maxLines
    }

    fun setAdditionalTextAlpha(alpha: Float = 1f) {
        additionalText?.alpha = alpha
    }

    fun updateAdditionalTextMargin(startMarginPx: Int? = null, topMarginPx: Int? = null, endMarginPx: Int? = null, bottomMarginPx: Int? = null) {
        val param = additionalText?.layoutParams as? MarginLayoutParams ?: return
        param.apply {
            leftMargin = startMarginPx ?: leftMargin
            topMargin = topMarginPx ?: topMargin
            rightMargin = endMarginPx ?: rightMargin
            bottomMargin = bottomMarginPx ?: bottomMargin
        }
        additionalText?.layoutParams = param
    }

    fun setupAdditionalTextAsSecure() {
        additionalText?.setupAsSecure()
    }

    fun setAdditionalEndIcon(iconRes: Int) {
        additionalEndIconRight?.apply {
            setImageResource(iconRes)
            visibility = VISIBLE
        }
    }

    fun setAdditionalEndIconClickListener(action: () -> Unit) {
        additionalEndIconRight?.setOnSingleClickListener(action)
    }

}