package com.design2.app.fragments

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.adapter.SimpleRecyclerViewAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.view.container.ExpandableContainer
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering

class CardsFragment : BaseFragment<FragmentCardsBinding>() {
    private lateinit var simpleAdapter: SimpleRecyclerViewAdapter
    private var simpleList = listOf("Test 1", "Test 2", "Test 3")


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.bal2.isChevronVisible(false)
        vb.bal4.apply {
            setValueText("2435 c")
            setValueIcon(com.design2.chili2.R.drawable.chili_ic_warning)
        }
        initRV()
    }

    private fun initRV(){
        simpleAdapter = SimpleRecyclerViewAdapter(requireContext())
        vb.ecvRv.layoutManager = LinearLayoutManager(requireContext())
        vb.ecvRv.adapter = simpleAdapter
        simpleAdapter.updateList(simpleList)
        vb.ecvRvContainer.setState("Test case", simpleList.isEmpty())

        vb.ecvRvContainer.setActionClick {
            imitateListFromRV()
        }
        vb.ecvRvContainer.setEndIconClickListener {
            imitateListFromRV()
        }
    }
    private fun imitateListFromRV(){
        if (simpleList.isEmpty()) {
            Toast.makeText(requireContext(), "List is - filled", Toast.LENGTH_SHORT).show()
            simpleList = listOf("Test 1", "Test 2", "Test 3")

        } else {
            simpleList = listOf()
            Toast.makeText(requireContext(), "List is - empty", Toast.LENGTH_SHORT).show()
        }
        simpleAdapter.updateList(simpleList)
        vb.ecvRvContainer.setState("Test case", simpleList.isEmpty())
    }

    private fun ExpandableContainer.setState(text: String, isEmpty: Boolean) {
        val emptyText: CharSequence? = null
        if (isEmpty){
            setSubtitle(text)
            setEndIcon(R.drawable.plus)
            setActionText(emptyText)
            setIsListEmpty(true)
        }else{
            setSubtitle(emptyText)
            setEndIcon(null)
            setActionText("Action")
            setIsListEmpty(false)
        }
        setIsExpanded(isExpanded)
    }


    override fun inflateViewBinging(): FragmentCardsBinding {
        return FragmentCardsBinding.inflate(layoutInflater)
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