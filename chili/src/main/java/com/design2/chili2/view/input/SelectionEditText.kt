package com.design2.chili2.view.input

import android.content.Context
import android.util.AttributeSet
import androidx.core.view.ViewCompat

class SelectionEditText(context: Context, attributeSet: AttributeSet) : androidx.appcompat.widget.AppCompatEditText(context, attributeSet) {

    private var pasteListener: PasteListener? = null
    private var selectionChangedListener: SelectionChangedListener? = null

    var startSelectionLimit = 0
    var endSelectionLimit = -1

    init {
        setupViews()
    }

    override fun onSelectionChanged(selStart: Int, selEnd: Int) {
        if (selectionChangedListener?.onSelectionChanged(selStart, selEnd) == true) return
        when {
            selStart == selEnd && selStart < startSelectionLimit -> {
                setSelection(startSelectionLimit)
            }
            selStart == selEnd && endSelectionLimit < length() && endSelectionLimit > -1 && selEnd > endSelectionLimit -> {
                setSelection(endSelectionLimit)
            }
            else -> super.onSelectionChanged(selStart, selEnd)
        }
    }

    override fun setSelection(index: Int) {
        super.setSelection(index.takeIf { it <= length() && it >= 0} ?: length())
    }

    private fun setupViews() {
        ViewCompat.setOnReceiveContentListener(this, arrayOf("text/plain")) { _, payload, ->
            if (pasteListener == null) return@setOnReceiveContentListener payload
            val pasteText = payload.clip.getItemAt(0).text.toString()
            val resultText = pasteListener?.onPasteText(text?.toString() ?: "", pasteText, selectionStart)
                ?: return@setOnReceiveContentListener payload
            setText(resultText)
            setSelection(selectionStart + resultText.length)
            null
        }
    }

    fun setPasteListener(pasteListener: PasteListener) {
        this.pasteListener = pasteListener
    }

    fun moveSelectionRight() {
        setSelection(selectionStart + 1)
    }

    fun moveSelectionToEnd() {
        setSelection(length())
    }

    fun moveSelectionToStart() {
        setSelection(0)
    }

    fun setSelectionChangedListener(listener: SelectionChangedListener) {
        this.selectionChangedListener = listener
    }
}

interface PasteListener {
    fun onPasteText(fieldText: String, newText: String, selectionPosition: Int): String?
}

interface SelectionChangedListener {
    fun onSelectionChanged(selStart: Int, selEnd: Int): Boolean
}