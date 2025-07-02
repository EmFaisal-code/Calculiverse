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
import androidx.room.Room;

public class LingkaranActivity extends AppCompatActivity {
    private MaterialButton btnToggleForm;
    private EditText etInput;
    private TextView tv1, tv2, tv3, tvOutput1, tvOutput2, tvOutput3;
    private ImageButton clearButton;
    private List<String> options = Arrays.asList("Jari-jari", "Diameter", "Luas", "Keliling");
    private int inputIndex = 0;
    private static final double PI = Math.PI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lingkaran);

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
            boolean isFavorit = db.favoritDao().isFavorit("Lingkaran");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Datar", "Lingkaran");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Datar", "Lingkaran");
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
            double r = 0, d = 0, luas = 0, keliling = 0;
            switch (inputIndex) {
                case 0: // Jari-jari
                    r = Double.parseDouble(s);
                    d = 2 * r;
                    luas = PI * r * r;
                    keliling = 2 * PI * r;
                    break;
                case 1: // Diameter
                    d = Double.parseDouble(s);
                    r = d / 2.0;
                    luas = PI * r * r;
                    keliling = 2 * PI * r;
                    break;
                case 2: // Luas
                    luas = Double.parseDouble(s);
                    r = Math.sqrt(luas / PI);
                    d = 2 * r;
                    keliling = 2 * PI * r;
                    break;
                case 3: // Keliling
                    keliling = Double.parseDouble(s);
                    r = keliling / (2 * PI);
                    d = 2 * r;
                    luas = PI * r * r;
                    break;
            }
            // Output urut: (inputIndex+1)%4, (inputIndex+2)%4, (inputIndex+3)%4
            double[] values = {r, d, luas, keliling};
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