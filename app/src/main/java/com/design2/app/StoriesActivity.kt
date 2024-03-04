package com.design2.app

import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
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
    private var currentClickedView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setupViews()
    }

    private fun setupViews () {
        val adapter = HighlightsAdapter(this)
        adapter.addItems(stories)
        vb.rvHighlights.adapter = adapter
    }

    private fun initPager() {
        val adapter = StoriesPagerAdapter(this, stories, this)
        vb.viewPager.apply {
            this.isVisible = true
            this.adapter = adapter
            offscreenPageLimit = 7
            setCurrentItem(currentIndex, false)
        }
    }

    override fun onHighlightClick(clickedView: View, index: Int) {
        currentIndex = index
        currentClickedView = clickedView
        initPager()
        showStory(clickedView)
    }

    override fun closingStory() {
        vb.rvHighlights.apply {
            smoothScrollToPosition(vb.viewPager.currentItem)
            postDelayed({
                updateCurrentStoryView()
                currentClickedView?.let { closeStory(it) }
                currentClickedView = null
            }, 100)
        }
    }

    override fun viewPagerSwipes(isEnabled: Boolean) { vb.viewPager.isUserInputEnabled = isEnabled }

    override fun onAllStoriesViewClick() {
        Toast.makeText(this, "Переход на экран событий!", Toast.LENGTH_SHORT).show()
    }

    override fun goToNextFragment() {
        updateCurrentStoryView()
        if (vb.viewPager.currentItem + 1 < stories.size)
            vb.viewPager.setCurrentItem(vb.viewPager.currentItem + 1, true)
        else closingStory()
    }

    override fun goToPrevFragment() {
        updateCurrentStoryView()
        if (vb.viewPager.currentItem > 0)
            vb.viewPager.setCurrentItem(vb.viewPager.currentItem - 1, true)
    }

    override fun openDeeplink(deeplink: String) {
        Toast.makeText(this, deeplink, Toast.LENGTH_LONG).show()
    }

    private fun updateCurrentStoryView() =
        vb.rvHighlights.findViewHolderForAdapterPosition(vb.viewPager.currentItem)?.let {
            currentClickedView = it.itemView
        }

    private fun showStory(clickedView: View, duration: Long = 310L) {
        val location = IntArray(2).apply { clickedView.getLocationOnScreen(this) }
        vb.viewPager.apply {
            pivotX = 0f
            pivotY = 0f
            scaleX = clickedView.width.toFloat() / vb.root.width
            scaleY = clickedView.height.toFloat() / vb.root.height
            translationX = location[0].toFloat()
            translationY = location[1].toFloat()
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .translationX(0f)
                .translationY(0f)
                .setDuration(duration)
                .setInterpolator(AccelerateDecelerateInterpolator())
                .start()
        }
    }

    private fun closeStory(clickedView: View, duration: Long = 180L) {
        val location = IntArray(2).apply { clickedView.getLocationOnScreen(this) }
        vb.viewPager.animate()
            .scaleX(clickedView.width.toFloat() / vb.root.width)
            .scaleY(clickedView.height.toFloat() / vb.root.height)
            .translationX(location[0].toFloat())
            .translationY((location[1] / 2).toFloat())
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .withEndAction { vb.viewPager.isVisible = false }
            .start()
    }

    override fun onDestroy() {
        super.onDestroy()
        vb.viewPager.adapter = null
    }

    private val uris = listOf(
        DetailedStory(
            1,
            1,
            false,
            "https://flomaster.top/uploads/posts/2023-10/1697556295_flomaster-top-p-priroda-illyustratsiya-instagram-1.jpg",
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
            "https://img.gazeta.ru/files3/225/15619225/602fd8501fa16_img-pic_32ratio_1200x800-1200x800-70042.jpg",
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
        Story(
            1,
            1,
            "1-Получи 10 ГБ",
            true,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcRCv_Gx6Fde6mja_lLmll0fzrxRvcKLHGrPxnqMrQLWKqXi9IYy&usqp=CAU",
            uris,
            listOf("#FF0051")
        ),
        Story(
            2,
            2,
            "2-Переходи на О!",
            false,
            "https://picsum.photos/200/300",
            uris,
            listOf("#FF0051", "#1C67F8")
        ),
        Story(
            3,
            3,
            "3-Билеты на Open Air",
            false,
            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2ecYTDMQHg-Q7GrDtvXE7VKQNASRMChOvQR1EZyFMKHQAIwCKwqyB8HTvFbENV0e2tBk&usqp=CAU",
            uris
        ),
        Story(
            4,
            4,
            "4-Билеты на Open Air",
            false,
            "https://flomaster.top/uploads/posts/2023-10/1697556295_flomaster-top-p-priroda-illyustratsiya-instagram-1.jpg",
            uris,
            listOf("#FF0051", "#1C67F8")
        ),
        Story(
            5,
            5,
            "5-Билеты на Open Air",
            false,
            "https://www.novochag.ru/upload/img_cache/276/276da953d07c68f7614b96b9a77be396_ce_3750x2500x0x0_cropped_666x444.jpg",
            uris
        ),
        Story(
            6,
            6,
            "6-Билеты на Open Air",
            false,
            "https://s0.rbk.ru/v6_top_pics/media/img/3/52/756723200429523.jpg",
            uris
        ),
        Story(
            7,
            7,
            "7-Билеты на Open Air",
            false,
            "https://img.gazeta.ru/files3/225/15619225/602fd8501fa16_img-pic_32ratio_1200x800-1200x800-70042.jpg",
            uris
        )
    )

}