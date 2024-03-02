package com.design2.chili2.view.stories


import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.setPadding
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoryBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlin.collections.ArrayList


class StoryView : ConstraintLayout {

    private var mContext: Context? = null

    private var currentStoryIndex: Int = 0
    private var currentStory: StoryModel? = null
    private var stories: ArrayList<StoryModel> = arrayListOf()
    private var progressBars: ArrayList<ProgressBar> = arrayListOf()
    private var timeRemaining = 0L
    private var timeFinished = 0L
    private var timer: CountDownTimer? = null
    private var listener: StoryListener? = null
    private var isPaused: Boolean = false
    private lateinit var binding: ChiliViewStoryBinding
    private lateinit var gestureDetector: GestureDetectorCompat

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(
        context,
        attrs
    ) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private var exoPlayer: ExoPlayer? = null

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build().also {
            binding.storyVideoView.player = exoPlayer
        }
        binding.storyVideoView.visible()
    }

    private fun init(context: Context?) {
        mContext = context
        binding = ChiliViewStoryBinding.inflate(LayoutInflater.from(context), this, true)
        gestureDetector = GestureDetectorCompat(context!!, StoryGestureListener())
    }

    private fun next(storyModel: StoryModel) {
        pauseTimer()
        timeRemaining = 0
        currentStory = storyModel

        with(binding) {
            if (storyModel.title != null) {
                storyTitleView.visible()
                storyTitleView.text = storyModel.title
            } else storyTitleView.gone()

            if (storyModel.description != null) {
                storySubtitleView.visible()
                storySubtitleView.text = storyModel.description
            } else storySubtitleView.gone()
        }

        playContentByStoryType(storyModel.storyType)
    }

    private fun playContentByStoryType(storyType: StoryType) {
        with(binding) {
            storyImageView.gone()
            storyVideoView.gone()
            storyLottieView.gone()
        }
        when(storyType) {
            StoryType.LOTTIE -> playLottieAnimation()
            StoryType.VIDEO -> playVideo()
            StoryType.IMAGE -> loadImage()
        }
    }

    private fun playLottieAnimation() {
        with(binding.storyLottieView) {
            visible()
            timer = getTimer()
            addAnimatorListener(object : AnimatorListener{
                override fun onAnimationStart(p0: Animator?) {
                    timer?.start()
                }

                override fun onAnimationEnd(p0: Animator?) {
                    timer?.cancel()
                }

                override fun onAnimationCancel(p0: Animator?) {
                    timer?.cancel()
                }

                override fun onAnimationRepeat(p0: Animator?) {

                }

            })
            setAnimationFromUrl(currentStory?.lottie)
            repeatCount = LottieDrawable.INFINITE
            playAnimation()
        }
    }

    private fun pauseLottieAnimation() {
        with(binding.storyLottieView) {
            if (isAnimating) pauseAnimation()
        }
    }

    private fun resumeLottieAnimation() {
        with(binding.storyLottieView) {
            if (!isAnimating) resumeAnimation()
        }
    }

    private fun playVideo() {
        initializePlayer()
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY && exoPlayer?.playWhenReady == true) {
                    timer = getTimer()
                    timer?.start()
                }
            }
        })
        currentStory?.video?.let { videoUrl ->
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer?.let {
                it.setMediaItem(mediaItem)
                it.prepare()
                it.playWhenReady = true
            }
            binding.storyVideoView.player = exoPlayer
        }
    }

    private fun pauseVideo() {
        exoPlayer?.playWhenReady = false
    }

    private fun resumeVideo() {
        exoPlayer?.playWhenReady = true
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun loadImage() {
        timer = getTimer()
        binding.storyImageView.apply {
            visible()
            setImageByUrl(currentStory?.image)
        }
        listener?.setImageFor(currentStoryIndex, currentStory, binding.storyImageView)
        timer?.start()
        binding.touchableView.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)


            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (binding.root.translationY > 100) {
                        finishWithAnimation()
                    } else {
                        binding.root.animate().translationY(0f).setDuration(200).withEndAction {
                            resumeTimer()

                        }.start()
                    }
                }
            }

            true
        }

    }

    private fun getTimer(time: Long = 0) = object :
        CountDownTimer(if (time > 0) time else currentStory?.time?.times(1000.toLong()) ?: 0, 10) {
        override fun onFinish() {
            if (currentStoryIndex < stories.size - 1) {
                listener?.onFinished(currentStoryIndex)
                currentStoryIndex++
                next(stories[currentStoryIndex])
            } else {
                listener?.onAllFinished()
            }
        }

        override fun onTick(millisUntilFinished: Long) {
            timeRemaining = millisUntilFinished
            val numerator =
                (currentStory?.time?.times(1000.toLong()) ?: 0) - millisUntilFinished
            timeFinished = numerator

            progressBars[currentStoryIndex].progress =
                (numerator * 100 / (currentStory?.time?.times(1000.toLong()) ?: 0)).toInt()
        }
    }

    fun load(
        stories: ArrayList<StoryModel> = arrayListOf(),
        listener: StoryListener? = null
    ) {
        with(binding) {
            closeButton.setOnClickListener { listener?.onAllFinished() }
            progressBarsContainer.removeAllViews()
        }


        for (story in stories) {
            val progressBar =
                ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                            .apply {
                                setPadding(4, 0, 4, 0)
                            }
                    max = 100
                    progress = 0
                    progressDrawable = context.getDrawable(R.drawable.chili_story_progress_bar)
                }
            progressBars.add(progressBar)
            binding.progressBarsContainer.addView(progressBar)
        }
        invalidate()
        binding.progressBarsContainer.visibility = View.VISIBLE
        this.stories = stories
        this.listener = listener
        if (this.stories.isNotEmpty()) {
            listener?.onStart()
            next(stories[0])
        }
    }

    inner class StoryGestureListener : GestureDetector.SimpleOnGestureListener() {
        private var initialTouchY: Float = 0f

        override fun onDown(e: MotionEvent): Boolean {
            initialTouchY = e.rawY
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            e?.let {
                if (it.x < binding.touchableView.width / 2) {
                    moveToPreviousSegment()
                } else {
                    moveToNextSegment()
                }
            }
            return super.onSingleTapUp(e)
        }

        override fun onScroll(
            e1: MotionEvent,
            e2: MotionEvent,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            val deltaY = e2.rawY - initialTouchY
            if (deltaY > 0) {
                binding.root.translationY = deltaY
                return true
            }
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            isPaused = true
            pauseTimer()
        }
    }

    private fun moveToNextSegment() {
        if (currentStoryIndex < this.stories.size - 1) {
            this.progressBars[currentStoryIndex].progress = 100
            currentStoryIndex++
            next(this.stories[currentStoryIndex])
        } else {
            finishWithAnimation()
        }
    }

    private fun moveToPreviousSegment() {
        if (currentStoryIndex > 0) {
            this.progressBars[currentStoryIndex].progress = 0
            currentStoryIndex--
            next(this.stories[currentStoryIndex])
        }
    }

    private fun finishWithAnimation() {
        this@StoryView.animate()
            .translationY(0f)
            .setDuration(200)
            .withEndAction {
                listener?.onAllFinished()
            }.start()
    }

    private fun pauseTimer() {
        timer?.cancel()
        timer = null

        when(currentStory?.storyType) {
            StoryType.VIDEO -> pauseVideo()
            StoryType.LOTTIE -> pauseLottieAnimation()
            else -> {}
        }
    }

    private fun resumeTimer() {
        if (isPaused) {
            isPaused = false
            timer = getTimer(timeRemaining).also { it.start() }

            when(currentStory?.storyType) {
                StoryType.VIDEO -> resumeVideo()
                StoryType.LOTTIE -> resumeLottieAnimation()
                else -> {}
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.storyLottieView.cancelAnimation()
        timer?.cancel()
        timer = null
    }
}

interface StoryListener {
    fun onAllFinished()
    fun onFinished(index: Int)
    fun onStart()
    fun setImageFor(index: Int, story: StoryModel?, imageView: ImageView)
}