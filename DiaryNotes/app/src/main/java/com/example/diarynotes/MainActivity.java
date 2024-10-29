package com.example.diarynotes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // Button to trigger the date picker dialog
    private RecyclerView notesRecyclerView;
    private NotesAdapter notesAdapter;
    private List<Note> notesList = new ArrayList<>();

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference notesDatabaseReference;
    private Button selectDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_main);

        // Initialize the button to select a date
        selectDateButton = findViewById(R.id.selectDateButton);
        // Initialize Firebase database reference
        firebaseDatabase = FirebaseDatabase.getInstance();
        notesDatabaseReference = firebaseDatabase.getReference("DiaryNotes");

        // Set up RecyclerView for displaying notes
        notesRecyclerView = findViewById(R.id.notesRecyclerView);
        notesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter and set it to RecyclerView
        notesAdapter = new NotesAdapter(notesList);
        notesRecyclerView.setAdapter(notesAdapter);

        // Fetch all notes from Firebase
        fetchAllNotes();


        // Set an OnClickListener to handle button clicks
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date using Calendar instance
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR); // Current year
                int month = calendar.get(Calendar.MONTH); // Current month
                int day = calendar.get(Calendar.DAY_OF_MONTH); // Current day

                // Create a DatePickerDialog to let the user select a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            // This method is called when the user sets the date
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the selected date as a string (Day/Month/Year)
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                                // Create an Intent to start the NoteActivity and pass the selected date to it
                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                intent.putExtra("selectedDate", selectedDate); // Pass the selected date
                                startActivity(intent); // Start NoteActivity
                            }
                        }, year, month, day); // Pass the current date as the default date

                // Show the date picker dialog to the user
                datePickerDialog.show();
            }
        });
    }

    private void fetchAllNotes() {
        notesDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Note note = snapshot.getValue(Note.class);
                    notesList.add(note);
                }
                notesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MainActivity.this, "Failed to load notes", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
