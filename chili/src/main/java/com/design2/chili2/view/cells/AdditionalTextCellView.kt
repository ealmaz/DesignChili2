package com.design2.chili2.view.cells

import android.content.Context
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.design2.chili2.R
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView
import com.design2.chili2.extensions.setupConstraint
import com.facebook.shimmer.ShimmerFrameLayout

class AdditionalTextCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.additionalTextCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_AdditionalText
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var additionalText: TextView? = null

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
                recycle()
            }
    }

    private fun inflateAdditionalText() {
        this.additionalText = TextView(context).apply {
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
            textAlignment = TEXT_ALIGNMENT_TEXT_END
            shimmeringPairs[this] = createShimmerForAdditionalText()
            maxEms = 8
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.END
        }
        vb.flEndPlaceHolder.addView(additionalText)
    }

    private fun createShimmerForAdditionalText(): ShimmerFrameLayout = with(vb) {
        val shimmerLayout = context.createShimmerLayout {
            setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
        }
        shimmerLayout.addView(context.createShimmerView(R.dimen.view_46dp))
        rootView.addView(shimmerLayout)
        rootView.setupConstraint {
            connect(shimmerLayout.id, BOTTOM, PARENT_ID, BOTTOM, 0)
            connect(shimmerLayout.id, END, R.id.iv_chevron, START, 0)
            connect(shimmerLayout.id, TOP, PARENT_ID, TOP, 0)
        }
        return shimmerLayout
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
        if (isVisible) additionalText?.setPadding(0, 0,0, 0)
        else additionalText?.setPadding(0, 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int) {
        additionalText?.setTextAppearance(resId)
    }
}