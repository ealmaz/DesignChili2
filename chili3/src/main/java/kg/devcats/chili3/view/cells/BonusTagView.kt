package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupAsSecure
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewBonusTagBinding

class BonusTagView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bonusTagViewStyle,
    defStyleRes: Int = R.style.BonusTagViewStyle
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewBonusTagBinding

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupView()
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewBonusTagBinding.inflate(LayoutInflater.from(context), this, true)
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

    private fun setupView() {
        setIcon(R.drawable.ic_bonus_new)
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.setTextOrHide(charSequence)
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

    fun setIcon(drawable: Drawable?) {
        vb.ivIcon.setImageOrHide(drawable)
    }

    fun setupAsSecureTag() {
        vb.tvLabel.setupAsSecure()
    }
}