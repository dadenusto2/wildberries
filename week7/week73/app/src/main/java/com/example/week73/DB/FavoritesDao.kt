package com.example.week73.DB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.week73.model.FavoritesUrl

/**
* интефейс для запросв к БД
*/
@Dao
interface FavoritesDao {
    /**
     * выборка по текущему subId
     */
    @Query("SELECT * FROM FavoritesUrl where subId = :sub_id")
    fun getFavorites(sub_id: String): MutableList<FavoritesUrl?>

    /**
     * выборка всех элементов
     */
    @Query("SELECT * FROM FavoritesUrl ")
    fun getAll(): MutableList<FavoritesUrl?>

    /**
     * вставка
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(favoritesUrl: FavoritesUrl?): Long?

    /**
     * удалить конкретный элемент из табицы
     */
    @Query("DELETE FROM FavoritesUrl")
    fun deleteAll()
}