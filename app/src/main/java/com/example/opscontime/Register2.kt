package com.example.opscontime

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Register2 : AppCompatActivity() {
    private lateinit var edEmail: EditText
    private lateinit var edPassword: EditText
    private lateinit var edConfirm: EditText
    private lateinit var btnRegister: Button

    // Firebase
    private lateinit var mauth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)

        // Typecast the variables to the design
        edEmail = findViewById(R.id.editTextUsername)
        edPassword = findViewById(R.id.editTextPassword)
        edConfirm = findViewById(R.id.editTextConfirmPassword)
        btnRegister = findViewById(R.id.buttonRegister)

        // Firebase
        mauth = FirebaseAuth.getInstance()

        btnRegister.setOnClickListener {
            registerUser()
        }
    }

    // Method to register
    private fun registerUser() {
        try {
            val email = edEmail.text.toString().trim()
            val password = edPassword.text.toString().trim()
            val confirmPassword = edConfirm.text.toString().trim()

            // Check if email, password, and confirm password are not empty
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirmPassword)) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // Check if password and confirm password match
            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return
            }

            // Attempt registration using Firebase Authentication
            mauth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                        // Get the current user's UID
                        val user = FirebaseAuth.getInstance().currentUser
                        val userId = user?.uid

                        // Check if UID is not null
                        if (userId != null) {
                            // Associate user's data with their UID in the database
                            val userRef = FirebaseDatabase.getInstance().reference.child("users").child(userId)
                            // Set any initial data you want for the user
                            userRef.child("email").setValue(email) // Example: Storing email address
                            // You can add more data fields as needed
                        } else {
                            // User's UID is null
                            Toast.makeText(this, "Failed to get user ID", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Log error if registration fails
                        Log.e("Register", "Registration failed", task.exception)
                        Toast.makeText(this, "Registration failed. Please try again.", Toast.LENGTH_SHORT).show()
                    }
                }
        } catch (e: Exception) {
            // Log any unexpected exceptions
            Log.e("Register", "Error during registration", e)
            Toast.makeText(this, "An unexpected error occurred. Please try again.", Toast.LENGTH_SHORT).show()
        }
    }

}