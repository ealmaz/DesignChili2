package kg.devcats.chili3.view.divider

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewDividerBinding

class DividerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.dividerViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_DividerViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewDividerBinding

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewDividerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.DividerView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.DividerView_dividerTitle)?.let { setTitle(it) }
                getString(R.styleable.DividerView_dividerSubtitle)?.let { setSubtitle(it) }
                getString(R.styleable.DividerView_dividerActionText)?.let { setActionText(it) }
                getDrawable(R.styleable.DividerView_dividerEndIcon)?.let { setEndIcon(it) }
                setIsNotificationVisible(
                    getBoolean(
                        R.styleable.DividerView_dividerIsNotificationVisible,
                        false
                    )
                )
                getResourceId(
                    R.styleable.DividerView_dividerTitleTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
                getResourceId(
                    R.styleable.DividerView_dividerSubtitleTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
                getResourceId(
                    R.styleable.DividerView_dividerActionTextAppearance,
                    -1
                ).takeIf { it != -1 }?.let { setActionTextAppearance(it) }

                recycle()
            }
    }

    fun setTitle(title: String?) {
        vb.tvTitle.setTextOrHide(title)
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.setTextOrHide(resId)
    }

    fun setSubtitle(subtitle: String?) {
        vb.tvSubtitle.setTextOrHide(subtitle)
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setTextOrHide(resId)
    }

    fun setActionText(value: String?) {
        vb.tvAction.setTextOrHide(value)
    }

    fun setActionText(@StringRes resId: Int) {
        vb.tvAction.setTextOrHide(resId)
    }

    fun setEndIcon(icon: Drawable?) {
        vb.ivEndIcon.setImageOrHide(icon)
    }

    fun setEndIcon(url: String) {
        vb.ivEndIcon.setImageOrHide(url)
    }

    fun setEndIcon(@DrawableRes resId: Int) {
        vb.ivEndIcon.setImageOrHide(resId)
    }

    fun setIsNotificationVisible(isNotificationVisible: Boolean) {
        vb.ivNotification.isVisible = isNotificationVisible
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setActionTextAppearance(@StyleRes resId: Int) {
        vb.tvAction.setTextAppearance(resId)
    }

    fun setEndIconClickListener(action: () -> Unit) {
        vb.ivEndIcon.setOnSingleClickListener(action)
    }

    fun setActionClickListener(action: () -> Unit) {
        vb.tvAction.setOnSingleClickListener(action)
    }
}