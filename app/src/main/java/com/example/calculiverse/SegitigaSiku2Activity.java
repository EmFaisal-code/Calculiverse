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

public class SegitigaSiku2Activity extends AppCompatActivity {
    private MaterialButton btnToggleForm1, btnToggleForm2;
    private EditText etInput1, etInput2;
    private TextView tv1, tv2, tv3, tv4, tv5, tvOutput1, tvOutput2, tvOutput3, tvOutput4, tvOutput5;
    private ImageButton clearButton;
    private List<String> options = Arrays.asList("Sisi Miring", "Sisi A", "Sisi B", "Sudut A", "Sudut B", "Luas", "Keliling");
    private int inputIndex1 = 0;
    private int inputIndex2 = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segitiga_siku2);

        btnToggleForm1 = findViewById(R.id.btnToggleForm);
        btnToggleForm2 = findViewById(R.id.btnToggleForm2);
        etInput1 = findViewById(R.id.et_input_1);
        etInput2 = findViewById(R.id.et_input_2);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tvOutput1 = findViewById(R.id.tv_output_1);
        tvOutput2 = findViewById(R.id.tv_output_2);
        tvOutput3 = findViewById(R.id.tv_output_3);
        tvOutput4 = findViewById(R.id.tv_output_4);
        tvOutput5 = findViewById(R.id.tv_output_5);
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
            boolean isFavorit = db.favoritDao().isFavorit("Segitiga siku-siku");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Datar", "Segitiga siku-siku");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Datar", "Segitiga siku-siku");
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
        int[] outIdx = new int[5];
        int idx = 0;
        for (int i = 0; i < options.size(); i++) {
            if (i != inputIndex1 && i != inputIndex2) {
                outIdx[idx++] = i;
            }
        }
        tv1.setText(options.get(outIdx[0]));
        tv2.setText(options.get(outIdx[1]));
        tv3.setText(options.get(outIdx[2]));
        tv4.setText(options.get(outIdx[3]));
        tv5.setText(options.get(outIdx[4]));
    }

    private void calculateAndShowResult() {
        String s1 = etInput1.getText().toString();
        String s2 = etInput2.getText().toString();
        if (TextUtils.isEmpty(s1) || TextUtils.isEmpty(s2)) {
            clearAllOutputs();
            return;
        }
        try {
            // Urutan: Sisi Miring, Sisi A, Sisi B, Sudut A, Sudut B, Luas, Keliling
            double[] values = new double[7];
            boolean valid = false;
            double v1 = Double.parseDouble(s1);
            double v2 = Double.parseDouble(s2);
            int i1 = inputIndex1, i2 = inputIndex2;
            // Kombinasi input
            if ((i1 == 0 && i2 == 1) || (i1 == 1 && i2 == 0)) { // Sisi Miring & Sisi A
                double c = (i1 == 0) ? v1 : v2;
                double a = (i1 == 1) ? v1 : v2;
                double b = Math.sqrt(c * c - a * a);
                double sudutA = Math.toDegrees(Math.asin(a / c));
                double sudutB = 90 - sudutA;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 0 && i2 == 2) || (i1 == 2 && i2 == 0)) { // Sisi Miring & Sisi B
                double c = (i1 == 0) ? v1 : v2;
                double b = (i1 == 2) ? v1 : v2;
                double a = Math.sqrt(c * c - b * b);
                double sudutB = Math.toDegrees(Math.asin(b / c));
                double sudutA = 90 - sudutB;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 1 && i2 == 2) || (i1 == 2 && i2 == 1)) { // Sisi A & Sisi B
                double a = (i1 == 1) ? v1 : v2;
                double b = (i1 == 2) ? v1 : v2;
                double c = Math.sqrt(a * a + b * b);
                double sudutA = Math.toDegrees(Math.asin(a / c));
                double sudutB = 90 - sudutA;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 0 && i2 == 3) || (i1 == 3 && i2 == 0)) { // Sisi Miring & Sudut A
                double c = (i1 == 0) ? v1 : v2;
                double sudutA = (i1 == 3) ? v1 : v2;
                double a = c * Math.sin(Math.toRadians(sudutA));
                double b = c * Math.cos(Math.toRadians(sudutA));
                double sudutB = 90 - sudutA;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 0 && i2 == 4) || (i1 == 4 && i2 == 0)) { // Sisi Miring & Sudut B
                double c = (i1 == 0) ? v1 : v2;
                double sudutB = (i1 == 4) ? v1 : v2;
                double b = c * Math.sin(Math.toRadians(sudutB));
                double a = c * Math.cos(Math.toRadians(sudutB));
                double sudutA = 90 - sudutB;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 1 && i2 == 3) || (i1 == 3 && i2 == 1)) { // Sisi A & Sudut A
                double a = (i1 == 1) ? v1 : v2;
                double sudutA = (i1 == 3) ? v1 : v2;
                double c = a / Math.sin(Math.toRadians(sudutA));
                double b = Math.sqrt(c * c - a * a);
                double sudutB = 90 - sudutA;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 2 && i2 == 4) || (i1 == 4 && i2 == 2)) { // Sisi B & Sudut B
                double b = (i1 == 2) ? v1 : v2;
                double sudutB = (i1 == 4) ? v1 : v2;
                double c = b / Math.sin(Math.toRadians(sudutB));
                double a = Math.sqrt(c * c - b * b);
                double sudutA = 90 - sudutB;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 1 && i2 == 4) || (i1 == 4 && i2 == 1)) { // Sisi A & Sudut B
                double a = (i1 == 1) ? v1 : v2;
                double sudutB = (i1 == 4) ? v1 : v2;
                double c = a / Math.cos(Math.toRadians(sudutB));
                double b = Math.sqrt(c * c - a * a);
                double sudutA = 90 - sudutB;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            } else if ((i1 == 2 && i2 == 3) || (i1 == 3 && i2 == 2)) { // Sisi B & Sudut A
                double b = (i1 == 2) ? v1 : v2;
                double sudutA = (i1 == 3) ? v1 : v2;
                double c = b / Math.cos(Math.toRadians(sudutA));
                double a = Math.sqrt(c * c - b * b);
                double sudutB = 90 - sudutA;
                double luas = 0.5 * a * b;
                double keliling = a + b + c;
                values[0] = c; values[1] = a; values[2] = b; values[3] = sudutA; values[4] = sudutB; values[5] = luas; values[6] = keliling;
                valid = true;
            }
            if (valid) {
                int[] outIdx = new int[5];
                int idx = 0;
                for (int i = 0; i < options.size(); i++) {
                    if (i != inputIndex1 && i != inputIndex2) {
                        outIdx[idx++] = i;
                    }
                }
                tvOutput1.setText(formatNumber(values[outIdx[0]]));
                tvOutput2.setText(formatNumber(values[outIdx[1]]));
                tvOutput3.setText(formatNumber(values[outIdx[2]]));
                tvOutput4.setText(formatNumber(values[outIdx[3]]));
                tvOutput5.setText(formatNumber(values[outIdx[4]]));
            } else {
                clearAllOutputs();
            }
        } catch (Exception e) {
            clearAllOutputs();
        }
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
        tvOutput4.setText("--");
        tvOutput5.setText("--");
    }
    private String formatNumber(double value) {
        if (Double.isNaN(value) || Double.isInfinite(value)) return "--";
        if (value == (long) value)
            return String.format("%d", (long) value);
        else
            return String.format("%.4f", value);
    }
}