package kg.devcats.chili3.view.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.extensions.drawable
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewBottomNavigationBinding

class ChiliBottomNavigationView : ConstraintLayout {

    private lateinit var vb: ChiliViewBottomNavigationBinding

    constructor(context: Context) : super(context) {
        inflateViews()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        inflateViews()
        obtainAttributes(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        inflateViews()
        obtainAttributes(attrs, defStyle)
    }

    private fun inflateViews() {
        vb = ChiliViewBottomNavigationBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet, defStyle: Int = R.style.Chili_BottomNavigationViewStyle) {
        context?.obtainStyledAttributes(attrs, R.styleable.ChiliBottomNavigationView, R.attr.chiliBottomNavigationViewDefaultStyle, defStyle)?.run {
            getResourceId(R.styleable.ChiliBottomNavigationView_menu, -1)
                .takeIf { it != -1 }?.let { inflateMenu(it) }
            getDrawable(R.styleable.ChiliBottomNavigationView_icon)
                ?.let { setIcon(it) }
            getResourceId(R.styleable.ChiliBottomNavigationView_background, R.drawable.chili_rounded_nav_bar)
                .takeIf { it != -1 }?.let { setBottomNavBackground(it) }
            vb.bottomNav.itemIconTintList = null
            recycle()
        }
    }

    fun inflateMenu(resId: Int) = with(vb.bottomNav) {
        menu.clear()
        inflateMenu(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivCenteredIcon.setImageResource(resId)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivCenteredIcon.setImageDrawable(drawable)
        vb.bottomNav.removeBackgroundForItem(2)
    }

    fun setBottomNavBackground(drawableId: Int) {
        vb.viewBackground.background = context.drawable(drawableId)
    }

    fun setupMenuItemsVisibility(vararg items: Pair<Int, Boolean>) {
        vb.bottomNav.setupMenuItemsVisibility(*items)
    }

    fun updateBadgeVisibilityState(itemId: Int, isVisible: Boolean) {
        vb.bottomNav.updateBadgeVisibilityState(itemId, isVisible)
    }

}