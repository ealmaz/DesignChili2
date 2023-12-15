package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellDetailBinding
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.image.SquircleView
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

class DetailCellView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.detailCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_DetailCellView
) : ConstraintLayout(context, attributeSet, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliViewCellDetailBinding

    private val shimmerViewPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    private var roundedCornerMode = RoundedCornerMode.SINGLE.value
    private var surfaceClickAbility = true

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyleRes)
        setupShimmering()
    }

    private fun initView(context: Context) {
        vb = ChiliViewCellDetailBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.DetailCellView, defStyleAttr, defStyleRes).run {
            setTitle(getString(R.styleable.DetailCellView_title))
            setSubtitle(getString(R.styleable.DetailCellView_subtitle))
            setValue(getString(R.styleable.DetailCellView_value))
            setStatus(getString(R.styleable.DetailCellView_status))
            setIcon(getDrawable(R.styleable.DetailCellView_icon))
            setupIsSurfaceClickable(getBoolean(R.styleable.DetailCellView_isSurfaceClickable, true))
            getInteger(R.styleable.DetailCellView_roundedCornerMode, RoundedCornerMode.SINGLE.value).let {
                roundedCornerMode = it
                this@DetailCellView.setupRoundedCellCornersMode(it)
            }
            getResourceId(R.styleable.DetailCellView_titleTextAppearance, -1).takeIf { it != -1 }?.let {
                setTitleTextAppearance(it)
            }
            getResourceId(R.styleable.DetailCellView_subtitleTextAppearance, -1).takeIf { it != -1 }?.let {
                setSubtitleTextAppearance(it)
            }
            getResourceId(R.styleable.DetailCellView_valueTextAppearance, -1).takeIf { it != -1 }?.let {
                setValueTextAppearance(it)
            }
            getResourceId(R.styleable.DetailCellView_statusTextAppearance, -1).takeIf { it != -1 }?.let {
                setStatusTextAppearance(it)
            }
            getInteger(R.styleable.DetailCellView_titleMaxLines, -1).takeIf { it != -1 }?.let {
                setTitleMaxLines(it)
            }
            getInteger(R.styleable.DetailCellView_subtitleMaxLines, -1).takeIf { it != -1 }?.let {
                setSubtitleMaxLines(it)
            }
            recycle()
        }
    }

    private fun setupShimmering() {
        shimmerViewPairs[vb.tvTitle] = vb.viewTitleShimmer
        shimmerViewPairs[vb.svIcon] = vb.viewIconShimmer
    }

    fun setTitle(text: CharSequence?) {
        vb.tvTitle.text = text
    }

    fun setTitle(@StringRes textResId: Int) {
        vb.tvTitle.setText(textResId)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setTitleMaxLines(value: Int) {
        vb.tvTitle.apply {
            maxLines = value
            ellipsize = TextUtils.TruncateAt.END
        }
    }

    fun setSubtitleMaxLines(value: Int) {
        vb.tvSubtitle.apply {
            maxLines = value
            ellipsize = TextUtils.TruncateAt.END
        }
    }

    fun setSubtitle(text: CharSequence?) {
        vb.tvSubtitle.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(vb.tvSubtitle)
        else shimmerViewPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitle(@StringRes textResId: Int) {
        vb.tvSubtitle.setTextOrHide(textResId)
        shimmerViewPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setValue(@StringRes textResId: Int) {
        vb.tvValue.setTextOrHide(textResId)
        shimmerViewPairs[vb.tvValue] = null
    }

    fun setValue(text: CharSequence?) {
        vb.tvValue.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(vb.tvValue)
        else shimmerViewPairs[vb.tvValue] = null
    }

    fun getValueTextView(): TextView {
        return vb.tvValue
    }

    fun setValueTextColor(@ColorInt colorInt: Int) {
        vb.tvValue.setTextColor(colorInt)
    }

    fun setValueTextAppearance(@StyleRes resId: Int) {
        vb.tvValue.setTextAppearance(resId)
    }

    fun setStatus(text: CharSequence?) {
        vb.tvStatus.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(vb.tvStatus)
        else shimmerViewPairs[vb.tvStatus] = null
    }

    fun getStatusTextView(): TextView {
        return vb.tvStatus
    }

    fun setStatus(@StringRes textResId: Int) {
        vb.tvStatus.setTextOrHide(textResId)
        shimmerViewPairs[vb.tvStatus] = null
    }

    fun setStatusTextAppearance(@StyleRes resId: Int) {
        vb.tvStatus.setTextAppearance(resId)
    }

    fun setupIsSurfaceClickable(isSurfaceClickable: Boolean) {
        surfaceClickAbility = isSurfaceClickable
        this.setIsSurfaceClickable(isSurfaceClickable, roundedCornerMode)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        roundedCornerMode = mode.value
        this.setupRoundedCellCornersMode(mode.value, surfaceClickAbility)
    }

    fun setIcon(@DrawableRes drawableResId: Int) {
        vb.svIcon.setImageResource(drawableResId)
    }

    fun setIcon(drawable: Drawable?) {
        vb.svIcon.setImageDrawable(drawable)
    }

    fun setIconUrl(url: String?) {
        if (url == null) return
        Glide.with(vb.svIcon)
            .load(url)
            .dontTransform()
            .into(vb.svIcon)
    }

    override fun getShimmeringViewsPair() = shimmerViewPairs
}