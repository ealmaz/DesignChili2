package com.design2.chili2.view.story

class Story(
    val id: Int? = null,
    val orderNumber: Int? = null,
    val title: String? = null,
    val isMarketingCenter: Boolean = false,
    val image: String? = null,
    val stories: List<DetailedStory>? = null,
    val borderColors: List<String>? = null
) {
    fun isRead(): Boolean {
        this.stories?.forEach {
            if (!it.isViewed) return false
        }
        return true
    }
}

class DetailedStory(
    val id: Int? = null,
    val orderNumber: Int? = null,
    val isViewed: Boolean = false,
    val image: String? = null,
    val title: String? = "",
    val description: String? = "",
    val button: ButtonModel? = null
)

class ButtonModel(
    val title: String? = null,
    val deepLink: String? = null,
    val titleColor: String? = null,
    val backgroundColor: String? = null
)