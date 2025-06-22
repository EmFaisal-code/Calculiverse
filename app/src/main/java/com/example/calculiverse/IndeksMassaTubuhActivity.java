package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class IndeksMassaTubuhActivity extends AppCompatActivity {
    private EditText etTinggi, etBerat;
    private LinearLayout layoutHasil;
    private TextView textNilaiIMT, textStatus, textRange;
    private View barUnderweight, barNormal, barOverweight, barObese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_indeks_massa_tubuh);

        etTinggi = findViewById(R.id.et_tinggi);
        etBerat = findViewById(R.id.et_berat);
        layoutHasil = findViewById(R.id.layoutHasil);
        textNilaiIMT = findViewById(R.id.textNilaiIMT);
        textStatus = findViewById(R.id.textStatus);
        textRange = findViewById(R.id.textRange);
        barUnderweight = findViewById(R.id.bar_underweight);
        barNormal = findViewById(R.id.bar_normal);
        barOverweight = findViewById(R.id.bar_overweight);
        barObese = findViewById(R.id.bar_obese);

        ImageButton clearButton = findViewById(R.id.clear_button);
        clearButton.setOnClickListener(v -> {
            etTinggi.setText("");
            etBerat.setText("");
        });

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                hitungIMT();
            }
        };
        etTinggi.addTextChangedListener(watcher);
        etBerat.addTextChangedListener(watcher);

        // Set initial state
        hitungIMT();
    }

    private void hitungIMT() {
        String tinggiStr = etTinggi.getText().toString();
        String beratStr = etBerat.getText().toString();
        resetBarColors();

        if (tinggiStr.isEmpty() || beratStr.isEmpty()) {
            textNilaiIMT.setText("--");
            textStatus.setText("");
            textRange.setText("");
            return;
        }

        try {
            double tinggi = Double.parseDouble(tinggiStr) / 100.0; // cm ke meter
            double berat = Double.parseDouble(beratStr);

            if (tinggi <= 0 || berat <= 0) {
                textNilaiIMT.setText("--");
                textStatus.setText("");
                textRange.setText("");
                return;
            }

            double imt = berat / (tinggi * tinggi);
            textNilaiIMT.setText(String.format("%.2f", imt));

            if (imt < 18.5) {
                textStatus.setText("Berat kurang");
                textRange.setText("IMT < 18.5");
                barUnderweight.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_underweight));
            } else if (imt < 25) {
                textStatus.setText("Berat normal");
                textRange.setText("IMT = 18.5 - 24.9");
                barNormal.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_normal));
            } else if (imt < 30) {
                textStatus.setText("Kelebihan berat");
                textRange.setText("IMT = 25 - 29.9");
                barOverweight.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_overweight));
            } else {
                textStatus.setText("Kegemukan");
                textRange.setText("IMT > 30");
                barObese.setBackgroundColor(ContextCompat.getColor(this, R.color.bar_obese));
            }

        } catch (NumberFormatException e) {
            textNilaiIMT.setText("--");
            textStatus.setText("");
            textRange.setText("");
        }
    }

    private void resetBarColors() {
        int defaultColor = ContextCompat.getColor(this, R.color.bar_default);
        barUnderweight.setBackgroundColor(defaultColor);
        barNormal.setBackgroundColor(defaultColor);
        barOverweight.setBackgroundColor(defaultColor);
        barObese.setBackgroundColor(defaultColor);
    }
}