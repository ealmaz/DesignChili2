package com.design2.chili2.view.stories

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.design2.chili2.R
import com.design2.chili2.databinding.ActivityStoryBinding

class StoryActivity : AppCompatActivity() {

    private var progressBarHandler = Handler(Looper.getMainLooper())
    private lateinit var binding: ActivityStoryBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        binding.storyView.load(
            arrayListOf(
                StoryModel(
                    "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
                ),
                StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", image = "https://badgeland.com/media/webp_image/catalog/product/cache/afb3d9b5d6719d7ac9304f40f95ae75d/t/h/this-is-fine-katte-pin.webp", ),
                StoryModel("https://m.media-amazon.com/images/I/51U9SFk6SJL.jpg", ),
                StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", lottie = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = StoryType.LOTTIE),
                StoryModel(title = "Bla bla bla bla", video = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = StoryType.VIDEO)
            ),
            object : StoryListener {
                override fun onAllFinished() {
                    finishWithAnimation()
                }

                override fun onFinished(index: Int) {
                }

                override fun setImageFor(index: Int, story: StoryModel?, imageView: ImageView) {
                }

                override fun onStart() {
                }
            }
        )
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
