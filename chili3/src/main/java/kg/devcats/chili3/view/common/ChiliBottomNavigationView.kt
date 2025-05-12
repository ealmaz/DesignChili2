package kg.devcats.chili3.view.common

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.OvershootInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.google.android.material.navigation.NavigationBarView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewBottomNavigationBinding

class ChiliBottomNavigationView : ConstraintLayout {

    private lateinit var vb: ChiliViewBottomNavigationBinding
    private var animatedItemScale = DEFAULT_ANIMATED_ITEM_SCALE
    private var animatedItemDuration = DEFAULT_ANIMATED_ITEM_DURATION
    private var isItemAnimated = false
    private val mapOfItemAnimations by lazy { mutableMapOf<View, AnimatorSet>() }

    private var onItemSelectedListener: NavigationBarView.OnItemSelectedListener? = null
    private var onIconClick: (() -> Unit)? = null

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
            setItemsAnimation(getBoolean(R.styleable.ChiliBottomNavigationView_isAnimated, false))
            getFloat(R.styleable.ChiliBottomNavigationView_animationScale, -1f)
                .takeIf { it < 0 }?.let(::setAnimatedItemScale)
            getInt(R.styleable.ChiliBottomNavigationView_android_animationDuration, -1)
                .takeIf { it != -1 }?.let(::setAnimatedItemDuration)

            vb.bottomNav.itemIconTintList = null
            vb.bottomNav.removeBackgroundForItem(2)
            vb.viewBackground.setOnClickListener(null)
            recycle()
            setupItemSelectedAnimation()
        }
    }

    fun inflateMenu(resId: Int) = with(vb.bottomNav) {
        menu.clear()
        inflateMenu(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
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

    fun setOnIconClickListener(action: () -> Unit) {
        onIconClick = action
    }

    fun getSelectedItemId(): Int {
        return vb.bottomNav.selectedItemId
    }

    fun getBottomNavView(): CustomBottomNavigationView {
        return vb.bottomNav
    }

    fun getBackgroundView(): View {
        return vb.viewBackground
    }

    fun setItemsAnimation(isAnimated: Boolean) {
        isItemAnimated = isAnimated
    }

    fun setAnimatedItemScale(scale: Float?) {
        scale?.let {
            if (scale in 1f..3f) animatedItemScale = scale
        }
        mapOfItemAnimations.clear()
    }

    fun setAnimatedItemDuration(duration: Int?) {
        duration?.let {
            if (it in 1..1000) animatedItemDuration = duration.toLong()
        }
        mapOfItemAnimations.clear()
    }

    fun setOnItemSelectedListener(listener: NavigationBarView.OnItemSelectedListener) {
        this.onItemSelectedListener = listener
    }

    private fun setupItemSelectedAnimation() = with(vb.bottomNav){
        setupOnIconClickAnimation()
        setOnItemSelectedListener { menuItem ->
            if(!isItemAnimated) {
                return@setOnItemSelectedListener onItemSelected(menuItem)
            }

            val selectedItemView = findViewById<View>(menuItem.itemId)
            selectedItemView.startAnimation()
            return@setOnItemSelectedListener onItemSelected(menuItem)
        }
    }

    private fun onItemSelected(menuItem: MenuItem): Boolean {
        return onItemSelectedListener?.onNavigationItemSelected(menuItem) == true
    }

    private fun setupOnIconClickAnimation() = with(vb.ivIcon) {
        setOnSingleClickListener {
            if(!isItemAnimated) {
                onIconClick?.invoke()
                return@setOnSingleClickListener
            }
            startAnimation()
            onIconClick?.invoke()
        }
    }

    private fun applyBounceAnimation(view: View): AnimatorSet {
        val scaleXUp = ObjectAnimator.ofFloat(view, "scaleX", 1.0f, animatedItemScale)
        val scaleYUp = ObjectAnimator.ofFloat(view, "scaleY", 1.0f, animatedItemScale)

        val scaleXDown = ObjectAnimator.ofFloat(view, "scaleX", animatedItemScale, 1.0f)
        val scaleYDown = ObjectAnimator.ofFloat(view, "scaleY", animatedItemScale, 1.0f)

        scaleXDown.interpolator = OvershootInterpolator()
        scaleYDown.interpolator = OvershootInterpolator()

        val animatorSet = AnimatorSet().apply {
            play(scaleXUp).with(scaleYUp)
            play(scaleXDown).with(scaleYDown).after(scaleXUp)
            duration = animatedItemDuration
        }
        mapOfItemAnimations[view] = animatorSet
        return animatorSet
    }

    private fun View.startAnimation() {
        (mapOfItemAnimations[this] ?: applyBounceAnimation(this)).run {
            end()
            start()
        }
    }

    companion object {
        private const val DEFAULT_ANIMATED_ITEM_SCALE = 1.3F
        private const val DEFAULT_ANIMATED_ITEM_DURATION = 68L
    }
}