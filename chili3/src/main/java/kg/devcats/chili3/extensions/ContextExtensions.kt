package kg.devcats.chili3.extensions

import android.content.Context
import android.util.TypedValue
import androidx.annotation.AttrRes

internal fun Context.getColorFromAttr(
    @AttrRes attrColor: Int,
    typedValue: TypedValue = TypedValue(),
    resolveRefs: Boolean = true
): Int {
    theme.resolveAttribute(attrColor, typedValue, resolveRefs)
    return typedValue.data
}

fun Context.getDimensionFromAttr(resId: Int, defaultValue: Float = 0F): Float {
    val typedValue = TypedValue()
    val resolved = theme.resolveAttribute(resId, typedValue, true)
    return if (resolved) {
        TypedValue.complexToDimension(typedValue.data, resources.displayMetrics)
    } else {
        defaultValue
    }
}