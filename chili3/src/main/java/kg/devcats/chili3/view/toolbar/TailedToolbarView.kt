package kg.devcats.chili3.view.toolbar

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliTailedToolbarBinding

open class TailedToolbarView : LinearLayout {

    protected lateinit var vb: ChiliTailedToolbarBinding

    constructor(context: Context) : super(context) {
        inflateView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateView(context)
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        inflateView(context)
        obtainAttributes(attrs, defStyle)
    }

    protected open fun inflateView(context: Context) {
        vb = ChiliTailedToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    protected fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_TailedToolbarView) {
        context?.obtainStyledAttributes(attrs, R.styleable.TailedToolbarView, R.attr.TailedToolbarDefaultStyle, defStyle)?.run {
            setTitle(getString(R.styleable.TailedToolbarView_title))
            setAdditionalText(getString(R.styleable.TailedToolbarView_additionalText))
            getBoolean(R.styleable.TailedToolbarView_isDividerVisible, true).let {
                setupDividerVisibility(it)
            }
            getResourceId(R.styleable.TailedToolbarView_toolbarLeftIcon, -1).takeIf { it != -1 }?.let {
                setStartIcon(it)
            }
            getDimensionPixelSize(R.styleable.TailedToolbarView_toolbarLeftIconSize, -1).takeIf { it != -1 }?.let {
                setStartIconSize(it, it)
            }

            getResourceId(R.styleable.TailedToolbarView_toolbarTextAppearance, -1).takeIf { it != -1 }?.let {
                setTitleTextAppearance(it)
            }
            getResourceId(R.styleable.TailedToolbarView_additionalTextTextAppearance, -1).takeIf { it != -1 }?.let {
                setAdditionalTextTextAppearance(it)
            }
            getColor(R.styleable.TailedToolbarView_background, -1).takeIf { it != -1 }?.let {
                setToolbarBackgroundColor(it)
            }
            getResourceId(R.styleable.TailedToolbarView_navigationIcon, -1).takeIf { it != -1 }?.let {
                setNavigationIcon(it)
            }
            getBoolean(R.styleable.TailedToolbarView_titleCentered, false).let {
                setIsTitleCentered(it)
            }
            getColor(R.styleable.TailedToolbarView_titleColor, -1).takeIf { it != -1}?.let {
                setTitleColor(it)
            }
            recycle()
        }
    }

    fun setTitleColor(@ColorInt colorRes:Int) {
        vb.toolbarView.setTitleTextColor(colorRes)
    }

    fun initToolbar(config: Configuration) {
        configureToolbar(config)
        (config.hostActivity as? AppCompatActivity)?.run {
            setSupportActionBar(vb.toolbarView)
            supportActionBar?.setDisplayHomeAsUpEnabled(config.isNavigateUpButtonEnabled)
            supportActionBar?.setHomeButtonEnabled(config.isNavigateUpButtonEnabled)
        }
        vb.toolbarView.setNavigationOnClickListener { config.onNavigateUpClick.invoke(config.hostActivity) }
    }

    private fun configureToolbar(config: Configuration) {
        vb.toolbarView.apply {
            config.navigationIconRes?.let { this@TailedToolbarView.setNavigationIcon(it) }
            config.title?.let { this@TailedToolbarView.setTitle(it) }
            config.centeredTitle?.let { setIsTitleCentered(it) }
        }
    }

    fun setNavigationIcon(@DrawableRes drawableRes: Int) {
        vb.toolbarView.setNavigationIcon(drawableRes)
    }

    fun setTitle(title: String?) {
        vb.toolbarView.title = title
    }

    fun setTitleTextAppearance(@StyleRes textAppearanceRes: Int) {
        vb.toolbarView.setTitleTextAppearance(context, textAppearanceRes)
    }

    fun setIsTitleCentered(centered: Boolean) {
        vb.toolbarView.isTitleCentered = centered
    }

    fun setAdditionalText(@StringRes resId: Int?) {
        vb.tvAdditionalText.setTextOrHide(resId)
    }

    fun setAdditionalText(text: String?) {
        vb.tvAdditionalText.setTextOrHide(text)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int) {
        vb.tvAdditionalText.setTextAppearance(resId)
    }

    fun setStartIcon(@DrawableRes drawableId: Int) {
        setStartIconVisibility(true)
        vb.ivStartIcon.setImageResource(drawableId)
    }

    fun setStartIcon(drawable: Drawable?) {
        setStartIconVisibility(true)
        vb.ivStartIcon.setImageDrawable(drawable)
    }

    fun setStartIconVisibility(isVisible: Boolean) {
        vb.ivStartIcon.visibility = when(isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setStartIconClickListener(action: () -> Unit) {
        vb.ivStartIcon.setOnSingleClickListener { action.invoke() }
    }

    fun setStartIconSize(widthPx: Int, heightPx: Int) {
        val layoutParams = vb.ivStartIcon.layoutParams?.apply {
            width = widthPx
            height = heightPx
        } ?: LayoutParams(widthPx, heightPx)
        vb.ivStartIcon.layoutParams = layoutParams
    }

    fun setToolbarBackgroundColor(@ColorInt colorInt: Int) {
        vb.llRoot.setBackgroundColor(colorInt)
    }

    fun setupDividerVisibility(isVisible: Boolean) {
        vb.divider.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun isUpHomeEnabled(hostActivity: AppCompatActivity, isEnabled: Boolean) {
        hostActivity.apply {
            supportActionBar?.setDisplayHomeAsUpEnabled(isEnabled)
            supportActionBar?.setHomeButtonEnabled(isEnabled)
        }
    }

    fun setOnLongClick(action: () -> Unit) {
        vb.toolbarView.setOnLongClickListener { action.invoke(); true }
    }

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: String? = null,
        val centeredTitle: Boolean? = null,
        val navigationIconRes: Int? = null,
        val onNavigateUpClick: FragmentActivity.() -> Unit = { onBackPressed() },
        val isNavigateUpButtonEnabled: Boolean = false,
    )
}