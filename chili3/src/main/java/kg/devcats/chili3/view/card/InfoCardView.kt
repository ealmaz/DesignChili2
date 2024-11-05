package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardInfoBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible
import kg.devcats.chili3.util.ButtonType

class InfoCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.infoCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_InfoCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardInfoBinding

    private var buttonType: ButtonType = ButtonType.PRIMARY

    override val styleableAttrRes: IntArray = R.styleable.InfoCardView

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardInfoBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.InfoCardView_title))
        setSubtitle(getText(R.styleable.InfoCardView_subtitle))
        setButtonTitle(getText(R.styleable.InfoCardView_btnTitle))
        getLayoutDimension(R.styleable.InfoCardView_btnActionType, -1).takeIf { it != -1 }?.let { type ->
            when (type) {
                ButtonType.PRIMARY.value -> ButtonType.PRIMARY
                else -> ButtonType.ADDITIONAL
            }.also { setButtonType(it) }
        }
        getString(R.styleable.InfoCardView_horizontalPrimaryBtnText)
            ?.let { setHorizontalPrimaryButtonText(it) }
        getString(R.styleable.InfoCardView_horizontalAdditionalBtnText)
            ?.let { setHorizontalAdditionalButtonText(it) }
    }

    fun setTitle(resId: Int) {
        vb.tvTitle.setText(resId)
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.text = title
    }

    fun setSubtitle(resId: Int) {
        vb.tvSubtitle.setText(resId)
    }

    fun setSubtitle(subtitle: CharSequence?) {
        vb.tvSubtitle.text = subtitle
    }

    fun setButtonTitle(resId: Int) {
        vb.btnPrimary.setText(resId)
        vb.btnAdditional.setText(resId)
    }

    fun setButtonTitle(title: CharSequence?) {
        vb.btnPrimary.text = title
        vb.btnAdditional.text = title
    }


    fun setButtonType(buttonType: ButtonType) {
        this.buttonType = buttonType
        when (buttonType) {
            ButtonType.PRIMARY -> {
                vb.btnPrimary.visible()
                vb.btnAdditional.gone()
            }
            ButtonType.ADDITIONAL -> {
                vb.btnPrimary.gone()
                vb.btnAdditional.visible()
            }
        }
    }

    fun setOnPrimaryClickListener(action: () -> Unit) {
        vb.btnPrimary.setOnSingleClickListener(action)
    }

    fun setOnAdditionalClickListener(action: () -> Unit) {
        vb.btnAdditional.setOnSingleClickListener(action)
    }

    fun setHorizontalPrimaryButtonText(title: String) {
        vb.btnHorizontalPrimary.apply {
            visible()
            text = title
        }
    }

    fun setHorizontalAdditionalButtonText(title: String) {
        vb.btnHorizontalAdditional.apply {
            visible()
            text = title
        }
    }

    fun setHorizontalPrimaryButtonListener(onClick: () -> Unit) {
        vb.btnHorizontalPrimary.setOnSingleClickListener(onClick)
    }

    fun setHorizontalAdditionalButtonListener(onClick: () -> Unit) {
        vb.btnHorizontalAdditional.setOnSingleClickListener(onClick)
    }
}