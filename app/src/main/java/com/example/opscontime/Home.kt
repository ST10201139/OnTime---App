package com.example.opscontime


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class Home : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize bottom navigation view
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_view_entries -> {
                    // Handle click on "View Entries" menu item
                    onViewEntriesClicked()
                    true
                }
                R.id.menu_add_entries -> {
                    // Handle click on "Add Entries" menu item
                    onAddEntriesClicked()
                    true
                }
                R.id.menu_add_goals -> {
                    // Handle click on "Add Goals" menu item
                    onViewGoalsClicked()
                    true
                }
                R.id.menu_add_hours -> {
                    // Handle click on "Add Hours" menu item
                    onViewTotalHoursClicked()
                    true
                }
                R.id.menu_pie_chart -> {
                    onPieChartClicked()
                    true
                }
                else -> false
            }
        }
    }

    // Handle click on "View Entries" menu item
    private fun onViewEntriesClicked() {
        val intent = Intent(this, ViewTimeSheetEntries::class.java)
        startActivity(intent)
    }

    // Handle click on "Add Entries" menu item
    private fun onAddEntriesClicked() {
        val intent = Intent(this, AddTimeEntries::class.java)
        startActivity(intent)
    }

    // Handle click on "Add Goals" menu item
    private fun onViewGoalsClicked() {
        val intent = Intent(this, ViewMyGoals::class.java)
        startActivity(intent)
    }

    // Handle click on "Add Hours" menu item
    private fun onViewTotalHoursClicked() {
        val intent = Intent(this, ViewTotalHours::class.java)
        startActivity(intent)
    }
    // Handle click on "Pie Chart" menu item
    private fun onPieChartClicked() {
        val intent = Intent(this, PieChartActivity::class.java)
        startActivity(intent)
    }
    override fun onStart() {
        super.onStart()

        // Check if user is signed in (non-null) and update UI accordingly
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // If user is not signed in, navigate to login screen
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Finish the Home activity
        }
    }
}