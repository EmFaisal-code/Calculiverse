package com.example.calculiverse;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FavoritDao {
    @Insert
    void insert(Favorit favorit);

    @Delete
    void delete(Favorit favorit);

    @Query("SELECT * FROM favorit")
    List<Favorit> getAllFavorit();

    @Query("SELECT EXISTS(SELECT * FROM favorit WHERE subkategori = :subkategori)")
    boolean isFavorit(String subkategori);

    @Query("DELETE FROM favorit WHERE kategori = :kategori AND subkategori = :subkategori")
    void deleteByKategoriAndSubkategori(String kategori, String subkategori);
}
