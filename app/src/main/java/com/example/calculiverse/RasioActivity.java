package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RasioActivity extends AppCompatActivity {
    private EditText etInputA, etInputB, etOutputX, etOutputY;
    private EditText etInputC, etInputD, etOutputE, etOutputF;
    private TextView tvMetode1, tvMetode2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_rasio);

        // Inisialisasi views
        etInputA = findViewById(R.id.et_input_a);
        etInputB = findViewById(R.id.et_input_b);
        etOutputX = findViewById(R.id.et_output_x);
        etOutputY = findViewById(R.id.et_output_y);
        etInputC = findViewById(R.id.et_input_c);
        etInputD = findViewById(R.id.et_input_d);
        etOutputE = findViewById(R.id.et_output_e);
        etOutputF = findViewById(R.id.et_output_f);
        tvMetode1 = findViewById(R.id.tv_metode_1);
        tvMetode2 = findViewById(R.id.tv_metode_2);

        ImageButton btnInfo1 = findViewById(R.id.button_info_metode_1);
        ImageButton btnInfo2 = findViewById(R.id.button_info_metode_2);
        ImageButton clearButton = findViewById(R.id.clear_button);

        // Setup tombol info
        btnInfo1.setOnClickListener(v -> {
            tvMetode1.setText("Rasio adalah perbandingan antara dua nilai yang menunjukkan seberapa besar satu nilai dibandingkan nilai lainnya.");
        });

        btnInfo2.setOnClickListener(v -> {
            tvMetode2.setText("Untuk rasio 4:3, jika salah satu nilainya 800 maka nilai lainnya adalah 600");
        });

        // Setup clear button
        clearButton.setOnClickListener(v -> {
            clearAllFields();
        });

        // Setup TextWatcher untuk bagian reduksi (rasio)
        TextWatcher reduksiWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateReduksi();
            }
        };

        etInputA.addTextChangedListener(reduksiWatcher);
        etInputB.addTextChangedListener(reduksiWatcher);

        // Setup TextWatcher untuk bagian deduksi (nilai yang hilang)
        TextWatcher deduksiWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                calculateDeduksi();
            }
        };

        etInputC.addTextChangedListener(deduksiWatcher);
        etInputD.addTextChangedListener(deduksiWatcher);
        etOutputE.addTextChangedListener(deduksiWatcher);
    }

    private void calculateReduksi() {
        String inputAStr = etInputA.getText().toString();
        String inputBStr = etInputB.getText().toString();

        if (!inputAStr.isEmpty() && !inputBStr.isEmpty()) {
            try {
                double a = Double.parseDouble(inputAStr);
                double b = Double.parseDouble(inputBStr);

                // Hitung rasio
                double gcd = findGCD(a, b);
                double ratioA = a / gcd;
                double ratioB = b / gcd;
                
                // Format hasil
                String formattedRatioA = formatNumber(ratioA);
                String formattedRatioB = formatNumber(ratioB);
                
                // Tampilkan hasil pada field X dan Y
                etOutputX.setText(formattedRatioA);
                etOutputY.setText(formattedRatioB);
            } catch (NumberFormatException e) {
                // Handle error jika input bukan angka
            }
        }
    }

    private void calculateDeduksi() {
        String inputCStr = etInputC.getText().toString(); // X
        String inputDStr = etInputD.getText().toString(); // Y
        String outputEStr = etOutputE.getText().toString(); // A
        String outputFStr = etOutputF.getText().toString(); // B

        try {
            // Cek apakah kedua rasio terisi
            if (!inputCStr.isEmpty() && !inputDStr.isEmpty()) {
                double x = Double.parseDouble(inputCStr);
                double y = Double.parseDouble(inputDStr);

                // Jika nilai A terisi dan B kosong, hitung B
                if (!outputEStr.isEmpty() && outputFStr.isEmpty()) {
                    double a = Double.parseDouble(outputEStr);
                    double b = (a * y) / x;
                    etOutputF.setText(formatNumber(b));
                }
                // Jika nilai B terisi dan A kosong, hitung A
                else if (outputEStr.isEmpty() && !outputFStr.isEmpty()) {
                    double b = Double.parseDouble(outputFStr);
                    double a = (b * x) / y;
                    etOutputE.setText(formatNumber(a));
                }
            }
        } catch (NumberFormatException e) {
            // Handle error jika input bukan angka
        }
    }

    private double findGCD(double a, double b) {
        a = Math.abs(a);
        b = Math.abs(b);
        while (b > 0) {
            double temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private String formatNumber(double number) {
        if (number == Math.floor(number)) {
            // Jika bilangan bulat, tampilkan tanpa desimal
            return String.format("%.0f", number);
        } else {
            // Jika desimal, tampilkan 2 angka di belakang koma
            return String.format("%.2f", number);
        }
    }

    private void clearAllFields() {
        etInputA.setText("");
        etInputB.setText("");
        etOutputX.setText("");
        etOutputY.setText("");
        etInputC.setText("");
        etInputD.setText("");
        etOutputE.setText("");
        etOutputF.setText("");
        tvMetode1.setText("Temukan rasio dua nilai");
        tvMetode2.setText("Temukan nilai yang hilang berdasarkan rasio");
    }
}