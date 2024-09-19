package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.databinding.ChiliViewCardCellBinding
import kg.devcats.chili3.extensions.getColorFromAttr
import kg.devcats.chili3.extensions.setBoldTextWeight
import kg.devcats.chili3.extensions.setNormalTextWeight
import kg.devcats.chili3.extensions.setSurfaceClick
import kg.devcats.chili3.extensions.visible

class CardCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.design2.chili2.R.attr.cardCellViewDefaultStyle,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_CellViewStyle_CardCellView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliViewCardCellBinding

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewCardCellBinding.inflate(LayoutInflater.from(context), this, true)
        setupShimmering()
    }

    private fun obtainAttributes(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.CardCellView, defStyleAttr, defStyleRes)
            .apply {
                getString(R.styleable.CardCellView_cardCellTitle)?.let { setTitle(it) }
                getString(R.styleable.CardCellView_cardCellSubtitle)?.let { setSubtitle(it) }
                getString(R.styleable.CardCellView_cardCellValue)?.let { setAdditionalText(it) }
                getDrawable(R.styleable.CardCellView_cardCellIcon)?.let { setIcon(it) }
                getDrawable(R.styleable.CardCellView_cardCellOverlay)?.let { setOverlay(it) }
                setOverlayAlpha(getFloat(R.styleable.CardCellView_cardCellOverlayAlpha, 0.4f))
                getDrawable(R.styleable.CardCellView_cardCellOverlayIcon)?.let { setOverlayIcon(it) }
                setIsMain(getBoolean(R.styleable.CardCellView_cardCellIsMain, false))
                setIsBlocked(getBoolean(R.styleable.CardCellView_cardCellIsBlocked, false))
                setIsUniqueStated(
                    getBoolean(
                        R.styleable.CardCellView_cardCellIsUniqueStatused, false
                    )
                )
                setIsChevronVisible(
                    getBoolean(
                        R.styleable.CardCellView_cardCellIsChevronVisible, false
                    )
                )
                getResourceId(
                    R.styleable.CardCellView_cardCellTitleTextAppearance, -1
                ).takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
                getResourceId(
                    R.styleable.CardCellView_cardCellSubtitleTextAppearance, -1
                ).takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
                getResourceId(
                    R.styleable.CardCellView_cardCellAdditionalTextAppearance, -1
                ).takeIf { it != -1 }?.let { setAdditionalTextAppearance(it) }
                getDimension(R.styleable.CardCellView_cardCellIconWidth, 60.dpF).let {
                    setIconWidth(
                        it.toInt()
                    )
                }
                getDimension(
                    R.styleable.CardCellView_cardCellIconHeight, 40.dpF
                ).let { setIconHeight(it.toInt()) }
                getInteger(
                    R.styleable.CardCellView_cardCellAdditionalTextVerticalAlign, 0
                ).let { setAdditionalTextVerticalAlign(it) }
                setTitleMaxLines(getInteger(R.styleable.CardCellView_cardCellTitleMaxLines, 3))
                setSubtitleMaxLines(
                    getInteger(
                        R.styleable.CardCellView_cardCellSubtitleMaxLines, 1
                    )
                )
                setAdditionalTextMaxLines(
                    getInteger(
                        R.styleable.CardCellView_cardCellAdditionalTextMaxLines, 2
                    )
                )

                setupSurfaceClicks(
                    getBoolean(
                        R.styleable.CardCellView_cardCellIsSurfaceClickable,
                        true
                    )
                )

                recycle()
            }
    }

    private fun setupShimmering() {
        shimmeringPairs[vb.clContent] = vb.viewShimmerContent
    }

    fun setupSurfaceClicks(isSurfaceClickable: Boolean) {
        with(vb) {
            if (isSurfaceClickable) {
                setSurfaceClick(
                    onPressedState = {
                        tvTitle.setBoldTextWeight()
                        tvSubtitle.setBoldTextWeight()
                        tvAdditionalText.setBoldTextWeight()
                    },
                    onDefaultState = {
                        tvTitle.setNormalTextWeight()
                        tvSubtitle.setNormalTextWeight()
                        tvAdditionalText.setNormalTextWeight()
                    }
                )
            }
        }
    }

    fun setTitle(title: String?) {
        vb.tvTitle.setTextOrHide(title)
    }

    fun setTitle(@StringRes resId: Int?) {
        vb.tvTitle.setTextOrHide(resId)
    }

    fun setTitle(spanned: Spanned?) {
        vb.tvTitle.setTextOrHide(spanned)
    }

    fun setSubtitle(subtitle: String?) {
        vb.tvSubtitle.setTextOrHide(subtitle)
    }

    fun setSubtitle(@StringRes resId: Int?) {
        vb.tvSubtitle.setTextOrHide(resId)
    }

    fun setSubtitle(spanned: Spanned?) {
        vb.tvSubtitle.setTextOrHide(spanned)
    }

    fun setAdditionalText(value: String?) {
        vb.tvAdditionalText.setTextOrHide(value)
    }

    fun setAdditionalText(spanned: Spanned?) {
        vb.tvAdditionalText.setTextOrHide(spanned)
    }

    fun setAdditionalText(@StringRes resId: Int?) {
        vb.tvAdditionalText.setTextOrHide(resId)
    }

    fun setTitleMaxLines(lines: Int) {
        vb.tvTitle.maxLines = lines
    }

    fun setSubtitleMaxLines(lines: Int) {
        vb.tvSubtitle.maxLines = lines
    }

    fun setAdditionalTextMaxLines(lines: Int) {
        vb.tvAdditionalText.maxLines = lines
    }

    fun setIcon(icon: Drawable?) {
        vb.ivIcon.setImageDrawable(icon)
    }

    fun setIcon(url: String, placeHolderDrawable: Drawable? = null) {
        vb.ivIcon.setImageByUrl(url, placeHolderDrawable)
    }

    fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIconWidth(width: Int) {
        vb.ivIcon.layoutParams.width = width
    }

    fun setIconHeight(height: Int) {
        vb.ivIcon.layoutParams.height = height
    }
    fun setAdditionalTextVerticalAlign(gravity: Int = Gravity.TOP) {
        vb.endContainer.apply {
            val params = layoutParams as? LayoutParams
            if (params != null) {
                when (gravity) {
                    Gravity.TOP -> params.verticalBias = 0f // top
                    Gravity.CENTER -> params.verticalBias = 0.5f // center
                    Gravity.BOTTOM -> params.verticalBias = 1f // bottom
                }
                layoutParams = params
            }
        }
    }

    fun setOverlay(icon: Drawable?, alpha: Float = 0.4f) {
        vb.ivOverlay.apply {
            setImageOrHide(icon)
            setAlpha(alpha)
        }
    }

    fun setOverlay(url: String?, alpha: Float = 0.4f) {
        vb.ivOverlay.apply {
            setImageOrHide(url)
            setAlpha(alpha)
        }
    }

    fun setOverlay(@DrawableRes resId: Int?, alpha: Float = 0.4f) {
        vb.ivOverlay.apply {
            setImageOrHide(resId)
            setAlpha(alpha)
        }
    }

    fun setOverlayAlpha(alpha: Float = 0.4f) {
        vb.ivOverlay.apply {
            visible()
            setAlpha(alpha)
        }
    }

    fun setOverlayIcon(icon: Drawable?, isOverlayVisible: Boolean = true) {
        with(vb) {
            ivOverlayIcon.setImageOrHide(icon)
            ivOverlay.isVisible = isOverlayVisible
        }
    }

    fun setOverlayIcon(url: String?, isOverlayVisible: Boolean = true) {
        with(vb) {
            ivOverlayIcon.setImageOrHide(url)
            ivOverlay.isVisible = isOverlayVisible
        }
    }

    fun setOverlayIcon(@DrawableRes resId: Int?, isOverlayVisible: Boolean = true) {
        with(vb) {
            ivOverlayIcon.setImageOrHide(resId)
            ivOverlay.isVisible = isOverlayVisible
        }
    }

    fun setIsMain(isMain: Boolean) {
        vb.ivStar.isVisible = isMain
    }

    fun setIsBlocked(
        isBlocked: Boolean, alpha: Float = 0.4f, blockingIcon: Int = R.drawable.chili_ic_lock,
        errorTextColor: Int = context.getColorFromAttr(com.design2.chili2.R.attr.ChiliErrorTextColor)
    ) {
        with(vb) {
            val alphaValue = if (isBlocked) alpha else 1f

            tvTitle.alpha = alphaValue
            if (isBlocked) tvSubtitle.setTextColor(errorTextColor)
            tvAdditionalText.alpha = alphaValue
            ivStar.alpha = alphaValue
            ivChevron.alpha = alphaValue
            if (isBlocked) setOverlayIcon(blockingIcon)
            ivOverlay.apply {
                isVisible = isBlocked
                setAlpha(alpha)
            }
        }
    }

    fun setIsUniqueStated(
        isUniqueStated: Boolean,
        uniqueColor: Int = context.getColorFromAttr(com.design2.chili2.R.attr.ChiliErrorTextColor),
        defaultColorSubtitle: Int = context.getColorFromAttr(com.design2.chili2.R.attr.ChiliValueTextColor),
        defaultColorAdditional: Int = context.getColorFromAttr(com.design2.chili2.R.attr.ChiliPrimaryTextColor)
    ) {
        with(vb) {
            if (isUniqueStated) {
                tvSubtitle.setTextColor(uniqueColor)
                tvAdditionalText.setTextColor(uniqueColor)
            } else {
                tvSubtitle.setTextColor(defaultColorSubtitle)
                tvAdditionalText.setTextColor(defaultColorAdditional)
            }
        }
    }

    fun setIsChevronVisible(isChevronVisible: Boolean) {
        vb.ivChevron.isVisible = isChevronVisible
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setAdditionalTextAppearance(@StyleRes resId: Int) {
        vb.tvAdditionalText.setTextAppearance(resId)
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs
}
