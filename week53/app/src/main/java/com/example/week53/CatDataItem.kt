package com.example.week53

import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//класс дданных фото кота
@Serializable
data class CatDataItem(
    val breeds: List< Breads>? = null,
    val categories: List<Categories>? = null,
    val height: Int? = null,
    val id: String? = null,
    val url: String? = null,
    val width: Int? = null
)

@Serializable
data class Categories(
    val id: @Contextual Double? = null,
    val name: String? = null
)

@Serializable
data class Breads(
    val adaptability: Int? = null,
    val affection_level: Int? = null,
    val alt_names: String? = null,
    val cfa_url: String? = null,
    val child_friendly: Int? = null,
    val cat_friendly: Int? = null,
    val country_code: String? = null,
    val country_codes: String? = null,
    val description: String? = null,
    val dog_friendly: Int? = null,
    val energy_level: Int? = null,
    val experimental: Int? = null,
    val grooming: Int? = null,
    val hairless: Int? = null,
    val health_issues: Int? = null,
    val hypoallergenic: Int? = null,
    val id: String? = null,
    val indoor: Int? = null,
    val intelligence: Int? = null,
    val lap: Int? = null,
    val life_span: String? = null,
    val name: String? = null,
    val natural: Int? = null,
    val origin: String? = null,
    val rare: Int? = null,
    val reference_image_id: String? = null,
    val rex: Int? = null,
    val shedding_level: Int? = null,
    val short_legs: Int? = null,
    val social_needs: Int? = null,
    val stranger_friendly: Int? = null,
    val suppressed_tail: Int? = null,
    val temperament: String? = null,
    val vcahospitals_url: String? = null,
    val vetstreet_url: String? = null,
    val vocalisation: Int? = null,
    val weight: Weight? = null,
    val wikipedia_url: String
)

@Serializable
data class Weight(
    val imperial: String? = null,
    val metric: String? = null
)
