package com.example.opscontime

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Adapter for the RecyclerView in ViewTotalHours activity
class CategoryHoursAdapter(private val categoryStatsList: MutableList<Pair<String, Double>>) :
    RecyclerView.Adapter<CategoryHoursAdapter.CategoryHoursViewHolder>() {

    // Create view holder for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHoursViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_hours_items, parent, false)
        return CategoryHoursViewHolder(itemView)
    }

    // Bind data to each item in the RecyclerView
    override fun onBindViewHolder(holder: CategoryHoursViewHolder, position: Int) {
        val (category, totalHours) = categoryStatsList[position]
        holder.bind(category, totalHours)
    }

    // Get the total number of items in the RecyclerView
    override fun getItemCount(): Int {
        return categoryStatsList.size
    }

    // Set category statistics and notify adapter of data changes
    fun setCategoryStats(stats: List<Pair<String, Double>>) {
        categoryStatsList.clear()
        categoryStatsList.addAll(stats)
        notifyDataSetChanged()
    }

    // View holder class to hold references to views for each item
    inner class CategoryHoursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryTextView: TextView = itemView.findViewById(R.id.textViewCategory)
        private val totalHoursTextView: TextView = itemView.findViewById(R.id.textViewHours)

        // Bind data to views
        fun bind(category: String, totalHours: Double) {
            categoryTextView.text = category
            totalHoursTextView.text = String.format("%.1f", totalHours)
        }
    }
}
