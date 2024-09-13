package com.design2.app

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.design2.app.databinding.ActivityStoryBinding
import com.design2.chili2.view.stories.ChilliButtonType
import com.design2.chili2.view.stories.ChilliStoryBlock
import com.design2.chili2.view.stories.ChilliStoryModel
import com.design2.chili2.view.stories.ChilliStoryType
import com.design2.chili2.view.stories.StoryMoveListener
import com.design2.chili2.view.stories.StoryOnFinishListener


class StoryActivity : AppCompatActivity(), StoryMoveListener, StoryOnFinishListener {

    private lateinit var binding: ActivityStoryBinding


    private val storyBlock = ChilliStoryBlock(1, false, arrayListOf(
        ChilliStoryModel(
            mediaUrl = "https://devminio.o.kg/media-service/UserStory/221bbc7d-9c65-4d21-ab6f-de27afdca23c",
            buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
        ),
        ChilliStoryModel(
            title = "What is Lorem Ipsum?",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
            mediaUrl = "https://devminio.o.kg/media-service/UserStory/33bbbc34-50c5-4d7e-9204-e2bcae2a88bd"
        ),
        ChilliStoryModel(mediaUrl = "https://devminio.o.kg/media-service/UserStory/3a7279d1-6342-4f14-9496-8bd89b3afa66", buttonText = "Go", buttonType = ChilliButtonType.SECONDARY),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://devminio.o.kg/media-service/UserStory/6e1a016d-f3dd-49a7-a482-3d2a61e7220a", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://devminio.o.kg/media-service/UserStory/663d691d-7fb5-4d0c-8735-4fc8705dfddb", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://devminio.o.kg/media-service/UserStory/cdb6b7e3-98ba-41bb-b7ab-6c48f0f50a52", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "Bla bla bla bla", mediaUrl = "https://devminio.o.kg/media-service/UserStory/94bd241a-3603-4d36-956b-4f385a664d0f", storyType = ChilliStoryType.VIDEO)
    ))

    private val storyBlock1 = ChilliStoryBlock(1, false, arrayListOf(
        ChilliStoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
            buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
        ),
        ChilliStoryModel(
            title = "What is Lorem Ipsum?",
            description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
            mediaUrl = "https://badgeland.com/media/webp_image/catalog/product/cache/afb3d9b5d6719d7ac9304f40f95ae75d/t/h/this-is-fine-katte-pin.webp"
        ),
        ChilliStoryModel(mediaUrl = "https://m.media-amazon.com/images/I/51U9SFk6SJL.jpg", buttonText = "Go", buttonType = ChilliButtonType.SECONDARY),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://devminio.o.kg/media-service/UserStory/d2ca5499-e3f9-45dc-927f-d74b90b96193", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://devminio.o.kg/media-service/UserStory/a8275a33-0526-46a0-8aea-3fc22a76271d", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = ChilliStoryType.VIDEO)
    ))

    private val storyBlock2 = ChilliStoryBlock(1, false, arrayListOf(
        ChilliStoryModel(isViewed = true, title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(isViewed = true,
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712"
        ),
        ChilliStoryModel(isViewed = true, title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = ChilliStoryType.VIDEO)
    ))

    private val storyBlock3 = ChilliStoryBlock(1, false, arrayListOf(
        ChilliStoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712", isViewed = true
        ),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://badgeland.com/media/webp_image/catalog/product/cache/afb3d9b5d6719d7ac9304f40f95ae75d/t/h/this-is-fine-katte-pin.webp", isViewed = true ),
        ChilliStoryModel(mediaUrl = "https://m.media-amazon.com/images/I/51U9SFk6SJL.jpg", isViewed = false),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = ChilliStoryType.LOTTIE),
        ChilliStoryModel(title = "Bla bla bla bla", mediaUrl = "https://www.learningcontainer.com/wp-content/uploads/2020/05/sample-mp4-file.mp4", storyType = ChilliStoryType.VIDEO)
    ))

    private val storyBlock4 = ChilliStoryBlock(1, false, arrayListOf(
        ChilliStoryModel(
            mediaUrl = "https://static.wikia.nocookie.net/adventures-of-chris-and-tifa/images/c/c6/71FA6EC3-137C-4A43-87A0-10130B2AC0A4.jpg/revision/latest?cb=20210830075712",
        ),
        ChilliStoryModel(title = "What is Lorem Ipsum?", description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry", mediaUrl = "https://lottie.host/68efeab0-c1cf-41ef-ade1-4cede3b3bacc/ZE6c1LKeNs.json", storyType = ChilliStoryType.LOTTIE),
    ))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        hideStatusBar()

        setupViews()
    }

    private fun hideStatusBar() {
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setupViews() {
        binding.storiesView.setupStories(listOf(storyBlock, storyBlock1, storyBlock2, storyBlock3, storyBlock4), this, this, this)
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

    override fun onAllStoriesFinished() {
    }

    override fun onStoryClose() {
    }
}
