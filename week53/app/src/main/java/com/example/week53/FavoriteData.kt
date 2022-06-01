package com.example.week53

import kotlinx.serialization.Serializable

//класс для запроса оцененных котов
@Serializable
data class FavoriteData(
    val country_code: String,
    val created_at: String,
    val id: Int,
    val image_id: String,
    val sub_id: String,
    val value: Int
)