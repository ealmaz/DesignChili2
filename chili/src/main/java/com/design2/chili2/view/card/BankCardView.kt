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
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.visible
import com.facebook.shimmer.ShimmerFrameLayout


class BankCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.bankCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_BankCard
) : BaseCardView(context, attrs, defStyleAttr) {

    private var pan: String = ""
    private var panToggleState = CardFieldToggleState.ICON_NONE
    private var cardPanHideDelegate = {pan: CharSequence, isHidden: Boolean ->
        if (isHidden) {
            try { pan.replaceRange(6..11, " • • •  • •") }
            catch (_: Exception) {}
            pan

        } else pan
    }

    private var cvvToggleState = CardFieldToggleState.ICON_NONE
    private var cvv: String = ""
    private var cardCvvHideDelegate = {pan: CharSequence, isHidden: Boolean ->
        if (isHidden) "• • •"
        else pan
    }

    override val styleableAttrRes: IntArray = R.styleable.BankCardView
    override val rootContainer: View
        get() = view.rootContainer

    private lateinit var view: BankCardViewVariables

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_bank_card, this, true)
        this.view = BankCardViewVariables(
            tvCardHolderName = view.findViewById(R.id.tv_card_holder_name),
            tvCardPan = view.findViewById(R.id.tv_card_pan),
            tvCvv = view.findViewById(R.id.tv_cvv),
            tvDueDate = view.findViewById(R.id.tv_due_date),
            ivIcon = view.findViewById(R.id.iv_card_icon),
            rootContainer = view.findViewById(R.id.root_view),
            ivPanToggle = view.findViewById(R.id.iv_pan_toggle),
            ivCvvToggle = view.findViewById(R.id.iv_cvv_toggle),
            llPan = view.findViewById(R.id.ll_card_pan),
            llCvv = view.findViewById(R.id.ll_cvv),
            panShimmer = view.findViewById(R.id.pan_shimmer),
            cvvShimmer = view.findViewById(R.id.cvv_shimmer)
        )
    }

    override fun TypedArray.obtainAttributes() {
        getBoolean(R.styleable.BankCardView_isCardPanToggleEnabled, false).let {
            if (it) setupCardPanToggle()
        }
        getBoolean(R.styleable.BankCardView_isCvvToggleEnabled, false).let {
            if (it) setupCvvToggle()
        }
        setCardPan(getText(R.styleable.BankCardView_cardPan))
        setCardCvv(getText(R.styleable.BankCardView_cvv))
        setCardHolderName(getText(R.styleable.BankCardView_cardHolderName))
        setCardDueDate(getText(R.styleable.BankCardView_dueDate))
        setCardIcon(getResourceId(R.styleable.BankCardView_cardIcon, -1).takeIf { it != -1 })
    }

    override fun setupView() {
        super.setupView()
        setCardBackgroundColor(Color.TRANSPARENT)
    }

    fun setCardPan(charSequence: CharSequence?) {
        if (charSequence == null) return
        this.pan = charSequence.toString()
        view.panShimmer.gone()
        view.tvCardPan.visible()
        cardPanHideDelegate(charSequence, panToggleState == CardFieldToggleState.ICON_SHOW).let {
            view.tvCardPan.text = it
        }
    }

    fun setCardCvv(charSequence: CharSequence?) {
        if (charSequence == null) return
        this.cvv = charSequence.toString()
        view.cvvShimmer.gone()
        view.tvCvv.visible()
       cardCvvHideDelegate(charSequence, cvvToggleState == CardFieldToggleState.ICON_SHOW).let {
           view.tvCvv.text = it
       }
    }

    fun setCardDueDate(charSequence: CharSequence?) {
        view.tvDueDate.text = charSequence
    }

    fun setCardHolderName(charSequence: CharSequence?) {
        view.tvCardHolderName.text = charSequence
    }

    fun setCardPanTextAppearance(textAppearance: Int) {
        view.tvCardPan.setTextAppearance(textAppearance)
    }

    fun setCardCvvTextAppearance(textAppearance: Int) {
        view.tvCvv.setTextAppearance(textAppearance)
    }

    fun setCardDueDateTextAppearance(textAppearance: Int) {
        view.tvDueDate.setTextAppearance(textAppearance)
    }

    fun setCardHolderNameTextAppearance(textAppearance: Int) {
        view.tvCardHolderName.setTextAppearance(textAppearance)
    }

    fun setCardIcon(drawable: Drawable?) {
        view.ivIcon.apply {
            if (drawable != null) visible()
            else invisible()
            setImageDrawable(drawable)
        }
    }

    fun setCardIcon(resId: Int?) {
        view.ivIcon.apply {
            if (resId != null) {
                visible()
                setImageResource(resId)
            }
            else invisible()
        }
    }

    fun setCardPanHidingDelegate(delegate :((CharSequence?, Boolean) -> CharSequence)) {
        this.cardPanHideDelegate = delegate
    }

    fun setCvvHidingDelegate(delegate :((CharSequence?, Boolean) -> CharSequence)) {
        this.cardCvvHideDelegate = delegate
    }

    fun setPanBackgroundTint(@ColorRes color: Int) {
        view.llPan.background.setTint(context.color(color))
    }

    fun setCvvBackgroundTint(@ColorRes color: Int) {
        view.llCvv.background.setTint(context.color(color))
    }

    fun setPanBackground(@DrawableRes drawable: Int) {
        view.llPan.background = context.drawable(drawable)
    }

    fun setCvvBackground(@DrawableRes drawable: Int) {
        view.llCvv.background = context.drawable(drawable)
    }

    fun setCvvToggleTint(@ColorRes color: Int) {
        view.ivCvvToggle.imageTintList = ColorStateList.valueOf(context.color(color))
    }

    fun setPanToggleTint(@ColorRes color: Int) {
        view.ivPanToggle.imageTintList = ColorStateList.valueOf(context.color(color))
    }

    private fun setupCardPanToggle() = with(view) {
        ivPanToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        panToggleState = CardFieldToggleState.ICON_SHOW
        llPan.isClickable = true
        llPan.isFocusable = true
        llPan.setOnClickListener {
            if (panToggleState == CardFieldToggleState.ICON_SHOW) {
                panToggleState = CardFieldToggleState.ICON_COPY
                ivPanToggle.setImageResource(R.drawable.chili_ic_copy)
                tvCardPan.text = pan
            } else {
                copyText(pan)
            }
        }
    }

    private fun setupCvvToggle() = with(view) {
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

    fun setupCardPanToggle(onClick: () -> Unit) = with(view) {
        ivPanToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        panToggleState = CardFieldToggleState.ICON_SHOW
        llPan.isClickable = true
        llPan.isFocusable = true
        llPan.setOnClickListener {
            if (panToggleState == CardFieldToggleState.ICON_SHOW) {
                tvCardPan.gone()
                panShimmer.visible()
                panToggleState = CardFieldToggleState.ICON_COPY
                ivPanToggle.setImageResource(R.drawable.chili_ic_copy)
                onClick()
            } else {
                copyText(pan)
            }
        }
    }

    fun setupCvvToggle(onClick: () -> Unit) = with(view) {
        ivCvvToggle.setImageResource(R.drawable.chili_password_toggle_drawable)
        cvvToggleState = CardFieldToggleState.ICON_SHOW
        llCvv.isClickable = true
        llCvv.isFocusable = true
        llCvv.setOnClickListener {
            if (cvvToggleState == CardFieldToggleState.ICON_SHOW) {
                tvCvv.gone()
                cvvShimmer.visible()
                cvvToggleState = CardFieldToggleState.ICON_COPY
                ivCvvToggle.setImageResource(R.drawable.chili_ic_copy)
                onClick()
            } else {
                copyText(cvv)
            }
        }
    }

    private fun copyText(text: String) {
        val clipboard: ClipboardManager? = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
        val clip = ClipData.newPlainText(text, text)
        clipboard?.setPrimaryClip(clip)
    }

}

data class BankCardViewVariables(
    val tvCardPan: TextView,
    val tvDueDate: TextView,
    val tvCvv: TextView,
    val tvCardHolderName: TextView,
    val ivIcon: ImageView,
    val ivCvvToggle: ImageView,
    val ivPanToggle: ImageView,
    val llCvv: ConstraintLayout,
    val llPan: ConstraintLayout,
    val rootContainer: ConstraintLayout,
    val panShimmer: ShimmerFrameLayout,
    val cvvShimmer: ShimmerFrameLayout
)

enum class CardFieldToggleState {
    ICON_SHOW, ICON_COPY, ICON_NONE
}