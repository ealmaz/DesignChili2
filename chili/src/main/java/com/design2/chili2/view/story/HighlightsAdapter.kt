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
    private val stories = ArrayList<Story>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighlightsVH {
        val inflater = LayoutInflater.from(parent.context)
        val layout = when (viewType) {
            ALL_STORIES -> R.layout.chili_item_highlight_all_stories
            ALGA -> R.layout.chili_item_story_highlight_alga
            FOR_YOU -> R.layout.chili_item_story_highlight_for_you
            else -> R.layout.chili_item_story_highlight
        }
        val view: View = inflater.inflate(layout, parent, false)
        return HighlightsVH(view)
    }

    override fun getItemViewType(position: Int): Int {
        return when(stories[position].type) {
            StoryType.ALL_STORIES -> ALL_STORIES
            StoryType.ALGA -> ALGA
            StoryType.FOR_YOU -> FOR_YOU
            else -> GENERAL
        }
    }

    override fun onBindViewHolder(holder: HighlightsVH, position: Int) {
        holder.bind(stories[position])
    }

    override fun getItemCount(): Int {
        return stories.size
    }

    fun addItems(items: List<Story>) {
        this.stories.addAll(items)
        this.stories.add(Story(8, "Все события", true, StoryType.ALL_STORIES, null))
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
            loadImage(item.imageUrl)
            textView.text = item.title
            if (item.isRead == true) borderContainer.setHighlighterState(HighlighterState.GONE)
        }

        private fun setupClickListener() {
            borderContainer.setOnClickListener {
                if (adapterPosition == 7) listener.onAllStoriesViewClick()
                else listener.onHighlightClick(adapterPosition)
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
        const val ALL_STORIES = 1
        const val ALGA = 2
        const val FOR_YOU = 3
        const val GENERAL = 0
    }
}

interface OnHighlightClickListener {
    fun onHighlightClick(index: Int)
    fun onAllStoriesViewClick()
}