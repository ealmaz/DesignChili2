package kg.devcats.chili3.view.navigation_components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.extensions.applyStateListAnimatorFromTheme
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliCircleStartIconViewToolbarBinding

class CircleStartIconChiliToolbar : LinearLayout {

    private lateinit var vb: ChiliCircleStartIconViewToolbarBinding

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
        vb = ChiliCircleStartIconViewToolbarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_CircleStartIconChiliToolbarStyle) {
        context?.obtainStyledAttributes(attrs, R.styleable.CircleStartIconChiliToolbar, R.attr.CircleStartIconChiliToolbarDefaultStyle, defStyle)?.run {
            getColor(R.styleable.CircleStartIconChiliToolbar_background, -1).takeIf { it != -1 }?.let {
                setToolbarBackgroundColor(it)
            }
            setStartIcon(
                getResourceId(R.styleable.CircleStartIconChiliToolbar_startIcon, -1).takeIf { it != -1 }
            )
            setTitle(getString(R.styleable.CircleStartIconChiliToolbar_title))
            getResourceId(R.styleable.CircleStartIconChiliToolbar_titleTextAppearance, -1).takeIf { it != -1 }?.let {
                setTitleTextAppearance(it)
            }
            setClickableOnProfile(
                getBoolean(R.styleable.CircleStartIconChiliToolbar_isProfileClickable, false)
            )
            setPrimaryEndIcon(
                getResourceId(R.styleable.CircleStartIconChiliToolbar_endIconPrimary, -1).takeIf { it != -1 }
            )
            setSecondaryEndIcon(
                getResourceId(R.styleable.CircleStartIconChiliToolbar_endIconSecondary, -1).takeIf { it != -1 }
            )
            recycle()
        }
    }

    fun initToolbar(config: Configuration): Unit = with(vb)  {
        (config.hostActivity as? AppCompatActivity)?.run { setSupportActionBar(toolbar)}
        setTitle(config.title)
        setStartIcon(config.startIcon)
        setPrimaryEndIcon(config.endIconPrimary)
        setSecondaryEndIcon(config.endIconSecondary)
        llProfileContainer.setOnSingleClickListener { config.onClick(ClickableElementType.PROFILE_CONTAINER) }
        ibEndIconPrimary.setOnSingleClickListener { config.onClick(ClickableElementType.END_ICON) }
        ibEndIconSecondary.setOnSingleClickListener { config.onClick(ClickableElementType.ADDITIONAL_END_ICON) }
    }

    fun setToolbarBackgroundColor(@ColorInt colorInt: Int) {
        vb.llRoot.setBackgroundColor(colorInt)
    }

    private fun setTitle(icon: Any?) {
        when (icon) {
            is String -> setTitle(title = icon)
            is Int -> setTitle(stringRes = icon)
            else -> setTitle(stringRes = null)
        }
    }

    fun setTitle( @StringRes stringRes: Int?) {
        vb.toolbarTitle.setTextOrHide(stringRes)
    }

    fun setTitle(title: String?) {
        vb.toolbarTitle.setTextOrHide(title)
    }

    fun getTitle(): String = vb.toolbarTitle.text.toString()

    fun setTitleTextAppearance(@StyleRes textAppearanceRes: Int) {
        vb.toolbarTitle.setTextAppearance(textAppearanceRes)
    }

    private fun setStartIcon(icon: Any?) {
        when (icon) {
            is String -> setStartIcon(uri = icon)
            is Int -> setStartIcon(drawableRes = icon)
            else -> setStartIcon(drawableRes = null)
        }
    }

    fun setStartIcon(@DrawableRes drawableRes: Int?) {
        vb.startIcon.setImageOrHide(drawableRes)
    }

    fun setStartIcon(uri: String?) = with(vb.startIcon) {
        isVisible = !uri.isNullOrEmpty()
        setImageByUrl(uri)
    }

    private fun setClickableOnProfile(clickable: Boolean) = with(vb.llProfileContainer) {
        isClickable = clickable
        isFocusable = clickable

        if (clickable) applyStateListAnimatorFromTheme(
            context,
            R.attr.CircleStartIconChiliToolbarProfileContainerStateListAnimator
        )
        else stateListAnimator = null
    }

    private fun setPrimaryEndIcon(icon: Any?) {
        when (icon) {
            is String -> setPrimaryEndIcon(uri = icon)
            is Int -> setPrimaryEndIcon(drawableRes = icon)
            else -> setPrimaryEndIcon(drawableRes = null)
        }
    }

    fun setPrimaryEndIcon(@DrawableRes drawableRes: Int?) = with(vb) {
        llIcons.isVisible = drawableRes != null || ibEndIconSecondary.isVisible
        ibEndIconPrimary.setImageOrHide(drawableRes)
    }

    fun setPrimaryEndIcon(uri: String?) = with(vb) {
        llIcons.isVisible = !uri.isNullOrEmpty() || ibEndIconSecondary.isVisible
        ibEndIconPrimary.apply {
            isVisible = !uri.isNullOrEmpty()
            setImageByUrl(uri)
        }
    }

    private fun setSecondaryEndIcon(icon: Any?) {
        when (icon) {
            is String -> setSecondaryEndIcon(uri = icon)
            is Int -> setSecondaryEndIcon(drawableRes = icon)
            else -> setSecondaryEndIcon(drawableRes = null)
        }
    }

    fun setSecondaryEndIcon(@DrawableRes drawableRes: Int?) = with(vb) {
        llIcons.isVisible = drawableRes != null || ibEndIconPrimary.isVisible
        ibEndIconSecondary.setImageOrHide(drawableRes)
    }

    fun setSecondaryEndIcon(uri: String?) = with(vb) {
        llIcons.isVisible = !uri.isNullOrEmpty() || ibEndIconPrimary.isVisible
        ibEndIconSecondary.apply {
            isVisible = !uri.isNullOrEmpty()
            setImageByUrl(uri)
        }
    }

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: Any? = null,
        val startIcon: Any? = null,
        val endIconPrimary: Any? = null,
        val endIconSecondary: Any? = null,
        val onClick: (ClickableElementType) -> Unit
    )

    enum class ClickableElementType {
        PROFILE_CONTAINER,
        END_ICON,
        ADDITIONAL_END_ICON
    }

}