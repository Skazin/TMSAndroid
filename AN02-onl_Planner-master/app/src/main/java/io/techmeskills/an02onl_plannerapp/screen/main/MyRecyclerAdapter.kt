package io.techmeskills.an02onl_plannerapp.screen.main

import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R

class MyRecyclerAdapter(
        private val onClick: (Note) -> Unit,
        private val onDelete: (Int) -> Unit
) : androidx.recyclerview.widget.ListAdapter<Note, MyRecyclerAdapter.MyViewHolder>(NoteAdapterDiffCallback()) {

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false),
            ::onCardClick, onDelete
    )

    override fun onBindViewHolder(holder: MyRecyclerAdapter.MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    private fun onCardClick(position: Int) {
        onClick(getItem(position))
    }

    fun swipeDelete(position: Int) {
        onDelete(position)
    }


    inner class MyViewHolder(itemView: View, private val onCardClick: (Int) -> Unit, private val onCardDelete: (Int) -> Unit) :
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
}

class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.date == newItem.date && oldItem.title == newItem.title
    }
}

abstract class SwipeToDeleteCallback : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val background = ColorDrawable(Color.RED)

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


            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
}
