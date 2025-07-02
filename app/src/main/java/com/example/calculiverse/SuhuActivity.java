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

public class SuhuActivity extends AppCompatActivity {
    private EditText etCelcius, etFahrenheit, etKelvin;
    private TextView tvPerCelcius, tvPerFahrenheit, tvPerKelvin;
    private boolean isEditing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_suhu);

        etCelcius = findViewById(R.id.et_output_celcius);
        etFahrenheit = findViewById(R.id.et_output_fahrenheit);
        etKelvin = findViewById(R.id.et_output_kelvin);
        ImageButton clearButton = findViewById(R.id.clear_button);
        tvPerCelcius = findViewById(R.id.tv_per_celcius);
        tvPerFahrenheit = findViewById(R.id.tv_per_fahrenheit);
        tvPerKelvin = findViewById(R.id.tv_per_kelvin);

        // Fungsi untuk update label konversi per satuan
        Runnable updatePerLabels = () -> {
            if (etCelcius.isFocused()) {
                // Input Celcius aktif
                tvPerCelcius.setText("1 °C");
                // 1 °F ke °C
                double cFromF = (1 - 32) * 5.0 / 9.0;
                tvPerFahrenheit.setText(String.format("1 °F = %.2f °C", cFromF));
                // 1 K ke °C
                double cFromK = 1 - 273.15;
                tvPerKelvin.setText(String.format("1 K = %.2f °C", cFromK));
            } else if (etFahrenheit.isFocused()) {
                // Input Fahrenheit aktif
                // 1 °C ke °F
                double fFromC = 1 * 9.0 / 5.0 + 32;
                tvPerCelcius.setText(String.format("1 °C = %.2f °F", fFromC));
                tvPerFahrenheit.setText("1 °F");
                // 1 K ke °F
                double fFromK = (1 - 273.15) * 9.0 / 5.0 + 32;
                tvPerKelvin.setText(String.format("1 K = %.2f °F", fFromK));
            } else if (etKelvin.isFocused()) {
                // Input Kelvin aktif
                // 1 °C ke K
                double kFromC = 1 + 273.15;
                tvPerCelcius.setText(String.format("1 °C = %.2f K", kFromC));
                // 1 °F ke K
                double kFromF = (1 - 32) * 5.0 / 9.0 + 273.15;
                tvPerFahrenheit.setText(String.format("1 °F = %.2f K", kFromF));
                tvPerKelvin.setText("1 K");
            } else {
                // Default (tidak ada yang fokus)
                tvPerCelcius.setText("1 °C");
                tvPerFahrenheit.setText("1 °F");
                tvPerKelvin.setText("1 K");
            }
        };

        // TextWatcher untuk Celcius
        etCelcius.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (etCelcius.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double c = Double.parseDouble(input);
                            double f = c * 9.0 / 5.0 + 32;
                            double k = c + 273.15;
                            etFahrenheit.setText(String.format("%.2f", f));
                            etKelvin.setText(String.format("%.2f", k));
                        } catch (NumberFormatException e) {
                            etFahrenheit.setText("");
                            etKelvin.setText("");
                        }
                    } else {
                        etFahrenheit.setText("");
                        etKelvin.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });

        // TextWatcher untuk Fahrenheit
        etFahrenheit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (etFahrenheit.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double f = Double.parseDouble(input);
                            double c = (f - 32) * 5.0 / 9.0;
                            double k = (f - 32) * 5.0 / 9.0 + 273.15;
                            etCelcius.setText(String.format("%.2f", c));
                            etKelvin.setText(String.format("%.2f", k));
                        } catch (NumberFormatException e) {
                            etCelcius.setText("");
                            etKelvin.setText("");
                        }
                    } else {
                        etCelcius.setText("");
                        etKelvin.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });

        // TextWatcher untuk Kelvin
        etKelvin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (etKelvin.isFocused() && !isEditing) {
                    isEditing = true;
                    String input = s.toString();
                    if (!input.isEmpty()) {
                        try {
                            double k = Double.parseDouble(input);
                            double c = k - 273.15;
                            double f = (k - 273.15) * 9.0 / 5.0 + 32;
                            etCelcius.setText(String.format("%.2f", c));
                            etFahrenheit.setText(String.format("%.2f", f));
                        } catch (NumberFormatException e) {
                            etCelcius.setText("");
                            etFahrenheit.setText("");
                        }
                    } else {
                        etCelcius.setText("");
                        etFahrenheit.setText("");
                    }
                    updatePerLabels.run();
                    isEditing = false;
                }
            }
        });

        // Tombol clear
        clearButton.setOnClickListener(v -> {
            etCelcius.setText("");
            etFahrenheit.setText("");
            etKelvin.setText("");
            tvPerCelcius.setText("1 °C");
            tvPerFahrenheit.setText("1 °F");
            tvPerKelvin.setText("1 K");
        });

        etCelcius.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etFahrenheit.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());
        etKelvin.setOnFocusChangeListener((v, hasFocus) -> updatePerLabels.run());

        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Suhu");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Pengonversi satuan", "Suhu");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Pengonversi satuan", "Suhu");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }
}