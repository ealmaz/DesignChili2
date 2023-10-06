package com.design2.app.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.design2.app.R
import com.design2.chili2.view.cells.EditableCellView

class EditableCellViewsAdapter(var dragStart: ((RecyclerView.ViewHolder) -> Unit)?) :
    RecyclerView.Adapter<EditableCellViewsAdapter.ViewHolder>() {

    var isEditMode: Boolean = false
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_editable_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cell = holder.itemView.findViewById<EditableCellView>(R.id.editable_cell_view)
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


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}
