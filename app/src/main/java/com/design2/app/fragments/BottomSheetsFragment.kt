package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FrgmentBottomSheetsBinding
import com.design2.chili2.view.modals.base.BaseFragmentBottomSheetDialogFragment
import com.design2.chili2.view.modals.bottom_sheet.*
import com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet.Option
import com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import com.design2.chili2.view.modals.in_app.InAppPushBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior

class BottomSheetsFragment : BaseFragment<FrgmentBottomSheetsBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.action.setOnClickListener {
            ActionBottomSheet.create(listOf(
                ActionBottomSheetButton("First", ActionBottomSheetButtonType.SIMPLE, {Toast.makeText(context, "OnClcik first", Toast.LENGTH_SHORT).show()}),
                ActionBottomSheetButton("Second", ActionBottomSheetButtonType.SIMPLE, {Toast.makeText(context, "OnClcik second", Toast.LENGTH_SHORT).show()}),
                ActionBottomSheetButton("Third", ActionBottomSheetButtonType.SIMPLE, {Toast.makeText(context, "OnClcik third", Toast.LENGTH_SHORT).show()}),
                ActionBottomSheetButton("Cancel", ActionBottomSheetButtonType.ACCENT, {dismiss()}),
            )).show(childFragmentManager)
        }
        vb.info.setOnClickListener {
            InfoBottomSheet.Builder()
                .setIcon(R.drawable.ic_cat)
                .setMessage("Текстовый блок, который содержит 120 символов, и этого количества, должно хватить для информации в четыре строки.")
                .setPrimaryButton("Понятно" to {Toast.makeText(context, "Понятно", Toast.LENGTH_SHORT).show()})
                .setSecondaryButton("Ясно" to {Toast.makeText(context, "Ясно", Toast.LENGTH_SHORT).show()})
//                .setDismissEvent { Toast.makeText(context, "Dismissed", Toast.LENGTH_SHORT).show() }
                .setIsHideable(false)
                .build()
                .show(childFragmentManager)
        }
        vb.detail.setOnClickListener {
            DetailedInfoBottomSheet.Builder()
                .setIcon(R.drawable.ic_cat)
                .setMessage("Текстовый блок, который содержит много текста и не может уместиться в четыре строки (как в маленьком Bottom-sheet).\n\n" +
                        "Возможно имеет какую-то инструкцию или подробное описание функционал. Плюс тут есть картиночка. \n\n" +
                        "Высота зависит от контента.")
                .setPrimaryButton("Понятно" to {Toast.makeText(context, "Понятно", Toast.LENGTH_SHORT).show()})
                .build()
                .show(childFragmentManager)
        }
        vb.description.setOnClickListener {
            DescriptionBottomSheet.Builder()
                .setIcon(R.drawable.ic_cat)
                .setTitle("Title")
                .setDescription("Description")
                .setDescriptionSecondary("Secondary info")
                .setSecondaryButton("Понятно" to {Toast.makeText(context, "Понятно", Toast.LENGTH_SHORT).show()})
                .build()
                .show(childFragmentManager)
        }
        vb.custom.setOnClickListener {
            CustomFragmentBottomSheet().show(childFragmentManager)
        }
        vb.customWithBuilder.setOnClickListener {



            FragmentBottomSheet.Builder()
                .setContentFragment(CommonViewsFragment())
                .setIsBackButtonEnabled(true)
                .setHorizontalMargin(resources.getDimension(com.design2.chili2.R.dimen.padding_16dp).toInt())
                .setBottomMargin(resources.getDimension(com.design2.chili2.R.dimen.padding_16dp).toInt())
                .setState(BottomSheetBehavior.STATE_EXPANDED)
                .build()
                .show(childFragmentManager)
        }
        vb.inApp.setOnClickListener {
            InAppPushBottomSheet.Builder()
                .setBtnMoreInfo("Подробнее" to {
                    dismiss()
                })
                .setDescription("Описание описания, которое описывает описанное описание описанного описания,\n" +
                        "максимум из 190 символов, но если ничего \n\n" +
                        "не помещается, не проблема, потому что у нас всегда есть спецсимвол такой как троеточиеef evremiv ervmeive ervnervn ervnervne enruvneuv eunrvuernv eurnvueirv eurnvuev eurnvuev ervneurv")
                .setTitle("Максимальная длина заголовка равна 60 символам, а если не помещается то verververveverv erverv erverv erverv erve")
                .setOnBannerClick {
                    dismiss()
                }
                .build()
                .show(childFragmentManager)
        }
        vb.inAppBanner.setOnClickListener {
            InAppPushBottomSheet.Builder()
                .setBannerUrl("https://cdn.pixabay.com/photo/2016/11/29/12/13/fence-1869401_1280.jpg")
                .setBtnMoreInfo("Подробнее" to {
                    dismiss()
                })
                .setDescription("Описание описания, которое описывает описанное описание описанного описания,\n" +
                        "максимум из 190 символов, но если ничего \n\n" +
                        "не помещается, не проблема, потому что у нас всегда есть спецсимвол такой как троеточиеef evremiv ervmeive ervnervn ervnervne enruvneuv eunrvuernv eurnvueirv eurnvuev eurnvuev ervneurv")
                .setTitle("Максимальная длина заголовка равна 60 символам, а если не помещается то verververveverv erverv erverv erverv erve")
                .setOnBannerClick {
                    dismiss()
                }
                .build()
                .show(childFragmentManager)
        }
        vb.visbileBottomSheet.setOnClickListener {
            openFragment(InteractiveBottomSheetFragment())
        }
        vb.searchSelectorBs.setOnClickListener {
            SearchSelectorBottomSheet.Builder()
                .setIsSearchAvailable(true)
                .setIsSingleSelection(true)
                .setIsHeaderVisible(true)
                .build(requireContext(), listOf(
                    Option("option1", "Option1", false),
                    Option("option2", "Option2", false),
                    Option("option3", "Option3", false),
                    Option("option4", "Option4", true),
                    Option("option5", "Option5", false),
                    Option("option6", "Option6", false),
                    Option("option7", "Option7", false),
                    Option("option8", "Option8", false),
                    Option("option9", "Option9", false),
                ))
                .show()
        }
    }

    override fun inflateViewBinging(): FrgmentBottomSheetsBinding {
        return FrgmentBottomSheetsBinding.inflate(layoutInflater)
    }
}

class CustomFragmentBottomSheet : BaseFragmentBottomSheetDialogFragment() {

    override var topDrawableVisible: Boolean = true
    override var hasCloseIcon: Boolean = true
    override var isHideable: Boolean = false
    override var isBackButtonEnabled: Boolean = false

    override fun setupBottomSheetBehavior(behavior: BottomSheetBehavior<*>?) {
        behavior?.peekHeight = getWindowHeight() * 30 / 100
        behavior?.isHideable = isHideable
        behavior?.halfExpandedRatio = 0.3f
        behavior?.state = BottomSheetBehavior.STATE_HALF_EXPANDED
    }

    override fun createFragment(): Fragment {
        return CommonViewsFragment()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        view.findViewById<LinearLayout>(com.design.chili.R.id.ll_content).apply {
//            setBackgroundResource(com.design.chili.R.drawable.chili_bg_rounded_bottom_sheet_gray)
//        }
    }
}