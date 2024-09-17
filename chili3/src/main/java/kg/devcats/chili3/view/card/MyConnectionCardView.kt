package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.text.Spanned
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
import kg.devcats.chili3.model.MyConnectionProfile
import kg.devcats.chili3.model.PackageLeftOver
import kg.devcats.chili3.model.PackageType

class MyConnectionCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.myConnectionCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_MyConnectionCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewMyConnectionCardViewBinding
    private var isUnauthorized: Boolean = false
    private var isWithPackage: Boolean = true
    private var unlimitedText: String? = null
    private var withoutPackageTitle: String? = null
    private var packageSuspendedText: String? = null
    private var packageEmptyText: String? = null

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
        setUnauthorizedSubtitle(getString(R.styleable.MyConnectionCardView_unauthorizedSubtitle))
        getResourceId(R.styleable.MyConnectionCardView_unauthorizedImage, -1)
            .takeIf { it != -1 }?.let { setUnauthorizedImage(it) }
        setBalanceTitle(getString(R.styleable.MyConnectionCardView_balanceTitle))
        getResourceId(R.styleable.MyConnectionCardView_unlimitedDrawable, -1)
            .takeIf { it != -1 }?.let { setUnlimitedIcon(it) }
        setIsUnauthorized(
            getBoolean(R.styleable.MyConnectionCardView_isUnauthorized, false)
        )
        setWithoutPackageSubtitle(getString(R.styleable.MyConnectionCardView_withoutPackageSubtitle))
        unlimitedText = getString(R.styleable.MyConnectionCardView_unlimitedText)
        withoutPackageTitle = getString(R.styleable.MyConnectionCardView_withoutPackageTitle)
        packageSuspendedText = getString(R.styleable.MyConnectionCardView_packageSuspendedText)
        packageEmptyText = getString(R.styleable.MyConnectionCardView_packageEmptyText)
    }

    override fun setupShimmeringViews() {
        super.setupShimmeringViews()
        with(vb) {
            shimmeringPairs[tvTitle] = viewTitleShimmer
            shimmeringPairs[llBalanceContainer] = viewBalanceShimmer
        }
    }

    fun setTitle(charSequence: CharSequence?) {
        vb.tvTitle.text = charSequence
    }

    private fun setUnauthorizedTitle(charSequence: CharSequence?) {
        vb.viewUnauthorized.tvUaTitle.text = charSequence
    }

    fun setUnauthorizedSubtitle(charSequence: CharSequence?) {
        vb.viewUnauthorized.tvDescription.text = charSequence
    }

    fun setUnauthorizedTitle(spanned: Spanned) {
        vb.viewUnauthorized.tvUaTitle.text = spanned
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

    private fun setWithoutPackageSubtitle(charSequence: CharSequence?) {
        vb.tvWithoutPackageSubtitle.text = charSequence
    }

    fun setIsUnauthorized(isUnauthorized: Boolean) = with(vb) {
        this@MyConnectionCardView.isUnauthorized = isUnauthorized
        viewUnauthorized.root.isVisible = isUnauthorized
        flTitleContainer.isInvisible = isUnauthorized
        llLeftoverInfo.isVisible = !isUnauthorized
        llWithoutPackage.isVisible = !isUnauthorized
    }

    fun setUnauthorizedInfo(title: Spanned, subtitle: CharSequence, @DrawableRes image: Int) {
        setIsUnauthorized(true)
        setUnauthorizedTitle(title)
        setUnauthorizedSubtitle(subtitle)
        setUnauthorizedImage(image)
    }

    private fun setWithoutPackage(tariffName: String) = with(vb) {
        isWithPackage = false
        plvCall.gone()
        plvInternet.gone()
        llWithoutPackage.visible()
        tvWithoutPackageTitle.text = tariffName
    }

    fun setProfile(profile: MyConnectionProfile) {
        setBalance(profile.balance)
        if (profile.isWithPackages) setLeftOverPackages(profile.packages.orEmpty())
        else setWithoutPackage(profile.getFormattedTariffName(withoutPackageTitle))
    }

    private fun setLeftOverPackages(packages: List<PackageLeftOver>) = with(vb) {
        isWithPackage = true
        llWithoutPackage.gone()
        val internet = packages.firstOrNull { it.packageType == PackageType.INTERNET }
        val call = packages.firstOrNull { it.packageType == PackageType.CALL }
        setUpInternetPackage(internet)
        setUpCallPackage(call)
    }

    private fun setUpInternetPackage(leftOver: PackageLeftOver?) = with(vb) {
        if (leftOver == null) return@with


        if (leftOver.isUnlimitedAndNotSuspended()){
            plvInternet.setUnlimitedInternetPackage()
        }else {
            plvInternet.setPackage(
                leftOver.getFormattedRemain(resources),
                when {
                    leftOver.isUnlimitedAndNotSuspended() -> unlimitedText
                    leftOver.isSuspended -> packageSuspendedText
                    leftOver.isPackageEmpty() -> packageEmptyText
                    else -> leftOver.getFormattedLimit(resources)
                },
                leftOver.getLeftOverPercentage(),
                context.color(com.design2.chili2.R.color.cyan_1)
            )
        }
    }

    private fun setUpCallPackage(leftOver: PackageLeftOver?) = with(vb) {
        if (leftOver == null) return@with
        plvCall.setPackage(
            leftOver.getFormattedRemain(resources),
            when {
                leftOver.isUnlimitedAndNotSuspended() -> unlimitedText
                leftOver.isSuspended -> packageSuspendedText
                leftOver.isPackageEmpty() -> packageEmptyText
                else -> leftOver.getFormattedLimit(resources)
            },
            leftOver.getLeftOverPercentage(),
            context.getColor(R.color.c_80C01B)
        )
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
        llWithoutPackage.gone()
        plvCall.visible()
        plvInternet.visible()
        tvBalanceTitle.maxLines = 1
        plvInternet.startShimmering()
        plvCall.startShimmering()
    }

    override fun onStopShimmer() = with(vb) {
        super.onStopShimmer()
        viewUnauthorized.root.isVisible = isUnauthorized
        llLeftoverInfo.isVisible = !isUnauthorized
        plvCall.isVisible = isWithPackage
        plvInternet.isVisible = isWithPackage
        llWithoutPackage.isVisible = !isWithPackage
        tvBalanceTitle.maxLines = 2
        plvCall.stopShimmering(false)
        plvInternet.stopShimmering(false)
    }
}