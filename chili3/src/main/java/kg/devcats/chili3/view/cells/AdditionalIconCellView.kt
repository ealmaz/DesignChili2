package kg.devcats.chili3.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
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

    private var bonusAmount: TextView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateBonusTag()
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
            getInteger(
                kg.devcats.chili3.R.styleable.AdditionalIconCellView_bonus_amount_percent,
                -1
            ).takeIf { it != -1 }?.let {
                setBonusAmount(it)
            }
            recycle()
        }
    }

    private fun inflateBonusTag() {
        val clBonus = ConstraintLayout(context).apply {
            id = View.generateViewId()
            minWidth = resources.getDimensionPixelSize(R.dimen.view_46dp)
            setBackgroundResource(kg.devcats.chili3.R.drawable.bonus_tag_bg)

            val tvBonus = TextView(context).apply {
                id = View.generateViewId()
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
            bonusAmount = tvBonus

            val ivBonus = ImageView(context).apply {
                id = View.generateViewId()
                setImageResource(kg.devcats.chili3.R.drawable.ic_bonus_new)
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                setPadding(
                    resources.getDimensionPixelSize(R.dimen.padding_2dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp),
                    resources.getDimensionPixelSize(R.dimen.padding_4dp)
                )
            }

            addView(tvBonus)
            addView(ivBonus)

            val tvBonusParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            ).apply {
                startToStart = LayoutParams.PARENT_ID
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
            }
            tvBonus.layoutParams = tvBonusParams

            val ivBonusParams = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                0,
            ).apply {
                startToEnd = tvBonus.id
                endToEnd = LayoutParams.PARENT_ID
                topToTop = LayoutParams.PARENT_ID
                bottomToBottom = LayoutParams.PARENT_ID
            }
            ivBonus.layoutParams = ivBonusParams
        }
        val clBonusParams = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        ).apply {
            endToStart = R.id.iv_chevron
            bottomToBottom = R.id.iv_chevron
            topToTop = R.id.iv_chevron
        }
        clBonus.layoutParams = clBonusParams
        vb.rootView.addView(clBonus)
    }

    fun setBonusAmount(amount : Int) {
        bonusAmount?.text = "$amount%"
    }
}