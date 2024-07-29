package kg.devcats.chili3.view.container

import android.os.Bundle
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.RecyclerView
import com.design2.chili2.view.modals.base.BaseBottomSheetDialogFragment
import kg.devcats.chili3.databinding.ChiliBottomSheetWithRecyclerBinding
import kg.devcats.chili3.extensions.visible

class BottomSheetWithRecycler private constructor() : BaseBottomSheetDialogFragment() {

    private lateinit var vb: ChiliBottomSheetWithRecyclerBinding

    override var topDrawableView: View? = null
    override var innerTopDrawableView: View? = null
    override var closeIconView: View? = null

    var adapter: RecyclerView.Adapter<*>? = null

    private var title: String? = null
    private var titleSpanned: Spanned? = null
    @DrawableRes
    private var titleResId: Int? = null
    private var subtitle: String? = null
    private var subtitleSpanned: Spanned? = null
    @DrawableRes
    private var subtitleResId: Int? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        vb = ChiliBottomSheetWithRecyclerBinding.inflate(inflater, container, false)
        return vb.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        adapter?.let {
            vb.recyclerView.adapter = it
        }
        title?.let { setTitle(it) }
        titleSpanned?.let { setTitle(it) }
        titleResId?.let { setTitle(it) }
        subtitle?.let { setSubtitle(it) }
        subtitleSpanned?.let { setSubtitle(it) }
        subtitleResId?.let { setSubtitle(it) }
    }

    private fun setTitle(@StringRes resId: Int) {
        vb.tvTitle.apply {
            visible()
            setText(resId)
        }
    }

    private fun setTitle(message: String) {
        vb.tvTitle.apply {
            visible()
            text = message
        }
    }

    private fun setTitle(spanned: Spanned?) {
        vb.tvTitle.apply {
            text = spanned
            visible()
        }
    }

    private fun setSubtitle(@StringRes resId: Int) {
        vb.tvSubtitle.apply {
            visible()
            setText(resId)
        }
    }

    private fun setSubtitle(message: String) {
        vb.tvSubtitle.apply {
            visible()
            text = message
        }
    }

    private fun setSubtitle(spanned: Spanned?) {
        vb.tvSubtitle.apply {
            text = spanned
            visible()
        }
    }

    class Builder {

        var adapter: RecyclerView.Adapter<*>? = null
        private var title: String? = null
        private var titleSpanned: Spanned? = null
        @DrawableRes
        private var titleResId: Int? = null

        private var subtitle: String? = null
        private var subtitleSpanned: Spanned? = null
        @DrawableRes
        private var subtitleResId: Int? = null

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }

        fun setTitle(textSpanned: Spanned): Builder {
            this.titleSpanned = textSpanned
            return this
        }

        fun setTitle(@DrawableRes resId: Int): Builder {
            this.titleResId = resId
            return this
        }

        fun setSubtitle(subtitle: String): Builder {
            this.subtitle = subtitle
            return this
        }

        fun setSubtitle(subtitle: Spanned): Builder {
            this.subtitleSpanned = subtitle
            return this
        }

        fun setSubtitle(@DrawableRes resId: Int): Builder {
            this.subtitleResId = resId
            return this
        }

        fun setAdapter(adapter: RecyclerView.Adapter<*>): Builder {
            this.adapter = adapter
            return this
        }

        fun build(): BottomSheetWithRecycler {
            return BottomSheetWithRecycler().apply {
                this.adapter = this@Builder.adapter
                this.title = this@Builder.title
                this.titleSpanned = this@Builder.titleSpanned
                this.titleResId = this@Builder.titleResId
                this.subtitle = this@Builder.subtitle
                this.subtitleSpanned = this@Builder.subtitleSpanned
                this.subtitleResId = this@Builder.subtitleResId
                this.adapter = this@Builder.adapter
            }
        }
    }
}