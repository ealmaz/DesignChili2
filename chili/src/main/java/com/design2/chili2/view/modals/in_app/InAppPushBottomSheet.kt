package com.design2.chili2.view.modals.in_app

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private var btnMoreInfo: Pair<String, InAppPushBottomSheet.() -> Unit>? = null

    private var imageListener : RequestBuilder<Drawable>? = null

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

    private fun setupButton() {
        vb.btnMore.apply {
            when (btnMoreInfo == null) {
                true -> gone()
                else -> {
                    visible()
                    text = btnMoreInfo?.first
                    setOnSingleClickListener { btnMoreInfo?.second?.invoke(this@InAppPushBottomSheet) }
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
                    bannerView.setImageDrawable(resource) },
                onError = { bannerView.gone() }
            )
        }
        vb.ivBanner.setOnSingleClickListener { onBannerClick?.invoke(this) }
    }

    class Builder {
        private var bannerUrl: String? = null
        private var onBannerClick: (InAppPushBottomSheet.() -> Unit)? = null
        private var title: String? = null
        private var description: String? = null
        private var btnMoreInfo: Pair<String, InAppPushBottomSheet.() -> Unit>? = null
        private var isHideable: Boolean = false

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

        fun setBtnMoreInfo(btnMoreInfo: Pair<String, InAppPushBottomSheet.() -> Unit>?): Builder {
            this.btnMoreInfo = btnMoreInfo
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun build(): InAppPushBottomSheet {
            return InAppPushBottomSheet().apply {
                bannerUrl = this@Builder.bannerUrl
                onBannerClick = this@Builder.onBannerClick
                title = this@Builder.title
                description = this@Builder.description
                btnMoreInfo = this@Builder.btnMoreInfo
                isHideable = this@Builder.isHideable
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        imageListener?.listener(null)
    }
}