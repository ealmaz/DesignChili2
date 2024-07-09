package kg.devcats.chili3.view.cells

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import com.design2.chili2.extensions.setImageByUrl
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewQuickActionButtonBinding

class QuickActionButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.quickActionButtonViewStyle,
    defStyleRes: Int = R.style.ChiliQuickActionButtonTextColor
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewQuickActionButtonBinding

    private var onCardClickListener: OnClickListener? = null
    private val alphaEnabled = 1.0f
    private val alphaDisabled = 0.3f
    private var rippleIconId: Int? = null
    private var iconId: Int ? = null
    private val rootContainer: View
        get() = vb.rootView

    private fun inflateView(context: Context) {
        vb = ChiliViewQuickActionButtonBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupView()
    }

    private fun setupView() {
        clickHandler()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun clickHandler() {
        rootContainer.setOnTouchListener(OnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN){
                rippleIconId?.let {
                    vb.ivIcon.setImageResource(it)
                }
                return@OnTouchListener true
            }
            if (event?.action == MotionEvent.ACTION_UP) {
                iconId?.let {
                    vb.ivIcon.setImageResource(it)
                }
                return@OnTouchListener true
            }
            return@OnTouchListener false
        })
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.QuickActionButtonView,
            defStyleAttr,
            defStyleRes
        ).run {
            setTitle(getText(R.styleable.QuickActionButtonView_title))
            getResourceId(R.styleable.QuickActionButtonView_titleTextAppearance, -1)
                .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
            getResourceId(R.styleable.QuickActionButtonView_icon, -1)
                .takeIf { it != -1 }?.let { setIcon(it) }

            rippleIconId = getResourceId(R.styleable.QuickActionButtonView_rippleIcon, -1)
                .takeIf { it != -1 }

            setIsCardEnabled(getBoolean(R.styleable.QuickActionButtonView_isEnable, true))
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
        this.onCardClickListener = listener
        vb.root.setOnClickListener(onCardClickListener)
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
            root.setOnClickListener(onCardClickListener)
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