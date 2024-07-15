package kg.devcats.chili3.view.modals.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

import com.design2.chili2.extensions.setBottomMargin
import com.design2.chili2.extensions.setHorizontalMargin
import com.design2.chili2.view.modals.base.BaseBottomSheetDialogFragment
import kg.devcats.chili3.R
import kg.devcats.chili3.databinding.Chili3ViewBottomSheetBaseFragmentBinding

abstract class BaseFragmentBottomSheetDialogFragment : BaseBottomSheetDialogFragment() {

    private var titleResId: Int = -1
    private var title:String = ""
    private lateinit var vb: Chili3ViewBottomSheetBaseFragmentBinding

    override var topDrawableView: View? = null
    override var closeIconView: View? = null

    protected open var horizontalMargin: Int = 0
    protected open var bottomMargin: Int = 0

    @DrawableRes
    protected open var backgroundDrawable: Int = R.drawable.chili3_bg_rounded_bottom_sheet

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?,
    ): View? {
        vb = Chili3ViewBottomSheetBaseFragmentBinding.inflate(inflater, container, false)
        initViewVariables()
        val fragment = createFragment()
        fragment.arguments = Bundle().apply {
            putString("title",title)
        }
        childFragmentManager.beginTransaction()
            .replace(R.id.bottom_sheet_container, fragment)
            .commit()
        vb.llContent.setHorizontalMargin(horizontalMargin)
        vb.llContent.setBottomMargin(bottomMargin)
        if (title.isNotEmpty()) {
            setTitle(title)
        }
        if (titleResId != -1){
            setTitle(titleResId)
        }
        return vb.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.Chili3_BottomSheetStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        vb.llContent.setBackgroundResource(backgroundDrawable)
    }

    private fun initViewVariables() {
        topDrawableView = vb.ivTopDrawable
        closeIconView = vb.ivClose
    }

    private fun setTitle(@StringRes id:Int){
        vb.tvTitle.setText(id)
    }

    private fun setTitle(title:String){
        vb.tvTitle.text = title
    }

    fun setTitleValue(value:String){
        title = value
    }

    fun setTitleResId(@StringRes id:Int){
        titleResId = id
    }

    abstract fun createFragment(): Fragment
}