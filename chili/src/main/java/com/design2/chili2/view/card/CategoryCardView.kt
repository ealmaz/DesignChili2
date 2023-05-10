package com.design2.chili2.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.R
import com.facebook.shimmer.ShimmerFrameLayout

class CategoryCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.categoryCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CategoryCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: CategoryCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.CategoryCardView

    override val rootContainer: View
        get() = view.root

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_card_category, this, true)
        this.view = CategoryCardViewViewVariables(
            tvLabel = view.findViewById(R.id.tv_label),
            ivIcon = view.findViewById(R.id.iv_icon),
            root = view.findViewById(R.id.root_view),
            labelShimmer = view.findViewById(R.id.view_label_shimmer),
            iconShimmer = view.findViewById(R.id.view_icon_shimmer),
        )
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.CategoryCardView_title))
        getResourceId(R.styleable.CategoryCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.CategoryCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[view.ivIcon] = view.iconShimmer
        shimmeringPairs[view.tvLabel] = view.labelShimmer
    }

    fun setTitle(charSequence: CharSequence) {
        view.tvLabel.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvLabel.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        view.tvLabel.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        view.ivIcon.setImageResource(resId)
    }

    fun setIcon(drawable: Drawable) {
        view.ivIcon.setImageDrawable(drawable)
    }
}

data class CategoryCardViewViewVariables(
    val tvLabel: TextView,
    val ivIcon: ImageView,
    val root: ConstraintLayout,
    val labelShimmer: ShimmerFrameLayout,
    val iconShimmer: ShimmerFrameLayout,
)