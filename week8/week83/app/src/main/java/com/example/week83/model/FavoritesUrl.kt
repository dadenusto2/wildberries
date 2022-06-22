package com.example.week83.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Тип данных для хранения сслыкок на лайкнутых котов
 */
@Entity
data class FavoritesUrl(
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    @ColumnInfo
    var subId: String? = "",

    @ColumnInfo
    var url: String? = ""
) {
    /**
     * Сравнение элементов
     *
     * @param other - другой параметр
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FavoritesUrl) return false

        if (subId != other.subId) return false
        if (url != other.url) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (subId?.hashCode() ?: 0)
        result = 31 * result + (url?.hashCode() ?: 0)
        return result
    }
}