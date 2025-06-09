package kg.devcats.chili3.view.toolbar

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.FragmentActivity
import com.design2.chili2.view.navigation_components.ChiliToolbar

class CollapsingChiliToolbar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet
) : ChiliToolbar(context, attrs) {

    fun setupCollapsingToolbar(collapseToolbarConfig: Configuration) {
        collapseToolbarConfig.run {
            initToolbar(Configuration(
                hostActivity = hostActivity,
                title = title,
                centeredTitle = centeredTitle,
                navigationIconRes = navigationIconRes,
                onNavigateUpClick = onNavigateUpClick,
                isNavigateUpButtonEnabled = isNavigateUpButtonEnabled,
                subtitle = subtitle
            ))
            scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
                val location = IntArray(2)
                triggerTextView.getLocationOnScreen(location)
                val isCollapsed = location[1] <= vb.toolbarView.height

                if (isCollapsed) {
                    (onCollapsed ?: {
                        setSubtitle(triggerTextView.text)
                        setSubtitleTextAppearance(kg.devcats.chili3.R.style.Chili_H12_Secondary)
                        setTitleTextAppearance(kg.devcats.chili3.R.style.Chili_H14_Primary_Bold)
                    }).invoke()
                } else {
                    (onDefault ?: {
                        setSubtitle(subtitle = null)
                        setTitleTextAppearance(kg.devcats.chili3.R.style.Chili_H16_Primary_Bold)
                    }).invoke()
                }
            }
        }

    }

    data class Configuration(
        val hostActivity: FragmentActivity,
        val title: String? = null,
        val centeredTitle: Boolean? = null,
        val navigationIconRes: Int? = null,
        val onNavigateUpClick: FragmentActivity.() -> Unit = { onBackPressed() },
        val isNavigateUpButtonEnabled: Boolean = false,
        val subtitle: CharSequence? = null,
        val scrollView: NestedScrollView,
        val triggerTextView: TextView,
        val onDefault: (() -> Unit)? = null,
        val onCollapsed: (() -> Unit)? = null
    )
}