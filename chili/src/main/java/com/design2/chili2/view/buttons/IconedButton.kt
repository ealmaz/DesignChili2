package com.design2.chili2.view.buttons

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.design2.chili2.R
import com.design2.chili2.extensions.setImageByUrl
import com.design2.chili2.extensions.visible

@Deprecated("")
class IconedButton @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = R.attr.loaderButtonDefaultStyle,
    defStyle: Int = R.style.Chili_ButtonStyle
) : FrameLayout(context, attributeSet, defStyleAttr, defStyle) {

    private lateinit var view: IconedButtonViewVariables

    init {
        initView(context)
        obtainAttributes(context, attributeSet, defStyleAttr, defStyle)
    }

    private fun initView(context: Context) {
        val view = LayoutInflater.from(context).inflate(R.layout.chili_view_button_iconed, this, true)
        this.view = IconedButtonViewVariables(
            rootView = view.findViewById(R.id.root_view),
            title = view.findViewById(R.id.tv_title),
            icon = view.findViewById(R.id.iv_icon)
        )
    }

    private fun obtainAttributes(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int, defStyle: Int) {
        context.obtainStyledAttributes(attributeSet, R.styleable.IconedButton, defStyleAttr, defStyle).run {
            setText(getString(R.styleable.LoaderButton_android_text))
            setEnabled(getBoolean(R.styleable.LoaderButton_android_enabled, true))
            recycle()
        }
    }

    fun setText(text: String?) {
        view.title.text = text
    }

    fun setText(@StringRes textResId: Int) {
        view.title.setText(textResId)
    }

    fun setIcon(string: String) {
        view.icon.apply {
            visible()
            setImageByUrl(string)
        }
    }

    fun setIcon(@DrawableRes drawableRes: Int) {
        view.icon.apply {
            visible()
            setImageResource(drawableRes)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        view.rootView.setOnClickListener(l)
    }

    override fun isEnabled(): Boolean {
        return view.rootView.isEnabled
    }

    override fun setEnabled(enabled: Boolean) {
        view.rootView.isEnabled = enabled
    }
}

data class IconedButtonViewVariables(
    val rootView: LinearLayout,
    val title: TextView,
    val icon: ImageView
)