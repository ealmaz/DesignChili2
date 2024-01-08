package com.design2.chili2.view.story

interface StoryCallbackListener {
    fun closeStory()
    fun openDeeplink(deeplink: String)
    fun goToNextFragment()
    fun goToPrevFragment()
}