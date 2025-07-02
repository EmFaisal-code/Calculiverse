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

public class SegitigaActivity extends AppCompatActivity {
    private EditText etA, etB, etC;
    private TextView tvLuas, tvPerimeter, tvAB, tvBC, tvAC, tvTA, tvTB, tvTC;
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_segitiga);

        etA = findViewById(R.id.et_input_a);
        etB = findViewById(R.id.et_input_b);
        etC = findViewById(R.id.et_input_c);
        tvLuas = findViewById(R.id.tv_luas);
        tvPerimeter = findViewById(R.id.tv_perimeter);
        tvAB = findViewById(R.id.tv_ab);
        tvBC = findViewById(R.id.tv_bc);
        tvAC = findViewById(R.id.tv_ac);
        tvTA = findViewById(R.id.tv_t_a);
        tvTB = findViewById(R.id.tv_t_b);
        tvTC = findViewById(R.id.tv_t_c);
        clearButton = findViewById(R.id.clear_button);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                hitungSegitiga();
            }
        };
        etA.addTextChangedListener(watcher);
        etB.addTextChangedListener(watcher);
        etC.addTextChangedListener(watcher);

        clearButton.setOnClickListener(v -> {
            etA.setText("");
            etB.setText("");
            etC.setText("");
            tvLuas.setText("-");
            tvPerimeter.setText("-");
            tvAB.setText("-");
            tvBC.setText("-");
            tvAC.setText("-");
            tvTA.setText("-");
            tvTB.setText("-");
            tvTC.setText("-");
        });
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Segitiga");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Datar", "Segitiga");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Datar", "Segitiga");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitungSegitiga() {
        String sa = etA.getText().toString();
        String sb = etB.getText().toString();
        String sc = etC.getText().toString();
        if (sa.isEmpty() || sb.isEmpty() || sc.isEmpty()) {
            tvLuas.setText("-");
            tvPerimeter.setText("-");
            tvAB.setText("-");
            tvBC.setText("-");
            tvAC.setText("-");
            tvTA.setText("-");
            tvTB.setText("-");
            tvTC.setText("-");
            return;
        }
        try {
            double a = Double.parseDouble(sa);
            double b = Double.parseDouble(sb);
            double c = Double.parseDouble(sc);
            if (a <= 0 || b <= 0 || c <= 0 || a + b <= c || a + c <= b || b + c <= a) {
                // Bukan segitiga valid
                tvLuas.setText("-");
                tvPerimeter.setText("-");
                tvAB.setText("-");
                tvBC.setText("-");
                tvAC.setText("-");
                tvTA.setText("-");
                tvTB.setText("-");
                tvTC.setText("-");
                return;
            }
            double keliling = a + b + c;
            double s = keliling / 2.0;
            double luas = Math.sqrt(s * (s - a) * (s - b) * (s - c));
            // Sudut (dalam derajat)
            double sudutA = Math.toDegrees(Math.acos((b*b + c*c - a*a) / (2*b*c)));
            double sudutB = Math.toDegrees(Math.acos((a*a + c*c - b*b) / (2*a*c)));
            double sudutC = Math.toDegrees(Math.acos((a*a + b*b - c*c) / (2*a*b)));
            // Tinggi
            double tA = 2 * luas / a;
            double tB = 2 * luas / b;
            double tC = 2 * luas / c;
            // Set hasil
            tvLuas.setText(String.format("%.4f", luas));
            tvPerimeter.setText(String.format("%.4f", keliling));
            tvAB.setText(String.format("%.2f°", sudutA));
            tvBC.setText(String.format("%.2f°", sudutB));
            tvAC.setText(String.format("%.2f°", sudutC));
            tvTA.setText(String.format("%.4f", tA));
            tvTB.setText(String.format("%.4f", tB));
            tvTC.setText(String.format("%.4f", tC));
        } catch (Exception e) {
            tvLuas.setText("-");
            tvPerimeter.setText("-");
            tvAB.setText("-");
            tvBC.setText("-");
            tvAC.setText("-");
            tvTA.setText("-");
            tvTB.setText("-");
            tvTC.setText("-");
        }
    }
}