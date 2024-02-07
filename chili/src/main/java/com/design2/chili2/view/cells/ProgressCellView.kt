package com.design2.chili2.view.cells

import android.content.Context
import android.text.Spanned
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellProgressBinding
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

class ProgressCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.progressCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_ProgressCellView
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliViewCellProgressBinding

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewCellProgressBinding.inflate(LayoutInflater.from(context), this, true)
        setupShimmeringViews()
    }

    private fun setupShimmeringViews() {
        shimmeringPairs[vb.llContent] = vb.viewShimmerContent
    }


    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ProgressCellView, defStyleAttr, defStyleRes).run {
            getBoolean(R.styleable.ProgressCellView_animateProgress, false).let {
                vb.aplProgress.setIsProgressAnimated(it)
            }
            getColor(R.styleable.ProgressCellView_progressColor, -1).takeIf { it != -1 }?.let {
                setProgressColor(it)
            }
            getColor(R.styleable.ProgressCellView_progressBackgroundColor, -1).takeIf { it != -1 }?.let {
                vb.aplProgress.setProgressBackgroundColor(it)
            }
            getInteger(R.styleable.ProgressCellView_progressPercent, 50).let {
                setProgressPercent(it)
            }
            getString(R.styleable.ProgressCellView_title)?.let {
                setTitle(it)
            }
            getString(R.styleable.ProgressCellView_description)?.let {
                setDescriptionText(it)
            }
            getBoolean(R.styleable.ProgressCellView_isBackgroundTransparent, false).let {
                if (it) removeBackground() else setupRoundedBackground()
            }
            recycle()
        }
    }

    fun setupRoundedBackground() {
        this.setupRoundedCellCornersMode(RoundedCornerMode.SINGLE.value)
    }

    fun removeBackground() {
        this.background = null
    }

    fun setProgressPercent(progress: Int) {
        vb.aplProgress.setProgress(progress)
    }

    fun setTitle(text: String) {
        vb.tvTitle.text = text
    }

    fun setTitle(@StringRes textResId: Int) {
        vb.tvTitle.setText(textResId)
    }

    fun setTitle(spanned: Spanned) {
        vb.tvTitle.text = spanned
    }

    fun setDescriptionText(description: String?) {
        vb.tvDescription.setTextOrHide(description)
    }

    fun setDescriptionText(@StringRes descriptionResId: Int?) {
        vb.tvDescription.setTextOrHide(descriptionResId)
    }

    fun setDescriptionText(spanned: Spanned?) {
        vb.tvDescription.setTextOrHide(spanned)
    }

    fun setProgressColor(@ColorInt colorInt: Int) {
        vb.aplProgress.setProgressColor(colorInt)
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs
}