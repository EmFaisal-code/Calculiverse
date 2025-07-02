package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class PanjangActivity extends AppCompatActivity {
    private EditText etNm, etMm, etCm, etM, etKm, etLy, etAu, etPc, etFt, etIn, etMi, etYd;
    private TextView tvPerNm, tvPerMm, tvPerCm, tvPerM, tvPerKm, tvPerLy, tvPerAu, tvPerPc, tvPerFt, tvPerIn, tvPerMi, tvPerYd;
    private boolean isEditing = false;

    // Konstanta konversi ke meter
    private static final double NM_TO_M = 1e-9;
    private static final double MM_TO_M = 1e-3;
    private static final double CM_TO_M = 1e-2;
    private static final double KM_TO_M = 1e3;
    private static final double LY_TO_M = 9.4607304725808e15;
    private static final double AU_TO_M = 1.495978707e11;
    private static final double PC_TO_M = 3.08567758149137e16;
    private static final double FT_TO_M = 0.3048;
    private static final double IN_TO_M = 0.0254;
    private static final double MI_TO_M = 1609.344;
    private static final double YD_TO_M = 0.9144;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_panjang);

        etNm = findViewById(R.id.et_output_nm);
        etMm = findViewById(R.id.et_output_mm);
        etCm = findViewById(R.id.et_output_cm);
        etM = findViewById(R.id.et_output_m);
        etKm = findViewById(R.id.et_output_km);
        etLy = findViewById(R.id.et_output_ly);
        etAu = findViewById(R.id.et_output_au);
        etPc = findViewById(R.id.et_output_pc);
        etFt = findViewById(R.id.et_output_ft);
        etIn = findViewById(R.id.et_output_in);
        etMi = findViewById(R.id.et_output_mi);
        etYd = findViewById(R.id.et_output_yd);
        tvPerNm = findViewById(R.id.tv_per_nm);
        tvPerMm = findViewById(R.id.tv_per_mm);
        tvPerCm = findViewById(R.id.tv_per_cm);
        tvPerM = findViewById(R.id.tv_per_m);
        tvPerKm = findViewById(R.id.tv_per_km);
        tvPerLy = findViewById(R.id.tv_per_ly);
        tvPerAu = findViewById(R.id.tv_per_au);
        tvPerPc = findViewById(R.id.tv_per_pc);
        tvPerFt = findViewById(R.id.tv_per_ft);
        tvPerIn = findViewById(R.id.tv_per_in);
        tvPerMi = findViewById(R.id.tv_per_mi);
        tvPerYd = findViewById(R.id.tv_per_yd);
        ImageButton clearButton = findViewById(R.id.clear_button);

        EditText[] etArr = {etNm, etMm, etCm, etM, etKm, etLy, etAu, etPc, etFt, etIn, etMi, etYd};
        TextView[] tvArr = {tvPerNm, tvPerMm, tvPerCm, tvPerM, tvPerKm, tvPerLy, tvPerAu, tvPerPc, tvPerFt, tvPerIn, tvPerMi, tvPerYd};
        String[] satuan = {"nm", "mm", "cm", "m", "km", "ly", "au", "pc", "ft", "in", "mi", "yd"};
        double[] toMeter = {NM_TO_M, MM_TO_M, CM_TO_M, 1.0, KM_TO_M, LY_TO_M, AU_TO_M, PC_TO_M, FT_TO_M, IN_TO_M, MI_TO_M, YD_TO_M};

        // Fungsi update label per
        Runnable updatePerLabels = () -> {
            for (int i = 0; i < etArr.length; i++) {
                if (etArr[i].isFocused()) {
                    tvArr[i].setText("1 " + satuan[i]);
                    for (int j = 0; j < etArr.length; j++) {
                        if (i != j) {
                            double val = toMeter[j] / toMeter[i];
                            tvArr[j].setText(String.format("1 %s = %s %s", satuan[j], formatNumber(val), satuan[i]));
                        }
                    }
                }
            }
        };

        // Helper format
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.##########E0");
        java.text.DecimalFormat dfShort = new java.text.DecimalFormat("0.########");
        java.text.DecimalFormat dfInt = new java.text.DecimalFormat("0");
        java.util.function.Function<Double, String> formatNumber = val -> {
            if (val >= 1e7 || val <= 1e-3 && val != 0) return df.format(val);
            if (val == Math.floor(val)) return dfInt.format(val);
            return dfShort.format(val);
        };

        // TextWatcher untuk masing-masing EditText
        for (int i = 0; i < etArr.length; i++) {
            final int idx = i;
            etArr[i].addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override public void afterTextChanged(Editable s) {
                    if (etArr[idx].isFocused() && !isEditing) {
                        isEditing = true;
                        String input = s.toString();
                        if (!input.isEmpty()) {
                            try {
                                double val = Double.parseDouble(input);
                                double meter = val * toMeter[idx];
                                for (int j = 0; j < etArr.length; j++) {
                                    if (j != idx) {
                                        double other = meter / toMeter[j];
                                        etArr[j].setText(formatNumber.apply(other));
                                    }
                                }
                            } catch (NumberFormatException e) {
                                for (int j = 0; j < etArr.length; j++) if (j != idx) etArr[j].setText("");
                            }
                        } else {
                            for (int j = 0; j < etArr.length; j++) if (j != idx) etArr[j].setText("");
                        }
                        updatePerLabels.run();
                        isEditing = false;
                    }
                }
            });
            etArr[i].setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        }

        // Tombol clear
        clearButton.setOnClickListener(v -> {
            for (EditText et : etArr) et.setText("");
            for (int i = 0; i < tvArr.length; i++) tvArr[i].setText("1 " + satuan[i]);
        });
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Panjang");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Pengonversi satuan", "Panjang");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Pengonversi satuan", "Panjang");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String formatNumber(double val) {
        java.text.DecimalFormat df = new java.text.DecimalFormat("0.##########E0");
        java.text.DecimalFormat dfShort = new java.text.DecimalFormat("0.########");
        java.text.DecimalFormat dfInt = new java.text.DecimalFormat("0");
        if (val >= 1e7 || (val <= 1e-3 && val != 0)) return df.format(val);
        if (val == Math.floor(val)) return dfInt.format(val);
        return dfShort.format(val);
    }
}