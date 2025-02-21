package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.StyleRes
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.dp
import com.design2.chili2.view.card.BaseCardView
import com.google.android.material.divider.MaterialDivider
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardHorizontalDividerBinding

class CardHorizontalDivider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.cardHorizontalDividerStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_CardHorizontalDivider
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardHorizontalDividerBinding
    override val styleableAttrRes: IntArray = R.styleable.CardHorizontalDivider
    init { initView(context, attrs, defStyleAttr, defStyleRes) }

    override fun inflateView(context: Context) {
        vb = ChiliViewCardHorizontalDividerBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun TypedArray.obtainAttributes() {
    }

    fun setSections(sectionList: List<DividerCardSection>) {
        sectionList.forEachIndexed { index, section ->
            val sectionContainer = createLinearLayoutContainer(orientation = LinearLayout.HORIZONTAL)
            val contentContainer = createLinearLayoutContainer(orientation = LinearLayout.VERTICAL)
            val titleTV = section.title?.let(::createTextView)
            val subTitle = section.subtitle?.let(::createTextView)
            val verticalDivider = index.takeIf { it > 0 && it < sectionList.size }?.let {
                createVerticalDivider()
            }

            titleTV?.let(contentContainer::addView)
            subTitle?.let(contentContainer::addView)
            verticalDivider?.let(sectionContainer::addView)
            sectionContainer.addView(contentContainer)

            vb.sectionsContainer.addView(sectionContainer)
        }
    }

    private fun createVerticalDivider() = MaterialDivider(context).apply {
        layoutParams = ViewGroup.LayoutParams(1.dp, ViewGroup.LayoutParams.MATCH_PARENT)
        setDividerColorResource(R.color.c_E0E0E5)
    }

    private fun createLinearLayoutContainer(orientation: Int) = LinearLayout(context).apply {
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        layoutParams.weight = 1f
        this.layoutParams = layoutParams
        this.orientation = orientation
    }

    private fun createTextView(textView: DividerTextView): TextView = TextView(context).apply {
        text = textView.text
        maxEms = 8
        maxLines = 1
        ellipsize = TextUtils.TruncateAt.END
        setTextAppearance(textView.textAppearance)
        textView.textColorRes?.let { setTextColor(context.color(it)) }
        gravity = Gravity.CENTER_HORIZONTAL
    }
}

data class DividerCardSection(
    val title: DividerTextView? = null,
    val subtitle: DividerTextView? = null
)

data class DividerTextView(
    val text: String,
    @StyleRes val textAppearance: Int = com.design2.chili2.R.style.Chili_H9_Secondary,
    @ColorRes val textColorRes: Int? = null
)