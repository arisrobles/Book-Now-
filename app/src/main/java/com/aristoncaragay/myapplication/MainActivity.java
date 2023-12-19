package com.aristoncaragay.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private long selectedCheckInDate = 0;
    private long selectedCheckOutDate = 0;
    private TextView reservationText;
    private Button btDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reservationText = findViewById(R.id.reservationText);
        btDetails = findViewById(R.id.btDetails);
        btDetails.setVisibility(View.GONE); // Initially set to GONE

        btDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Details.class);
                startActivity(intent);
            }
        });
    }

    public void onBookNowButtonClicked(View view) {
        showCheckInDatePicker();
    }

    private void showCheckInDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        selectedCheckInDate = selectedCalendar.getTimeInMillis();
                        String checkInMessage = "Check-in date selected: " + getFormattedDate(selectedCheckInDate);
                        showSnackbar(checkInMessage);

                        // Now show the Check-out DatePicker
                        showCheckOutDatePicker();
                    }
                }, year, month, dayOfMonth);

        // Set the minimum date to today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        datePickerDialog.show();
    }

    private void showCheckOutDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar selectedCalendar = Calendar.getInstance();
                        selectedCalendar.set(year, month, dayOfMonth);

                        selectedCheckOutDate = selectedCalendar.getTimeInMillis();
                        String checkOutMessage = "Check-out date selected: " + getFormattedDate(selectedCheckOutDate);
                        showSnackbar(checkOutMessage);

                        // Update TextView with Check-out date
                        updateReservationText();
                    }
                }, year, month, dayOfMonth);

        // Set the minimum date to the selected check-in date
        datePickerDialog.getDatePicker().setMinDate(selectedCheckInDate);

        datePickerDialog.show();
    }

    private void updateReservationText() {
        if (selectedCheckInDate > 0 && selectedCheckOutDate > 0) {
            String reservationMessage = "Reservation settled\nCheck-in: " +
                    getFormattedDate(selectedCheckInDate) +
                    "\nCheck-out: " + getFormattedDate(selectedCheckOutDate);
            reservationText.setText(reservationMessage);

            // Check both dates are selected, then show the "See Details" button
            if (btDetails.getVisibility() == View.GONE) {
                btDetails.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showSnackbar(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show();
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
}
