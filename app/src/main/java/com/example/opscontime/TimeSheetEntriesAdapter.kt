package com.example.opscontime

import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TimeSheetEntryAdapter(private val entries: MutableList<ViewTimeSheetEntries.EntryModel>) : RecyclerView.Adapter<TimeSheetEntryAdapter.ViewHolder>() {

    private var expandedPosition = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.time_entry_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.bind(entry, position)
    }

    override fun getItemCount(): Int = entries.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cardView: CardView = itemView.findViewById(R.id.cardView)
        private val entryNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        private val entryDescTextView: TextView = itemView.findViewById(R.id.textViewDescription)
        private val startDateTextView: TextView = itemView.findViewById(R.id.textViewStartDate)
        private val endDateTextView: TextView = itemView.findViewById(R.id.textViewEndDate)
        private val startTimeTextView: TextView = itemView.findViewById(R.id.textViewStartTime)
        private val endTimeTextView: TextView = itemView.findViewById(R.id.textViewEndTime)

        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        init {
            cardView.setOnClickListener {
                val position = adapterPosition
                expandedPosition = if (expandedPosition == position) -1 else position
                notifyDataSetChanged()
            }
        }

        fun bind(entry: ViewTimeSheetEntries.EntryModel, position: Int) {
            val isExpanded = position == expandedPosition
            entryNameTextView.text = entry.entryName
            entryDescTextView.text = entry.entryDesc
            startDateTextView.text = "Start Date: ${entry.startDateString}"
            endDateTextView.text = "End Date: ${entry.endDateString}"
            startTimeTextView.text = "Start Time: ${entry.startTimeString}"
            endTimeTextView.text = "End Time: ${entry.endTimeString}"

            categoryTextView.text = "Category: ${entry.category}"

            // Load image from base64 string
            if (entry.imageUrl != null && entry.imageUrl!!.isNotEmpty()) {
                imageView.visibility = if (isExpanded) View.VISIBLE else View.GONE
                val imageBytes = Base64.decode(entry.imageUrl, Base64.DEFAULT)
                val imageBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                imageView.setImageBitmap(imageBitmap)
            } else {
                imageView.visibility = View.GONE
            }

            // Adjust visibility of views based on expanded state
            entryDescTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            startDateTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            endDateTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            startTimeTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            endTimeTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            categoryTextView.visibility = if (isExpanded) View.VISIBLE else View.GONE
        }
    }
}