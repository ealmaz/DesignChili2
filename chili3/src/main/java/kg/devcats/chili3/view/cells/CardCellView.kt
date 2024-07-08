package com.design2.chili2.view.cells

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import com.design2.chili2.R
import kg.devcats.chili3.databinding.ChiliViewCardCellBinding

class CardCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_BaseCellViewStyle
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val vb: ChiliViewCardCellBinding =
        ChiliViewCardCellBinding.inflate(LayoutInflater.from(context), this, true)

    init {
        setupAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    @SuppressLint("CustomViewStyleable")
    private fun setupAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        val a = context.obtainStyledAttributes(attrs, kg.devcats.chili3.R.styleable.CardCellView, defStyleAttr, defStyleRes)
        val title = a.getString(kg.devcats.chili3.R.styleable.CardCellView_title)
        val subtitle = a.getString(kg.devcats.chili3.R.styleable.CardCellView_subtitle)
        val value = a.getString(kg.devcats.chili3.R.styleable.CardCellView_value)
        val icon = a.getDrawable(kg.devcats.chili3.R.styleable.CardCellView_icon ?: kg.devcats.chili3.R.drawable.chili_ic_add_card_cell)
        val isMain = a.getBoolean(kg.devcats.chili3.R.styleable.CardCellView_isMain, false)
        val isBlocked = a.getBoolean(kg.devcats.chili3.R.styleable.CardCellView_isBlocked, false)
        val isUniqueStatused = a.getBoolean(kg.devcats.chili3.R.styleable.CardCellView_isUniqueStatused, false)
        val isChevronVisible = a.getBoolean(kg.devcats.chili3.R.styleable.CardCellView_isChevronVisible, false)
        a.recycle()

        setTitle(title)
        setSubtitle(subtitle)
        setValue(value)
        setIcon(icon)
        setMain(isMain)
        setBlocked(isBlocked)
        setUniqueStatused(isUniqueStatused)
        setChevronVisible(isChevronVisible)
    }

    fun setTitle(title: String?) {
        vb.tvTitle.text = title
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setSubtitle(subtitle: String?) {
        vb.tvSubtitle.text = subtitle
        vb.tvSubtitle.visibility = if (subtitle.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
        vb.tvSubtitle.visibility = View.VISIBLE
    }

    fun setValue(value: String?) {
        vb.tvValue.text = value
        vb.tvValue.visibility = if (value.isNullOrEmpty()) View.GONE else View.VISIBLE
    }

    fun setValue(@StringRes resId: Int) {
        vb.tvValue.setText(resId)
    }

    fun setIcon(icon: Drawable?) {
        vb.ivIcon.setImageDrawable(icon ?: AppCompatResources.getDrawable(context, kg.devcats.chili3.R.drawable.chili_ic_add_card_cell))
    }

    fun setIcon(@DrawableRes resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setMain(isMain: Boolean) {
        vb.ivStar.visibility = if (isMain) View.VISIBLE else View.GONE
    }

    fun setBlocked(isBlocked: Boolean) {
        with(vb) {
            val alphaValue = if (isBlocked) 0.5f else 1f

            ivIcon.alpha = alphaValue
            tvTitle.alpha = alphaValue
            tvSubtitle.alpha = alphaValue
            tvValue.alpha = alphaValue
            ivStar.alpha = alphaValue
            ivChevron.alpha = alphaValue

            ivLock.visibility = if (isBlocked) View.VISIBLE else View.GONE
        }
    }

    fun setUniqueStatused(isUniqueStatused: Boolean) {
        // Update UI for unique status
        with(vb) {
            if (isUniqueStatused) {
                tvSubtitle.setTextColor(resources.getColor(R.color.folly_1))
                tvValue.setTextColor(resources.getColor(R.color.folly_1))
            }
        }
    }

    fun setChevronVisible(isChevronVisible: Boolean) {
        vb.ivChevron.visibility = if (isChevronVisible) View.VISIBLE else View.GONE
    }
}
