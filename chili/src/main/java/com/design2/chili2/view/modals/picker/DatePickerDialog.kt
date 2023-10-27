package com.design2.chili2.view.modals.picker

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewDialogDatePickerBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import java.util.Calendar

class DatePickerDialog : DialogFragment() {

    lateinit var vb: ChiliViewDialogDatePickerBinding

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
        vb = ChiliViewDialogDatePickerBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupPicker(savedInstanceState)
        dialog?.window?.setLayout(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
    }

    private fun setupViews() {
        this.vb.run {
            tvTitle.text = arguments?.getString(ARG_TITLE)
            btnDone.text = arguments?.getString(ARG_BUTTON_TEXT)
            btnDone.setOnSingleClickListener {
                setFragmentResult()
                dismiss()
            }
        }
    }

    private fun setupDialogLayoutManagerParams(dialog: Dialog) {
        dialog.window?.attributes = (dialog.window?.attributes ?: LayoutParams()).apply {
            gravity = Gravity.BOTTOM
            width = LayoutParams.MATCH_PARENT
            horizontalMargin = resources.getDimensionPixelSize(R.dimen.padding_16dp).toFloat()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(ARG_CURRENT_DATE, getSelectedDate())
        super.onSaveInstanceState(outState)
    }

    private fun setFragmentResult() {
        parentFragmentManager.setFragmentResult(PICKER_DIALOG_RESULT, bundleOf(
            ARG_SELECTED_DATE to getSelectedDate()
        ))
    }

    private fun getSelectedDate(): Calendar {
        val result = Calendar.getInstance()
        vb.datePicker.run {
            result.set(year, month, dayOfMonth, 0, 0, 0)
        }
        return result
    }

    private fun setupPicker(savedInstanceState: Bundle?) {
        val currentDate = when (savedInstanceState != null) {
            true -> savedInstanceState.getSerializable(ARG_CURRENT_DATE) as? Calendar
            else -> requireArguments().getSerializable(ARG_CURRENT_DATE) as? Calendar
        }
        (currentDate ?: Calendar.getInstance()).run {
            vb.datePicker.updateDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            )
        }

        val endLimitDate = requireArguments().getSerializable(ARG_END_LIMIT_DATE) as? Calendar
        endLimitDate?.let { vb.datePicker.maxDate = endLimitDate.timeInMillis }

        val startLimitDate = requireArguments().getSerializable(ARG_START_LIMIT_DATE) as? Calendar
        startLimitDate?.let { vb.datePicker.minDate = startLimitDate.timeInMillis }
    }

    companion object {
        const val PICKER_DIALOG_RESULT = "pickerDialogResult"

        const val ARG_SELECTED_DATE = "selectedDate"
        private const val ARG_CURRENT_DATE = "currentDate"
        private const val ARG_END_LIMIT_DATE = "endLimitDate"
        private const val ARG_START_LIMIT_DATE = "startLimitDate"
        private const val ARG_TITLE = "title"
        private const val ARG_BUTTON_TEXT = "buttonText"

        fun create(
            buttonText: String,
            titleText: String,
            currentDate: Calendar = Calendar.getInstance(),
            startLimitDate: Calendar? = null,
            endLimitDate: Calendar? = null,
        ): DatePickerDialog {
            return DatePickerDialog().apply {
                arguments = bundleOf(
                    ARG_CURRENT_DATE to currentDate,
                    ARG_END_LIMIT_DATE to endLimitDate,
                    ARG_START_LIMIT_DATE to startLimitDate,
                    ARG_TITLE to titleText,
                    ARG_BUTTON_TEXT to buttonText
                )
            }
        }
    }
}