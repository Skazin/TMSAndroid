package io.techmeskills.an02onl_plannerapp.screen.main

import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.graphics.ColorUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R
import io.techmeskills.an02onl_plannerapp.R.drawable.*
import io.techmeskills.an02onl_plannerapp.models.Note
import java.text.SimpleDateFormat
import java.util.*

class MyRecyclerAdapter(
    private val onClick: (Note) -> Unit,
    private val onDelete: (Note) -> Unit,
    private val onPin: (Note) -> Unit
) : ListAdapter<Note, MyRecyclerAdapter.NoteViewHolder>(NoteAdapterDiffCallback()) {



    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ) = NoteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false),
    ::onCardClick,
    ::swipeDelete,
    ::onCardPin)


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
         holder.bind(getItem(position))
    }

    private fun onCardClick(position: Int) {
        onClick(getItem(position))
    }

    private fun onCardPin(position: Int) {
        onPin(getItem(position))
    }

    fun swipeDelete(position: Int) {
        onDelete(getItem(position))
    }


    inner class NoteViewHolder(itemView: View, private val onCardClick: (Int) -> Unit, private val onCardDelete: (Int) -> Unit, private val onCardPin: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
        private val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)
        private val cloudCheck = itemView.findViewById<ImageView>(R.id.cloudCheck)
        private val notification = itemView.findViewById<ImageView>(R.id.notification)
        private val pin = itemView.findViewById<ImageView>(R.id.pinned)


        init {
            itemView.setOnClickListener{
                onCardClick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                onCardPin(adapterPosition)
                return@setOnLongClickListener true
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: Note) {
            itemView.setBackgroundColor(Color.parseColor(item.backgroundColor))
            largeTextView.text = item.title
            largeTextView.setTextColor(getReverseColor(Color.parseColor(item.backgroundColor)))
            smallTextView.text = dateFormatter.format(Date(item.date))
            smallTextView.setTextColor(getReverseColor(Color.parseColor(item.backgroundColor)))
            if(item.notificationOn) {
                notification.setImageResource(ic_notification_on)
            } else notification.setImageResource(ic_notification_off)
            notification.setColorFilter(getReverseColor(Color.parseColor(item.backgroundColor)))
            if(item.fromCloud) {
                cloudCheck.setImageResource(ic_cloud_check)
            } else cloudCheck.setImageResource(ic_not_cloud)
            cloudCheck.setColorFilter(getReverseColor(Color.parseColor(item.backgroundColor)))
            pin.isVisible = item.notePinned
            pin.setColorFilter(getReverseColor(Color.parseColor(item.backgroundColor)))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getReverseColor(colorToInvert: Int) : Int {
        ColorUtils.calculateLuminance(colorToInvert)
        val y: Double = ColorUtils.calculateLuminance(colorToInvert)
        return if (y >= 0.5) Color.BLACK else Color.WHITE
    }

    companion object {
        private val dateFormatter = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
    }
}

class NoteAdapterDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.date == newItem.date
                && oldItem.title == newItem.title
                && oldItem.fromCloud == newItem.fromCloud
                && oldItem.notificationOn == newItem.notificationOn
                && oldItem.notePinned == newItem.notePinned
    }
}

open class SwipeToDeleteCallback(private val background: Drawable?, private val deleteIcon: Drawable?) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val intrinsicWidth = deleteIcon!!.intrinsicWidth
    private val intrinsicHeight = deleteIcon!!.intrinsicHeight

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
            val itemHeight = itemView.bottom - itemView.top
            background?.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
            background?.draw(c)

            // Calculate position of delete icon
            val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
            val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
            val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
            val deleteIconRight = itemView.right - deleteIconMargin
            val deleteIconBottom = deleteIconTop + intrinsicHeight

            // Draw the delete icon
            deleteIcon!!.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
            deleteIcon.draw(c)

            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
}
