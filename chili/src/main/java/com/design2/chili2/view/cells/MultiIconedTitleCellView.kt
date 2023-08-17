package com.design2.chili2.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setupRoundedCellCornersMode
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.ItemDecorator
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.image.SquircleView
import com.facebook.shimmer.ShimmerFrameLayout

class MultiIconedTitleCellView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.infoCellViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CellViewStyle_InfoCellView
) : ConstraintLayout(context, attrs, defStyleAttr, defStyleRes) {

    lateinit var view: MultiIconedTitleCellViewVariables
    lateinit var adapter: MultiIconedAdapter

    init {
        initView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_multi_iconed_cell, this)
        this.view = MultiIconedTitleCellViewVariables(
            rootView = view.findViewById(R.id.root_view),
            tvTitle = view.findViewById(R.id.tv_title),
            tvDescription = view.findViewById(R.id.tv_description),
            rvIcons = view.findViewById(R.id.rv_icons),
            divider = view.findViewById(R.id.divider),
            ivInfo = view.findViewById(R.id.iv_info),
            iconsShimmer = view.findViewById(R.id.shimmer)
        )
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        adapter = MultiIconedAdapter()
        view.rvIcons.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        view.rvIcons.addItemDecoration(
            ItemDecorator(
                context.resources.getDimension(R.dimen.padding_desc_4dp).toInt()
            )
        )
        view.rvIcons.adapter = adapter
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.InfoCellView, defStyleAttr, defStyleRes).run {
            getString(R.styleable.InfoCellView_title)?.let { setTitle(it) }
            getBoolean(R.styleable.InfoCellView_isDividerVisible, false).let { setDividerVisibility(it) }
            getInteger(R.styleable.InfoCellView_roundedCornerMode, -1).takeIf { it != -1 }?.let {
                view.rootView.setupRoundedCellCornersMode(it)
            }
            recycle()
        }
    }

    fun setTitle(@StringRes resId: Int) {
        view.tvTitle.setText(resId)
    }

    fun setTitle(text: String) {
        view.tvTitle.text = text
    }

    fun setDividerVisibility(isVisible: Boolean) {
        view.divider.visibility = when (isVisible) {
            true -> View.VISIBLE
            else -> View.GONE
        }
    }

    fun setDescription(description: String) {
        view.tvDescription.visible()
        view.tvDescription.text = description
    }

    fun setDescription(description: CharSequence) {
        view.tvDescription.visible()
        view.tvDescription.text = description
    }

    fun setRoundedMode(mode: RoundedCornerMode) {
        view.rootView.setupRoundedCellCornersMode(mode.value)
    }

    fun setupCornerRoundedMode(mode: RoundedCornerMode) {
        view.rootView.setupRoundedCellCornersMode(mode.value)
    }

    fun setIsInfoButtonVisible(isVisible: Boolean) {
        view.ivInfo.isVisible = isVisible
    }

    fun setInfoButtonClickListener(onClick: () -> Unit) {
        view.ivInfo.setOnSingleClickListener {
            onClick.invoke()
        }
    }

    fun setIcons(icons: ArrayList<Any>) {
        adapter.addIcons(icons)
    }

    fun startShimmering() {
        view.iconsShimmer.startShimmer()
        adapter.addIcons(arrayListOf(R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,R.drawable.chili_multi_icon,))
    }

    fun stopShimmering(){
        view.iconsShimmer.stopShimmer()
    }
}

data class MultiIconedTitleCellViewVariables(
    val rootView: ConstraintLayout,
    val tvTitle: TextView,
    val tvDescription: TextView,
    val rvIcons: RecyclerView,
    val divider: View,
    val ivInfo: ImageView,
    val iconsShimmer: ShimmerFrameLayout
)

class MultiIconedAdapter() : RecyclerView.Adapter<MultiIconedAdapter.IconVH>() {

    private val icons = ArrayList<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconVH {
        return IconVH(LayoutInflater.from(parent.context)
            .inflate(R.layout.chili_item_icon, parent, false))
    }

    override fun onBindViewHolder(holder: IconVH, position: Int) {
        holder.bind(icons[position])
    }

    override fun getItemCount(): Int = icons.size

    fun addIcons(icons: ArrayList<Any>) {
        this.icons.clear()
        this.icons.addAll(icons)
        notifyDataSetChanged()
    }

    class IconVH(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(item: Any) {
            val ivImg = itemView.findViewById<SquircleView>(R.id.iv_img)
            if (item is String) {
                ivImg.setImageByUrl(item)
            }
            if (item is Int) {
                ivImg.setImageResource(item)
            }
        }
    }
}