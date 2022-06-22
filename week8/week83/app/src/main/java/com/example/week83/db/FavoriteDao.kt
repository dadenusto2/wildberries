package com.example.week83.db

import androidx.room.*
import com.example.week83.model.FavoritesUrl

@Dao
interface FavoriteDao {
    /**
     * выборка по текущему subId
     */
    @Query("SELECT * FROM FavoritesUrl")
    fun getFavorites(): MutableList<FavoritesUrl?>

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
     * удалить все элементы из табицы
     */
    @Delete
    fun deleteFaVorite(favoritesUrl: FavoritesUrl?)
    /**
     * удалить конкретный элемент из табицы
     */
    @Query("DELETE FROM FavoritesUrl")
    fun deleteAll()
}