package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
        vb.otp.setMessageText("ddweewrwerwerwerwerwerwerwerwerwerwerwerwerwerwerwe")
        vb.otp.setActionText("fwefwefwef")



        vb.otp.apply {
            setActionText("Сбросить пароль")
            setOnActionClickListener {
                Toast.makeText(requireContext(), "OnActionClick", Toast.LENGTH_SHORT).show()
            }
        }
        vb.otp.setOnOtpCompleteListener(object : OnOtpCompleteListener {
            override fun onOtpInputComplete(otp: String) {
                vb.otp.setupState(OtpItemState.ERROR)
                vb.otp.setMessageText("Неверный пароль")
                vb.otp.setActionText("Сбросить пароль")
                vb.otp.setActionTextEnabled(true)
            }

            override fun onInput(text: String?) {
                vb.otp.setupState(OtpItemState.INACTIVE)
                vb.otp.setMessageText("")
                vb.otp.setActionTextEnabled(false)
                vb.otp.setActionText("0:45")
            }
        })
    }

    override fun inflateViewBinging(): FragmentInputFieldsBinding {
        return FragmentInputFieldsBinding.inflate(layoutInflater)
    }
}