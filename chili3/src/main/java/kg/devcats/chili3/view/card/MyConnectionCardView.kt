package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DrawableRes
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.card.BaseCardView
import com.design2.chili2.view.shimmer.startShimmering
import com.design2.chili2.view.shimmer.stopShimmering
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewMyConnectionCardViewBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible
import kg.devcats.chili3.util.PackageType

class MyConnectionCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.myConnectionCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_MyConnectionCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewMyConnectionCardViewBinding
    private var isUnauthorized: Boolean = false
    override val styleableAttrRes: IntArray = R.styleable.MyConnectionCardView

    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewMyConnectionCardViewBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun TypedArray.obtainAttributes() {
        setTitle(getText(R.styleable.MyConnectionCardView_title))
        setUnauthorizedTitle(getString(R.styleable.MyConnectionCardView_unauthorizedTitle))
        setSubtitle(getString(R.styleable.MyConnectionCardView_unauthorizedSubtitle))
        getResourceId(R.styleable.MyConnectionCardView_unauthorizedTitleDrawable, -1)
            .takeIf { it != -1 }?.let { setTitleEndDrawable(it) }
        getResourceId(R.styleable.MyConnectionCardView_unauthorizedImage, -1)
            .takeIf { it != -1 }?.let { setUnauthorizedImage(it) }
        setBalanceTitle(getString(R.styleable.MyConnectionCardView_balanceTitle))
        getResourceId(R.styleable.MyConnectionCardView_unlimitedDrawable, -1)
            .takeIf { it != -1 }?.let { setUnlimitedIcon(it) }
        setIsUnauthorized(
            getBoolean(R.styleable.MyConnectionCardView_isUnauthorized, false)
        )
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        with(vb) {
            shimmeringPairs[tvTitle] = viewTitleShimmer
            shimmeringPairs[llBalanceContainer] = viewBalanceShimmer
        }
    }

    private fun setTitleEndDrawable(@DrawableRes drawableId: Int) {
        vb.viewUnauthorized.tvUaTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawableId, 0)
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    private fun setUnauthorizedTitle(charSequence: CharSequence?) {
        vb.viewUnauthorized.tvUaTitle.text = charSequence
    }

    private fun setSubtitle(charSequence: CharSequence?) {
        vb.viewUnauthorized.tvDescription.text = charSequence
    }

    private fun setBalanceTitle(charSequence: CharSequence?) {
        vb.tvBalanceTitle.text = charSequence
    }

    private fun setUnlimitedIcon(@DrawableRes drawableId: Int) {
        vb.plvInternet.setUnlimitedIcon(drawableId)
    }

    private fun setUnauthorizedImage(@DrawableRes drawableId: Int) {
        vb.viewUnauthorized.ivUnauthorized.setImageDrawable(context.drawable(drawableId))
    }

    fun setIsUnauthorized(isUnauthorized: Boolean) = with(vb) {
        this@MyConnectionCardView.isUnauthorized = isUnauthorized
        viewUnauthorized.root.isVisible = isUnauthorized
        flTitleContainer.isInvisible = isUnauthorized
        llLeftoverInfo.isVisible = !isUnauthorized
    }

    fun setUnauthorizedInfo(title: String, subtitle: String, @DrawableRes titleDrawable: Int, @DrawableRes image: Int) {
        setIsUnauthorized(true)
        setUnauthorizedTitle(title)
        setSubtitle(subtitle)
        setTitleEndDrawable(titleDrawable)
        setUnauthorizedImage(image)
    }

    fun setWithoutPackage(tariffName: String, tariffDesc: String) = with(vb) {
        plvCall.gone()
        plvInternet.gone()
        plvWithoutPackage.visible()
        plvWithoutPackage.setWithoutPackage(tariffName, tariffDesc)
    }

    fun setPackage(remain: String, limit: String, progress: Int, type: PackageType) =
        with(vb) {
            plvWithoutPackage.gone()
            when (type) {
                PackageType.CALL -> {
                    plvCall.visible()
                    plvCall.setPackage(
                        remain,
                        limit,
                        progress,
                        context.getColor(R.color.c_80C01B)
                    )
                }

                PackageType.INTERNET -> {
                    plvInternet.visible()
                    plvInternet.setPackage(
                        remain,
                        limit,
                        progress,
                        context.color(com.design2.chili2.R.color.cyan_1)
                    )
                }
            }
        }

    fun setUnlimitedInternetPackage(description: String) = with(vb) {
        plvInternet.visible()
        plvWithoutPackage.gone()
        plvInternet.setUnlimitedInternetPackage(description)
    }

    fun setSuspendedPackage(title: String, description: String, type: PackageType) =
        with(vb) {
            plvWithoutPackage.gone()
            when (type) {
                PackageType.CALL -> {
                    plvCall.setSuspendedPackage(title, description)
                    plvCall.visible()
                }
                PackageType.INTERNET -> {
                    plvInternet.visible()
                    plvInternet.setSuspendedPackage(
                        title,
                        description
                    )
                }
            }
        }

    fun setEndedPackage(title: String, description: String, type: PackageType) =
        with(vb) {
            plvWithoutPackage.gone()
            when (type) {
                PackageType.CALL -> {
                    plvCall.visible()
                    plvCall.setPackageEnded(title, description)
                }
                PackageType.INTERNET -> {
                    plvInternet.visible()
                    plvInternet.setPackageEnded(title, description)
                }
            }
        }

    fun setBalance(charSequence: CharSequence?) = with(vb) {
        tvBalance.text = charSequence
    }

    fun setOnMyConnectionClickListener(action: () -> Unit) {
        vb.rootView.setOnSingleClickListener(action)
    }

    fun setOnUnauthorizedClickListener(action: () -> Unit) {
        vb.viewUnauthorized.root.setOnSingleClickListener(action)
    }

    override fun onStartShimmer() = with(vb) {
        super.onStartShimmer()
        viewUnauthorized.root.gone()
        llLeftoverInfo.visible()
        plvCall.startShimmering()
        plvInternet.startShimmering()
    }

    override fun onStopShimmer() = with(vb) {
        super.onStopShimmer()
        viewUnauthorized.root.isVisible = isUnauthorized
        llLeftoverInfo.isVisible = !isUnauthorized
        plvCall.stopShimmering()
        plvInternet.stopShimmering()
    }
}