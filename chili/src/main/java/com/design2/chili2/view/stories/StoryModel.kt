package com.design2.chili2.view.stories

data class StoryModel(
    val image: String? = null,
    val video: String? = null,
    val lottie: String? = null,
    val title: String? = null,
    val description: String? = null,
    val time: Int = 10,
    val storyType: StoryType = StoryType.IMAGE
)

enum class StoryType() {
    IMAGE, VIDEO, LOTTIE
}