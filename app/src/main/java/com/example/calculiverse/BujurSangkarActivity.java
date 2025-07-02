package com.example.calculiverse;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.button.MaterialButton;
import java.util.Arrays;
import java.util.List;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.TextUtils;
import android.widget.ImageButton;
import android.widget.Toast;

public class BujurSangkarActivity extends AppCompatActivity {
    private MaterialButton btnToggleForm;
    private EditText etInput;
    private TextView tv1, tv2, tvOutput1, tvOutput2;
    private List<String> options = Arrays.asList("sisi", "Luas", "Perimeter");
    private int inputIndex = 0; // 0: sisi, 1: luas, 2: perimeter
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bujur_sangkar);

        btnToggleForm = findViewById(R.id.btnToggleForm);
        etInput = findViewById(R.id.et_input);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tvOutput1 = findViewById(R.id.tv_output_1);
        tvOutput2 = findViewById(R.id.tv_output_2);
        clearButton = findViewById(R.id.clear_button);

        btnToggleForm.setOnClickListener(v -> showUniversalBottomSheet());
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                calculateAndShowResult();
            }
        });
        clearButton.setOnClickListener(v -> {
            etInput.setText("");
            tvOutput1.setText("--");
            tvOutput2.setText("--");
        });
        updateForm();
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Bujur sangkar");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Datar", "Bujur sangkar");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Datar", "Bujur sangkar");
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
        int out1 = (inputIndex + 1) % 3;
        int out2 = (inputIndex + 2) % 3;
        tv1.setText(options.get(out1));
        tv2.setText(options.get(out2));
        tvOutput1.setText("--");
        tvOutput2.setText("--");
        etInput.setText("");
    }

    private void calculateAndShowResult() {
        String inputStr = etInput.getText().toString();
        if (TextUtils.isEmpty(inputStr)) {
            tvOutput1.setText("--");
            tvOutput2.setText("--");
            return;
        }
        try {
            double input = Double.parseDouble(inputStr);
            double sisi = 0, luas = 0, perimeter = 0;
            if (inputIndex == 0) { // sisi
                sisi = input;
                luas = sisi * sisi;
                perimeter = 4 * sisi;
                tvOutput1.setText(formatNumber(luas));
                tvOutput2.setText(formatNumber(perimeter));
            } else if (inputIndex == 1) { // luas
                luas = input;
                if (luas < 0) throw new IllegalArgumentException();
                sisi = Math.sqrt(luas);
                perimeter = 4 * sisi;
                tvOutput1.setText(formatNumber(sisi));
                tvOutput2.setText(formatNumber(perimeter));
            } else if (inputIndex == 2) { // perimeter
                perimeter = input;
                sisi = perimeter / 4.0;
                luas = sisi * sisi;
                tvOutput1.setText(formatNumber(sisi));
                tvOutput2.setText(formatNumber(luas));
            }
        } catch (Exception e) {
            tvOutput1.setText("--");
            tvOutput2.setText("--");
        }
    }

    private String formatNumber(double value) {
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%.4f", value);
    }
}