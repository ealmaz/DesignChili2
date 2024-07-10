package kg.devcats.chili3.view.cells

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.extensions.setImageByUrl
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewQuickActionButtonBinding

class QuickActionButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.quickActionButtonViewStyle,
    defStyleRes: Int = R.style.QuickActionButtonTitleStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewQuickActionButtonBinding

    private var onClickListener: OnClickListener? = null
    @DrawableRes private var rippleIconId: Int? = null
    @DrawableRes private var disabledIconId: Int? = null
    @DrawableRes private var iconId: Int ? = null
    private val rootContainer: View
        get() = vb.rootView

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setClickHandler()
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewQuickActionButtonBinding.inflate(LayoutInflater.from(context), this, true)
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
                .takeIf { it != -1 }?.let {
                    iconId = it
                    setIcon(iconId!!)
                }
            rippleIconId = getResourceId(R.styleable.QuickActionButtonView_rippleIcon, -1)
                .takeIf { it != -1 }
            disabledIconId = getResourceId(R.styleable.QuickActionButtonView_disabledIcon, -1)
                .takeIf { it != -1 }
            setIsCardEnabled(getBoolean(R.styleable.QuickActionButtonView_isEnable, true))
            recycle()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setClickHandler() {
        rootContainer.setOnTouchListener(OnTouchListener { _, event ->
            if (event?.action == MotionEvent.ACTION_DOWN){
                rippleIconId?.let { setIcon(it) }
                return@OnTouchListener true
            }
            if (event?.action == MotionEvent.ACTION_CANCEL) {
                iconId?.let { setIcon(it) }
                return@OnTouchListener true
            }
            if (event?.action == MotionEvent.ACTION_UP) {
                iconId?.let { setIcon(it) }
                this.onClickListener?.onClick(vb.rootView)
                return@OnTouchListener true
            }
            return@OnTouchListener false
        })
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.text = charSequence
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String?) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setIsCardEnabled(isEnabled: Boolean) {
        when (isEnabled) {
            true -> enableButton()
            else -> disableButton()
        }
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        this.onClickListener = listener
        vb.root.setOnClickListener(onClickListener)
    }

    fun disableButton() {
        vb.root.isEnabled = false
        setTitleTextAppearance(R.style.QuickActionButtonDisabled)
        disabledIconId?.let {
            setIcon(it)
        }
    }


    fun enableButton() {
        vb.root.isEnabled = true
        setTitleTextAppearance(R.style.QuickActionButtonTitleStyle)
        iconId?.let { setIcon(it) }
    }
}