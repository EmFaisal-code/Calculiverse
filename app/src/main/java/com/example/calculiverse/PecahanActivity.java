package com.example.calculiverse;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class PecahanActivity extends AppCompatActivity {
    private EditText etInputA, etInputB;
    private EditText etOutputX, etOutputY;
    private EditText etInputC;
    private EditText etOutputD, etOutputE;
    private TextView tvHasil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pecahan);

        // Inisialisasi views
        etInputA = findViewById(R.id.et_input_a);
        etInputB = findViewById(R.id.et_input_b);
        etOutputX = findViewById(R.id.et_output_x);
        etOutputY = findViewById(R.id.et_output_y);
        etInputC = findViewById(R.id.et_input_c);
        etOutputD = findViewById(R.id.et_output_d);
        etOutputE = findViewById(R.id.et_output_e);
        tvHasil = findViewById(R.id.tv_hasil);

        // Tambahkan listener untuk setiap input
        setupInputListeners();

        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Pecahan");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Aljabar", "Pecahan");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Aljabar", "Pecahan");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });

        ImageButton btnClear = findViewById(R.id.clear_button);
        btnClear.setOnClickListener(v -> {
            etInputA.setText("");
            etInputB.setText("");
            etOutputX.setText("");
            etOutputY.setText("");
            etInputC.setText("");
            etOutputD.setText("");
            etOutputE.setText("");
            tvHasil.setText("");
        });
    }

    private void setupInputListeners() {
        // Listener untuk input A
        etInputA.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { hitungPecahan(); }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        // Listener untuk input B
        etInputB.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { hitungPecahan(); }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
        // Listener untuk desimal ke pecahan
        etInputC.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { desimalKePecahan(); }
            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });
    }

    private void hitungPecahan() {
        // Ambil nilai dari input
        String strA = etInputA.getText().toString();
        String strB = etInputB.getText().toString();

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
                    etOutputX.setText("");
                    etOutputY.setText("");
                    tvHasil.setText("-");
                }
            } catch (NumberFormatException e) {
                etOutputX.setText("");
                etOutputY.setText("");
                tvHasil.setText("-");
            }
        } else {
            etOutputX.setText("");
            etOutputY.setText("");
            tvHasil.setText("");
        }
    }

    // Fungsi untuk desimal ke pecahan
    private void desimalKePecahan() {
        String strC = etInputC.getText().toString();
        if (TextUtils.isEmpty(strC)) {
            if (etOutputD != null) etOutputD.setText("");
            if (etOutputE != null) etOutputE.setText("");
            return;
        }
        try {
            double value = Double.parseDouble(strC);
            int decimalPlaces = getDecimalPlaces(strC);
            int denominator = (int) Math.pow(10, decimalPlaces);
            int numerator = (int) Math.round(value * denominator);
            int gcd = gcd(numerator, denominator);
            numerator /= gcd;
            denominator /= gcd;
            if (etOutputD != null) etOutputD.setText(String.valueOf(numerator));
            if (etOutputE != null) etOutputE.setText(String.valueOf(denominator));
        } catch (Exception e) {
            if (etOutputD != null) etOutputD.setText("");
            if (etOutputE != null) etOutputE.setText("");
        }
    }

    // Fungsi untuk mencari FPB (GCD)
    private int gcd(int a, int b) {
        if (b == 0) return a;
        return gcd(b, a % b);
    }

    // Fungsi untuk menghitung jumlah angka di belakang koma
    private int getDecimalPlaces(String value) {
        int index = value.indexOf('.');
        if (index < 0) return 0;
        return value.length() - index - 1;
    }
}
