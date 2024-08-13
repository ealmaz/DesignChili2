package com.design2.chili2.view.navigation_components

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
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewToolbarBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide

class ChiliToolbar : LinearLayout {

    private lateinit var vb: ChiliViewToolbarBinding

    constructor(context: Context) : super(context) {
        setupView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setupView()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setupView()
        obtainAttributes(attrs, defStyle)
    }

    private fun setupView() {
        vb = ChiliViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_BaseNavigationComponentsStyle_ChiliToolbar) {
        context?.obtainStyledAttributes(attrs, R.styleable.ChiliToolbar, R.attr.toolbarDefaultStyle, defStyle)?.run {
            setTitle(getString(R.styleable.ChiliToolbar_title))
            setAdditionalText(getString(R.styleable.ChiliToolbar_additionalText))
            getBoolean(R.styleable.ChiliToolbar_isDividerVisible, true).let {
                setupDividerVisibility(it)
            }
            getResourceId(R.styleable.ChiliToolbar_toolbarEndIcon, -1).takeIf { it != -1 }?.let {
                setEndIcon(it)
            }
            getDimensionPixelSize(R.styleable.ChiliToolbar_toolbarEndIconSize, -1).takeIf { it != -1 }?.let {
                setEndIconSize(it, it)
            }

            getResourceId(R.styleable.ChiliToolbar_toolbarLeftIcon, -1).takeIf { it != -1 }?.let {
                setStartIcon(it)
            }
            getDimensionPixelSize(R.styleable.ChiliToolbar_toolbarLeftIconSize, -1).takeIf { it != -1 }?.let {
                setStartIconSize(it, it)
            }

            getResourceId(R.styleable.ChiliToolbar_toolbarTextAppearance, -1).takeIf { it != -1 }?.let {
                setTitleTextAppearance(it)
            }
            getResourceId(R.styleable.ChiliToolbar_additionalTextTextAppearance, -1).takeIf { it != -1 }?.let {
                setAdditionalTextTextAppearance(it)
            }
            getColor(R.styleable.ChiliToolbar_background, -1).takeIf { it != -1 }?.let {
                setToolbarBackgroundColor(it)
            }
            getResourceId(R.styleable.ChiliToolbar_navigationIcon, -1).takeIf { it != -1 }?.let {
                setNavigationIcon(it)
            }
            getResourceId(R.styleable.ChiliToolbar_collapseIcon, -1).takeIf { it != -1 }?.let {
                setCollapseIcon(it)
            }
            getBoolean(R.styleable.ChiliToolbar_titleCentered, false).let {
                setIsTitleCentered(it)
            }
            getColor(R.styleable.ChiliToolbar_titleColor, -1).takeIf { it != -1}?.let {
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
            config.navigationIconRes?.let { this@ChiliToolbar.setNavigationIcon(it) }
            config.title?.let { this@ChiliToolbar.setTitle(it) }
            config.centeredTitle?.let { setIsTitleCentered(it) }
        }
    }

    fun setupBasic(activity: FragmentActivity, title: String = "") {
        initToolbar(
            Configuration(
                hostActivity = activity,
                title = title,
                navigationIconRes = R.drawable.chili_ic_back_arrow_rounded,
                isNavigateUpButtonEnabled = true
            ))
    }

    fun setNavigationIcon(@DrawableRes drawableRes: Int) {
        vb.toolbarView.setNavigationIcon(drawableRes)
    }

    fun setCollapseIcon(@DrawableRes drawableRes: Int) {
        vb.toolbarView.setCollapseIcon(drawableRes)
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

    fun setEndIcon(@DrawableRes drawableId: Int) {
        setIconVisibility(true)
        vb.ivEndIcon.setImageResource(drawableId)
    }

    fun setEndIcon(drawable: Drawable?) {
        setIconVisibility(true)
        vb.ivEndIcon.setImageDrawable(drawable)
    }

    fun setIconVisibility(isVisible: Boolean) {
        vb.ivEndIcon.visibility = when(isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setEndIconClickListener(action: () -> Unit) {
        vb.ivEndIcon.setOnSingleClickListener { action.invoke() }
    }

    fun setEndIconSize(widthPx: Int, heightPx: Int) {
        val layoutParams = vb.ivEndIcon.layoutParams?.apply {
            width = widthPx
            height = heightPx
        } ?: LayoutParams(widthPx, heightPx)
        vb.ivEndIcon.layoutParams = layoutParams
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