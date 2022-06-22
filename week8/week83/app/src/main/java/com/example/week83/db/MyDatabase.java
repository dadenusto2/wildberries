package com.example.week83.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.week83.db.FavoriteDao;
import com.example.week83.model.FavoritesUrl;

// bump version number if your schema changes
@Database(entities = {FavoritesUrl.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    // Declare your data access objects as abstract
    public abstract FavoriteDao favoriteDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}