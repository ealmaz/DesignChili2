package com.design2.app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Toast
import androidx.core.text.parseAsHtml
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.MainActivity
import com.design2.app.adapter.EditableCellViewsAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCellBinding
import com.design2.chili2.R
import com.design2.chili2.util.RoundedCornerMode
import com.design2.chili2.view.container.shadow_layout.model.ShadowType
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.startShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import com.design2.chili2.view.shimmer.stopShimmering


class CellViewsFragment : BaseFragment<FragmentCellBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.actionCell.setIsChevronVisible(false)
        vb.btnP1.setOnClickListener {
            vb.progress.setProgressPercent(100)
        }
        vb.firstCell.apply {
            setupIsSurfaceClickable(false)
            setRoundedMode(RoundedCornerMode.TOP)
        }
        vb.btnP2.setOnClickListener {
            vb.progress.setProgressPercent(0)
        }
        vb.btnP4.setOnClickListener {
            vb.progress4.setProgressPercent(80)
        }
        vb.btnP5.setOnClickListener {
            vb.progress4.setProgressPercent(20)
        }
        vb.dcv1.setValueTextColor(resources.getColor(com.design2.chili2.R.color.red_1))

        vb.dcv1.setValue("100 <u>c</u>".parseAsHtml())
        vb.dcv1.setIconUrl("https://minio.o.kg/call-center/telega.png")
        vb.testAdditionalTextCellView.apply {
            setOnClickListener {
                setAdditionalText("0")
            }
        }

        vb.progress.removeBackground()

        vb.progress4.startShimmering()

        val icon =
            "https://tal7aouy.gallerycdn.vsassets.io/extensions/tal7aouy/icons/3.6.3/1679578385939/Microsoft.VisualStudio.Services.Icons.Default"

        vb.multiiconedCellView.apply {
            setIsActionButtonVisible(true)
            setActionBtnText("Весь список")
            setIcons(arrayListOf(icon, icon, icon))
            setIconsTopMargin(com.design2.chili2.R.dimen.view_12dp)
            setOnItemClicked {
                Toast.makeText(this.context, "Cell Clicked", Toast.LENGTH_SHORT).show()
            }
        }

        vb.shadowEx.updateShadowType(ShadowType.TOP)
        vb.shadowEx2.updateShadowType(ShadowType.BOTTOM)

        vb.multiiconedCellView2.apply {
            setIsInfoButtonVisible(true)
            setIcons(arrayListOf(icon, icon, icon))
        }
        val adapter = EditableCellViewsAdapter(null)
        val touchHelper = getItemTouchHelper(adapter)
        adapter.dragStart = {
            touchHelper.startDrag(it)
        }
        vb.editCellViewRv.itemAnimator = DefaultItemAnimator()
        vb.editCellViewRv.adapter = adapter
        touchHelper.attachToRecyclerView(vb.editCellViewRv)
        vb.editModeBtn.setOnClickListener {
            adapter.isEditMode = !adapter.isEditMode
        }
        vb.checkboxWithEndText.apply {
            setTitleTextAppearance(com.design2.chili2.R.style.Chili_H7_Value)
            setSubtitle("Тазалык 30402340234")
            setSubtitleTextAppearance(com.design2.chili2.R.style.Chili_H7_Value)
            insertEndText("4000 c")
        }

        //CardCellView
        vb.cardcell1.startShimmering()
        vb.cardcell2.startShimmering()
        Handler(Looper.getMainLooper()).postDelayed({ vb.cardcell2.stopShimmering() }, 2000)
        vb.cardcell4.apply {
            setIcon(com.design2.app.R.drawable.ic_card_default)
            setTitle("Test Title")
            setSubtitle("Test Subtitle")
            setAdditionalText("121212 c")
        }
    }

    private fun getItemTouchHelper(adapter: EditableCellViewsAdapter) =
        ItemTouchHelper(object : ItemTouchHelper.Callback() {

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
            ): Int {
                return makeMovementFlags(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
            override fun isLongPressDragEnabled() = false
            override fun isItemViewSwipeEnabled() = false

        })

    override fun onResume() {
        super.onResume()

    }

    override fun inflateViewBinging(): FragmentCellBinding {
        return FragmentCellBinding.inflate(layoutInflater)
    }

    override fun startShimmering() {
        super.startShimmering()
        vb.root.startGroupShimmering()
    }

    override fun stopShimmering() {
        super.stopShimmering()
        vb.root.stopGroupShimmering()
    }
}