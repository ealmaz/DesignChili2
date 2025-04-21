package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.TextView
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setUrlImageByCoil
import com.design2.chili2.view.card.BaseCardView
import com.google.android.material.internal.ViewUtils.dpToPx
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardProductBinding
import kg.devcats.chili3.extensions.getDimensionFromAttr
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.pxToDp
import kg.devcats.chili3.extensions.visible

class ProductCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.productCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_ProductCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardProductBinding

    override val styleableAttrRes: IntArray = R.styleable.ProductCardView

    override val rootContainer: View
        get() = vb.clContainer

    private var imageSize: Pair<Int, Int> = Pair(0, 0)

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardProductBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.sivImage] = vb.viewIconShimmer
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
        shimmeringPairs[vb.tvSubtitle] = vb.viewSubtitleShimmer
        shimmeringPairs[vb.tvDescription] = vb.viewDescriptionShimmer
        shimmeringPairs[vb.llDiscount] = null
    }

    override fun TypedArray.obtainAttributes() {
        setImageSize(
            getImageSize(R.attr.ChiliProductCardViewImageHeight, 210.dpF),
            getImageSize(R.attr.ChiliProductCardViewImageWidth, 168.dpF)
        )
        getString(R.styleable.ProductCardView_productImage)?.let {
            setProductImage(it)
        }
        getInteger(R.styleable.ProductCardView_android_scaleType, ScaleType.FIT_CENTER.ordinal).let {
            setScaleType(it)
        }
        getBoolean(R.styleable.ProductCardView_android_adjustViewBounds, false).let {
            setAdjustViewBounds(it)
        }
        setTitle(getText(R.styleable.ProductCardView_title))
        getResourceId(R.styleable.ProductCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        setSubtitle(getText(R.styleable.ProductCardView_subtitle))
        getResourceId(R.styleable.ProductCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
        getResourceId(R.styleable.ProductCardView_subtitleBackgroundColor, R.drawable.chili_bg_product_subtitle)
            .takeIf { it != -1 }?.let { setSubtitleBackground(it) }
        setDescription(getText(R.styleable.ProductCardView_description))
        getResourceId(R.styleable.ProductCardView_descriptionTextAppearance, -1)
            .takeIf { it != -1 }?.let { setDescriptionTextAppearance(it) }
        setDiscount(getText(R.styleable.ProductCardView_discount))
        getResourceId(R.styleable.ProductCardView_discountTextAppearance, -1)
            .takeIf { it != -1 }?.let { setDiscountTextAppearance(it) }
        getResourceId(R.styleable.ProductCardView_discountBackgroundColor, R.drawable.chili_bg_product_discount_gradient)
            .takeIf { it != -1 }?.let { setDiscountBackground(it) }
        getResourceId(R.styleable.ProductCardView_discountIcon, -1)
            .takeIf { it != -1 }?.let { setDiscountIcon(it) }
    }

    private fun getImageSize(@AttrRes resId: Int, defaultValue: Float = 0F): Int {
        return context.getDimensionFromAttr(resId, defaultValue).toInt()
    }

    /**
     * Must be called before [ProductCardView.setProductImage] method
     */
    fun setImageSize(width: Int, height: Int) {
        imageSize = width to height
    }

    fun setProductImage(src: String?, backgroundColor: Int? = null) {
        vb.sivImage.apply {
            backgroundColor?.let { setBackgroundColor(it) }
            setUrlImageByCoil(
                url = src,
                width = imageSize.first,
                height = imageSize.second
            )
        }
    }

    fun setScaleType(scaleType: Int) {
        vb.sivImage.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        vb.sivImage.adjustViewBounds = isAdjusted
    }

    fun setTitle(text: CharSequence?) {
        vb.tvTitle.text = text
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitle(text: CharSequence?) {
        vb.tvSubtitle.apply {
            setTextOrHide(text)
            vb.placeholder.isVisible = text.isNullOrEmpty()
        }
    }

    fun setSubtitle(resId: Int) {
        vb.tvSubtitle.apply {
            setText(resId)
            visible()
        }
    }

    fun setSubtitleTextAppearance(resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setSubtitleBackground(@DrawableRes drawableId: Int) {
        vb.tvSubtitle.apply {
            if (isGone) return
            background = context.drawable(drawableId)
        }
    }

    fun setSubtitleBackground(color: String?) {
        vb.tvSubtitle.apply {
            if (isGone || color.isNullOrEmpty()) return
            val cornerRadiusInDp = context.pxToDp(context.getDimensionFromAttr(R.attr.ChiliProductCardViewSubtitleCornerRadius, 6.dpF)).toInt()
            val drawable = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(Color.parseColor(color))
                this.cornerRadius = dpToPx(context, cornerRadiusInDp)
            }
            background = drawable
        }
    }

    fun setDescription(text: CharSequence?) {
        vb.tvDescription.text = text
    }

    fun setDescription(resId: Int) {
        vb.tvDescription.setText(resId)
    }

    fun setDescriptionTextAppearance(resId: Int) {
        vb.tvDescription.setTextAppearance(resId)
    }

    fun setDiscount(text: CharSequence?, iconUrl: String? = null) {
        with(vb) {
            llDiscount.apply {
                if (!text.isNullOrEmpty()) visible()
                else gone()
            }
            tvDiscount.text = text
        }
        iconUrl?.let { setDiscountIcon(it) }
    }

    fun setDiscount(resId: Int, iconRes: Int? = null) {
        with(vb) {
            llDiscount.visible()
            tvDiscount.setText(resId)
        }
        iconRes?.let { setDiscountIcon(it) }
    }

    private fun setDiscountIcon(@DrawableRes resId: Int) {
        vb.ivDiscountIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    private fun setDiscountIcon(url: String) {
        vb.ivDiscountIcon.apply {
            setImageByUrl(url)
            visible()
        }
    }

    fun setDiscountTextAppearance(resId: Int) {
        vb.tvDiscount.setTextAppearance(resId)
    }

    fun setDiscountBackground(@DrawableRes drawableId: Int) {
        if (vb.llDiscount.isGone) return
        vb.llDiscount.background = context.drawable(drawableId)
    }

    fun setDiscountBackground(color: String?) {
        if (vb.llDiscount.isGone) return
        val cornerRadiusInDp = context.pxToDp(context.getDimensionFromAttr(R.attr.ChiliProductCardViewDiscountCornerRadius, 6.dpF)).toInt()
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.parseColor(color))
            cornerRadius = dpToPx(context, cornerRadiusInDp)
        }
        vb.llDiscount.background = drawable
    }

    fun setDiscountBackground(colors: Array<String>) {
        if (vb.llDiscount.isGone) return
        val cornerRadiusInDp = context.pxToDp(context.getDimensionFromAttr(R.attr.ChiliProductCardViewDiscountCornerRadius, 6.dpF)).toInt()
        val cornerRadiusInPx = dpToPx(context, cornerRadiusInDp)
        val colorInts = colors.map { Color.parseColor(it) }.toIntArray()
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            if (colorInts.size > 1) {
                this.colors = colorInts
                gradientType = GradientDrawable.LINEAR_GRADIENT
                orientation = GradientDrawable.Orientation.LEFT_RIGHT
            } else {
                setColor(colorInts.firstOrNull() ?: Color.TRANSPARENT)
            }
            cornerRadius = cornerRadiusInPx
        }
        vb.llDiscount.background = drawable
    }

    fun setTags(tags: List<TagData>?) {
        tags.run {
            if (!isNullOrEmpty()) {
                forEach { tag ->
                    val parent = if (tag.isTopTag) vb.llTopTags else vb.llBottomTags
                    parent.addView(createTagView(tag, parent))
                    parent.visible()
                }
            }
        }
    }

    private fun createTagView(tag: TagData, root: ViewGroup): View {
        return LayoutInflater.from(context).inflate(R.layout.chili_view_tag_item, root, false).apply {
            val textView = findViewById<TextView>(R.id.tv_tag)
            val iconView = findViewById<ImageView>(R.id.iv_tag_icon)

            textView.apply {
                text = tag.text
                setTextAppearance(tag.textAppearance)
            }

            tag.backgroundColor?.let {
                val drawable = GradientDrawable().apply {
                    shape = GradientDrawable.RECTANGLE
                    setColor(Color.parseColor(it))
                    val cornerRadiusInDp = context.pxToDp(context.getDimensionFromAttr(R.attr.ChiliProductCardViewTagCornerRadius, 6.dpF)).toInt()
                    cornerRadius = dpToPx(context, cornerRadiusInDp)
                }
                background = drawable
            }

            iconView.setImageOrHide(tag.icon)
        }
    }

    fun setSubtitleParts(parts: List<HtmlTextPart>?) {
        with(vb){
            parts.run {
                if (!isNullOrEmpty()) {
                    tvSubtitle.gone()
                    llSubtitle.apply {
                        removeAllViews()
                        visible()
                        forEach { addView(createSubtitlePartView(it)) }
                    }
                }
            }
        }

    }

    private fun createSubtitlePartView(part: HtmlTextPart): TextView {
        return TextView(context).apply {
            setTextAppearance(part.textAppearance)
            maxLines = 1
            ellipsize = TextUtils.TruncateAt.END
            setTextOrHide(part.htmlText)
            vb.placeholder.isVisible = text.isNullOrEmpty()
            setPadding(
                context.getDimensionFromAttr(R.attr.ChiliProductCardViewSubtitlePartPaddingHorizontal, 4.dpF).toInt(),
                0,
                context.getDimensionFromAttr(R.attr.ChiliProductCardViewSubtitlePartPaddingHorizontal, 4.dpF).toInt(),
                0
            )

            background = GradientDrawable().apply {
                shape = GradientDrawable.RECTANGLE
                setColor(Color.parseColor(part.backgroundColor ?: "#00000000"))
                cornerRadius = context.getDimensionFromAttr(R.attr.ChiliProductCardViewSubtitleCornerRadius, 6.dpF)
            }
        }
    }


}

data class TagData(
    val text: String?,
    val icon: String? = null,
    val backgroundColor: String? = null,
    val textAppearance: Int = R.style.Chili_H12_White1,
    val isTopTag: Boolean = true
)

data class HtmlTextPart(
    val htmlText: CharSequence? = null,
    val backgroundColor: String? = null,
    val textAppearance: Int = R.style.Chili_H14_Primary,
)
