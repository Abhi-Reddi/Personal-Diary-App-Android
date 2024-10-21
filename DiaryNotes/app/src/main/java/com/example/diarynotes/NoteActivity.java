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

    // Firebase database reference
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;


}
