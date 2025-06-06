package com.example.calculiverse;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AljabarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aljabar);

        // Mengatur ListView untuk sub-kategori aljabar
        ListView listView = findViewById(R.id.lv_aljabar);
        String[] subKategori = getResources().getStringArray(R.array.kategori_aljabar);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
            R.layout.item_list_aljabar,
            R.id.tv_item_aljabar,
            subKategori);
        listView.setAdapter(adapter);

        // Menambahkan click listener untuk setiap sub-kategori
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedSubKategori = subKategori[position];
            
            switch (selectedSubKategori) {
                case "Rata-rata":
                    Intent intent = new Intent(AljabarActivity.this, RataRataActivity.class);
                    startActivity(intent);
                    break;
                case "Perbandingan":
                    // TODO: Buat aktivitas untuk kalkulator Perbandingan
                    Toast.makeText(this, "Membuka kalkulator Perbandingan", Toast.LENGTH_SHORT).show();
                    break;
                case "Rasio":
                    // TODO: Buat aktivitas untuk kalkulator Rasio
                    Toast.makeText(this, "Membuka kalkulator Rasio", Toast.LENGTH_SHORT).show();
                    break;
                case "Pecahan":
                    // TODO: Buat aktivitas untuk kalkulator Pecahan
                    Toast.makeText(this, "Membuka kalkulator Pecahan", Toast.LENGTH_SHORT).show();
                    break;
            }
        });
    }
} 