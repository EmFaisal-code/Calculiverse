package com.example.calculiverse;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class PecahanActivity extends AppCompatActivity {
    private EditText etInputA, etInputB;
    private TextView etOutputX, etOutputY, tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecahan);

        // Inisialisasi views
        etInputA = findViewById(R.id.et_input_a);
        etInputB = findViewById(R.id.et_input_b);
        etOutputX = findViewById(R.id.et_output_x);
        etOutputY = findViewById(R.id.et_output_y);
        tvHasil = findViewById(R.id.tv_hasil);

        // Tambahkan listener untuk setiap input
        setupInputListeners();
    }

    private void setupInputListeners() {
        // Listener untuk input A
        etInputA.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungPecahan();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Listener untuk input B
        etInputB.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungPecahan();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Listener untuk input X
        etOutputX.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungPecahan();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Listener untuk input Y
        etOutputY.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hitungPecahan();
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void hitungPecahan() {
        // Ambil nilai dari input
        String strA = etInputA.getText().toString();
        String strB = etInputB.getText().toString();
        String strX = etOutputX.getText().toString();
        String strY = etOutputY.getText().toString();

        // Jika X dan Y sudah diisi, tampilkan sebagai pecahan
        if (!TextUtils.isEmpty(strX) && !TextUtils.isEmpty(strY)) {
            tvHasil.setText(strX + "/" + strY);
            return;
        }

        // Jika input A dan B diisi, lakukan perhitungan
        if (!TextUtils.isEmpty(strA) && !TextUtils.isEmpty(strB)) {
            try {
                double a = Double.parseDouble(strA);
                double b = Double.parseDouble(strB);
                if (b != 0) {
                    // Sederhanakan pecahan
                    int pembilang = (int) a;
                    int penyebut = (int) b;
                    int gcd = gcd(pembilang, penyebut);
                    pembilang /= gcd;
                    penyebut /= gcd;
                    etOutputX.setText(String.valueOf(pembilang));
                    etOutputY.setText(String.valueOf(penyebut));
                    tvHasil.setText(pembilang + "/" + penyebut);
                } else {
                    tvHasil.setText("-");
                }
            } catch (NumberFormatException e) {
                tvHasil.setText("-");
            }
        } else {
            tvHasil.setText("");
        }
    }

    // Fungsi untuk mencari FPB (GCD)
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }
}
