package com.example.calculiverse;

import android.os.Bundle;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import androidx.annotation.NonNull;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.graphics.Color;

public class KalkulatorUsiaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kalkulator_usia);

        MaterialButton btnDate = findViewById(R.id.btn_date);
        TextView tvUsia = findViewById(R.id.tv_usia);
        TextView tvNextBirthday = findViewById(R.id.tv_next_birthday);

        // Set default date to today
        Calendar today = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy", new Locale("id"));
        btnDate.setText(sdf.format(today.getTime()));

        btnDate.setOnClickListener(v -> {
            CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
            constraintsBuilder.setEnd(today.getTimeInMillis()); // Only allow up to today
            MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Pilih tanggal lahir")
                    .setSelection(today.getTimeInMillis())
                    .setCalendarConstraints(constraintsBuilder.build())
                    .build();
            datePicker.addOnPositiveButtonClickListener(selection -> {
                Calendar picked = Calendar.getInstance();
                picked.setTimeInMillis(selection);
                btnDate.setText(sdf.format(picked.getTime()));

                // Calculate age
                Calendar birth = (Calendar) picked.clone();
                Calendar now = Calendar.getInstance();

                int years = now.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
                int months = now.get(Calendar.MONTH) - birth.get(Calendar.MONTH);
                int days = now.get(Calendar.DAY_OF_MONTH) - birth.get(Calendar.DAY_OF_MONTH);

                if (days < 0) {
                    months--;
                    Calendar temp = (Calendar) now.clone();
                    temp.add(Calendar.MONTH, -1);
                    days += temp.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
                if (months < 0) {
                    years--;
                    months += 12;
                }

                // Total days
                long totalDays = (now.getTimeInMillis() - birth.getTimeInMillis()) / (1000 * 60 * 60 * 24);

                // Format age string
                String usiaStr = String.format(Locale.getDefault(),
                        "<b><font color='#B8D1E3'>%d Tahun</font></b><br>" +
                        "<b><font color='#B8D1E3'>%d Bulan</font></b><br>" +
                        "<b><font color='#B8D1E3'>%d Hari</font></b><br><br>" +
                        "<font color='#B0B0B0'>%,d Hari</font>",
                        years, months, days, totalDays);
                tvUsia.setText(android.text.Html.fromHtml(usiaStr));

                // Next birthday
                Calendar nextBirthday = (Calendar) birth.clone();
                nextBirthday.set(Calendar.YEAR, now.get(Calendar.YEAR));
                if (now.after(nextBirthday) || now.equals(nextBirthday)) {
                    nextBirthday.add(Calendar.YEAR, 1);
                }
                long millisToNext = nextBirthday.getTimeInMillis() - now.getTimeInMillis();
                long daysToNextLong = millisToNext / (1000 * 60 * 60 * 24);

                // Calculate months and days to next birthday
                int monthsToNext = nextBirthday.get(Calendar.MONTH) - now.get(Calendar.MONTH);
                int daysToNext = nextBirthday.get(Calendar.DAY_OF_MONTH) - now.get(Calendar.DAY_OF_MONTH);
                if (daysToNext < 0) {
                    monthsToNext--;
                    Calendar temp = (Calendar) nextBirthday.clone();
                    temp.add(Calendar.MONTH, -1);
                    daysToNext += temp.getActualMaximum(Calendar.DAY_OF_MONTH);
                }
                if (monthsToNext < 0) {
                    monthsToNext += 12;
                }

                String nextStr = String.format(Locale.getDefault(),
                        "<b><font color='#B8D1E3'>%d Bulan</font></b><br>" +
                        "<b><font color='#B8D1E3'>%d Hari</font></b><br><br>" +
                        "<font color='#B0B0B0'>%,d Hari</font>",
                        monthsToNext, daysToNext, daysToNextLong
                );
                tvNextBirthday.setText(android.text.Html.fromHtml(nextStr));
            });
            datePicker.show(getSupportFragmentManager(), "DATE_PICKER");
        });

        // Tambahkan inisialisasi tombol clear
        android.widget.ImageButton clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(v -> {
            // Reset tanggal ke hari ini
            btnDate.setText(sdf.format(today.getTime()));
            // Kosongkan hasil usia dan ulang tahun berikutnya
            tvUsia.setText("- -");
            tvNextBirthday.setText("- -");
        });
    }
}