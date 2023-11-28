package com.design2.chili2.view.story

open class Story(
    val id: Int? = null,
    val title: String? = null,
    val isRead: Boolean? = null,
    val type: StoryType? = null,
    val imageUrl: String? = null,
    val stories: List<DetailedStory>? = null,
    val deeplink: String? = null,
    val duration: String? = null
)

enum class StoryType {
    GENERAL,
    ALGA,
    FOR_YOU,
    ALL_STORIES
}

open class DetailedStory(
    val imageUrl: String?,
    val title: String? = "",
    val subtitle: String? = "",
    val description: String? = ""
)