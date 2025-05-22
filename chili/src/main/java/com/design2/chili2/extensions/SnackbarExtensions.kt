package com.design2.chili2.extensions

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.design2.chili2.R
import com.design2.chili2.view.snackbar.ActionInfo
import com.design2.chili2.view.snackbar.ChiliSnackBar
import com.design2.chili2.view.snackbar.TimerInfo

fun AppCompatActivity.showSimpleSnackbar(rootView: View, message: String) {
    ChiliSnackBar.Builder(rootView)
        .setSnackbarMessage(message)
        .build()
        .show()
}


fun AppCompatActivity.showInfinitiveLoaderSnackbar(rootView: View, message: String) {
    ChiliSnackBar.Builder(rootView)
        .setSnackbarMessage(message)
        .setIsInfiniteLoaderSnackbar(true)
        .build()
        .show()
}

fun AppCompatActivity.showTimerActionBeforeSuccessCnackbar(
    rootView: View,
    timerMessage: String,
    successMessage: String,
    actionText: String,
    actionClick: (ChiliSnackBar) -> Unit,
    onTimerExpire: () -> Unit,
    timerDurationMills: Long
) {
    ChiliSnackBar.Builder(rootView)
        .setSnackbarDurationMills(timerDurationMills + 2000)
        .setSnackbarMessage(timerMessage)
        .setSnackbarTimerInfo(TimerInfo(timerDurationMills, {
            it.setSnackbarIcon(R.drawable.chili_ic_success)
            it.setMessage(successMessage)
            onTimerExpire()
        }))
        .setSnackbarActionInfo(ActionInfo(actionText, actionClick))
        .build()
        .show()
}

fun Fragment.showTimerSnackbar(text: String, onBtnClick: (ChiliSnackBar) -> Unit, onEndTime: () -> Unit, duration: Int = 5000): ChiliSnackBar {
    var isTimeFinished = false
    return ChiliSnackBar.Builder(requireView())
        .setSnackbarTimerInfo(
            TimerInfo(
                duration.toLong(),
                {
                    isTimeFinished = true
                    onEndTime()
                },
                showCountDownTimer = true,
                successIcon = R.drawable.chili_ic_success
            )
        )
        .setSnackbarDurationMills(duration.toLong() + 2000)
        .setSnackbarMessage(text)
        .setSnackbarActionInfo(
            ActionInfo("Cancel", onBtnClick)
        )
        .setOnDismissCallback { if ((isRemoving || isDetached || !isVisible) && !isTimeFinished) onEndTime() }
        .build().also { it.show() }
}