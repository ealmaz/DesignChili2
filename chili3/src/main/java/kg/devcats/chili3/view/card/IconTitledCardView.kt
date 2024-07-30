package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setUrlImage
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardIconTitledBinding
import kg.devcats.chili3.extensions.getDimensionFromAttr

class IconTitledCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.iconTitledViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_IconTitledCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes)  {

    private lateinit var vb: ChiliViewCardIconTitledBinding

    override val styleableAttrRes: IntArray = R.styleable.IconTitledCardView

    private var imageSize: Pair<Int, Int> = Pair(0, 0)

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.ivIcon] = vb.viewIconShimmer
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardIconTitledBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setImageSize(
            getImageSize(R.attr.ChiliIconTitledCardViewIconSize),
            getImageSize(R.attr.ChiliIconTitledCardViewIconSize)
        )
        getResourceId(R.styleable.IconTitledCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        getString(R.styleable.IconTitledCardView_title)?.let {
            setTitle(it)
        }
        getInteger(R.styleable.IconTitledCardView_android_scaleType, ScaleType.FIT_CENTER.ordinal).let {
            setScaleType(it)
        }
        getBoolean(R.styleable.IconTitledCardView_android_adjustViewBounds, false).let {
            setAdjustViewBounds(it)
        }
    }

    private fun getImageSize(@AttrRes resId: Int): Int {
        return context.getDimensionFromAttr(resId, 40.dpF).toInt()
    }

    fun setIcon(@DrawableRes drawableId: Int) {
        vb.ivIcon.setImageDrawable(context.drawable(drawableId))
    }

    fun setIcon(url: String?) {
        vb.ivIcon.setUrlImage(
            url = url,
            width = imageSize.first,
            height = imageSize.second
        )
    }

    /**
     * Must be called before setIcon(url: String?) method
     */
    fun setImageSize(width: Int, height: Int) {
        imageSize = width to height
    }

    fun setScaleType(scaleType: Int) {
        vb.ivIcon.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        vb.ivIcon.adjustViewBounds = isAdjusted
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.text = title
    }

}