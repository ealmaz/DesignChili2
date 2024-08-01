package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setUrlImageByCoil
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardCategoryMarketBinding
import kg.devcats.chili3.extensions.getColorFromAttr
import kg.devcats.chili3.util.ViewSize

class MarketCategoryCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.marketCategoryCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_MarketCategoryCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardCategoryMarketBinding
    private var imageSize: Pair<Int, Int> = Pair(0, 0)

    override val styleableAttrRes: IntArray = R.styleable.MarketCategoryCardView

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardCategoryMarketBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setupCardSize(getLayoutDimension(R.styleable.MarketCategoryCardView_viewSize, 0))
        setupCardBackgroundAndIconSize(getBoolean(R.styleable.MarketCategoryCardView_isHighlighted, false))
        setTitle(getText(R.styleable.MarketCategoryCardView_title))
        getResourceId(R.styleable.MarketCategoryCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let(::setTitleTextAppearance)
        getString(R.styleable.MarketCategoryCardView_iconSrc)?.let(::setCategoryIcon)
        getInteger(
            R.styleable.MarketCategoryCardView_android_scaleType,
            ImageView.ScaleType.FIT_CENTER.ordinal
        ).let(::setScaleType)
        getBoolean(R.styleable.MarketCategoryCardView_android_adjustViewBounds, false)
            .let(::setAdjustViewBounds)
    }

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
        shimmeringPairs[vb.ivIcons] = vb.viewIconShimmer
    }

    fun setupCardSize(viewSize: Int) {
        when (viewSize) {
            ViewSize.MEDIUM.value -> 109.dp to 76.dp
            ViewSize.LARGE.value -> 138.dp to 76.dp
            else -> null
        }?.also { vb.clContainer.setCustomViewSize(it.first, it.second) }
    }

    private fun setupCardBackgroundAndIconSize(isHighlighted: Boolean) = with(vb.clContainer) {
        if (isHighlighted) background = context.drawable(R.drawable.chili_bg_market_category_card_highlighted)
        else setBackgroundColor(context.getColorFromAttr(R.attr.ChiliMarketCategoryCardViewBackgroundColor))
        setupIconSize(isHighlighted)
    }

    /**
     * Must be called before setCategoryIcon(src: String?) method
     */
    private fun setupIconSize(isHighlighted: Boolean) {
        val (width, height) = when (isHighlighted) {
            true -> 104.dp to 32.dp
            else -> 74.dp to 52.dp
        }.also { imageSize = it }
        vb.ivIcons.setCustomViewSize(width, height)
    }

    fun setHighlightingOnView(isHighlighted: Boolean = false) {
        setupCardBackgroundAndIconSize(isHighlighted)
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(this.context.getText(resId))
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setCategoryIcon(src: String?) {
        vb.ivIcons.setUrlImageByCoil(
            url = src,
            width = imageSize.first,
            height = imageSize.second
        )
    }

    fun setScaleType(scaleType: Int) {
        vb.ivIcons.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        vb.ivIcons.adjustViewBounds = isAdjusted
    }

    private fun View.setCustomViewSize(width: Int, height: Int) {
        layoutParams.apply {
            this.width = width
            this.height = height
        }
    }

}