package kg.devcats.chili3.view.card

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Rect
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.core.view.doOnLayout
import androidx.core.view.isVisible
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.setImageOrHide
import com.design2.chili2.extensions.setIsSurfaceClickable
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
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
): BaseCardView(context, attrs, defStyleAttr, defStyleRes) {

    private lateinit var vb: ChiliViewCardAccountBinding
    private var titleValue: CharSequence? = null
    private var subtitleValue: CharSequence? = null
    private var isToggleHiddenState = false
    private var toggleChanged: ((Boolean) -> Unit)? = null
    private var subtitleValueByDelegate = { pan: CharSequence, isHidden: Boolean ->
        if (isHidden) "••••••••"
        else pan
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

    init { initView(context, attrs, defStyleAttr, defStyleRes) }

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

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) post {
            vb.tvTitle.text = null
            updateTitle()
        }
    }

    private fun updateTitle() = with(vb) {
        val containerWidth = llTitle.getViewWidth()
        val startIconWidth = ivTitleIcon.getViewWidth()
        val titleAdditionWidth = tvTitleAddition.getViewWidth()
        val chevronWidth = ivChevron.getViewWidth()
        val marginsWidth =
            ((if (startIconWidth > 0) 8 else 0) + (if (titleAdditionWidth > 0) 8 else 0)).dp
        val availableWidth =
            containerWidth - startIconWidth - titleAdditionWidth - chevronWidth - marginsWidth

        tvTitle.run {
            maxWidth = availableWidth
            maxLines = if (subtitleValue == null) 2 else 1
            ellipsize = TextUtils.TruncateAt.END
            text = titleValue
        }
    }

    private fun View.getViewWidth(): Int {
        if (!isVisible) return 0
        
        if (isOutOfRootContainer()) post {
            vb.tvTitle.text = null
            updateTitle()
        }
        return measuredWidth
    }

    private fun View.isOutOfRootContainer(): Boolean {
        val viewRect = Rect()
        this.getGlobalVisibleRect(viewRect)

        val rootRect = Rect()
        vb.llTitle.getGlobalVisibleRect(rootRect)
        return !rootRect.contains(viewRect)
    }

    fun setTitleIcon(url: String?) {
        vb.tvTitle.text = null
        vb.ivTitleIcon.apply {
            visible()
            setImageByUrl(url)
            callUpdateTitle()
        }
    }

    fun setTitleIcon(@DrawableRes drawableRes: Int?) = with(vb.ivTitleIcon) {
        vb.tvTitle.text = null
        if (drawableRes == null) shimmeringPairs.remove(this)
        else shimmeringPairs[this] = null
        vb.ivTitleIcon.apply {
            setImageOrHide(drawableRes) {
                callUpdateTitle()
            }
        }
    }

    private fun setupTitleIconSize(widthPx: Int?, heightPx: Int?) {
        vb.tvTitle.text = null
        vb.ivTitleIcon.apply {
            widthPx?.let { layoutParams.width = it }
            heightPx?.let { layoutParams.height = it }
            callUpdateTitle()
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
            text = null
            setTextAppearance(resId)
            callUpdateTitle()
        }
    }

    fun setTitleAddition(charSequence: CharSequence?) {
        vb.tvTitle.text = null
        vb.tvTitleAddition.run {
            setTextOrHide(charSequence) {
                callUpdateTitle()
            }
        }
    }

    fun setTitleAddition(@StringRes resId: Int) {
        setTitle(this.context.getText(resId))
    }

    fun setTitleAdditionTextAppearance(@StyleRes resId: Int) {
        vb.tvTitle.text = null
        vb.tvTitleAddition.run {
            setTextAppearance(resId)
            callUpdateTitle()
        }
    }

    fun setChevronVisibility(isChevronVisible: Boolean) {
        vb.tvTitle.text = null
        vb.ivChevron.run {
            isVisible = isChevronVisible
            callUpdateTitle()
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

    fun setSubtitle(charSequence: CharSequence?) {
        vb.llSubtitle.isVisible = !charSequence.isNullOrEmpty()
        vb.tvSubtitle.run {
            text = charSequence?.let { subtitleValueByDelegate(it, isToggleHiddenState) }
            subtitleValue = charSequence
            callUpdateTitle()
        }
    }

    fun setSubtitle(@StringRes resId: Int) {
        setSubtitle(this.context.getText(resId))
    }

    private fun View.callUpdateTitle() {
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