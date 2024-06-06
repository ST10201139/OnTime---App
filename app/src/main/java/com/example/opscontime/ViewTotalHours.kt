package com.example.opscontime

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewTotalHours : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategoryHoursAdapter
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_total_hours)

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCategoryHours)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = CategoryHoursAdapter(ArrayList())
        recyclerView.adapter = adapter

        // Initialize Firebase components
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        // Fetch data from Firebase and calculate total hours for each category
        RetreiveDataAndCalculateTotalHours()
    }

    private fun RetreiveDataAndCalculateTotalHours() {
        // Get the current user's ID
        val userId = firebaseAuth.currentUser?.uid ?: ""

        // Query the database for entries belonging to the current user
        database.child("users").child(userId).child("entries")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val categoryStats = mutableMapOf<String, Double>()

                    for (itemSnapshot in snapshot.children) {
                        val item = itemSnapshot.getValue(AddTimeEntries.EntryModel::class.java)

                        if (item != null) {
                            val category = item.category ?: "" // Ensure category is not null
                            val totalTime = categoryStats.getOrDefault(category, 0.0)
                            val newTotalTime = totalTime + calculateTotalHours(
                                item.startTimeString,
                                item.endTimeString
                            )
                            categoryStats[category] = newTotalTime
                        }
                    }

                    // Update RecyclerView with category statistics
                    updateRecyclerView(categoryStats)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                    // For example, log the error
                    Log.e("ViewTotalHours", "DatabaseError: ${error.message}", error.toException())
                }
            })
    }


    private fun calculateTotalHours(startTimeString: String?, endTimeString: String?): Double {
        if (startTimeString == null || endTimeString == null) {
            return 0.0
        }

        // Split the start and end time strings into hours and minutes
        val startTimeParts = startTimeString.split(":")
        val endTimeParts = endTimeString.split(":")

        // Extract hours and minutes from the time strings
        val startHours = startTimeParts[0].toInt()
        val startMinutes = startTimeParts[1].toInt()
        val endHours = endTimeParts[0].toInt()
        val endMinutes = endTimeParts[1].toInt()

        // Calculate the total time in minutes
        val totalStartMinutes = startHours * 60 + startMinutes
        val totalEndMinutes = endHours * 60 + endMinutes

        // Calculate the total hours
        var totalHours = (totalEndMinutes - totalStartMinutes) / 60.0

        // If the end time is before the start time (crossing midnight), add 24 hours
        if (totalHours < 0) {
            totalHours += 24.0
        }

        return totalHours
    }


    private fun updateRecyclerView(categoryStats: Map<String, Double>) {
        // Update the RecyclerView with the calculated category statistics
        val categoryList = categoryStats.entries.map { it.key to it.value }
        adapter.setCategoryStats(categoryList)
    }
}