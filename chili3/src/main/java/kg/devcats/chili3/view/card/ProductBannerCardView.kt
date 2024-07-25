package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardProductBannerBinding

class ProductBannerCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bannerCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_ProductBannerCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardProductBannerBinding

    override val styleableAttrRes: IntArray = R.styleable.ProductBannerCardView

    override val rootContainer: View
        get() = vb.rootView

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardProductBannerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.ProductBannerCardView_productBannerCardTitle))
        setSubtitle(getText(R.styleable.ProductBannerCardView_productBannerCardSubtitle))
        setIcon(getResourceId(R.styleable.ProductBannerCardView_productBannerCardIcon, -1))

        getResourceId(R.styleable.ProductBannerCardView_productBannerCardTitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }

        getResourceId(R.styleable.ProductBannerCardView_productBannerCardSubtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }

        getDimension(R.styleable.ProductBannerCardView_productBannerCardIconWidth, 125.dpF).let {
            setIconWidth(it.toInt())
        }
        getDimension(R.styleable.ProductBannerCardView_productBannerCardIconHeight, 88.dpF).let {
            setIconHeight(it.toInt())
        }

        setTitleMaxLines(getInteger(R.styleable.ProductBannerCardView_productBannerCardTitleMaxLines, 1))
        setSubtitleMaxLines(getInteger(R.styleable.ProductBannerCardView_productBannerCardSubtitleMaxLines, 1))
    }

    override fun setupShimmeringViews() {
        // Add shimmer view setup if needed
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvBannerTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvBannerTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvBannerTitle.setTextAppearance(resId)
    }

    fun setTitleMaxLines(lines: Int) {
        vb.tvBannerTitle.maxLines = lines
    }

    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvBannerSubtitle.setTextOrHide(charSequence)
    }

    fun setSubtitle(resId: Int) {
        vb.tvBannerSubtitle.setTextOrHide(resId)
    }

    fun setSubtitleTextAppearance(resId: Int) {
        vb.tvBannerSubtitle.setTextAppearance(resId)
    }

    fun setSubtitleMaxLines(lines: Int) {
        vb.tvBannerSubtitle.maxLines = lines
    }

    fun setIcon(resId: Int) {
        vb.ivBannerIcon.setImageOrHide(resId)
    }

    fun setIcon(drawable: Drawable?) {
        vb.ivBannerIcon.setImageOrHide(drawable)
    }

    fun setIcon(url: String?) {
        vb.ivBannerIcon.setImageOrHide(url)
    }

    fun setIconWidth(width: Int) {
        vb.ivBannerIcon.layoutParams.width = width
    }

    fun setIconHeight(height: Int) {
        vb.ivBannerIcon.layoutParams.height = height
    }
}