package kg.devcats.chili3.view.toolbar

import android.content.Context
import android.text.Spanned
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.get
import com.design2.chili2.R
import com.design2.chili2.extensions.createShimmerLayout
import com.design2.chili2.extensions.createShimmerView
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.cells.BaseCellView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.extensions.visible
import kg.devcats.chili3.view.cells.BonusTagView
import org.w3c.dom.Text

class ToolbarWithBonusTag @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
) : TailedToolbarView(context, attrs) {

    private var bonusTagView : BonusTagView? = null

    override fun inflateView(context: Context) {
        super.inflateView(context)
        inflateEndIcon()
    }

    private fun inflateEndIcon() {
        this.bonusTagView = BonusTagView(context)
        vb.flEndPlaceHolder.apply {
            visible()
            addView(bonusTagView)
        }
    }

    fun setTagTitle(text: String?) {
        bonusTagView?.setTitle(text)
    }

    fun setTagTitle(@StringRes resId: Int) {
        bonusTagView?.setTitle(resId)
    }

    fun setTagTitle(text: Spanned) {
        bonusTagView?.setTitle(text)
    }

    fun setOnTagClickListener(listener : OnClickListener) {
        bonusTagView?.setOnClickListener(listener)
    }

    fun setOnTagClickListener(onClick: () -> Unit) {
        bonusTagView?.setOnSingleClickListener { onClick.invoke() }
    }
}