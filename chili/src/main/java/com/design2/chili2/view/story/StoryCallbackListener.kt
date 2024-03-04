package com.design2.chili2.view.story

interface StoryCallbackListener {
    fun closingStory()
    fun viewPagerSwipes(isEnabled: Boolean)
    fun openDeeplink(deeplink: String)
    fun goToNextFragment()
    fun goToPrevFragment()
}