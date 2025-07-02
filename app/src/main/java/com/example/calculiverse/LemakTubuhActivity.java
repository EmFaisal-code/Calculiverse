package com.example.calculiverse;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import com.google.android.material.button.MaterialButton;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import androidx.room.Room;

import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageButton;
import android.text.TextWatcher;
import android.text.Editable;
import android.widget.Toast;

public class LemakTubuhActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lemak_tubuh);


        MaterialButton btnGender = findViewById(R.id.btn_gender);
        EditText etTinggi = findViewById(R.id.et_tinggi);
        EditText etBerat = findViewById(R.id.et_berat);
        EditText etUsia = findViewById(R.id.et_usia);
        TextView tvHasil = findViewById(R.id.tv_hasil);
        ImageButton clearButton = findViewById(R.id.clear_button);

        // Default: Pria
        btnGender.setText("Pria");
        btnGender.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_gender));
        btnGender.setIconTint(null);

        btnGender.setOnClickListener(v -> {
            String current = btnGender.getText().toString();
            if (current.equals("Pria")) {
                btnGender.setText("Perempuan");
                btnGender.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_gender));
                btnGender.setIconTint(null);
            } else {
                btnGender.setText("Pria");
                btnGender.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_gender));
                btnGender.setIconTint(null);
            }
            hitungLemakTubuh(etTinggi, etBerat, etUsia, btnGender, tvHasil);
        });

        clearButton.setOnClickListener(v -> {
            etTinggi.setText("");
            etBerat.setText("");
            etUsia.setText("");
            tvHasil.setText("- -");
            btnGender.setText("Pria");
            btnGender.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_gender));
            btnGender.setIconTint(null);
        });

        // Hitung otomatis saat input berubah
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Cek jika semua input terisi, langsung hitung
                String tinggiStr = etTinggi.getText().toString();
                String beratStr = etBerat.getText().toString();
                String usiaStr = etUsia.getText().toString();
                if (!tinggiStr.isEmpty() && !beratStr.isEmpty() && !usiaStr.isEmpty()) {
                    hitungLemakTubuh(etTinggi, etBerat, etUsia, btnGender, tvHasil);
                } else {
                    tvHasil.setText("- -");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        };
        etTinggi.addTextChangedListener(watcher);
        etBerat.addTextChangedListener(watcher);
        etUsia.addTextChangedListener(watcher);

        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Lemak tubuh");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Kesehatan", "Lemak tubuh");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Kesehatan", "Lemak tubuh");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitungLemakTubuh(EditText etTinggi, EditText etBerat, EditText etUsia, MaterialButton btnGender, TextView tvHasil) {
        String tinggiStr = etTinggi.getText().toString();
        String beratStr = etBerat.getText().toString();
        String usiaStr = etUsia.getText().toString();
        String gender = btnGender.getText().toString();

        if (tinggiStr.isEmpty() || beratStr.isEmpty() || usiaStr.isEmpty()) {
            tvHasil.setText("- -");
            return;
        }
        try {
            double tinggi = Double.parseDouble(tinggiStr) / 100.0; // cm ke meter
            double berat = Double.parseDouble(beratStr);
            int usia = Integer.parseInt(usiaStr);
            if (tinggi <= 0 || berat <= 0 || usia <= 0) {
                tvHasil.setText("- -");
                return;
            }
            double bmi = berat / (tinggi * tinggi);
            double persenLemak;
            if (gender.equals("Pria")) {
                persenLemak = 1.20 * bmi + 0.23 * usia - 16.2;
            } else {
                persenLemak = 1.20 * bmi + 0.23 * usia - 5.4;
            }
            tvHasil.setText(String.format("%.1f", persenLemak));
        } catch (Exception e) {
            tvHasil.setText("- -");
        }
    }
}