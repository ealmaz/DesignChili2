package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellStatusMarkerMarkerBgBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.visible

class StatusMarkerCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    private val defStyleAttr: Int = R.attr.statusMarkerCellViewDefaultStyle,
    private val defStyleRes: Int = R.style.Chili_CellViewStyle_StatusMarkerCellView
): BaseCellView(context, attrs, R.attr.cellViewDefaultStyle, R.style.Chili_CellViewStyle_BaseCellViewStyle) {

    private lateinit var statusVb: ChiliViewCellStatusMarkerMarkerBgBinding

    override fun inflateView(context: Context) {
        super.inflateView(context)
        val statusView = LayoutInflater.from(context).inflate(R.layout.chili_view_cell_status_marker_marker_bg, vb.flEndPlaceHolder, false)
        statusVb = ChiliViewCellStatusMarkerMarkerBgBinding.bind(statusView)
        statusView.rootView.gone()
        vb.flEndPlaceHolder.addView(statusView)
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