package com.design2.app.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.adapter.SimpleDiscountCardRecyclerViewAdapter
import com.design2.app.adapter.SimpleTextRecyclerViewAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.view.container.ExpandableContainer
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering

class CardsFragment : BaseFragment<FragmentCardsBinding>() {
    private lateinit var simpleAdapter: SimpleTextRecyclerViewAdapter
    private lateinit var discountCardAdapter: SimpleDiscountCardRecyclerViewAdapter
//    private var simpleList = listOf("Test 1", "Test 2", "Test 3")
    private var simpleList = listOf<String>()

    var isExpanded = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.bal2.isChevronVisible(false)
        vb.bal4.apply {
            setValueText("2435 c")
            setValueIcon(com.design2.chili2.R.drawable.chili_ic_warning)
        }
        vb.cat2.setGravity(Gravity.CENTER_HORIZONTAL)
        initRV()
        initDiscountCard()
        initBanners()
        vb.bankCardIcon.setStartIcon(R.drawable.gpay)
        vb.bankCardView.setupCardPanToggle {
            vb.root.postDelayed({
                                vb.bankCardView.setCardPan("9417 1243 3425 4215")
            }, 1000)
        }
        vb.bankCardView.setCardPan("9417 1243 3425 4215")
        vb.bankCardView.setupCvvToggle {
            vb.root.postDelayed({
                vb.bankCardView.setCardCvv("321")
            }, 1000)
        }
        vb.bankCardView.setCardCvv("321")
        vb.btnExpand.setOnClickListener {
            setExpandedState(!isExpanded)
        }
    }

    private fun setExpandedState(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        setAllContainersIsExpanded(isExpanded)
    }

    private fun initDiscountCard(){
        discountCardAdapter = SimpleDiscountCardRecyclerViewAdapter(requireContext())
        vb.ecvDiscountRv.layoutManager = LinearLayoutManager(requireContext())
        vb.ecvDiscountRv.adapter = discountCardAdapter
        discountCardAdapter.updateListWithIcons(listOf("https://minio.o.kg/pams-receiver-documents/33944000008719-kvxZCbF1Gpb7iDGSyGXhgEcsRbvoKTOx3cYWIlK89mPgSI3L4F.png"))
    }

    private fun initRV(){
        simpleAdapter = SimpleTextRecyclerViewAdapter(requireContext())
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
        }else{
            setSubtitle(emptyText)
            setEndIcon(null)
            setActionText("Action")
        }
    }

    private var isElcapSubtitleShimmerShow = false
    private fun initBanners() = with(vb) {
        pbcvElcap.apply {
            setOnClickListener {
                isElcapSubtitleShimmerShow = !isElcapSubtitleShimmerShow
                if (isElcapSubtitleShimmerShow) {
                    setSubtitleText("Your card is ready")
                    setSubtitleColor(com.design2.chili2.R.color.magenta_1)
                    setSubtitleContainerBackground(R.drawable.bg_rounded_white_with_paddings)
                    onStartShimmer()
                } else {
                    setSubtitleText("Apply card right now")
                    setSubtitleColor(com.design2.chili2.R.color.white_1)
                    setSubtitleContainerBackground(0)
                    onStopShimmer()
                }
            }
        }
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