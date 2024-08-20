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
import com.design2.app.StoryActivity
import com.design2.app.adapter.ShimmerAdapter
import com.design2.app.adapter.SimpleDiscountCardRecyclerViewAdapter
import com.design2.app.adapter.SimpleTextRecyclerViewAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentCardsBinding
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setScrollListener
import com.design2.chili2.view.container.ExpandableContainer
import com.design2.chili2.view.image.AutoScrollCarouselImageAdapter
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import kg.devcats.chili3.model.MyConnectionProfile
import kg.devcats.chili3.model.PackageLeftOver
import kg.devcats.chili3.model.PackageType
import kg.devcats.chili3.util.StoriesStatus

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

        vb.btnAccountCardsExamples.setOnClickListener {
            openFragment(AccountCardsExamplesFragment())
        }

        vb.btnExpandableGridRecyclerViewExamples.setOnClickListener {
            openFragment(ExpandableGridRecyclerViewExampleFragment())
        }

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
                "https://devminio.o.kg/media-service/Bonus/ky/9170f031-4cd7-4c7e-ad7d-04a423eef673.png",
                "https://devminio.o.kg/media-service/Bonus/ky/0d6780b9-cbf6-41a7-8750-4da9d7688715.jpg",
                "https://devminio.o.kg/media-service/Bonus/ky/9c7937f2-ff27-44f5-95a9-d51a0aaa7a8f.jpg",
                "https://devminio.o.kg/media-service/Bonus/ky/c18c82e9-d3ab-4086-b4b7-8160b60c9894.png",
                ),
            this)

        vb.bannerView1.run {
            PagerSnapHelper().attachToRecyclerView(this)
            this.adapter = adapter
            resumeAutoScroll()
        }

        vb.cardCv.setScrollListener({ vb.bannerView1.pauseAutoScroll() },
            {  vb.bannerView1.resumeAutoScroll()})


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

        vb.scvStories.setOnClickListener {
            vb.scvStories.setStatus(StoriesStatus.VIEWED.value)
        }
        vb.scvStories1.setOnClickListener {
            vb.scvStories1.setStatus(StoriesStatus.VIEWED.value)
        }
        vb.scvStories2.setOnClickListener {
            vb.scvStories2.setStatus(StoriesStatus.VIEWED.value)
        }

        vb.iconStateCardView.apply {
            setupAsErrorState(com.design2.chili2.R.drawable.chili_ic_warning)
            setOnSingleClickListener {
                clearErrorState()
            }
        }

        vb.pcvPromo1.setIcon("https://minio.o.kg/catalog/icons/light/taxes.png")

        vb.pcvProduct.setDiscountBackground("#10C44C")
        vb.pcvProduct.setOnClickListener {
            vb.pcvProduct.setDiscountBackground(arrayOf("#FF6491", "#F91155"))
        }
        vb.myConnectionCardView.apply {
            val list = listOf(
                PackageLeftOver(PackageType.INTERNET, "10 Гб", "из 40 Гб", leftOverPercent = 20),
                PackageLeftOver(PackageType.CALL, "10 мин", "из 40 мин", leftOverPercent = 80)
            )
            setProfile(
                MyConnectionProfile("100 c", isWithPackages = true, packages = list)
            )
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
        val icon = "https://minio.o.kg/media-service/DiscountCard/narodny.png"
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