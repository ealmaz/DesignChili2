package com.design2.chili2.extensions

import android.view.View
import androidx.annotation.DrawableRes
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

fun Fragment.showTimerSnackbar(
    text: String,
    onBtnClick: (ChiliSnackBar) -> Unit,
    onEndTime: () -> Unit,
    duration: Int = 5000,
    actionText: String
) {
    var isTimeFinished = false
    ChiliSnackBar.Builder(requireView())
        .setSnackbarTimerInfo(
            TimerInfo(
                duration.toLong(),
                {
                    isTimeFinished = true
                    onEndTime()
                },
                showCountDownTimer = true,
                successIcon = R.drawable.chili_ic_success_24
            )
        )
        .setSnackbarMessage(text)
        .setSnackbarActionInfo(
            ActionInfo(actionText, onBtnClick)
        )
        .setOnDismissCallback { if ((isRemoving || isDetached || !isVisible) && !isTimeFinished) onEndTime() }
        .build()
        .show()
}

fun AppCompatActivity.showTimerSnackbar(
    text: String,
    onBtnClick: (ChiliSnackBar) -> Unit,
    onEndTime: () -> Unit,
    duration: Int = 5000,
    view: View,
    actionText: String
) {
    ChiliSnackBar.Builder(view)
        .setSnackbarTimerInfo(
            TimerInfo(
                duration.toLong(),
                { onEndTime() },
                showCountDownTimer = true,
                successIcon = R.drawable.chili_ic_success_24
            )
        )
        .setSnackbarMessage(text)
        .setSnackbarActionInfo(
            ActionInfo(actionText, onBtnClick)
        )
        .setOnDismissCallback { if (isDestroyed) onEndTime() }
        .build()
        .show()
}

fun AppCompatActivity.showLogoSnackbar(text: String, @DrawableRes logo: Int, view: View) {
    ChiliSnackBar.Builder(view)
        .setSnackbarIcon(logo)
        .setSnackbarMessage(text)
        .build()
        .show()
}

fun Fragment.showLogoSnackbar(text: String, @DrawableRes logo: Int) {
    ChiliSnackBar.Builder(requireView())
        .setSnackbarIcon(logo)
        .setSnackbarMessage(text)
        .build()
        .show()
}