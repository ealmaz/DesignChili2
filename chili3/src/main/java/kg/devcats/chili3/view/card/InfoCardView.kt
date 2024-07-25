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
        setButtonTitle(getText(R.styleable.InfoCardView_buttonTitle))
        getLayoutDimension(R.styleable.InfoCardView_buttonType, 0).let { type ->
            when (type) {
                ButtonType.PRIMARY.value -> ButtonType.PRIMARY
                else -> ButtonType.ADDITIONAL
            }.also { setButtonType(it) }
        }
    }

    fun setTitle(title: CharSequence?) {
        vb.tvTitle.text = title
    }

    fun setSubtitle(subtitle: CharSequence?) {
        vb.tvSubtitle.text = subtitle
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

}