package com.design2.app.extensions

import com.design2.app.R
import kg.devcats.chili3.R as R3
import kg.devcats.chili3.view.card.AccountCardView

fun AccountCardView.setNonAuthorizedState(title: String?, subTitle: CharSequence?) {
    setTitleIcon(drawableRes = null)
    setTitleTextAppearance(R3.style.Chili_H16_Primary_Bold)
    setTitle(title)
    setSubtitleTextAppearance(R3.style.Chili_H14_Secondary)
    setChevronVisibility(isChevronVisible = false)
    setTitleAddition(charSequence = null)
    setActionButtonIcon(drawableRes = null)
    setActionButtonText("Войти")
    setActionButtonEndIcon(null)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitle(subTitle)
}

fun AccountCardView.setNonIdentifiedState(title: String?) {
    setTitleIcon(R.drawable.non_identified)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = false)
    setTitle(title)
    setActionButtonIcon(R.drawable.chili_ic_o_bank)
    setActionButtonText(null)
    setActionButtonEndIcon(R3.drawable.chili_ic_chevron_rounded_24)
    setToggleIconVisibility(isIconVisible = false)
    setTitleTextAppearance(R3.style.Chili_H14_AccentSecondary_700)
    setSubtitle(charSequence = null)
}

fun AccountCardView.setIdentificationInProcessState(title: String?) {
    setTitleIcon(R.drawable.ic_dentification_in_process)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = false)
    setActionButtonIcon(R.drawable.chili_ic_o_bank)
    setActionButtonText(null)
    setActionButtonEndIcon(R3.drawable.chili_ic_chevron_rounded_24)
    setToggleIconVisibility(isIconVisible = false)
    setTitleTextAppearance(R3.style.Chili_H14_Secondary_700)
    setSubtitle(charSequence = null)
    setTitle(title)
}

fun AccountCardView.setFavoritePaymentAmountAvailableState(
    title: String?,
    titleAddition: String?,
    isToggleHiddenState: Boolean = false,
    subTitle: CharSequence?
) {
    setTitleIcon(R3.drawable.chili_ic_star_20)
    setTitleTextAppearance(R3.style.Chili_H14_Primary_Bold)
    setTitleAdditionTextAppearance(R3.style.Chili_H14_Value)
    setChevronVisibility(isChevronVisible = true)
    setTitleAddition(titleAddition)
    setTitle(title)
    setActionButtonIcon(R.drawable.chili_ic_o_bank)
    setActionButtonText(null)
    setActionButtonEndIcon(R3.drawable.chili_ic_chevron_rounded_24)
    setToggleIconVisibility(isIconVisible = true)
    setToggleIconState(isToggleHiddenState)
    setSubtitleTextAppearance(R3.style.Chili_H14_Primary_Bold)
    setSubtitle(subTitle)
}

fun AccountCardView.setFavoritePaymentAmountUnavailableState(
    title: String?,
    titleAddition: String?,
    subTitle: CharSequence?
) {
    setTitleIcon(R3.drawable.chili_ic_star)
    setTitleTextAppearance(R3.style.Chili_H14_Primary_Bold)
    setTitleAdditionTextAppearance(R3.style.Chili_H14_Value)
    setChevronVisibility(isChevronVisible = true)
    setTitleAddition(titleAddition)
    setTitle(title)
    setActionButtonIcon(R.drawable.chili_ic_o_bank)
    setActionButtonText(null)
    setActionButtonEndIcon(R3.drawable.chili_ic_chevron_rounded_24)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitleTextAppearance(R3.style.Chili_H14_Warning_Bold)
    setSubtitle(subTitle)
}

fun AccountCardView.setNoFavoritePaymentAmountState(title: String?, subTitle: CharSequence?) {
    setTitleIcon(R.drawable.chili_ic_star_outlined)
    setTitleTextAppearance(R3.style.Chili_H14_Primary_Bold)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = true)
    setTitle(title)
    setActionButtonIcon(R.drawable.chili_ic_o_bank)
    setActionButtonText(null)
    setActionButtonEndIcon(R3.drawable.chili_ic_chevron_rounded_24)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitleTextAppearance(R3.style.Chili_H14_Warning_Bold)
    setSubtitle(subTitle)
}