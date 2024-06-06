package com.example.opscontime

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class PieChartActivity : AppCompatActivity() {

    private lateinit var pieChart: PieChart
    private lateinit var database: DatabaseReference
    private lateinit var buttonSelectDateRange: Button
    private lateinit var buttonCheckGoals: Button
    private var startDate: Long = 0
    private var endDate: Long = 0
    private var minGoal: Float = 0f
    private var maxGoal: Float = 0f
    private var originalPieEntries: Map<String, Float> = emptyMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        pieChart = findViewById(R.id.pieChart)
        buttonSelectDateRange = findViewById(R.id.buttonSelectDateRange)
        buttonCheckGoals = findViewById(R.id.buttonCheckGoals)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        userId?.let {
            // Set up the button click listener
            buttonSelectDateRange.setOnClickListener {
                showDateRangePicker()
            }

            buttonCheckGoals.setOnClickListener {
                filterCategoriesByGoals()
            }
        }
    }

    private fun showDateRangePicker() {
        val dateRangePicker = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select Date Range")
            .build()

        dateRangePicker.show(supportFragmentManager, "date_range_picker")

        dateRangePicker.addOnPositiveButtonClickListener { selection ->
            startDate = selection.first ?: 0
            endDate = selection.second ?: 0
            retrieveGoalsAndDataFromFirebase()
        }
    }

    private fun retrieveGoalsAndDataFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val userRef = database.child("users").child(it)

            userRef.child("goals").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    minGoal = dataSnapshot.child("minGoal").getValue(Float::class.java) ?: 0f
                    maxGoal = dataSnapshot.child("maxGoal").getValue(Float::class.java) ?: 0f
                    retrieveDataFromFirebase()
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle database error
                }
            })
        }
    }

    private fun retrieveDataFromFirebase() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        userId?.let {
            val entriesRef = database.child("users").child(it).child("entries")

            entriesRef.orderByChild("startDateString")
                .startAt(formatDate(startDate))
                .endAt(formatDate(endDate))
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val pieEntries = HashMap<String, Float>()

                        for (entrySnapshot in dataSnapshot.children) {
                            val entry = entrySnapshot.getValue(AddTimeEntries.EntryModel::class.java)
                            entry?.let {
                                val totalTime = calculateTotalTime(entry)

                                // Aggregate total time by category
                                val category = it.category ?: "Unknown"
                                pieEntries[category] = (pieEntries[category] ?: 0f) + totalTime
                            }
                        }

                        originalPieEntries = pieEntries
                        // Update the PieChart
                        updatePieChart(pieEntries)
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle database error
                    }
                })
        }
    }

    private fun calculateTotalTime(entry: AddTimeEntries.EntryModel): Float {
        val startTimeParts = entry.startTimeString?.split(":") ?: listOf("0", "0")
        val endTimeParts = entry.endTimeString?.split(":") ?: listOf("0", "0")

        val startHour = startTimeParts[0].toIntOrNull() ?: 0
        val startMinute = startTimeParts[1].toIntOrNull() ?: 0
        val endHour = endTimeParts[0].toIntOrNull() ?: 0
        val endMinute = endTimeParts[1].toIntOrNull() ?: 0

        // Convert hours and minutes to total minutes
        val totalStartMinutes = startHour * 60 + startMinute
        val totalEndMinutes = endHour * 60 + endMinute

        // If end time is smaller than start time, it means the time spans across midnight
        val correctedTotalEndMinutes = if (totalEndMinutes < totalStartMinutes) totalEndMinutes + 24 * 60 else totalEndMinutes

        // Calculate total time in hours
        val totalTime = (correctedTotalEndMinutes - totalStartMinutes).toFloat() / 60

        return totalTime
    }

    private fun filterCategoriesByGoals() {
        val filteredEntries = originalPieEntries.filter { it.value in minGoal..maxGoal }
        updatePieChart(filteredEntries, true)
    }

    private fun updatePieChart(entries: Map<String, Float>, filtered: Boolean = false) {
        val pieEntries = entries.map { PieEntry(it.value, it.key) }.toMutableList()

        // Assign colors to different entries based on their category
        val colors = ArrayList<Int>().apply {
            add(Color.BLUE)
            add(Color.GREEN)
            add(Color.RED)
            add(Color.YELLOW)
            add(Color.CYAN)
            add(Color.MAGENTA)
            add(Color.LTGRAY)
            // Add more colors as needed
        }

        // Add Min and Max Goals as separate entries
        if (!filtered) {
            pieEntries.add(PieEntry(minGoal, "Min Goal"))
            pieEntries.add(PieEntry(maxGoal, "Max Goal"))
        }

        val dataSet = PieDataSet(pieEntries, "Total Hours Worked")
        dataSet.colors = colors

        val data = PieData(dataSet)
        pieChart.data = data
        pieChart.invalidate()  // Refresh the chart
    }

    private fun formatDate(milliseconds: Long): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = milliseconds
        val year = calendar.get(Calendar.YEAR)
        val month = (calendar.get(Calendar.MONTH) + 1).toString().padStart(2, '0')
        val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
        return "$year-$month-$day"
    }
}
