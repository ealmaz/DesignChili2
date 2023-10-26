package com.design2.chili2.view.modals.picker

import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewDialogRangeDatePickerBinding
import com.design2.chili2.extensions.setOnSingleClickListener
import java.util.*

class RangeDatePickerDialog : DialogFragment() {

    private var listener: RangePickerListener? = null

    lateinit var vb: ChiliViewDialogRangeDatePickerBinding

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
        vb = ChiliViewDialogRangeDatePickerBinding.inflate(inflater, container, false)
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
            btnDone.text = arguments?.getString(ARG_BUTTON_TEXT)
            tvTitleFrom.text = arguments?.getString(ARG_START_TITLE)
            tvTitleTo.text = arguments?.getString(ARG_END_TITLE)
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
        outState.putSerializable(ARG_CURRENT_START_DATE, getSelectedDate(DateType.FROM))
        outState.putSerializable(ARG_CURRENT_END_DATE, getSelectedDate(DateType.TO))
        super.onSaveInstanceState(outState)
    }

    private fun setFragmentResult() {
        if (listener != null) {
            listener?.onRangeSelected(getSelectedDate(DateType.FROM), getSelectedDate(DateType.TO))
        } else {
            parentFragmentManager.setFragmentResult(RANGE_PICKER_DIALOG_RESULT, bundleOf(
                ARG_SELECTED_START_DATE to getSelectedDate(DateType.FROM),
                ARG_SELECTED_END_DATE to getSelectedDate(DateType.TO)
            ))
        }
    }

    private fun getSelectedDate(type: DateType): Calendar {
        val result = Calendar.getInstance()
        when (type) {
            DateType.FROM -> vb.datePickerFrom.apply {
                result.set(year, month, dayOfMonth, 0, 0, 0)

            }
            DateType.TO -> vb.datePickerTo.apply {
                result.set(year, month, dayOfMonth, 23, 59, 59)
            }
        }
        return result
    }

    private fun setupPicker(savedInstanceState: Bundle?) {
        val currentStartDate = when (savedInstanceState != null) {
            true -> savedInstanceState.getSerializable(ARG_CURRENT_START_DATE) as? Calendar
            else -> requireArguments().getSerializable(ARG_CURRENT_START_DATE) as? Calendar
        }
        (currentStartDate ?: Calendar.getInstance()).run {
            vb.datePickerFrom.updateDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            )
        }

        val currentEndDate = when (savedInstanceState != null) {
            true -> savedInstanceState.getSerializable(ARG_CURRENT_END_DATE) as? Calendar
            else -> requireArguments().getSerializable(ARG_CURRENT_END_DATE) as? Calendar
        }
        (currentEndDate ?: Calendar.getInstance()).run {
            vb.datePickerTo.updateDate(
                get(Calendar.YEAR),
                get(Calendar.MONTH),
                get(Calendar.DAY_OF_MONTH)
            )
        }

        val endLimitDate = requireArguments().getSerializable(ARG_END_LIMIT_DATE) as? Calendar
        endLimitDate?.let {
            vb.datePickerTo.maxDate = endLimitDate.timeInMillis
            vb.datePickerFrom.maxDate = endLimitDate.timeInMillis
        }

        val startLimitDate = requireArguments().getSerializable(ARG_START_LIMIT_DATE) as? Calendar
        startLimitDate?.let {
            vb.datePickerTo.minDate = startLimitDate.timeInMillis
            vb.datePickerFrom.minDate = startLimitDate.timeInMillis
        }
    }

    companion object {
        const val RANGE_PICKER_DIALOG_RESULT = "rangePickerDialogResult"

        const val ARG_SELECTED_START_DATE = "selectedStartDate"
        const val ARG_SELECTED_END_DATE = "selectedEndDate"

        private const val ARG_CURRENT_START_DATE = "currentStartDate"
        private const val ARG_CURRENT_END_DATE = "currentEndDate"
        private const val ARG_END_LIMIT_DATE = "endLimitDate"
        private const val ARG_START_LIMIT_DATE = "startLimitDate"
        private const val ARG_START_TITLE = "startTITLE"
        private const val ARG_END_TITLE = "endTitle"
        private const val ARG_BUTTON_TEXT = "buttonText"

        fun create(
            buttonText: String,
            startTitle: String,
            endTitle: String,
            currentStartDate: Calendar = Calendar.getInstance(),
            currentEndDate: Calendar = Calendar.getInstance(),
            startLimitDate: Calendar? = null,
            endLimitDate: Calendar? = null,
            listener: RangePickerListener? = null
        ): RangeDatePickerDialog {
            return RangeDatePickerDialog().apply {
                this.listener = listener
                arguments = bundleOf(
                    ARG_CURRENT_START_DATE to currentStartDate,
                    ARG_CURRENT_END_DATE to currentEndDate,
                    ARG_END_LIMIT_DATE to endLimitDate,
                    ARG_START_LIMIT_DATE to startLimitDate,
                    ARG_START_TITLE to startTitle,
                    ARG_END_TITLE to endTitle,
                    ARG_BUTTON_TEXT to buttonText
                )
            }
        }
    }
}

private enum class DateType {
    FROM, TO
}

interface RangePickerListener {
    fun onRangeSelected(startDate: Calendar, endDate: Calendar)
}