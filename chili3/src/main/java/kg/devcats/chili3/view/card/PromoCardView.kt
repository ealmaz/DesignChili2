package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardPromoBinding
import kg.devcats.chili3.util.PromoStatus

class PromoCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.promoCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PromoCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    override val styleableAttrRes: IntArray = R.styleable.PromoCardView

    private var promoStatus = mutableListOf(
        PromoStatus.NEW to null,
        PromoStatus.ACTIVE to null,
        PromoStatus.WAIT to null,
        PromoStatus.EXPIRED to context.drawable(R.drawable.chili_bg_expired_promo),
    )

    private lateinit var vb: ChiliViewCardPromoBinding

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardPromoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.PromoCardView_title))
        getResourceId(R.styleable.PromoCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.PromoCardView_promoIcon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        setStatusText(getText(R.styleable.PromoCardView_promoStatusText))
        getResourceId(R.styleable.PromoCardView_promoStatusTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getLayoutDimension(R.styleable.PromoCardView_promoStatus, -1)
            .takeIf { it != -1 }?.let { setStatus(it) }
        getDrawable(R.styleable.PromoCardView_statusBackground)?.let {
            setStatusBackground(it)
        }
        getDrawable(R.styleable.PromoCardView_promoExpiredBorder)?.let {
            updateStatusBorder(PromoStatus.EXPIRED, it)
        }

    }

    override fun setupShimmeringViews() {

    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setStatusText(charSequence: CharSequence?) {
        vb.tvStatus.text = charSequence
    }

    fun setStatusText(resId: Int) {
        vb.tvStatus.setText(resId)
    }

    fun setStatusTextAppearance(resId: Int) {
        vb.tvStatus.setTextAppearance(resId)
    }

    fun setStatusBackground(drawable: Drawable?) {
        vb.cvStatus.background = drawable
    }

    fun setStatusBackground(@DrawableRes drawableId: Int) {
        vb.cvStatus.background = context.drawable(drawableId)
    }

    fun setPromoBackground(status: Int) {
        vb.clContainer.background = promoStatus[status].second
    }

    private fun setStatus(status: Int) {
        when (status) {
            PromoStatus.NEW.value -> setStatusBackground(R.drawable.chili_bg_promo_status_new)
            PromoStatus.ACTIVE.value -> setStatusBackground(R.drawable.chili_bg_promo_status_new)
            PromoStatus.WAIT.value -> setStatusBackground(R.drawable.chili_bg_promo_status_new)
            PromoStatus.EXPIRED.value -> {
                setStatusBackground(R.drawable.chili_bg_promo_status_new)
                setPromoBackground(status)
            }
            else -> {}
        }
    }

    private fun updateStatusBorder(status: PromoStatus, newDrawable: Drawable) {
        val index = promoStatus.indexOfFirst { it.first == status }
        if (index != -1) {
            promoStatus[index] = status to newDrawable
        }
    }

}