package com.example.diarynotes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class NoteActivity extends AppCompatActivity {

    // Constant for image picker request code
    private static final int PICK_IMAGE_REQUEST = 1;

    // UI components for displaying date, note content, and selected image
    private TextView noteDateTextView;
    private EditText noteEditText;
    private Button saveNoteButton, selectImageButton;
    private ImageView noteImageView;

    // Variables to hold selected date and image URI
    private String selectedDate;
    private Uri imageUri;

    // Firebase database and storage references
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Initializing UI components
        noteDateTextView = findViewById(R.id.noteDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        noteImageView = findViewById(R.id.noteImageView);

        // Get the selected date passed from MainActivity
        selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            // Display the selected date
            noteDateTextView.setText("Date: " + selectedDate);
        }

        // Initialize Firebase database and storage references
        firebaseDatabase = FirebaseDatabase.getInstance();
        notesDatabaseReference = firebaseDatabase.getReference("DiaryNotes");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        // Set up the button to open the image picker
        selectImageButton.setOnClickListener(v -> openImagePicker());

        // Set up the button to save the note and image
        saveNoteButton.setOnClickListener(v -> {
            // Get the text from the note field
            String noteText = noteEditText.getText().toString();
            if (!noteText.isEmpty()) {
                // Save the note to Firebase if note is not empty
                saveNoteToFirebase(selectedDate, noteText);

                // If an image is selected, upload it to Firebase Storage
                if (imageUri != null) {
                    uploadImageToFirebase();
                } else {
                    // Clear the inputs if no image is selected
                    clearInputs();
                }
            } else {
                // Show a message if the note field is empty
                Toast.makeText(NoteActivity.this, "Please write a note before saving.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open the image picker to select an image from the device
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the selected image is valid
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData(); // Get the URI of the selected image
            noteImageView.setVisibility(View.VISIBLE); // Show the ImageView
            noteImageView.setImageURI(imageUri); // Display the selected image
        }
    }

    // Method to save the note text to Firebase
    private void saveNoteToFirebase(String date, String note) {
        notesDatabaseReference.child(date).setValue(note) // Save note under the date as key
                .addOnSuccessListener(aVoid -> 
                    Toast.makeText(NoteActivity.this, "Note saved to Firebase for " + date, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> 
                    Toast.makeText(NoteActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Method to upload the selected image to Firebase Storage
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Create a unique reference for the image using the current time
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + "_note.jpg");
            imageRef.putFile(imageUri) // Upload the image file to Firebase Storage
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(NoteActivity.this, "Image uploaded to Firebase", Toast.LENGTH_SHORT).show();
                        clearInputs(); // Clear inputs after successful upload
                    })
                    .addOnFailureListener(e -> 
                        Toast.makeText(NoteActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    // Method to clear the input fields and reset the image selection
    private void clearInputs() {
        noteEditText.setText(""); // Clear the note field
        noteImageView.setVisibility(View.GONE); // Hide the ImageView
        imageUri = null; // Reset the image URI
    }
}
