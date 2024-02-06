package com.design2.chili2.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.BOTTOM
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import com.design2.chili2.R
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setupConstraint
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView

class ActionCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.actionCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_Action
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var tvAction: TextView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateAction()
    }

    override fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(attrs, R.styleable.ActionCellView, defStyleAttr, defStyleRes)
            .run {
                getString(R.styleable.ActionCellView_actionText)?.let {
                    setActionText(it)
                }
                getBoolean(R.styleable.ActionCellView_isActionVisible, true).let {
                    setIsActionVisible(it)
                }
                getBoolean(R.styleable.ActionCellView_isActionEnabled, true).let {
                    setIsActionEnabled(it)
                }
                recycle()
            }
    }

    private fun inflateAction() = with(vb) {
        val shimmerLayout = context.createShimmerLayout {
            setPadding(resources.getDimensionPixelSize(R.dimen.padding_8dp), 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
        }

        shimmerLayout.addView(context.createShimmerView(R.dimen.view_46dp))
        rootView.addView(shimmerLayout)

        rootView.setupConstraint {
            connect(shimmerLayout.id, BOTTOM, PARENT_ID, BOTTOM, 0)
            connect(shimmerLayout.id, END, R.id.iv_chevron, START, 0)
            connect(shimmerLayout.id, TOP, PARENT_ID, TOP, 0)
        }

        tvAction = TextView(context, null, R.attr.componentButtonDefaultStyle, R.style.Chili_ButtonStyle_Component).apply {
            setPadding(resources.getDimensionPixelSize(R.dimen.padding_8dp), 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
            shimmeringPairs[this] = shimmerLayout
        }
        flEndPlaceHolder.addView(tvAction)
    }

    fun setActionText(text: String?) {
        tvAction?.text = text
    }

    fun setActionText(@StringRes textResId: Int) {
        tvAction?.setText(textResId)
    }

    override fun setIsChevronVisible(isVisible: Boolean) {
        super.setIsChevronVisible(isVisible)
        if (isVisible) tvAction?.setPadding(0, 0,0, 0)
        else tvAction?.setPadding(resources.getDimensionPixelSize(R.dimen.padding_8dp), 0, resources.getDimensionPixelSize(R.dimen.padding_8dp), 0)
    }

    fun setActionClickListener(action: () -> Unit) {
        tvAction?.setOnSingleClickListener(action)
    }

    fun setIsActionVisible(isVisible: Boolean) {
        tvAction?.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setIsActionEnabled(isEnabled: Boolean) {
        tvAction?.isEnabled = isEnabled
    }
}