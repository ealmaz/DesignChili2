package com.design2.chili2.util

enum class IconType {
    PLUS, CHEVRON
}

enum class IconStatus {
    SELECTED, ACTIVE, UNSELECTED, UNAVAILABLE
}

enum class IconSize(val value: Int) {
    SMALL(-1), MEDIUM(-2), LARGE(-3)
}

enum class RoundedCornerMode(var value: Int) {
    SINGLE(0), TOP(1), MIDDLE(2), BOTTOM(3),WITHOUT_ROUNDS(4)
}

enum class HighlighterState(var value: Int) {
    GONE(0), VISIBLE(1), WITH_ICON(2)
}

fun Int.toHighlighterStateEnum(): HighlighterState? {
    return when(this) {
        HighlighterState.GONE.value -> HighlighterState.GONE
        HighlighterState.VISIBLE.value -> HighlighterState.VISIBLE
        HighlighterState.WITH_ICON.value -> HighlighterState.WITH_ICON
        else -> null
    }
}

enum class MediaType(val value: Int) {
    IMAGE_URL(0), LOTTIE_JSON(1)
}