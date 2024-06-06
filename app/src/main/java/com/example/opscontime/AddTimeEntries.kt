package com.example.opscontime

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTimeEntries : AppCompatActivity() {

    private lateinit var categorySpinner: Spinner
    private lateinit var entryNameEditText: EditText
    private lateinit var entryDescriptionEditText: EditText
    private lateinit var startDateButton: EditText
    private lateinit var startTimeButton: Button
    private lateinit var endDateButton: EditText
    private lateinit var endTimeButton: Button
    private lateinit var saveButton: Button
    private lateinit var captureImageButton: Button
    private lateinit var uploadImageButton: Button
    private lateinit var imageView: ImageView

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var startTimeDate: Date? = null
    private var endTimeDate: Date? = null
    private var startDate: Date? = null
    private var endDate: Date? = null
    private val REQUEST_IMAGE_CAPTURE = 1
    private val PICK_IMAGE_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_time_entries)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference.child("users").child(auth.currentUser?.uid ?: "")

        // Initialize views
        categorySpinner = findViewById(R.id.spinnerCategories)
        entryNameEditText = findViewById(R.id.editTextName)
        entryDescriptionEditText = findViewById(R.id.editTextDescription)
        startTimeButton = findViewById(R.id.btnStartTimer)
        startDateButton = findViewById(R.id.editTextStartDate)
        endDateButton = findViewById(R.id.editTextEndDate)
        endTimeButton = findViewById(R.id.btnEndTimer)
        saveButton = findViewById(R.id.buttonSave)
        captureImageButton = findViewById(R.id.buttonCamera)
        uploadImageButton = findViewById(R.id.buttonUpload)
        imageView = findViewById(R.id.imageView3)

        // Populate spinner with categories
        val spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.spinner_items,
            android.R.layout.simple_spinner_dropdown_item
        )
        categorySpinner.adapter = spinnerAdapter

        // Set click listeners
        startDateButton.setOnClickListener {
            showDatePicker(startDateListener)
        }

        startTimeButton.setOnClickListener {
            showTimePicker(startTimeListener)
        }

        endDateButton.setOnClickListener {
            showDatePicker(endDateListener)
        }

        endTimeButton.setOnClickListener {
            showTimePicker(endTimeListener)
        }

        saveButton.setOnClickListener {
            val selectedItem = categorySpinner.selectedItem as String
            val entryName = entryNameEditText.text.toString()
            val entryDescription = entryDescriptionEditText.text.toString()

            if (entryName.isEmpty()) {
                entryNameEditText.error = "Please enter Entry Name"
                return@setOnClickListener
            }

            if (entryDescription.isEmpty()) {
                entryDescriptionEditText.error = "Please enter Description"
                return@setOnClickListener
            }

            // Save entry to Firebase
            saveToFirebase(selectedItem, entryName, entryDescription)
        }

        captureImageButton.setOnClickListener {
            dispatchTakePictureIntent()
        }

        uploadImageButton.setOnClickListener {
            // Open the phone's gallery to select an image
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST)
        }
    }

    // Function to display date picker dialog
    private fun showDatePicker(dateSetListener: DatePickerDialog.OnDateSetListener) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, dateSetListener, year, month, day)
        datePickerDialog.show()
    }

    // Function to display time picker dialog
    private fun showTimePicker(timeSetListener: TimePickerDialog.OnTimeSetListener) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this, timeSetListener, hour, minute, true)
        timePickerDialog.show()
    }

    // Listener for start date selection
    private val startDateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)
        startDate = selectedCalendar.time
        updateEditTextDate(startDateButton, startDate, "yyyy-MM-dd")
    }

    // Listener for start time selection
    private val startTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        startTimeDate = selectedCalendar.time
        updateButtonText(startTimeButton, startTimeDate, "HH:mm")
    }

    // Listener for end date selection
    private val endDateListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(year, month, day)
        endDate = selectedCalendar.time
        updateEditTextDate(endDateButton, endDate, "yyyy-MM-dd")
    }

    // Listener for end time selection
    private val endTimeListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
        val selectedCalendar = Calendar.getInstance()
        selectedCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
        selectedCalendar.set(Calendar.MINUTE, minute)
        endTimeDate = selectedCalendar.time
        updateButtonText(endTimeButton, endTimeDate, "HH:mm")
    }

    // Function to update date in EditText
    private fun updateEditTextDate(editText: EditText, date: Date?, format: String) {
        date?.let {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            editText.setText(dateFormat.format(it))
        }
    }

    // Function to update button text
    private fun updateButtonText(button: Button, date: Date?, format: String) {
        date?.let {
            val dateFormat = SimpleDateFormat(format, Locale.getDefault())
            button.text = dateFormat.format(it)
        }
    }

    // Function to capture image from camera
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Function to handle result of image capture or selection
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Capture image from camera
            val imageBitmap = data?.extras?.get("data") as Bitmap
            imageView.setImageBitmap(imageBitmap)
        } else if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            // Select image from gallery
            val selectedImageUri = data?.data
            imageView.setImageURI(selectedImageUri)
        }
    }

    // Function to save entry to Firebase
    private fun saveToFirebase(category: String, entryName: String, entryDescription: String) {
        val userId = auth.currentUser?.uid ?: ""
        val entry = EntryModel(
            entryName,
            entryDescription,
            startDateButton.text.toString(),
            endDateButton.text.toString(),
            startTimeButton.text.toString(),
            endTimeButton.text.toString(),
            category,
            null,
            null
        )

        val key = database.child("entries").push().key
        key?.let {
            entry.imageName = it

            // Convert image to base64 string
            val imageBitmap = (imageView.drawable as BitmapDrawable).bitmap
            val outputStream = ByteArrayOutputStream()
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            val imageBytes = outputStream.toByteArray()
            val base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

            entry.imageUrl = base64Image

            // Save entry to Firebase
            database.child("entries").child(key).setValue(entry)
                .addOnSuccessListener {
                    Toast.makeText(this, "Entry saved successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { error ->
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "Error saving entry: ${error.message}", error)
                }
        }
    }


    // Model class for entry
    data class EntryModel(
        var entryName: String? = null,
        var entryDesc: String? = null,
        var startDateString: String? = null,
        var endDateString: String? = null,
        var startTimeString: String? = null,
        var endTimeString: String? = null,

        var category: String? = null,
        var imageName: String? = null,
        var imageUrl: String? = null
    )
}
