package com.design2.chili2.view.container

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.visible
import com.design2.chili2.view.card.ExpandableCardItemView
import com.design2.chili2.view.shimmer.ShimmeringView
import com.design2.chili2.view.shimmer.startShimmering
import com.design2.chili2.view.shimmer.stopShimmering
import com.facebook.shimmer.ShimmerFrameLayout

class ExpandableCardContainer @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.expandableCardContainerDefaultStyle,
    defStyleRes: Int = R.style.Chili_Container_ExpandableCardContainer,
) : LinearLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private val mutableShimmeringViewMap = mutableMapOf<View, ShimmerFrameLayout?>()

    private lateinit var view: ExpandableCardContainerVariables

    private var isExpandable: Boolean = false

    private var isExpanded: Boolean = false

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupViews()
    }

    private fun inflateView(context: Context) {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.chili_view_container_expandable_card, this, true)
        this.view = ExpandableCardContainerVariables(
            tvTitle = view.findViewById(R.id.tv_title),
            tvSubtitle = view.findViewById(R.id.tv_subtitle),
            tvValue = view.findViewById(R.id.tv_value),
            ivArrow = view.findViewById(R.id.iv_arrow),
            rootView = view.findViewById(R.id.root_view),
            titleShimmer = view.findViewById(R.id.view_title_shimmer),
            subtitleShimmer = view.findViewById(R.id.view_subtitle_shimmer)
        )
    }

    private fun obtainAttributes(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) {
        context.obtainStyledAttributes(attrs, R.styleable.ExpandableCardContainer, defStyleAttr, defStyleRes).run {
            getString(R.styleable.ExpandableCardContainer_title).run { setTitle(this) }
            getString(R.styleable.ExpandableCardContainer_subtitle).run { setSubtitle(this) }
            getString(R.styleable.ExpandableCardContainer_value).run { setValue(this) }
            setIsExpandable(getBoolean(R.styleable.ExpandableCardContainer_isExpandable, false))
            setIsExpanded(getBoolean(R.styleable.ExpandableCardContainer_isExpanded, false))
            recycle()
        }
    }

    private fun setupViews() {
        mutableShimmeringViewMap[view.tvTitle] = view.titleShimmer
    }


    fun setTitle(charSequence: CharSequence?) {
        view.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        view.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) mutableShimmeringViewMap.remove(view.tvSubtitle)
        else mutableShimmeringViewMap[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setSubtitle(resId: Int?) {
        view.tvSubtitle.setTextOrHide(resId)
        if (resId == null) mutableShimmeringViewMap.remove(view.tvSubtitle)
        else mutableShimmeringViewMap[view.tvSubtitle] = view.subtitleShimmer
    }

    fun setValue(charSequence: CharSequence?) {
        view.tvValue.setTextOrHide(charSequence)
        if (charSequence == null) mutableShimmeringViewMap.remove(view.tvValue)
        else mutableShimmeringViewMap[view.tvValue] = null
    }

    fun setValue(resId: Int) {
        view.tvValue.setText(resId)
        if (resId == null) mutableShimmeringViewMap.remove(view.tvValue)
        else mutableShimmeringViewMap[view.tvValue] = null
    }

    fun setIsExpandable(isExpandable: Boolean?) {
        this.isExpandable = isExpandable ?: false
        if (this.isExpandable) setupAsExpandable()
        else setupAsUnExpandable()
    }

    private fun setupAsExpandable() = with(view) {
        ivArrow.visible()
        ivArrow.setOnClickListener { setIsExpanded(!isExpanded) }
        rootView.setOnClickListener { setIsExpanded(!isExpanded) }
    }

    private fun setupAsUnExpandable() = with(view) {
        ivArrow.gone()
        ivArrow.setOnClickListener {}
        rootView.setOnClickListener {}
    }

    fun setIsExpanded(isExpanded: Boolean?) {
        this.isExpanded = isExpanded ?: false
        if (this.isExpanded) {
            rotateChevron(180f)
            children.forEach {
                if (it is ExpandableCardItemView) it.visible()
            }
        } else {
            rotateChevron(0f)
            children.forEach {
                if (it is ExpandableCardItemView) it.gone()
            }
        }
    }

    private fun rotateChevron(rotation: Float = 0f) {
        view.ivArrow.animate().rotation(rotation)
    }

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        if (child is ExpandableCardItemView) child.isVisible = isExpanded
        super.addView(child, index, params)
    }

    private fun createExpandableRow(): ExpandableCardItemView {
        return ExpandableCardItemView(context)
    }

    private fun setupExpandableRow(item: ExpandableItemData, itemView: ExpandableCardItemView) {
        itemView.apply {
            setTitle(item.title)
            setSubtitle(item.subTitle)
            setTitleValue(item.titleValue)
            setSubtitleValue(item.subtitleValue)
        }
    }

    override fun onStartShimmer() {
        children.forEach { (it as? ShimmeringView)?.startShimmering() }
    }

    override fun onStopShimmer() {
        children.forEach { (it as? ShimmeringView)?.stopShimmering() }
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = mutableShimmeringViewMap
}

data class ExpandableCardContainerVariables(
    val tvTitle: TextView,
    val tvSubtitle: TextView,
    val tvValue: TextView,
    val ivArrow: ImageView,
    val rootView: ConstraintLayout,
    val titleShimmer: ShimmerFrameLayout,
    val subtitleShimmer: ShimmerFrameLayout
)

data class ExpandableItemData(
    val title: CharSequence? = null,
    val subTitle: CharSequence? = null,
    val titleValue: CharSequence? = null,
    val subtitleValue: CharSequence? = null,
)