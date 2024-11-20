package kg.devcats.chili3.view.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Spanned
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.extensions.setOnSingleClickListener
import kg.devcats.chili3.extensions.visible
import kg.devcats.chili3.view.cells.BonusTagView

class ToolbarWithBonusTag @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : TailedToolbarView(context, attrs) {

    private var bonusTagView : BonusTagView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateEndIcon()
    }

    private fun inflateEndIcon() {
        this.bonusTagView = BonusTagView(context)
        vb.flEndPlaceHolder.apply {
            visible()
            addView(bonusTagView)
        }
    }

    fun setTagTitle(text: String?) {
        bonusTagView?.setTitle(text)
    }

    fun setTagTitle(@StringRes resId: Int) {
        bonusTagView?.setTitle(resId)
    }

    fun setTagTitle(text: Spanned) {
        bonusTagView?.setTitle(text)
    }

    fun setTagIcon(@DrawableRes resId: Int) {
        bonusTagView?.setIcon(resId)
    }

    fun setTagIcon(url: String?) {
        bonusTagView?.setIcon(url)
    }

    fun setTagIcon(drawable: Drawable?) {
        bonusTagView?.setIcon(drawable)
    }

    fun setTagAlpha(alpha: Float = 1f) {
        bonusTagView?.alpha = alpha
    }

    fun setTagState(isAvailable: Boolean) {
        setTagAlpha(if (isAvailable) 1f else 0.4f)
    }

    fun setOnTagClickListener(onClick: () -> Unit) {
        bonusTagView?.setOnSingleClickListener { onClick.invoke() }
    }

    fun setupAsSecureTag() {
        bonusTagView?.setupAsSecureTag()
    }
}