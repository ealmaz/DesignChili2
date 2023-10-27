package com.design2.chili2.view.input

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.*
import android.text.method.DigitsKeyListener
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.updatePadding
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewInputBaseBinding
import com.design2.chili2.extensions.*
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.visible
import com.design2.chili2.util.cyrillicRegex
import com.design2.chili2.view.input.text_watchers.ClearTextIconTextWatcher
import com.design2.chili2.view.input.text_watchers.SimpleTextWatcher
import com.google.android.material.textfield.TextInputLayout

open class BaseInputView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.inputViewDefaultStyle,
    defStyle: Int = R.style.Chili_InputViewStyle_Simple
): ConstraintLayout(context, attrs, defStyleAttr, defStyle) {

    lateinit var vb: ChiliViewInputBaseBinding

    protected val textWatchers by lazy { mutableListOf<TextWatcher>() }

    protected var messageText: String? = null
    protected var isErrorShown: Boolean = false
    protected var isLabelBehaviorEnabled: Boolean = false

    @ColorInt protected var hintTextColor: Int = -1
    protected var fieldBackground: Int = -1
    protected var fieldErrorBackground: Int = -1

    @StyleRes protected var inputViewTextAppearanceRes: Int = -1

    @ColorInt protected var messageTextColor: Int = -1
    @ColorInt protected var errorMessageTextColor: Int = -1

    init {
        inflateViews(context)
        obtainAttributes(attrs, defStyleAttr, defStyle)
    }

    private fun inflateViews(context: Context) {
        vb = ChiliViewInputBaseBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(attrs: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context?.obtainStyledAttributes(attrs, R.styleable.BaseInputView, defStyleAttr, defStyle)?.run {

            getResourceId(R.styleable.BaseInputView_errorFieldBackground, ContextCompat.getColor(context, R.color.red_3)).let {
                setupErrorFieldBackground(it)
            }
            getColor(R.styleable.BaseInputView_hintTextColor, ContextCompat.getColor(context, R.color.gray_5)).let {
                setupHintTextColor(it)
            }
            getColor(R.styleable.BaseInputView_errorMessageTextColor, ContextCompat.getColor(context, R.color.red_1)).let {
                errorMessageTextColor = it
            }
            getColor(R.styleable.BaseInputView_messageTextColor, ContextCompat.getColor(context, R.color.black_5)).let {
                messageTextColor = it
            }
            getResourceId(R.styleable.BaseInputView_fieldBackground, ContextCompat.getColor(context, R.color.gray_5)).let {
                setupFieldBackground(it)
            }
            getColor(R.styleable.BaseInputView_endIconTint, ContextCompat.getColor(context, R.color.gray_1)).let {
                vb.ivEndIcon.setColorFilter(it)
            }
            getDrawable(R.styleable.BaseInputView_endIconDrawable)?.let {
                setInputRightDrawable(it)
            }
            getString(R.styleable.BaseInputView_android_hint)?.let { hint ->
                setHint(hint)
            }

            getBoolean(R.styleable.BaseInputView_android_enabled, true).let {enabled ->
                setIsInputEnabled(enabled)
            }

            getBoolean(R.styleable.BaseInputView_android_textAllCaps, false).let {isCaps ->
                setIsTextAllCaps(isCaps)
            }

            getString(R.styleable.BaseInputView_message)?.let { message ->
                setMessage(message)
            }

            getString(R.styleable.BaseInputView_errorMessage)?.let { error ->
                setupFieldAsError(error)
            }

            getInteger(R.styleable.BaseInputView_android_gravity, Gravity.CENTER).let { gravity ->
                setGravity(gravity)
            }
            getString(R.styleable.BaseInputView_actionButtonText)?.let {
                setAction(it)
            }
            getInteger(R.styleable.BaseInputView_android_inputType, InputType.TYPE_CLASS_TEXT).let {
                setInputType(it)
            }
            getInteger(R.styleable.BaseInputView_endIconMode, TextInputLayout.END_ICON_CUSTOM).let {
                setupEndDrawableMode(it)
            }
            getResourceId(R.styleable.BaseInputView_android_textAppearance, -1).takeIf { it != -1 }?.let {
                setupInputTextAppearance(it)
            }
            getColorStateList(R.styleable.BaseInputView_android_editTextColor)?.let {
                vb.etInput.setTextColor(it)
            }
            getInteger(R.styleable.BaseInputView_android_maxLength, -1)
                .takeIf { it != -1 }?.let {
                    setMaxLength(it)
                }
            recycle()
        }
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        var superState: Parcelable? = null
        (state as? Bundle)?.let { bundle ->
            setText(bundle.getString(EditText::class.java.canonicalName))
            superState = bundle.getParcelable(SUPER_STATE)
        }
        super.onRestoreInstanceState(superState)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val supperState = super.onSaveInstanceState()
        return Bundle().apply {
            putString(EditText::class.java.canonicalName, getInputText())
            putParcelable(SUPER_STATE, supperState)
        }
    }

    protected open fun setupHintTextColor(@ColorInt color: Int) {
        hintTextColor = color
        vb.etInput.setHintTextColor(color)
    }

    private fun setupErrorFieldBackground(resId: Int) {
        fieldErrorBackground = resId
        vb.tilInputContainer.setBackgroundResource(resId)
    }

    private fun setupFieldBackground(resId: Int) {
        fieldBackground = resId
        vb.tilInputContainer.setBackgroundResource(resId)
    }

    fun getInputField(): SelectionEditText {
        return vb.etInput
    }

    fun setText(text: String?) {
        vb.etInput.setText(text)
    }

    fun appendString(text: String) {
        vb.etInput.append(text)
    }

    fun setSelectionStartLimit(limit: Int) {
        vb.etInput.startSelectionLimit = limit
    }

    fun setSelection(selection: Int) {
        vb.etInput.setSelection(selection)
    }

    fun setSelectionToEnd() {
        vb.etInput.text?.length?.let {
            setSelection(it)
        }
    }

    open fun getInputText() = vb.etInput.text?.toString() ?: ""

    open fun isInputEmpty() = getInputText().isEmpty()

    open fun clearInput() = setText("")

    fun setHint(hint: String?) {
        vb.etInput.hint = hint
    }

    fun setInputType(type: Int) {
        vb.etInput.inputType = type
    }

    fun setIsTextAllCaps(isCaps: Boolean) {
        vb.etInput.isAllCaps =isCaps
        vb.etInput.inputType = InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS
        vb.etInput.transformationMethod = null
    }

    fun showSystemKeyboard() {
        requestInputFocus()
        showKeyboard()
    }

    fun requestInputFocus() {
        vb.etInput.requestFocus()
    }

    private fun showKeyboard() {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(vb.etInput, InputMethodManager.SHOW_IMPLICIT)
    }

    fun clearAndSetFilters(filter: Array<InputFilter>) {
        vb.etInput.filters = filter
    }

    fun addFilter(inputFilter: InputFilter) {
        vb.etInput.filters += inputFilter
    }

    fun addRegexFilter(regex: Regex) {
        addFilter { source, start, end, _, _, _ ->
            var keepOriginal = true
            val sb: StringBuilder = StringBuilder(end - start)
            for (i in start until end) {
                if (regex.matches(source[i].toString()))
                    sb.append(source[i].toString())
                else
                    keepOriginal = false
            }
            if (keepOriginal) null else {
                if (source is Spanned) {
                    val sp = SpannableString(sb)
                    TextUtils.copySpansFrom(source, start, sb.length, null, sp, 0)
                    sp
                } else {
                    sb
                }
            }
        }
    }

    fun setMaxLength(charCount: Int) {
        addFilter(InputFilter.LengthFilter(charCount))
    }

    fun setOnLongClick(action: () -> Unit) {
        vb.etInput.setOnLongClickListener { action(); true }
    }

    fun setOnDoubleClick(action: () -> Unit) {
        vb.etInput.setOnDoubleClickListener { action() }
    }

    fun disableSuggestions() {
        vb.etInput.apply { inputType = inputType or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS }
    }

    fun disableEdition() {
        vb.etInput.isEnabled = false
    }

    fun isInputEnabled() = vb.etInput.isEnabled

    fun setIsInputEnabled(isEnabled: Boolean) {
        vb.etInput.isEnabled = isEnabled
    }

    fun disableSystemKeyboard() {
        vb.etInput.showSoftInputOnFocus = false
    }

    fun setInputLetterSpacing(spacing: Float) {
        vb.etInput.letterSpacing = spacing
    }

    fun changeInputPositionToStart() {
        setGravity(Gravity.LEFT)
    }

    fun changeInputPositionToCenter() {
        setGravity(Gravity.CENTER)
    }

    fun setGravity(gravity: Int) {
        vb.etInput.gravity = gravity
    }

    fun setEms(n: Int) {
        vb.etInput.setEms(n)
    }

    fun setFocusChangeListener(onFocus: () -> Unit = {}, onFocusLost: () -> Unit = {}) {
        vb.etInput.setOnFocusChangeListener { _, hasFocus ->
            when {
                hasFocus -> onFocus()
                else -> onFocusLost()
            }
        }
    }

    fun setupForCyrillicInput() {
        addRegexFilter(cyrillicRegex)
    }

    fun setDigitKeyListener(symbols: String) {
        vb.etInput.keyListener = DigitsKeyListener.getInstance(symbols)
    }

    fun addTextWatcher(
        beforeTextChanged: ((s: CharSequence?, start: Int, count: Int, after: Int) -> Unit)? = null,
        onTextChanged: ((s: CharSequence?, start: Int, before: Int, count: Int) -> Unit)? = null,
        afterTextChanged: ((s: Editable?) -> Unit)? = null,
        onTextChangedListener: ((String?) -> Unit)? = null
    ) {
        val textWatcher = SimpleTextWatcher(beforeTextChanged, onTextChanged, afterTextChanged, onTextChangedListener)
        vb.etInput.addTextChangedListener(textWatcher)
        textWatchers.add(textWatcher)
    }

    fun addTextWatcher(textWatcher: TextWatcher) {
        vb.etInput.addTextChangedListener(textWatcher)
        textWatchers.add(textWatcher)
    }

    fun removeAllTextWatcher() {
        textWatchers.forEach {
            vb.etInput.removeTextChangedListener(it)
        }
        textWatchers.clear()
    }

    fun getAllTextWatchers(): List<TextWatcher> {
        return textWatchers
    }

    fun setSimpleTextChangedListener(onTextChanged: (String?) -> Unit) {
        val textWatcher = SimpleTextWatcher(onTextChangedListener = onTextChanged)
        vb.etInput.addTextChangedListener(textWatcher)
    }

    fun doAfterTextChanged(action: (Editable?) -> Unit) {
        val textWatcher = SimpleTextWatcher(afterTextChanged = action)
        vb.etInput.addTextChangedListener(textWatcher)
    }

    fun setTextChangeListener(onTextChanged: ((CharSequence?, Int, Int, Int) -> Unit)? = null) {
        val textWatcher = SimpleTextWatcher(
            onTextChanged = onTextChanged,
            beforeTextChanged = { _, _, _, _ -> clearFieldError() })
        vb.etInput.addTextChangedListener(textWatcher)
    }


    fun setInputTailedIcon(@DrawableRes drawableId: Int) {
        vb.etInput.run {
            setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, drawableId, 0)
            val frameLayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            if (vb.etInput.gravity in listOf(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL)) {
                frameLayoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            }
            this.layoutParams = frameLayoutParams
            minEms = 1
        }
    }

    fun setInputTailedIcon(drawable: Drawable) {
        vb.etInput.run {
            setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null)
            val frameLayoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            )
            if (vb.etInput.gravity in listOf(Gravity.CENTER, Gravity.CENTER_HORIZONTAL, Gravity.CENTER_VERTICAL)) {
                frameLayoutParams.gravity = Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL
            }
            this.layoutParams = frameLayoutParams
            minEms = 1
        }
    }

    fun matchInputWidthToParentWidth() {
        vb.etInput.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
    }

    fun setInputRightDrawable(drawable: Drawable) {
        getInputRightImageView().apply {
            visible()
            setImageDrawable(drawable)
        }
        vb.etInput.updatePadding(right = resources.getDimensionPixelSize(R.dimen.padding_32dp))
    }

    fun setInputRightDrawable(@DrawableRes drawableId: Int) {
        getInputRightImageView().apply {
            visible()
            setImageResource(drawableId)
        }
        vb.etInput.updatePadding(right = resources.getDimensionPixelSize(R.dimen.padding_32dp))
    }

    fun isInputRightDrawableExist(): Boolean {
        return getInputRightImageView().drawable != null
    }

    fun removeInputRightDrawable() {
        getInputRightImageView().gone()
        vb.etInput.updatePadding(right = resources.getDimensionPixelSize(R.dimen.padding_0dp))
    }

    fun getInputRightImageView(): ImageView {
        return vb.ivEndIcon
    }

    fun setupInputTextAppearance(@StyleRes inputViewTextAppearanceRes: Int) {
        this.inputViewTextAppearanceRes = inputViewTextAppearanceRes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            vb.etInput.setTextAppearance(inputViewTextAppearanceRes)
        } else {
            vb.etInput.setTextAppearance(context, inputViewTextAppearanceRes)
        }
    }

    private fun setupEndDrawableMode(mode: Int) {
       when (mode) {
           TextInputLayout.END_ICON_CLEAR_TEXT -> setupClearTextButton()
           TextInputLayout.END_ICON_PASSWORD_TOGGLE -> setupAsPasswordField()
           TextInputLayout.END_ICON_NONE -> removeInputRightDrawable()
       }
    }

    fun setupAsPasswordField() {
        removeInputRightDrawable()
        vb.tilInputContainer.apply {
            setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
            endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
            setEndIconDrawable(R.drawable.chili_password_toggle_drawable)
            setupInputTextAppearance(inputViewTextAppearanceRes)
        }
    }

    fun setAction(title: String, action: () -> Unit = {}) {
        vb.flActionBg.visible()
        vb.tvAction.apply {
            text = title
            setOnSingleClickListener(action)
            visible()
        }
    }

    fun setActionEnabled(isEnabled: Boolean) {
        vb.tvAction.isEnabled = isEnabled
    }

    fun setSingleLine(singleLine: Boolean){
        vb.etInput.isSingleLine = singleLine
    }

    fun setOnActionClickListener(action: () -> Unit = {}) {
        vb.tvAction.setOnClickListener { action.invoke() }
    }

    fun hideAction() {
        vb.apply {
            tvAction.gone()
            flActionBg.gone()
        }
    }

    fun setMessage(@StringRes resId: Int) {
        setMessage(resources.getString(resId))
    }

    fun setMessage(text: String?) {
        messageText = text
        when (text.isNullOrBlank()) {
            true -> vb.tvMessage.gone()
            else -> {
                vb.tvMessage.apply {
                    setText(messageText)
                    setTextColor(messageTextColor)
                    visible()
                    if (isLabelBehaviorEnabled && !(getInputField().hasFocus())) gone()
                }
            }
        }
    }

    fun setupFieldAsError(@StringRes errorTextResId: Int) {
        setupFieldAsError(resources.getString(errorTextResId))
    }

    fun setupFieldAsError(errorText: String) {
        vb.tvMessage.apply {
            text = errorText
            setTextColor(errorMessageTextColor)
            isErrorShown = true
            visible()
        }
        vb.tilInputContainer.setBackgroundResource(fieldErrorBackground)
    }

    fun clearFieldError() {
        if (!isErrorShown) return
        vb.tilInputContainer.setBackgroundResource(fieldBackground)
        setMessage(messageText)
        isErrorShown = false
    }

    fun hideMessage() {
        vb.tvMessage.gone()
    }

    fun setupClearTextButton(
        clearText: (() -> Unit)? = null,
        isInputEmpty: (() -> Boolean)? = null,
        isInputEnabled: (() -> Boolean)? = null,
    ) {
        vb.tilInputContainer.apply {
            if (endIconMode == TextInputLayout.END_ICON_PASSWORD_TOGGLE) {
                endIconMode = TextInputLayout.END_ICON_NONE
            }
        }
        if (!isInputRightDrawableExist()) setInputRightDrawable(R.drawable.chili_ic_clear)
        vb.etInput.addTextChangedListener(
            ClearTextIconTextWatcher(
                getInputRightImageView(),
                clearText ?: ::clearInput,
                isInputEmpty ?: ::isInputEmpty,
                isInputEnabled ?: ::isInputEnabled)
        )
    }

    fun setPasteTextListener(onPasteListener: PasteListener) {
        vb.etInput.setPasteListener(onPasteListener)
    }

    fun setActionWithColor(title: String, @ColorInt textColor: Int, action: () -> Unit = {}) {
        setAction(title, action)
        vb.tvAction.setTextColor(textColor)
    }

    fun setupMessageAsLabelBehavior(isEnabled: Boolean) {
        isLabelBehaviorEnabled = isEnabled
        if (!isErrorShown) vb.tvMessage.gone()
        setFocusChangeListener({
            if (!isErrorShown && isLabelBehaviorEnabled) vb.tvMessage.visible()
        }, {
            if (!isErrorShown && isLabelBehaviorEnabled) vb.tvMessage.gone()
        })
    }

    fun setupOnGetFocusAction(action: () -> Unit) {
        vb.clickableMask.apply {
            visible()
            setOnSingleClickListener {
                requestFocus()
                showSystemKeyboard()
                action.invoke()
            }
        }
        setFocusChangeListener({
            vb.clickableMask.gone()
            action.invoke()
        }, {
            vb.clickableMask.visible()
        })
    }


    companion object {
        const val SUPER_STATE = "superState"
    }
}