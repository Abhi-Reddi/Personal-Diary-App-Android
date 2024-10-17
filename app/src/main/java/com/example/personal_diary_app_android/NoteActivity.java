package com.example.personal_diary_app_android;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class NoteActivity extends AppCompatActivity {

    private TextView noteDateTextView;
    private EditText noteEditText;
    private Button saveButton;
   // private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        // Initialize Firebase Firestore
        //db = FirebaseFirestore.getInstance();

        noteDateTextView = findViewById(R.id.noteDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.saveButton);

        // Get selected date passed from the previous activity
        String selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            noteDateTextView.setText("Date: " + selectedDate);
        }

        // Save the diary entry when the save button is clicked
        saveButton.setOnClickListener(v -> saveDiaryEntry(selectedDate));
    }

    // Method to save diary entry to Firestore
    private void saveDiaryEntry(String selectedDate) {
        String noteContent = noteEditText.getText().toString();

        // Get the user's unique ID from Firebase Authentication
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Create a map of the diary entry data
        Map<String, Object> diaryEntry = new HashMap<>();
        diaryEntry.put("date", selectedDate);
        diaryEntry.put("note", noteContent);

        // Save the diary entry in Firestore under the user's collection
      /*  db.collection("users").document(userId)
                .collection("diaryEntries")
                .add(diaryEntry)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(NoteActivity.this, "Diary entry saved!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NoteActivity.this, "Error saving entry", Toast.LENGTH_SHORT).show();
                });*/
    }
}
