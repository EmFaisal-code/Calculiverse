package com.example.calculiverse;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RataRataActivity extends AppCompatActivity {
    private EditText etInputA, etInputB;
    private TextView tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rata_rata);

        etInputA = findViewById(R.id.et_input_a);
        etInputB = findViewById(R.id.et_input_b);
        tvHasil = findViewById(R.id.tv_hasil);

        // Tambahkan TextWatcher ke kedua input
        etInputA.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungRataRata();
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        etInputB.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungRataRata();
            }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void hitungRataRata() {
        String inputA = etInputA.getText().toString().trim();
        String inputB = etInputB.getText().toString().trim();

        if (TextUtils.isEmpty(inputA) || TextUtils.isEmpty(inputB)) {
            tvHasil.setText("");
            return;
        }

        try {
            double angkaA = Double.parseDouble(inputA);
            double angkaB = Double.parseDouble(inputB);
            double rataRata = (angkaA + angkaB) / 2.0;
            tvHasil.setText(String.format(" %.2f", rataRata));
        } catch (NumberFormatException e) {
            tvHasil.setText("");
        }
    }
} 