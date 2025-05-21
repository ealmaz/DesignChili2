package com.design2.app.fragments

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.text.parseAsHtml
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentDemoTextFontScaleBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import com.google.android.material.slider.Slider
import kg.devcats.chili3.view.cells.CardCellView

class DemoTextFontScaleFragment : BaseFragment<FragmentDemoTextFontScaleBinding>() {

    override fun inflateViewBinging(): FragmentDemoTextFontScaleBinding {
        return FragmentDemoTextFontScaleBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vb.cardcell.setCardState()
        vb.cardcellBlocked.setCardBlockState()

        val steps = listOf(
            0.8f,
            0.9f,
            1.0f,
            1.1f,
            1.3f,
            1.5f,
            1.7f,
            2.0f,
        )

        with(vb.changeSizeSlider) {
            valueFrom = 0f
            valueTo = (steps.size - 1).toFloat()
            stepSize = 1f
            value = steps.indexOf(resources.configuration.fontScale).toFloat()
            vb.sliderValue.text = "Значение FontScale: ${resources.configuration.fontScale}x"

            setLabelFormatter { value ->
                val index = value.toInt().coerceIn(0, steps.lastIndex)
                "${steps[index]}x"
            }

            addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
                override fun onStartTrackingTouch(slider: Slider) {}

                override fun onStopTrackingTouch(slider: Slider) {
                    val index = slider.value.toInt().coerceIn(0, steps.lastIndex)
                    val realValue = steps[index]
                    applyFontScale(realValue)
                }
            })
        }
    }

    private fun CardCellView.setCardState() {
        setIcon(com.design2.app.R.drawable.ic_card_default)
        setTitle("Банковский счет")
        setSubtitle("****2345")
        val text = "1212 <u>c</u>".parseAsHtml()
        setAdditionalText(text)
        setOnSingleClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun CardCellView.setCardBlockState() {
        setIcon(com.design2.app.R.drawable.ic_card_default)
        setTitle("Банковский счет")
        setSubtitle("В блоке")
        val text = "1212 <u>c</u>".parseAsHtml()
        setAdditionalText(text)
        setIsBlocked(true)
        setOnSingleClickListener {
            Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    fun applyFontScale(value: Float) {
        val prefs = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putFloat("fontScale", value).apply()
        requireActivity().recreate()
    }
}