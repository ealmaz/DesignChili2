package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.content.res.AppCompatResources
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
                getString(R.styleable.CardCellView_title)?.let { setTitle(it) }
                getString(R.styleable.CardCellView_subtitle)?.let { setSubtitle(it) }
                getString(R.styleable.CardCellView_value)?.let { setAdditionalText(it) }
                getDrawable(R.styleable.CardCellView_icon)?.let { setIcon(it) }
                setMain(getBoolean(R.styleable.CardCellView_isMain, false))
                setBlocked(getBoolean(R.styleable.CardCellView_isBlocked, false))
                setUniqueStatused(getBoolean(R.styleable.CardCellView_isUniqueStatused, false))
                setChevronVisible(getBoolean(R.styleable.CardCellView_isChevronVisible, false))
                getResourceId(R.styleable.CardCellView_titleTextAppearance, -1).takeIf { it != -1 }
                    ?.let { setTitleTextAppearance(it) }
                getResourceId(
                    R.styleable.CardCellView_subtitleTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
                getResourceId(
                    R.styleable.CardCellView_additionalTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let { setAdditionalTextAppearance(it) }
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
            visibility = if (subtitle.isNullOrEmpty()) View.GONE else View.VISIBLE
        }

    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
        vb.tvSubtitle.visibility = View.VISIBLE
    }

    fun setAdditionalText(value: String?) {
        vb.tvAdditionalText.text = value
        vb.tvAdditionalText.visibility = if (value.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    fun setAdditionalText(@StringRes resId: Int) {
        vb.tvAdditionalText.setText(resId)
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

    fun setMain(isMain: Boolean) {
        vb.ivStar.visibility = if (isMain) View.VISIBLE else View.GONE
    }

    fun setBlocked(isBlocked: Boolean) {
        with(vb) {
            val alphaValue = if (isBlocked) 0.5f else 1f

            ivIcon.alpha = alphaValue
            tvTitle.alpha = alphaValue
            tvSubtitle.alpha = alphaValue
            tvAdditionalText.alpha = alphaValue
            ivStar.alpha = alphaValue
            ivChevron.alpha = alphaValue

            ivLock.visibility = if (isBlocked) View.VISIBLE else View.GONE
        }
    }

    fun setUniqueStatused(
        isUniqueStatused: Boolean,
        color: Int = resources.getColor(com.design2.chili2.R.color.folly_1)
    ) {
        with(vb) {
            if (isUniqueStatused) {
                tvSubtitle.setTextColor(color)
                tvAdditionalText.setTextColor(color)
            }
        }
    }

    fun setChevronVisible(isChevronVisible: Boolean) {
        vb.ivChevron.visibility = if (isChevronVisible) View.VISIBLE else View.GONE
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
