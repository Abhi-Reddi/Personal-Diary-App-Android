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

    // Button to trigger the date picker dialog
    private Button selectDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the layout for this activity
        setContentView(R.layout.activity_main);

        // Initialize the button to select a date
        selectDateButton = findViewById(R.id.selectDateButton);

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
}
