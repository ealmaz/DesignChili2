package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.core.view.isVisible
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardStateIconBinding
import kg.devcats.chili3.extensions.getColorFromAttr
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.invisible
import kg.devcats.chili3.extensions.visible

class IconStateCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.iconStateCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_IconStateCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardStateIconBinding

    override val styleableAttrRes: IntArray = R.styleable.IconStateCardView
    override val rootContainer: View
        get() = vb.rootView

    private var icon: Int? = null

    override fun inflateView(context: Context) {
        vb = ChiliViewCardStateIconBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun TypedArray.obtainAttributes() {
        getResourceId(R.styleable.IconStateCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        setTitle(getText(R.styleable.IconStateCardView_title))
        setSubtitle(getText(R.styleable.IconStateCardView_subtitle))
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        with(vb) {
            shimmeringPairs[tvTitle] = viewTitleShimmer
            shimmeringPairs[tvSubtitle] = viewSubtitleShimmer
        }
    }

    override fun onStartShimmer() {
        super.onStartShimmer()
        if (icon != null) vb.ivIcon.invisible()
    }

    override fun onStopShimmer() {
        super.onStopShimmer()
        with(vb.ivIcon) {
            if (icon != null) visible() else gone()
        }
    }

    fun setIcon(iconRes: Int?) {
        icon = iconRes
        vb.ivIcon.run {
            iconRes?.let {
                setImageResource(it)
                visible()
            } ?: gone()
        }
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.run {
            setImageDrawable(drawable)
            visible()
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvSubtitle.text = charSequence
    }

    fun setSubtitle(resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setIconVisibility(isVisible: Boolean) {
        vb.ivIcon.isVisible = isVisible
    }

    fun setupAsErrorState(errorIconRes: Int? = null) = with(vb) {
        setIcon(errorIconRes)
        rootView.setBackgroundResource(R.drawable.chili_bg_card_error_with_border)
    }

    fun clearErrorState() {
        setIcon(null)
        vb.rootView.setBackgroundColor(context.getColorFromAttr(com.design2.chili2.R.attr.ChiliCardViewBackground))
    }
}