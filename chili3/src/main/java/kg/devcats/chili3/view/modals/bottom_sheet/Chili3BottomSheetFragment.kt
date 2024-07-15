package kg.devcats.chili3.view.modals.bottom_sheet

import androidx.annotation.DrawableRes
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kg.devcats.chili3.view.modals.base.BaseFragmentBottomSheetDialogFragment

class Chili3BottomSheetFragment: BaseFragmentBottomSheetDialogFragment() {

    private var contentFragment: Fragment? = null

    override var topDrawableVisible: Boolean = true
    override var hasCloseIcon: Boolean = true
    override var isHideable: Boolean = false
    override var isBackButtonEnabled: Boolean = false
    override var state: Int = BottomSheetBehavior.STATE_HALF_EXPANDED

    override var horizontalMargin: Int = 0
    override var bottomMargin: Int = 0

    override fun setupBottomSheetBehavior(behavior: BottomSheetBehavior<*>?) {
        behavior?.peekHeight = getWindowHeight() * 30 / 100
        behavior?.isHideable = isHideable
        behavior?.halfExpandedRatio = 0.3f
        behavior?.state = state
    }

    override fun createFragment(): Fragment {
        return contentFragment ?: Fragment()
    }

    class Builder {

        private var contentFragment: Fragment? = null
        private var topDrawableVisible: Boolean = true
        private var hasCloseIcon: Boolean = true
        private var isHideable: Boolean = false
        private var isBackButtonEnabled: Boolean = false
        private var horizontalMargin: Int = 0
        private var bottomMargin: Int = 0
        private var state: Int = BottomSheetBehavior.STATE_EXPANDED
        @DrawableRes
        private var backgroundDrawable: Int = kg.devcats.chili3.R.drawable.chili3_bg_rounded_bottom_sheet

        private var onCloseIconClick: (() -> Boolean)? = null


        fun setContentFragment(contentFragment: Fragment): Builder {
            this.contentFragment = contentFragment
            return this
        }

        fun setDrawableVisible(topDrawableVisible: Boolean): Builder {
            this.topDrawableVisible = topDrawableVisible
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

        fun setIsBackButtonEnabled(isBackButtonEnabled: Boolean): Builder {
            this.isBackButtonEnabled = isBackButtonEnabled
            return this
        }

        fun setBackgroundDrawable(@DrawableRes backgroundDrawable: Int): Builder {
            this.backgroundDrawable = backgroundDrawable
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

        fun build(): Chili3BottomSheetFragment {
            return Chili3BottomSheetFragment().apply {
                contentFragment = this@Builder.contentFragment
                topDrawableVisible = this@Builder.topDrawableVisible
                hasCloseIcon = this@Builder.hasCloseIcon
                isHideable = this@Builder.isHideable
                isBackButtonEnabled = this@Builder.isBackButtonEnabled
                backgroundDrawable = this@Builder.backgroundDrawable
                horizontalMargin = this@Builder.horizontalMargin
                bottomMargin = this@Builder.bottomMargin
                state = this@Builder.state
                onCloseIconClick = this@Builder.onCloseIconClick
            }
        }

    }

}