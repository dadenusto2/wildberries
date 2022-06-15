package com.example.week73

import kotlinx.serialization.Serializable

//класс для запроса оцененных котов
@Serializable
class FavoriteData() {
    var id: Int = 0

    var country_code: String = ""

    var created_at: String = ""

    var image_id: String = ""

    var sub_id: String = ""

    var value: Int = 0
}