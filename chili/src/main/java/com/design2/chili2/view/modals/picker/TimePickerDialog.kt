package com.design2.chili2.view.modals.picker

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewDialogTimePickerBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import java.util.*

class TimePickerDialog: DialogFragment() {

    lateinit var vb: ChiliViewDialogTimePickerBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        setupDialogLayoutManagerParams(dialog)
        return dialog
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        vb = ChiliViewDialogTimePickerBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupPicker(savedInstanceState)
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun setupViews() {
        this.vb.run {
            timePicker.setIs24HourView(true)
            tvTitle.text = arguments?.getString(TIME_ARG_TITLE)
            btnDone.text = arguments?.getString(TIME_ARG_BUTTON_TEXT)
            btnDone.setOnSingleClickListener {
                setFragmentResult()
                dismiss()
            }
        }
    }

    private fun setupDialogLayoutManagerParams(dialog: Dialog?) {
        dialog?.window?.attributes = (dialog?.window?.attributes ?: WindowManager.LayoutParams()).apply {
            gravity = Gravity.BOTTOM
            width = WindowManager.LayoutParams.MATCH_PARENT
            horizontalMargin = resources.getDimensionPixelSize(R.dimen.padding_16dp).toFloat()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(TIME_ARG_CURRENT_TIME, getSelectedTime())
        super.onSaveInstanceState(outState)
    }

    private fun setFragmentResult() {
        val requestKey = arguments?.getString(TIME_PICKER_DIALOG_RESULT) ?: TIME_PICKER_DIALOG_RESULT
        parentFragmentManager.setFragmentResult(requestKey, bundleOf(
            TIME_ARG_SELECTED_TIME to getSelectedTime())
        )
    }

    private fun getSelectedTime(): Calendar {
        val result = Calendar.getInstance()
        vb.timePicker.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                result.set(Calendar.HOUR_OF_DAY, hour)
                result.set(Calendar.MINUTE, minute)
            }
        }
        return result
    }

    private fun setupPicker(savedInstanceState: Bundle?) {
        val currentDate = when (savedInstanceState != null) {
            true -> savedInstanceState.getSerializable(TIME_ARG_CURRENT_TIME) as? Calendar
            else -> requireArguments().getSerializable(TIME_ARG_CURRENT_TIME) as? Calendar
        }
        (currentDate ?: Calendar.getInstance()).run {
            vb.timePicker.setIs24HourView(true)
            setPickerCurrentHour(get(Calendar.HOUR_OF_DAY))
            setPickerCurrentMinute(get(Calendar.MINUTE))
        }
    }

    private fun setPickerCurrentHour(currentHour: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.timePicker.hour = currentHour
        } else {
            vb.timePicker.currentHour = currentHour
        }
    }

    private fun setPickerCurrentMinute(currentMinute: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.timePicker.minute = currentMinute
        } else {
            vb.timePicker.currentMinute = currentMinute
        }
    }

    companion object {
        const val TIME_PICKER_DIALOG_RESULT = "timePickerDialogResult"

        const val TIME_ARG_SELECTED_TIME = "selectedTime"
        private const val TIME_ARG_CURRENT_TIME = "currentTime"
        private const val TIME_ARG_TITLE = "title"
        private const val TIME_ARG_BUTTON_TEXT = "buttonText"

        fun create(
            buttonText: String,
            titleText: String,
            currentTime: Calendar = Calendar.getInstance(),
            requestKey: String
        ): TimePickerDialog {
            return TimePickerDialog().apply {
                arguments = bundleOf(
                    TIME_ARG_CURRENT_TIME to currentTime,
                    TIME_ARG_TITLE to titleText,
                    TIME_ARG_BUTTON_TEXT to buttonText,
                    TIME_PICKER_DIALOG_RESULT to requestKey
                )
            }
        }
    }
}