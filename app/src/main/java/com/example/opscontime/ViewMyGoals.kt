package com.example.opscontime

import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class ViewMyGoals : AppCompatActivity() {

    //variables
    private lateinit var textViewMinHours: TextView
    private lateinit var textViewMaxHours: TextView
    private lateinit var editTextMinGoal: EditText
    private lateinit var editTextMaxGoal: EditText
    private lateinit var progressBarMin: ProgressBar
    private lateinit var progressBarMax: ProgressBar
    private lateinit var buttonSaveGoals: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_my_goals)

        // Initialize Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Initialize views
        textViewMinHours = findViewById(R.id.textViewMinHours)
        textViewMaxHours = findViewById(R.id.textViewMaxHours)
        editTextMinGoal = findViewById(R.id.editTextMinGoal)
        editTextMaxGoal = findViewById(R.id.editTextMaxGoal)
        progressBarMin = findViewById(R.id.progressBarMin)
        progressBarMax = findViewById(R.id.progressBarMax)
        buttonSaveGoals = findViewById(R.id.buttonSaveGoals)

        // Set onClickListener for save button
        buttonSaveGoals.setOnClickListener {
            saveGoals()
        }
    }

    //save goals method
    private fun saveGoals() {
        val minGoalText = editTextMinGoal.text.toString()
        val maxGoalText = editTextMaxGoal.text.toString()

        // Check if the input fields are not empty
        if (minGoalText.isNotEmpty() && maxGoalText.isNotEmpty()) {
            val minGoal = minGoalText.toIntOrNull()
            val maxGoal = maxGoalText.toIntOrNull()

            // Check if both minGoal and maxGoal are not null
            if (minGoal != null && maxGoal != null) {
                try {
                    // Set the text for textViewMinHours and textViewMaxHours
                    textViewMinHours.text = "Minimum Hours: $minGoal"
                    textViewMaxHours.text = "Maximum Hours: $maxGoal"

                    // Update progress bars
                    updateProgressBar(progressBarMin, minGoal)
                    updateProgressBar(progressBarMax, maxGoal)

                    // Save goals to Firebase Realtime Database
                    val userId = firebaseAuth.currentUser?.uid
                    userId?.let {
                        database.reference.child("users").child(userId).child("goals").setValue(mapOf(
                            "minGoal" to minGoal,
                            "maxGoal" to maxGoal
                        ))
                    }

                    // Display a toast message indicating that the goals have been saved
                    Toast.makeText(this, "Goals saved", Toast.LENGTH_SHORT).show()

                    // Use a Handler to delay the execution of finish() by a couple of seconds
                    Handler().postDelayed({
                        // Navigate back to the home page after a delay
                        finish()
                    }, 2000) // 2000 milliseconds = 2 seconds
                } catch (e: Exception) {
                    // Handle any exceptions that may occur
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Show error message if either minGoal or maxGoal is null
                Toast.makeText(this, "Please enter valid goals", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Show error message if input fields are empty
            Toast.makeText(this, "Please enter both minimum and maximum goals", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateProgressBar(progressBar: ProgressBar, goal: Int) {
        // Set progress to the goal value
        progressBar.progress = goal
    }
}
