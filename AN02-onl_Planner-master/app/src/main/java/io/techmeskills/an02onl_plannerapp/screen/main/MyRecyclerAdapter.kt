package io.techmeskills.an02onl_plannerapp.screen.main

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.graphics.drawable.shapes.Shape
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ListAdapter
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.models.Note
import io.techmeskills.an02onl_plannerapp.R.drawable.delete_background

class MyRecyclerAdapter(
    private val onClick: (Note) -> Unit,
    private val onDelete: (Note) -> Unit,
    private val onAddNew: () -> Unit
) : ListAdapter<Note, RecyclerView.ViewHolder>(NoteAdapterDiffCallback()) {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) : RecyclerView.ViewHolder = when (viewType) {
        ADD_NEW_VIEW_TYPE -> AddNewViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item_add, parent, false),
            onAddNew)
        else -> NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false),
            ::onCardClick,
            ::swipeDelete)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is AddNewNote -> ADD_NEW_VIEW_TYPE
            else -> NOTE_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is NoteViewHolder -> holder.bind(getItem(position))
            else -> (holder as AddNewViewHolder).bind()
        }
    }


    private fun onCardClick(position: Int) {
        onClick(getItem(position))
    }

    fun swipeDelete(position: Int) {
        onDelete(getItem(position))
    }


    inner class NoteViewHolder(itemView: View, private val onCardClick: (Int) -> Unit, private val onCardDelete: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        private val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)

        init {
            itemView.setOnClickListener{
                onCardClick(adapterPosition)
            }
        }

        fun bind(item: Note) {
            largeTextView.text = item.title
            smallTextView.text = item.date
        }
    }

    inner class AddNewViewHolder(
        itemView: View,
        private val onItemClick: () -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                onItemClick()
            }
        }

        fun bind() = Unit
    }

    companion object {
        const val ADD_NEW_VIEW_TYPE = 322
        const val NOTE_VIEW_TYPE = 111
    }
}

class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.date == newItem.date && oldItem.title == newItem.title
    }
}

open class SwipeToDeleteCallback(private val background: Drawable?) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

        override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        }

        override fun onChildDraw(
                c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
        ) {

            val itemView = viewHolder.itemView
            background?.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background?.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
}
