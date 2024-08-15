package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardCaptionsBinding

class CaptionsCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chiliCaptionsCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CaptionsCard
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardCaptionsBinding

    override val rootContainer: View
        get() = vb.root

    override val styleableAttrRes: IntArray
        get() = R.styleable.ChiliCaptionsCardView

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardCaptionsBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getString(R.styleable.ChiliCaptionsCardView_title))
        setValue(getString(R.styleable.ChiliCaptionsCardView_value))
        getResourceId(R.styleable.ChiliCaptionsCardView_titleTextAppearance, -1).takeIf { it != -1 }?.let {
            setTitleTextAppearance(it)
        }
        getResourceId(R.styleable.ChiliCaptionsCardView_valueTextAppearance, -1).takeIf { it != -1 }?.let {
            setValueTextAppearance(it)
        }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.llRoot] = vb.shimmer
    }

    fun setTitle(text: CharSequence?) {
        vb.tvTitle.setTextOrHide(text)
    }

    fun setTitle(textRes: Int?) {
        vb.tvTitle.setTextOrHide(textRes)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setValue(text: CharSequence?) {
        vb.tvValue.setTextOrHide(text)
    }

    fun setValue(textRes: Int?) {
        vb.tvValue.setTextOrHide(textRes)
    }

    fun setValueTextAppearance(resId: Int) {
        vb.tvValue.setTextAppearance(resId)
    }

}