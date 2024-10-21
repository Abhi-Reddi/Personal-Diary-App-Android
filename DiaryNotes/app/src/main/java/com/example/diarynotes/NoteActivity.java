package com.example.diarynotes;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoteActivity extends AppCompatActivity {

    private TextView noteDateTextView;
    private EditText noteEditText;
    private Button saveNoteButton;
    private String selectedDate;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        noteDateTextView = findViewById(R.id.noteDateTextView);
        noteEditText = findViewById(R.id.noteEditText);
        saveNoteButton = findViewById(R.id.saveNoteButton);
        selectedDate = getIntent().getStringExtra("selectedDate");
        if (selectedDate != null) {
            noteDateTextView.setText("Notes for: " + selectedDate);
        }

        firebaseDatabase = FirebaseDatabase.getInstance();
        notesDatabaseReference = firebaseDatabase.getReference("DiaryNotes");

        saveNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteText = noteEditText.getText().toString();
                if (!noteText.isEmpty()) {
                    saveNoteToFirebase(selectedDate, noteText);
                } else {
                    Toast.makeText(NoteActivity.this, "Please write a note before saving.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveNoteToFirebase(String date, String note) {
        notesDatabaseReference.child(date).setValue(note)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(NoteActivity.this, "Note saved to Firebase for " + date, Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(NoteActivity.this, "Failed to save note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}
