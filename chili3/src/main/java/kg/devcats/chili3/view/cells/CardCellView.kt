package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.setImageByUrl
import kg.devcats.chili3.R
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.databinding.ChiliViewCardCellBinding

class CardCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.design2.chili2.R.attr.cellViewDefaultStyle,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_CellViewStyle_BaseCellViewStyle
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
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.CardCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.CardCellView_cardCellTitle)?.let { setTitle(it) }
                getString(R.styleable.CardCellView_cardCellSubtitle)?.let { setSubtitle(it) }
                getString(R.styleable.CardCellView_cardCellValue)?.let { setAdditionalText(it) }
                getDrawable(R.styleable.CardCellView_cardCellIcon)?.let { setIcon(it) }
                setIsMain(getBoolean(R.styleable.CardCellView_cardCellIsMain, false))
                setIsBlocked(getBoolean(R.styleable.CardCellView_cardCellIsBlocked, false))
                setIsUniqueStated(getBoolean(R.styleable.CardCellView_cardCellIsUniqueStatused, false))
                setIsChevronVisible(getBoolean(R.styleable.CardCellView_cardCellIsChevronVisible, false))
                getResourceId(R.styleable.CardCellView_cardCellTitleTextAppearance, -1).takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
                getResourceId(R.styleable.CardCellView_cardCellSubtitleTextAppearance, -1).takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
                getResourceId(R.styleable.CardCellView_cardCellAdditionalTextAppearance, -1).takeIf { it != -1 }?.let { setAdditionalTextAppearance(it) }
                recycle()
            }
    }

    private fun setupShimmering() {
        shimmeringPairs[vb.clContent] = vb.viewShimmerContent
    }

    fun setTitle(title: String?) {
        vb.tvTitle.apply {
            text = title
            isVisible = !title.isNullOrEmpty()
        }
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.apply {
            setText(resId)
            isVisible = true
        }
    }

    fun setSubtitle(subtitle: String?) {
        vb.tvSubtitle.apply {
            text = subtitle
            isVisible = !subtitle.isNullOrEmpty()
        }

    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.apply {
            setText(resId)
            isVisible = true
        }

    }

    fun setAdditionalText(value: String?) {
        vb.tvAdditionalText.apply {
            text = value
            isVisible = !value.isNullOrEmpty()
        }
    }

    fun setAdditionalText(@StringRes resId: Int) {
        vb.tvAdditionalText.apply {
            setText(resId)
            isVisible = true
        }
    }

    fun setIcon(icon: Drawable?) {
        vb.ivIcon.setImageDrawable(icon)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIsMain(isMain: Boolean) {
        vb.ivStar.isVisible = isMain
    }

    fun setIsBlocked(isBlocked: Boolean) {
        with(vb) {
            val alphaValue = if (isBlocked) 0.4f else 1f

            ivIcon.alpha = alphaValue
            tvTitle.alpha = alphaValue
            tvSubtitle.alpha = alphaValue
            tvAdditionalText.alpha = alphaValue
            ivStar.alpha = alphaValue
            ivChevron.alpha = alphaValue

            ivLock.isVisible = isBlocked
        }
    }

    fun setIsUniqueStated(
        isUniqueStated: Boolean,
        color: Int = resources.getColor(com.design2.chili2.R.color.folly_1)
    ) {
        with(vb) {
            if (isUniqueStated) {
                tvSubtitle.setTextColor(color)
                tvAdditionalText.setTextColor(color)
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
