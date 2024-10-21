package com.design2.app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.text.parseAsHtml
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentAccountCardExamplesBinding
import com.design2.app.extensions.setFavoritePaymentAmountAvailableState
import com.design2.app.extensions.setFavoritePaymentAmountUnavailableState
import com.design2.app.extensions.setIdentificationInProcessState
import com.design2.app.extensions.setNoFavoritePaymentAmountState
import com.design2.app.extensions.setNonAuthorizedState
import com.design2.app.extensions.setNonIdentifiedState
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering

class AccountCardsExamplesFragment : BaseFragment<FragmentAccountCardExamplesBinding>() {

    override fun inflateViewBinging(): FragmentAccountCardExamplesBinding {
        return FragmentAccountCardExamplesBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)

        vb.acv.apply {
            setNoFavoritePaymentAmountState(
                "Избранный счет ",
                "Временно недоступен \nВременно недоступен"
            )
        }

        vb.acv8.apply {
            setToggleIconState(isHiddenState = false)
            setSubtitle("1212 <u>c</u>".parseAsHtml())
            onToggleChange {
                println("toggle $it")
            }
            onMainContentClick {
                println("main click")
            }
            onActionButtonClick {
                println("action ")
            }
        }
        vb.acvDynamic.setFavoritePaymentAmountAvailableState(
            "Карта Visa",
            "•••• 1234",
            isToggleHiddenState = true,
            "1212 <u>c</u>".parseAsHtml()
        )

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setNonAuthorizedState(
                "Оплачивайте",
                "Более 2000 сервисов")
        },6000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setNonIdentifiedState("Пройдите \nИдентификацию")
        },3000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setFavoritePaymentAmountAvailableState(
                "Карта Visa",
                "•••• 1234",
                isToggleHiddenState = false,
                "1212 <u>c</u>".parseAsHtml()
            )
        },9000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setFavoritePaymentAmountUnavailableState(
                "Карта Visa Карта Visa \n Карта Visa Карта Visa ",
                "•••• 1234 \n•••• 1234",
                "Временно недоступен"
            )
        },12_000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setNoFavoritePaymentAmountState(
                "Избранный счет ",
                "Временно недоступен \nВременно недоступен"
            )
        },15_000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setIdentificationInProcessState("Ваша заявка \nв обработке")
        },18_000)

        Handler(Looper.getMainLooper()).postDelayed({
            vb.acvDynamic.setNonAuthorizedState(
                "Оплачивайте",
                "Более 2000 сервисов")
        },20_000)
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