package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.design2.chili2.extensions.setImageByUrl
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewBonusTagBinding

class BonusTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bonusTagViewStyle,
    defStyleRes: Int = R.style.BonusTagTitleStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewBonusTagBinding
    private var onClickListener: OnClickListener? = null
    private val alphaEnabled = 1.0f
    private val alphaDisabled = 0.3f
    private var iconId: Int ? = null

    private fun inflateView(context: Context) {
        vb = ChiliViewBonusTagBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupView()
    }

    private fun setupView() {
        setIcon(R.drawable.ic_bonus_new)
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.BonusTagView,
            defStyleAttr,
            defStyleRes
        ).run {
            setTitle(getText(R.styleable.BonusTagView_title))
            getResourceId(R.styleable.BonusTagView_titleTextAppearance, -1)
                .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
            getResourceId(R.styleable.BonusTagView_icon, -1)
                .takeIf { it != -1 }?.let { setIcon(it) }
            recycle()
        }
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
        iconId = resId
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setIsCardEnabled(isEnabled: Boolean) {
        when (isEnabled) {
            true -> enableCard()
            else -> disableCard()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
        vb.root.setOnClickListener(onClickListener)
    }

    private fun disableCard() {
        with(vb) {
            root.setOnClickListener(null)
            root.isEnabled = false
            tvLabel.alpha = alphaDisabled
            ivIcon.alpha = alphaDisabled
        }
    }

    private fun enableCard() {
        with(vb) {
            root.setOnClickListener(onClickListener)
            root.isEnabled = true
            tvLabel.alpha = alphaEnabled
            ivIcon.alpha = alphaEnabled
        }
    }

    private fun onStartShimmer() {
        disableCard()
    }

    private fun onStopShimmer() {
        enableCard()
    }
}