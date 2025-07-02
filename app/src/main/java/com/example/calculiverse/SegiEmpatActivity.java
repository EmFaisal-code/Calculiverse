package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

public class SegiEmpatActivity extends AppCompatActivity {
    private EditText etInput1, etInput2;
    private TextView tvOutput1, tvOutput2;
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segi_empat);

        etInput1 = findViewById(R.id.et_input_1);
        etInput2 = findViewById(R.id.et_input_2);
        tvOutput1 = findViewById(R.id.tv_output_1);
        tvOutput2 = findViewById(R.id.tv_output_2);
        clearButton = findViewById(R.id.clear_button);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calculateAndShowResult(); }
        };
        etInput1.addTextChangedListener(watcher);
        etInput2.addTextChangedListener(watcher);
        clearButton.setOnClickListener(v -> clearAll());
        clearAll();
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Segiempat");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Datar", "Segiempat");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Datar", "Segiempat");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAndShowResult() {
        String s1 = etInput1.getText().toString();
        String s2 = etInput2.getText().toString();
        if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)) {
            tvOutput1.setText("--");
            tvOutput2.setText("--");
            return;
        }
        try {
            double panjang = Double.parseDouble(s1);
            double lebar = Double.parseDouble(s2);
            double luas = panjang * lebar;
            double keliling = 2 * (panjang + lebar);
            tvOutput1.setText(formatNumber(luas));
            tvOutput2.setText(formatNumber(keliling));
        } catch (Exception e) {
            tvOutput1.setText("--");
            tvOutput2.setText("--");
        }
    }

    private void clearAll() {
        etInput1.setText("");
        etInput2.setText("");
        tvOutput1.setText("--");
        tvOutput2.setText("--");
    }
    private String formatNumber(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "--";
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%.4f", value);
    }
}