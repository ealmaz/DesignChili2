package com.design2.chili2.view.stories

import java.io.Serializable

data class ChilliStoryModel(
    val id: Long? = null,
    val mediaUrl: String? = null,
    val title: String? = null,
    val description: String? = null,
    val duration: Int = 10,
    val backgroundColor: String? = null,
    val storyType: ChilliStoryType = ChilliStoryType.IMAGE,
    val isViewed: Boolean? = false,
    val titleTextColor: String? = null,
    val subtitleTextColor: String? = null,
    val buttonType: ChilliButtonType = ChilliButtonType.ADDITIONAL,
    val buttonText: String? = null,
    val deeplink: String? = null,
    val scaleType: StoryScaleType? = StoryScaleType.BOTTOM_HORIZONTAL_CROP
): Serializable

data class ChilliStoryBlock(
    val id: Long? = null,
    val isViewed: Boolean? = false,
    val stories: ArrayList<ChilliStoryModel>? = arrayListOf(),
    val blockType: String? = null
): Serializable

enum class ChilliStoryType {
    IMAGE, VIDEO, LOTTIE
}

enum class ChilliButtonType {
    SECONDARY, ADDITIONAL
}