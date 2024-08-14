package com.design2.chili2.view.card

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.view.isInvisible
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBankCardBinding
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setTopMargin
import com.design2.chili2.extensions.visible


class BankCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bankCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_BankCard
) : BaseCardView(context, attrs, defStyleAttr) {


    private var isPanShimmering = false
    private var isCvvShimmering = false
    private var pan: String = ""
    private var panToggleState = CardFieldToggleState.ICON_NONE
    private var cardPanHideDelegate: (CharSequence, Boolean) -> CharSequence =
        { pan: CharSequence, isHidden: Boolean ->
            if (isHidden) {
                try {
                    pan.replaceRange(6..11, " • • •  • •")
                } catch (_: Exception) {
                    pan
                }
            } else pan
        }

    private var cvvToggleState = CardFieldToggleState.ICON_NONE
    private var cvv: String = ""
    private var cardCvvHideDelegate = { pan: CharSequence, isHidden: Boolean ->
        if (isHidden) "• • •"
        else pan
    }

    override val styleableAttrRes: IntArray = R.styleable.BankCardView
    override val rootContainer: View
        get() = vb.rootView

    private lateinit var vb: ChiliViewBankCardBinding

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewBankCardBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        getBoolean(R.styleable.BankCardView_isCardPanToggleEnabled, false).let {
            if (it) setupCardPanToggle()
        }
        getBoolean(R.styleable.BankCardView_isCvvToggleEnabled, false).let {
            if (it) setupCvvToggle()
        }
        getResourceId(R.styleable.BaseCardView_cardBackground, -1).takeIf { it != -1 }?.let {
            setCardBackground(it)
        }
        setCardPan(getText(R.styleable.BankCardView_cardPan))
        setCardCvv(getText(R.styleable.BankCardView_cvv))
        setCardHolderName(getText(R.styleable.BankCardView_cardHolderName))
        setCardDueDate(getText(R.styleable.BankCardView_dueDate))
        setCardIcon(getResourceId(R.styleable.BankCardView_cardIcon, -1).takeIf { it != -1 })
        setStartIcon(getResourceId(R.styleable.BankCardView_startIcon, -1).takeIf { it != -1 })
        setStartIconVisibility(getBoolean(R.styleable.BankCardView_isStartIconVisible, false))

    }

    override fun setupView() {
        super.setupView()
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    fun setCardPan(charSequence: CharSequence?) {
        if (charSequence == null) return
        this.pan = charSequence.toString()
        vb.panShimmer.gone()
        vb.tvCardPan.visible()
        vb.ivPanToggle.visible()
        isPanShimmering = false
        cardPanHideDelegate(charSequence, panToggleState == CardFieldToggleState.ICON_SHOW).let {
            vb.tvCardPan.text = it
        }
    }

    fun setCardCvv(charSequence: CharSequence?) {
        if (charSequence == null) return
        this.cvv = charSequence.toString()
        vb.cvvShimmer.gone()
        vb.tvCvv.visible()
        vb.ivCvvToggle.visible()
        isCvvShimmering = false
        cardCvvHideDelegate(charSequence, cvvToggleState == CardFieldToggleState.ICON_SHOW).let {
            vb.tvCvv.text = it
        }
    }

    fun setCardDueDate(charSequence: CharSequence?) {
        vb.tvDueDate.text = charSequence
    }

    fun setCardHolderName(charSequence: CharSequence?) {
        vb.tvCardHolderName.text = charSequence
    }

    fun setCardPanTextAppearance(textAppearance: Int) {
        vb.tvCardPan.setTextAppearance(textAppearance)
    }

    fun setCardCvvTextAppearance(textAppearance: Int) {
        vb.tvCvv.setTextAppearance(textAppearance)
    }

    fun setCardDueDateTextAppearance(textAppearance: Int) {
        vb.tvDueDate.setTextAppearance(textAppearance)
    }

    fun setCardHolderNameTextAppearance(textAppearance: Int) {
        vb.tvCardHolderName.setTextAppearance(textAppearance)
    }

    fun setCardIcon(drawable: Drawable?) {
        vb.ivCardIcon.apply {
            if (drawable != null) visible()
            else invisible()
            setImageDrawable(drawable)
        }
    }

    fun setCardIcon(resId: Int?) {
        vb.ivCardIcon.apply {
            if (resId != null) {
                visible()
                setImageResource(resId)
            } else invisible()
        }
    }

    fun setStartIcon(resId: Int?) {
        vb.ivStartIcon.apply {
            if (resId != null) {
                visible()
                setImageResource(resId)
            } else invisible()
        }
    }

    fun setStartIcon(drawable: Drawable?) {
        vb.ivStartIcon.apply {
            if (drawable != null) {
                visible()
                setImageDrawable(drawable)
            } else invisible()
        }
    }

    fun setStartIconVisibility(isVisible: Boolean) {
        vb.ivStartIcon.isInvisible = !isVisible
    }

    fun setCardPanHidingDelegate(delegate: ((CharSequence?, Boolean) -> CharSequence)) {
        this.cardPanHideDelegate = delegate
    }

    fun setCvvHidingDelegate(delegate: ((CharSequence?, Boolean) -> CharSequence)) {
        this.cardCvvHideDelegate = delegate
    }

    fun setPanBackgroundTint(@ColorRes color: Int) {
        vb.llCardPan.background.setTint(context.color(color))
    }

    fun setCvvBackgroundTint(@ColorRes color: Int) {
        vb.llCvv.background.setTint(context.color(color))
    }

    fun setPanBackground(@DrawableRes drawable: Int) {
        vb.llCardPan.background = context.drawable(drawable)
    }

    fun setCvvBackground(@DrawableRes drawable: Int) {
        vb.llCvv.background = context.drawable(drawable)
    }

    fun setCvvToggleTint(@ColorRes color: Int) {
        vb.ivCvvToggle.imageTintList = ColorStateList.valueOf(context.color(color))
    }

    fun setPanToggleTint(@ColorRes color: Int) {
        vb.ivPanToggle.imageTintList = ColorStateList.valueOf(context.color(color))
    }

    private fun setupCardPanToggle() = with(vb) {
        ivPanToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        panToggleState = CardFieldToggleState.ICON_SHOW
        llCardPan.isClickable = true
        llCardPan.isFocusable = true
        llCardPan.setOnClickListener {
            if (panToggleState == CardFieldToggleState.ICON_SHOW) {
                panToggleState = CardFieldToggleState.ICON_COPY
                ivPanToggle.setImageResource(R.drawable.chili_ic_copy)
                tvCardPan.text = pan
            } else {
                copyText(pan)
            }
        }
    }

    private fun setupCvvToggle() = with(vb) {
        ivCvvToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        cvvToggleState = CardFieldToggleState.ICON_SHOW
        llCvv.isClickable = true
        llCvv.isFocusable = true
        llCvv.setOnClickListener {
            if (cvvToggleState == CardFieldToggleState.ICON_SHOW) {
                cvvToggleState = CardFieldToggleState.ICON_COPY
                ivCvvToggle.setImageResource(R.drawable.chili_ic_copy)
                tvCvv.text = cvv
            } else {
                copyText(cvv)
            }
        }
    }

    fun setupCardPanToggle(onClick: () -> Unit) = with(vb) {
        ivPanToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        panToggleState = CardFieldToggleState.ICON_SHOW
        llCardPan.isClickable = true
        llCardPan.isFocusable = true
        llCardPan.setOnClickListener {
            if (isPanShimmering) return@setOnClickListener
            if (panToggleState == CardFieldToggleState.ICON_SHOW) {
                onClick()
            }
            togglePanToggleState()
        }
    }

    fun resetPanToggleState() {
        this.panToggleState = CardFieldToggleState.ICON_NONE
        togglePanToggleState()
    }

    fun togglePanToggleState() = with(vb) {
        if (panToggleState == CardFieldToggleState.ICON_SHOW) {
            isPanShimmering = true
            tvCardPan.gone()
            panShimmer.visible()
            ivPanToggle.invisible()
            panToggleState = CardFieldToggleState.ICON_COPY
            ivPanToggle.setImageResource(R.drawable.chili_ic_copy)
        } else {
            if (panToggleState == CardFieldToggleState.ICON_COPY) copyText(pan)
            panToggleState = CardFieldToggleState.ICON_SHOW
            ivPanToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
            setCardPan(pan)
        }
    }

    fun setupCvvToggle(onClick: () -> Unit) = with(vb) {
        ivCvvToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        cvvToggleState = CardFieldToggleState.ICON_SHOW
        llCvv.isClickable = true
        llCvv.isFocusable = true
        llCvv.setOnClickListener {
            if (isCvvShimmering) return@setOnClickListener
            if (cvvToggleState == CardFieldToggleState.ICON_SHOW) {
                onClick()
            }
            toggleCvvToggleState()
        }
    }

    fun resetCvvToggleState() {
        this.cvvToggleState = CardFieldToggleState.ICON_NONE
        toggleCvvToggleState()
    }

    fun toggleCvvToggleState() = with(vb) {
        if (cvvToggleState == CardFieldToggleState.ICON_SHOW) {
            isCvvShimmering = true
            tvCvv.gone()
            ivCvvToggle.invisible()
            cvvShimmer.visible()
            cvvToggleState = CardFieldToggleState.ICON_COPY
            ivCvvToggle.setImageResource(R.drawable.chili_ic_copy)
        } else {
            if (cvvToggleState == CardFieldToggleState.ICON_COPY) copyText(cvv)
            cvvToggleState = CardFieldToggleState.ICON_SHOW
            ivCvvToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
            setCardCvv(cvv)
        }
    }

    private fun copyText(text: String) {
        val clipboard: ClipboardManager? =
            context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(text, text)
        clipboard?.setPrimaryClip(clip)
        Toast.makeText(context, R.string.chili_copied_to_clipboard, Toast.LENGTH_SHORT).show()
    }

    fun setPanPinFieldYOffset(marginTop: Int) {
        vb.llCardPan.setTopMargin(marginTop.dp)
    }

    override fun setCardBackground(drawable: Drawable) {
        vb.ivCardBg.setImageDrawable(drawable)
    }

    override fun setCardBackground(resId: Int) {
        vb.ivCardBg.setImageResource(resId)
    }

    fun setCardBackground(url: String?) {
        val screenWidth = context.resources.displayMetrics.widthPixels
        val fixingWidth = context.resources.getDimension(R.dimen.view_48dp).toInt() // Суммарный размер отступов с обеих сторон
        val newWidth = screenWidth - fixingWidth
        val heightPx = resources.getDimension(R.dimen.view_200dp).toInt()

        vb.ivCardBg.setImageByUrl(url, newWidth, heightPx)
    }
}

enum class CardFieldToggleState {
    ICON_SHOW, ICON_COPY, ICON_NONE
}