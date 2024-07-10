package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
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
        vb.rootView.background = gradientDrawable()
    }

    private fun gradientDrawable(): Drawable {
        return GradientDrawable(
            GradientDrawable.Orientation.BL_TR,
            intArrayOf(
                Color.parseColor("#793AE0"),
                Color.parseColor("#E747A0"),
                Color.parseColor("#F63155"),
                Color.parseColor("#F1056F"),
            )
        ).apply {
            cornerRadius = context.resources.getDimension(com.design2.chili2.R.dimen.view_21dp)
            gradientType = GradientDrawable.LINEAR_GRADIENT
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.text = charSequence
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvLabel.setText(resId)
    }

    private fun setTitleTextAppearance(@StyleRes resId: Int) {
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

    override fun setOnClickListener(listener: OnClickListener?) {
        vb.root.setOnClickListener(listener)
    }
}