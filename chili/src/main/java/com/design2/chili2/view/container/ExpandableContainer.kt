package com.design2.chili2.view.container

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.DimenRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewContainerExpandableBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible
import com.design2.chili2.storage.ChiliComponentsPreferences
import com.design2.chili2.util.IconSize

class ExpandableContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableContainerViewStyle,
    defStyleRes: Int = R.style.Chili_Container_ExpandableContainerViewStyle
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewContainerExpandableBinding

    private val componentPref: ChiliComponentsPreferences by lazy {
        ChiliComponentsPreferences.getInstance(context)
    }

    var isExpanded: Boolean = false
    var onClosureAction: ((isExpanded: Boolean) -> Unit)? = null
    private var isNeedToSaveState: Boolean = false

    init {
        inflateView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView() {
        vb = ChiliViewContainerExpandableBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ExpandableContainer, defStyleAttr, defStyleRes).run {
            setTitle(getText(R.styleable.ExpandableContainer_title))
            setTitleTextAppearance(getResourceId(R.styleable.ExpandableContainer_titleTextAppearance, -1).takeIf { it != -1 })
            setSubtitle(getText(R.styleable.ExpandableContainer_subtitle))
            setSubtitleTextAppearance(getResourceId(R.styleable.ExpandableContainer_subtitleTextAppearance, -1).takeIf { it != -1 })
            setEndIcon(getDrawable(R.styleable.ExpandableContainer_endIcon))
            getLayoutDimension(R.styleable.ExpandableContainer_endIconSize, IconSize.SMALL.value).let {
                when (it) {
                    IconSize.SMALL.value -> setEndIconSize(IconSize.SMALL)
                    IconSize.MEDIUM.value -> setEndIconSize(IconSize.MEDIUM)
                    IconSize.LARGE.value -> setEndIconSize(IconSize.LARGE)
                    else -> setupEndIconSize(it,it)
                }
            }
            setIsEndIconClickable(getBoolean(R.styleable.ExpandableContainer_isEndIconClickable, false))
            setActionText(getText(R.styleable.ExpandableContainer_actionText))
            setActionTextAppearance(getResourceId(R.styleable.ExpandableContainer_actionTextTextAppearance, -1).takeIf { it != -1 })
            setIsActionTextEnabled(getBoolean(R.styleable.ExpandableContainer_actionTextEnabled, true))
            setAdditionalText(getText(R.styleable.ExpandableContainer_additionalText))
            setAdditionalTextTextAppearance(getResourceId(R.styleable.ExpandableContainer_additionalTextTextAppearance, -1).takeIf { it != -1 })
            setClosureIndicatorVisibility(getBoolean(R.styleable.ExpandableContainer_closureIndicatorVisibility, true))
            setIsNeedToSaveExpandedState(getBoolean(R.styleable.ExpandableContainer_isNeedToSaveExpandedState, false))
            setIsExpandedOrRestoreStateFromPreferences(getBoolean(R.styleable.ExpandableContainer_isExpanded, true), false)
            recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        if (isNeedToSaveState) componentPref.saveIsExpandableContainerExpanded(id.toString(), isExpanded)
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(SUPER_STATE, superState)
            putBoolean(EXPANDED_STATE, isExpanded)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle).let { super.onRestoreInstanceState(it?.getParcelable(SUPER_STATE)) }
    }

    private fun setIsExpandedOrRestoreStateFromPreferences(isExpanded: Boolean, isAnimated: Boolean) {
        if (!isNeedToSaveState) setIsExpanded(isExpanded, isAnimated)
        else setIsExpanded(componentPref.getIsExpandableContainerExpanded(id.toString()), isAnimated)
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int?) {
        if (resId == null) return
        vb.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) = with(vb.tvSubtitle) {
        setText(charSequence)
        isVisible = (charSequence != null && isExpanded)
    }

    fun setSubtitle(resId: Int?) = with(vb.tvSubtitle) {
        resId?.let { setText(it) }
        isVisible = (resId != null && isExpanded)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setActionText(charSequence: CharSequence?) {
        vb.tvAction.setTextOrHide(charSequence)
    }

    fun setActionText(resId: Int?) {
        vb.tvAction.setTextOrHide(resId)
    }

    fun setActionClick(action: () -> Unit) {
        vb.tvAction.setOnSingleClickListener(action)
    }

    fun setActionTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        vb.tvAction.setTextAppearance(resId)
    }

    fun setIsActionTextEnabled(isEnabled: Boolean) {
        vb.tvAction.isEnabled = isEnabled
    }

    fun setAdditionalText(charSequence: CharSequence?) {
        vb.tvAdditionalText.setTextOrHide(charSequence)
    }

    fun setAdditionalText(resId: Int?) {
        vb.tvAdditionalText.setTextOrHide(resId)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        vb.tvAdditionalText.setTextAppearance(resId)
    }

    fun setIsActionVisible(isVisible: Boolean) {
        vb.tvAction.isVisible = isVisible
    }


    fun setEndIcon(resId: Int) {
        vb.ivEndIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    fun setEndIcon(drawable: Drawable?) {
        vb.ivEndIcon.apply {
            setImageDrawable(drawable)
            if (drawable != null) visible() else gone()
        }
    }

    fun setIsEndIconClickable(isClickableValue: Boolean) = with(vb.ivEndIcon) {
        isFocusable = isClickableValue
        isClickable = isClickableValue
    }

    fun setEndIconClickListener(action: () -> Unit) {
        vb.ivEndIcon.setOnSingleClickListener(action)
    }

    fun setEndIconSize(iconSize: IconSize) {
        val size = when(iconSize) {
            IconSize.LARGE -> R.dimen.view_48dp
            IconSize.MEDIUM -> R.dimen.view_46dp
            IconSize.SMALL -> R.dimen.view_32dp
        }
        setEndIconSize(size, size)
    }

    fun setEndIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupEndIconSize(widthPx, heightPx)
    }

    private fun setupEndIconSize(widthPx: Int, heightPx: Int) {
        val params = vb.ivEndIcon.layoutParams
        params?.height = heightPx
        params?.width = widthPx
        vb.ivEndIcon.layoutParams = params
    }

    fun setClosureIndicatorVisibility(isVisible: Boolean) {
        vb.ivClosureIndicator.isVisible = isVisible
        if (isVisible) setupClosureButton()
    }

    private fun setupClosureButton() {
        vb.ivClosureIndicator.setOnClickListener {
            setIsExpanded(!isExpanded)
            onClosureAction?.invoke(isExpanded)
        }
        vb.tvTitle.setOnClickListener {
            setIsExpanded(!isExpanded)
            onClosureAction?.invoke(isExpanded)
        }
    }

    fun setIsNeedToSaveExpandedState(isNeed: Boolean) {
        this.isNeedToSaveState = isNeed
    }

    fun setIsExpanded(isExpanded: Boolean, isAnimated: Boolean = true) {
        this.isExpanded = isExpanded
        if (this.isExpanded) {
            rotateChevron(0f, isAnimated)
            vb.tvSubtitle.isVisible = !vb.tvSubtitle.text.isNullOrBlank()
        } else {
            rotateChevron(-90f, isAnimated)
            vb.tvSubtitle.gone()
        }
        vb.tvSubtitle.post {
            animateExpanding(isAnimated, isExpanded)
            childrenViewsVisibilityAfterAnimation(isExpanded)
        }
    }

    private fun animateExpanding(isAnimated: Boolean, isExpanded: Boolean) {
        val newHeight = if (isExpanded) calculateExpandedHeight() else calculateCollapsedHeight()
        if (isAnimated) {
            val animator = ValueAnimator.ofInt(height, newHeight).apply {
                addUpdateListener { valueAnimator ->
                    val height = valueAnimator.animatedValue as Int
                    layoutParams.height = height
                    requestLayout()
                }
                duration = 300
                addListener(
                    object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator) {}
                        override fun onAnimationEnd(animation: Animator) {
                            layoutParams.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
                            requestLayout()
                            updateRecyclerViewHeight()
                        }
                        override fun onAnimationCancel(animation: Animator) {}
                        override fun onAnimationRepeat(animation: Animator) {}
                    }
                )
            }
            animator.start()
        } else {
            layoutParams?.height = ConstraintLayout.LayoutParams.WRAP_CONTENT
            requestLayout()
            updateRecyclerViewHeight()
        }
    }

    private fun updateRecyclerViewHeight() {
        children.find { it is RecyclerView }.let {
            (it as? RecyclerView)?.adapter?.notifyDataSetChanged()
        }
    }

    private fun childrenViewsVisibilityAfterAnimation(isExpanded: Boolean){
        children.filter { it != vb.rootView }.forEach {
            it.isVisible = isExpanded
        }
    }

    private fun calculateExpandedHeight(): Int {
        return height + children.filter { it != vb.rootView }.sumOf { it.height }
    }

    private fun calculateCollapsedHeight(): Int {
        val firstChildHeight = getChildAt(0)?.height ?: 0
        return firstChildHeight + paddingTop + paddingBottom
    }

    private fun rotateChevron(rotation: Float = 0f, isAnimated: Boolean = true) {
        when (isAnimated) {
            true -> vb.ivClosureIndicator.animate().rotation(rotation)
            else -> vb.ivClosureIndicator.rotation = rotation
        }
    }

    companion object {
        const val SUPER_STATE = "super_state"
        const val EXPANDED_STATE = "expanded_state"
    }
}
