package com.example.diarynotes;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    // Button to select the date
    private Button selectDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set the layout for the activity

        // Initialize the button by linking it to the view in the XML layout
        selectDateButton = findViewById(R.id.selectDateButton);
        
        // Set an OnClickListener to handle the date selection action when the button is clicked
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create a DatePickerDialog to allow the user to pick a date
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // Format the selected date (month is 0-based, so add 1)
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                                // Create an Intent to start the NoteActivity and pass the selected date
                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                intent.putExtra("selectedDate", selectedDate); // Pass the selected date to the new activity
                                startActivity(intent); // Start the NoteActivity
                            }
                        }, year, month, day); // Pass the current year, month, and day as default values

                // Show the DatePickerDialog to the user
                datePickerDialog.show();
            }
        });
    }
}
