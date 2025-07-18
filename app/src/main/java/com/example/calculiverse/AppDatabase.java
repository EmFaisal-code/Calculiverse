package com.example.calculiverse;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Favorit.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract FavoritDao favoritDao();
}
