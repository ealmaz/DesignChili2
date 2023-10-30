package com.design2.chili2.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewMultiIconedCellBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.ItemDecorator
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.cells.adapter.MultiIconedAdapter
import com.design2.chili2.view.cells.adapter.MultiIconedShimmerAdapter
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
    lateinit var shimmerAdapter: MultiIconedShimmerAdapter

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        vb = ChiliViewMultiIconedCellBinding.inflate(LayoutInflater.from(context), this, true)
        setupRecyclerView()
        setupShimmer()
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

    private fun setupShimmer(){
        shimmerAdapter = MultiIconedShimmerAdapter()
        vb.rvIconsShimmer.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        vb.rvIconsShimmer.addItemDecoration(
            ItemDecorator(
                context.resources.getDimension(R.dimen.padding_desc_4dp).toInt()
            )
        )
        vb.rvIconsShimmer.adapter = shimmerAdapter
        shimmerAdapter.addIcons(arrayListOf(R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,))
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.InfoCellView, defStyleAttr, defStyleRes).run {
            getString(R.styleable.InfoCellView_title)?.let { setTitle(it) }
            getBoolean(R.styleable.InfoCellView_isDividerVisible, false).let { setDividerVisibility(it) }
            getInteger(R.styleable.InfoCellView_roundedCornerMode, -1).takeIf { it != -1 }?.let {
                vb.rootView.setupRoundedCellCornersMode(it)
            }
            recycle()
        }
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

    fun setIcons(icons: ArrayList<String>) {
        adapter.addIcons(icons)
    }
    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = mutableShimmeringViewMap
}