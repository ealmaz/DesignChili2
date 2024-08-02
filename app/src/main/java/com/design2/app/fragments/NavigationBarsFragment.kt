package com.design2.app.fragments

import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentNavigationBarsBinding

class NavigationBarsFragment : BaseFragment<FragmentNavigationBarsBinding>() {
    override fun inflateViewBinging(): FragmentNavigationBarsBinding {
        return FragmentNavigationBarsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(vb) {
            val scaleLabel = "Items animation duration:"
            val durationLabel = "Items scale on animation:"
            tvDuration.text = "$scaleLabel 68"
            tvScale.text = "$durationLabel 1.3"

            sc.setOnCheckedChangeListener { _, isChecked ->
                clBtn.setItemsAnimation(isChecked)
            }

            sbDuration.setOnSeekBarChangeListener { _, progress, _ ->
                tvDuration.text = "$durationLabel $progress"
                clBtn.setAnimatedItemDuration(progress)
            }

            sbScale.setOnSeekBarChangeListener { _, progress, _ ->
                val scale = 1 + progress / 10f
                tvScale.text = "$scaleLabel $scale"
                clBtn.setAnimatedItemScale(scale)
            }
        }

        vb.clBtn.setOnItemSelectedListener {
            println("selected ${it.title}")
            true
        }
        vb.clBtn.setOnIconClickListener {
            println("icon")
        }
    }

    private fun SeekBar.setOnSeekBarChangeListener(
        onProgressChanged: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit
    ) {
        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }
}