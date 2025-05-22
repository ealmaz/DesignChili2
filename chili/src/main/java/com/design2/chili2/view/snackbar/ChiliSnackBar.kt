package com.design2.chili2.view.snackbar

import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import com.design2.chili2.R
import com.design2.chili2.extensions.*
import com.google.android.material.snackbar.BaseTransientBottomBar

class ChiliSnackBar private constructor(
    parent: ViewGroup,
    var content: SnackbarLayoutView)
    : BaseTransientBottomBar<ChiliSnackBar>(parent, content, content) {

    private var timer: CountDownTimer? = null
    private var vb = content.vb

    init {
        getView().setBackgroundColor(ContextCompat.getColor(view.context, android.R.color.transparent))
        getView().setPadding(0, 0, 0, 0)
    }

    private fun setupVisibilityCallback(onDismissCallback: (() -> (Unit))?, onShowCallback: (() -> (Unit))?, onTimerExpire : ((ChiliSnackBar) -> Unit)?) {
        addCallback(object : BaseTransientBottomBar.BaseCallback<ChiliSnackBar>() {
            override fun onDismissed(transientBottomBar: ChiliSnackBar?, event: Int) {
                super.onDismissed(transientBottomBar, event)
                if (event == DISMISS_EVENT_CONSECUTIVE) onTimerExpire?.invoke(this@ChiliSnackBar)
                timer?.cancel()
                onDismissCallback?.invoke()
            }

            override fun onShown(transientBottomBar: ChiliSnackBar?) {
                super.onShown(transientBottomBar)
                onShowCallback?.invoke()
            }
        })
    }

    fun setSnackbarIcon(@DrawableRes icon: Int = -1) {
        if (icon == -1) return
        vb.apply {
            ivIcon.visible()
            ivIcon.setImageResource(icon)
            pbProgress.gone()
            tvSecondsLeft.gone()
        }
    }

    fun setupSnackbarAsLoading(isInfiniteLoaderSnackbar: Boolean) {
        if (!isInfiniteLoaderSnackbar) return
        setInfinitiveDuration()
        vb.apply {
            ivIcon.invisible()
            tvSecondsLeft.gone()
            pbProgress.indeterminateDrawable = context?.drawable(R.drawable.chili_circular_loop_progress_bar)
            pbProgress.isIndeterminate = true
            pbProgress.visible()
        }
    }

    fun setInfinitiveDuration() {
        duration = LENGTH_INDEFINITE
    }

    fun setMessage(message: String?) {
        vb.tvMessage.text = message
    }

    fun setMessage(@StringRes messageRes: Int) {
        vb.tvMessage.setText(messageRes)
    }

    fun setupActionButton(actionInfo: ActionInfo?) {
        when (actionInfo == null) {
            true -> vb.tvAction.gone()
            else -> {
                vb.tvAction.apply {
                    visible()
                    text = actionInfo.title
                    setOnSingleClickListener { actionInfo.onClick?.invoke(this@ChiliSnackBar) }
                }
            }
        }
    }

    fun setupTimer(timerInfo: TimerInfo?) {
        if (timerInfo == null) return
        timer = when (timerInfo.showCountDownTimer) {
            true -> setupTimerWithCountDown(timerInfo)
            else -> setupInvisibleTimer(timerInfo)
        }
        timer?.start()
    }

    fun setupBackgroundColor(@ColorRes color: Int?) {
        color?.let { content.setCustomBackgroundColor(it) }
    }

    fun setupGravity(gravity: Int) {
        val view = this.getView()
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = gravity
        view.layoutParams = params
    }

    private fun setupInvisibleTimer(timerInfo: TimerInfo): CountDownTimer {
        return object : CountDownTimer(timerInfo.durationMills, 1000) {
            override fun onTick(millisUntilFinished: Long) {}
            override fun onFinish() {
                timerInfo.onTimerExpire?.invoke(this@ChiliSnackBar)
            }
        }
    }

    private fun setupTimerWithCountDown(timerInfo: TimerInfo): CountDownTimer {
        vb.ivIcon.invisible()
        vb.pbProgress.apply {
            visible()
            progressDrawable = context?.drawable(R.drawable.chili_circular_progress_bar)
            max = timerInfo.durationMills.toInt()
        }
        vb.tvSecondsLeft.visible()
        return object : CountDownTimer(timerInfo.getSnackbarDuration(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                if (millisUntilFinished <= timerInfo.successDurationMills) {
                    setSnackbarIcon(timerInfo.successIcon)
                }
                setTimeToView(millisUntilFinished - timerInfo.successDurationMills)
            }

            override fun onFinish() {
                timerInfo.onTimerExpire?.invoke(this@ChiliSnackBar)
                dismiss()
            }
        }
    }

    private fun setTimeToView(time: Long) = with(vb) {
        val secondsLeft = (time / 1000 + 1).toString()
        tvSecondsLeft.text = secondsLeft
        pbProgress.progress = time.toInt()
    }

    class Builder(val rootView: View) {

        private var snackbarMessage: String? = null
        private var snackbarDurationMills: Long? = null
        private var snackbarActionInfo: ActionInfo? = null

        private var snackbarTimerInfo: TimerInfo? = null
        @DrawableRes private var snackbarIcon: Int = -1
        private var isInfiniteLoaderSnackbar: Boolean = false
        @ColorRes private var backgroundColor: Int? = null
        private var gravity: Int = Gravity.TOP
        private var onDismissCallback: (() -> (Unit))? = null
        private var onShowCallback: (() -> (Unit))? = null

        fun setSnackbarMessage(snackbarMessage: String): Builder {
            this.snackbarMessage = snackbarMessage
            return this
        }
        fun setSnackbarDurationMills(snackbarDurationMills: Long): Builder {
            this.snackbarDurationMills = snackbarDurationMills
            return this
        }
        fun setSnackbarActionInfo(snackbarActionInfo: ActionInfo): Builder {
            this.snackbarActionInfo = snackbarActionInfo
            return this
        }
        fun setOnDismissCallback(action: () -> (Unit)): Builder {
            this.onDismissCallback = action
            return this
        }
        fun setOnShowCallback(action: () -> Unit): Builder {
            this.onShowCallback = action
            return this
        }
        fun setSnackbarTimerInfo(snackbarTimerInfo: TimerInfo): Builder {
            this.snackbarTimerInfo = snackbarTimerInfo
            return this
        }
        fun setSnackbarIcon(@DrawableRes snackbarIcon: Int): Builder {
            this.snackbarIcon = snackbarIcon
            return this
        }
        fun setSnackbarBackgroundColor(@ColorRes color: Int): Builder {
            this.backgroundColor = color
            return this
        }
        fun setGravity(gravity: Int): Builder {
            this.gravity = gravity
            return this
        }
        fun setIsInfiniteLoaderSnackbar(isInfiniteLoaderSnackbar: Boolean): Builder {
            this.isInfiniteLoaderSnackbar = isInfiniteLoaderSnackbar
            return this
        }

        fun build(): ChiliSnackBar {
            val parent = rootView.findSuitableParent()
            val snackbarLayoutView = SnackbarLayoutView(parent.context)
            return ChiliSnackBar(parent, snackbarLayoutView).apply {
                setAnimationMode(ANIMATION_MODE_FADE)
                setMessage(snackbarMessage)
                setupTimer(snackbarTimerInfo)
                duration = snackbarDurationMills?.toInt() ?: snackbarTimerInfo?.getSnackbarDuration()?.toInt() ?: 5000
                setupActionButton(snackbarActionInfo)
                setSnackbarIcon(snackbarIcon)
                setupSnackbarAsLoading(isInfiniteLoaderSnackbar)
                setupBackgroundColor(backgroundColor)
                setupGravity(gravity)
                setupVisibilityCallback(onDismissCallback, onShowCallback, snackbarTimerInfo?.onTimerExpire)
            }
        }
    }
}

data class ActionInfo(
    var title: String? = null,
    var onClick: ((ChiliSnackBar) -> Unit)? = null
)

data class TimerInfo(
    var durationMills: Long = 0,
    var onTimerExpire: ((ChiliSnackBar) -> Unit)? = null,
    var showCountDownTimer: Boolean = true,
    @DrawableRes var successIcon: Int = R.drawable.chili_ic_success,
    var successDurationMills: Long = 2000,
) {
    fun getSnackbarDuration(): Long {
        return (durationMills).let {
            if(showCountDownTimer) it + successDurationMills else it
        }
    }
}

private fun View?.findSuitableParent(): ViewGroup {
    var view = this
    var fallback: ViewGroup? = null
    do {
        if (view is CoordinatorLayout) {
            return view
        } else if (view is FrameLayout) {
            if (view.id == android.R.id.content) {
                return view
            } else {
                fallback = view
            }
        }

        if (view != null) {
            val parent = view.parent
            view = if (parent is View) parent else null
        }
    } while (view != null)
    return fallback ?: throw IllegalArgumentException(
        "No suitable parent found from the given view. Please provide a valid view.")
}
