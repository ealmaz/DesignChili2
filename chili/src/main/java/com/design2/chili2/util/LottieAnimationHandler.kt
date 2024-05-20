package com.design2.chili2.util

import android.animation.Animator
import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.annotation.RawRes
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieComposition
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable.INFINITE
import com.airbnb.lottie.LottieResult
import java.lang.ref.WeakReference
import java.util.LinkedList

class LottieAnimationHandler(private val animationView: LottieAnimationView) {

    private var isAnimating = false
    private val animationQueue = LinkedList<AnimationData>()

    init {
        animationView.addAnimatorListener(object: Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationCancel(animation: Animator) {}

            override fun onAnimationEnd(animation: Animator) {
                isAnimating = false
                startNextAnimation()
            }
            override fun onAnimationRepeat(animation: Animator) { startNextAnimation() }
        })
    }

    fun addToAnimationQueue(animation: AnimationData) {
        if (!animationQueue.contains(animation)) animationQueue.add(animation)
        if (!isAnimating) startNextAnimation()
    }

    private fun startNextAnimation() {
        val animData = animationQueue.pollFirst() ?: return
        isAnimating = true
        val repeatCount = if (animData.isInfiniteRepeat) INFINITE else 0

        when {
            animData.animationRes != null -> setLottieAnimation(animData.animationRes, repeatCount)
            !animData.animationJson.isNullOrBlank() -> setLottieAnimation(animData.animationJson, repeatCount)
            !animData.animationUrl.isNullOrBlank() -> setUrlLottieAnimation(animData.animationUrl, repeatCount)
        }
    }

    private fun setLottieAnimation(@RawRes lottieRes: Int, mRepeatCount: Int) {
        animationView.apply {
            LottieCompositionFactory.fromRawResSync(animationView.context, lottieRes)?.let { result ->
                result.value?.let { setComposition(it) }
            }
            repeatCount = mRepeatCount
            playAnimation()
        }
    }

    private fun setLottieAnimation(lottieString: String, mRepeatCount: Int) {
        animationView.apply {
            LottieCompositionFactory.fromJsonStringSync(lottieString, null)?.let { result ->
                result.value?.let { setComposition(it) }
            }
            repeatCount = mRepeatCount
            playAnimation()
        }
    }

    private fun setUrlLottieAnimation(lottieString: String, mRepeatCount: Int) {
        val weakContext: WeakReference<Context> = WeakReference(animationView.context)
        Thread {
            val result: LottieResult<LottieComposition> =
                LottieCompositionFactory.fromUrlSync(weakContext.get(), lottieString)
            animationView.apply {
                result.value?.let {
                    Handler(Looper.getMainLooper()).post {
                        setComposition(it)
                        repeatCount = mRepeatCount
                        playAnimation()
                    }
                }
            }
        }.start()
    }
}

data class AnimationData(
    val animationRes: Int? = null,
    val animationJson: String? = null,
    val animationUrl: String? = null,
    val isInfiniteRepeat: Boolean = false
)