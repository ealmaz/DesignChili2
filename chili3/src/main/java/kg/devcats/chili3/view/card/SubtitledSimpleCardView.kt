package kg.devcats.chili3.view.card

import com.design2.chili2.view.card.BaseCardView
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import com.design2.chili2.extensions.setImageByUrl
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardSimpleSubtitledBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.invisible
import kg.devcats.chili3.extensions.visible

class SimpleCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.subtitledSimpleCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_SubtitledSimpleCardView
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardSimpleSubtitledBinding

    override val styleableAttrRes: IntArray = R.styleable.SubtitledSimpleCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewCardSimpleSubtitledBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init { initView(context, attrs, defStyleAttr, defStyleRes) }


    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.SubtitledSimpleCardView_title))
        getResourceId(R.styleable.SubtitledSimpleCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setTitleTextAppearance(it) }
        getResourceId(R.styleable.SubtitledSimpleCardView_icon, -1)
            .takeIf { it != -1 }?.let { setIcon(it) }
        getResourceId(R.styleable.SubtitledSimpleCardView_iconSecondary, -1)
            .takeIf { it != -1 }?.let { setSecondaryIcon(it) }
        setSubtitle(getString(R.styleable.SubtitledSimpleCardView_subtitle))
        getResourceId(R.styleable.SubtitledSimpleCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let { setSubtitleTextAppearance(it) }
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        with(vb) {
            shimmeringPairs[ivIcon] = viewIconShimmer
            shimmeringPairs[tvTitle] = viewTitleShimmer
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitleTextAppearance(resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setIcon(resId: Int) {
        vb.ivIcon.setImageResource(resId)
    }

    fun setIcon(url: String) {
        vb.ivIcon.setImageByUrl(url)
    }

    fun setIcon(drawable: Drawable) {
        vb.ivIcon.setImageDrawable(drawable)
    }

    fun setIconVisibility(isVisible: Boolean) {
        vb.ivIcon.run { if (isVisible) visible() else invisible() }
    }

    fun setEmoji(emoji: String) {
        vb.tvEmoji.text = emoji
    }

    fun setEmojiVisibility(isVisible: Boolean) {
        vb.tvEmoji.run { if (isVisible) visible() else gone() }
    }

    fun setSubtitle(charSequence: CharSequence?) = with(vb) {
        charSequence?.let { tvSubtitle.visible() } ?: tvSubtitle.gone()
        tvSubtitle.text = charSequence
    }

    fun setSubtitle(text: String) = with(vb) {
        tvSubtitle.visible()
        tvSubtitle.text = text
    }

    fun setSubtitleTextAppearance(resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setSecondaryIcon(@DrawableRes resId: Int) = with(vb) {
        ivIconSecondary.visible()
        ivIconSecondary.setImageResource(resId)
    }

    fun hideSecondaryIcon(){
        vb.ivIconSecondary.gone()
    }

    fun hideSubtitle(){
        vb.tvSubtitle.gone()
    }
}