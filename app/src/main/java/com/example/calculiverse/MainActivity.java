package com.example.calculiverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String[] aljabarList = {"Persentase", "Rata-rata", "Perbandingan", "Rasio"};
    private final String[] geometriList = {"Bentuk", "Volume"};
    private final String[] keuanganList = {"Bunga", "Investasi"};
    private final String[] kesehatanList = {"BMI", "Kalori"};
    private final String[] waktuList = {"Konversi Waktu", "Hitung Mundur"};
    private final String[] pengonversiSatuanList = {"Panjang", "Berat", "Suhu"};
    private KategoriAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        TextView tvLabelKategori = findViewById(R.id.tv_label_kategori);
        ListView listView = findViewById(R.id.lv_list_subkategori);

        // Data untuk setiap kategori
        ArrayList<Kategori> aljabarData = new ArrayList<>();
        aljabarData.add(new Kategori("Aljabar", "Pecahan"));
        aljabarData.add(new Kategori("Aljabar", "Rata-rata"));
        aljabarData.add(new Kategori("Aljabar", "Perbandingan"));
        aljabarData.add(new Kategori("Aljabar", "Rasio"));

        ArrayList<Kategori> geometriData = new ArrayList<>();
        geometriData.add(new Kategori("Geometri", "Bentuk"));
        geometriData.add(new Kategori("Geometri", "Volume"));

        ArrayList<Kategori> keuanganData = new ArrayList<>();
        keuanganData.add(new Kategori("Keuangan", "Bunga"));
        keuanganData.add(new Kategori("Keuangan", "Harga satuan"));

        ArrayList<Kategori> kesehatanData = new ArrayList<>();
        kesehatanData.add(new Kategori("Kesehatan", "BMI"));
        kesehatanData.add(new Kategori("Kesehatan", "Kalori"));

        ArrayList<Kategori> waktuData = new ArrayList<>();
        waktuData.add(new Kategori("Waktu", "Konversi Waktu"));
        waktuData.add(new Kategori("Waktu", "Hitung Mundur"));

        ArrayList<Kategori> pengonversiSatuanData = new ArrayList<>();
        pengonversiSatuanData.add(new Kategori("Pengonversi Satuan", "Panjang"));
        pengonversiSatuanData.add(new Kategori("Pengonversi Satuan", "Berat"));
        pengonversiSatuanData.add(new Kategori("Pengonversi Satuan", "Suhu"));

        KategoriAdapter kategoriAdapter = new KategoriAdapter(this);
        kategoriAdapter.setKategoris(aljabarData);
        listView.setAdapter(kategoriAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Kategori selected = (Kategori) kategoriAdapter.getItem(position);
                String selectedSubKategori = selected.getSubkategori();
                Intent intent = null;

                switch (selectedSubKategori) {
                    case "Pecahan":
                        intent = new Intent(MainActivity.this, PecahanActivity.class);
                        startActivity(intent);
                        break;
                    case "Rata-rata":
                        intent = new Intent(MainActivity.this, RataRataActivity.class);
                        startActivity(intent);
                        break;
                    case "Perbandingan":
                        intent = new Intent(MainActivity.this, PerbandinganActivity.class);
                        startActivity(intent);
                        break;
                    case "Rasio":
                        intent = new Intent(MainActivity.this, RasioActivity.class);
                        startActivity(intent);
                        break;
                    case "Bunga":
                        intent = new Intent(MainActivity.this, BungaActivity.class);
                        startActivity(intent);
                        break;
                    case "Harga satuan":
                            intent = new Intent (MainActivity.this, HargaSatuanActivity.class);
                            startActivity(intent);
                        break;

                }
            }
        });

        // OnClick untuk icon kategori di atas
        LinearLayout layoutAljabar = findViewById(R.id.layout_aljabar);
        LinearLayout layoutGeometri = findViewById(R.id.layout_geometri);
        LinearLayout layoutPengonversiSatuan = findViewById(R.id.layout_pengonversi_satuan);
        LinearLayout layoutKeuangan = findViewById(R.id.layout_keuangan);
        LinearLayout layoutKesehatan = findViewById(R.id.layout_kesehatan);
        LinearLayout layoutWaktu = findViewById(R.id.layout_waktu);

        layoutAljabar.setOnClickListener(v -> {
            tvLabelKategori.setText("Aljabar");
            kategoriAdapter.setKategoris(aljabarData);
            kategoriAdapter.notifyDataSetChanged();
        });
        layoutGeometri.setOnClickListener(v -> {
            tvLabelKategori.setText("Geometri");
            kategoriAdapter.setKategoris(geometriData);
            kategoriAdapter.notifyDataSetChanged();
        });
        layoutPengonversiSatuan.setOnClickListener(v -> {
            tvLabelKategori.setText("Pengonversi Satuan");
            kategoriAdapter.setKategoris(pengonversiSatuanData);
            kategoriAdapter.notifyDataSetChanged();
        });
        layoutKeuangan.setOnClickListener(v -> {
            tvLabelKategori.setText("Keuangan");
            kategoriAdapter.setKategoris(keuanganData);
            kategoriAdapter.notifyDataSetChanged();
        });
        layoutKesehatan.setOnClickListener(v -> {
            tvLabelKategori.setText("Kesehatan");
            kategoriAdapter.setKategoris(kesehatanData);
            kategoriAdapter.notifyDataSetChanged();
        });
        layoutWaktu.setOnClickListener(v -> {
            tvLabelKategori.setText("Waktu");
            kategoriAdapter.setKategoris(waktuData);
            kategoriAdapter.notifyDataSetChanged();
        });

        // Default: Aljabar
        tvLabelKategori.setText("Aljabar");
        kategoriAdapter.setKategoris(aljabarData);
        kategoriAdapter.notifyDataSetChanged();
    }
}

