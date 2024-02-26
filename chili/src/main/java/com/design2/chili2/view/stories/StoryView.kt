package com.design2.chili2.view.stories


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.CountDownTimer
import android.util.AttributeSet
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
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewStoryBinding
import com.design2.chili2.extensions.setImageByUrl
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

    private fun init(context: Context?) {
        mContext = context
        binding = ChiliViewStoryBinding.inflate(LayoutInflater.from(context), this, true)
        gestureDetector = GestureDetectorCompat(context, StoryGestureListener())
    }

    private fun next(storyModel: StoryModel) {
        currentStory = storyModel
        loadImage()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun loadImage() {
        timer = getTimer()
        binding.storyImageView.setImageByUrl(currentStory?.image)
        listener?.setImageFor(currentStoryIndex, currentStory, binding.storyImageView)
        timer?.start()

        binding.root.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)


            when (event.action) {
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    if (binding.root.translationY > 100) {
                        finishWithAnimation()
                    } else {
                        binding.root.animate().translationY(0f).setDuration(200).withEndAction {
//                            resumeTimer()
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
        binding.closeButton.setOnClickListener { listener?.onAllFinished() }
        binding.progressBarsContainer.removeAllViews()

        for (story in stories) {
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setPadding(4, 0,4,0)
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
                if (it.x < binding.root.width / 2) {
                    moveToPreviousSegment()
                } else {
                    moveToNextSegment()
                }
            }
            return super.onSingleTapUp(e)
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            val deltaY = e2.rawY - initialTouchY
            if (deltaY > 0) {
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

    private fun moveToNextSegment() {
        if (currentStoryIndex < this.stories.size - 1) {
            timer?.cancel()
            this.progressBars[currentStoryIndex].progress = 100
            currentStoryIndex++
            next(this.stories[currentStoryIndex])
        }
    }

    private fun moveToPreviousSegment() {
        if (currentStoryIndex > 0) {
            timer?.cancel()
            this.progressBars[currentStoryIndex].progress = 0
            currentStoryIndex--
            next(this.stories[currentStoryIndex])
        }
    }

    private fun openNextUserStory() {
    }

    private fun openPreviousUserStory() {
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
    }

    private fun resumeTimer() {
        timer = getTimer(timeRemaining)
        timer?.start()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
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