package com.design2.chili2.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewTitledInfoCellBinding
import com.design2.chili2.extensions.visible

class TitledInfoCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.infoCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_InfoCellView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    lateinit var vb: ChiliViewTitledInfoCellBinding

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewTitledInfoCellBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.InfoCellView, defStyleAttr, defStyleRes).run {
            getString(R.styleable.InfoCellView_title)?.let { setTitle(it) }
            getBoolean(R.styleable.InfoCellView_isDividerVisible, false).let { setDividerVisibility(it) }
            recycle()
        }
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setTitle(text: String) {
        vb.tvTitle.text = text
    }

    fun setSubtitle(subtitle: String) {
        vb.tvSubtitle.visible()
        vb.tvSubtitle.text = subtitle
    }

    fun setDividerVisibility(isVisible: Boolean) {
        vb.divider.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setIsInfoButtonVisible(isVisible: Boolean) {
        vb.ivInfo.isVisible = isVisible
    }

    fun setInfoButtonClickListener(onClick: () -> Unit) {
        vb.ivInfo.setOnClickListener {
            onClick.invoke()
        }
    }
}