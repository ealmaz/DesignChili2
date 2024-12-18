package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.isVisible
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setTopMargin
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardBlockInfoBinding
import kg.devcats.chili3.extensions.getColorFromAttr
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible

class InfoBlockCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.chiliInfoBlockCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_InfoBlockCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardBlockInfoBinding

    override val styleableAttrRes: IntArray = R.styleable.ChiliInfoBlockCardView

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardBlockInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        decideCardState(getInteger(R.styleable.ChiliInfoBlockCardView_cardState, 0))
        setTitleTextAppearance(getResourceId(R.styleable.ChiliInfoBlockCardView_titleTextAppearance, 0))
        setSubtitleTextAppearance(getResourceId(R.styleable.ChiliInfoBlockCardView_subtitleTextAppearance, 0))
        setTitle(getText(R.styleable.ChiliInfoBlockCardView_title))
        setSubtitle(getText(R.styleable.ChiliInfoBlockCardView_subtitle))
        setActionButtonTitle(getText(R.styleable.ChiliInfoBlockCardView_btnTitle))
        setActionButtonVisibility(getBoolean(R.styleable.ChiliInfoBlockCardView_isButtonVisible, false))
        setCloseButtonVisibility(getBoolean(R.styleable.ChiliInfoBlockCardView_isCloseIconVisible, false))
    }

    override fun setupView() {
        super.setupView()
        setIsSurfaceClickable(false)
    }

    private fun decideCardState(value: Int) {
        when (value) {
            CardState.NEUTRAL.value -> CardState.NEUTRAL
            CardState.WARNING.value -> CardState.WARNING
            CardState.ERROR.value -> CardState.ERROR
            CardState.SUCCESS.value -> CardState.SUCCESS
            else -> CardState.NEUTRAL
        }.also { setupCardState(it) }
    }

    fun setupCardState(cardState: CardState) = with(vb) {
        when (cardState) {
            CardState.NEUTRAL -> {
                llRoot.background = context.drawable(R.drawable.chili_info_block_bg_neutral)
                btnAction.background = context.drawable(R.drawable.chili_bg_info_block_btn_neutral)
                ivCloseIcon.setColorFilter(context.getColorFromAttr(R.attr.ChiliInfoBlockViewContentColorNeutral))
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorNeutral))
                tvSubtitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorNeutral))
                ivIcon.setImageResource(R.drawable.chili_ic_information)
            }

            CardState.WARNING -> {
                llRoot.background = context.drawable(R.drawable.chili_info_block_bg_warning)
                btnAction.background = context.drawable(R.drawable.chili_bg_info_block_btn_warning)
                ivCloseIcon.setColorFilter(context.getColorFromAttr(R.attr.ChiliInfoBlockViewContentColorWarning))
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorWarning))
                tvSubtitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorWarning))
                ivIcon.setImageResource(R.drawable.chili_ic_caution)
            }

            CardState.ERROR -> {
                llRoot.background = context.drawable(R.drawable.chili_info_block_bg_error)
                btnAction.background = context.drawable(R.drawable.chili_bg_info_block_btn_error)
                ivCloseIcon.setColorFilter(context.getColorFromAttr(R.attr.ChiliInfoBlockViewContentColorError))
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorError))
                tvSubtitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorError))
                ivIcon.setImageResource(R.drawable.chili_ic_error)
            }

            CardState.SUCCESS -> {
                llRoot.background = context.drawable(R.drawable.chili_info_block_bg_success)
                btnAction.background = context.drawable(R.drawable.chili_bg_info_block_btn_success)
                ivCloseIcon.setColorFilter(context.getColorFromAttr(R.attr.ChiliInfoBlockViewContentColorSuccess))
                tvTitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorSuccess))
                tvSubtitle.setTextColor(context.getColorFromAttr(R.attr.ChiliInfoBlockViewTextColorSuccess))
                ivIcon.setImageResource(R.drawable.chili3_ic_success)
            }
        }
    }

    fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.setTextOrHide(resId)
        setupSubtitleMargin()
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.setTextOrHide(title)
        setupSubtitleMargin()
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.setTextAppearance(resId)
    }

    fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.setTextOrHide(resId)
        setupSubtitleMargin()
    }

    fun setSubtitle(subtitle: CharSequence?) {
        vb.tvSubtitle.setTextOrHide(subtitle)
        setupSubtitleMargin()
    }

    private fun setupSubtitleMargin() = with(vb) {
        if (tvTitle.isVisible) tvSubtitle.setTopMargin(4.dp) else tvSubtitle.setTopMargin(0)
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setActionButtonTitle(@StringRes resId: Int) {
        vb.btnAction.run { visible(); setText(resId) }
    }

    fun setActionButtonTitle(title: CharSequence?) {
        vb.btnAction.run { visible(); text = title }
    }

    fun setActionButtonVisibility(isVisible: Boolean) {
        vb.btnAction.isVisible = isVisible
    }

    fun setupActionButton(title: CharSequence, onClick: () -> Unit) {
        vb.btnAction.run {
            visible()
            text = title
            setOnSingleClickListener { onClick() }
        }
    }

    fun setupActionButton(@StringRes resId: Int, onClick: () -> Unit) {
        vb.btnAction.run {
            visible()
            setText(resId)
            setOnSingleClickListener { onClick() }
        }
    }

    fun setCloseButtonVisibility(isVisible: Boolean) {
        vb.ivCloseIcon.isVisible = isVisible
        if (isVisible) setOnCloseButtonClickListener { vb.root.gone() }
    }

    fun setOnCloseButtonClickListener(onClick: () -> Unit) {
        vb.ivCloseIcon.run { visible(); setOnSingleClickListener { onClick() } }
    }

    enum class CardState(val value: Int) {
        NEUTRAL(0), WARNING(1), ERROR(2), SUCCESS(3)
    }
}
