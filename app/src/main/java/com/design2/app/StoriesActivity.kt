package com.design2.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.app.databinding.ActivityStoriesBinding
import com.design2.chili2.view.story.DetailedStory
import com.design2.chili2.view.story.HighlightsAdapter
import com.design2.chili2.view.story.OnHighlightClickListener
import com.design2.chili2.view.story.Story
import com.design2.chili2.view.story.StoryCallbackListener
import com.design2.chili2.view.story.StoryType
import com.design2.chili2.view.story.detail.StoriesPagerAdapter

class StoriesActivity : AppCompatActivity(), OnHighlightClickListener, StoryCallbackListener {

    private lateinit var vb: ActivityStoriesBinding
    private var currentIndex: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(vb.root)

        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        vb.rvHighlights.layoutManager = layoutManager
        val adapter = HighlightsAdapter(this)
        adapter.addItems(stories)
        vb.rvHighlights.adapter = adapter
    }

    override fun onHighlightClick(index: Int) {
        vb.rvHighlights.isVisible = false
        vb.viewPager.isVisible = true
        currentIndex = index
        initPager()
    }

    override fun onAllStoriesViewClick() {
        Toast.makeText(this, "Переход на экран событий!", Toast.LENGTH_SHORT).show()
    }

    private fun initPager() {
        val adapter = StoriesPagerAdapter(this, stories, this)
        vb.viewPager.apply {
            this.adapter = adapter
            offscreenPageLimit = 7
            setCurrentItem(currentIndex, false)
        }
    }

    override fun goToNextFragment() {
        if (vb.viewPager.currentItem + 1 < stories.size)
            vb.viewPager.setCurrentItem(vb.viewPager.currentItem + 1, true)
        else closeStory()
    }

    override fun goToPrevFragment() {
        if (vb.viewPager.currentItem > 0)
            vb.viewPager.setCurrentItem(vb.viewPager.currentItem - 1, true)
    }

    override fun closeStory() {
        with(vb) {
            viewPager.isVisible = false
            rvHighlights.isVisible = true
            rvHighlights.scrollToPosition(viewPager.currentItem + 1)
        }
    }

    override fun openDeeplink() {
        Toast.makeText(this, "Переход по диплинку", Toast.LENGTH_LONG).show()
    }

    private val uris = listOf(
        DetailedStory(
            "https://picsum.photos/200/300",
            "\uD83D\uDD25 Для абонентов действует выгодное предложение!",
            "Мы рекомендуем отталкиваться от своих потребностей. Следуйте своим предпочтениям в карусели\uD83D\uDC46",
            "Мы рекомендуем отталкиваться от своих потребностей. Следуйте своим предпочтениям в карусели\uD83D\uDC46\n" +
                    "\n" +
                    "Не забывайте и о наших специальных тарифах для:\n" +
                    "✅ обучения,\n" +
                    "✅ семьи,\n" +
                    "✅ умных устройств,\n" +
                    "✅ роутеров и модемов,\n" +
                    "✅ регионов.\n" +
                    "\n" +
                    "Подключите на 4 недели всего за 495 сомов вместо 750 сомов! \uD83D\uDCA5\n" +
                    "\uD83D\uDCA1Самое приятное – продлевать действие этого тарифа по сниженной стоимости можно ещё 2 раза."
        ),
        DetailedStory("https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg"),
        DetailedStory("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU")
    )

    private val stories = listOf(
        Story(1, "Получи 10 ГБ", false, StoryType.ALGA, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU", uris),
        Story(2, "Переходи на О!", false, StoryType.GENERAL, "https://picsum.photos/200/300", uris),
        Story(3, "Билеты на Open Air", true, StoryType.GENERAL, "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg", uris),
        Story(4, "Билеты на Open Air", false, StoryType.FOR_YOU, "https://picsum.photos/200/300", uris),
        Story(5, "Билеты на Open Air", false, StoryType.FOR_YOU, "https://picsum.photos/200/300", uris),
        Story(6, "Билеты на Open Air", false, StoryType.GENERAL, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU\"", uris),
        Story(7, "Билеты на Open Air", true, StoryType.GENERAL, "https://picsum.photos/200/300", uris)
    )
}