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
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliCircleStartIconViewToolbarBinding
import kg.devcats.chili3.extensions.gone

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
            is Int -> setStartIcon(drawableId = icon)
            else -> setStartIcon(drawableId = null)
        }
    }

    fun setStartIcon(@DrawableRes drawableId: Int?) {
        vb.startIcon.setImageOrHide(drawableId)
    }

    fun setStartIcon(uri: String?) = with(vb.startIcon) {
       uri?.let { setImageByUrl(uri) } ?: gone()
    }

    private fun setPrimaryEndIcon(icon: Any?) {
        when (icon) {
            is String -> setPrimaryEndIcon(uri = icon)
            is Int -> setPrimaryEndIcon(drawableId = icon)
            else -> setPrimaryEndIcon(drawableId = null)
        }
    }

    fun setPrimaryEndIcon(@DrawableRes drawableId: Int?) = with(vb) {
        llIcons.isVisible = drawableId != null || ibEndIconSecondary.isVisible
        ibEndIconPrimary.setImageOrHide(drawableId)
    }

    fun setPrimaryEndIcon(uri: String?) = with(vb) {
        llIcons.isVisible = !uri.isNullOrEmpty() || ibEndIconSecondary.isVisible
        uri?.let { ibEndIconPrimary.setImageByUrl(uri) } ?: gone()
    }

    private fun setSecondaryEndIcon(icon: Any?) {
        when (icon) {
            is String -> setSecondaryEndIcon(uri = icon)
            is Int -> setSecondaryEndIcon(drawableId = icon)
            else -> setSecondaryEndIcon(drawableId = null)
        }
    }

    fun setSecondaryEndIcon(@DrawableRes drawableId: Int?) = with(vb) {
        llIcons.isVisible = drawableId != null || ibEndIconPrimary.isVisible
        ibEndIconSecondary.setImageOrHide(drawableId)

    }

    fun setSecondaryEndIcon(uri: String?) = with(vb) {
        llIcons.isVisible = !uri.isNullOrEmpty() || ibEndIconPrimary.isVisible
       uri?.let { ibEndIconSecondary.setImageByUrl(uri) } ?: gone()
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