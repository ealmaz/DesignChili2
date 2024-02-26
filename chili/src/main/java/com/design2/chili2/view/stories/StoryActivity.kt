package com.design2.chili2.view.stories

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.setPadding
import com.design2.chili2.R
import com.design2.chili2.databinding.ActivityStoryBinding
import com.design2.chili2.databinding.ChiliViewStoryBinding

class StoryActivity : AppCompatActivity() {

    private var progressBarHandler = Handler(Looper.getMainLooper())
    private var currentProgressIndex = 0
    private val segmentDuration = 10000L
    private val numberOfSegments = 5
    private lateinit var binding: ActivityStoryBinding
//    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        initializeProgressBars()
//        startProgressUpdate()
//
//        gestureDetector = GestureDetectorCompat(this, StoryGestureListener())

        binding.storyView.load(
            arrayListOf(
                StoryModel(
                    "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
                ),
                StoryModel("https://play-lh.googleusercontent.com/ZibMoJTN17NMZiExnORzm4xMlVy2ItCzEP-_MPchJGTBMTUrQKQWkzY2pMnYpi_yKh0", ),
                StoryModel("https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712", ),
                StoryModel("https://play-lh.googleusercontent.com/ZibMoJTN17NMZiExnORzm4xMlVy2ItCzEP-_MPchJGTBMTUrQKQWkzY2pMnYpi_yKh0", ),
                StoryModel("https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712")
            ),
            object : StoryListener {
                override fun onAllFinished() {
                    finishWithAnimation()
                }

                override fun onFinished(index: Int) {
                }

                override fun setImageFor(index: Int, story: StoryModel?, imageView: ImageView) {
                    // Extra Functionality
                }

                override fun onStart() {
                    // pre-load images
                }
            }
        )

    }

    inner class StoryGestureListener : GestureDetector.SimpleOnGestureListener() {
        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100

        override fun onDown(e: MotionEvent?): Boolean {
            return true
        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            e?.let {
                if (it.x < binding.root.width / 2) {
                    // Обработка касания левой стороны
                    moveToNextSegment()
                } else {
                    // Обработка касания правой стороны
                    moveToPreviousSegment()
                }
            }
            return super.onSingleTapUp(e)
        }

        override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
            val distanceX = e2!!.x - e1!!.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) {
                    openPreviousUserStory()
                } else {
                    openNextUserStory()
                }
                return true
            } else if (Math.abs(distanceY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD && distanceY > 0) {
                finishWithAnimation()
                return true
            }
            return false
        }
    }

    private fun initializeProgressBars() {
        for (i in 0 until numberOfSegments) {
            val progressBar = ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f).apply {
                    setPadding(8)
                }
                max = 100
                progress = 0
            }
//            binding.progressBarsContainer.addView(progressBar)
        }
    }

    private fun startProgressUpdate() {
        val runnable = object : Runnable {
            override fun run() {
                if (currentProgressIndex < numberOfSegments) {
//                    val progressBar = binding.progressBarsContainer.getChildAt(currentProgressIndex) as ProgressBar
//                    progressBar.progress += 1
//                    if (progressBar.progress >= 100) {
//                        currentProgressIndex += 1
//                    }
                    progressBarHandler.postDelayed(this, segmentDuration / 100)
                }
            }
        }
        progressBarHandler.postDelayed(runnable, segmentDuration / 100)
    }

    // Методы для перемещения между сегментами и сторисами
    private fun moveToNextSegment() {
        // Реализация перехода к следующему сегменту
    }

    private fun moveToPreviousSegment() {
        // Реализация перехода к предыдущему сегменту
    }

    private fun openNextUserStory() {
        // Реализация открытия следующего сториса пользователя
    }

    private fun openPreviousUserStory() {
        // Реализация открытия предыдущего сториса пользователя
    }

    private fun finishWithAnimation() {
        finish()
        overridePendingTransition(R.anim.stay, R.anim.slide_down)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressBarHandler.removeCallbacksAndMessages(null)
    }
}
