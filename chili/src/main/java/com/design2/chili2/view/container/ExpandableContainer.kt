package com.design2.chili2.view.container

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.StyleRes
import androidx.core.view.children
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.IconSize

class ExpandableContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableContainerViewStyle,
    defStyleRes: Int = R.style.Chili_Container_ExpandableContainerViewStyle
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var view: ExpandableContainerViewVariables

    var isExpanded: Boolean = false

    init {
        inflateView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView() {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_container_expandable, this)
        this.view = ExpandableContainerViewVariables(
            root = view.findViewById(R.id.root_view),
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            tvAction = view.findViewById(R.id.tv_action),
            ivEndIcon = view.findViewById(R.id.iv_end_icon),
            ivClosureIndicator = view.findViewById(R.id.iv_closure_indicator),
            tvAdditionalText = view.findViewById(R.id.tv_additional_text)
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
            setIsExpanded(getBoolean(R.styleable.ExpandableContainer_isExpanded, true), false)
            recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(SUPER_STATE, superState)
            putBoolean(EXPANDED_STATE, isExpanded)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle).let {
            setIsExpanded(
                isExpanded = it?.getBoolean(EXPANDED_STATE, true) ?: true,
                isAnimated = false
            )
            super.onRestoreInstanceState(it?.getParcelable(SUPER_STATE))
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        view.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int?) {
        if (resId == null) return
        view.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        view.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        view.tvSubtitle.setTextOrHide(charSequence)
    }

    fun setSubtitle(resId: Int?) {
        view.tvSubtitle.setTextOrHide(resId)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        view.tvSubtitle.setTextAppearance(resId)
    }

    fun setActionText(charSequence: CharSequence?) {
        view.tvAction.setTextOrHide(charSequence)
    }

    fun setActionText(resId: Int?) {
        view.tvAction.setTextOrHide(resId)
    }

    fun setActionTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        view.tvAction.setTextAppearance(resId)
    }

    fun setIsActionTextEnabled(isEnabled: Boolean) {
        view.tvAction.isEnabled = isEnabled
    }

    fun setAdditionalText(charSequence: CharSequence?) {
        view.tvAdditionalText.setTextOrHide(charSequence)
    }

    fun setAdditionalText(resId: Int?) {
        view.tvAdditionalText.setTextOrHide(resId)
    }

    fun setAdditionalTextTextAppearance(@StyleRes resId: Int?) {
        if (resId == null) return
        view.tvAdditionalText.setTextAppearance(resId)
    }


    fun setEndIcon(resId: Int) {
        view.ivEndIcon.apply {
            setImageResource(resId)
            visible()
        }
    }

    fun setEndIcon(drawable: Drawable?) {
        view.ivEndIcon.apply {
            setImageDrawable(drawable)
            if (drawable != null) visible() else gone()
        }
    }


    fun setIsEndIconClickable(isClickableValue: Boolean) = with(view.ivEndIcon) {
        isFocusable = isClickableValue
        isClickable = isClickableValue
    }

    fun setEndIconClickListener(action: () -> Unit) {
        view.ivEndIcon.setOnSingleClickListener(action)
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
        val params = view.ivEndIcon.layoutParams
        params?.height = heightPx
        params?.width = widthPx
        view.ivEndIcon.layoutParams = params
    }

    fun setClosureIndicatorVisibility(isVisible: Boolean) {
        view.ivClosureIndicator.isVisible = isVisible
        if (isVisible) setupClosureButton()
    }

    private fun setupClosureButton() {
        view.ivClosureIndicator.setOnClickListener { setIsExpanded(!isExpanded) }
        view.tvTitle.setOnClickListener { setIsExpanded(!isExpanded) }
    }

    fun setIsExpanded(isExpanded: Boolean, isAnimated: Boolean = true) {
        this.isExpanded = isExpanded
        if (this.isExpanded) {
            rotateChevron(0f, isAnimated)
            children.forEach {
                if (it != view.root) it.visible()
            }
            if (!(view.tvSubtitle.text.isNullOrBlank())) view.tvSubtitle.visible()
        } else {
            rotateChevron(-90f, isAnimated)
            children.forEach {
                if (it != view.root) it.gone()
            }
            view.tvSubtitle.gone()
        }
    }

    private fun rotateChevron(rotation: Float = 0f, isAnimated: Boolean = true) {
        when (isAnimated) {
            true -> view.ivClosureIndicator.animate().rotation(rotation)
            else -> view.ivClosureIndicator.rotation = rotation
        }
    }

    companion object {
        const val SUPER_STATE = "super_state"
        const val EXPANDED_STATE = "expanded_state"
    }
}

data class ExpandableContainerViewVariables(
    val root: View,
    val tvTitle: TextView,
    val tvSubtitle: TextView,
    val tvAction: TextView,
    val ivEndIcon: ImageView,
    val ivClosureIndicator: ImageView,
    val tvAdditionalText: TextView,
)