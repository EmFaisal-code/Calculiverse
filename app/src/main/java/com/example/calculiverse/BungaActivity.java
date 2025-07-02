package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

import java.text.DecimalFormat;

public class BungaActivity extends AppCompatActivity {

    LinearLayout layoutBungaSederhana, layoutBungaMajemuk;
    Button btnToggleForm, btnPeriode, btnPeriodeMajemuk, btnMajemuk;
    EditText etDeposit, etKurs, etPeriode;
    EditText etDepositMajemuk, etKursMajemuk, etPeriodeMajemuk;
    TextView tvNilaiAkhir, tvBunga;
    boolean isBungaSederhana = true;
    boolean isBulan = true;
    boolean isBulanMajemuk = true;
    private int statusMajemukSaatIni = 0;
    private final String[] statusMajemukArray = {
            "Harian", "Mingguan", "Bulanan", "Triwulan", "Tahunan"
    };
    private final int[] frekuensiMajemuk = {
            365, 52, 12, 4, 1
    };

    // Variabel untuk menyimpan nilai sementara
    private String tempDeposit = "";
    private String tempKurs = "";
    private String tempPeriode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_bunga);

        // Inisialisasi view
        layoutBungaSederhana = findViewById(R.id.layoutBungaSederhana);
        layoutBungaMajemuk = findViewById(R.id.layoutBungaMajemuk);
        btnToggleForm = findViewById(R.id.btnToggleForm);
        btnPeriode = findViewById(R.id.btnPeriode);
        btnPeriodeMajemuk = findViewById(R.id.btnPeriodeMajemuk);
        btnMajemuk = findViewById(R.id.btnMajemuk);
        
        // EditText untuk bunga sederhana
        etDeposit = findViewById(R.id.et_input_deposit);
        etKurs = findViewById(R.id.et_input_kurs);
        etPeriode = findViewById(R.id.et_input_periode);
        
        // EditText untuk bunga majemuk
        etDepositMajemuk = findViewById(R.id.et_input_deposit_majemuk);
        etKursMajemuk = findViewById(R.id.et_input_kurs_majemuk);
        etPeriodeMajemuk = findViewById(R.id.et_input_periode_majemuk);
        
        tvNilaiAkhir = findViewById(R.id.tv_output_nilai_akhir);
        tvBunga = findViewById(R.id.tv_output_bunga);
        ImageButton clearButton = findViewById(R.id.clear_button);

        // Tambahkan TextWatcher untuk setiap EditText
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                hitungBunga();
            }
        };

        // Setup clear button
        clearButton.setOnClickListener(v -> {
            clearAllFields();
        });

        // Tambahkan TextWatcher untuk EditText bunga sederhana
        etDeposit.addTextChangedListener(textWatcher);
        etKurs.addTextChangedListener(textWatcher);
        etPeriode.addTextChangedListener(textWatcher);

        // Tambahkan TextWatcher untuk EditText bunga majemuk
        etDepositMajemuk.addTextChangedListener(textWatcher);
        etKursMajemuk.addTextChangedListener(textWatcher);
        etPeriodeMajemuk.addTextChangedListener(textWatcher);

        btnToggleForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBungaSederhana) {
                    // Simpan nilai dari form bunga sederhana
                    tempDeposit = etDeposit.getText().toString();
                    tempKurs = etKurs.getText().toString();
                    tempPeriode = etPeriode.getText().toString();
                    
                    // Set nilai ke form bunga majemuk
                    etDepositMajemuk.setText(tempDeposit);
                    etKursMajemuk.setText(tempKurs);
                    etPeriodeMajemuk.setText(tempPeriode);
                    
                    layoutBungaSederhana.setVisibility(View.GONE);
                    layoutBungaMajemuk.setVisibility(View.VISIBLE);
                    btnToggleForm.setText("Bunga Majemuk");
                    btnPeriodeMajemuk.setText("Bulan");
                    btnMajemuk.setText(statusMajemukArray[0]);
                    isBulanMajemuk = true;
                    statusMajemukSaatIni = 0;
                } else {
                    // Simpan nilai dari form bunga majemuk
                    tempDeposit = etDepositMajemuk.getText().toString();
                    tempKurs = etKursMajemuk.getText().toString();
                    tempPeriode = etPeriodeMajemuk.getText().toString();
                    
                    // Set nilai ke form bunga sederhana
                    etDeposit.setText(tempDeposit);
                    etKurs.setText(tempKurs);
                    etPeriode.setText(tempPeriode);
                    
                    layoutBungaSederhana.setVisibility(View.VISIBLE);
                    layoutBungaMajemuk.setVisibility(View.GONE);
                    btnToggleForm.setText("Bunga Sederhana");
                    btnPeriode.setText("Bulan");
                    isBulan = true;
                }
                isBungaSederhana = !isBungaSederhana;
                hitungBunga();
            }
        });

        btnPeriode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBulan = !isBulan;
                if (isBulan) {
                    btnPeriode.setText("Bulan");
                } else {
                    btnPeriode.setText("Tahun");
                }
                hitungBunga();
            }
        });

        btnPeriodeMajemuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBulanMajemuk = !isBulanMajemuk;
                if (isBulanMajemuk) {
                    btnPeriodeMajemuk.setText("Bulan");
                } else {
                    btnPeriodeMajemuk.setText("Tahun");
                }
                hitungBunga();
            }
        });

        btnMajemuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                statusMajemukSaatIni = (statusMajemukSaatIni + 1) % statusMajemukArray.length;
                btnMajemuk.setText(statusMajemukArray[statusMajemukSaatIni]);
                hitungBunga();
            }
        });
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Bunga");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Keuangan", "Bunga");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Keuangan", "Bunga");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAllFields() {
        etDeposit.setText("");
        etKurs.setText("");
        etPeriode.setText("");
        etDepositMajemuk.setText("");
        etKursMajemuk.setText("");
        etPeriodeMajemuk.setText("");
        tempDeposit = "";
        tempKurs = "";
        tempPeriode = "";
        tvNilaiAkhir.setText("- -");
        tvBunga.setText("- -");
    }

    private void hitungBunga() {
        try {
            String depositStr, kursStr, periodeStr;

            if (isBungaSederhana) {
                depositStr = etDeposit.getText().toString();
                kursStr = etKurs.getText().toString();
                periodeStr = etPeriode.getText().toString();
            } else {
                depositStr = etDepositMajemuk.getText().toString();
                kursStr = etKursMajemuk.getText().toString();
                periodeStr = etPeriodeMajemuk.getText().toString();
            }

            if (depositStr.isEmpty() || kursStr.isEmpty() || periodeStr.isEmpty()) {
                tvNilaiAkhir.setText("- -");
                tvBunga.setText("- -");
                return;
            }

            double deposit = Double.parseDouble(depositStr);
            double kurs = Double.parseDouble(kursStr);
            double periode = Double.parseDouble(periodeStr);

            double nilaiAkhir, bunga;

            if (isBungaSederhana) {
                // Perhitungan Bunga Sederhana
                if (isBulan) {
                    // Konversi periode bulan ke tahun
                    periode = periode / 12;
                }
                bunga = deposit * (kurs / 100) * periode;
                nilaiAkhir = deposit + bunga;
            } else {
                // Perhitungan Bunga Majemuk
                int frekuensi = frekuensiMajemuk[statusMajemukSaatIni];
                if (isBulanMajemuk) {
                    // Konversi periode bulan ke tahun
                    periode = periode / 12;
                }
                nilaiAkhir = deposit * Math.pow(1 + (kurs / 100) / frekuensi, frekuensi * periode);
                bunga = nilaiAkhir - deposit;
            }

            // Format hasil dengan 2 angka desimal
            DecimalFormat df = new DecimalFormat("#,##0.00");
            tvNilaiAkhir.setText(df.format(nilaiAkhir));
            tvBunga.setText(df.format(bunga));

        } catch (NumberFormatException e) {
            tvNilaiAkhir.setText("- -");
            tvBunga.setText("- -");
            Toast.makeText(this, "Mohon masukkan angka yang valid", Toast.LENGTH_SHORT).show();
        }
    }
}