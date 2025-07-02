package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class HargaSatuanActivity extends AppCompatActivity {
    private EditText[] etKuantitas;
    private EditText[] etHarga;
    private TextView[] tvHasil;
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harga_satuan);

        // Inisialisasi array untuk EditText dan TextView
        etKuantitas = new EditText[]{
            findViewById(R.id.et_input_kuantitas_2),
            findViewById(R.id.et_input_kuantitas_3),
            findViewById(R.id.et_input_kuantitas_4),
            findViewById(R.id.et_input_kuantitas_5)
        };

        etHarga = new EditText[]{
            findViewById(R.id.et_input_harga_2),
            findViewById(R.id.et_input_harga_3),
            findViewById(R.id.et_input_harga_4),
            findViewById(R.id.et_input_harga_5)
        };

        tvHasil = new TextView[]{
            findViewById(R.id.tv_output_hasil_2),
            findViewById(R.id.tv_output_hasil_3),
            findViewById(R.id.tv_output_hasil_4),
            findViewById(R.id.tv_output_hasil_5)
        };

        clearButton = findViewById(R.id.clear_button);

        // Menambahkan TextWatcher untuk setiap EditText
        for (int i = 0; i < etKuantitas.length; i++) {
            final int index = i;
            TextWatcher textWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    calculateResult(index);
                }
            };

            etKuantitas[i].addTextChangedListener(textWatcher);
            etHarga[i].addTextChangedListener(textWatcher);
        }

        // Menambahkan onClick listener untuk tombol clear
        clearButton.setOnClickListener(v -> clearAllFields());
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Harga satuan");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Keuangan", "Harga satuan");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Keuangan", "Harga satuan");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateResult(int index) {
        String kuantitasStr = etKuantitas[index].getText().toString();
        String hargaStr = etHarga[index].getText().toString();

        // Jika salah satu field kosong, hasil juga kosong
        if (kuantitasStr.isEmpty() || hargaStr.isEmpty()) {
            tvHasil[index].setText("");
            return;
        }

        try {
            double kuantitas = Double.parseDouble(kuantitasStr);
            double harga = Double.parseDouble(hargaStr);

            // Menghitung harga satuan
            double hasil = harga / kuantitas;
            
            // Format hasil dengan 2 angka desimal
            String formattedResult = String.format("%.2f", hasil);
            tvHasil[index].setText(formattedResult);
        } catch (NumberFormatException e) {
            tvHasil[index].setText("");
        }
    }

    private void clearAllFields() {
        for (int i = 0; i < etKuantitas.length; i++) {
            etKuantitas[i].setText("");
            etHarga[i].setText("");
            tvHasil[i].setText("");
        }
    }
}