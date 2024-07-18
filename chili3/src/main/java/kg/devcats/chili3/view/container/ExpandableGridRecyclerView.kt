package kg.devcats.chili3.view.container

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.chili2.storage.ComponentsPreferences
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliExpandableGridRecyclerViewBinding

class ExpandableGridRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = com.design2.chili2.R.attr.expandableContainerViewStyle,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_Container_ExpandableContainerViewStyle
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var vb: ChiliExpandableGridRecyclerViewBinding

    private val componentPref: ComponentsPreferences by lazy {
        ComponentsPreferences.getInstance(context)
    }

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    var isExpanded: Boolean = false
    var isShimmering: Boolean = false
    var visibleItems: Int = 0
    var onClosureAction: ((isExpanded: Boolean) -> Unit)? = null
    private var isNeedToSaveState: Boolean = false

    private var items: List<ExpandableGridItem> = listOf()
    private var shimmeringItems = List(8) {
        ExpandableGridItem(it.toLong(), null, null, null, true)
    }

    private var listener: IconTitledAdapter.Listener? = null
    private val gridAdapter by lazy {
        IconTitledAdapter { listener?.onItemClick(it) }
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

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.ExpandableGridRecyclerView, defStyleAttr, defStyleRes).run {
            setIsNeedToSaveExpandedState(getBoolean(R.styleable.ExpandableGridRecyclerView_isNeedToSaveExpandedState, false))
            setIsExpandedOrRestoreStateFromPreferences(getBoolean(R.styleable.ExpandableGridRecyclerView_isExpanded, true))
            setVisibileItems(getInteger(R.styleable.ExpandableGridRecyclerView_visibleItems, 0))
            setupClosureButton(getDrawable(R.styleable.ExpandableGridRecyclerView_bottomIcon))
            setupRecyclerView()
            recycle()
        }
    }

    override fun onSaveInstanceState(): Parcelable {
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
        layoutManager = GridLayoutManager(context, visibleItems, LinearLayoutManager.VERTICAL, false)
    }

    fun setVisibileItems(count: Int) {
        visibleItems = count
    }

    private fun setIsExpandedOrRestoreStateFromPreferences(isExpanded: Boolean) {
        if (!isNeedToSaveState) setIsExpanded(isExpanded)
        else setIsExpanded(componentPref.getIsExpandableContainerExpanded(id.toString()))
    }

    private fun setupClosureButton(drawable: Drawable?) = with(vb.ivClosureIndicator) {
        drawable?.let { setImageDrawable(it) }
        setOnClickListener {
            setIsExpanded(!isExpanded)
            onClosureAction?.invoke(isExpanded)
        }
    }

    fun setIsNeedToSaveExpandedState(isNeed: Boolean) {
        this.isNeedToSaveState = isNeed
    }

    fun setIsExpanded(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        if (this.isExpanded) {
            rotateChevron(0f)
        } else {
            rotateChevron(-180f)
        }
        vb.ivClosureIndicator.post {
            submitList(isExpanded)
        }
    }

    private fun submitList(isExpanded: Boolean) {
        when {
            isShimmering && isExpanded -> gridAdapter.submitList(shimmeringItems)
            isShimmering && !isExpanded -> gridAdapter.submitList(getItems(shimmeringItems))
            isExpanded -> gridAdapter.submitList(items)
            !isExpanded -> gridAdapter.submitList(getItems(items))
        }
    }

    private fun getItems(items: List<ExpandableGridItem>): List<ExpandableGridItem> {
        return if (items.size <= visibleItems) {
            items
        } else {
            items.subList(0, visibleItems)
        }
    }

    private fun rotateChevron(rotation: Float = 0f) {
        vb.ivClosureIndicator.animate().rotation(rotation)
    }

    fun setListener(listener: IconTitledAdapter.Listener?){
        this.listener = listener
    }

    fun setItems(items: List<ExpandableGridItem>) {
        this.items = items
        submitList(isExpanded)
    }

    companion object {
        const val SUPER_STATE = "super_grid_state"
        const val EXPANDED_STATE = "grid_expanded_state"
    }

}