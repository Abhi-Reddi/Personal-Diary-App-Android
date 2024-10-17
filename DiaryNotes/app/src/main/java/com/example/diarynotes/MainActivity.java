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

    private Button selectDateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the date picker button
        selectDateButton = findViewById(R.id.selectDateButton);

        // Set up date picker dialog on button click
        selectDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the current date
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                // Create DatePickerDialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                                // The selected date (month + 1 because it's 0-indexed)
                                String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;

                                // Open NoteActivity and pass the selected date
                                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                                intent.putExtra("selectedDate", selectedDate);
                                startActivity(intent);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });
    }
}
