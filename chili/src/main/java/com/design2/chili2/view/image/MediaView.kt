package com.design2.chili2.view.image

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.airbnb.lottie.LottieAnimationView
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewMediaBinding
import com.design2.chili2.extensions.dpF
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.util.AnimationData
import com.design2.chili2.util.MediaType
import com.design2.chili2.util.LottieAnimationHandler

class MediaView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chiliMediaViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_MediaViewStyle
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var vb: ChiliViewMediaBinding

    private var lottieAnimationHandler: LottieAnimationHandler? = null
    private var image: ImageView? = null
    private var imageSize: Pair<Int, Int> = Pair(0, 0)
    private var cornerRadius: Float = 12.dpF
    private val path = Path()

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    fun initView(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        inflateView(context)
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    fun inflateView(context: Context) {
        vb = ChiliViewMediaBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.MediaView, defStyleAttr, defStyleRes).run {
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
            getDimension(R.styleable.MediaView_cornerRadius, cornerRadius).let {
                setCornerRadius(it)
            }
            recycle()
        }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        path.reset()
        path.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, Path.Direction.CW)
    }

    override fun dispatchDraw(canvas: Canvas) {
        val save = canvas.save()
        canvas.clipPath(path)
        super.dispatchDraw(canvas)
        canvas.restoreToCount(save)
    }

    fun setCornerRadius(radius: Float) {
        cornerRadius = radius
        path.reset()
        path.addRoundRect(0f, 0f, width.toFloat(), height.toFloat(), cornerRadius, cornerRadius, Path.Direction.CW)
        invalidate()
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
            setImageByUrl(src, imageSize.first, imageSize.second)
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

    override fun setOnClickListener(listener: OnClickListener?) {
        vb.root.setOnClickListener(listener)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        lottieAnimationHandler = null
    }

}

