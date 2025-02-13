package kg.devcats.chili3.view.container

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.chili2.storage.ChiliComponentsPreferences
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.R
import kg.devcats.chili3.adapter.GridSpacesDecoration
import kg.devcats.chili3.adapter.IconTitledAdapter
import kg.devcats.chili3.databinding.ChiliExpandableGridRecyclerViewBinding
import kg.devcats.chili3.model.ExpandableGridItem

class ExpandableGridRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.design2.chili2.R.attr.expandableContainerViewStyle,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_Container_ExpandableContainerViewStyle
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliExpandableGridRecyclerViewBinding

    private val componentPref: ChiliComponentsPreferences by lazy {
        ChiliComponentsPreferences.getInstance(context)
    }

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    private var isExpanded: Boolean = false
    private var isShimmering: Boolean = false
    private var isNeedToSaveState: Boolean = false
    var visibleItemCount: Int = 0
    var onClosureAction: ((isExpanded: Boolean) -> Unit)? = null

    private var items: List<ExpandableGridItem> = listOf()
    private var shimmeringItems = List(8) {
        ExpandableGridItem(it.toLong(), isShimmering = true)
    }

    private var listener: Listener? = null
    private val gridAdapter by lazy {
        IconTitledAdapter {
            listener?.onItemClick(it.deeplink)
            listener?.onExpandableGridItemClick(it)
        }
    }

    init {
        inflateView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView() {
        vb = ChiliExpandableGridRecyclerViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) {
        context.obtainStyledAttributes(
            attrs,
            R.styleable.ExpandableGridRecyclerView,
            defStyleAttr,
            defStyleRes
        ).run {
            setIsNeedToSaveExpandedState(
                getBoolean(
                    R.styleable.ExpandableGridRecyclerView_isNeedToSaveExpandedState,
                    false
                )
            )
            setIsExpandedOrRestoreStateFromPreferences(
                getBoolean(
                    R.styleable.ExpandableGridRecyclerView_isExpanded,
                    true
                )
            )
            visibleItemCount =
                getInteger(R.styleable.ExpandableGridRecyclerView_visibleItemCount, 0)
            setupClosureButton(
                getBoolean(R.styleable.ExpandableGridRecyclerView_isClosureButtonVisible, true),
                getDrawable(R.styleable.ExpandableGridRecyclerView_bottomIcon)
            )
            setupRecyclerView()
            recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        if (isNeedToSaveState) componentPref.saveIsExpandableContainerExpanded(
            id.toString(),
            isExpanded
        )
        val superState = super.onSaveInstanceState()
        return Bundle().apply {
            putParcelable(SUPER_STATE, superState)
            putBoolean(EXPANDED_STATE, isExpanded)
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        (state as? Bundle).let { super.onRestoreInstanceState(it?.getParcelable(SUPER_STATE)) }
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs

    override fun onStartShimmer() {
        isShimmering = true
        submitList(isExpanded)
    }

    override fun onStopShimmer() {
        isShimmering = false
        submitList(isExpanded)
    }

    private fun setupRecyclerView() = with(vb.recyclerView) {
        adapter = gridAdapter
        layoutManager =
            GridLayoutManager(context, visibleItemCount, LinearLayoutManager.VERTICAL, false)
        addItemDecoration(GridSpacesDecoration(8, 8, 12))
    }

    private fun setIsExpandedOrRestoreStateFromPreferences(isExpanded: Boolean) {
        if (!isNeedToSaveState) setIsExpanded(isExpanded)
        else setIsExpanded(componentPref.getIsExpandableContainerExpanded(id.toString()))
    }

    private fun setupClosureButton(isBtnVisible: Boolean, drawable: Drawable?) = with(vb) {
        drawable?.let { ivClosureIndicator.setImageDrawable(it) }
        llClosureContainer.apply {
            isVisible = isBtnVisible
            setOnClickListener {
                setIsExpanded(!isExpanded)
                onClosureAction?.invoke(isExpanded)
            }
        }
    }

    fun setIsNeedToSaveExpandedState(isNeed: Boolean) {
        this.isNeedToSaveState = isNeed
    }

    fun setIsExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        if (this.isExpanded) {
            rotateChevron(-180f)
        } else {
            rotateChevron(0f)
        }
        vb.ivClosureIndicator.post {
            submitList(isExpanded)
        }
    }

    private fun submitList(isExpanded: Boolean) {
        when {
            isShimmering && isExpanded -> gridAdapter.submitList(shimmeringItems)
            isShimmering && !isExpanded -> gridAdapter.submitList(filterItems(shimmeringItems))
            isExpanded -> gridAdapter.submitList(items)
            !isExpanded -> gridAdapter.submitList(filterItems(items))
        }
    }

    private fun filterItems(items: List<ExpandableGridItem>): List<ExpandableGridItem> {
        return if (items.size <= visibleItemCount) {
            items
        } else {
            items.subList(0, visibleItemCount)
        }
    }

    private fun rotateChevron(rotation: Float = 0f) {
        vb.ivClosureIndicator.animate().rotation(rotation)
    }

    fun setListener(listener: Listener?) {
        this.listener = listener
    }

    fun setItems(items: List<ExpandableGridItem>) {
        this.items = items
        submitList(isExpanded)
    }

    fun isClosureButtonVisible(isVisible: Boolean) {
        vb.llClosureContainer.isVisible = isVisible
    }

    interface Listener {
        @Deprecated("Use onExpandableGridItemClick() method")
        fun onItemClick(deeplink: String?) {
        }

        fun onExpandableGridItemClick(item: ExpandableGridItem) {}
    }

    companion object {
        const val SUPER_STATE = "super_grid_state"
        const val EXPANDED_STATE = "grid_expanded_state"
    }

}