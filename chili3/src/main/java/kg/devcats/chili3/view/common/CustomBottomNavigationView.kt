package kg.devcats.chili3.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import androidx.core.view.children
import com.design2.chili2.extensions.drawable
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kg.devcats.chili3.R

class CustomBottomNavigationView(context: Context, attributeSet: AttributeSet)
    : BottomNavigationView(context, attributeSet) {

    private val notifiedItemsId = mutableSetOf<Int>()
    private val badgeTag = "badge"

    fun setupMenuItemsVisibility(vararg items: Pair<Int, Boolean>) {
        clearAllBadges()
        items.forEach { menu.findItem(it.first)?.isVisible = it.second }
        restoreBadges()
    }

    private fun clearAllBadges() {
        (getChildAt(0) as BottomNavigationMenuView).children.forEach {
            (it as? BottomNavigationItemView)?.removeBadge()
        }
    }

    fun removeBackgroundForItem(index: Int) {
        val itemView: View = (getChildAt(0) as BottomNavigationMenuView).getChildAt(index)
        itemView.background = null
    }

    private fun restoreBadges() {
        notifiedItemsId.forEach { addBadgeOnItem(it) }
    }

    fun updateBadgeVisibilityState(itemId: Int, isVisible: Boolean) {
        if (isVisible) addBadgeOnItem(itemId)
        else removeBadgeOnItem(itemId)
    }

    private fun addBadgeOnItem(itemId: Int) {
        notifiedItemsId.add(itemId)
        val drawable = context.drawable(R.drawable.chili_ic_dot)
        val childes = (getChildAt(0) as BottomNavigationMenuView).children
        val view = childes.firstOrNull { it.id == itemId } as BottomNavigationItemView
        val img = ImageView(context).apply { background = drawable; tag = badgeTag }
        view.addView(img, getNotifierLayoutParams())
    }

    private fun removeBadgeOnItem(itemId: Int) {
        notifiedItemsId.remove(itemId)
        (getChildAt(0) as BottomNavigationMenuView).children
                .firstOrNull { it.id == itemId }
                ?.let { (it as? BottomNavigationItemView)?.removeBadge() }
    }

    private fun getNotifierLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
            gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
            leftMargin = resources.getDimension(com.design2.chili2.R.dimen.padding_8dp).toInt()
            topMargin = resources.getDimension(com.design2.chili2.R.dimen.padding_8dp).toInt()
        }
    }

    fun BottomNavigationItemView.removeBadge() {
        while (children.filter { it.tag == badgeTag }.count() > 0) {
            children.filter { it.tag == badgeTag }.forEach { removeView(it) }
        }
    }
}