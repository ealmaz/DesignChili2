package com.design2.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.StoriesActivity
import com.design2.app.ToolbarActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentAllViewsBinding

class AllViewsFragment : BaseFragment<FragmentAllViewsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.text.setOnClickListener {
            openFragment(TextAppearancesFragment())
        }
        vb.stories.setOnClickListener {
            val intent = Intent(requireContext(), StoriesActivity::class.java)
            startActivity(intent)
        }
        vb.buttons.setOnClickListener {
            openFragment(ButtonsFragment())
        }
        vb.fileds.setOnClickListener {
            openFragment(InputFields())
        }

        vb.cards.setOnClickListener {
            openFragment(CardsFragment())
        }
        vb.cellsNew.setOnClickListener {
            openFragment(CellViewsFragment())
        }
        vb.snackbar.setOnClickListener {
            openFragment(SnackbarFragment())
        }
        vb.common.setOnClickListener {
            openFragment(CommonViewsFragment())
        }
        vb.bottomSheet.setOnClickListener {
            openFragment(BottomSheetsFragment())
        }
        vb.toolbars.setOnClickListener {
            startActivity(Intent(requireActivity(), ToolbarActivity::class.java))
        }
        vb.pickers.setOnClickListener {
            openFragment(DatePickerFargment())
        }
        vb.navBars.setOnClickListener {
            openFragment(NavigationBarsFragment())
        }
        vb.highlighters.setOnClickListener {
            openFragment(HighlightersFragment())
        }

        vb.overlays.setOnClickListener {
            openFragment(OverlaysFragment())
        }

        vb.groupingContainer.setOnClickListener {
            openFragment(GroupdableRVFragment())
        }

        vb.tooltips.setOnClickListener {
            openFragment(TooltipsFragment())
        }
        
        (activity as MainActivity).setUpHomeEnabled(false)
    }

    override fun inflateViewBinging(): FragmentAllViewsBinding {
        return FragmentAllViewsBinding.inflate(layoutInflater)
    }
}