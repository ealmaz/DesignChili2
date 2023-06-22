package com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.R
import com.design2.chili2.extensions.gone
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.io.Serializable

class SearchSelectorBottomSheet private constructor(
    mContext: Context,
    private val optionsList: List<Option>,
    private val isSingleSelectionType: Boolean,
    private val isSearchAvailable: Boolean,
    private val isHeadersVisible: Boolean
) : BaseViewBottomSheetDialogFragment(), SearchSelectorItemListener {

    private var filterText: String = ""

    private val searchSelectorAdapter: SearchSelectorAdapter by lazy {
        SearchSelectorAdapter(this, isHeadersVisible)
    }

    init {
        setupView()
    }

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val bottomSheetView: View = layoutInflater.inflate(R.layout.chili_view_bottom_sheet_search_selector, null)
        searchSelectorAdapter.addItems(optionsList)
        bottomSheetView.findViewById<RecyclerView>(R.id.recycler_view)?.run {
            layoutManager = LinearLayoutManager(context)
            adapter = searchSelectorAdapter
        }
        bottomSheetView.findViewById<ImageView>(R.id.iv_close)?.setOnClickListener {
            this.dismiss()
        }
        setupSearchInputView(bottomSheetView)
        return bottomSheetView
    }

    private fun setupView() {

    }

    private fun setupSearchInputView(view: View) {
        if (!isSearchAvailable) {
            view.findViewById<LinearLayout>(R.id.ll_search)?.gone()
            return
        }
        view.findViewById<EditText>(R.id.et_search)?.run {
            doAfterTextChanged {
                expandBottomSheet()
                filterText = it?.toString() ?: ""
                searchSelectorAdapter.addItems(optionsList, filterText)
            }
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) expandBottomSheet()
            }
        }
    }

    private fun expandBottomSheet() {
        if (isExpandBottomSheet()) return
        val bottomSheetInternal = bottomSheetView?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
        BottomSheetBehavior.from(bottomSheetInternal).state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun isExpandBottomSheet(): Boolean {
        val bottomSheetInternal = bottomSheetView?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
        return BottomSheetBehavior.from(bottomSheetInternal).state == BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onItemSelection(selectedId: String) {
        if (isSingleSelectionType) setSelectedForSingleType(selectedId)
        else setSelectedForMultipleType(selectedId)
    }

    private fun setSelectedForSingleType(selectedId: String) {
        optionsList.forEach {
            it.isSelected = if (it.id == selectedId) !it.isSelected
            else false
        }
        dismiss()
    }

    private fun setSelectedForMultipleType(selectedId: String) {
        optionsList.find { it.id == selectedId }?.apply {
            isSelected = !isSelected
        }
        searchSelectorAdapter.addItems(optionsList, filterText)
    }

    class Builder {
        private var isSearchAvailable: Boolean = true
        private var isSingleSelection: Boolean = true
        private var isHeaderVisible: Boolean = true
        private var options: List<Option>? = null

        fun setIsSearchAvailable(isAvailable: Boolean): Builder {
            isSearchAvailable = isAvailable
            return this
        }

        fun setIsSingleSelection(isSingle: Boolean): Builder {
            isSingleSelection = isSingle
            return this
        }

        fun setIsHeaderVisible(isVisible: Boolean): Builder {
            isHeaderVisible = isVisible
            return this
        }

        fun build(context: Context, options: List<Option>): SearchSelectorBottomSheet {
            return SearchSelectorBottomSheet(context, options, isSingleSelection, isSearchAvailable, isHeaderVisible)
        }
    }
}

data class Option(val id: String, val value: String, var isSelected: Boolean): Serializable
