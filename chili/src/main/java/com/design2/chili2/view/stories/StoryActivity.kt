package com.design2.chili2.view.stories

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.design2.chili2.R
import com.design2.chili2.databinding.ActivityStoryBinding
import com.design2.chili2.view.stories.adapter.PageTransformer
import com.design2.chili2.view.stories.adapter.StoryPagerAdapter

class StoryActivity : AppCompatActivity(), StoryListener {

    private lateinit var binding: ActivityStoryBinding

    private val storyBlock1 = StoryBlock(1, false, arrayListOf(
        StoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
            buttonText = "Go", buttonType = ButtonType.ADDITIONAL
        ),
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://badgeland.com/media/webp_image/catalog/product/cache/afb3d9b5d6719d7ac9304f40f95ae75d/t/h/this-is-fine-katte-pin.webp", ),
        StoryModel(mediaUrl = "https://m.media-amazon.com/images/I/51U9SFk6SJL.jpg", buttonText = "Go", buttonType = ButtonType.SECONDARY),
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = StoryType.LOTTIE),
        StoryModel(title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = StoryType.VIDEO)
    ))

    private val storyBlock2 = StoryBlock(1, false, arrayListOf(
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = StoryType.LOTTIE),
        StoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712"
        ),
        StoryModel(title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = StoryType.VIDEO)
    ))

    private val storyBlock3 = StoryBlock(1, false, arrayListOf(
        StoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712", isViewed = true
        ),
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://badgeland.com/media/webp_image/catalog/product/cache/afb3d9b5d6719d7ac9304f40f95ae75d/t/h/this-is-fine-katte-pin.webp", isViewed = true ),
        StoryModel(mediaUrl = "https://m.media-amazon.com/images/I/51U9SFk6SJL.jpg", isViewed = true),
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = StoryType.LOTTIE),
        StoryModel(title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = StoryType.VIDEO)
    ))

    private val storyBlock4 = StoryBlock(1, false, arrayListOf(
        StoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
        ),
        StoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = StoryType.LOTTIE),
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupViews()
    }

    private fun setupViews() {
        binding.storiesView.setupStories(listOf(storyBlock1, storyBlock2, storyBlock3, storyBlock4), this, this)
    }

    private fun finishWithAnimation() {
        finish()
//        overridePendingTransition(R.anim.stay, R.anim.zoom_out)
    }

    override fun onAllStoriesCompleted() {
        binding.storiesView.moveToNextPage { finishWithAnimation() }
    }

    override fun onClose() {
        finishWithAnimation()
    }

    override fun onFinished(index: Int) {
        // Remember lastFinished story
    }

    override fun onStart(index: Int) {
        // Remember lastStarted story
    }
}
