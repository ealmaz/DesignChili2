package com.design2.app.extensions

import kg.devcats.chili3.R
import kg.devcats.chili3.view.card.AccountCardView

fun AccountCardView.setNonAuthorizedState(title: String?, subTitle: CharSequence?) {
    setTitleIcon(drawableRes = null)
    setTitleTextAppearance(R.style.Chili_H16_Primary_Bold)
    setTitle(title)
    setSubtitleTextAppearance(R.style.Chili_H14_Secondary)
    setChevronVisibility(isChevronVisible = false)
    setTitleAddition(charSequence = null)
    setActionButtonIcon(drawableRes = null)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitle(subTitle)
}

fun AccountCardView.setNonIdentifiedState(title: String?) {
    setTitleIcon(com.design2.app.R.drawable.non_identified)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = false)
    setTitle(title)
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = false)
    setTitleTextAppearance(R.style.Chili_H14_AccentSecondary_700)
    setSubtitle(charSequence = null)
}

fun AccountCardView.setIdentificationInProcessState(title: String?) {
    setTitleIcon(com.design2.app.R.drawable.ic_dentification_in_process)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = false)
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = false)
    setTitleTextAppearance(R.style.Chili_H14_Secondary_700)
    setSubtitle(charSequence = null)
    setTitle(title)
}

fun AccountCardView.setFavoritePaymentAmountAvailableState(
    title: String?,
    titleAddition: String?,
    isToggleHiddenState: Boolean = false,
    subTitle: CharSequence?
) {
    setTitleIcon(R.drawable.chili_ic_star_20)
    setTitleTextAppearance(R.style.Chili_H14_Primary_Bold)
    setTitleAdditionTextAppearance(R.style.Chili_H14_Value)
    setChevronVisibility(isChevronVisible = true)
    setTitleAddition(titleAddition)
    setTitle(title)
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = true)
    setToggleIconState(isToggleHiddenState)
    setSubtitleTextAppearance(R.style.Chili_H14_Primary_Bold)
    setSubtitle(subTitle)
}

fun AccountCardView.setBankSyncState() {
    setTitleIcon(com.design2.app.R.drawable.ic_dentification_in_process)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = false)
    setTitle("Продолжить \nперенос данных")
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = false)
    setTitleTextAppearance(R.style.Chili_H14_Primary_700)
    setSubtitle(charSequence = null)
}

fun AccountCardView.setFavoritePaymentAmountUnavailableState(
    title: String?,
    titleAddition: String?,
    subTitle: CharSequence?
) {
    setTitleIcon(R.drawable.chili_ic_star)
    setTitleTextAppearance(R.style.Chili_H14_Primary_Bold)
    setTitleAdditionTextAppearance(R.style.Chili_H14_Value)
    setChevronVisibility(isChevronVisible = true)
    setTitleAddition(titleAddition)
    setTitle(title)
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitleTextAppearance(R.style.Chili_H14_Warning_Bold)
    setSubtitle(subTitle)
}

fun AccountCardView.setNoFavoritePaymentAmountState(title: String?, subTitle: CharSequence?) {
    setTitleIcon(com.design2.app.R.drawable.chili_ic_star_outlined)
    setTitleTextAppearance(R.style.Chili_H14_Primary_Bold)
    setTitleAddition(charSequence = null)
    setChevronVisibility(isChevronVisible = true)
    setTitle(title)
    setActionButtonIcon(com.design2.app.R.drawable.chili_ic_card)
    setToggleIconVisibility(isIconVisible = false)
    setSubtitleTextAppearance(R.style.Chili_H14_Warning_Bold)
    setSubtitle(subTitle)
}