package com.design2.chili2.view.stories


import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.request.RequestOptions
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoryBinding
import com.design2.chili2.extensions.applyCenterCrop
import com.design2.chili2.extensions.applyFitCenter
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.setDrawableFromUrlWithListeners
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.horizontalFitBottom
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.visible
import kotlin.collections.ArrayList

class StoryView : ConstraintLayout {
    private lateinit var binding: ChiliViewStoryBinding

    private lateinit var gestureDetector: GestureDetectorCompat

    private var currentStoryIndex: Int = 0
    private var timeRemaining = 0L
    private var timeFinished = 0L
    private var isPaused: Boolean = false
    private var isAllStoriesFinished: Boolean = false

    private var currentStory: ChilliStoryModel? = null
    private var stories: ArrayList<ChilliStoryModel> = arrayListOf()

    private var timer: CountDownTimer? = null
    private var onMoveListener: StoryMoveListener? = null
    private var onFinishListener: StoryOnFinishListener? = null
    private var onClickListener: StoryClickListener? = null

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
    private fun playNext(storyModel: ChilliStoryModel) {
        onMoveListener?.onStart(currentStoryIndex)
        resetTimer()
        timeRemaining = 0
        currentStory = storyModel

        with(binding) {
            if (storyModel.backgroundColor != null) {
                try {
                    val backgroundColor = Color.parseColor(storyModel.backgroundColor)
                    binding.clRoot.setBackgroundColor(backgroundColor)
                } catch (e: Exception) {
                    binding.clRoot.setBackgroundColor(context.color(R.color.white_1))
                }
            } else {
                binding.clRoot.setBackgroundColor(context.color(R.color.white_1))
            }

            if (storyModel.title != null) {
                storyTitleView.apply {
                    visible()
                    text = storyModel.title
                    storyModel.titleTextColor?.let {
                        try {
                            val titleColor = Color.parseColor(it)
                            setTextColor(titleColor)
                            closeButton.setColorFilter(titleColor)
                        } catch (e: IllegalArgumentException) {
                            setTextColor(Color.BLACK)
                            closeButton.setColorFilter(Color.BLACK)
                        }
                    }
                }
            } else storyTitleView.gone()

            if (storyModel.description != null) {
                storySubtitleView.apply {
                    visible()
                    text = storyModel.description
                    storyModel.subtitleTextColor?.let {
                        try {
                            val subTitleColor = Color.parseColor(it)
                            setTextColor(subTitleColor)
                        } catch (e: IllegalArgumentException) {
                            setTextColor(Color.BLACK)
                        }
                    }
                }
            } else storySubtitleView.gone()

            setupButton(storyModel)

            touchableView.setOnTouchListener { _, event ->
                gestureDetector.onTouchEvent(event)

                when (event.action) {
                    MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                        resumeTimer()
                    }
                }

                true
            }
        }

        playContentByStoryType(storyModel.storyType)
    }

    private fun setupButton(storyModel: ChilliStoryModel) {
        with(binding) {
            when {
                storyModel.buttonText != null && storyModel.buttonType == ChilliButtonType.ADDITIONAL -> {
                    additionalButton.apply {
                        visible()
                        text = storyModel.buttonText
                        setOnSingleClickListener {
                            if (storyModel.deeplink != null) onClickListener?.onDeeplinkClick(
                                storyModel.deeplink
                            )
                            else finishStories()
                        }
                    }
                    secondaryButton.gone()
                }

                storyModel.buttonText != null && storyModel.buttonType == ChilliButtonType.SECONDARY -> {
                    secondaryButton.apply {
                        visible()
                        text = storyModel.buttonText
                        setOnSingleClickListener {
                            if (storyModel.deeplink != null) onClickListener?.onDeeplinkClick(
                                storyModel.deeplink
                            )
                            else finishStories()
                        }
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

    private fun playContentByStoryType(storyType: ChilliStoryType) {
        with(binding) {
            storyImageView.gone()
            storyVideoView.gone()
            exoPlayer?.stop()
            storyLottieView.gone()
        }

        when (storyType) {
            ChilliStoryType.LOTTIE -> playLottieAnimation()
            ChilliStoryType.VIDEO -> playVideo()
            ChilliStoryType.IMAGE -> loadImage()
        }
    }

    private fun loadImage() {
        with(binding) {

            progressCircular.visible()
            storyImageView.apply {
                visible()
                try {
                    setDrawableFromUrlWithListeners(
                        currentStory?.mediaUrl,
                        RequestOptions(),
                        { drawable ->
                            progressCircular.gone()
                            if (currentStory?.scaleType == StoryScaleType.BOTTOM_HORIZONTAL_CROP)
                                horizontalFitBottom(drawable)
                            else applyCenterCrop()
                        }, {
                            progressCircular.gone()
                        }
                    )
                } catch (e: Exception) {
                    progressCircular.gone()
                }
            }
        }

        startTimer()
    }

    private fun playLottieAnimation() {
        with(binding.storyLottieView) {
            binding.progressCircular.visible()
            visible()
            try {
                timer = getTimer()
                addAnimatorListener(object : AnimatorListener {
                    override fun onAnimationStart(p0: Animator) {
                        timer?.start()
                    }

                    override fun onAnimationEnd(p0: Animator) {
                        timer?.cancel()
                    }

                    override fun onAnimationCancel(p0: Animator) {
                        timer?.cancel()
                    }

                    override fun onAnimationRepeat(p0: Animator) {}
                })

                repeatCount = LottieDrawable.INFINITE
                setAnimationFromUrl(currentStory?.mediaUrl)
                addLottieOnCompositionLoadedListener {
                    if (currentStory?.scaleType == StoryScaleType.BOTTOM_HORIZONTAL_CROP) this.post {
                        horizontalFitBottom(
                            it
                        )
                    }
                    else this.post { applyCenterCrop() }
                    binding.progressCircular.gone()
                    playAnimation()
                }
            } catch (e: Exception) {
                binding.progressCircular.gone()
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
            try {
                exoPlayer?.run {
                    if (currentStory?.scaleType == StoryScaleType.BOTTOM_HORIZONTAL_CROP) binding.storyVideoView.horizontalFitBottom(
                        this
                    )
                    else binding.storyVideoView.applyFitCenter()
                    setMediaItem(MediaItem.fromUri(videoUrl))
                    prepare()
                    playWhenReady = true
                }
                binding.storyVideoView.player = exoPlayer
            } catch (e: Exception) {
                binding.progressCircular.gone()
            }
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
        stories: ArrayList<ChilliStoryModel> = arrayListOf(),
        moveListener: StoryMoveListener? = null,
        finishListener: StoryOnFinishListener? = null,
        clickListener: StoryClickListener? = null
    ) {
        this.stories = stories
        this.onMoveListener = moveListener
        this.onFinishListener = finishListener
        this.onClickListener = clickListener

        with(binding) {
            progressBarsContainer.removeAllViews()
            progressBars.clear()
            closeButton.setOnClickListener {
                finishStories()
            }
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
                    progress =
                        if (!stories.all { it.isViewed == true } && story.isViewed == true) 1000 else 0
                    progressDrawable = context.getDrawable(R.drawable.chili_story_progress_bar)
                }
            progressBars.add(progressBar)
            binding.progressBarsContainer.addView(progressBar)
        }
        binding.progressBarsContainer.visible()

        if (this.stories.isNotEmpty()) {
            onMoveListener?.onStart(currentStoryIndex)
            playNext(this.stories[currentStoryIndex])
        }
        invalidate()
    }


    private fun moveToNextSegment() {
        if (currentStoryIndex < this.stories.size - 1) {
            onMoveListener?.onFinished(currentStoryIndex)
            this.progressBars[currentStoryIndex].progress = 1000
            currentStoryIndex++
            playNext(this.stories[currentStoryIndex])
        } else {
            onMoveListener?.onAllStoriesCompleted()
            onFinishListener?.onAllStoriesFinished()
        }
    }

    private fun moveToPreviousSegment() {
        if (currentStoryIndex > 0) {
            this.progressBars[currentStoryIndex].progress = 0
            currentStoryIndex--
            playNext(this.stories[currentStoryIndex])
        } else {
            this.progressBars[currentStoryIndex].progress = 0
            playNext(this.stories[currentStoryIndex])
            onMoveListener?.onPreviousClick()
        }
    }

    fun finishStories() {
        resetTimer()
        onMoveListener?.onClose()
        onFinishListener?.onStoryClose()
    }

    private fun initializeContent(storyType: ChilliStoryType) {
        with(binding) {

            binding.storyImageView.gone()
            binding.storyVideoView.gone()
            binding.storyLottieView.gone()

            when (storyType) {
                ChilliStoryType.IMAGE -> {
                    progressCircular.visible()
                    storyImageView.apply {
                        visible()
                        try {
                            setDrawableFromUrlWithListeners(
                                currentStory?.mediaUrl,
                                RequestOptions(),
                                { drawable ->
                                    progressCircular.gone()
                                    if (currentStory?.scaleType == StoryScaleType.BOTTOM_HORIZONTAL_CROP)
                                        horizontalFitBottom(drawable)
                                    else applyCenterCrop()
                                }, {
                                    progressCircular.gone()
                                }
                            )
                        } catch (e: Exception) {
                            progressCircular.gone()
                        }
                    }
                }

                ChilliStoryType.VIDEO -> {
                    binding.storyVideoView.visible()
                    exoPlayer?.let { player ->
                        binding.storyVideoView.player = player
                        currentStory?.mediaUrl?.let {
                            player.setMediaItem(MediaItem.fromUri(it))
                            player.prepare()
                        }
                    }
                }

                ChilliStoryType.LOTTIE -> {
                    binding.storyLottieView.visible()
                    binding.storyLottieView.setAnimationFromUrl(currentStory?.mediaUrl)
                }
            }
        }
    }

    //region timer

    private fun getTimer(time: Long = 0) = object :
        CountDownTimer(
            if (time > 0) time else currentStory?.duration?.times(1000.toLong()) ?: 0,
            10
        ) {
        override fun onFinish() {
            if (currentStoryIndex < stories.size - 1) {
                onMoveListener?.onFinished(currentStoryIndex)
                currentStoryIndex++
                if (isVisible) playNext(stories[currentStoryIndex])
            } else {
                resetTimer()
                onMoveListener?.onAllStoriesCompleted()
                onFinishListener?.onAllStoriesFinished()
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
                ChilliStoryType.VIDEO -> exoPlayer?.playWhenReady = false
                ChilliStoryType.LOTTIE -> pauseLottieAnimation()
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
                currentStory?.storyType?.let { initializeContent(it) }
                isPaused = false
                timer = getTimer(timeRemaining).also { it.start() }

                when (currentStory?.storyType) {
                    ChilliStoryType.VIDEO -> {
                        exoPlayer?.playWhenReady = true
                        exoPlayer?.prepare()
                    }

                    ChilliStoryType.LOTTIE -> resumeLottieAnimation()
                    else -> {}
                }
            }

            isAllStoriesFinished && isVisible -> {
                progressBars[currentStoryIndex].progress = 0
                currentStory?.storyType?.let { playContentByStoryType(it) }
            }
        }
    }

    //endregion

    //region gesture listener

    inner class StoryGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            e.let {
                if (it.x < binding.touchableView.width / 2) {
                    moveToPreviousSegment()
                } else {
                    moveToNextSegment()
                }
            }
            return super.onSingleTapUp(e)
        }

        override fun onLongPress(e: MotionEvent) {
            super.onLongPress(e)
            pauseTimer()
        }
    }

    //endregion

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        resetTimer()
        binding.storyLottieView.cancelAnimation()
    }
}

enum class StoryScaleType {
    BOTTOM_HORIZONTAL_CROP,
    CENTER_CROP
}

interface StoryMoveListener {
    fun onAllStoriesCompleted()
    fun onClose()
    fun onFinished(index: Int)
    fun onStart(index: Int)
    fun onPreviousClick() {}
}

interface StoryClickListener {
    fun onDeeplinkClick(deeplink: String)
}

interface StoryOnFinishListener {
    fun onAllStoriesFinished()
    fun onStoryClose()
}