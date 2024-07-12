package kg.devcats.chili3.view.common

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewPinMatcherBinding

class PinMatcherView @JvmOverloads constructor(
    context: Context, attributeSet: AttributeSet? = null
) : LinearLayout(context, attributeSet) {

    private val vb: ChiliViewPinMatcherBinding = ChiliViewPinMatcherBinding.inflate(LayoutInflater.from(context), this, true)

    fun onEnter(enteredLength: Int) = with(vb) {
        imgPin1.setImageResource(if (enteredLength > 0) R.drawable.chili_ic_pin_matcher_item_selected else R.drawable.chili_ic_pin_matcher_item_unselected)
        imgPin2.setImageResource(if (enteredLength > 1) R.drawable.chili_ic_pin_matcher_item_selected else R.drawable.chili_ic_pin_matcher_item_unselected)
        imgPin3.setImageResource(if (enteredLength > 2) R.drawable.chili_ic_pin_matcher_item_selected else R.drawable.chili_ic_pin_matcher_item_unselected)
        imgPin4.setImageResource(if (enteredLength > 3) R.drawable.chili_ic_pin_matcher_item_selected else R.drawable.chili_ic_pin_matcher_item_unselected)
    }

    fun animateSuccess(onAnimationEnd: () -> Unit = {}) {
        val longWave = AnimationUtils.loadAnimation(context, R.anim.long_wave)
        val shortWave = AnimationUtils.loadAnimation(context, R.anim.short_wave)
        longWave.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {
                changeLockItemsDrawable(R.drawable.chili_ic_pin_matcher_item_correct)
            }

            override fun onAnimationRepeat(arg0: Animation) {}
            override fun onAnimationEnd(arg0: Animation) {
                onAnimationEnd()
            }
        })
        with(vb) {
            imgPin1.startAnimation(shortWave)
            imgPin2.startAnimation(longWave)
            imgPin3.startAnimation(shortWave)
            imgPin4.startAnimation(longWave)
        }
    }

    private fun changeLockItemsDrawable(drawableRes: Int) {
        with(vb) {
            imgPin1.setImageResource(drawableRes)
            imgPin2.setImageResource(drawableRes)
            imgPin3.setImageResource(drawableRes)
            imgPin4.setImageResource(drawableRes)
        }
    }

     fun animateError(onAnimationEnd: () -> Unit = {}) {
        val animation = AnimationUtils.loadAnimation(context, R.anim.shake)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(arg0: Animation) {
                changeLockItemsDrawable(R.drawable.chili_ic_pin_matcher_item_wrong)
            }

            override fun onAnimationRepeat(arg0: Animation) {}

            override fun onAnimationEnd(arg0: Animation) {
                onAnimationEnd()
            }
        })

        with(vb) {
            imgPin1.startAnimation(animation)
            imgPin2.startAnimation(animation)
            imgPin3.startAnimation(animation)
            imgPin4.startAnimation(animation)
        }
    }

    fun resetEnterState() {
        changeLockItemsDrawable(R.drawable.chili_ic_pin_matcher_item_unselected)
    }
}