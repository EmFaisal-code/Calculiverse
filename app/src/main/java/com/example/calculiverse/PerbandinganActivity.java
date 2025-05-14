package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PerbandinganActivity extends AppCompatActivity {
    private EditText etInputA, etInputX, etOutputB, etOutputY;
    private EditText etInputC, etInputE, etOutputD, etOutputF;
    private boolean isCalculating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perbandingan);

        // Inisialisasi view untuk berbanding terbalik
        etInputA = findViewById(R.id.et_input_a);
        etInputX = findViewById(R.id.et_input_x);
        etOutputB = findViewById(R.id.et_output_b);
        etOutputY = findViewById(R.id.et_output_y);

        // Inisialisasi view untuk berbanding lurus
        etInputC = findViewById(R.id.et_input_c);
        etInputE = findViewById(R.id.et_input_e);
        etOutputD = findViewById(R.id.et_output_d);
        etOutputF = findViewById(R.id.et_output_f);

        // Inisialisasi tombol clear
        Button clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(v -> clearAllFields());

        // Setup TextWatcher untuk berbanding terbalik
        TextWatcher watcherInverse = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (isCalculating) return;
                calculateInverse();
            }
        };
        etInputA.addTextChangedListener(watcherInverse);
        etInputX.addTextChangedListener(watcherInverse);
        etOutputB.addTextChangedListener(watcherInverse);
        etOutputY.addTextChangedListener(watcherInverse);

        // Setup TextWatcher untuk berbanding lurus
        TextWatcher watcherDirect = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (isCalculating) return;
                calculateDirect();
            }
        };
        etInputC.addTextChangedListener(watcherDirect);
        etInputE.addTextChangedListener(watcherDirect);
        etOutputD.addTextChangedListener(watcherDirect);
        etOutputF.addTextChangedListener(watcherDirect);
    }

    private void clearAllFields() {
        isCalculating = true;
        etInputA.setText("");
        etInputX.setText("");
        etOutputB.setText("");
        etOutputY.setText("");
        etInputC.setText("");
        etInputE.setText("");
        etOutputD.setText("");
        etOutputF.setText("");
        isCalculating = false;
    }

    private void calculateInverse() {
        isCalculating = true;
        String aStr = etInputA.getText().toString();
        String xStr = etInputX.getText().toString();
        String bStr = etOutputB.getText().toString();
        String yStr = etOutputY.getText().toString();

        int filled = 0;
        if (!aStr.isEmpty()) filled++;
        if (!xStr.isEmpty()) filled++;
        if (!bStr.isEmpty()) filled++;
        if (!yStr.isEmpty()) filled++;

        // Kosongkan field output jika input berubah
        if (filled < 3) {
            if (aStr.isEmpty()) etInputA.setText("");
            if (xStr.isEmpty()) etInputX.setText("");
            if (bStr.isEmpty()) etOutputB.setText("");
            if (yStr.isEmpty()) etOutputY.setText("");
            isCalculating = false;
            return;
        }

        try {
            if (aStr.isEmpty()) {
                double x = Double.parseDouble(xStr);
                double b = Double.parseDouble(bStr);
                double y = Double.parseDouble(yStr);
                double a = (y * b) / x;
                etInputA.setText(formatResult(a));
            } else if (xStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double b = Double.parseDouble(bStr);
                double y = Double.parseDouble(yStr);
                double x = (y * b) / a;
                etInputX.setText(formatResult(x));
            } else if (bStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double x = Double.parseDouble(xStr);
                double y = Double.parseDouble(yStr);
                double b = (x * y) / a;
                etOutputB.setText(formatResult(b));
            } else if (yStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double x = Double.parseDouble(xStr);
                double b = Double.parseDouble(bStr);
                double y = (a * b) / x;
                etOutputY.setText(formatResult(y));
            }
        } catch (Exception e) {
            // ignore error
        }
        isCalculating = false;
    }

    private void calculateDirect() {
        isCalculating = true;
        String aStr = etInputC.getText().toString();
        String xStr = etInputE.getText().toString();
        String bStr = etOutputD.getText().toString();
        String yStr = etOutputF.getText().toString();

        int filled = 0;
        if (!aStr.isEmpty()) filled++;
        if (!xStr.isEmpty()) filled++;
        if (!bStr.isEmpty()) filled++;
        if (!yStr.isEmpty()) filled++;

        // Kosongkan field output jika input berubah
        if (filled < 3) {
            if (aStr.isEmpty()) etInputC.setText("");
            if (xStr.isEmpty()) etInputE.setText("");
            if (bStr.isEmpty()) etOutputD.setText("");
            if (yStr.isEmpty()) etOutputF.setText("");
            isCalculating = false;
            return;
        }

        try {
            if (aStr.isEmpty()) {
                double x = Double.parseDouble(xStr);
                double b = Double.parseDouble(bStr);
                double y = Double.parseDouble(yStr);
                double a = (b * x) / y;
                etInputC.setText(formatResult(a));
            } else if (xStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double b = Double.parseDouble(bStr);
                double y = Double.parseDouble(yStr);
                double x = (a * y) / b;
                etInputE.setText(formatResult(x));
            } else if (bStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double x = Double.parseDouble(xStr);
                double y = Double.parseDouble(yStr);
                double b = (a * x) / y;
                etOutputD.setText(formatResult(b));
            } else if (yStr.isEmpty()) {
                double a = Double.parseDouble(aStr);
                double x = Double.parseDouble(xStr);
                double b = Double.parseDouble(bStr);
                double y = (b * x) / a;
                etOutputF.setText(formatResult(y));
            }
        } catch (Exception e) {
            // ignore error
        }
        isCalculating = false;
    }

    private String formatResult(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%.2f", value);
    }
}