package com.design2.chili2.view.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewButtonLoaderBinding
import com.design2.chili2.extensions.gone
import com.design2.chili2.extensions.invisible
import com.design2.chili2.extensions.visible

@Deprecated("Use ChiliButton with isLoading state instead")
class LoaderButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.loaderButtonDefaultStyle,
    defStyle: Int = R.style.Chili_LoaderButton
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle) {

    private lateinit var vb: ChiliViewButtonLoaderBinding

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
    }

    private fun initView(context: Context) {
        vb = ChiliViewButtonLoaderBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.LoaderButton, defStyleAttr, defStyle).run {
            setText(getString(R.styleable.LoaderButton_android_text))
            setEnabled(getBoolean(R.styleable.LoaderButton_android_enabled, true))
            setIsLoading(getBoolean(R.styleable.LoaderButton_isLoading, false))
            getResourceId(R.styleable.LoaderButton_textAppearance, -1).takeIf { it != -1 }?.let {
                setTextAppearance(it)
            }
            recycle()
        }
    }

    fun setText(text: String?) {
        vb.button.text = text
    }

    fun setText(@StringRes textResId: Int) {
        vb.button.setText(textResId)
    }

    fun setIsLoading(isLoading: Boolean) {
        when (isLoading) {
            true -> showLoader()
            else -> hideLoader()
        }
    }

    fun setTextAppearance(resId: Int) {
        vb.button.setTextAppearance(resId)
    }

    private fun showLoader() = with(vb) {
        button.invisible()
        progress.visible()
    }

    private fun hideLoader() = with (vb) {
        button.visible()
        progress.gone()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        vb.button.setOnClickListener(l)
    }

    override fun isEnabled(): Boolean {
        return vb.button.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        vb.button.isEnabled = enabled
    }
}