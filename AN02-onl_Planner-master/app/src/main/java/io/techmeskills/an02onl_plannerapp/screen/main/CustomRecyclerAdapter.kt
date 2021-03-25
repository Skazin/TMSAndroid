package io.techmeskills.an02onl_plannerapp.screen.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R

class CustomRecyclerAdapter (private val values: List<Note>) :
    RecyclerView.Adapter<CustomRecyclerAdapter.MyViewHolder>() {

    override fun getItemCount(): Int {
        return values.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recyclerview_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(values[position])
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
       private val largeTextView: TextView = itemView.findViewById(R.id.textViewLarge)
       private val smallTextView: TextView = itemView.findViewById(R.id.textViewSmall)

        fun bind(item: Note) {
            largeTextView.text = item.title
            smallTextView.text = item.group
        }
    }

    }