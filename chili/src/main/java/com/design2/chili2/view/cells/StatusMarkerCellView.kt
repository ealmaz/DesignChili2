package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.TOP
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellStatusMarkerMarkerBgBinding
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setupConstraint
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout

class StatusMarkerCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val defStyleAttr: Int = R.attr.statusMarkerCellViewDefaultStyle,
    private val defStyleRes: Int = R.style.Chili_CellViewStyle_StatusMarkerCellView
): BaseCellView(context, attrs, R.attr.cellViewDefaultStyle, R.style.Chili_CellViewStyle_BaseCellViewStyle) {

    private lateinit var statusVb: ChiliViewCellStatusMarkerMarkerBgBinding

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateStatusMarker()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttrParent: Int, defStyleResParet: Int) {
        super.obtainAttributes(context, attrs, defStyleAttrParent, defStyleResParet)
        context.obtainStyledAttributes(attrs, R.styleable.StatusMarkerCellView, defStyleAttr, defStyleRes).run {
            setStatusText(getText(R.styleable.StatusMarkerCellView_statusText))
            getResourceId(R.styleable.StatusMarkerCellView_statusTextTextAppearance, -1).takeIf { it != -1 }?.let {
                setStatusTextTextAppearance(it)
            }
            getResourceId(R.styleable.StatusMarkerCellView_statusIcon, -1).takeIf { it != -1 }?.let {
                setStatusIcon(it)
            }
            getColor(R.styleable.StatusMarkerCellView_statusBackgroundColor, -1).takeIf { it != -1 }?.let {
                setStatusBackgroundColor(it)
            }
            getColor(R.styleable.StatusMarkerCellView_statusTextColor, Int.MIN_VALUE).takeIf { it != Int.MIN_VALUE }?.let {
                setStatusTextColor(it)
            }
            recycle()
        }
    }

    private fun inflateStatusMarker() = with(vb) {
        val statusView = LayoutInflater.from(context).inflate(R.layout.chili_view_cell_status_marker_marker_bg, flEndPlaceHolder, false)
        statusVb = ChiliViewCellStatusMarkerMarkerBgBinding.bind(statusView).apply {
            shimmeringPairs[root] = createShimmerForStatusMarker()
        }
        statusView.rootView.gone()
        flEndPlaceHolder.addView(statusView)
    }

    private fun createShimmerForStatusMarker(): ShimmerFrameLayout = with(vb) {
        val shimmerLayout = context.createShimmerLayout {
            setPadding(resources.getDimensionPixelSize(R.dimen.padding_8dp), 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
        }
        shimmerLayout.addView(context.createShimmerView(R.dimen.view_40dp))
        rootView.addView(shimmerLayout)
        rootView.setupConstraint {
            connect(shimmerLayout.id, BOTTOM, PARENT_ID, BOTTOM, 0)
            connect(shimmerLayout.id, END, R.id.iv_chevron, START, 0)
            connect(shimmerLayout.id, TOP, PARENT_ID, TOP, 0)
            connect(R.id.view_title_shimmer, END, shimmerLayout.id, START, 0)
        }
        return shimmerLayout
    }

    fun setupStatus(
        status: CharSequence?,
        iconResId: Int?,
        @ColorInt textColorInt: Int? = null,
        @ColorInt backgroundColorInt: Int? = null
    ) {
        setStatusText(status)
        setStatusIcon(iconResId)
        textColorInt?.let { setStatusTextColor(it) }
        backgroundColorInt?.let { setStatusBackgroundColor(it) }
    }

    fun setStatusText(charSequence: CharSequence?) {
        statusVb.tvStatus.text = charSequence
        if (charSequence != null) statusVb.markerRootView.visible()
        else statusVb.markerRootView.gone()
    }

    fun setStatusText(resId: Int) {
        statusVb.tvStatus.setText(resId)
        statusVb.markerRootView.visible()
    }

    fun setStatusTextTextAppearance(resId: Int) {
        statusVb.tvStatus.setTextAppearance(resId)
    }

    fun setStatusTextColor(@ColorInt color: Int) {
        statusVb.tvStatus.setTextColor(color)
    }

    fun setStatusIcon(resId: Int?) = with(statusVb.ivIcon) {
        if (resId == null) gone()
        else {
            visible()
            statusVb.markerRootView.visible()
            setImageResource(resId)
        }
    }

    fun setStatusIcon(iconDrawable: Drawable?) = with(statusVb.ivIcon) {
        if (iconDrawable == null) gone()
        else {
            visible()
            statusVb.markerRootView.visible()
            setImageDrawable(drawable)
        }
    }

    fun setStatusBackgroundColor(@ColorInt color: Int) {
        statusVb.markerRootView.setCardBackgroundColor(color)
    }
}