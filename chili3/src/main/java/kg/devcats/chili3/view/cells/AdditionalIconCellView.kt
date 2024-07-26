package kg.devcats.chili3.view.cells

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.view.cells.BaseCellView

class AdditionalIconCellView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.additionalIconCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle_AdditionalIcon
) : BaseCellView(context, attrs, defStyleAttr, defStyleRes) {

    private var tagBackground : ConstraintLayout? = null

    private var tagText: TextView? = null

    private var tagIcon: ImageView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflatePercentageTag()
    }

    override fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        super.obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        context.obtainStyledAttributes(
            attrs,
            kg.devcats.chili3.R.styleable.AdditionalIconCellView,
            defStyleAttr,
            defStyleRes
        ).run {
            getString(kg.devcats.chili3.R.styleable.AdditionalIconCellView_tag_text)?.let {
                setTagText(it)
            }
            getResourceId(
                kg.devcats.chili3.R.styleable.AdditionalIconCellView_tag_icon,
                -1
            ).takeIf { it != -1 }?.let {
                setTagIcon(it)
            }
            getResourceId(
                kg.devcats.chili3.R.styleable.AdditionalIconCellView_tag_background,
                -1
            ).takeIf { it != -1 }?.let {
                setTagBackground(it)
            }
            recycle()
        }
    }

    private fun inflatePercentageTag() {
        val clPercentage = ConstraintLayout(context).apply {
            id = View.generateViewId()
            layoutParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                endToStart = R.id.iv_chevron
                bottomToBottom = R.id.iv_chevron
                topToTop = R.id.iv_chevron
            }
            minWidth = resources.getDimensionPixelSize(R.dimen.view_46dp)

            val tvPercentage = TextView(context).apply {
                id = View.generateViewId()
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    LayoutParams.WRAP_CONTENT
                ).apply {
                    startToStart = LayoutParams.PARENT_ID
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                }
                setTextColor(ContextCompat.getColor(context, R.color.white_1))
                setTextAppearance(R.style.Chili_H9_Primary_700)
                gravity = Gravity.CENTER
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.padding_8dp),
                    resources.getDimensionPixelSize(R.dimen.padding_6dp),
                    0,
                    resources.getDimensionPixelSize(R.dimen.padding_6dp)
                )
            }
            tagText = tvPercentage

            val icon = ImageView(context).apply {
                id = View.generateViewId()
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.padding_2dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp)
                )
                layoutParams = LayoutParams(
                    LayoutParams.WRAP_CONTENT,
                    0,
                ).apply {
                    startToEnd = tvPercentage.id
                    endToEnd = LayoutParams.PARENT_ID
                    topToTop = LayoutParams.PARENT_ID
                    bottomToBottom = LayoutParams.PARENT_ID
                }
            }
            tagIcon = icon

            addView(tvPercentage)
            addView(icon)
        }
        tagBackground = clPercentage
        vb.rootView.addView(clPercentage)
    }

    fun setTagText(text: String?) {
        tagText?.text = text
    }

    fun setTagText(@StringRes resId: Int) {
        tagText?.setText(resId)
    }

    fun setTagIcon(@DrawableRes icon: Int) {
        tagIcon?.setImageResource(icon)
    }

    fun setTagIcon(drawable: Drawable) {
        tagIcon?.setImageDrawable(drawable)
    }

    fun setTagBackground(@DrawableRes background : Int) {
        tagBackground?.setBackgroundResource(background)
    }

    fun setTagBackground(drawable: Drawable) {
        tagBackground?.background = drawable
    }
}