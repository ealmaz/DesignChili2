package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellBaseBinding
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.IconSize
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

open class BaseCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    protected lateinit var vb: ChiliViewCellBaseBinding

    protected val shimmeringPairs: MutableMap<View, ShimmerFrameLayout?> = mutableMapOf()

    private var roundedCornerMode = RoundedCornerMode.SINGLE.value

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupShimmerViews()
    }

    protected open fun inflateView(context: Context) {
        vb = ChiliViewCellBaseBinding.inflate(LayoutInflater.from(context), this, true)
    }

    protected open fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.BaseCellView, defStyleAttr, defStyleRes)
            .run {
                getResourceId(R.styleable.BaseCellView_cellBackground, -1).takeIf { it != -1 }?.let {
                    vb.rootView.setBackgroundResource(it)
                }
                getResourceId(R.styleable.BaseCellView_android_icon, -1).takeIf { it != -1 }?.let {
                    setIcon(it)
                }
                getString(R.styleable.BaseCellView_title)?.let { setTitle(it) }
                getString(R.styleable.BaseCellView_subtitle)?.let { setSubtitle(it) }
                getInteger(R.styleable.BaseCellView_roundedCornerMode, roundedCornerMode).let {
                    roundedCornerMode = getInteger(R.styleable.BaseCellView_roundedCornerMode, roundedCornerMode)
                    this@BaseCellView.setupRoundedCellCornersMode(it)
                }
                getBoolean(R.styleable.BaseCellView_isDividerVisible, true).let {
                    setDividerVisibility(it)
                }
                getLayoutDimension(R.styleable.BaseCellView_cellIconSize, IconSize.SMALL.value).let {
                    when (it) {
                        IconSize.SMALL.value -> setIconSize(IconSize.SMALL)
                        IconSize.MEDIUM.value -> setIconSize(IconSize.MEDIUM)
                        IconSize.LARGE.value -> setIconSize(IconSize.LARGE)
                        else -> setupIconSize(it,it)
                    }
                }
                getBoolean(R.styleable.BaseCellView_isSurfaceClickable, true).let {
                    setupIsSurfaceClickable(it)
                }
                getDimensionPixelSize(R.styleable.BaseCellView_cellIconVerticalMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(topMarginPx = it, bottomMarginPx = it)
                }
                getDimensionPixelSize(R.styleable.BaseCellView_cellIconHorizontalMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(startMarginPx = it, endMarginPx = it)
                }

                getDimensionPixelSize(R.styleable.BaseCellView_cellIconStartMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(startMarginPx = it)
                }

                getDimensionPixelSize(R.styleable.BaseCellView_cellIconEndMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(endMarginPx = it)
                }

                getDimensionPixelSize(R.styleable.BaseCellView_cellIconTopMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(topMarginPx = it)
                }

                getDimensionPixelSize(R.styleable.BaseCellView_cellIconBottomMargin, -1).takeIf { it != -1 }?.let {
                    updateIconMargin(bottomMarginPx = it)
                }
                getBoolean(R.styleable.BaseCellView_isChevronVisible, true).let {
                    setIsChevronVisible(it)
                }
                getResourceId(R.styleable.BaseCellView_titleTextAppearance, -1).takeIf { it != -1 }?.let {
                    setTitleTextAppearance(it)
                }
                getResourceId(R.styleable.BaseCellView_subtitleTextAppearance, -1).takeIf { it != -1 }?.let {
                    setSubtitleTextAppearance(it)
                }
                getDimensionPixelSize(R.styleable.BaseCellView_android_minHeight, -1).takeIf { it != -1 }?.let {
                    updateRootViewMinHeight(it)
                }
                getInteger(R.styleable.BaseCellView_android_maxLines, -1).takeIf { it != -1 }?.let {
                    setTitleMaxLines(it)
                }
                getInteger(R.styleable.BaseCellView_titleMaxLines, -1).takeIf { it != -1 }?.let {
                    setTitleMaxLines(it)
                }
                getInteger(R.styleable.BaseCellView_subtitleMaxLines, -1).takeIf { it != -1 }?.let {
                    setSubtitleMaxLines(it)
                }
                recycle()
            }
    }

    private fun setupShimmerViews() {
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
    }

    fun setTitle(text: String?) {
        vb.tvTitle.text = text
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitle(text: Spanned) {
        vb.tvTitle.text = text
    }

    fun setTitleOrHide(title: String?) = vb.tvTitle.setTextOrHide(title)

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.tvTitle.setTextAppearance(resId)
        } else {
            vb.tvTitle.setTextAppearance(context, resId)
        }
    }

    fun setTitleMaxLines(maxLines: Int) {
        vb.tvTitle.maxLines = maxLines
    }

    fun updateTitleMargin(startMarginPx: Int? = null, topMarginPx: Int? = null, endMarginPx: Int? = null, bottomMarginPx: Int? = null) {
        val param = vb.tvTitle.layoutParams as? MarginLayoutParams ?: return
        param.apply {
            leftMargin = startMarginPx ?: leftMargin
            topMargin = topMarginPx ?: topMargin
            rightMargin = endMarginPx ?: rightMargin
            bottomMargin = bottomMarginPx ?: bottomMargin
        }
        vb.tvTitle.layoutParams = param
    }

    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvSubtitle.apply {
            setTextOrHide(charSequence)
            if (charSequence == null) shimmeringPairs.remove(this)
            else shimmeringPairs[this] = vb.viewSubtitleShimmer
        }
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.apply {
            visible()
            setText(resId)
            shimmeringPairs[this] = vb.viewSubtitleShimmer
        }
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setSubtitleMaxLines(maxLines: Int) {
        vb.tvSubtitle.maxLines = maxLines
    }

    fun setIcon(@DrawableRes drawableRes: Int) {
        vb.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            setImageResource(drawableRes)
            setupIconShimmer()
        }
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            setImageDrawable(drawable)
            setupIconShimmer()
        }
    }

    fun setIcon(url: String?, placeHolderResId: Int? = null) {
        vb.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            Glide.with(this)
                .load(url)
                .placeholder(context.drawable(placeHolderResId?:R.drawable.chili_ic_stub))
                .dontTransform()
                .into(vb.ivIcon)
            setupIconShimmer()
        }
    }

    private fun setupIconShimmer() {
        shimmeringPairs[vb.ivIcon] = vb.viewIconShimmer
    }

    fun getIconView(): ImageView {
        return vb.ivIcon
    }

    fun setDividerVisibility(isVisible: Boolean) {
        vb.divider.apply {
            when(isVisible) {
                true -> visible()
                else -> gone()
            }
        }
    }

    fun getDividerView(): View {
        return vb.divider
    }

    fun loadIcon(url: String?) {
        url?.let {
            vb.ivIcon.run {
                setImageByUrl(url)
                visible()
                updateTitleMargin(startMarginPx = 0)
            }
        }
    }

    fun updateIconMargin(startMarginPx: Int? = null, topMarginPx: Int? = null, endMarginPx: Int? = null, bottomMarginPx: Int? = null) {
        val param = vb.ivIcon.layoutParams as? MarginLayoutParams ?: return
        param.apply {
            leftMargin = startMarginPx ?: leftMargin
            topMargin = topMarginPx ?: topMargin
            rightMargin = endMarginPx ?: rightMargin
            bottomMargin = bottomMarginPx ?: bottomMargin
        }
        vb.ivIcon.layoutParams = param
    }

    fun setIconSize(iconSize: IconSize) {
        val margin14px = resources.getDimensionPixelSize(R.dimen.padding_14dp)
        val margin8px = resources.getDimensionPixelSize(R.dimen.padding_8dp)
        val size = when(iconSize) {
            IconSize.LARGE -> {
                updateIconMargin(topMarginPx = margin14px, bottomMarginPx = margin14px)
                R.dimen.view_48dp
            }
            IconSize.MEDIUM -> {
                updateIconMargin(topMarginPx = margin14px, bottomMarginPx = margin14px)
                R.dimen.view_46dp
            }
            IconSize.SMALL -> {
                updateIconMargin(topMarginPx = margin8px, bottomMarginPx = margin8px)
                R.dimen.view_32dp
            }
        }
        setIconSize(size, size)
    }


    fun setIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupIconSize(widthPx, heightPx)
    }

    private fun setupIconSize(widthPx: Int, heightPx: Int) {
        val params = vb.ivIcon.layoutParams
        params.height = heightPx
        params.width = widthPx
        vb.ivIcon.layoutParams = params
    }

    fun setupRoundedModeByPosition(isFirst: Boolean, isLast: Boolean) {
        val roundedMode = when {
            isFirst && isLast -> RoundedCornerMode.SINGLE
            isFirst -> RoundedCornerMode.TOP
            isLast -> RoundedCornerMode.BOTTOM
            else -> RoundedCornerMode.MIDDLE
        }
        setupCornerRoundedMode(roundedMode)
        setDividerVisibility(!isLast)
    }

    fun setRoundedMode(mode: RoundedCornerMode) {
        roundedCornerMode = mode.value
        this.setupRoundedCellCornersMode(mode.value)
    }

    fun setupIsSurfaceClickable(isSurfaceClickable: Boolean) {
        this.setIsSurfaceClickable(isSurfaceClickable, roundedCornerMode)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        roundedCornerMode = mode.value
        this.setupRoundedCellCornersMode(mode.value)
    }

    open fun setIsChevronVisible(isVisible: Boolean) {
        vb.ivChevron.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun updateRootViewMinHeight(minHeight: Int) {
        vb.rootView.minHeight = minHeight
    }

    override fun getShimmeringViewsPair() = shimmeringPairs

    fun setChevron(drawable: Drawable) {
        vb.ivChevron.apply {
            visible()
            setImageDrawable(drawable)
        }
    }

    fun setChevron(drawableRes: Int) {
        vb.ivChevron.apply {
            visible()
            setImageResource(drawableRes)
        }
    }
}