package com.design2.chili2.view.modals.bottom_sheet

import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.design2.chili2.R
import com.design2.chili2.view.modals.base.BaseFragmentBottomSheetDialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior

class FragmentBottomSheet : BaseFragmentBottomSheetDialogFragment() {

    private var contentFragment: Fragment? = null

    override var topDrawableVisible: Boolean = true
    override var innerTopDrawableVisible: Boolean = false
    override var hasCloseIcon: Boolean = true
    override var isHideable: Boolean = false
    override var isBackButtonEnabled: Boolean = false
    override var state: Int = BottomSheetBehavior.STATE_HALF_EXPANDED

    override var horizontalMargin: Int = 0
    override var bottomMargin: Int = 0

    override var skipCollapsed: Boolean = true
    override var isFitToContents: Boolean? = null
    override var halfExpandedRatio: Float? = null
    override var peekHeight: Int? = null

    override fun setupBottomSheetBehavior(behavior: BottomSheetBehavior<*>?) {
        behavior?.peekHeight = peekHeight ?: (getWindowHeight() * 30 / 100)
        behavior?.isHideable = isHideable
        behavior?.halfExpandedRatio = halfExpandedRatio ?: 0.3f
        behavior?.state = state
        behavior?.skipCollapsed = skipCollapsed
        isFitToContents?.let { behavior?.isFitToContents = it }
    }

    override fun createFragment(): Fragment {
        return contentFragment ?: Fragment()
    }

    class Builder {

        private var contentFragment: Fragment? = null
        private var topDrawableVisible: Boolean = true
        private var newInnerDrawableVisible: Boolean = false
        private var hasCloseIcon: Boolean = true
        private var isHideable: Boolean = false
        private var isBackButtonEnabled: Boolean = false
        private var horizontalMargin: Int = 0
        private var bottomMargin: Int = 0
        private var state: Int = BottomSheetBehavior.STATE_HALF_EXPANDED
        @DrawableRes private var backgroundDrawable: Int = R.drawable.chili_bg_rounded_bottom_sheet
        @DrawableRes private var closeIconDrawable: Int = R.drawable.chili_ic_clear
        @StyleRes private var bottomSheetStyle: Int = R.style.Chili_BottomSheetStyle

        private var onCloseIconClick: (() -> Boolean)? = null
        private var skipCollapsed: Boolean = true
        private var halfExpandedRatio: Float? = null
        private var peekHeight: Int? = null
        private var isFitToContents: Boolean? = null

        fun setContentFragment(contentFragment: Fragment): Builder {
            this.contentFragment = contentFragment
            return this
        }

        fun setDrawableVisible(topDrawableVisible: Boolean): Builder {
            this.topDrawableVisible = topDrawableVisible
            return this
        }

        fun setInnerTopDrawableVisible(newInnerDrawableVisible: Boolean): Builder {
            this.newInnerDrawableVisible = newInnerDrawableVisible
            return this
        }

        fun setCloseIcon(@DrawableRes drawable: Int): Builder {
            this.closeIconDrawable = drawable
            return this
        }

        fun setBottomSheetStyle(@StyleRes style: Int): Builder {
            this.bottomSheetStyle = style
            return this
        }

        fun setHasCloseIcon(hasCloseIcon: Boolean): Builder {
            this.hasCloseIcon = hasCloseIcon
            return this
        }

        fun setIsHideable(isHideable: Boolean): Builder {
            this.isHideable = isHideable
            return this
        }

        fun setBackgroundDrawable(@DrawableRes backgroundDrawable: Int): Builder {
            this.backgroundDrawable = backgroundDrawable
            return this
        }

        fun setIsBackButtonEnabled(isBackButtonEnabled: Boolean): Builder {
            this.isBackButtonEnabled = isBackButtonEnabled
            return this
        }

        fun setState(state: Int): Builder {
            this.state = state
            return this
        }

        fun setHorizontalMargin(margin: Int): Builder {
            this.horizontalMargin = margin
            return this
        }

        fun setBottomMargin(margin: Int): Builder {
            this.bottomMargin = margin
            return this
        }

        fun setOnCloseIconClickListener(onCloseIconClick: () -> Boolean): Builder {
            this.onCloseIconClick = onCloseIconClick
            return this
        }

        fun setSkipCollapsed(skip: Boolean): Builder {
            this.skipCollapsed = skip
            return this
        }

        fun setHalfExpandedRatio(ratio: Float): Builder {
            this.halfExpandedRatio = ratio
            return this
        }

        fun setPeekHeight(peekHeight: Int): Builder {
            this.peekHeight = peekHeight
            return this
        }

        fun setIsFitToContents(isFitToContents: Boolean): Builder {
            this.isFitToContents = isFitToContents
            return this
        }

        fun build(): FragmentBottomSheet {
            return FragmentBottomSheet().apply {
                contentFragment = this@Builder.contentFragment
                topDrawableVisible = this@Builder.topDrawableVisible
                innerTopDrawableVisible = this@Builder.newInnerDrawableVisible
                hasCloseIcon = this@Builder.hasCloseIcon
                bottomSheetStyle = this@Builder.bottomSheetStyle
                isHideable = this@Builder.isHideable
                isBackButtonEnabled = this@Builder.isBackButtonEnabled
                backgroundDrawable = this@Builder.backgroundDrawable
                closeIconDrawable = this@Builder.closeIconDrawable
                horizontalMargin = this@Builder.horizontalMargin
                bottomMargin = this@Builder.bottomMargin
                state = this@Builder.state
                onCloseIconClick = this@Builder.onCloseIconClick
                skipCollapsed = this@Builder.skipCollapsed
                halfExpandedRatio = this@Builder.halfExpandedRatio
                peekHeight = this@Builder.peekHeight
                isFitToContents = this@Builder.isFitToContents
            }
        }

    }

}