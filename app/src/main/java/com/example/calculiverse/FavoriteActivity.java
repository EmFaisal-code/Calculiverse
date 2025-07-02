package com.example.calculiverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.room.Room;

import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

    private AppDatabase db;
    private LinearLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_favorite);

        db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        contentLayout = findViewById(R.id.content_layout);
        loadFavorites();

        BottomNavigationView bottomNav = findViewById(R.id.bot_nav);
        bottomNav.setSelectedItemId(R.id.navigation_favorite); // Aktifkan item kalkulator

        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.navigation_home) {
                startActivity(new Intent(this, MainActivity.class));
                return true;
            } else if (id == R.id.navigation_calculator) {
                startActivity(new Intent(this, KalkulatorActivity.class));
                return true;
            } else if (id == R.id.navigation_favorite) {
                return true;
            }
            return false;
        });
    }

    private void loadFavorites() {
        contentLayout.removeAllViews();
        List<Favorit> favoritList = db.favoritDao().getAllFavorit();
        if (favoritList.isEmpty()) {
            TextView emptyView = new TextView(this);
            emptyView.setText("Belum ada favorit");
            emptyView.setTextColor(0xFFFFFFFF);
            emptyView.setTextSize(18);
            contentLayout.addView(emptyView);
            return;
        }
        for (Favorit favorit : favoritList) {
            LinearLayout itemLayout = new LinearLayout(this);
            itemLayout.setOrientation(LinearLayout.HORIZONTAL);
            itemLayout.setPadding(24, 24, 24, 24);
            itemLayout.setBackgroundColor(0xFF232323);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 16);
            itemLayout.setLayoutParams(params);

            TextView tv = new TextView(this);
            tv.setText(favorit.kategori + " - " + favorit.subkategori);
            tv.setTextColor(0xFFFFFFFF);
            tv.setTextSize(18);
            tv.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));

            ImageButton btnDelete = new ImageButton(this);
            btnDelete.setImageResource(R.drawable.ic_delete);
            btnDelete.setBackgroundColor(0x00000000);
            btnDelete.setOnClickListener(v -> {
                db.favoritDao().delete(favorit);
                Toast.makeText(this, "Favorit dihapus", Toast.LENGTH_SHORT).show();
                loadFavorites();
            });
            LinearLayout.LayoutParams btnParams = new LinearLayout.LayoutParams(80, 80);
            btnDelete.setLayoutParams(btnParams);

            itemLayout.addView(tv);
            itemLayout.addView(btnDelete);
            contentLayout.addView(itemLayout);

            itemLayout.setOnClickListener(v -> {
                Intent intent = getIntentForFavorit(favorit);
                if (intent != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "Belum ada halaman untuk " + favorit.subkategori, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private Intent getIntentForFavorit(Favorit favorit) {
        switch (favorit.kategori) {
            case "Aljabar":
                switch (favorit.subkategori) {
                    case "Pecahan": return new Intent(this, PecahanActivity.class);
                    case "Rata-rata": return new Intent(this, RataRataActivity.class);
                    case "Perbandingan": return new Intent(this, PerbandinganActivity.class);
                    case "Rasio": return new Intent(this, RasioActivity.class);
                }
                break;
            case "Pengonversi satuan":
                switch (favorit.subkategori) {
                    case "Panjang": return new Intent(this, PanjangActivity.class);
                    case "Suhu": return new Intent(this, SuhuActivity.class);
                    case "Berat": return new Intent(this, LemakTubuhActivity.class); // Ganti ke BeratActivity jika ada
                }
                break;
            case "Keuangan":
                switch (favorit.subkategori) {
                    case "Bunga": return new Intent(this, BungaActivity.class);
                    case "Harga satuan": return new Intent(this, HargaSatuanActivity.class);
                }
                break;
            case "Kesehatan":
                switch (favorit.subkategori) {
                    case "Indeks massa tubuh": return new Intent(this, IndeksMassaTubuhActivity.class);
                    case "Lemak tubuh": return new Intent(this, LemakTubuhActivity.class);
                }
                break;
            case "Waktu":
                switch (favorit.subkategori) {
                    case "Interval waktu": return new Intent(this, IntervalWaktuActivity.class);
                    case "Kalkulator usia": return new Intent(this, KalkulatorUsiaActivity.class);
                }
                break;
            case "Geometri":
                if ("Datar".equals(favorit.subkategori)) {
                    return new Intent(this, DatarActivity.class);
                } else if ("Ruang".equals(favorit.subkategori)) {
                    return new Intent(this, RuangActivity.class);
                }
                break;
            case "Datar":
                switch (favorit.subkategori) {
                    case "Segitiga": return new Intent(this, SegitigaActivity.class);
                    case "Segitiga siku-siku": return new Intent(this, SegitigaSiku2Activity.class); // Ganti jika ada activity khusus
                    case "Bujur eangkar": return new Intent(this, BujurSangkarActivity.class);
                    case "Segiempat": return new Intent(this, SegiEmpatActivity.class); // Ganti jika ada activity khusus
                    case "Lingkaran": return new Intent(this, LingkaranActivity.class); // Ganti jika ada activity khusus
                }
                break;
            case "Ruang":
                switch (favorit.subkategori) {
                    case "Kubus": return new Intent(this, KubusActivity.class); // Ganti jika ada activity khusus
                    case "Prisma": return new Intent(this, PrismaActivity.class);
                    case "Tabung": return new Intent(this, TabungActivity.class); // Ganti jika ada activity khusus
                    case "Bola": return new Intent(this, BolaActivity.class); // Ganti jika ada activity khusus
                }
                break;
        }
        return null;
    }
}