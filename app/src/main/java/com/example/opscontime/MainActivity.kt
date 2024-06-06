package com.example.opscontime


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    private lateinit var btnlog: Button
    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var reg: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Typecasting views
        btnlog = findViewById(R.id.buttonLogin)
        edEmail = findViewById(R.id.editTextUsername)
        edPassword = findViewById(R.id.editTextPassword)
        reg = findViewById(R.id.buttonRegister)

        // Set OnClickListener for the login button
        btnlog.setOnClickListener {
            // Get email and password from EditText fields
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()

            // Check if email and password are not empty
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter valid details", Toast.LENGTH_SHORT).show()
            } else {
                // Call loginUser function with email and password
                loginUser(email, password)
            }
        }

        // Set click listener for the registration button
        reg.setOnClickListener {
            val intentReg = Intent(this, Register2::class.java)
            startActivity(intentReg)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                // Login successful
                Toast.makeText(this, "You are now logged in", Toast.LENGTH_SHORT).show()

                // Get the current user's UID
                val user = FirebaseAuth.getInstance().currentUser
                val userId = user?.uid

                // Check if UID is not null
                if (userId != null) {
                    // Associate user's data with their UID in the database
                    // For example, you can create a node under "users" using the UID
                    val userRef =
                        FirebaseDatabase.getInstance().reference.child("users").child(userId)
                    // Set any initial data you want for the user
                    userRef.child("email").setValue(email) // Example: Storing email address
                    // You can add more data fields as needed

                    // Start Home activity
                    val intent = Intent(this, Home::class.java)
                    startActivity(intent)
                    finish() // Finish the LoginActivity
                } else {
                    // User's UID is null
                    Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Login failed
                Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
