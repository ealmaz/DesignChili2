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
import com.design2.chili2.databinding.ChiliViewDialogNumberPickerBinding
import com.design2.chili2.extensions.setOnSingleClickListener

class NumberPickerDialog : DialogFragment() {

    private lateinit var vb: ChiliViewDialogNumberPickerBinding

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
        vb = ChiliViewDialogNumberPickerBinding.inflate(inflater, container, false)
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        setupPicker()
        dialog?.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    private fun setupViews() {
        this.vb.run {
            tvTitle.text = arguments?.getString(ARG_TITLE)
            btnDone.text = arguments?.getString(ARG_BUTTON_TEXT)
            btnDone.setOnSingleClickListener {
                setFragmentResult()
                dismiss()
            }

            numberPicker.run {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    selectionDividerHeight = resources.getDimensionPixelSize(R.dimen.view_1dp)
                }
            }

        }
    }

    private fun setupDialogLayoutManagerParams(dialog: Dialog) {
        dialog.window?.attributes = (dialog.window?.attributes ?: WindowManager.LayoutParams()).apply {
            gravity = Gravity.BOTTOM
            width = WindowManager.LayoutParams.MATCH_PARENT
            horizontalMargin = resources.getDimensionPixelSize(R.dimen.padding_16dp).toFloat()
        }
    }

    private fun setFragmentResult() {
        parentFragmentManager.setFragmentResult(NUMBER_PICKER_RESULT, bundleOf(ARG_SELECTED_NUMBER to vb.numberPicker.value))
    }

    private fun setupPicker() = with(vb) {
        numberPicker.minValue = requireArguments().getInt(ARG_MIN_NUMBER)
        numberPicker.maxValue = requireArguments().getInt(ARG_MAX_NUMBER)
    }

    companion object {
        const val NUMBER_PICKER_RESULT = "numberPickerDialog"

        const val ARG_SELECTED_NUMBER = "selectedNumber"
        private const val ARG_MIN_NUMBER = "minNumber"
        private const val ARG_MAX_NUMBER = "maxNumber"
        private const val ARG_TITLE = "title"
        private const val ARG_BUTTON_TEXT = "buttonText"

        fun create(
            buttonText: String,
            titleText: String,
            minNumber: Int,
            maxNumber: Int
        ): NumberPickerDialog {
            return NumberPickerDialog().apply {
                arguments = bundleOf(
                    ARG_TITLE to titleText,
                    ARG_BUTTON_TEXT to buttonText,
                    ARG_MIN_NUMBER to minNumber,
                    ARG_MAX_NUMBER to maxNumber
                )
            }
        }
    }
}