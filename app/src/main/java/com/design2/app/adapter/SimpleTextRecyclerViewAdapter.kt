package com.design2.app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.databinding.ItemSimpleTextBinding

class SimpleTextRecyclerViewAdapter(private val context: Context) :
    RecyclerView.Adapter<SimpleTextRecyclerViewAdapter.ViewHolder>() {

    private var data: List<String> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vb = ItemSimpleTextBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(vb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item
    }

    override fun getItemCount(): Int {
        return data.size
    }

    inner class ViewHolder(holderVb: ItemSimpleTextBinding) : RecyclerView.ViewHolder(holderVb.root) {
        val textView: TextView = holderVb.tvItem
    }

    // Method to update the dataset and refresh the adapter
    fun updateList(newData: List<String>) {
        data = newData
        notifyDataSetChanged()
    }
}
