package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setUrlImage
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardPromoBinding
import kg.devcats.chili3.extensions.getDimensionFromAttr
import kg.devcats.chili3.util.PromoStatus

class PromoCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.promoCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_PromoCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    override val styleableAttrRes: IntArray = R.styleable.PromoCardView
    private var promoStatus = PromoStatus.NO_STATUS

    private var promoStatuses = mutableListOf(
        PromoStatus.NO_STATUS to null,
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
            .takeIf { it != -1 }?.let { setStatusTextAppearance(it) }
        getLayoutDimension(R.styleable.PromoCardView_promoStatus, -1)
            .takeIf { it != -1 }?.let { setStatus(it) }
        getDrawable(R.styleable.PromoCardView_statusBackground)?.let {
            setStatusBackground(it)
        }
        getDrawable(R.styleable.PromoCardView_promoExpiredBorder)?.let {
            updateStatusBorder(PromoStatus.EXPIRED, it)
        }
    }

    override val rootContainer: View
        get() = vb.flRoot

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.tvLabel] = vb.viewLabelShimmer
        shimmeringPairs[vb.ivIcon] = vb.viewIconShimmer
        shimmeringPairs[vb.tvStatus] = vb.viewStatusShimmer
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
        vb.ivIcon.setUrlImage(
            url = url,
            width = context.getDimensionFromAttr(R.attr.ChiliPromoCardViewIconSize, 32.dpF).toInt(),
            height = context.getDimensionFromAttr(R.attr.ChiliPromoCardViewIconSize, 32.dpF).toInt()
        )
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
        vb.tvStatus.background = drawable
    }

    fun setStatusBackground(@DrawableRes drawableId: Int) {
        vb.tvStatus.background = context.drawable(drawableId)
    }

    fun setPromoBackground(status: Int) {
        vb.clContainer.background = promoStatuses[status].second
        promoStatus = promoStatuses[status].first
    }

    fun setStatus(status: Int) {
        when (status) {
            PromoStatus.NEW.value -> setStatusBackground(R.drawable.chili_bg_promo_status_new_gradient)
            PromoStatus.ACTIVE.value -> setStatusBackground(R.drawable.chili_bg_promo_status_active_gradient)
            PromoStatus.WAIT.value -> setStatusBackground(R.drawable.chili_bg_promo_status_wait_gradient)
            PromoStatus.EXPIRED.value -> {
                setStatusBackground(R.drawable.chili_bg_promo_status_expired_gradient)
                setPromoBackground(status)
            }
            else -> {
                setStatusBackground(null)
                setPromoBackground(status)
            }
        }
    }

    private fun updateStatusBorder(status: PromoStatus, newDrawable: Drawable) {
        val index = promoStatuses.indexOfFirst { it.first == status }
        if (index != -1) {
            promoStatuses[index] = status to newDrawable
        }
    }

    override fun onStartShimmer() {
        if (promoStatus == PromoStatus.EXPIRED) vb.clContainer.background = null
    }

    override fun onStopShimmer() {
        if (promoStatus == PromoStatus.EXPIRED) {
            vb.clContainer.background = promoStatuses[promoStatus.value].second
        }
    }

}