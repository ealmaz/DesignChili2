package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCardCategoryBinding
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible

class CategoryCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.categoryCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CategoryCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardCategoryBinding

    override val styleableAttrRes: IntArray = R.styleable.CategoryCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewCardCategoryBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.CategoryCardView_title))
        getResourceId(R.styleable.CategoryCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        setSubtitle(getText(R.styleable.CategoryCardView_subtitle))
        getResourceId(R.styleable.CategoryCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
        getResourceId(R.styleable.CategoryCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.ivIcon] = vb.viewIconShimmer
        shimmeringPairs[vb.tvLabel] = vb.viewLabelShimmer
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvLabel.setTextAppearance(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        with(vb.tvSubtitle){
            isVisible = !charSequence.isNullOrEmpty()
            text = charSequence
        }
    }

    fun setSubtitle(resId: Int) {
        with(vb.tvSubtitle){
            visible()
            setText(resId)
        }
    }

    fun setSubtitleTextAppearance(resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setScaleType(scaleType: Int) {
        vb.ivIcon.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        vb.ivIcon.adjustViewBounds = isAdjusted
    }

    fun setGravity(gravity: Int){
        val iconParams = vb.ivIcon.layoutParams as ConstraintLayout.LayoutParams
        val labelParams = vb.tvLabel.layoutParams as ConstraintLayout.LayoutParams
        when(gravity){
            Gravity.CENTER_HORIZONTAL -> {
                iconParams.horizontalBias = 0.5f
                labelParams.horizontalBias = 0.5f
            }
            Gravity.START -> {
                iconParams.horizontalBias = 0f
                labelParams.horizontalBias = 0f
            }
            Gravity.END -> {
                iconParams.horizontalBias = 1f
                labelParams.horizontalBias = 1f
            }
        }
        vb.ivIcon.layoutParams = iconParams
        vb.tvLabel.layoutParams = labelParams
    }
}