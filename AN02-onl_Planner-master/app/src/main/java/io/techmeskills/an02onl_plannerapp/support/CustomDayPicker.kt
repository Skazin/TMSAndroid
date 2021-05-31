package io.techmeskills.an02onl_plannerapp.support

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.techmeskills.an02onl_plannerapp.R
import java.text.SimpleDateFormat
import java.util.*

class CustomDayPicker @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attributeSet, defStyle) {

    private val daysRecyclerView: RecyclerView by lazy { findViewById(R.id.days) }
    private val buttonToday: RecyclerView by lazy { findViewById(R.id.pickToday) }

    var onDayChangeCallback: DateChangeListener? = null

//    var selectedDate: Date?
//    get() {
//        return (daysRecyclerView.adapter as? DaysAdapter)?.selectedDate
//    }
//    set(value) {
//        (daysRecyclerView.adapter as? DaysAdapter)?.selectedDate = value
//    }

    init {
        View.inflate(context, R.layout.custom_calendar_view, this)
        daysRecyclerView.adapter = DaysAdapter(generateDays()) {
            onDayChangeCallback?.onDateChanged(it)
        }

        buttonToday.setOnClickListener {
            daysRecyclerView.scrollToPosition(0)
        }
    }

    private fun generateDays(): List<Date> {
        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val list = arrayListOf<Date>()

        for(i in 0..30) {
            list.add(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    class DaysAdapter(
        private val items: List<Date>,
        private val onDateChangeCallback: (Date) -> Unit
    ): RecyclerView.Adapter<DaysViewHolder>() {

        var selectedDate: Date? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
            return DaysViewHolder(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.calendar_list_item, parent, false),
            ::onItemClick)
        }

        private fun onItemClick(position: Int) {
            val prevPosition = items.indexOf(selectedDate)
            selectedDate = items[position]
            onDateChangeCallback(items[position])
            notifyItemChanged(prevPosition)
            notifyItemChanged(position)
        }

        override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {
            holder.bind(items[position], selectedDate == items[position])
        }

        override fun getItemCount() = items.size
    }

    class DaysViewHolder(
        itemView: View,
        onClick: (Int) -> Unit
    ): RecyclerView.ViewHolder(itemView) {

        private val dayNumber: TextView = itemView.findViewById(R.id.dayNumber)
        private val dayName: TextView = itemView.findViewById(R.id.dayName)

        init {
            itemView.setOnClickListener {
                onClick(adapterPosition)
            }
        }

        fun bind(date: Date, selected: Boolean) {
            dayNumber.text = monthDayFormatter.format(date)
            dayName.text = dayFormatter.format(date)

            itemView.setBackgroundResource(
                if(selected) R.drawable.selected_date_background else 0
            )

            val color = if (selected) Color.WHITE else Color.BLACK

            dayNumber.setTextColor(color)
            dayName.setTextColor(color)
        }

        companion object{
            val monthDayFormatter = SimpleDateFormat("dd", Locale.getDefault())
            val dayFormatter = SimpleDateFormat("EEE", Locale.getDefault())
        }
    }

    interface DateChangeListener {
        fun onDateChanged(date: Date)
    }
}