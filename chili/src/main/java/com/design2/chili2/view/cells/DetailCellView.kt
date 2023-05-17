package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spanned
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

    private lateinit var view: DetailCellViewVariables

    private val shimmerViewPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyleRes)
        setupShimmering()
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_cell_detail, this)
        this.view = DetailCellViewVariables(
            root = view.findViewById(R.id.root),
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            tvValue = view.findViewById(R.id.tv_value),
            tvStatus = view.findViewById(R.id.tv_status),
            svIcon = view.findViewById(R.id.sv_icon),
            titleShimmer = view.findViewById(R.id.view_title_shimmer),
            subtitleShimmer = view.findViewById(R.id.view_subtitle_shimmer),
            iconShimmer = view.findViewById(R.id.view_icon_shimmer),
        )
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
                this@DetailCellView.setupRoundedCellCornersMode(it)
            }
            recycle()
        }
    }

    private fun setupShimmering() {
        shimmerViewPairs[view.tvTitle] = view.titleShimmer
        shimmerViewPairs[view.svIcon] = view.iconShimmer
    }

    fun setTitle(text: CharSequence?) {
        view.tvTitle.text = text
    }

    fun setTitle(@StringRes textResId: Int) {
        view.tvTitle.setText(textResId)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        view.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitle(text: CharSequence?) {
        view.tvSubtitle.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(view.tvSubtitle)
        else shimmerViewPairs[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setSubtitle(@StringRes textResId: Int) {
        view.tvSubtitle.setTextOrHide(textResId)
        shimmerViewPairs[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        view.tvSubtitle.setTextAppearance(resId)
    }

    fun setValue(@StringRes textResId: Int) {
        view.tvValue.setTextOrHide(textResId)
        shimmerViewPairs[view.tvValue] = null
    }

    fun setValue(text: CharSequence?) {
        view.tvValue.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(view.tvValue)
        else shimmerViewPairs[view.tvValue] = null
    }

    fun setValueTextColor(@ColorInt colorInt: Int) {
        view.tvValue.setTextColor(colorInt)
    }

    fun setValueTextAppearance(@StyleRes resId: Int) {
        view.tvValue.setTextAppearance(resId)
    }

    fun setStatus(text: CharSequence?) {
        view.tvStatus.setTextOrHide(text)
        if (text == null) shimmerViewPairs.remove(view.tvStatus)
        else shimmerViewPairs[view.tvStatus] = null
    }

    fun setStatus(@StringRes textResId: Int) {
        view.tvStatus.setTextOrHide(textResId)
        shimmerViewPairs[view.tvStatus] = null
    }

    fun setStatusTextAppearance(@StyleRes resId: Int) {
        view.tvStatus.setTextAppearance(resId)
    }

    fun setupIsSurfaceClickable(isSurfaceClickable: Boolean) {
        this.setIsSurfaceClickable(isSurfaceClickable)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        this.setupRoundedCellCornersMode(mode.value)
    }

    fun setIcon(@DrawableRes drawableResId: Int) {
        view.svIcon.setImageResource(drawableResId)
    }

    fun setIcon(drawable: Drawable?) {
        view.svIcon.setImageDrawable(drawable)
    }

    fun setIconUrl(url: String?) {
        if (url == null) return
        Glide.with(view.svIcon)
            .load(url)
            .dontTransform()
            .into(view.svIcon)
    }

    override fun getShimmeringViewsPair() = shimmerViewPairs
}

data class DetailCellViewVariables(
    val root: ConstraintLayout,
    val tvTitle: TextView,
    val tvSubtitle: TextView,
    val tvValue: TextView,
    val tvStatus: TextView,
    val svIcon: SquircleView,
    val titleShimmer: ShimmerFrameLayout,
    val subtitleShimmer: ShimmerFrameLayout,
    val iconShimmer: ShimmerFrameLayout
)