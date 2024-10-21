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

    private static final int PICK_IMAGE_REQUEST = 1;

    private TextView noteDateTextView;
    private EditText noteEditText;
    private Button saveNoteButton, selectImageButton;
    private ImageView noteImageView;
    private String selectedDate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        noteDateTextView = findViewById(R.id.noteDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        selectImageButton = findViewById(R.id.selectImageButton);
        noteImageView = findViewById(R.id.noteImageView);

        selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            noteDateTextView.setText("Notes for: " + selectedDate);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        notesDatabaseReference = firebaseDatabase.getReference("DiaryNotes");
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference("images");

        selectImageButton.setOnClickListener(v -> openImagePicker());

        saveNoteButton.setOnClickListener(v -> {
            String noteText = noteEditText.getText().toString();
            if (!noteText.isEmpty()) {
                saveNoteToFirebase(selectedDate, noteText);

                // Check if an image has been selected, then upload it
                if (imageUri != null) {
                    uploadImageToFirebase();
                } else {
                    clearInputs();
                }
            } else {
                Toast.makeText(NoteActivity.this, "Please write a note before saving.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            noteImageView.setVisibility(View.VISIBLE);
            noteImageView.setImageURI(imageUri);
        }
    }

    private void saveNoteToFirebase(String date, String note) {
        notesDatabaseReference.child(date).setValue(note)
                .addOnSuccessListener(aVoid -> Toast.makeText(NoteActivity.this, "Note saved to Firebase for " + date, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(NoteActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void uploadImageToFirebase() {
        if (imageUri != null) {
            StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + "_note.jpg");
            imageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        Toast.makeText(NoteActivity.this, "Image uploaded to Firebase", Toast.LENGTH_SHORT).show();
                        clearInputs(); // Clear inputs after uploading the image
                    })
                    .addOnFailureListener(e -> Toast.makeText(NoteActivity.this, "Failed to upload image: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void clearInputs() {
        noteEditText.setText(""); // Clear the note EditText
        noteImageView.setVisibility(View.GONE); // Hide the ImageView
        imageUri = null; // Reset the image URI
    }
}
