package com.example.calculiverse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Favorit {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String kategori;
    public String subkategori;

    public Favorit(String kategori, String subkategori) {
        this.kategori = kategori;
        this.subkategori = subkategori;
    }
}
