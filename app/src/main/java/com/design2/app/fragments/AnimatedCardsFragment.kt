package com.design2.app.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.StateListAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.R
import com.design2.app.base.BaseFragment
import com.design2.app.databinding.FragmentAnimatedCardsBinding
import com.design2.app.databinding.ItemViewCellBinding
import com.design2.chili2.view.shimmer.startGroupShimmering
import com.design2.chili2.view.shimmer.stopGroupShimmering
import kg.devcats.chili3.util.StoriesStatus
import kotlin.math.roundToInt
import kotlin.random.Random

class AnimatedCardsFragment : BaseFragment<FragmentAnimatedCardsBinding>() {

    private var storiesConfig = ViewConfigs(ViewType.STORIES_VIEW)
    private var storiesSmallConfig = ViewConfigs(ViewType.SMALL_STORIES_VIEW)
    private var marketAdsConfig = ViewConfigs(ViewType.PRODUCT_VIEW)
    private var bannerConfig = ViewConfigs(ViewType.MEDIA_VIEW)
    private var selectedView: ViewConfigs? = null
    private val durationDownLabel = "Press down duration:"
    private val durationUpLabel = "Press up duration:"
    private val scaleLabel = "Items scale:"

    override fun inflateViewBinging(): FragmentAnimatedCardsBinding {
        return FragmentAnimatedCardsBinding.inflate(layoutInflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()

        updateValues()

        vb.rv.adapter = InterpolatorListAdapter {
            selectedView?.viewInterpolator = it
            createCustomStateListAnimator()
            updateValues()
        }
    }

    private fun initListeners() = with(vb) {
        rg.setOnCheckedChangeListener { _, checkedId ->
            selectedView = when (checkedId) {
                R.id.rb_stories -> storiesConfig
                R.id.rb_stories_small -> storiesSmallConfig
                R.id.rb_banners -> bannerConfig
                R.id.rb_market_ads -> marketAdsConfig
                else -> null
            }
            updateValues()
            createCustomStateListAnimator()
        }

        sbDurationDown.setOnSeekBarChangeListener { _, progress, _ ->
            tvDurationDown.text = "$durationDownLabel $progress"
            selectedView?.viewDurationDown = progress
            createCustomStateListAnimator()
        }

        sbDurationUp.setOnSeekBarChangeListener { _, progress, _ ->
            tvDurationUp.text = "$durationUpLabel $progress"
            selectedView?.viewDurationUp = progress
            createCustomStateListAnimator()
        }

        sbScale.setOnSeekBarChangeListener { _, progress, _ ->
            val scale = (0.5 + progress / 100f).roundToTwoDecimalPlaces()
            tvScale.text = "$scaleLabel $scale"
            selectedView?.viewScale = progress
            createCustomStateListAnimator()
        }

        scvStories.run {
            setOnClickListener {
                setStatus(StoriesStatus.values()[Random.nextInt(0, 2)].value)
            }
        }

        scvStoriesSmall.run {
            setOnClickListener {
                setStatus(StoriesStatus.values()[Random.nextInt(0, 2)].value)
            }
        }
    }

    private fun updateValues() = with(vb) {
        tvDurationDown.text = "$durationDownLabel ${selectedView?.viewDurationDown ?: 0}"
        sbDurationDown.progress = selectedView?.viewDurationDown ?: 0

        tvDurationUp.text = "$durationUpLabel ${selectedView?.viewDurationUp ?: 0}"
        sbDurationUp.progress = selectedView?.viewDurationUp ?: 0

        val scale = ((selectedView?.viewScale?.div(100f))?.plus(0.5) ?: 0.0).roundToTwoDecimalPlaces()
        tvScale.text = "$scaleLabel $scale"
        sbScale.progress = selectedView?.viewScale ?: 0

        vb.ec.setTitle("Interpolator: ${selectedView?.viewInterpolator?.first ?: ""}")
    }

    private fun SeekBar.setOnSeekBarChangeListener(
        onProgressChanged: (seekBar: SeekBar?, progress: Int, fromUser: Boolean) -> Unit
    ) {
        this.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                onProgressChanged(seekBar, progress, fromUser)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit
            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        })
    }

    private fun Double.roundToTwoDecimalPlaces(): Float {
        return (this * 100).roundToInt() / 100f
    }

    private fun createCustomStateListAnimator() {
        val (_, scale, mDurationDown, mDurationUp, mInterpolator) = selectedView ?: ViewConfigs()
        val polar = AnimationUtils.loadInterpolator(context, mInterpolator.second)
        val s: Float = (scale.div(100f)).plus(0.5f)
        val pressedAnimatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1.0f, s).apply {
                    duration = mDurationDown.toLong()
                    interpolator = polar
                },
                ObjectAnimator.ofFloat(view, "scaleY", 1.0f, s).apply {
                    duration = mDurationDown.toLong()
                    interpolator = polar
                }
            )
        }

        val defaultAnimatorSet = AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", s, 1.0f).apply {
                    duration = mDurationUp.toLong()
                    interpolator = polar
                },
                ObjectAnimator.ofFloat(view, "scaleY", s, 1.0f).apply {
                    duration = mDurationUp.toLong()
                    interpolator = polar
                }
            )
        }

        val stateListAnimator = StateListAnimator().apply {
            addState(intArrayOf(android.R.attr.state_pressed), pressedAnimatorSet)
            addState(intArrayOf(), defaultAnimatorSet)
        }
        setAnimator(stateListAnimator)
    }

    private fun setAnimator(stateListAnimator: StateListAnimator) = with(vb) {
        val view = when (selectedView?.viewType) {
            ViewType.STORIES_VIEW -> scvStories
            ViewType.SMALL_STORIES_VIEW -> scvStoriesSmall
            ViewType.PRODUCT_VIEW -> pcvProduct
            ViewType.MEDIA_VIEW -> mv
            else -> null
        }
        view?.stateListAnimator = stateListAnimator
        view?.foreground = null
    }

    private data class ViewConfigs(
        var viewType: ViewType? = null,
        var viewScale: Int = 43, // 43 it's 93 (43 + 50)
        var viewDurationDown: Int = 80,
        var viewDurationUp: Int = 80,
        var viewInterpolator: Pair<String, Int> = "linear" to android.R.interpolator.linear,
    )

    private enum class ViewType {
        STORIES_VIEW, SMALL_STORIES_VIEW, PRODUCT_VIEW, MEDIA_VIEW
    }

    override fun startShimmering() {
        super.startShimmering()
        vb.root.startGroupShimmering()
    }

    override fun stopShimmering() {
        super.stopShimmering()
        vb.root.stopGroupShimmering()
    }
}

class InterpolatorListAdapter(private val onClick: (Pair<String, Int>) -> Unit) :
    RecyclerView.Adapter<InterpolatorViewVH>() {

    private val interpolators: List<Pair<String, Int>> by lazy {
        listOf(
            "bounce" to android.R.interpolator.bounce,
            "linear" to android.R.interpolator.linear,
            "cycle" to android.R.interpolator.cycle,
            "overshoot" to android.R.interpolator.overshoot,
            "anticipate" to android.R.interpolator.anticipate,
            "accelerate_cubic" to android.R.interpolator.accelerate_cubic,
            "accelerate_decelerate" to android.R.interpolator.accelerate_decelerate,
            "accelerate_quad" to android.R.interpolator.accelerate_quad,
            "accelerate_quint" to android.R.interpolator.accelerate_quint,
            "anticipate_overshoot" to android.R.interpolator.anticipate_overshoot,
            "decelerate_cubic" to android.R.interpolator.decelerate_cubic,
            "decelerate_quad" to android.R.interpolator.decelerate_quad,
            "decelerate_quint" to android.R.interpolator.decelerate_quint,
            "fast_out_linear_in" to android.R.interpolator.fast_out_linear_in,
            "fast_out_slow_in" to android.R.interpolator.fast_out_slow_in,
            "linear_out_slow_in" to android.R.interpolator.linear_out_slow_in
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InterpolatorViewVH {
        val view = ItemViewCellBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InterpolatorViewVH.create(view, onClick)
    }

    override fun getItemCount(): Int {
        return interpolators.size
    }

    override fun onBindViewHolder(holder: InterpolatorViewVH, position: Int) {
        holder.onBind(interpolators[position])
    }

}

class InterpolatorViewVH(val vb: ItemViewCellBinding) : RecyclerView.ViewHolder(vb.root) {

    private var data: Pair<String, Int>? = null

    fun onBind(data: Pair<String, Int>) {
        vb.item.apply { setTitle(data.first) }
        this.data = data
    }

    companion object {
        fun create(
            vb: ItemViewCellBinding,
            onSelect: (Pair<String, Int>) -> Unit
        ): InterpolatorViewVH {
            return InterpolatorViewVH(vb).apply {
                vb.item.setOnClickListener {
                    data?.let { it1 -> onSelect(it1) }
                }
            }
        }
    }
}