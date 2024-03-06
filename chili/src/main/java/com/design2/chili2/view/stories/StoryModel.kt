package com.design2.chili2.view.stories

import java.io.Serializable
import java.time.Duration

data class StoryModel(
    val id: Long? = null,
    val mediaUrl: String? = null,
    val title: String? = null,
    val description: String? = null,
    val duration: Int = 10,
    val storyType: StoryType = StoryType.IMAGE,
    val isViewed: Boolean? = false
): Serializable

data class StoryBlock(
    val id: Long? = null,
    val isViewed: Boolean? = false,
    val stories: ArrayList<StoryModel>? = arrayListOf()
): Serializable

enum class StoryType {
    IMAGE, VIDEO, LOTTIE
}