package com.design2.chili2.view.cells

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellInfoBinding
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

class InfoCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.infoCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_InfoCellView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliViewCellInfoBinding

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    private var roundedCornerMode = RoundedCornerMode.SINGLE.value
    private var surfaceClickAbility = true

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupShimmering()
        setupViews()
    }

    private fun initView(context: Context) {
        vb = ChiliViewCellInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.InfoCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.InfoCellView_title)?.let { setTitle(it) }
                getString(R.styleable.InfoCellView_subtitle)?.let { setSubtitle(it) }
                getBoolean(
                    R.styleable.InfoCellView_isDividerVisible,
                    false
                ).let { setDividerVisibility(it) }
                getBoolean(
                    R.styleable.InfoCellView_isSurfaceClickable,
                    true
                ).let { setupIsSurfaceClickable(it) }
                getInteger(R.styleable.InfoCellView_roundedCornerMode, -1).takeIf { it != -1 }
                    ?.let {
                        roundedCornerMode = it
                        vb.rootView.setupRoundedCellCornersMode(it, surfaceClickAbility)
                    }
                getResourceId(R.styleable.InfoCellView_titleTextAppearance, -1).takeIf { it != -1 }
                    ?.let {
                        setTitleTextAppearance(it)
                    }
                getResourceId(
                    R.styleable.InfoCellView_subtitleTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let {
                    setSubtitleTextAppearance(it)
                }
                recycle()
            }
    }

    private fun setupShimmering() {
        shimmeringPairs.apply {
            put(vb.tvTitle, vb.viewTitleShimmer)
            put(vb.tvSubtitle, vb.viewSubtitleShimmer)
        }
    }

    private fun setupViews() {
        this.setupCornerRoundedMode(RoundedCornerMode.SINGLE)
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setTitle(text: String) {
        vb.tvTitle.text = text
    }

    fun setTitle(text: Spanned) {
        vb.tvTitle.text = text
    }

    fun setTitle(charSequence: CharSequence) {
        vb.tvTitle.text = charSequence
    }

    fun setSubtitle(charSequence: CharSequence) {
        vb.tvSubtitle.text = charSequence
    }

    fun setSubtitle(text: String) {
        vb.tvSubtitle.text = text
    }

    fun setSubtitle(text: Spanned) {
        vb.tvSubtitle.text = text
    }

    fun setDividerVisibility(isVisible: Boolean) {
        vb.divider.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    // Note: divider xml layout already have margin_start:8dp
    fun setDividerHorizontalMargin(start: Int? = null, end: Int? = null) {
        vb.divider.apply {
            val params = layoutParams as ViewGroup.MarginLayoutParams
            params.marginStart = start?.dp ?: params.marginStart
            params.marginEnd = end?.dp ?: params.marginEnd
            layoutParams = params
        }
    }

    fun setupIsSurfaceClickable(isSurfaceClickable: Boolean) {
        surfaceClickAbility = isSurfaceClickable
        this.setIsSurfaceClickable(isSurfaceClickable, roundedCornerMode)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        roundedCornerMode = mode.value
        this.setupRoundedCellCornersMode(mode.value, surfaceClickAbility)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs

}