package com.design2.chili2.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewMultiIconedCellBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTopMargin
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.ItemDecorator
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.cells.adapter.MultiIconedAdapter
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout

class MultiIconedTitleCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.infoCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_InfoCellView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private val mutableShimmeringViewMap = mutableMapOf<View, ShimmerFrameLayout?>()

    private lateinit var vb: ChiliViewMultiIconedCellBinding
    lateinit var adapter: MultiIconedAdapter
    lateinit var shimmerAdapter: MultiIconedAdapter

    private var iconsPixelSize = 0

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewMultiIconedCellBinding.inflate(LayoutInflater.from(context), this, true)
        setupRecyclerView()
        setupShimmerPairs()
    }

    private fun setupShimmerPairs() {
        mutableShimmeringViewMap[vb.rvIcons] = vb.iconsShimmer
    }

    private fun setupRecyclerView() {
        adapter = MultiIconedAdapter()
        vb.rvIcons.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        vb.rvIcons.addItemDecoration(
            ItemDecorator(
                context.resources.getDimension(R.dimen.padding_desc_4dp).toInt()
            )
        )
        vb.rvIcons.adapter = adapter
    }

    private fun setupShimmer() {
        shimmerAdapter = MultiIconedAdapter()
        vb.rvIconsShimmer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        vb.rvIconsShimmer.addItemDecoration(
            ItemDecorator(
                context.resources.getDimension(R.dimen.padding_desc_4dp).toInt()
            )
        )
        vb.rvIconsShimmer.adapter = shimmerAdapter
        shimmerAdapter.addShimmerIcons(
            arrayListOf(
                R.drawable.chili_multi_icon,
                R.drawable.chili_multi_icon,
                R.drawable.chili_multi_icon,
                R.drawable.chili_multi_icon,
                R.drawable.chili_multi_icon,
                R.drawable.chili_multi_icon
            ), iconsPixelSize
        )
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.InfoCellView, defStyleAttr, defStyleRes).run {
            getString(R.styleable.InfoCellView_title)?.let { setTitle(it) }
            getBoolean(R.styleable.InfoCellView_isDividerVisible, false).let { setDividerVisibility(it) }
            getInteger(R.styleable.InfoCellView_roundedCornerMode, -1).takeIf { it != -1 }?.let {
                vb.rootView.setupRoundedCellCornersMode(it)
            }
            getDimensionPixelSize(R.styleable.InfoCellView_iconsSize, resources.getDimensionPixelSize(R.dimen.view_24dp)).let {
                iconsPixelSize = it
            }
            recycle()
        }
        setupShimmer()
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitle(text: String) {
        vb.tvTitle.text = text
    }

    fun setDividerVisibility(isVisible: Boolean) {
        vb.divider.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setDescription(description: String) {
        vb.tvDescription.visible()
        vb.tvDescription.text = description
    }

    fun setDescription(description: CharSequence) {
        vb.tvDescription.visible()
        vb.tvDescription.text = description
    }

    fun setRoundedMode(mode: RoundedCornerMode) {
        vb.rootView.setupRoundedCellCornersMode(mode.value)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        vb.rootView.setupRoundedCellCornersMode(mode.value)
    }

    fun setIsInfoButtonVisible(isVisible: Boolean) {
        vb.ivInfo.isVisible = isVisible
    }

    fun setInfoButtonClickListener(onClick: () -> Unit) {
        vb.ivInfo.setOnSingleClickListener {
            onClick.invoke()
        }
    }

    fun setIsActionButtonVisible(isVisible: Boolean) {
        vb.tvAction.isVisible = isVisible
    }

    fun setActionButtonClickListener(onClick: () -> Unit) {
        vb.tvAction.setOnSingleClickListener {
            onClick.invoke()
        }
    }

    fun setActionBtnText(text: String) {
        vb.tvAction.text = text
    }

    fun setActionBtnText(@StringRes textRes: Int) {
        vb.tvAction.setText(textRes)
    }

    fun setIconsTopMargin(@DimenRes marginRes: Int) {
        val margin = resources.getDimensionPixelSize(marginRes)
        vb.rvIcons.setTopMargin(margin)
        vb.rvIconsShimmer.setTopMargin(margin)
    }

    fun setOnItemClicked(clickListener: () -> Unit) {
        adapter.listener = clickListener
    }

    fun setIcons(icons: ArrayList<String>) {
        adapter.addIcons(icons, iconsPixelSize)
    }
    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = mutableShimmeringViewMap
}