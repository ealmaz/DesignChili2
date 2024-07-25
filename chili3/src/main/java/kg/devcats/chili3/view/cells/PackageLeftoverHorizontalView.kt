package kg.devcats.chili3.view.cells

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.view.isVisible
import com.design2.chili2.extensions.color
import com.design2.chili2.extensions.drawable
import com.design2.chili2.extensions.loadImage
import com.design2.chili2.view.shimmer.ShimmeringView
import com.facebook.shimmer.ShimmerFrameLayout
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewPackageLeftoverHorizontalViewBinding
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.invisible
import kg.devcats.chili3.extensions.visible

class PackageLeftoverHorizontalView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.packageLeftoverHorizontalView,
    defStyleRes: Int = com.design2.chili2.R.style.Chili_CellViewStyle_PackageLeftoverHorizontalCellView
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), ShimmeringView {

    private lateinit var binding: ChiliViewPackageLeftoverHorizontalViewBinding

    private val shimmeringPairs = mutableMapOf<View, ShimmerFrameLayout?>()

    private var pieChartIconIndex = 0

    private val handler = Handler(Looper.getMainLooper())

    private var isAnimating = false

    init {
        initView(context)
    }

    private fun initView(context: Context) {
        binding = ChiliViewPackageLeftoverHorizontalViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )
        setupShimmering()
    }

    fun setUnlimitedIcon(@DrawableRes drawableId: Int) {
        binding.ivUnlimited.setImageDrawable(context.drawable(drawableId))
    }

    private fun setupShimmering() {
        shimmeringPairs[binding.clContainer] = binding.viewShimmerContent
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


    fun startChangePieChartIcon(
        icons: List<String>
    ) {
        with(binding) {
            if (icons.isEmpty()) {
                disableExistAnimation()
                return
            }
            if (isAnimating) return
            isAnimating = true
            ivTethering.visible()
            ivTethering.loadImage(icons[pieChartIconIndex])
            pieChartIconIndex = (pieChartIconIndex + 1) % icons.size
            if (icons.size > 1) {
                val fadeOut = AlphaAnimation(1f, 0.1f).apply { duration = FADE_OUT_DURATION }
                setAnimationListener(fadeOut) { endChangePieChartIcon(icons) }
                ivTethering.startAnimation(fadeOut)
            }
        }
    }

    private fun endChangePieChartIcon(
        icons: List<String>
    ) {
        with(binding) {
            if (icons.isNotEmpty()) ivTethering.loadImage(icons[pieChartIconIndex])
            val fadeIn = AlphaAnimation(0.1f, 1f).apply { duration = FADE_IN_DURATION }
            setAnimationListener(fadeIn) {
                isAnimating = false
                handler.postDelayed(
                    { startChangePieChartIcon(icons) },
                    DELAY
                )
            }
            ivTethering.startAnimation(fadeIn)
        }
    }

    private fun setAnimationListener(animation: Animation, onAnimationEnd: () -> Unit) {
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(anim: Animation) {}
            override fun onAnimationEnd(anim: Animation) {
                onAnimationEnd()
            }

            override fun onAnimationRepeat(anim: Animation) {}
        })
    }

    private fun disableExistAnimation() {
        binding.ivTethering.apply {
            invisible()
            animation = null
        }
        handler.removeCallbacksAndMessages(null)
        isAnimating = false
        pieChartIconIndex = 0
    }

    override fun getShimmeringViewsPair(): Map<View, ShimmerFrameLayout?> = shimmeringPairs

    companion object {
        private const val DELAY = 3000L
        private const val FADE_IN_DURATION = 1000L
        private const val FADE_OUT_DURATION = 800L
    }

}