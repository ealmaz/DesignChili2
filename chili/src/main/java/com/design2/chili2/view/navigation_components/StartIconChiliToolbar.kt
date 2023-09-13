package com.design2.chili2.view.navigation_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.MenuRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.view.image.SquircleView
import com.google.android.material.appbar.MaterialToolbar

class StartIconChiliToolbar : LinearLayout {

    private lateinit var view: StartIconChiliToolbarViewVariables

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
        val view = LayoutInflater.from(context).inflate(R.layout.chili_start_icon_view_toolbar, this)
        this.view = StartIconChiliToolbarViewVariables(
            toolbar = view.findViewById(R.id.toolbar),
            rootView = view.findViewById(R.id.ll_root_start_icon),
            ivStartIcon = view.findViewById(R.id.toolbar_icon),
            toolbarTitle = view.findViewById(R.id.toolbar_title)
        )
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
        (config.hostActivity as? AppCompatActivity)?.run { setSupportActionBar(view.toolbar)}
        view.toolbar.setNavigationOnClickListener { config.onNavigateUpClick.invoke(config.hostActivity) }
    }

    private fun configureToolbar(config: Configuration) {
        view.toolbar.apply { config.title?.let { this@StartIconChiliToolbar.setTitle(it) } }
    }

    fun setToolbarBackgroundColor(@ColorInt colorInt: Int) {
        view.rootView.setBackgroundColor(colorInt)
    }

    /** Navigation Icon */

    fun setNavigationIcon(@DrawableRes drawableRes: Int) {
        view.toolbar.setNavigationIcon(drawableRes)
    }

    fun onNavigationIconClick(onNavigationIconClick: () -> Unit) {
        view.toolbar.setNavigationOnClickListener {
            onNavigationIconClick()
        }
    }

    fun hideNavigationIcon() {
        view.toolbar.navigationIcon = null
    }

    /** Toolbar title */

    fun setTitle(title: String?) {
        view.toolbarTitle.text = title
    }

    fun getTitle(): String = view.toolbarTitle.text.toString()

    fun setTitleTextAppearance(@StyleRes textAppearanceRes: Int) {
        view.toolbarTitle.setTextAppearance(textAppearanceRes)
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
        view.ivStartIcon.setImageResource(drawableId)
    }
    private fun setStartIcon(uri: String?) {
        uri?.let {  view.ivStartIcon.setImageByUrl(uri) }
    }

    fun setStartIconVisibility(isVisible: Boolean) {
        view.ivStartIcon.visibility = when(isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    /** Menu */

    fun inflateChiliMenu(@MenuRes menuId: Int, onMenuItemClicked: (MenuItem) -> Unit){
        view.toolbar.inflateMenu(menuId)
        view.toolbar.setOnMenuItemClickListener {
            onMenuItemClicked(it)
            true
        }
    }
    fun setMenuItemInvisible(id: Int) {
        view.toolbar.menu?.findItem(id)?.isVisible = false
    }

    /** Configuration */

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: String? = null,
        val onNavigateUpClick: FragmentActivity.() -> Unit = { onBackPressed() },
    )
}

data class StartIconChiliToolbarViewVariables(
    val rootView: ConstraintLayout,
    val toolbar: MaterialToolbar,
    val toolbarTitle: TextView,
    val ivStartIcon: SquircleView)