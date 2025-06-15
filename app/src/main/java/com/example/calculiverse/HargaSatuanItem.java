package com.example.calculiverse;

public class HargaSatuanItem {
    private long id;
    private String kuantitas;
    private String harga;
    private String hasilPerKemasan;

    public HargaSatuanItem(){
        this.id = System.currentTimeMillis();
        this.kuantitas = "";
        this.harga = "";
        this.hasilPerKemasan = "";
    }
    public HargaSatuanItem(String kuantitas, String harga){
        this.id = System.currentTimeMillis();
        this.kuantitas = kuantitas;
        this.harga = harga;
        this.hasilPerKemasan = "";
    }
    // Buat getter dan setter untuk semua field
    public long getId() {
        return id;
    }

    public String getKuantitas() {
        return kuantitas;
    }

    public void setKuantitas(String kuantitas) {
        this.kuantitas = kuantitas;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getHasilPerKemasan() {
        return hasilPerKemasan; // <-- Perbaikan di sini
    }

    public void setHasilPerKemasan(String hasilPerKemasan) { // <-- Tambahkan setter ini
        this.hasilPerKemasan = hasilPerKemasan;
    }
}
