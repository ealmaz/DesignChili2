package com.design2.chili2.view.tooltip

import android.content.Context
import android.graphics.Color
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.LinearLayout
import android.widget.PopupWindow
import androidx.annotation.DimenRes
import androidx.annotation.StringRes
import com.design2.chili2.databinding.ChiliViewTooltipBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.visible

class TooltipView(builder: Builder) : PopupWindow.OnDismissListener {

    private lateinit var vb: ChiliViewTooltipBinding

    private var mDefaultPopupWindowStyleRes = android.R.attr.popupWindowStyle
    private var mOnShowListener: OnShowListener? = null
    private var mOnDismissListener: OnDismissListener? = null
    private var mOnCloseBtnListener: OnCloseBtnListener? = null
    private var mOnClickListener: OnClickListener? = null
    private var mOnSkipListener: OnSkipListener? = null

    private var mPopupWindow: PopupWindow? = null
    private var mContext: Context
    private var mTitle: CharSequence
    private var mSubtitle: CharSequence
    private var mRootView: ViewGroup?
    private var mAnchorView: View?
    private var mContentLayout: View? = null
    private var mGravity: Int
    private var mArrowAlign: Int
    private var mFocusable: Boolean
    private var mMargin = 0f

    private var mEventAction: String = ON_SKIP_ACTION

    init {
        mContext = builder.context
        mTitle = builder.title
        mSubtitle = builder.subtitle
        mAnchorView = builder.anchorView
        mRootView = TooltipUtils.findFrameLayout(mAnchorView)
        mGravity = builder.gravity
        mArrowAlign = builder.arrowAlign
        mMargin = builder.margin
        mFocusable = builder.focusable
        mOnShowListener = builder.onShowListener
        mOnDismissListener = builder.onDismissListener
        mOnCloseBtnListener = builder.onCloseBtnListener
        mOnClickListener = builder.onClickListener
        mOnSkipListener = builder.onSkipListener
        init()
    }

    private fun init() {
        configPopupWindow()
        inflateViews()
        configContentView()
    }

    private fun configPopupWindow() {
        mPopupWindow = PopupWindow(mContext, null, mDefaultPopupWindowStyleRes).apply {
            width = LinearLayout.LayoutParams.MATCH_PARENT
            setOnDismissListener(this@TooltipView)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            isOutsideTouchable = true
            isClippingEnabled = false
            isClippingEnabled = false
            isFocusable = mFocusable
        }
    }

    private fun inflateViews() {
        vb = ChiliViewTooltipBinding.inflate(LayoutInflater.from(mContext))
        mContentLayout = vb.root
    }

    private fun configContentView() {
        with(vb) {
            clRoot.setOnClickListener { setAction(ON_VIEW_CLICK_ACTION) }
            tvTitle.text = mTitle
            tvSubtitle.text = mSubtitle
            imgClose.setOnClickListener { setAction(ON_CLOSE_BTN_ACTION) }
        }
        initArrow()
        mPopupWindow?.contentView = mContentLayout
    }

    private fun initArrow() {
        when {
            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_START -> {
                hideAllArrows()
                vb.bottomArrowStart.visible()
            }

            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_CENTER -> {
                hideAllArrows()
                vb.bottomArrowCenter.visible()
            }

            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_END -> {
                hideAllArrows()
                vb.bottomArrowEnd.visible()
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_START -> {
                hideAllArrows()
                vb.topArrowStart.visible()
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_CENTER -> {
                hideAllArrows()
                vb.topArrowCenter.visible()
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_END -> {
                hideAllArrows()
                vb.topArrowEnd.visible()
            }

            else -> hideAllArrows()
        }
    }

    private fun hideAllArrows() {
        with(vb) {
            topArrowStart.gone()
            topArrowCenter.gone()
            topArrowEnd.gone()
            bottomArrowStart.gone()
            bottomArrowCenter.gone()
            bottomArrowEnd.gone()
        }
    }

    private fun setAction(action: String) {
        mEventAction = action
        dismiss()
    }

    fun show() {
        if (mPopupWindow?.isShowing == true) return
        mContentLayout?.viewTreeObserver?.addOnGlobalLayoutListener(mLocationLayoutListener)

        mRootView?.post {
            mPopupWindow?.showAtLocation(
                mRootView,
                Gravity.NO_GRAVITY,
                mRootView?.width ?: 0,
                mRootView?.height ?: 0
            )
        }
    }

    private val mLocationLayoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val popup = mPopupWindow ?: return

            TooltipUtils.removeOnGlobalLayoutListener(popup.contentView, this)

            popup.contentView.viewTreeObserver.addOnGlobalLayoutListener(mShowLayoutListener)

            calculatePopupLocation()?.let {
                popup.isClippingEnabled = true
                popup.update(it.x.toInt(), it.y.toInt(), popup.width, popup.height)
                popup.contentView.requestLayout()
            }
        }
    }

    private val mShowLayoutListener: OnGlobalLayoutListener = object : OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            val popup = mPopupWindow ?: return
            TooltipUtils.removeOnGlobalLayoutListener(popup.contentView, this)
            mOnShowListener?.onShow(this@TooltipView)
        }
    }

    private fun calculatePopupLocation(): PointF? {
        val popup = mPopupWindow ?: return null
        val anchorView = mAnchorView ?: return null

        val location = PointF()
        val anchorRect: RectF = TooltipUtils.calculateRectInWindow(anchorView)
        val anchorCenter = PointF(anchorRect.centerX(), anchorRect.centerY())

        when {
            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_START -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.top - popup.contentView.height - mMargin
            }

            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_CENTER -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.top - popup.contentView.height - mMargin
            }

            mGravity == GRAVITY_TOP && mArrowAlign == ALIGN_END -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.top - popup.contentView.height - mMargin
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_START -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.bottom + mMargin
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_CENTER -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.bottom + mMargin
            }

            mGravity == GRAVITY_BOTTOM && mArrowAlign == ALIGN_END -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorRect.bottom + mMargin
            }

            mGravity == GRAVITY_CENTER -> {
                location.x = anchorCenter.x - popup.contentView.width / 2f
                location.y = anchorCenter.y - popup.contentView.height / 2f
            }
        }
        return location
    }

    override fun onDismiss() {
        TooltipUtils.removeOnGlobalLayoutListener(
            mPopupWindow?.contentView,
            mLocationLayoutListener
        )

        when (mEventAction) {
            ON_CLOSE_BTN_ACTION -> mOnCloseBtnListener?.onClose(this)
            ON_VIEW_CLICK_ACTION -> mOnClickListener?.onClick(this@TooltipView)
            else -> mOnSkipListener?.onSkip(this@TooltipView)
        }

        mOnDismissListener?.onDismiss(this)

        TooltipUtils.removeOnGlobalLayoutListener(
            mPopupWindow?.contentView,
            mLocationLayoutListener
        )
        TooltipUtils.removeOnGlobalLayoutListener(mPopupWindow?.contentView, mShowLayoutListener)
    }

    fun dismiss() {
        mPopupWindow?.dismiss()
    }

    interface OnShowListener {
        fun onShow(tooltipView: TooltipView)
    }

    interface OnDismissListener {
        fun onDismiss(tooltipView: TooltipView)
    }

    interface OnCloseBtnListener {
        fun onClose(tooltipView: TooltipView)
    }

    interface OnClickListener {
        fun onClick(tooltipView: TooltipView)
    }

    interface OnSkipListener {
        fun onSkip(tooltipView: TooltipView)
    }

    class Builder(context: Context) {
        var onShowListener: OnShowListener? = null
        var onDismissListener: OnDismissListener? = null
        var onCloseBtnListener: OnCloseBtnListener? = null
        var onClickListener: OnClickListener? = null
        var onSkipListener: OnSkipListener? = null

        val context: Context
        var title: CharSequence = ""
        var subtitle: CharSequence = ""
        var anchorView: View? = null
        var gravity: Int = GRAVITY_BOTTOM
        var arrowAlign: Int = ALIGN_CENTER
        var focusable: Boolean = true
        var margin = 0f

        init {
            this.context = context
        }

        fun title(text: CharSequence): Builder {
            title = text; return this
        }

        fun title(@StringRes textRes: Int): Builder {
            title = context.getString(textRes); return this
        }

        fun subtitle(text: CharSequence): Builder {
            subtitle = text; return this
        }

        fun subtitle(@StringRes textRes: Int): Builder {
            subtitle = context.getString(textRes); return this
        }

        fun anchorView(view: View): Builder {
            anchorView = view; return this
        }

        fun gravity(gr: Int): Builder {
            gravity = gr; return this
        }

        fun arrowAlign(align: Int): Builder {
            arrowAlign = align; return this
        }

        fun margin(mar: Float): Builder {
            margin = mar; return this
        }

        fun margin(@DimenRes marginRes: Int): Builder {
            margin = context.resources.getDimension(marginRes)
            return this
        }

        fun onShowListener(listener: OnShowListener): Builder {
            onShowListener = listener
            return this
        }

        fun onDismissListener(listener: OnDismissListener): Builder {
            onDismissListener = listener
            return this
        }

        fun onCloseBtnListener(listener: OnCloseBtnListener): Builder {
            onCloseBtnListener = listener
            return this
        }

        fun onClickListener(listener: OnClickListener): Builder {
            onClickListener = listener
            return this
        }

        fun onSkipListener(listener: OnSkipListener): Builder {
            onSkipListener = listener
            return this
        }

        fun build(): TooltipView {
            return TooltipView(this)
        }
    }

    companion object {
        const val GRAVITY_TOP = 0
        const val GRAVITY_BOTTOM = 1
        const val GRAVITY_CENTER = 4

        const val ALIGN_CENTER = 0
        const val ALIGN_START = 1
        const val ALIGN_END = 2

        const val ON_VIEW_CLICK_ACTION = "ON_VIEW_CLICK_ACTION"
        const val ON_CLOSE_BTN_ACTION = "ON_CLOSE_BTN_ACTION"
        const val ON_SKIP_ACTION = "ON_SKIP_ACTION"
    }
}