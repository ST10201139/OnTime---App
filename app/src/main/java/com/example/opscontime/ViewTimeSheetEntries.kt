package com.example.opscontime

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewTimeSheetEntries : AppCompatActivity() {

    // Add RecyclerView and adapter variables
    private lateinit var timeSheetEntriesRecyclerView: RecyclerView
    private lateinit var timeSheetEntryAdapter: TimeSheetEntryAdapter
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_time_sheet_entries)

        // Initialize RecyclerView
        timeSheetEntriesRecyclerView = findViewById(R.id.recyclerViewEntries)
        timeSheetEntriesRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Firebase components
        database = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        // Retrieve data for the current user from Firebase Realtime Database
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            val userId = user.uid
            database.child("users").child(userId).child("entries").addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val entries = mutableListOf<EntryModel>()
                    for (postSnapshot in snapshot.children) {
                        val entry = postSnapshot.getValue(EntryModel::class.java)
                        entry?.let {
                            // Fetch the base64 image data and store it in the EntryModel object
                            val imageUrl = postSnapshot.child("imageUrl").getValue(String::class.java)
                            it.imageUrl = imageUrl
                            entries.add(it)
                        }
                    }
                    // Initialize and set up the RecyclerView adapter
                    timeSheetEntryAdapter = TimeSheetEntryAdapter(entries)
                    timeSheetEntriesRecyclerView.adapter = timeSheetEntryAdapter
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle any errors
                }
            })
        }
    }

    data class EntryModel(
        var entryName: String? = "",
        var entryDesc: String? = "",
        var startDateString: String? = "",
        var endDateString: String? = "",
        var startTimeString: String? = "",
        var endTimeString: String? = "",

        var category: String? = "",
        var imageUrl: String? = ""
    )
}