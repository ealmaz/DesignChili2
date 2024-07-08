package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.util.IconSize
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardStoriesBinding
import kg.devcats.chili3.util.StoriesStatus

class StoriesCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.storiesCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_StoriesCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardStoriesBinding

    private var imageSize: Pair<Int, Int> = Pair(0, 0)

    var storiesStatus = mutableListOf(
        StoriesStatus.VIEWED to context.drawable(R.drawable.chili_bg_viewed_stories),
        StoriesStatus.UNVIEWED to context.drawable(R.drawable.chili_bg_unviewed_stories),
    )

    override val styleableAttrRes: IntArray = R.styleable.StoriesCardView

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardStoriesBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        getLayoutDimension(R.styleable.StoriesCardView_android_layout_width, 0).let {
            imageSize = it to imageSize.second
        }
        getLayoutDimension(R.styleable.StoriesCardView_android_layout_height, 0).let {
            imageSize = imageSize.first to it
        }
        getLayoutDimension(R.styleable.StoriesCardView_cardIconSize, 0).let {
            when (it) {
                IconSize.SMALL.value -> setIconSize(IconSize.SMALL)
                IconSize.MEDIUM.value -> setIconSize(IconSize.MEDIUM)
                IconSize.LARGE.value -> setIconSize(IconSize.LARGE)
            }
        }
        getInteger(R.styleable.StoriesCardView_android_scaleType, ScaleType.FIT_CENTER.ordinal).let {
            setScaleType(it)
        }
        getBoolean(R.styleable.StoriesCardView_android_adjustViewBounds, false).let {
            setAdjustViewBounds(it)
        }
        getString(R.styleable.StoriesCardView_storiesSrc)?.let {
            setStories(it)
        }
        getDrawable(R.styleable.StoriesCardView_viewedBorder)?.let {
            updateStatusBorder(StoriesStatus.VIEWED, it)
        }
        getDrawable(R.styleable.StoriesCardView_unviewedBorder)?.let {
            updateStatusBorder(StoriesStatus.UNVIEWED, it)
        }
        getLayoutDimension(R.styleable.StoriesCardView_storiesStatus, StoriesStatus.UNVIEWED.value).let {
            setStatus(it)
        }
    }

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.viewStatus] = vb.viewStatusShimmer
        shimmeringPairs[vb.cvStories] = null
    }

    private fun updateStatusBorder(status: StoriesStatus, newDrawable: Drawable) {
        val index = storiesStatus.indexOfFirst { it.first == status }
        if (index != -1) {
            storiesStatus[index] = status to newDrawable
        }
    }

    fun setStories(src: String) {
        vb.ivStories.setImageByUrl(src, imageSize.first - 4.dp, imageSize.second - 4.dp)
    }

    fun setStatus(status: Int) {
        vb.viewStatus.background = storiesStatus[status].second
    }

    fun setScaleType(scaleType: Int) {
        vb.ivStories.scaleType = ImageView.ScaleType.values()[scaleType]
    }

    fun setAdjustViewBounds(isAdjusted: Boolean) {
        vb.ivStories.adjustViewBounds = isAdjusted
    }

    private fun setCustomViewSize(width: Int, height: Int) {
        val layoutParams = vb.flContainer.layoutParams
        layoutParams.width = width
        layoutParams.height = height
        vb.flContainer.layoutParams = layoutParams
    }

    fun setIconSize(iconSize: IconSize) {
        val (width, height) = when (iconSize) {
            IconSize.SMALL -> {
                val size = 88.dp to 78.dp
                imageSize = size
                size
            }
            IconSize.MEDIUM -> {
                val size = 128.dp to 78.dp
                imageSize = size
                size
            }
            IconSize.LARGE -> {
                val size = 163.dp to 78.dp
                imageSize = size
                size
            }
        }
        setCustomViewSize(width, height)
    }

    override fun setOnClickListener(listener: OnClickListener?) {
        vb.root.setOnClickListener(listener)
    }

}