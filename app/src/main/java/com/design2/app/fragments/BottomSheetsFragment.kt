package com.design2.app.fragments

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.MainActivity
import com.design2.app.R
import com.design2.app.adapter.SampleRadioItem
import com.design2.app.adapter.SelectorAdapter
import com.design2.app.adapter.SimpleTextRecyclerViewAdapter
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FrgmentBottomSheetsBinding
import com.design2.chili2.extensions.dp
import com.design2.chili2.view.modals.base.BaseFragmentBottomSheetDialogFragment
import com.design2.chili2.view.modals.bottom_sheet.*
import com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet.Option
import com.design2.chili2.view.modals.bottom_sheet.serach_bottom_sheet.SearchSelectorBottomSheet
import com.design2.chili2.view.modals.bottom_sheet_constructor.BottomSheetConfig
import com.design2.chili2.view.modals.bottom_sheet_constructor.Margins
import com.design2.chili2.view.modals.bottom_sheet_constructor.Size
import com.design2.chili2.view.modals.bottom_sheet_constructor.buildBottomSheet
import com.design2.chili2.view.modals.in_app.InAppPushBottomSheet
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kg.devcats.chili3.view.container.BottomSheetWithRecycler

class BottomSheetsFragment : BaseFragment<FrgmentBottomSheetsBinding>() {

    val list: RecyclerView by lazy {
        RecyclerView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = SimpleTextRecyclerViewAdapter(requireContext()).apply {
                updateList(listOf(
                    "Назначение платежа:\nBalance",
                    "Реквизит:\n+996777999000",
                    "Сумма:\n5000,00 c",
                    "Комиссия:\n151,00 с",
                    "Оплачено: \n5000",
                    "Способ оплаты:\n" + "VISA •••• 6445",
                    "Дата совершения перевода:\n" + "12.02.2020 / 14:13:27",
                    "Квитанция:\n" + "123681263786128"
                )

                )
            }
        }
    }


    var customBS1: BottomSheetDialogFragment? = null

    val customBS2: BottomSheetDialogFragment by lazy {
        requireContext().buildBottomSheet(BottomSheetConfig()) {
            block(Gravity.CENTER, LinearLayout.VERTICAL) {
                image(com.design2.chili2.R.drawable.chili_ic_done, size = Size(
                    widthMatchParent = true, heightDimenRes = com.design2.chili2.R.dimen.view_64dp
                ), imageUrl = "https://minio.o.kg/media-service/Akcha_bulak/ios_dark_rus.png")
                customView(list)
            }
            block(Gravity.CENTER, LinearLayout.HORIZONTAL) {
                button(
                    textCharSequence = "Ясно",
                    buttonStyle = com.design2.chili2.R.style.Chili_ButtonStyle_Additional,
                    margins = Margins(top = 16.dp, left = 16.dp, right = 8.dp, bottom = 16.dp)
                ) {
                    Toast.makeText(requireContext(), "ясно", Toast.LENGTH_SHORT).show()
                }
                button(
                    textCharSequence = "Понятно",
                    buttonStyle = com.design2.chili2.R.style.Chili_ButtonStyle_Primary,
                    margins = Margins(top = 16.dp, right = 16.dp, bottom = 16.dp)
                ) {
                    Toast.makeText(requireContext(), "Понятно", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customBS1 = requireContext().buildBottomSheet(BottomSheetConfig()) {
            block(Gravity.CENTER, LinearLayout.VERTICAL) {
                image(
                    R.drawable.ic_cat,
                    margins = Margins(top = 10.dp, bottom = 16.dp)
                )
                text(
                    textCharSequence = "Заголовок содержит в себе до 60\nсимволов и может быть в 2 строки",
                    textAppearance = com.design2.chili2.R.style.Chili_H8_Primary_Bold,
                    isCentered = true,
                    margins = Margins(left = 16.dp, right = 16.dp, bottom = 8.dp)
                )
                text(
                    textCharSequence = "Описание содержит до 113 символов, что очень приятно, потому что теперь можно написать его аж в целых три строки и развернуть любую мысль",
                    textAppearance = com.design2.chili2.R.style.Chili_H8_Primary,
                    isCentered = true,
                    margins = Margins(left = 16.dp, right = 16.dp)
                )
            }

            block(Gravity.CENTER, LinearLayout.VERTICAL) {
                button(
                    textCharSequence = "Ясно",
                    buttonStyle = com.design2.chili2.R.style.Chili_ButtonStyle_Additional,
                    margins = Margins(top = 16.dp, left = 16.dp, right = 16.dp, bottom = 12.dp)
                ) {
                    Toast.makeText(requireContext(), "ясно", Toast.LENGTH_SHORT).show()
                }
                button(
                    textCharSequence = "Понятно",
                    buttonStyle = com.design2.chili2.R.style.Chili_ButtonStyle_Primary,
                    margins = Margins(left = 16.dp, right = 16.dp, bottom = 12.dp)
                ) {
                    Toast.makeText(requireContext(), "Понятно", Toast.LENGTH_SHORT).show()
                }
            }
        }


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
                .setMessage("LALALLAТекстовый блок, который содержит 120 символов, и этого количества, должно хватить для информации в четыре строки.d dqwdqwdiwqdjiqwd qwjdiqwdjiqwjdiq qwidqiwdjiqwdj qwndiqwndqiw iqwdiqwndiqwndi qwndiqwdniqwdn qwndiqwndiqw")
                .setPrimaryButton("Понятно" to {Toast.makeText(context, "Понятно", Toast.LENGTH_SHORT).show()})
                .setSecondaryButton("Ясно" to {Toast.makeText(context, "Ясно", Toast.LENGTH_SHORT).show()})
                .setTextMaxLines(7)
                .setMessageTextAppearance(com.design2.chili2.R.style.Chili_H4_Error)
                .setHeaderText("Pfgybnt lfyyst")
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
                .setIsDraggable(false)
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
        vb.chili3NewCells.setOnClickListener {
            val bottomSheet = FragmentBottomSheet.Builder()
                .setContentFragment(NewCellViewsFragment())
                .setState(BottomSheetBehavior.STATE_EXPANDED)
                .setDrawableVisible(false)
                .setInnerTopDrawableVisible(true)
                .build()
            bottomSheet.show(childFragmentManager)
        }
        vb.chili3BottomSheetWRecview.setOnClickListener {
            var bsh: BottomSheetWithRecycler? = null
            val adapter = SelectorAdapter(listOf(
                SampleRadioItem("Visa","···· 1234"),
                SampleRadioItem("Visa o!Dengi","···· 12421"),
                SampleRadioItem("Банковский счет","···· 2341"),
                SampleRadioItem("ELCART ЭЛКАП","···· 1234"),
                SampleRadioItem("Visa","···· 1234"),
                SampleRadioItem("Visa o!Dengi","···· 12421"),
                SampleRadioItem("Банковский счет","···· 2341")
            )){ selectedData ->
                Toast.makeText(context, "$selectedData", Toast.LENGTH_SHORT).show()
                // bsh?.dismiss()
            }
            bsh = BottomSheetWithRecycler.Builder()
                .setTitle("Это боттомщит где вы можете засетить свой адаптер к ресайклу")
                .setSubtitle(getString(R.string.app_name))
                .setAdapter(adapter)
                .setSubtitleAppearance(com.design2.chili2.R.style.Chili_H4_Error)
                .build()
            bsh.show(childFragmentManager)

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
        vb.detailCustom.setOnClickListener {
            DetailedInfoBottomSheet.Builder()
                .setIcon(R.drawable.ic_cat)
                .setMessage(R.string.agree_terms)
                .setIconSizeDimenRes(com.design2.chili2.R.dimen.view_64dp)
                .setIsTopDrawableVisible(false)
                .setPrimaryButton("Start" to {
                    this.dismiss()
                })
                .setSecondaryButton("Later" to {
                    this.dismiss()
                })
                .setOnDismissCallback {
                    Toast.makeText(requireContext(), "OnDismiss", Toast.LENGTH_SHORT).show()
                }
                .setTextCentered(true)
                .build()
                .show(childFragmentManager)
        }


        vb.bsConstructor.setOnClickListener {
            customBS1?.show(childFragmentManager, null)
        }


        vb.bsConstructor2.setOnClickListener {
            customBS2.show(childFragmentManager, null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        customBS1 = null
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