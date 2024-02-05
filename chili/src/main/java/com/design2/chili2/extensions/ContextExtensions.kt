package com.design2.chili2.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.LayerDrawable
import android.os.Build
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import android.view.Window
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.facebook.shimmer.ShimmerFrameLayout

internal fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.color(@ColorRes colorId: Int): Int {
    return ContextCompat.getColor(this, colorId)
}

fun Context.drawable(@DrawableRes drawableId: Int): Drawable? {
    return ContextCompat.getDrawable(this, drawableId)
}


@RequiresApi(api = Build.VERSION_CODES.M)
fun Window.updateNavigationBarColor() {
    val metrics = DisplayMetrics()
    windowManager.defaultDisplay.getMetrics(metrics)
    val dimDrawable = GradientDrawable()
    val navigationBarDrawable = GradientDrawable()
    navigationBarDrawable.shape = GradientDrawable.RECTANGLE
    navigationBarDrawable.setColor(context.getColorFromAttr(R.attr.ChiliScreenBackground))
    val layers: Array<Drawable> = arrayOf(dimDrawable, navigationBarDrawable)
    val windowBackground = LayerDrawable(layers)
    windowBackground.setLayerInsetTop(1, metrics.heightPixels)
    setBackgroundDrawable(windowBackground)
}

fun Context.getPixelSizeFromAttr(@AttrRes resId: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(resId, typedValue, true)
    return resources.getDimensionPixelSize(typedValue.resourceId)
}

fun Context.createShimmerLayout(block: ShimmerFrameLayout.() -> Unit? = {}): ShimmerFrameLayout {
    return ShimmerFrameLayout(this).apply {
        block()
        layoutParams = ConstraintLayout.LayoutParams(0, ConstraintLayout.LayoutParams.WRAP_CONTENT)
        visibility = View.GONE
    }
}

fun Context.createShimmerView(@DimenRes width: Int): View {
    return View(this).apply {
        layoutParams = ConstraintLayout.LayoutParams(
            resources.getDimensionPixelSize(width),
            resources.getDimensionPixelSize(R.dimen.view_8dp)
        )
        background = ContextCompat.getDrawable(context, R.drawable.chili_bg_shimmer)
    }
}