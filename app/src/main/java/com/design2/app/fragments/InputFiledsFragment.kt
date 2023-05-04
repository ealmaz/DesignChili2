package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import com.design2.app.MainActivity
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentInputFieldsBinding
import com.design2.chili2.view.input.otp.OnOtpCompleteListener
import com.design2.chili2.view.input.otp.OtpItemState

class InputFields : BaseFragment<FragmentInputFieldsBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).setUpHomeEnabled(true)
        vb.field1.setupClearTextButton()
        vb.field2Mask.setupNewMask("12313123123XXXXXXXXX")
        vb.field2Mask.requestFocus()
        vb.field0.setMaxLength(3)


        vb.otp.apply {
            setActionText("Сбросить пароль")
            setActionTextAppearance(com.design2.chili2.R.style.Chili_ComponentButtonText)
        }
        vb.otp.setOnOtpCompleteListener(object : OnOtpCompleteListener {
            override fun onOtpInputComplete(otp: String) {
                vb.otp.setupState(OtpItemState.ERROR)
                vb.otp.setMessageText("Неверный пароль")
            }

            override fun onInput(text: String?) {
                vb.otp.setupState(OtpItemState.INACTIVE)
                vb.otp.setMessageText("")
            }
        })
    }

    override fun inflateViewBinging(): FragmentInputFieldsBinding {
        return FragmentInputFieldsBinding.inflate(layoutInflater)
    }
}