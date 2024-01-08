package com.design2.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.design2.app.databinding.ActivityStoriesBinding
import com.design2.chili2.view.story.ButtonModel
import com.design2.chili2.view.story.DetailedStory
import com.design2.chili2.view.story.HighlightsAdapter
import com.design2.chili2.view.story.OnHighlightClickListener
import com.design2.chili2.view.story.Story
import com.design2.chili2.view.story.StoryCallbackListener
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

    override fun openDeeplink(deeplink: String) {
        Toast.makeText(this, deeplink, Toast.LENGTH_LONG).show()
    }

    private val uris = listOf(
        DetailedStory(
            1,
            1,
            false,
            "https://picsum.photos/200/300",
            "\uD83D\uDD25 Для абонентов действует выгодное предложение!",
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
                    "\uD83D\uDCA1Самое приятное – продлевать действие этого тарифа по сниженной стоимости можно ещё 2 раза.",
            ButtonModel("Детали", "Переход по диплинку со сториса 1", "#ffffff", "#f0047f")
        ),
        DetailedStory(
            2,
            3,
            false,
            "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg",
            null,
            null,
            ButtonModel("Детали", "Переход по диплинку со сториса 3", "#ffffff", "#f0047f")
        ),
        DetailedStory(
            3,
            2,
            false,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU",
            "just a title so you know...",
            null,
            ButtonModel("Детали", "Переход по диплинку со сториса 2", "#ffffff", "#82B752")
        )
    )

    private val stories = listOf(
        Story(2, 2, "Переходи на О!", false, "https://picsum.photos/200/300", uris, listOf("#FF0051", "#1C67F8")),
        Story(3, 3, "Билеты на Open Air", false, "https://cdn.pixabay.com/photo/2015/04/19/08/32/marguerite-729510__340.jpg", uris),
        Story(4, 4, "Билеты на Open Air", false, "https://picsum.photos/200/300", uris, listOf("#FF0051", "#1C67F8")),
        Story(5, 5, "Билеты на Open Air", false, "https://picsum.photos/200/300", uris),
        Story(1, 1, "Получи 10 ГБ", true, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU", uris, listOf("#FF0051")),
        Story(6, 6, "Билеты на Open Air", false, "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU\"", uris),
        Story(7, 7, "Билеты на Open Air", false, "https://picsum.photos/200/300", uris)
    )
}