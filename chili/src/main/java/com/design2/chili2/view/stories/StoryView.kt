package com.design2.chili2.view.stories


import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.content.Context
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.isVisible
import com.airbnb.lottie.LottieDrawable
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoryBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import kotlin.collections.ArrayList
import kotlin.math.max
import kotlin.math.min


class StoryView : ConstraintLayout {
    private lateinit var binding: ChiliViewStoryBinding

    private lateinit var gestureDetector: GestureDetectorCompat

    private var currentStoryIndex: Int = 0
    private var timeRemaining = 0L
    private var timeFinished = 0L
    private var isPaused: Boolean = false
    private var isAllStoriesFinished: Boolean = false

    private var currentStory: StoryModel? = null
    private var stories: ArrayList<StoryModel> = arrayListOf()

    private var timer: CountDownTimer? = null
    private var listener: StoryListener? = null

    private var progressBars: ArrayList<ProgressBar> = arrayListOf()
    private var exoPlayer: ExoPlayer? = null

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

    //region initialize

    private fun initializePlayer() {
        exoPlayer = ExoPlayer.Builder(context).build()
    }

    private fun init(context: Context?) {
        binding = ChiliViewStoryBinding.inflate(LayoutInflater.from(context), this, true)
        gestureDetector = GestureDetectorCompat(context!!, StoryGestureListener())
    }

    //endregion


    //region playing content
    @SuppressLint("ClickableViewAccessibility")
    private fun playNext(storyModel: StoryModel) {
        resetTimer()
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

            if (storyModel.buttonText != null) {
                additionalButton.apply {
                    visible()
                    text = storyModel.buttonText

                }
            }
            setupButton(storyModel)

            touchableView.setOnTouchListener { _, event ->
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

        playContentByStoryType(storyModel.storyType)
    }

    private fun setupButton(storyModel: StoryModel) {
        with(binding) {
            when {
                storyModel.buttonText != null && storyModel.buttonType == ButtonType.ADDITIONAL -> {
                    additionalButton.apply {
                        visible()
                        text = storyModel.buttonText
                    }
                    secondaryButton.gone()
                }
                storyModel.buttonText != null && storyModel.buttonType == ButtonType.SECONDARY -> {
                    secondaryButton.apply {
                        visible()
                        text = storyModel.buttonText
                    }
                    additionalButton.gone()
                }
                else -> {
                    additionalButton.gone()
                    secondaryButton.gone()
                }
            }
        }
    }

    private fun playContentByStoryType(storyType: StoryType) {
        with(binding) {
            storyImageView.gone()
            storyVideoView.gone()
            storyLottieView.gone()
        }

        when (storyType) {
            StoryType.LOTTIE -> playLottieAnimation()
            StoryType.VIDEO -> playVideo()
            StoryType.IMAGE -> loadImage()
        }
    }

    private fun loadImage() {
        binding.storyImageView.apply {
            visible()
            setImageByUrl(currentStory?.mediaUrl)
        }
        startTimer()
    }

    private fun playLottieAnimation() {
        with(binding.storyLottieView) {
            binding.progressCircular.visible()
            visible()
            timer = getTimer()
            addAnimatorListener(object : AnimatorListener {
                override fun onAnimationStart(p0: Animator?) {
                    timer?.start()
                }

                override fun onAnimationEnd(p0: Animator?) {
                    timer?.cancel()
                }

                override fun onAnimationCancel(p0: Animator?) {
                    timer?.cancel()
                }

                override fun onAnimationRepeat(p0: Animator?) {}
            })

            repeatCount = LottieDrawable.INFINITE
            setAnimationFromUrl(currentStory?.mediaUrl)
            addLottieOnCompositionLoadedListener {
                binding.progressCircular.gone()
                playAnimation()
            }
        }
    }

    private fun playVideo() {
        initializePlayer()
        exoPlayer?.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.progressCircular.visible()
                        binding.storyVideoView.gone()
                    }

                    Player.STATE_READY -> {
                        startTimer()
                        binding.storyVideoView.visible()
                        binding.progressCircular.gone()
                    }
                }
            }
        })

        currentStory?.mediaUrl?.let { videoUrl ->
            exoPlayer?.run {
                setMediaItem(MediaItem.fromUri(videoUrl))
                prepare()
                playWhenReady = true
            }
            binding.storyVideoView.player = exoPlayer
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

    //endregion

    fun setupStories(
        stories: ArrayList<StoryModel> = arrayListOf(),
        listener: StoryListener? = null
    ) {
        this.stories = stories
        this.listener = listener

        with(binding) {
            progressBarsContainer.removeAllViews()
            progressBars.clear()
            closeButton.setOnClickListener { listener?.onClose() }
            additionalButton.setOnClickListener { listener?.onClose() }
        }

        currentStoryIndex = stories.indexOfFirst { it.isViewed != true }.let {
            if (it == -1)
                if (stories.all { story -> story.isViewed == true } || stories.isEmpty()) 0 else stories.size - 1
            else it
        }

        for (story in stories) {
            val progressBar =
                ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                    layoutParams =
                        LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                            .apply {
                                setPadding(4, 0, 4, 0)
                            }
                    max = 1000
                    progress = if (story.isViewed == true) 1000 else 0
                    progressDrawable = context.getDrawable(R.drawable.chili_story_progress_bar)
                }
            progressBars.add(progressBar)
            binding.progressBarsContainer.addView(progressBar)
        }
        invalidate()
        binding.progressBarsContainer.visible()

        if (this.stories.isNotEmpty()) {
            listener?.onStart(currentStoryIndex)
            playNext(this.stories[currentStoryIndex])
        }
    }


    private fun moveToNextSegment() {
        if (currentStoryIndex < this.stories.size - 1) {
            this.progressBars[currentStoryIndex].progress = 1000
            currentStoryIndex++
            playNext(this.stories[currentStoryIndex])
        } else {
            listener?.onAllStoriesCompleted()
        }
    }

    private fun moveToPreviousSegment() {
        if (currentStoryIndex > 0) {
            this.progressBars[currentStoryIndex].progress = 0
            currentStoryIndex--
            playNext(this.stories[currentStoryIndex])
        }
    }

    private fun finishWithAnimation() {
        this@StoryView.animate()
            .translationY(0f)
            .setDuration(200)
            .withEndAction {
                listener?.onClose()
            }.start()
    }

    //region timer

    private fun getTimer(time: Long = 0) = object :
        CountDownTimer(
            if (time > 0) time else currentStory?.duration?.times(1000.toLong()) ?: 0,
            10
        ) {
        override fun onFinish() {
            if (currentStoryIndex < stories.size - 1) {
                listener?.onFinished(currentStoryIndex)
                currentStoryIndex++
                playNext(stories[currentStoryIndex])
            } else {
                resetTimer()
                listener?.onAllStoriesCompleted()
                isAllStoriesFinished = true
            }
        }

        override fun onTick(millisUntilFinished: Long) {
            timeRemaining = millisUntilFinished
            timeFinished = (currentStory?.duration?.times(1000.toLong()) ?: 0) - millisUntilFinished

            progressBars[currentStoryIndex].progress =
                (timeFinished * 100 / (currentStory?.duration?.times(100.toLong()) ?: 0)).toInt()
        }
    }

    fun startTimer() {
        timer = getTimer()
        timer?.start()
    }

    fun pauseTimer() {
        if (timer != null && timeRemaining > 0) {
            timer?.cancel()
            timer = null
            isPaused = true

            when (currentStory?.storyType) {
                StoryType.VIDEO -> exoPlayer?.playWhenReady = false
                StoryType.LOTTIE -> pauseLottieAnimation()
                else -> {}
            }
        }
    }

    fun resetTimer() {
        timer?.cancel()
        timer = null
        if (exoPlayer?.isPlaying == true) exoPlayer?.stop()
    }

    fun resumeTimer() {
        when {
            isPaused -> {
                isPaused = false
                timer = getTimer(timeRemaining).also { it.start() }

                when (currentStory?.storyType) {
                    StoryType.VIDEO -> {
                        exoPlayer?.playWhenReady = true
                        exoPlayer?.prepare()
                    }
                    StoryType.LOTTIE -> resumeLottieAnimation()
                    else -> {}
                }
            }
            isAllStoriesFinished && isVisible  -> {
                progressBars[currentStoryIndex].progress = 0
                currentStory?.storyType?.let { playContentByStoryType(it) }
            }
        }
    }

    //endregion

    //region gesture listener

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
                val scale = max(0.8f, 1 - min(1f, deltaY / 5000))

                binding.root.scaleX = scale
                binding.root.scaleY = scale
                binding.root.translationY = deltaY
                return true
            }
            return false
        }

        override fun onLongPress(e: MotionEvent?) {
            super.onLongPress(e)
            pauseTimer()
        }
    }

    //endregion

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        binding.storyLottieView.cancelAnimation()
        timer?.cancel()
        timer = null
    }
}

interface StoryListener {
    fun onAllStoriesCompleted()
    fun onClose()
    fun onFinished(index: Int)
    fun onStart(index: Int)
}