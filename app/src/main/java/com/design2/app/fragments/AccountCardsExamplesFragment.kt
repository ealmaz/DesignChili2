package com.design2.app.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
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

        vb.acv8.onToggleChange {
            println("toggle $it")
        }
        vb.acv8.onMainContentClick {
            println("main click")
        }
        vb.acv8.onActionButtonClick {
            println("action ")
        }
        vb.acvDynamic.setFavoritePaymentAmountAvailableState(
            "Карта Visa",
            "•••• 1234",
            isToggleHiddenState = true,
            "3 350,00 c"
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
                "3 350,00 c"
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