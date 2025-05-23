package kg.devcats.chili3.view.card

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.StyleRes
import com.design2.chili2.extensions.setupAsSecure
import com.design2.chili2.view.card.BaseCardView
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliCardHorizontalDividerContentBinding
import kg.devcats.chili3.databinding.ChiliViewCardHorizontalDividerBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible

class CardHorizontalDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cardHorizontalDividerStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CardHorizontalDivider
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardHorizontalDividerBinding

    override val rootContainer: View
        get() = vb.root

    override val styleableAttrRes: IntArray
        get() = R.styleable.CardHorizontalDivider

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardHorizontalDividerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        shimmeringPairs[vb.sectionsContainer] = vb.sectionShimmer
    }

    override fun onStartShimmer() {
        super.onStartShimmer()
        vb.sectionsContainer.gone()
    }

    override fun onStopShimmer() {
        super.onStopShimmer()
        vb.sectionsContainer.visible()
    }

    fun clearSections() = vb.sectionsContainer.removeAllViews()

    fun setSections(sectionList: List<DividerCardSection>) {
        clearSections()
        sectionList.forEachIndexed { index, section ->
            val sectionBinding = ChiliCardHorizontalDividerContentBinding.inflate(LayoutInflater.from(context), vb.sectionsContainer, false)

            with(sectionBinding) {
                tvTitle.apply {
                    text = section.title
                    setTextAppearance(section.titleTextAppearance)
                }
                tvSubtitle.apply {
                    text = section.subtitle
                    setTextAppearance(section.subtitleTextAppearance)
                    if (section.isSecurable) setupAsSecure()
                }
                dividerView.apply {
                    if (index > 0 && index < sectionList.size) visible()
                    else gone()
                }
                vb.sectionsContainer.addView(root)
            }
        }
    }
}

data class DividerCardSection(
    val title: CharSequence? = null,
    val subtitle: CharSequence? = null,
    @StyleRes val titleTextAppearance: Int = R.style.Chili_H12_Secondary,
    @StyleRes val subtitleTextAppearance: Int = R.style.Chili_H16_Primary_Bold,
    val isSecurable: Boolean = false
)