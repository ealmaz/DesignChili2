package com.design2.chili2.view.container

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.children
import androidx.core.view.isVisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewContainerExpandableCardBinding
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

    private lateinit var vb: ChiliViewContainerExpandableCardBinding

    private var isExpandable: Boolean = false

    private var isExpanded: Boolean = false

    init {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
        setupViews()
    }

    private fun inflateView(context: Context) {
        vb = ChiliViewContainerExpandableCardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
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
        mutableShimmeringViewMap[vb.tvTitle] = vb.viewTitleShimmer
    }


    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setSubtitle(charSequence: CharSequence?) {
        vb.tvSubtitle.setTextOrHide(charSequence)
        if (charSequence == null) mutableShimmeringViewMap.remove(vb.tvSubtitle)
        else mutableShimmeringViewMap[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setSubtitle(resId: Int?) {
        vb.tvSubtitle.setTextOrHide(resId)
        if (resId == null) mutableShimmeringViewMap.remove(vb.tvSubtitle)
        else mutableShimmeringViewMap[vb.tvSubtitle] = vb.viewSubtitleShimmer
    }

    fun setValue(charSequence: CharSequence?) {
        vb.tvValue.setTextOrHide(charSequence)
        if (charSequence == null) mutableShimmeringViewMap.remove(vb.tvValue)
        else mutableShimmeringViewMap[vb.tvValue] = null
    }

    fun setValue(resId: Int) {
        vb.tvValue.setText(resId)
        if (resId == null) mutableShimmeringViewMap.remove(vb.tvValue)
        else mutableShimmeringViewMap[vb.tvValue] = null
    }

    fun setIsExpandable(isExpandable: Boolean?) {
        this.isExpandable = isExpandable ?: false
        if (this.isExpandable) setupAsExpandable()
        else setupAsUnExpandable()
    }

    private fun setupAsExpandable() = with(vb) {
        ivArrow.visible()
        ivArrow.setOnClickListener { setIsExpanded(!isExpanded) }
        rootView.setOnClickListener { setIsExpanded(!isExpanded) }
    }

    private fun setupAsUnExpandable() = with(vb) {
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
        vb.ivArrow.animate().rotation(rotation)
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

data class ExpandableItemData(
    val title: CharSequence? = null,
    val subTitle: CharSequence? = null,
    val titleValue: CharSequence? = null,
    val subtitleValue: CharSequence? = null,
)