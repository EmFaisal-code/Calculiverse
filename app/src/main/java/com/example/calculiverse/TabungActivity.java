package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import java.util.Arrays;
import java.util.List;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class TabungActivity extends AppCompatActivity {
    private MaterialButton btnToggleForm1, btnToggleForm2;
    private EditText etInput1, etInput2;
    private TextView tv1, tv2, tv3, tvOutput1, tvOutput2, tvOutput3;
    private ImageButton clearButton;
    private List<String> options = Arrays.asList("Jari-jari", "Tinggi", "Luas Selimut", "Luas Permukaan", "Volume");
    private int inputIndex1 = 0;
    private int inputIndex2 = 1;
    private static final double PI = Math.PI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tabung);

        btnToggleForm1 = findViewById(R.id.btnToggleForm);
        btnToggleForm2 = findViewById(R.id.btnToggleForm2);
        etInput1 = findViewById(R.id.et_input_1);
        etInput2 = findViewById(R.id.et_input_2);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tvOutput1 = findViewById(R.id.tv_output_1);
        tvOutput2 = findViewById(R.id.tv_output_2);
        tvOutput3 = findViewById(R.id.tv_output_3);
        clearButton = findViewById(R.id.clear_button);

        btnToggleForm1.setOnClickListener(v -> showDropdown(1));
        btnToggleForm2.setOnClickListener(v -> showDropdown(2));
        clearButton.setOnClickListener(v -> clearAll());

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calculateAndShowResult(); }
        };
        etInput1.addTextChangedListener(watcher);
        etInput2.addTextChangedListener(watcher);
        updateForm();
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Tabung");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Ruang", "Tabung");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Ruang", "Tabung");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDropdown(int which) {
        UniversalFormChoiceBottomSheet.show(this, options, (index, option) -> {
            if (which == 1) {
                inputIndex1 = index;
                if (inputIndex1 == inputIndex2) inputIndex2 = (inputIndex2 + 1) % options.size();
            } else {
                inputIndex2 = index;
                if (inputIndex2 == inputIndex1) inputIndex1 = (inputIndex1 + 1) % options.size();
            }
            updateForm();
        });
    }

    private void updateForm() {
        btnToggleForm1.setText(options.get(inputIndex1));
        btnToggleForm2.setText(options.get(inputIndex2));
        etInput1.setText("");
        etInput2.setText("");
        etInput1.setHint("-");
        etInput2.setHint("-");
        clearAllOutputs();
        // Set label output sesuai input
        int[] outIdx = new int[3];
        int idx = 0;
        for (int i = 0; i < options.size(); i++) {
            if (i != inputIndex1 && i != inputIndex2 && idx < 3) {
                outIdx[idx++] = i;
            }
        }
        tv1.setText(options.get(outIdx[0]));
        tv2.setText(options.get(outIdx[1]));
        tv3.setText(options.get(outIdx[2]));
    }

    private void calculateAndShowResult() {
        String s1 = etInput1.getText().toString();
        String s2 = etInput2.getText().toString();
        if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)) {
            clearAllOutputs();
            return;
        }
        try {
            // Urutan: Jari-jari, Tinggi, Luas Selimut, Luas Permukaan, Volume
            double[] values = new double[5];
            boolean valid = false;
            double v1 = Double.parseDouble(s1);
            double v2 = Double.parseDouble(s2);
            int i1 = inputIndex1, i2 = inputIndex2;
            // Kombinasi input
            if ((i1 == 0 && i2 == 1) || (i1 == 1 && i2 == 0)) { // Jari-jari & Tinggi
                double r = (i1 == 0) ? v1 : v2;
                double t = (i1 == 1) ? v1 : v2;
                double luasSelimut = 2 * PI * r * t;
                double luasPermukaan = 2 * PI * r * (r + t);
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 0 && i2 == 2) || (i1 == 2 && i2 == 0)) { // Jari-jari & Luas Selimut
                double r = (i1 == 0) ? v1 : v2;
                double luasSelimut = (i1 == 2) ? v1 : v2;
                double t = luasSelimut / (2 * PI * r);
                double luasPermukaan = 2 * PI * r * (r + t);
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 0 && i2 == 3) || (i1 == 3 && i2 == 0)) { // Jari-jari & Luas Permukaan
                double r = (i1 == 0) ? v1 : v2;
                double luasPermukaan = (i1 == 3) ? v1 : v2;
                double t = (luasPermukaan / (2 * PI * r)) - r;
                double luasSelimut = 2 * PI * r * t;
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 0 && i2 == 4) || (i1 == 4 && i2 == 0)) { // Jari-jari & Volume
                double r = (i1 == 0) ? v1 : v2;
                double volume = (i1 == 4) ? v1 : v2;
                double t = volume / (PI * r * r);
                double luasSelimut = 2 * PI * r * t;
                double luasPermukaan = 2 * PI * r * (r + t);
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 1 && i2 == 2) || (i1 == 2 && i2 == 1)) { // Tinggi & Luas Selimut
                double t = (i1 == 1) ? v1 : v2;
                double luasSelimut = (i1 == 2) ? v1 : v2;
                double r = luasSelimut / (2 * PI * t);
                double luasPermukaan = 2 * PI * r * (r + t);
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 1 && i2 == 3) || (i1 == 3 && i2 == 1)) { // Tinggi & Luas Permukaan
                double t = (i1 == 1) ? v1 : v2;
                double luasPermukaan = (i1 == 3) ? v1 : v2;
                // luasPermukaan = 2πr(r+t) => quadratic in r: 2πr^2 + 2πrt - luasPermukaan = 0
                // r^2 + r*t - (luasPermukaan/(2π)) = 0
                double a = 1;
                double b = t;
                double c = -luasPermukaan / (2 * PI);
                double D = b * b - 4 * a * c;
                if (D < 0) throw new IllegalArgumentException();
                double r = (-b + Math.sqrt(D)) / (2 * a);
                double luasSelimut = 2 * PI * r * t;
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 1 && i2 == 4) || (i1 == 4 && i2 == 1)) { // Tinggi & Volume
                double t = (i1 == 1) ? v1 : v2;
                double volume = (i1 == 4) ? v1 : v2;
                double r = Math.sqrt(volume / (PI * t));
                double luasSelimut = 2 * PI * r * t;
                double luasPermukaan = 2 * PI * r * (r + t);
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 2 && i2 == 3) || (i1 == 3 && i2 == 2)) { // Luas Selimut & Luas Permukaan
                double luasSelimut = (i1 == 2) ? v1 : v2;
                double luasPermukaan = (i1 == 3) ? v1 : v2;
                // luasPermukaan = luasSelimut + 2πr^2
                // luasPermukaan - luasSelimut = 2πr^2
                double r = Math.sqrt((luasPermukaan - luasSelimut) / (2 * PI));
                double t = luasSelimut / (2 * PI * r);
                double volume = PI * r * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 2 && i2 == 4) || (i1 == 4 && i2 == 2)) { // Luas Selimut & Volume
                double luasSelimut = (i1 == 2) ? v1 : v2;
                double volume = (i1 == 4) ? v1 : v2;
                // volume = πr^2t, luasSelimut = 2πrt
                // t = luasSelimut/(2πr)
                // volume = πr^2 * (luasSelimut/(2πr)) = (r*luasSelimut)/2
                // r = 2*volume/luasSelimut
                double r = 2 * volume / luasSelimut;
                double t = luasSelimut / (2 * PI * r);
                double luasPermukaan = 2 * PI * r * (r + t);
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            } else if ((i1 == 3 && i2 == 4) || (i1 == 4 && i2 == 3)) { // Luas Permukaan & Volume
                double luasPermukaan = (i1 == 3) ? v1 : v2;
                double volume = (i1 == 4) ? v1 : v2;
                // luasPermukaan = 2πr(r+t), volume = πr^2t
                // t = volume/(πr^2)
                // luasPermukaan = 2πr(r+volume/(πr^2)) = 2πr^2 + 2volume/r
                // 2πr^2 + 2volume/r - luasPermukaan = 0
                // 2πr^3 - luasPermukaan*r + 2volume = 0
                // Cubic equation, solve numerically (Newton-Raphson)
                double r = solveRadiusFromPermukaanAndVolume(luasPermukaan, volume);
                double t = volume / (PI * r * r);
                double luasSelimut = 2 * PI * r * t;
                values[0] = r; values[1] = t; values[2] = luasSelimut; values[3] = luasPermukaan; values[4] = volume;
                valid = true;
            }
            if (valid) {
                int[] outIdx = new int[3];
                int idx = 0;
                for (int i = 0; i < options.size(); i++) {
                    if (i != inputIndex1 && i != inputIndex2 && idx < 3) {
                        outIdx[idx++] = i;
                    }
                }
                tvOutput1.setText(formatNumber(values[outIdx[0]]));
                tvOutput2.setText(formatNumber(values[outIdx[1]]));
                tvOutput3.setText(formatNumber(values[outIdx[2]]));
            } else {
                clearAllOutputs();
            }
        } catch (Exception e) {
            clearAllOutputs();
        }
    }

    // Newton-Raphson method for cubic: 2πr^3 - luasPermukaan*r + 2volume = 0
    private double solveRadiusFromPermukaanAndVolume(double luasPermukaan, double volume) {
        double r = 1.0;
        for (int i = 0; i < 100; i++) {
            double f = 2 * PI * r * r * r - luasPermukaan * r + 2 * volume;
            double df = 6 * PI * r * r - luasPermukaan;
            if (Math.abs(df) < 1e-8) break;
            double rNext = r - f / df;
            if (Math.abs(rNext - r) < 1e-8) return rNext;
            r = rNext;
        }
        return r;
    }

    private void clearAll() {
        etInput1.setText("");
        etInput2.setText("");
        clearAllOutputs();
    }
    private void clearAllOutputs() {
        tvOutput1.setText("--");
        tvOutput2.setText("--");
        tvOutput3.setText("--");
    }
    private String formatNumber(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "--";
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%.4f", value);
    }
}