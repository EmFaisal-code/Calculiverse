package com.example.calculiverse;

public class Kategori {
    private String kategori;
    private String subkategori;

    public Kategori(String kategori, String subkategori) {
        this.kategori = kategori;
        this.subkategori = subkategori;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getSubkategori() {
        return subkategori;
    }

    public void setSubkategori(String subkategori) {
        this.subkategori = subkategori;
    }
} 