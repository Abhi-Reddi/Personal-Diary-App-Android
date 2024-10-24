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

    private static final int PICK_IMAGE_REQUEST = 1; // Request code for selecting an image

    // UI elements for displaying the date, writing notes, selecting images, and saving notes
    private TextView noteDateTextView;
    private EditText noteEditText;
    private Button saveNoteButton, selectImageButton;
    private ImageView noteImageView;
    
    // Variables for the selected date and Firebase references
    private String selectedDate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri; // To store the URI of the selected image

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Initialize UI elements
        noteDateTextView = findViewById(R.id.noteDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        noteImageView = findViewById(R.id.noteImageView);

        // Retrieve the selected date from the Intent and set it on the TextView
        selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            noteDateTextView.setText("Date: " + selectedDate);
        }

        // Initialize Firebase components for database and storage
        firebaseDatabase = FirebaseDatabase.getInstance();
        notesDatabaseReference = firebaseDatabase.getReference("DiaryNotes");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        // Set an OnClickListener to select an image when the button is clicked
        selectImageButton.setOnClickListener(v -> openImagePicker());

        // Set an OnClickListener to save the note and upload the image (if any)
        saveNoteButton.setOnClickListener(v -> {
            String noteText = noteEditText.getText().toString(); // Get the note text
            if (!noteText.isEmpty()) {
                // Save the note to Firebase if the text is not empty
                saveNoteToFirebase(selectedDate, noteText);

                // Upload the image if one was selected
                if (imageUri != null) {
                    uploadImageToFirebase();
                } else {
                    // Clear the input fields if no image was selected
                    clearInputs();
                }
            } else {
                Toast.makeText(NoteActivity.this, "Please write a note before saving.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to open the image picker
    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT); // Create an intent to pick an image
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST); // Start the image picker activity
    }

    // Handle the result of the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // If an image was successfully selected, set the URI and display it in the ImageView
            imageUri = data.getData();
            noteImageView.setVisibility(View.VISIBLE);
            noteImageView.setImageURI(imageUri);
        }
    }

    // Method to save the note to Firebase Realtime Database
    private void saveNoteToFirebase(String date, String note) {
        notesDatabaseReference.child(date).setValue(note)
                .addOnSuccessListener(aVoid -> Toast.makeText(NoteActivity.this, "Note saved to Firebase for " + date, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(NoteActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    // Method to upload the selected image to Firebase Storage
    private void uploadImageToFirebase() {
        if (imageUri != null) {
            // Create a reference in Firebase Storage with a unique name for the image
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + "_note.jpg");
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(NoteActivity.this, "Image uploaded to Firebase", Toast.LENGTH_SHORT).show();
                        clearInputs(); // Clear the inputs after successful upload
                    })
                    .addOnFailureListener(e -> Toast.makeText(NoteActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    // Method to clear the note text and hide the ImageView after saving
    private void clearInputs() {
        noteEditText.setText("");
        noteImageView.setVisibility(View.GONE);
        imageUri = null; // Reset the image URI
    }
}
