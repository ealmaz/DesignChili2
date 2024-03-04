package com.design2.chili2.view.story

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.design2.chili2.R
import com.design2.chili2.util.HighlighterState
import com.design2.chili2.view.container.HighlighterContainer

class HighlightsAdapter(private val listener: OnHighlightClickListener)
    : RecyclerView.Adapter<HighlightsAdapter.HighlightsVH>() {
    private var stories = mutableListOf<Story>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsVH {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when (viewType) {
            ALL_STORIES -> R.layout.chili_item_highlight_all_stories
            MARKETING_CENTER -> R.layout.chili_item_story_highlight_for_you
            else -> R.layout.chili_item_story_highlight
        }
        val view: View = inflater.inflate(layout, parent, false)
        return HighlightsVH(view)
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 7) return ALL_STORIES
        if (stories[position].isMarketingCenter) return MARKETING_CENTER
        return GENERAL
    }

    override fun onBindViewHolder(holder: HighlightsVH, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    fun addItems(items: List<Story>) {
        stories.addAll(items.sortedBy { it.orderNumber })
        stories.add(Story(-1, -1, "Все события", false))
        notifyDataSetChanged()
    }

    inner class HighlightsVH(view: View) : RecyclerView.ViewHolder(view) {
        private lateinit var imageView: ImageView
        private lateinit var textView: TextView
        private lateinit var borderContainer: HighlighterContainer

        fun bind(item: Story) {
            imageView = itemView.findViewById(R.id.image)
            textView = itemView.findViewById(R.id.text)
            borderContainer = itemView.findViewById(R.id.border_container)
            setupStory(item)
        }

        private fun setupStory(item: Story) {
            setupClickListener()
            loadImage(item.image)
            textView.text = item.title
            if (item.isRead()) borderContainer.setHighlighterState(HighlighterState.GONE)
            else borderContainer.setHighlighterGradientColors(item.borderColors)
        }

        private fun setupClickListener() {
            borderContainer.setOnClickListener {
                if (adapterPosition == 7) listener.onAllStoriesViewClick()
                else listener.onHighlightClick(itemView, adapterPosition)
            }
        }

        private fun loadImage(imageUri: String?) {
            if (imageUri != null) {
                Glide.with(itemView)
                    .load(imageUri)
                    .centerCrop()
                    .into(imageView)
            }
        }
    }

    companion object {
        const val ALL_STORIES = -1
        const val GENERAL = 1
        const val MARKETING_CENTER = 2
    }
}

interface OnHighlightClickListener {
    fun onHighlightClick(clickedView: View, index: Int)
    fun onAllStoriesViewClick()
}