package com.design2.app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.databinding.ItemEditableCellBinding

class EditableCellViewsAdapter(var dragStart: ((RecyclerView.ViewHolder) -> Unit)?) :
    RecyclerView.Adapter<EditableCellViewsAdapter.ViewHolder>() {

    var isEditMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val vb = ItemEditableCellBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(vb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = holder.vb.editableCellView
        cell.isEditMode = this.isEditMode
        cell.setItemDragListener(
            started = {
                dragStart?.invoke(holder)
            }, end = {
                notifyDataSetChanged()
            })
        cell.setOnOptionClickListener {
            holder.itemView.context.let {
                Toast.makeText(it, "Option Clicked", Toast.LENGTH_SHORT).show()
            }
        }
        cell.setupRoundedModeByPosition(position == 0, position == itemCount - 1)
    }

    override fun getItemCount(): Int {
        return 4
    }

    fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    class ViewHolder(val vb: ItemEditableCellBinding) : RecyclerView.ViewHolder(vb.root) {}
}
