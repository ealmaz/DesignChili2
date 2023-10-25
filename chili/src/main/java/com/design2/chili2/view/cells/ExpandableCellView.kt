package com.design2.chili2.view.cells

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewCellExpandableBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.RoundedCornerMode

class ExpandableCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_ExpandableCellView
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    lateinit var vb: ChiliViewCellExpandableBinding

    private var isCellViewExpanded = false

    init {
        setupView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }
    private fun setupView(context: Context) {
        vb = ChiliViewCellExpandableBinding.inflate(LayoutInflater.from(context), this, true)
        this.setOnClickListener {
            setIsCellExpanded(!isCellViewExpanded)
        }
        this.setupRoundedCellCornersMode(RoundedCornerMode.SINGLE.value)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ExpandableCellView, defStyleAttr, defStyleRes).run {
            setIsCellExpanded(getBoolean(R.styleable.ExpandableCellView_isExpanded, false))
            setTitle(getString(R.styleable.ExpandableCellView_title))
            setDescription(getString(R.styleable.ExpandableCellView_description))
            recycle()
        }
    }

    fun setTitle(text: String?) {
        vb.tvTitle.text = text
    }

    fun setTitle(@StringRes textResId: Int) {
        vb.tvTitle.setText(textResId)
    }

    fun setDescription(text: String?) {
        vb.tvDescription.text = text
    }

    fun setDescription(@StringRes textResId: Int) {
        vb.tvDescription.setText(textResId)
    }

    fun setIsCellExpanded(isExpanded: Boolean) {
        when (isExpanded) {
            true -> expandCellView()
            else -> reduceCellView()
        }

    }

    fun expandCellView() {
        isCellViewExpanded = true
        vb.tvDescription.animate()
            .translationY(0f).alpha(1.0f)
            .setDuration(100)
            .setListener( object : AnimatorListenerAdapter() {
                override fun onAnimationStart(animation : Animator) {
                    super.onAnimationStart(animation)
                    vb.tvDescription.visible()
                    vb.tvDescription.alpha = 0.0f
                }
            })

        vb.ivChevron.animate().rotation(0F)
        vb.divider.visible()
    }

    fun reduceCellView() {
        isCellViewExpanded = false
        vb.divider.gone()
        vb.tvDescription.animate()
            .translationY(-vb.tvDescription.height.toFloat()+50)
            .setDuration(100)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation : Animator) {
                    super.onAnimationEnd(animation)
                    vb.tvDescription.alpha = 0.0f
                    vb.tvDescription.gone()
                }
            })
        vb.ivChevron.animate().rotation(180F)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return bundleOf(
            SUPER_STATE to superState,
            EXPANDED_STATE to isCellViewExpanded
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState: Parcelable? = null
        (state as? Bundle)?.let {
            superState = state.getParcelable(SUPER_STATE)
            setIsCellExpanded(state.getBoolean(EXPANDED_STATE))
        }
        return super.onRestoreInstanceState(superState)
    }

    companion object {
        const val SUPER_STATE = "super_state"
        const val EXPANDED_STATE = "expanded_state"
    }
}