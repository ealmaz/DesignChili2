package com.design2.chili2.view.input.otp

import android.content.ClipboardManager
import android.content.Context
import android.graphics.Rect
import android.text.InputFilter.LengthFilter
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.appcompat.widget.PopupMenu
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet.TOP
import androidx.constraintlayout.widget.ConstraintSet.START
import androidx.constraintlayout.widget.ConstraintSet.END
import androidx.constraintlayout.widget.ConstraintSet.PARENT_ID
import androidx.constraintlayout.widget.ConstraintSet.CHAIN_SPREAD_INSIDE
import androidx.core.view.children
import androidx.core.view.forEach
import androidx.core.widget.addTextChangedListener
import com.design2.chili2.R
import com.design2.chili2.extensions.dp
import com.design2.chili2.extensions.getPixelSizeFromAttr
import com.design2.chili2.extensions.setOnSingleClickListener
import com.design2.chili2.extensions.setTextOrHide
import com.design2.chili2.extensions.setupConstraint
import com.design2.chili2.view.input.SelectionChangedListener
import com.design2.chili2.view.input.SelectionEditText

class OtpInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.otpInputViewDefaultStyle,
    defStyleRes: Int = R.style.Chili_OtpInputViewStyle,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes), SelectionChangedListener {

    private lateinit var view: OtpInputViewVariables
    private var otpLength = DEFAULT_OTP_LENGTH

    private val pastePopupMenu: PopupMenu by lazy {
        val popup = PopupMenu(context, this)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_paste, popup.menu)
        popup.setOnMenuItemClickListener { onPastePopupMenuClicked(it) }
        popup
    }

    private val clipboard: ClipboardManager by lazy {
        context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    private var otpCompleteListener: OnOtpCompleteListener? = null

    init {
        inflateView(context)
        setupView()
        obtainAttributes(context, attrs, defStyleAttr, defStyleRes)
    }

    private fun inflateView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_input_otp, this, true)
        this.view = OtpInputViewVariables(
            etInput = view.findViewById(R.id.et_input),
            tvAction = view.findViewById(R.id.tv_action),
            tvMessage = view.findViewById(R.id.tv_message),
            itemContainer = view.findViewById(R.id.item_container)
        )
    }

    private fun obtainAttributes(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) {
        context.obtainStyledAttributes(attrs, R.styleable.OtpInputView, defStyleAttr, defStyleRes).run {
            setMessageText(getString(R.styleable.OtpInputView_message))
            setActionText(getString(R.styleable.OtpInputView_actionText))
            getResourceId(R.styleable.OtpInputView_messageTextAppearance, -1)
                .takeIf { it != -1 }?.let { setMessageTextAppearance(it) }
            getResourceId(R.styleable.OtpInputView_actionTextTextAppearance, -1)
                .takeIf { it != -1 }?.let { setActionTextAppearance(it) }
            getColor(R.styleable.OtpInputView_messageTextColor, -1)
                .takeIf { it != -1 }?.let { setMessageTextColor(it) }
            setOtpLength(getInteger(R.styleable.OtpInputView_otpLength, DEFAULT_OTP_LENGTH))
            recycle()
        }
    }

    fun setOtpLength(length: Int) {
        this.otpLength = length
        view.etInput.filters = arrayOf(
            LengthFilter(length)
        )
        setupOtpItemView(length)
    }

    private fun setupOtpItemView(length: Int) = when {
        length > DEFAULT_OTP_LENGTH -> addOtpItemViews(length - DEFAULT_OTP_LENGTH)
        length < DEFAULT_OTP_LENGTH -> removeOtpItemViews(DEFAULT_OTP_LENGTH - length)
        else -> {}
    }

    private fun addOtpItemViews(times: Int) {
        repeat(times) { addOtpItemView() }
    }

    private fun addOtpItemView() = with(view.itemContainer) {
        val itemsCount = childCount
        val items = children.toList()
        val oiv = OtpItemView(context, null)
        oiv.id = View.generateViewId()
        val paddingHorizontal = context.getPixelSizeFromAttr(R.attr.ChiliOtpInputViewItemHorizontalPadding)
        val paddingVertical = context.getPixelSizeFromAttr(R.attr.ChiliOtpInputViewItemVerticalPadding)
        oiv.setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical)
        oiv.setTextAppearance(R.style.Chili_H7_Primary_Bold)
        addView(oiv, ViewGroup.LayoutParams(44.dp, WRAP_CONTENT))
        setupConstraint {
            connect(oiv.id, TOP, PARENT_ID, TOP)
            connect(oiv.id, START, items[itemsCount - 1].id, END)
            connect(oiv.id, END, PARENT_ID, END)
            setHorizontalChainStyle(oiv.id, CHAIN_SPREAD_INSIDE)
            connect(items[itemsCount - 1].id, START, items[itemsCount - 2].id, END)
            connect(items[itemsCount - 1].id, END, oiv.id, START)
        }
    }

    private fun removeOtpItemViews(times: Int) {
        repeat(times) { removeOtpItemView() }
    }

    private fun removeOtpItemView() = with(view.itemContainer) {
        removeViewAt(view.itemContainer.childCount - 1)
        (children.last() as? OtpItemView)?.let { oiv ->
            setupConstraint { connect(oiv.id, END, PARENT_ID, END) }
        }
    }

    fun setActionText(text: CharSequence?) {
        view.tvAction.setTextOrHide(text)
    }

    fun setActionText(resId: Int) {
        view.tvAction.setTextOrHide(resId)
    }

    fun setActionTextAppearance(resId: Int) {
        view.tvAction.setTextAppearance(resId)
    }

    fun setOnActionClickListener(onClick: () -> Unit) {
        view.tvAction.setOnSingleClickListener { onClick.invoke() }
    }

    fun setActionTextEnabled(isEnabled: Boolean) {
        view.tvAction.isEnabled = isEnabled
    }

    fun setMessageText(text: CharSequence?) {
        view.tvMessage.setTextOrHide(text)
    }

    fun setMessageText(resId: Int) {
        view.tvMessage.setTextOrHide(resId)
    }

    fun setMessageTextAppearance(resId: Int) {
        view.tvMessage.setTextAppearance(resId)
    }

    fun setMessageTextColor(@ColorInt colorInt: Int) {
        view.tvMessage.setTextColor(colorInt)
    }

    fun setText(charSequence: CharSequence?) {
        view.etInput.setText(charSequence)
    }

    fun setText(resId: Int) {
        view.etInput.setText(resId)
    }

    fun setOnOtpCompleteListener(otpCompleteListener: OnOtpCompleteListener) {
        this.otpCompleteListener = otpCompleteListener
    }

    fun setupState(otpItemState: OtpItemState) {
        view.itemContainer.forEach {
            (it as? OtpItemView)?.setState(otpItemState)
        }
    }

    fun getInputText(): String {
        return view.etInput.text?.toString() ?: ""
    }

    override fun requestFocus(direction: Int, previouslyFocusedRect: Rect?): Boolean {
        requestFocusAndShowKeyboard()
        return super.requestFocus(direction, previouslyFocusedRect)
    }

    fun requestFocusAndShowKeyboard() {
        view.etInput.requestFocus()
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view.etInput, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun setupView() {
        view.etInput.apply {
            setOnLongClickListener {
                pastePopupMenu.show()
                true
            }
            setSelectAllOnFocus(false)
            setTextIsSelectable(false)
            setSelectionChangedListener(this@OtpInputView)
            setFirstItemActive()
            addTextChangedListener { text ->
                text?.let { this@OtpInputView.setTextToItems(it.toString()) }
            }
        }
    }

    private fun setFirstItemActive() {
        (view.itemContainer.children.firstOrNull() as? OtpItemView)
            ?.setState(OtpItemState.ACTIVE)
    }

    private fun setTextToItems(newText: String) {
        view.itemContainer.children.forEachIndexed { i, view ->
            (view as? OtpItemView)?.apply {
                text = newText.getOrNull(i)?.toString()
                when (newText.length) {
                    i + 1 -> setState(OtpItemState.ACTIVE)
                    else -> setState(OtpItemState.INACTIVE)
                }
            }
        }
        if (newText.isEmpty()) setFirstItemActive()
        otpCompleteListener?.onInput(newText)
        if (newText.length == otpLength) { otpCompleteListener?.onOtpInputComplete(newText) }
    }

    private fun onPastePopupMenuClicked(item: MenuItem): Boolean {
        if (item.itemId == R.id.paste) pasteClipDataToView()
        return true
    }

    private fun pasteClipDataToView() {
        try {
            val pasteData = clipboard.primaryClip?.getItemAt(0)?.text?.toString() ?: ""
            setText(pasteData.replace("[^0-9]".toRegex(), ""))
        } catch (_: Throwable) {}
    }

    companion object {
        const val DEFAULT_OTP_LENGTH = 6
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int): Boolean {
        if (selStart != selEnd || selStart != view.etInput.length()) view.etInput.moveSelectionToEnd()
        return true
    }
}

interface OnOtpCompleteListener {
    fun onOtpInputComplete(otp: String)
    fun onInput(text: String?)
}

data class OtpInputViewVariables(
    val etInput: SelectionEditText,
    val itemContainer: ConstraintLayout,
    val tvMessage: TextView,
    val tvAction: TextView,
)