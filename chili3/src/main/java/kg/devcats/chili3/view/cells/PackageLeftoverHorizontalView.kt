package kg.devcats.chili3.view.cells

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewPackageLeftoverHorizontalViewBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible

class PackageLeftoverHorizontalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.packageLeftoverHorizontalView,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_CellViewStyle_PackageLeftoverHorizontalCellView
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var binding: ChiliViewPackageLeftoverHorizontalViewBinding

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        binding = ChiliViewPackageLeftoverHorizontalViewBinding.inflate(LayoutInflater.from(context), this, true)
        setupShimmering()
    }

    fun setUnlimitedIcon(@DrawableRes drawableId: Int) {
        binding.ivUnlimited.setImageDrawable(context.drawable(drawableId))
    }

    private fun setupShimmering() {
        shimmeringPairs[binding.llLeftoverPackages] = binding.viewShimmerContent
    }

    fun setWithoutPackage(tariffName: String, tariffDesc: String) = with(binding) {
        tvRemain.text = tariffName
        tvRemainFrom.text = tariffDesc
        progressBar.setProgress(0)
    }

    fun setPackage(
        remain: String,
        limit: String,
        progress: Int,
        @ColorInt colorInt: Int
    ) = with(binding) {
        tvRemain.visible()
        ivUnlimited.gone()
        tvRemain.text = remain
        tvRemainFrom.text = limit
        progressBar.setProgress(progress)
        progressBar.setProgressColor(colorInt)
    }

    fun setUnlimitedInternetPackage(description: String) = with(binding) {
        tvRemain.gone()
        ivUnlimited.visible()
        tvRemainFrom.text = description
        progressBar.setProgress(100)
        progressBar.setProgressGradientColors(
            context.color(R.color.c_5AC8FA),
            context.color(R.color.c_f0047f),
            context.color(R.color.c_FD046A)
        )
    }

    fun setSuspendedPackage(title: String, description: String) = with(binding) {
        tvRemain.visible()
        ivUnlimited.gone()
        tvRemain.text = title
        tvRemainFrom.text = description
        progressBar.setProgress(0)
    }

    fun setPackageEnded(title: String, description: String) = with(binding) {
        tvRemain.visible()
        ivUnlimited.gone()
        tvRemain.text = title
        tvRemainFrom.text = description
        progressBar.setProgress(0)
    }

    fun setInternetTethering(isEnabled: Boolean, isTethering: Boolean) = with(binding) {
        ivTethering.isVisible = isEnabled
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?>  = shimmeringPairs

}