package com.design2.chili2.view.modals.in_app

import android.content.DialogInterface
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.RequestBuilder
import com.design2.chili2.R
import com.design2.chili2.databinding.ChiliViewInAppPushBinding
import com.design2.chili2.extensions.*
import com.design2.chili2.view.modals.base.BaseBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class InAppPushBottomSheet private constructor() : BaseBottomSheetDialogFragment() {

    private lateinit var vb: ChiliViewInAppPushBinding

    override var hasCloseIcon: Boolean = true

    var rootView: View? = null
    override var topDrawableView: View? = null
    override var innerTopDrawableView: View? = null
    override var closeIconView: View? = null

    private var bannerUrl: String? = null
    private var onBannerClick: (InAppPushBottomSheet.() -> Unit)? = null
    private var title: String? = null
    private var description: String? = null

    private var onDismissCallback: ((Boolean?) -> Unit)? = null

    private var checkBoxTitle: String? = null
    private var btnPrimary: Pair<String, InAppPushBottomSheet.() -> Unit>? = null

    private var imageListener: RequestBuilder<Drawable>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Chili_InAppPushStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        vb = ChiliViewInAppPushBinding.inflate(inflater, container, false)
        initViewVariables()
        return vb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBanner()
        setupButton()
        setupTitle()
        setupDescription()
        setupCheckBoxTitle()
    }

    override fun setupBottomSheetBehavior(behavior: BottomSheetBehavior<*>?) {
        behavior?.run {
            isHideable = false
            peekHeight = getWindowHeight()
            state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    private fun initViewVariables() {
        closeIconView = vb.btnClose
        rootView = vb.flRootView
    }

    private fun setupTitle() {
        vb.tvTitle.setTextOrHide(title)
    }

    private fun setupDescription() {
        vb.tvDescription.setTextOrHide(description)
    }

    private fun setupCheckBoxTitle() {
        with(vb) {
            checkBoxContainer.isVisible = !checkBoxTitle.isNullOrEmpty()
            chbShow.isVisible = !checkBoxTitle.isNullOrEmpty()
            chbTitle.text = checkBoxTitle
        }
    }

    fun isCheckBoxVisibleAndChecked(): Boolean? =
        if (vb.chbShow.isVisible()) vb.chbShow.isChecked else null

    private fun setupButton() {
        vb.btnMore.apply {
            when (btnPrimary == null) {
                true -> gone()
                else -> {
                    visible()
                    text = btnPrimary?.first
                    setOnSingleClickListener { btnPrimary?.second?.invoke(this@InAppPushBottomSheet) }
                }
            }
        }
    }

    private fun setupBanner() {
        vb.ivBanner.let { bannerView ->
            imageListener = bannerView.setImageByUrlWithListener(
                imageUrl = bannerUrl,
                onSuccess = { resource ->
                    bannerView.visible()
                    bannerView.setImageDrawable(resource)
                },
                onError = { bannerView.gone() }
            )
        }
        vb.ivBanner.setOnSingleClickListener { onBannerClick?.invoke(this) }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke(isCheckBoxVisibleAndChecked())
    }

    class Builder {
        private var bannerUrl: String? = null
        private var onBannerClick: (InAppPushBottomSheet.() -> Unit)? = null
        private var title: String? = null
        private var description: String? = null
        private var checkBoxTitle: String? = null
        private var btnPrimary: Pair<String, InAppPushBottomSheet.() -> Unit>? = null
        private var isHideable: Boolean = false
        private var onDismissCallback: ((Boolean?) -> Unit)? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setOnBannerClick(onBannerClick: (InAppPushBottomSheet.() -> Unit)?): Builder {
            this.onBannerClick = onBannerClick
            return this
        }

        fun setBannerUrl(bannerUrl: String): Builder {
            this.bannerUrl = bannerUrl
            return this
        }

        fun setDescription(description: String): Builder {
            this.description = description
            return this
        }

        fun setCheckBoxTitle(checkBoxTitle: String): Builder {
            this.checkBoxTitle = checkBoxTitle
            return this
        }

        fun setBtnMoreInfo(btnMoreInfo: Pair<String, InAppPushBottomSheet.() -> Unit>?): Builder {
            this.btnPrimary = btnMoreInfo
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun setOnDismissCallback(callback: (Boolean?) -> Unit): Builder {
            this.onDismissCallback = callback
            return this
        }

        fun build(): InAppPushBottomSheet {
            return InAppPushBottomSheet().apply {
                bannerUrl = this@Builder.bannerUrl
                onBannerClick = this@Builder.onBannerClick
                title = this@Builder.title
                description = this@Builder.description
                checkBoxTitle = this@Builder.checkBoxTitle
                btnPrimary = this@Builder.btnPrimary
                isHideable = this@Builder.isHideable
                onDismissCallback = this@Builder.onDismissCallback
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageListener?.listener(null)
    }
}