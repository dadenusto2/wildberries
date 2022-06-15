package com.example.week73;

import androidx.room.Database;
import androidx.room.RoomDatabase;

// bump version number if your schema changes
@Database(entities = {FavoritesUrl.class}, version = 2)
public abstract class MyDatabase extends RoomDatabase {
    // Declare your data access objects as abstract
    public abstract FavoriteRepository.UserDao userDao();

    // Database name to be used
    public static final String NAME = "MyDataBase";
}