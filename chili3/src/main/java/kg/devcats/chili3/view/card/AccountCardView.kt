package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setupAsSecure
import com.design2.chili2.view.card.BaseCardView
import com.facebook.shimmer.Shimmer
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.ChiliViewCardAccountBinding
import kg.devcats.chili3.extensions.getColorFromAttr
import kg.devcats.chili3.extensions.gone
import kg.devcats.chili3.extensions.visible
import com.design2.chili2.R as R2

class AccountCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.accountCardViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_CardViewStyle_AccountCardView
) : BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardAccountBinding
    private var titleValue: CharSequence? = null
    private var subtitleValue: CharSequence? = null
    private var isToggleHiddenState = false
    private var toggleChanged: ((Boolean) -> Unit)? = null
    private var isUseSecureSubtitle = false
    private var subtitleValueByDelegate = { pan: CharSequence, isHidden: Boolean ->
        if (isHidden && !isUseSecureSubtitle) "••••••••" else pan
    }
    private val titleIconShimmer by lazy {
        Shimmer.ColorHighlightBuilder()
            .setHighlightAlpha(1f)
            .setBaseAlpha(1f)
            .setWidthRatio(2.0F)
            .setBaseColor(context.getColorFromAttr(R2.attr.ChiliShimmerBoneColor))
            .setHighlightColor(context.getColorFromAttr(R.attr.ChiliShimmerHighlightColor))
            .build()
    }

    override val styleableAttrRes: IntArray = R.styleable.AccountCardView
    override val rootContainer: View
        get() = vb.rootView

    override fun inflateView(context: Context) {
        vb = ChiliViewCardAccountBinding.inflate(LayoutInflater.from(context), this, true)
    }

    override fun setupShimmeringViews() {
        shimmeringPairs[vb.tvTitle] = vb.viewTitleShimmer
        shimmeringPairs[vb.llContainer] = vb.viewSubtitleShimmer
    }

    init {
        initView(context, attrs, defStyleAttr, defStyleRes)
    }

    override fun TypedArray.obtainAttributes() {
        setTitleIcon(
            getResourceId(R.styleable.AccountCardView_titleIcon, -1).takeIf { it != -1 }
        )
        setTitleAddition(getText(R.styleable.AccountCardView_titleAddition))
        getResourceId(R.styleable.AccountCardView_titleAdditionTextAppearance, -1)
            .takeIf { it != -1 }?.let(::setTitleAdditionTextAppearance)

        setTitle(getText(R.styleable.AccountCardView_title))
        getResourceId(R.styleable.AccountCardView_titleTextAppearance, -1)
            .takeIf { it != -1 }?.let(::setTitleTextAppearance)

        setChevronVisibility(getBoolean(R.styleable.AccountCardView_isChevronVisible, false))

        setToggleIconVisibility(getBoolean(R.styleable.AccountCardView_isToggleIconVisible, false))
        setToggleIconState(getBoolean(R.styleable.AccountCardView_isToggleIconEnabled, false))

        setSubtitle(getText(R.styleable.AccountCardView_subtitle))
        getResourceId(R.styleable.AccountCardView_subtitleTextAppearance, -1)
            .takeIf { it != -1 }?.let(::setSubtitleTextAppearance)

        getResourceId(R.styleable.AccountCardView_actionButtonIcon, -1)
            .takeIf { it != -1 }?.let(::setActionButtonIcon)
        setActionButtonText(getString(R.styleable.AccountCardView_actionButtonText))
        getResourceId(R.styleable.AccountCardView_actionButtonTextAppearance, -1)
            .takeIf { it != -1 }?.let(::setActionButtonTextAppearance)

        setDividerVisibility(getBoolean(R.styleable.AccountCardView_isDividerVisible, true))

    }

    override fun setupView() {
        super.setupView()
        setupToggleButton()
    }

    private fun updateTitle(): Unit = with(vb) {
        tvTitle.post {
            val titleContainerWidth = llTitle.getViewWidth()
            val startIconWidth = ivTitleIcon.getViewWidth()
            val titleAdditionWidth = tvTitleAddition.getViewWidth()
            val chevronWidth = ivChevron.getViewWidth()
            val marginsWidth =
                ((if (startIconWidth > 0) 8 else 0) + (if (titleAdditionWidth > 0) 8 else 0)).dp
            val availableWidth =
                titleContainerWidth - startIconWidth - titleAdditionWidth - chevronWidth - marginsWidth

            tvTitle.maxWidth = availableWidth.takeIf { it >= 0 } ?: 0
        }
    }

    private fun View.getViewWidth(): Int {
        return when {
            !isVisible -> 0
            this is TextView -> paint.measureText(text.toString()).toInt()
            else -> measuredWidth
        }
    }

    fun setTitleIcon(url: String?) {
        vb.ivTitleIcon.apply {
            visible()
            setImageByUrl(url)
            preUpdateTitle()
        }
    }

    fun setTitleIcon(@DrawableRes drawableRes: Int?) = with(vb.ivTitleIcon) {
        if (drawableRes == null) shimmeringPairs.remove(this)
        else shimmeringPairs[this] = null
        vb.ivTitleIcon.apply {
            drawableRes?.let { setImageResource(it); visible() } ?: gone()
            preUpdateTitle()
        }
    }

    private fun setupTitleIconSize(widthPx: Int?, heightPx: Int?) {
        vb.ivTitleIcon.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
            preUpdateTitle()
        }
    }

    fun setTitleIconSize(@DimenRes widthDimenRes: Int, @DimenRes heightDimenRes: Int) {
        val widthPx = resources.getDimensionPixelSize(widthDimenRes)
        val heightPx = resources.getDimensionPixelSize(heightDimenRes)
        setupTitleIconSize(widthPx, heightPx)
    }

    fun setTitle(charSequence: CharSequence?) {
        titleValue = charSequence?.toString()
        vb.tvTitle.text = charSequence
    }

    fun setTitle(@StringRes resId: Int) {
        setTitle(this.context.getText(resId))
    }

    fun setTitleTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.run {
            setTextAppearance(resId)
            preUpdateTitle()
        }
    }

    fun setTitleAddition(charSequence: CharSequence?) {
        vb.tvTitleAddition.run {
            text = charSequence
            isVisible = charSequence != null
            preUpdateTitle()
        }
    }

    fun setTitleAddition(@StringRes resId: Int) {
        setTitle(this.context.getText(resId))
    }

    fun setTitleAdditionTextAppearance(@StyleRes resId: Int) {
        vb.tvTitleAddition.run {
            setTextAppearance(resId)
            preUpdateTitle()
        }
    }

    fun setChevronVisibility(isChevronVisible: Boolean) {
        vb.ivChevron.run {
            isVisible = isChevronVisible
            preUpdateTitle()
        }
    }

    fun onMainContentClick(onClick: () -> Unit) {
        vb.llContainer.setOnSingleClickListener(onClick)
    }

    fun setToggleIconVisibility(isIconVisible: Boolean) {
        vb.ivToggleIcon.isVisible = isIconVisible
        if (!isIconVisible) isToggleHiddenState = false
    }

    fun setToggleIconState(isHiddenState: Boolean) {
        isToggleHiddenState = isHiddenState
        handleToggleIconState()
    }

    fun onToggleChange(callback: (Boolean) -> Unit) {
        toggleChanged = callback
    }

    private fun setupToggleButton() {
        vb.ivToggleIcon.setOnClickListener {
            isToggleHiddenState = !isToggleHiddenState
            handleToggleIconState()
            toggleChanged?.let { it(isToggleHiddenState) }
        }
    }

    private fun handleToggleIconState() = with(vb) {
        ivToggleIcon.setImageResource(
            if (!isToggleHiddenState) R.drawable.chili_ic_eye
            else R.drawable.chili_ic_eye_slash
        )
        tvSubtitle.text = subtitleValue?.let { subtitleValueByDelegate(it, isToggleHiddenState) }
    }

    fun setSubtitle(charSequence: CharSequence?) = with(vb) {
        llSubtitle.isVisible = !charSequence.isNullOrEmpty()
        tvTitle.maxLines = if (charSequence != null) 1 else 2
        tvSubtitle.run {
            text = charSequence?.let { subtitleValueByDelegate(it, isToggleHiddenState) }
            subtitleValue = charSequence
            preUpdateTitle()
        }
    }

    fun setSubtitle(@StringRes resId: Int) {
        setSubtitle(this.context.getText(resId))
    }

    private fun View.preUpdateTitle() {
        if (!isVisible) {
            updateTitle()
            return
        }
        this.doOnLayout {
            post { updateTitle() }
        }
    }

    fun setSubtitleTextAppearance(@StyleRes resId: Int) {
        vb.tvSubtitle.setTextAppearance(resId)
    }

    fun setActionButtonIcon(@DrawableRes drawableRes: Int?) {
        vb.ibActionBtn.setStartIcon(drawableRes)
    }

    fun setActionButtonEndIcon(@DrawableRes drawableRes: Int?) {
        vb.ibActionBtn.setIcon(drawableRes)
    }

    fun setActionButtonText(text: String?) {
        vb.ibActionBtn.setText(text)
    }

    fun setActionButtonText(@StringRes resId: Int) {
        vb.ibActionBtn.setText(resId)
    }

    fun setActionButtonTextAppearance(@StyleRes resId: Int) {
        vb.ibActionBtn.setTextAppearance(resId)
    }

    fun setDividerVisibility(dividerVisible: Boolean) {
        vb.divider.isVisible = dividerVisible
    }

    fun onActionButtonClick(onClick: () -> Unit) {
        vb.ibActionBtn.setOnSingleClickListener(onClick)
    }

    fun setupSubtitleAsSecure() {
        isUseSecureSubtitle = true
        vb.tvSubtitle.setupAsSecure()
    }

    override fun onStartShimmer(): Unit = with(vb) {
        shimmerTitleIcon.run {
            setShimmer(titleIconShimmer)
            visible()
            startShimmer()
        }
    }

    override fun onStopShimmer(): Unit = with(vb) {
        this@AccountCardView.setIsSurfaceClickable(false)
        shimmerTitleIcon.run {
            stopShimmer()
            gone()
        }
    }
}