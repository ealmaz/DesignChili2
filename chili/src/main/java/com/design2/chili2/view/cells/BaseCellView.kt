package com.design2.chili2.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.*
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.extensions.*
import com.design2.chili2.util.IconSize
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.image.SquircleView
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

open class BaseCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    lateinit var view: BaseCellViewVariables

    protected val shimmeringPairs: MutableMap<View, ShimmerFrameLayout?> = mutableMapOf()

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupShimmerViews()
    }

    protected open fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_cell_base, this)
        this.view = BaseCellViewVariables(
            flStartPlaceholder = view.findViewById(R.id.fl_start_place_holder),
            flEndPlaceholder = view.findViewById(R.id.fl_end_place_holder),
            ivIcon = view.findViewById(R.id.iv_icon),
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            divider = view.findViewById(R.id.divider),
            rootView = view.findViewById(R.id.root_view),
            chevron = view.findViewById(R.id.iv_chevron),
            tvTitleShimmer = view.findViewById(R.id.view_title_shimmer),
            tvSubtitleShimmer = view.findViewById(R.id.view_subtitle_shimmer),
            iconShimmer = view.findViewById(R.id.view_icon_shimmer),
        )
    }

    protected open fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.BaseCellView, defStyleAttr, defStyleRes)
            .run {
                getResourceId(R.styleable.BaseCellView_android_icon, -1).takeIf { it != -1 }?.let {
                    setIcon(it)
                }
                getString(R.styleable.BaseCellView_title)?.let { setTitle(it) }
                getString(R.styleable.BaseCellView_subtitle)?.let { setSubtitle(it) }
                getInteger(R.styleable.BaseCellView_roundedCornerMode, 0).let {
                    view.rootView.setupRoundedCellCornersMode(it)
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
                getColor(R.styleable.BaseCellView_squircleIconBackgroundColor, -1).takeIf { it != -1 }?.let {
                    setSquircleIconBackgroundColor(it)
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
                recycle()
            }
    }

    private fun setupShimmerViews() {
        shimmeringPairs[view.tvTitle] = view.tvTitleShimmer
    }

    fun setTitle(text: String?) {
        view.tvTitle.text = text
    }

    fun setTitle(@StringRes resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setTitle(text: Spanned) {
        view.tvTitle.text = text
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            view.tvTitle.setTextAppearance(resId)
        } else {
            view.tvTitle.setTextAppearance(context, resId)
        }
    }

    fun updateTitleMargin(startMarginPx: Int? = null, topMarginPx: Int? = null, endMarginPx: Int? = null, bottomMarginPx: Int? = null) {
        val param = view.tvTitle.layoutParams as? MarginLayoutParams ?: return
        param.apply {
            leftMargin = startMarginPx ?: leftMargin
            topMargin = topMarginPx ?: topMargin
            rightMargin = endMarginPx ?: rightMargin
            bottomMargin = bottomMarginPx ?: bottomMargin
        }
        view.tvTitle.layoutParams = param
    }

    fun setSubtitle(charSequence: CharSequence?) {
        view.tvSubtitle.apply {
            setTextOrHide(charSequence)
            if (charSequence == null) shimmeringPairs.remove(this)
            else shimmeringPairs[this] = view.tvSubtitleShimmer
        }
    }

    fun setSubtitle(@StringRes resId: Int) {
        view.tvSubtitle.apply {
            visible()
            setText(resId)
            shimmeringPairs[this] = view.tvSubtitleShimmer
        }
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        view.tvSubtitle.setTextAppearance(resId)
    }

    fun setIcon(@DrawableRes drawableRes: Int) {
        view.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            setImageResource(drawableRes)
            setupIconShimmer()
        }
    }

    fun setIcon(drawable: Drawable) {
        view.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            setImageDrawable(drawable)
            setupIconShimmer()
        }
    }

    fun setIcon(url: String?, placeHolderResId: Int? = null) {
        view.ivIcon.apply {
            visible()
            updateTitleMargin(startMarginPx = 0)
            Glide.with(this)
                .load(url)
                .placeholder(context.drawable(placeHolderResId?:R.drawable.chili_ic_stub))
                .dontTransform()
                .into(view.ivIcon)
            setupIconShimmer()
        }
    }

    private fun setupIconShimmer() {
        shimmeringPairs[view.ivIcon] = view.iconShimmer
    }

    fun getIconView(): SquircleView {
        return view.ivIcon
    }

    fun setSquircleIconBackgroundColor(@ColorInt color: Int) {
        view.ivIcon.squircleBackgroundColor = color
    }

    fun setDividerVisibility(isVisible: Boolean) {
        view.divider.apply {
            when(isVisible) {
                true -> visible()
                else -> gone()
            }
        }
    }

    fun getDividerView(): View {
        return view.divider
    }

    fun loadIcon(url: String?) {
        url?.let {
            view.ivIcon.run {
                setImageByUrl(url)
                visible()
                updateTitleMargin(startMarginPx = 0)
            }
        }
    }

    fun updateIconMargin(startMarginPx: Int? = null, topMarginPx: Int? = null, endMarginPx: Int? = null, bottomMarginPx: Int? = null) {
        val param = view.ivIcon.layoutParams as? MarginLayoutParams ?: return
        param.apply {
            leftMargin = startMarginPx ?: leftMargin
            topMargin = topMarginPx ?: topMargin
            rightMargin = endMarginPx ?: rightMargin
            bottomMargin = bottomMarginPx ?: bottomMargin
        }
        view.ivIcon.layoutParams = param
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
        val params = view.ivIcon.layoutParams
        params.height = heightPx
        params.width = widthPx
        view.ivIcon.layoutParams = params
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
        view.rootView.setupRoundedCellCornersMode(mode.value)
    }

    fun setupIsSurfaceClickable(isSurfaceClickable: Boolean) {
        this.setIsSurfaceClickable(isSurfaceClickable)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        view.rootView.setupRoundedCellCornersMode(mode.value)
    }

    open fun setIsChevronVisible(isVisible: Boolean) {
        view.chevron.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun updateRootViewMinHeight(minHeight: Int) {
        view.rootView.minHeight = minHeight
    }

    override fun getShimmeringViewsPair() = shimmeringPairs
}

data class BaseCellViewVariables(
    var flStartPlaceholder: FrameLayout,
    var flEndPlaceholder: FrameLayout,
    var ivIcon: SquircleView,
    var tvTitle: TextView,
    var tvSubtitle: TextView,
    var divider: View,
    var rootView: ConstraintLayout,
    var chevron: ImageView,
    val tvTitleShimmer: ShimmerFrameLayout,
    val tvSubtitleShimmer: ShimmerFrameLayout,
    val iconShimmer: ShimmerFrameLayout)