package com.design2.chili2.view.image

import android.animation.AnimatorInflater
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.airbnb.lottie.LottieAnimationView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewMediaBinding
import com.design2.chili2.extensions.applyForegroundFromTheme
import com.design2.chili2.extensions.setUrlImageByCoil
import com.design2.chili2.util.AnimationData
import com.design2.chili2.util.MediaType
import com.design2.chili2.util.LottieAnimationHandler
import com.design2.chili2.view.card.BaseCardView

class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.mediaViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_MediaViewStyle
) : BaseCardView(context, attrs, defStyleAttr) {

    private lateinit var vb: ChiliViewMediaBinding

    private var lottieAnimationHandler: LottieAnimationHandler? = null
    private var image: ImageView? = null
    private var imageSize: Pair<Int, Int> = Pair(0, 0)

    override val styleableAttrRes: IntArray = R.styleable.MediaView

    override val rootContainer: View
        get() = vb.root

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewMediaBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        getLayoutDimension(R.styleable.MediaView_android_layout_width, 0).let {
            imageSize = it to imageSize.second
        }
        getLayoutDimension(R.styleable.MediaView_android_layout_height, 0).let {
            imageSize = imageSize.first to it
        }
        getInteger(R.styleable.MediaView_type, -1).takeIf { it != -1 }?.let {
            setMedia(getString(R.styleable.MediaView_mediaSrc), it)
        }
        getInteger(R.styleable.MediaView_android_scaleType, ScaleType.FIT_CENTER.ordinal).let {
            setScaleType(it)
        }
        getBoolean(R.styleable.MediaView_android_adjustViewBounds, false).let {
            setAdjustViewBounds(it)
        }
        getDimension(R.styleable.MediaView_cornerRadius, -1f)
            .takeIf { it != -1f }?.let { radius = it }
        getResourceId(R.styleable.MediaView_android_stateListAnimator, -1)
            .takeIf { it != -1 }.let { setupStateListAnimator(it) }
    }

    fun setMedia(src: String?, type: Int) {
        image = when (type) {
            MediaType.IMAGE_URL.value -> createImageView(src)
            MediaType.LOTTIE_URL.value -> createLottieView(src)
            else -> null
        }
    }

    private fun createLottieHandler(view: LottieAnimationView, src: String?) {
        lottieAnimationHandler = LottieAnimationHandler(view)
        lottieAnimationHandler?.addToAnimationQueue(AnimationData(animationUrl = src, isInfiniteRepeat = true))
    }

    private fun createImageView(src: String?) =
        ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setUrlImageByCoil(src, imageSize.first, imageSize.second)
        }.also(::addMediaView)

    private fun createLottieView(src: String?) =
        LottieAnimationView(context).apply {
            layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
            setCacheComposition(true)
            enableMergePathsForKitKatAndAbove(true)
        }
            .also(::addMediaView)
            .also { createLottieHandler(it, src) }

    private fun addMediaView(view: View) = with(vb.root) {
        removeAllViews()
        addView(view)
    }

    fun setScaleType(scaleType: Int) {
        image?.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        image?.adjustViewBounds = isAdjusted
    }

    fun setupStateListAnimator(animatorRes: Int?) {
        stateListAnimator =
            if (animatorRes != null) {
                foreground = null
                AnimatorInflater.loadStateListAnimator(context, animatorRes)
            } else {
                applyForegroundFromTheme(context, android.R.attr.selectableItemBackground)
                null
            }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lottieAnimationHandler = null
    }

}

