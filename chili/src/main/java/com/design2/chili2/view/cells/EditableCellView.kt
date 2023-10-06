package com.design2.chili2.view.cells

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.util.IconSize


class EditableCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.endIconCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_EndIcon
) : EndIconCellView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var views: EditableCellViewVariables
    private var chevronIsVisible: Boolean? = null

    var isEditMode: Boolean = false
        set(value) {
            views.apply {
                optionImg.isVisible = value
                dragImg.isVisible = value
                verticalDivider.isVisible = value
                endIconFrame.isVisible = !value
                if (chevronIsVisible!!) chevron.isVisible = !value
            }
            field = value
        }

    private fun inflateEditToolsView() {
        val dragImg = ImageView(context)
        val optionImg = ImageView(context)
        val divider = View(context)
        view.editToolsPlaceholder.addView(optionImg)
        view.editToolsPlaceholder.addView(divider)
        view.editToolsPlaceholder.addView(dragImg)
        dragImg.setImgDefaultParam()
        optionImg.setImgDefaultParam()
        divider.setDividerDefaultParam()
        views = EditableCellViewVariables(
            verticalDivider = divider,
            dragImg = dragImg,
            optionImg = optionImg,
            chevron = view.chevron,
            endIconFrame = view.flEndPlaceholder
        )

    }

    private fun ImageView.setImgDefaultParam() {
        val dp32 = (32 * resources.displayMetrics.density).toInt()
        val layoutParams = this.layoutParams as LinearLayout.LayoutParams
        layoutParams.width = dp32
        layoutParams.height = dp32
        this.layoutParams = layoutParams

        val outValue = TypedValue()
        context.theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        this.setBackgroundResource(outValue.resourceId)

        this.isFocusable = true

        this.isClickable = true

        val margin8dp = (8 * resources.displayMetrics.density).toInt()
        val marginLayoutParams = this.layoutParams as MarginLayoutParams
        marginLayoutParams.marginStart = margin8dp
        marginLayoutParams.marginEnd = margin8dp
        this.layoutParams = marginLayoutParams

        this.visibility = View.GONE
    }

    @SuppressLint("ResourceType")
    fun View.setDividerDefaultParam() {
        val outValue = TypedValue()
        context.theme.resolveAttribute(R.attr.ChiliDividerColor, outValue, true)
        this.setBackgroundResource(outValue.resourceId)

        val layoutParams = LinearLayout.LayoutParams(
            (0.8 * resources.displayMetrics.density).toInt(),
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        this.layoutParams = layoutParams

        visibility = View.GONE
    }

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateEditToolsView()
        saveChevronState()
    }


    private fun saveChevronState() {
        if (chevronIsVisible == null) {
            chevronIsVisible = views.chevron.isVisible
        }
    }

    override fun obtainAttributes(
        context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int
    ) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(
            attrs, R.styleable.EditableCellView, defStyleAttr, defStyleRes
        ).run {
            getResourceId(R.styleable.EditableCellView_dragIcon, -1).let {
                if (it > -1) setDragIcon(it)
            }
            getResourceId(R.styleable.EditableCellView_optionIcon, -1).let {
                if (it > -1) setOptionIcon(it)
            }
            getBoolean(R.styleable.EditableCellView_isDragIconVisible, false).let {
                setDragIconVisibility(it)
            }
            getBoolean(R.styleable.EditableCellView_isOptionIconVisible, false).let {
                setOptionIconVisibility(it)
            }
            getLayoutDimension(
                R.styleable.EditableCellView_editableIconsSize, IconSize.SMALL.value
            ).let {
                when (it) {
                    IconSize.SMALL.value -> setEndIconSize(IconSize.SMALL)
                    IconSize.MEDIUM.value -> setEndIconSize(IconSize.MEDIUM)
                    IconSize.LARGE.value -> setEndIconSize(IconSize.LARGE)
                    else -> setupEditableIconSize(it, it)
                }
            }
            recycle()
        }
    }

    fun setOnOptionClickListener(block: () -> Unit) {
        views.optionImg.setOnSingleClickListener(block)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setItemDragListener(started: () -> Unit, end: (() -> Unit)? = null) {
        views.dragImg.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> started.invoke()
                MotionEvent.ACTION_UP -> end?.invoke()
            }
            false
        }
    }

    fun setupEditableIconSize(widthPx: Int, heightPx: Int) {
        val dragParams = views.dragImg.layoutParams
        val optionParams = views.optionImg.layoutParams
        dragParams?.height = heightPx
        dragParams?.width = widthPx
        optionParams?.height = heightPx
        optionParams?.width = widthPx
        views.dragImg.layoutParams = dragParams
        views.optionImg.layoutParams = optionParams
    }

    fun setDragIconVisibility(isVisible: Boolean) {
        views.dragImg.isVisible = isVisible
    }

    fun setVerticalDividerVisibility(isVisible: Boolean) {
        views.verticalDivider.isVisible = isVisible
    }

    fun setOptionIconVisibility(isVisible: Boolean) {
        views.optionImg.isVisible = isVisible
    }

    fun setDragIcon(@DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            views.dragImg.setImageDrawable(context.drawable(drawableRes))
        }
    }

    fun setOptionIcon(@DrawableRes drawableRes: Int?) {
        drawableRes?.let {
            views.optionImg.setImageDrawable(context.drawable(drawableRes))
        }
    }

}

class EditableCellViewVariables(
    val verticalDivider: View,
    val dragImg: ImageView,
    val optionImg: ImageView,
    val chevron: View,
    val endIconFrame: View,
)