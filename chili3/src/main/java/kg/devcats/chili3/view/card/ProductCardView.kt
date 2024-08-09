package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.core.view.isGone
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.prepareViewForBounceAnimation
import com.design2.chili2.extensions.setOnClickListenerWithBounce
import com.design2.chili2.extensions.setOnSingleClickListenerWithBounce
import com.design2.chili2.extensions.setSafeOnClickListenerWithWarning
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
        shimmeringPairs[vb.tvDiscount] = null
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

    fun setProductImage(src: String?) {
        vb.sivImage.setUrlImageByCoil(
            url = src,
            width = imageSize.first,
            height = imageSize.second
        )
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
            this.text = text
            if (!text.isNullOrEmpty()) visible()
            else gone()
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
        if (vb.tvSubtitle.isGone) return
        vb.tvSubtitle.background = context.drawable(drawableId)
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

    fun setDiscount(text: CharSequence?) {
        vb.tvDiscount.apply {
            this.text = text
            if (!text.isNullOrEmpty()) visible()
            else gone()
        }
    }

    fun setDiscount(resId: Int) {
        vb.tvDiscount.apply {
            setText(resId)
            visible()
        }
    }

    fun setDiscountTextAppearance(resId: Int) {
        vb.tvDiscount.setTextAppearance(resId)
    }

    fun setDiscountBackground(@DrawableRes drawableId: Int) {
        if (vb.tvDiscount.isGone) return
        vb.tvDiscount.background = context.drawable(drawableId)
    }

    fun setDiscountBackground(color: String?) {
        if (vb.tvDiscount.isGone) return
        val cornerRadiusInDp = context.pxToDp(context.getDimensionFromAttr(R.attr.ChiliProductCardViewDiscountCornerRadius, 6.dpF)).toInt()
        val drawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            setColor(Color.parseColor(color))
            cornerRadius = dpToPx(context, cornerRadiusInDp)
        }
        vb.tvDiscount.background = drawable
    }

    fun setDiscountBackground(colors: Array<String>) {
        if (vb.tvDiscount.isGone) return
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
        vb.tvDiscount.background = drawable
    }


    override fun setOnClickListener(listener: OnClickListener?) {
        setSafeOnClickListenerWithWarning { super.setOnClickListener(listener) }
    }

    fun setupOnSingleClickListenerWithBounce(
        scale: Float = 0.95f,
        duration: Long = 200,
        onClick: () -> Unit = {}
    ) {
        setSafeOnClickListenerWithWarning {
            this.run {
                prepareViewForBounceAnimation(vb.root)
                setOnSingleClickListenerWithBounce(scale, duration, onClick)
            }
        }
    }

    fun setupOnClickListenerWithBounce(
        scale: Float = 0.95f,
        duration: Long = 200,
        onClick: () -> Unit = {}
    ) {
        setSafeOnClickListenerWithWarning {
            this.run {
                prepareViewForBounceAnimation(vb.root)
                setOnClickListenerWithBounce(scale, duration, onClick)
            }
        }
    }

}