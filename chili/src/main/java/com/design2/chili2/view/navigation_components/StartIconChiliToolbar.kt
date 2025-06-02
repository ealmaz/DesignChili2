package com.design2.chili2.view.navigation_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliStartIconViewToolbarBinding
import com.design2.chili2.extensions.setImageByUrl

class StartIconChiliToolbar : LinearLayout {

    private lateinit var vb: ChiliStartIconViewToolbarBinding

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
        vb = ChiliStartIconViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_BaseNavigationComponentsStyle_ChiliToolbar) {
        context?.obtainStyledAttributes(attrs, R.styleable.StartIconChiliToolbar, R.attr.toolbarDefaultStyle, defStyle)?.run {
            setTitle(getString(R.styleable.StartIconChiliToolbar_title))
            getResourceId(R.styleable.StartIconChiliToolbar_toolbarStartIcon, -1).takeIf { it != -1 }?.let {
                setStartIcon(it)
            }
            getColor(R.styleable.StartIconChiliToolbar_background, -1).takeIf { it != -1 }?.let {
                setToolbarBackgroundColor(it)
            }
            getResourceId(R.styleable.StartIconChiliToolbar_textAppearance, -1).takeIf { it != -1 }?.let {
                setTitleTextAppearance(it)
            }
            getResourceId(R.styleable.StartIconChiliToolbar_navigationIcon, -1).takeIf { it != -1 }?.let {
                setNavigationIcon(it)
            }
            recycle()
        }
    }

    /** Toolbar */

    fun initToolbar(config: Configuration) {
        configureToolbar(config)
        (config.hostActivity as? AppCompatActivity)?.run { setSupportActionBar(vb.toolbar)}
        vb.toolbar.setNavigationOnClickListener { config.onNavigateUpClick.invoke(config.hostActivity) }
    }

    private fun configureToolbar(config: Configuration) {
        vb.toolbar.apply { config.title?.let { this@StartIconChiliToolbar.setTitle(it) } }
    }

    fun setToolbarBackgroundColor(@ColorInt colorInt: Int) {
        vb.llRootStartIcon.setBackgroundColor(colorInt)
    }

    /** Navigation Icon */

    fun setNavigationIcon(@DrawableRes drawableRes: Int) {
        vb.toolbar.setNavigationIcon(drawableRes)
    }

    fun onNavigationIconClick(onNavigationIconClick: () -> Unit) {
        vb.toolbar.setNavigationOnClickListener {
            onNavigationIconClick()
        }
    }

    fun hideNavigationIcon() {
        vb.toolbar.navigationIcon = null
    }

    /** Toolbar title */

    fun setTitle(title: String?) {
        vb.toolbarTitle.text = title
    }

    fun getTitle(): String = vb.toolbarTitle.text.toString()

    fun setTitleTextAppearance(@StyleRes textAppearanceRes: Int) {
        vb.toolbarTitle.setTextAppearance(textAppearanceRes)
    }

    /** Start icon */

    fun setStartIcon(icon: Any?) {
        setStartIconVisibility(true)
        when (icon) {
            is String -> setStartIcon(icon)
            is Int -> setStartIcon(icon)
        }
    }

    private fun setStartIcon(@DrawableRes drawableId: Int) {
        vb.toolbarIcon.setImageResource(drawableId)
    }
    private fun setStartIcon(uri: String?) {
        uri?.let {  vb.toolbarIcon.setImageByUrl(uri) }
    }

    fun setStartIconVisibility(isVisible: Boolean) {
        vb.toolbarIcon.visibility = when(isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    /** Menu */

    fun inflateChiliMenu(@MenuRes menuId: Int, onMenuItemClicked: (MenuItem) -> Unit){
        vb.toolbar.inflateMenu(menuId)
        vb.toolbar.setOnMenuItemClickListener {
            onMenuItemClicked(it)
            true
        }
    }
    fun setMenuItemInvisible(id: Int) {
        vb.toolbar.menu?.findItem(id)?.isVisible = false
    }

    fun getRootContainer() = vb.llRootStartIcon

    /** Configuration */

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: String? = null,
        val onNavigateUpClick: FragmentActivity.() -> Unit = { onBackPressed() },
    )
}