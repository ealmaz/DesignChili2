package com.design2.chili2.view.modals.bottom_sheet_constructor

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentManager
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewBottomSheetUniversalConstructorBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.updateNavigationBarColor
import com.design2.chili2.extensions.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class ConfigurableBottomSheetFragment(val config: BottomSheetConfig) : BottomSheetDialogFragment() {

    val pendingBlock = mutableListOf<Pair<ContainerType, Any>>()

    private lateinit var vb: ChiliViewBottomSheetUniversalConstructorBinding

    var bottomSheetView: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.Chili_BottomSheetStyle)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) updateNavbarColor(dialog)
        dialog.run {
            setOnShowListener { onShowDialog(this) }
            setupBottomSheetBackButtonEnabled(dialog)
        }
        return dialog
    }

    private fun onShowDialog(dialog: Dialog) {
        val bottomSheet: FrameLayout? = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet)
        this.bottomSheetView = bottomSheet
        val behavior = bottomSheet?.let { BottomSheetBehavior.from<View>(bottomSheet) }
        setupBottomSheetBehavior(behavior)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (view.parent as View).setBackgroundColor(Color.TRANSPARENT)
        setupBottomSheetCloseIcon()
        setupBottomSheetHideable()
        setupTopDrawableVisibility()
        pendingBlock.forEach {
            when (val block = it.second) {
                is ViewGroup -> addViewToContainer(block, it.first)
                is FragmentContainer -> {
                    addViewToContainer(block.result, it.first)
                    block.inflateContainer(childFragmentManager)
                }
            }

        }
    }

    private fun addViewToContainer(view: View, containerType: ContainerType = ContainerType.NORMAL) {
        when (containerType) {
            ContainerType.NORMAL -> vb.bottomSheetView.addView(view)
            ContainerType.PINNED -> vb.bottomSheetPinnedView.addView(view)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = ChiliViewBottomSheetUniversalConstructorBinding.inflate(inflater)
        return vb.root
    }

    private fun setupBottomSheetCloseIcon() {
        vb.ivClose.apply {
            when (config.implementCloseIcon) {
                true -> {
                    visible()
                    setOnSingleClickListener {
                        config.onCloseBtnClick?.invoke()
                        dismiss()
                    }
                }
                else -> gone()
            }
        }
    }

    private fun setupTopDrawableVisibility() {
        vb.ivTopDrawable.isVisible = config.isTopDrawableVisible
    }

    private fun setupBottomSheetHideable() {
        if (!config.isHideable) {
            val touchOutsideView = dialog!!.window?.decorView?.findViewById<View>(com.google.android.material.R.id.touch_outside)
            touchOutsideView?.setOnClickListener(null)
        }
    }

    fun setupBottomSheetBehavior(behavior: BottomSheetBehavior<*>?) {
        behavior?.run {
            if (!config.isHideable) {
                isHideable = false
                peekHeight = getWindowHeight() * 20 / 100
            }
            skipCollapsed = true
            state = config.state
        }
    }

    private fun setupBottomSheetBackButtonEnabled(dialog: BottomSheetDialog) {
        if (!config.isHideable) {
            dialog.setOnKeyListener { _, keyCode, _ ->
                keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.ACTION_DOWN
            }
        }
    }

    private fun updateNavbarColor(dialog: Dialog) {
        dialog.window?.updateNavigationBarColor()
    }

    private fun getWindowHeight(): Int {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        return displayMetrics.heightPixels
    }

    fun addViewBlock(block: Any, containerType: ContainerType = ContainerType.NORMAL) {
        pendingBlock.add(containerType to block)
    }

    fun show(fragmentManager: FragmentManager) {
        this.show(fragmentManager, null)
    }

    override fun onPause() {
        super.onPause()
        dismiss()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        vb.bottomSheetView.removeAllViews()
        vb.bottomSheetPinnedView.removeAllViews()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        config.onDismissCallback?.invoke()
    }

}

enum class ContainerType {
    NORMAL, PINNED
}
