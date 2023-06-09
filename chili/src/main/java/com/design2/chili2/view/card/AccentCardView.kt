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
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout

class AccentCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.accentCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_AccentCardViewStyle
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: AccentCardViewViewVariables

    override val styleableAttrRes: IntArray = R.styleable.AccentCardView

    override val rootContainer: View
        get() = view.root

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_card_accent, this, true)
        this.view = AccentCardViewViewVariables(
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            ivStartIcon = view.findViewById(R.id.iv_start_icon),
            ivEndIcon = view.findViewById(R.id.iv_end_icon),
            root = view.findViewById(R.id.root_view),
            titleShimmer = view.findViewById(R.id.view_title_shimmer),
            subtitleShimmer = view.findViewById(R.id.view_subtitle_shimmer),
            startIconShimmer = view.findViewById(R.id.view_start_icon_shimmer),
            endIconShimmer = view.findViewById(R.id.view_end_icon_shimmer),
        )
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.AccentCardView_title))
        setSubtitle(getText(R.styleable.AccentCardView_subtitle))

        getResourceId(R.styleable.AccentCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }

        getResourceId(R.styleable.AccentCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }

        getResourceId(R.styleable.AccentCardView_titleStartIcon, -1)
            .takeIf { it != -1 }?.let { setTitleStartIcon(it) }

        getResourceId(R.styleable.AccentCardView_endIcon, -1)
            .takeIf { it != -1 }?.let { setEndIcon(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[view.tvTitle] = view.titleShimmer
    }

    fun setTitle(charSequence: CharSequence?) {
        view.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        view.tvTitle.setTextAppearance(resId)
    }


    fun setSubtitle(charSequence: CharSequence?) {
        view.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) shimmeringPairs.remove(view.tvSubtitle)
        else shimmeringPairs[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setSubtitle(resId: Int) {
        view.tvSubtitle.setTextOrHide(resId)
        shimmeringPairs[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setSubtitleTextAppearance(resId: Int) {
        view.tvSubtitle.setTextAppearance(resId)
    }

    fun setTitleStartIcon(resId: Int?) = with(view.ivStartIcon) {
        if (resId == null) {
            gone()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageResource(resId)
            shimmeringPairs[this] = view.startIconShimmer
        }
    }

    fun setTitleStartIcon(drawable: Drawable?) = with(view.ivStartIcon) {
        if (drawable == null) {
            gone()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = view.startIconShimmer
        }
    }

    fun setEndIcon(resId: Int?) = with(view.ivEndIcon) {
        if (resId == null) {
            invisible()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageResource(resId)
            shimmeringPairs[this] = view.endIconShimmer
        }
    }

    fun setEndIcon(drawable: Drawable?) = with(view.ivEndIcon) {
        if (drawable == null) {
            invisible()
            shimmeringPairs.remove(this)
        }
        else {
            visible()
            setImageDrawable(drawable)
            shimmeringPairs[this] = view.endIconShimmer
        }
    }
}

data class AccentCardViewViewVariables(
    val tvTitle: TextView,
    val tvSubtitle: TextView,
    val ivStartIcon: ImageView,
    val ivEndIcon: ImageView,
    val root: ConstraintLayout,
    val titleShimmer: ShimmerFrameLayout,
    val subtitleShimmer: ShimmerFrameLayout,
    val startIconShimmer: ShimmerFrameLayout,
    val endIconShimmer: ShimmerFrameLayout,
)