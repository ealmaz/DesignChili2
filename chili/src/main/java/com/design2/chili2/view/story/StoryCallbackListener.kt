package com.design2.chili2.view.story

interface StoryCallbackListener {
    fun closeStory()
    fun openDeeplink()
    fun goToNextFragment()
    fun goToPrevFragment()
}