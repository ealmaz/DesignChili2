package kg.devcats.chili3.view.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.databinding.ChiliViewCollapsingToolbarBinding
import kg.devcats.chili3.R.style.*

class CollapsingChiliToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val vb = ChiliViewCollapsingToolbarBinding.inflate(LayoutInflater.from(context), this, true)

    init { attrs?.let { obtainAttributes(it) } }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = Chili_CollapseToolbarView) {
        context?.obtainStyledAttributes(attrs, R.styleable.CollapsingChiliToolbar, kg.devcats.chili3.R.attr.CollapsingToolbarDefaultStyle, defStyle)?.run {
            setTitle(getString(R.styleable.CollapsingChiliToolbar_title))
            setSubtitle(getString(R.styleable.CollapsingChiliToolbar_subtitle))
            getBoolean(R.styleable.CollapsingChiliToolbar_isDividerVisible, true).let { setupDividerVisibility(it) }
            getResourceId(R.styleable.CollapsingChiliToolbar_toolbarEndIcon, -1).takeIf { it != -1 }?.let { setEndIcon(it) }
            getDimensionPixelSize(R.styleable.CollapsingChiliToolbar_toolbarEndIconSize, -1).takeIf { it != -1 }?.let { setEndIconSize(it, it) }
            getResourceId(R.styleable.CollapsingChiliToolbar_navigationIcon, -1).takeIf { it != -1 }?.let { setNavigateIcon(it) }
            getResourceId(R.styleable.CollapsingChiliToolbar_toolbarTextAppearance, -1).takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
            getColor(R.styleable.CollapsingChiliToolbar_background, -1).takeIf { it != -1 }?.let { setToolbarBackgroundColor(it) }
            getColor(R.styleable.CollapsingChiliToolbar_titleColor, -1).takeIf { it != -1}?.let { setTitleColor(it) }
            recycle()
        }
    }

    fun setupCollapsingToolbar(collapseToolbarConfig: Configuration) {
        collapseToolbarConfig.run {
            title?.let { setTitle(it) }
            subtitle?.let { setSubtitle(it) }

            if (isNavigateUpButtonEnabled) {
                setNavigateIcon(navigationIconRes ?: R.drawable.chili_ic_back)
                setNavigateIconClickListener { onNavigateUpClick(hostActivity) }
            }

            scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                val location = IntArray(2)
                triggerView.getLocationOnScreen(location)
                val isCollapsed = location[1] <= this@CollapsingChiliToolbar.height
                if (isCollapsed) {
                    (onCollapsed ?: {
                        setSubtitle(collapsingSubtitle ?: (triggerView as? TextView)?.text)
                        setTitleTextAppearance(Chili_H14_Primary_Bold)
                    }).invoke()
                } else {
                    (onDefault ?: {
                        setSubtitle(null)
                        setTitleTextAppearance(Chili_H16_Primary_Bold)
                    }).invoke()
                }
            }
        }
    }

    fun setTitle(text: CharSequence?) = vb.tvTitle.setTextOrHide(text)

    fun setSubtitle(text: CharSequence?) = vb.tvSubtitle.setTextOrHide(text)

    fun setTitleTextAppearance(@StyleRes style: Int) = vb.tvTitle.setTextAppearance(style)

    fun setSubtitleTextAppearance(@StyleRes style: Int) = vb.tvSubtitle.setTextAppearance(style)

    fun setTitleColor(@ColorInt color: Int) = vb.tvTitle.setTextColor(color)

    fun setNavigateIcon(@DrawableRes resId: Int?) = vb.ivNavigateIcon.setImageOrHide(resId)

    fun setNavigateIcon(drawable: Drawable?) = vb.ivNavigateIcon.setImageOrHide(drawable)

    fun setNavigateIconVisibility(visible: Boolean) {
        vb.ivNavigateIcon.isVisible = visible
    }

    fun setNavigateIconClickListener(action: () -> Unit) = vb.ivNavigateIcon.setOnSingleClickListener { action() }

    fun setEndIcon(@DrawableRes resId: Int?) = vb.ivEndIcon.setImageOrHide(resId)

    fun setEndIcon(drawable: Drawable?) = vb.ivEndIcon.setImageOrHide(drawable)

    fun setEndIconSize(widthPx: Int, heightPx: Int)  = vb.ivEndIcon.apply {
        val layParams = layoutParams?.apply {
            width = widthPx
            height = heightPx
        } ?: LayoutParams(widthPx, heightPx)
        layoutParams = layParams
    }

    fun setIconVisibility(visible: Boolean) {
        vb.ivEndIcon.isVisible = visible
    }

    fun setEndIconClickListener(action: () -> Unit) = vb.ivEndIcon.setOnSingleClickListener { action() }

    fun setToolbarBackgroundColor(@ColorInt color: Int) = vb.llRoot.setBackgroundColor(color)

    fun setupDividerVisibility(visible: Boolean) {
        vb.divider.isVisible = visible
    }

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: CharSequence? = null,
        val subtitle: CharSequence? = null,
        val collapsingSubtitle: CharSequence? = null,
        val navigationIconRes: Int? = null,
        val onNavigateUpClick: FragmentActivity.() -> Unit = { onBackPressedDispatcher.onBackPressed() },
        val isNavigateUpButtonEnabled: Boolean = false,
        val scrollView: NestedScrollView,
        val triggerView: View,
        val onDefault: (() -> Unit)? = null,
        val onCollapsed: (() -> Unit)? = null
    )

}
