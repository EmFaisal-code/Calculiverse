package com.example.calculiverse;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class IntervalWaktuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_interval_waktu);

        MaterialButton btnDate = findViewById(R.id.btn_date);
        MaterialButton btnClock = findViewById(R.id.btn_clock);
        MaterialButton btnDateEnd = findViewById(R.id.btn_date_end);
        MaterialButton btnClockEnd = findViewById(R.id.btn_clock_end);
        TextView tvUsia = findViewById(R.id.tv_usia);
        android.widget.ImageButton clearButton = findViewById(R.id.clear_button);

        // Default: today and now
        Calendar now = Calendar.getInstance();
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH.mm", Locale.getDefault());
        btnDate.setText(sdfDate.format(now.getTime()));
        btnClock.setText(sdfTime.format(now.getTime()));
        btnDateEnd.setText(sdfDate.format(now.getTime()));
        btnClockEnd.setText(sdfTime.format(now.getTime()));

        // State untuk tanggal & waktu
        final Calendar startCal = Calendar.getInstance();
        final Calendar endCal = Calendar.getInstance();
        final long[] lastStart = {startCal.getTimeInMillis()};
        final long[] lastEnd = {endCal.getTimeInMillis()};

        // Fungsi update hasil
        Runnable updateResult = () -> {
            long startMillis = startCal.getTimeInMillis();
            long endMillis = endCal.getTimeInMillis();
            if (endMillis < startMillis) {
                tvUsia.setText("- -");
                return;
            }
            long diffMillis = endMillis - startMillis;
            long totalMinutes = diffMillis / (1000 * 60);
            long totalHours = diffMillis / (1000 * 60 * 60);
            long totalDays = diffMillis / (1000 * 60 * 60 * 24);
            long years = totalDays / 365;
            long days = totalDays % 365;
            long hours = (diffMillis / (1000 * 60 * 60)) % 24;
            long minutes = (diffMillis / (1000 * 60)) % 60;

            String nl = System.lineSeparator();
            String result;
            if (diffMillis < 60 * 60 * 1000) { // < 1 jam
                result = String.format(Locale.getDefault(), "%d Menit" + nl + "%,d Menit", minutes, totalMinutes);
            } else if (diffMillis < 24 * 60 * 60 * 1000) { // < 1 hari
                result = String.format(Locale.getDefault(), "%d Jam" + nl + "%d Menit" + nl + "%,d Menit", hours, minutes, totalMinutes);
            } else if (diffMillis < 365L * 24 * 60 * 60 * 1000) { // < 1 tahun
                result = String.format(Locale.getDefault(), "%d Hari" + nl + "%d Jam" + nl + "%d Menit" + nl + "%,d Jam" + nl + "%,d Menit", totalDays, hours, minutes, totalHours, totalMinutes);
            } else { // >= 1 tahun
                result = String.format(Locale.getDefault(), "%d Tahun" + nl + "%d Hari" + nl + "%d Jam" + nl + "%d Menit" + nl + "%,d Hari" + nl + "%,d Jam" + nl + "%,d Menit", years, days, hours, minutes, totalDays, totalHours, totalMinutes);
            }
            tvUsia.setText(result);
        };

        // Set default
        startCal.setTimeInMillis(now.getTimeInMillis());
        endCal.setTimeInMillis(now.getTimeInMillis());
        tvUsia.setText("- -");

        btnDate.setOnClickListener(v -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal awal")
                    .setSelection(startCal.getTimeInMillis())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar picked = Calendar.getInstance();
                picked.setTimeInMillis(selection);
                btnDate.setText(sdfDate.format(picked.getTime()));
                int hour = startCal.get(Calendar.HOUR_OF_DAY);
                int minute = startCal.get(Calendar.MINUTE);
                startCal.set(Calendar.YEAR, picked.get(Calendar.YEAR));
                startCal.set(Calendar.MONTH, picked.get(Calendar.MONTH));
                startCal.set(Calendar.DAY_OF_MONTH, picked.get(Calendar.DAY_OF_MONTH));
                startCal.set(Calendar.HOUR_OF_DAY, hour);
                startCal.set(Calendar.MINUTE, minute);
                if (startCal.getTimeInMillis() != lastStart[0]) {
                    lastStart[0] = startCal.getTimeInMillis();
                    updateResult.run();
                }
            });
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER_START");
        });

        btnDateEnd.setOnClickListener(v -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal akhir")
                    .setSelection(endCal.getTimeInMillis())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar picked = Calendar.getInstance();
                picked.setTimeInMillis(selection);
                btnDateEnd.setText(sdfDate.format(picked.getTime()));
                int hour = endCal.get(Calendar.HOUR_OF_DAY);
                int minute = endCal.get(Calendar.MINUTE);
                endCal.set(Calendar.YEAR, picked.get(Calendar.YEAR));
                endCal.set(Calendar.MONTH, picked.get(Calendar.MONTH));
                endCal.set(Calendar.DAY_OF_MONTH, picked.get(Calendar.DAY_OF_MONTH));
                endCal.set(Calendar.HOUR_OF_DAY, hour);
                endCal.set(Calendar.MINUTE, minute);
                if (endCal.getTimeInMillis() != lastEnd[0]) {
                    lastEnd[0] = endCal.getTimeInMillis();
                    updateResult.run();
                }
            });
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER_END");
        });

        btnClock.setOnClickListener(v -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(startCal.get(Calendar.HOUR_OF_DAY))
                    .setMinute(startCal.get(Calendar.MINUTE))
                    .setTitleText("Pilih waktu awal")
                    .build();
            timePicker.addOnPositiveButtonClickListener(view -> {
                String jam = String.format(Locale.getDefault(), "%02d.%02d", timePicker.getHour(), timePicker.getMinute());
                btnClock.setText(jam);
                startCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                startCal.set(Calendar.MINUTE, timePicker.getMinute());
                startCal.set(Calendar.SECOND, 0);
                if (startCal.getTimeInMillis() != lastStart[0]) {
                    lastStart[0] = startCal.getTimeInMillis();
                    updateResult.run();
                }
            });
            timePicker.show(getSupportFragmentManager(), "TIME_PICKER_START");
        });

        btnClockEnd.setOnClickListener(v -> {
            MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(endCal.get(Calendar.HOUR_OF_DAY))
                    .setMinute(endCal.get(Calendar.MINUTE))
                    .setTitleText("Pilih waktu akhir")
                    .build();
            timePicker.addOnPositiveButtonClickListener(view -> {
                String jam = String.format(Locale.getDefault(), "%02d.%02d", timePicker.getHour(), timePicker.getMinute());
                btnClockEnd.setText(jam);
                endCal.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                endCal.set(Calendar.MINUTE, timePicker.getMinute());
                endCal.set(Calendar.SECOND, 0);
                if (endCal.getTimeInMillis() != lastEnd[0]) {
                    lastEnd[0] = endCal.getTimeInMillis();
                    updateResult.run();
                }
            });
            timePicker.show(getSupportFragmentManager(), "TIME_PICKER_END");
        });

        clearButton.setOnClickListener(v -> {
            Calendar reset = Calendar.getInstance();
            btnDate.setText(sdfDate.format(reset.getTime()));
            btnClock.setText(sdfTime.format(reset.getTime()));
            btnDateEnd.setText(sdfDate.format(reset.getTime()));
            btnClockEnd.setText(sdfTime.format(reset.getTime()));
            startCal.setTimeInMillis(reset.getTimeInMillis());
            endCal.setTimeInMillis(reset.getTimeInMillis());
            lastStart[0] = startCal.getTimeInMillis();
            lastEnd[0] = endCal.getTimeInMillis();
            tvUsia.setText("- -");
        });
    }
}