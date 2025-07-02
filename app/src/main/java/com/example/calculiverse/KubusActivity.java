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

public class KubusActivity extends AppCompatActivity {
    private MaterialButton btnToggleForm;
    private EditText etInput;
    private TextView tv1, tv2, tv3, tvOutput1, tvOutput2, tvOutput3;
    private ImageButton clearButton;
    private List<String> options = Arrays.asList("Sisi", "Luas Total", "Luas Permukaan", "Volume");
    private int inputIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_kubus);

        btnToggleForm = findViewById(R.id.btnToggleForm);
        etInput = findViewById(R.id.et_input_1);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tvOutput1 = findViewById(R.id.tv_output_1);
        tvOutput2 = findViewById(R.id.tv_output_2);
        tvOutput3 = findViewById(R.id.tv_output_3);
        clearButton = findViewById(R.id.clear_button);

        btnToggleForm.setOnClickListener(v -> showUniversalBottomSheet());
        clearButton.setOnClickListener(v -> clearAll());

        etInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { calculateAndShowResult(); }
        });
        updateForm();
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Kubus");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Ruang", "Kubus");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Ruang", "Kubus");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUniversalBottomSheet() {
        UniversalFormChoiceBottomSheet.show(this, options, (index, option) -> {
            inputIndex = index;
            updateForm();
        });
    }

    private void updateForm() {
        btnToggleForm.setText(options.get(inputIndex));
        etInput.setText("");
        etInput.setHint("-");
        clearAllOutputs();
        // Set label output sesuai input
        int out1 = (inputIndex + 1) % 4;
        int out2 = (inputIndex + 2) % 4;
        int out3 = (inputIndex + 3) % 4;
        tv1.setText(options.get(out1));
        tv2.setText(options.get(out2));
        tv3.setText(options.get(out3));
    }

    private void calculateAndShowResult() {
        String s = etInput.getText().toString();
        if (TextUtils.isEmpty(s)) {
            clearAllOutputs();
            return;
        }
        try {
            double sisi = 0, luasTotal = 0, luasPermukaan = 0, volume = 0;
            switch (inputIndex) {
                case 0: // Sisi
                    sisi = Double.parseDouble(s);
                    luasTotal = 6 * sisi * sisi;
                    luasPermukaan = luasTotal;
                    volume = sisi * sisi * sisi;
                    break;
                case 1: // Luas Total
                    luasTotal = Double.parseDouble(s);
                    sisi = Math.sqrt(luasTotal / 6);
                    luasPermukaan = luasTotal;
                    volume = sisi * sisi * sisi;
                    break;
                case 2: // Luas Permukaan
                    luasPermukaan = Double.parseDouble(s);
                    sisi = Math.sqrt(luasPermukaan / 6);
                    luasTotal = luasPermukaan;
                    volume = sisi * sisi * sisi;
                    break;
                case 3: // Volume
                    volume = Double.parseDouble(s);
                    sisi = Math.cbrt(volume);
                    luasTotal = 6 * sisi * sisi;
                    luasPermukaan = luasTotal;
                    break;
            }
            double[] values = {sisi, luasTotal, luasPermukaan, volume};
            int out1 = (inputIndex + 1) % 4;
            int out2 = (inputIndex + 2) % 4;
            int out3 = (inputIndex + 3) % 4;
            tvOutput1.setText(formatNumber(values[out1]));
            tvOutput2.setText(formatNumber(values[out2]));
            tvOutput3.setText(formatNumber(values[out3]));
        } catch (Exception e) {
            clearAllOutputs();
        }
    }

    private void clearAll() {
        etInput.setText("");
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