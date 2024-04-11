package com.design2.chili2.extensions

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.URLSpan
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.design2.chili2.R
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.image.SquircleView
import java.util.concurrent.TimeUnit

internal var View.lastItemClickTime: Long
    set(value) {
        setTag(R.id.lastItemClickTime, value)
    }
    get() {
        return getTag(R.id.lastItemClickTime) as? Long ?: 0L
    }

fun View.setOnSingleClickListener(action: () -> Unit) {
    setOnClickListener {
        if (lastItemClickTime == 0L
            || TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastItemClickTime) >= 1) {
            lastItemClickTime = System.currentTimeMillis()
            action()
        }
    }
}

fun View.setOnDoubleClickListener(action: () -> Unit) {
    setOnClickListener {
        val clickTime = System.currentTimeMillis()
        if (TimeUnit.MILLISECONDS.toSeconds(clickTime - lastItemClickTime) < 0.2) action()
        lastItemClickTime = clickTime
    }
}

internal fun View.visible() {
    visibility = View.VISIBLE
}

internal var View.lastCharInputTime: Long
    set(value) {
        setTag(R.id.lastCharInputTime, value)
    }
    get() {
        return getTag(R.id.lastCharInputTime) as? Long ?: 0L
    }

internal fun EditText.setOnTextChangedListenerWithDebounce(debounceSeconds: Long, action: () -> Unit) {
    addTextChangedListener {
        val changedTime = System.currentTimeMillis()
        if (TimeUnit.MILLISECONDS.toSeconds(changedTime - lastCharInputTime) > debounceSeconds) {
            action()
        }
    }
}

internal fun View.isVisible(): Boolean {
    return visibility == View.VISIBLE
}

internal fun View.invisible() {
    visibility = View.INVISIBLE
}

internal fun View.gone() {
    visibility = View.GONE
}

fun TextView.setTextOrHide(value: String?) {
    text = value
    when (value) {
        null -> gone()
        else -> visible()
    }
}

fun TextView.setTextOrHide(value: Spanned?) {
    text = value
    when (value) {
        null -> gone()
        else -> visible()
    }
}

fun TextView.setTextOrHide(resId: Int?) {
    when (resId) {
        null -> gone()
        else -> {
            visible()
            setText(resId)
        }
    }
}

fun TextView.setTextOrHide(charSequence: CharSequence?) {
    text = charSequence
    if (charSequence == null) gone()
    else visible()
}

fun ImageView.setImageOrHide(drawable: Drawable?) {
    when (drawable) {
        null -> gone()
        else -> {
            setImageDrawable(drawable)
            visible()
        }
    }
}

fun ImageView.setImageOrHide(@DrawableRes resId: Int?) {
    when (resId) {
        null -> gone()
        else -> {
            setImageResource(resId)
            visible()
        }
    }
}

fun SquircleView.setImageByUrl(url: String?) {
    Glide.with(this)
        .load(url)
        .dontTransform()
        .into(this)
}

fun ImageView.setImageByUrl(url: String?, placeHolderDrawable: Drawable? = null) {
    Glide.with(this)
        .load(url)
        .placeholder(placeHolderDrawable)
        .dontTransform()
        .into(this)
}

fun ImageView.setImageByUrl(url: String?, width: Int, height: Int, placeHolderDrawable: Drawable? = null) {
    Glide.with(this)
        .load(url)
        .placeholder(placeHolderDrawable)
        .override(width,height)
        .into(this)
}

fun ImageView.setImageByUrlWithListener(imageUrl: String?, onSuccess: ((Drawable) -> (Unit))? = null,
                                    onError: ((GlideException?) -> (Unit))? = null, requestOptions: RequestOptions? = null) : RequestBuilder<Drawable>? {
    if (imageUrl == null) {
        onError?.invoke(null)
        return null
    }
    val builder = Glide.with(this.context)
        .load(imageUrl)
        .listener(getGlideOnLoadListener(onSuccess, onError))
    if (requestOptions != null) builder.apply(requestOptions)
    builder.into(this)
    return builder
}

private fun getGlideOnLoadListener(onSuccess: ((Drawable) -> Unit)?, onError: ((GlideException?) -> Unit)?): RequestListener<Drawable>? {
    val isHaveActionParams = onSuccess != null || onError != null
    if (!isHaveActionParams) return null
    else return object: RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?,
                                  model: Any?,
                                  target: Target<Drawable>?,
                                  isFirstResource: Boolean): Boolean {
            onError?.invoke(e)
            return true
        }

        override fun onResourceReady(resource: Drawable,
                                     model: Any?,
                                     target: Target<Drawable>?,
                                     dataSource: DataSource?,
                                     isFirstResource: Boolean): Boolean {
            onSuccess?.invoke(resource)
            return false
        }
    }
}

fun View.setupRoundedCellCornersMode(modeValue: Int, isSurfaceClickable: Boolean? = null) {
    this.setBackgroundResource(
        when (modeValue) {
            RoundedCornerMode.TOP.value -> R.drawable.chili_cell_rounded_top_background
            RoundedCornerMode.MIDDLE.value -> R.drawable.chili_cell_rounded_middle_background
            RoundedCornerMode.BOTTOM.value -> R.drawable.chili_cell_rounded_bottom_background
            else -> R.drawable.chili_cell_rounded_background
        }
    )
    isSurfaceClickable?.let {
        setRippleForeground(modeValue, it)
    }
}

fun ViewGroup.setIsSurfaceClickable(isSurfaceClickable: Boolean, cornerMode: Int? = null) {
    isClickable = isSurfaceClickable
    isFocusable = isSurfaceClickable
    setRippleForeground(cornerMode, isSurfaceClickable)
}

fun View.setRippleForeground(cornerMode: Int? = null, isSurfaceClickable: Boolean) {
    foreground = if (isSurfaceClickable) {
        when (cornerMode) {
            RoundedCornerMode.TOP.value -> AppCompatResources.getDrawable(context, R.drawable.chili_ripple_top_corner_foreground)
            RoundedCornerMode.MIDDLE.value -> AppCompatResources.getDrawable(context, R.drawable.chili_ripple_middle_corner_foreground)
            RoundedCornerMode.BOTTOM.value -> AppCompatResources.getDrawable(context, R.drawable.chili_ripple_bottom_corner_background)
            else -> AppCompatResources.getDrawable(context, R.drawable.chili_ripple_rounded_corner_foreground)
        }
    } else  null
}


fun View.setLeftMargin(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(margin, params.topMargin, params.rightMargin, params.bottomMargin)
    layoutParams = params
}

fun View.setRightMargin(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, params.topMargin, margin, params.bottomMargin)
    layoutParams = params
}

fun View.setHorizontalMargin(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(margin, params.topMargin, margin, params.bottomMargin)
    layoutParams = params
}

fun View.setBottomMargin(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, params.topMargin, params.rightMargin, margin)
    layoutParams = params
}

fun View.setTopMargin(margin: Int) {
    val params = layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(params.leftMargin, margin, params.rightMargin, params.bottomMargin)
    layoutParams = params
}

/**
 * Метод позволяет слушать клики на линки в TextView
 * example:
 * string resource <string>Hello <a href="https://www.google.com"</a></string>
 * view.handleUrlClicks { url -> {action} }
 * */
fun TextView.handleUrlClicks(onClicked: ((String) -> Unit)? = null) {
    text = SpannableStringBuilder.valueOf(text).apply {
        getSpans(0, length, URLSpan::class.java).forEach {
            setSpan(object : ClickableSpan() {
                override fun onClick(widget: View) {
                    onClicked?.invoke(it.url)
                }
            }, getSpanStart(it), getSpanEnd(it), Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            removeSpan(it)
        }
    }
    movementMethod = LinkMovementMethod.getInstance()
}

fun ConstraintLayout.setupConstraint(action: ConstraintSet.() -> Unit) {
    val constraintSet = ConstraintSet()
    constraintSet.clone(this)
    action.invoke(constraintSet)
    constraintSet.applyTo(this)
}

fun Drawable.recolorDrawable(@ColorInt color: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this)
    DrawableCompat.setTint(wrapped, color)
    return wrapped
}

fun Context.recolorDrawable(@DrawableRes drawableId: Int, @ColorRes colorId: Int): Drawable {
    val wrapped = DrawableCompat.wrap(this.drawable(drawableId)!!)
    DrawableCompat.setTint(wrapped, this.color(colorId))
    return wrapped
}

fun Drawable.copy(): Drawable {
    return this.mutate().constantState?.newDrawable()?.mutate() ?: this
}

fun View.setOnDragDropListener(action: () -> Unit) {
    setOnTouchListener { v, event ->
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> action()
            MotionEvent.ACTION_UP -> v.performClick()
        }
        return@setOnTouchListener true
    }
}

fun View.setScrollListener(startScrollAction: () -> Unit, stopScrollAction:() -> Unit ) {
    setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                startScrollAction()
            }
            MotionEvent.ACTION_UP -> {
                stopScrollAction()
                v.performClick()
            }
        }
        return@setOnTouchListener false
    }
}