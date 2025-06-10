package com.design2.app

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.design2.app.databinding.ActivityStoryBinding
import com.design2.chili2.view.stories.ChilliButtonType
import com.design2.chili2.view.stories.ChilliStoryBlock
import com.design2.chili2.view.stories.ChilliStoryModel
import com.design2.chili2.view.stories.ChilliStoryType
import com.design2.chili2.view.stories.StoryClickListener
import com.design2.chili2.view.stories.StoryMoveListener
import com.design2.chili2.view.stories.StoryOnFinishListener
import com.design2.chili2.view.stories.StoryPageSelectedListener


class StoryActivity : AppCompatActivity(), StoryMoveListener, StoryOnFinishListener,
    StoryClickListener {

    private var _binding: ActivityStoryBinding? = null
    private val binding get() = _binding!!


    private val storyBlock = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                mediaUrl = "https://minio.o.kg/media-service/UserStory/2015dea6-5254-478f-91a5-35ffb911cde0",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/fbf549d5-2678-49e2-a505-ae2b5160e750"
            ),
            ChilliStoryModel(
                mediaUrl = "https://minio.o.kg/media-service/UserStory/7f641186-9c2c-43e0-863e-1da5828d32a9",
                buttonText = "Go",
                backgroundColor = "#000000",
                buttonType = ChilliButtonType.SECONDARY,
                deeplink = "hello.kg"
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/89c1063c-6a7f-4276-822e-c3734efb7e15",
//                storyType = ChilliStoryType.LOTTIE
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/7af372c5-5d4e-4d1b-947c-d88ab4b10f82",
//                storyType = ChilliStoryType.LOTTIE
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/6840d7c4-dded-4ab1-97a9-6d0b07b9d8ad",
//                storyType = ChilliStoryType.LOTTIE
            ),
        ),
        "block1"
    )

    private val storyBlock10 = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/782f2092-8761-4d9d-99eb-c24cab58f00b",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                storyType = ChilliStoryType.LOTTIE,
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/b66f3f44-1ffc-4367-9b51-93380186aff9"
            ),
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/4c67d6e1-d3dc-4e5c-bfcf-3e2585d0b893",
                buttonText = "Go",
                backgroundColor = "#000000",
                buttonType = ChilliButtonType.SECONDARY,
                deeplink = "hello.kg"
            )
        ),
        "block10"
    )

    private val storyBlock20 = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/b3639669-1831-4370-999f-11df9b02b563",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/05e76ae0-2e09-4c9a-b4cf-99489bd4f6a8"
            ),
        ),
        "block20"
    )

    private val storyBlock30 = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                mediaUrl = "https://minio.o.kg/media-service/UserStory/8265a34b-d0bc-4f4f-a140-a5eaed36c4b8",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/1fa06dc2-7fb3-49bc-b4bf-27c23dc60063"
            ),
            ChilliStoryModel(
                mediaUrl = "https://minio.o.kg/media-service/UserStory/447fcd60-0e25-4288-b19a-506c931222af",
                buttonText = "Go",
                backgroundColor = "#000000",
                buttonType = ChilliButtonType.SECONDARY,
                deeplink = "hello.kg"
            ),
        ),
        "block30"
    )

    private val storyBlock40 = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/e1bcfae6-4fff-49bf-bead-caed5a248513",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/93915eb7-955a-4218-9bae-2fdba8bbe0d3"
            ),
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/0a8af819-be0e-45e2-a815-24f385a3e4f7",
                buttonText = "Go",
                backgroundColor = "#000000",
                buttonType = ChilliButtonType.SECONDARY,
                deeplink = "hello.kg"
            ),
            ChilliStoryModel(
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/7d7e7560-a620-45b8-9873-59d2fee10bc8",
                storyType = ChilliStoryType.LOTTIE
            ),
        ),
        "block40"
    )

    private val storyBlock50 = ChilliStoryBlock(
        1, false, arrayListOf(
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                mediaUrl = "https://minio.o.kg/media-service/UserStory/25fca244-d093-4d26-b063-234978c88764",
                buttonText = "Go", buttonType = ChilliButtonType.ADDITIONAL
            ),
            ChilliStoryModel(
                storyType = ChilliStoryType.LOTTIE,
                title = "What is Lorem Ipsum?",
                description = "Lorem Ipsum is simply dummy text of the printing and typesetting industry",
                mediaUrl = "https://minio.o.kg/media-service/UserStory/dd272959-b50f-4373-8ab7-c2e4a541c04d"
            ),
        ),
        "block50"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        _binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViews()
    }

    override fun onSaveInstanceState(outState: Bundle) {}

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun hideStatusBar() {
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun setupViews() {
        binding.storiesView.setupStories(
            listOf(
                storyBlock,
                storyBlock10,
                storyBlock20,
                storyBlock30,
                storyBlock40,
                storyBlock50,
            ), this, this, this, this, currentStoryBlock = "block1"
        )
    }

    private fun finishWithAnimation() {
        finish()
//        overridePendingTransition(R.anim.stay, R.anim.zoom_out)
    }

    override fun onAllStoriesCompleted(blockType: String?) {
        binding.storiesView.moveToNextPage { finishWithAnimation() }
    }

    override fun onPreviousClick() {
        binding.storiesView.moveToPreviousPage()
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

    override fun onStart(index: Int, blockType: String?) {
    }

    override fun onAllStoriesFinished() {
    }

    override fun onStoryClose() {
    }

    override fun onDeeplinkClick(deeplink: String) {
        Toast.makeText(this, deeplink, Toast.LENGTH_SHORT).show()
    }
}
