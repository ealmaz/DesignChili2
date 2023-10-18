package com.design2.chili2.view.modals.bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import com.design2.chili2.R
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.view.modals.base.BaseViewBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.io.Serializable

class ActionBottomSheet : BaseViewBottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Chili_BottomSheetStyle)
    }

    override fun createContentView(inflater: LayoutInflater, container: ViewGroup?): View {
        val linearLayout = LinearLayout(context).apply {
            val padding = resources.getDimensionPixelSize(R.dimen.padding_16dp)
            setPadding(padding, padding, padding, padding)
            orientation = LinearLayout.VERTICAL
            setBackgroundResource(R.drawable.chili_bg_rounded_bottom_sheet)
        }
        renderActionButtons(linearLayout)
        return linearLayout
    }

    private fun renderActionButtons(layout: LinearLayout) {
        val buttons = arguments?.get(BUTTONS_ARG) as? List<ActionBottomSheetButton>
        buttons?.forEach { buttonInfo ->
            layout.addView(createActionButtonView(buttonInfo))
        }
    }

    private fun createActionButtonView(buttonInfo: ActionBottomSheetButton): TextView {
        val buttonStyle = when(buttonInfo.type) {
            ActionBottomSheetButtonType.SIMPLE -> R.style.Chili_ActionBottomSheetButtonStyle
            ActionBottomSheetButtonType.ACCENT -> R.style.Chili_ActionBottomSheetAccentButtonStyle
        }
        return TextView(context, null, 0, buttonStyle).apply {
            text = buttonInfo.title
            setOnSingleClickListener { buttonInfo.onClickAction.invoke(this@ActionBottomSheet) }
        }
    }

    companion object {

        const val BUTTONS_ARG = "buttons"

        fun create(buttons: List<ActionBottomSheetButton>): ActionBottomSheet {
            return ActionBottomSheet().apply {
                arguments = bundleOf(BUTTONS_ARG to buttons)
            }
        }
    }
}

enum class ActionBottomSheetButtonType: Serializable {
    SIMPLE, ACCENT
}

data class ActionBottomSheetButton(
    val title: String?,
    val type: ActionBottomSheetButtonType,
    @Transient val onClickAction: BottomSheetDialogFragment.() -> Unit
): Serializable