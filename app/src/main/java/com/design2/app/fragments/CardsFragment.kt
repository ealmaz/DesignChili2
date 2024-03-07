package com.design2.app.fragments

import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.adapter.ShimmerAdapter
import com.design2.app.adapter.SimpleDiscountCardRecyclerViewAdapter
import com.design2.app.adapter.SimpleTextRecyclerViewAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.extensions.dp
import com.design2.chili2.view.container.ExpandableContainer
import com.design2.chili2.view.image.AutoScrollCarouselImageAdapter
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import com.design2.app.StoryActivity

class CardsFragment : BaseFragment<FragmentCardsBinding>(), AutoScrollCarouselImageAdapter.Listener {
    private lateinit var simpleAdapter: SimpleTextRecyclerViewAdapter
    private lateinit var discountCardAdapter: SimpleDiscountCardRecyclerViewAdapter
//    private var simpleList = listOf("Test 1", "Test 2", "Test 3")
    private var simpleList = listOf<String>()

    var isExpanded = false


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val shimmerAdapter = ShimmerAdapter(requireContext())
        vb.rvLoadingShimmers.adapter = shimmerAdapter

//        Handler(Looper.getMainLooper()).postDelayed({
//            shimmerAdapter.setItems(listOf("Shimmer", "Blablabla", "OLOLOLO", "SIUUUUUUU"))
//        }, 4000)

        vb.pcv1.setOnClickListener {
            vb.pcv2.setIsCardEnabled(false)
            vb.pcv3.setIsCardEnabled(true)
        }

        vb.pcv2.setOnClickListener {
            vb.pcv3.setIsCardEnabled(false)
        }

        vb.pcv3.setOnClickListener {
            vb.pcv2.setIsCardEnabled(true)
        }

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
        vb.bankCardView.setPanPinFieldYOffset(25.dp)
        vb.bankCardView.setupCvvToggle {
            vb.root.postDelayed({
                vb.bankCardView.setCardCvv("321")
                vb.bankCardView.resetPanToggleState()
            }, 1000)
        }
        vb.bankCardView.setCardCvv("321")
        vb.btnExpand.setOnClickListener {
            requireContext().startActivity(Intent(requireActivity(), StoryActivity::class.java))
            setExpandedState(!isExpanded)
        }
        val adapter = AutoScrollCarouselImageAdapter(
            listOf(
                "https://minio.o.kg/media-service/Bonus/ru/82446e5c-267d-4782-a777-dd9da2950256.png",
                "https://minio.o.kg/media-service/Bonus/ru/9274b232-02d0-4924-a28b-2e774f2ae1cb.jpg",
                "https://minio.o.kg/media-service/Bonus/ru/75369902-3870-40ad-8cb6-891fe404084e.jpg",
                "https://minio.o.kg/media-service/Bonus/ru/4a54862d-eaa0-4fac-83ff-0794f7ff32a2.jpg",
                "https://minio.o.kg/media-service/Bonus/ru/b9007171-27a6-41fe-ab1c-d0efae20855c.jpg",
                "https://minio.o.kg/media-service/Bonus/ru/68b4602e-d17c-429e-8132-afebef54d105.jpg"
                ),
            this)

        vb.bannerView1.run {
            PagerSnapHelper().attachToRecyclerView(this)
            this.adapter = adapter
            resumeAutoScroll()
        }

//        ShowcaseHelper(requireActivity()).showShowcase(vb.accent, ShowcaseHelper.ShowcaseData(
//            title = "Title",
//            message = "Message dfdjgkjsdkgjdslkg  dkjngkjdsgkjdskjghsdk jdkfskjdfjskjfl jdslkfjlsdkjf skdjfklsdjflk kjd foldknf dlkfjlskdfl ldkfjslkdjflk",
//            gravity = ShowcaseHelper.ShowcasePosition.BOTTOM,
//            indicatorsCount = 3,
//            currentIndicatorPosition = 0,
//            buttonText = "Ponyatno"
//        ), {}, {})

        vb.bonusPartnerCard.apply {
            setTitle("Bonus Partner")
            setIcon("https://minio.o.kg/catalog/icons/light/taxes.png")
            setSubtitle("до 2%")
        }
        vb.bonusPartnerCard2.apply {
            setTitle("Bonus Partner la la la")
            setIcon("https://minio.o.kg/catalog/icons/light/taxes.png")
            setSubtitle("до 2%")
        }
    }

    private fun setExpandedState(isExpanded: Boolean) {
        this.isExpanded = isExpanded
        setAllContainersIsExpanded(isExpanded)
    }

    private fun initDiscountCard(){
        discountCardAdapter = SimpleDiscountCardRecyclerViewAdapter(requireContext())
        vb.ecvDiscountRv.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        vb.ecvDiscountRv.adapter = discountCardAdapter
        val icon = "https://minio.o.kg/pams-receiver-documents/33944000008719-kvxZCbF1Gpb7iDGSyGXhgEcsRbvoKTOx3cYWIlK89mPgSI3L4F.png"
        val collection = mutableListOf<String>()
        for (i in 0..10){
            collection.add(icon)
        }
        discountCardAdapter.updateListWithIcons(collection)
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

    override fun onBannerClicked(position: Int) {

    }
}